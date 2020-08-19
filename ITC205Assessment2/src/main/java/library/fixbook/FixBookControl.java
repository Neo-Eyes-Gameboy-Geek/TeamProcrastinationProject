package library.fixbook;

import library.entities.Book;
import library.entities.Library;

public class FixBookControl {

    private FixBookUI uI;

    private enum ControlState {
        INITIALISED, READY, FIXING
    };
    private ControlState state;

    private Library library;
    private Book currentBook;

    public FixBookControl() {
        this.library = Library.getInstance();
        state = ControlState.INITIALISED;
    }

    public void setUI(FixBookUI uI) {
        if (!state.equals(ControlState.INITIALISED)) {
            throw new RuntimeException("FixBookControl: cannot call setUI except in INITIALISED state");
        }

        this.uI = uI;
        uI.setState(FixBookUI.UIState.READY);
        state = ControlState.READY;
    }

    public void bookScanned(int bookId) {
        if (!state.equals(ControlState.READY)) {
            throw new RuntimeException("FixBookControl: cannot call bookScanned except in READY state");
        }

        currentBook = library.getBook(bookId);

        if (currentBook == null) {
            uI.display("Invalid bookId");
            return;
        }
        if (!currentBook.isDamaged()) {
            uI.display("Book has not been damaged");
            return;
        }
        String bookName = currentBook.toString();
        uI.display(bookName);
        uI.setState(FixBookUI.UIState.FIXING);
        state = ControlState.FIXING;
    }

    public void fixBook(boolean mustFix) {
        if (!state.equals(ControlState.FIXING)) {
            throw new RuntimeException("FixBookControl: cannot call fixBook except in FIXING state");
        }

        if (mustFix) {
            library.repairBook(currentBook);
        }

        currentBook = null;
        uI.setState(FixBookUI.UIState.READY);
        state = ControlState.READY;
    }

    public void scanningComplete() {
        if (!state.equals(ControlState.READY)) {
            throw new RuntimeException("FixBookControl: cannot call scanningComplete except in READY state");
        }

        uI.setState(FixBookUI.UIState.COMPLETED);
    }

}
