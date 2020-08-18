package library.borrowbook;

import java.util.ArrayList;
import java.util.List;

import library.entities.Book;
import library.entities.Library;
import library.entities.Loan;
import library.entities.Member;

public class BorrowBookControl {

    private BorrowBookUI uI;

    private Library library;
    private Member member;

    private enum ControlState {
        INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED
    };
    private ControlState state;

    private List<Book> pendingList;
    private List<Loan> completedList;
    private Book book;

    public BorrowBookControl() {
        this.library = Library.getInstance();
        state = ControlState.INITIALISED;
    }

    public void setUI(BorrowBookUI uI) {
        if (!state.equals(ControlState.INITIALISED)) {
            throw new RuntimeException("BorrowBookControl: cannot call setUI except in INITIALISED state");
        }

        this.uI = uI;
        uI.setState(BorrowBookUI.UIState.READY);
        state = ControlState.READY;
    }

    public void swipeCard(int memberId) {
        if (!state.equals(ControlState.READY)) {
            throw new RuntimeException("BorrowBookControl: cannot call cardSwiped except in READY state");
        }

        member = library.getMember(memberId);
        if (member == null) {
            uI.displayUI("Invalid memberId");
            return;
        }
        if (library.canMemberBorrow(member)) {
            pendingList = new ArrayList<>();
            uI.setState(BorrowBookUI.UIState.SCANNING);
            state = ControlState.SCANNING;
        } else {
            uI.displayUI("Member cannot borrow at this time");
            uI.setState(BorrowBookUI.UIState.RESTRICTED);
        }
    }

    public void scanBook(int bookId) {
        book = null;
        if (!state.equals(ControlState.SCANNING)) {
            throw new RuntimeException("BorrowBookControl: cannot call bookScanned except in SCANNING state");
        }

        book = library.getBook(bookId);
        if (book == null) {
            uI.displayUI("Invalid bookId");
            return;
        }
        if (!book.isAvailable()) {
            uI.displayUI("Book cannot be borrowed");
            return;
        }
        pendingList.add(book);
        for (Book book : pendingList) {
            uI.displayUI(book.toString());
        }

        if (library.getNumberOfLoansRemainingForMember(member) - pendingList.size() == 0) {
            uI.displayUI("Loan limit reached");
            completeBorrow();
        }
    }

    public void completeBorrow() {
        if (pendingList.size() == 0) {
            cancelBorrow();
        } else {
            uI.displayUI("\nFinal Borrowing List");
            for (Book book : pendingList) {
                uI.displayUI(book.toString());
            }

            completedList = new ArrayList<Loan>();
            uI.setState(BorrowBookUI.UIState.FINALISING);
            state = ControlState.FINALISING;
        }
    }

    public void CommitLoans() {
        if (!state.equals(ControlState.FINALISING)) {
            throw new RuntimeException("BorrowBookControl: cannot call commitLoans except in FINALISING state");
        }

        for (Book book : pendingList) {
            Loan loan = library.issueLoan(book, member);
            completedList.add(loan);
        }
        uI.displayUI("Completed Loan Slip");
        for (Loan loan : completedList) {
            uI.displayUI(loan.toString());
        }

        uI.setState(BorrowBookUI.UIState.COMPLETED);
        state = ControlState.COMPLETED;
    }

    public void cancelBorrow() {
        uI.setState(BorrowBookUI.UIState.CANCELLED);
        state = ControlState.CANCELLED;
    }

}
