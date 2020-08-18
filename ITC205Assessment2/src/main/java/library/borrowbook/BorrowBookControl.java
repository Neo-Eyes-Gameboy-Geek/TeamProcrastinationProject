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
        ui.setState(BorrowBookUI.UIState.READY);
        state = ControlState.READY;
    }

    public void swipeCard(int memberId) {
        if (!state.equals(ControlState.READY)) {
            throw new RuntimeException("BorrowBookControl: cannot call cardSwiped except in READY state");
        }

        member = library.getMember(memberId);
        if (member == null) {
            ui.displayUI("Invalid memberId");
            return;
        }
        if (library.canMemberBorrow(member)) {
            pendingList = new ArrayList<>();
            ui.setState(BorrowBookUI.UIState.SCANNING);
            state = ControlState.SCANNING;
        } else {
            ui.displayUI("Member cannot borrow at this time");
            ui.setState(BorrowBookUI.UIState.RESTRICTED);
        }
    }

    public void scanBook(int bookId) {
        book = null;
        if (!state.equals(ControlState.SCANNING)) {
            throw new RuntimeException("BorrowBookControl: cannot call bookScanned except in SCANNING state");
        }

        book = library.getBook(bookId);
        if (book == null) {
            ui.displayUI("Invalid bookId");
            return;
        }
        if (!book.isAvailable()) {
            ui.displayUI("Book cannot be borrowed");
            return;
        }
        pendingList.add(book);
        for (Book B : pendingList) {
            ui.displayUI(B.toString());
        }

        if (library.getNumberOfLoansRemainingForMember(member) - pendingList.size() == 0) {
            ui.displayUI("Loan limit reached");
            completeBorrow();
        }
    }

    public void completeBorrow() {
        if (pendingList.size() == 0) {
            cancelBorrow();
        } else {
            ui.displayUI("\nFinal Borrowing List");
            for (Book book : pendingList) {
                ui.displayUI(book.toString());
            }

            compleatedList = new ArrayList<Loan>();
            ui.setState(BorrowBookUI.UIState.FINALISING);
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
        ui.displayUI("Completed Loan Slip");
        for (Loan loan : compleatedList) {
            ui.displayUI(loan.toString());
        }

        ui.setState(BorrowBookUI.UIState.COMPLETED);
        state = ControlState.COMPLETED;
    }

    public void cancelBorrow() {
        ui.setState(BorrowBookUI.UIState.CANCELLED);
        state = ControlState.CANCELLED;
    }

}
