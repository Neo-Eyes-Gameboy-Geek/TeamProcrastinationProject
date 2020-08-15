package library.borrowbook;

import java.util.ArrayList;
import java.util.List;

import library.entities.Book;
import library.entities.Library;
import library.entities.Loan;
import library.entities.Member;

public class BorrowBookControl {

    private BorrowBookUI ui;

    private Library library;
    private Member member;

    private enum ControlState {
        INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED
    };
    private ControlState state;

    private List<Book> pendingList;
    private List<Loan> compleatedList;
    private Book book;

    public BorrowBookControl() {
        this.library = Library.getInstance();
        state = ControlState.INITIALISED;
    }

    public void setUi(BorrowBookUI ui) {
        if (!state.equals(ControlState.INITIALISED)) {
            throw new RuntimeException("BorrowBookControl: cannot call setUI except in INITIALISED state");
        }

        this.ui = ui;
        ui.SeT_StAtE(BorrowBookUI.uI_STaTe.READY);
        state = ControlState.READY;
    }

    public void swipeCard(int memberId) {
        if (!state.equals(ControlState.READY)) {
            throw new RuntimeException("BorrowBookControl: cannot call cardSwiped except in READY state");
        }

        member = library.getMember(memberId);
        if (member == null) {
            ui.DiSpLaY("Invalid memberId");
            return;
        }
        if (library.canMemberBorrow(member)) {
            pendingList = new ArrayList<>();
            ui.SeT_StAtE(BorrowBookUI.uI_STaTe.SCANNING);
            state = ControlState.SCANNING;
        } else {
            ui.DiSpLaY("Member cannot borrow at this time");
            ui.SeT_StAtE(BorrowBookUI.uI_STaTe.RESTRICTED);
        }
    }

    public void scanBook(int bookId) {
        book = null;
        if (!state.equals(ControlState.SCANNING)) {
            throw new RuntimeException("BorrowBookControl: cannot call bookScanned except in SCANNING state");
        }

        book = library.getBook(bookId);
        if (book == null) {
            ui.DiSpLaY("Invalid bookId");
            return;
        }
        if (!book.isAvailable()) {
            ui.DiSpLaY("Book cannot be borrowed");
            return;
        }
        pendingList.add(book);
        for (Book B : pendingList) {
            ui.DiSpLaY(B.toString());
        }

        if (library.getNumberOfLoansRemainingForMember(member) - pendingList.size() == 0) {
            ui.DiSpLaY("Loan limit reached");
            completeBorrow();
        }
    }

    public void completeBorrow() {
        if (pendingList.size() == 0) {
            cancelBorrow();
        } else {
            ui.DiSpLaY("\nFinal Borrowing List");
            for (Book book : pendingList) {
                ui.DiSpLaY(book.toString());
            }

            compleatedList = new ArrayList<Loan>();
            ui.SeT_StAtE(BorrowBookUI.uI_STaTe.FINALISING);
            state = ControlState.FINALISING;
        }
    }

    public void CommitLoans() {
        if (!state.equals(ControlState.FINALISING)) {
            throw new RuntimeException("BorrowBookControl: cannot call commitLoans except in FINALISING state");
        }

        for (Book book : pendingList) {
            Loan loan = library.issueLoan(book, member);
            compleatedList.add(loan);
        }
        ui.DiSpLaY("Completed Loan Slip");
        for (Loan loan : compleatedList) {
            ui.DiSpLaY(loan.toString());
        }

        ui.SeT_StAtE(BorrowBookUI.uI_STaTe.COMPLETED);
        state = ControlState.COMPLETED;
    }

    public void cancelBorrow() {
        ui.SeT_StAtE(BorrowBookUI.uI_STaTe.CANCELLED);
        state = ControlState.CANCELLED;
    }

}
