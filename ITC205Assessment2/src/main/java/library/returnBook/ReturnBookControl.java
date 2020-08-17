package library.returnBook;

import library.entities.Book;
import library.entities.Library;
import library.entities.Loan;

public class ReturnBookControl {

    private ReturnBookUI ui;

    private enum controlState {
        INITIALISED, READY, INSPECTING
    };
    private controlState state;

    private Library library;
    private Loan currentLoan;

    public ReturnBookControl() {
        this.library = Library.getInstance();
        state = controlState.INITIALISED;
    }

    public void setUi(ReturnBookUI ui) {
        if (!state.equals(controlState.INITIALISED)) {
            throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
        }

        this.ui = ui;
        ui.setState(ReturnBookUI.UiState.READY);
        state = controlState.READY;
    }

    public void bookScanned(int bookId) {
        if (!state.equals(controlState.READY)) {
            throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
        }

        Book currentBook = library.getBook(bookId);

        if (currentBook == null) {
            ui.display("Invalid Book Id");
            return;
        }
        if (!currentBook.isOnLoan()) {
            ui.display("Book has not been borrowed");
            return;
        }
        currentLoan = library.getLoanByBookId(bookId);
        double overDueFine = 0.0;
        if (currentLoan.isBookOverdue()) {
            overDueFine = library.calculateOverDueFine(currentLoan);
        }
        ui.display("Inspecting");
        ui.display(currentBook.toString());
        ui.display(currentLoan.toString());

        if (currentLoan.isBookOverdue()) {
            ui.display(String.format("\nOverdue fine : $%.2f", overDueFine));
        }
        ui.setState(ReturnBookUI.UiState.INSPECTING);
        state = controlState.INSPECTING;
    }

    public void scanningComplete() {
        if (!state.equals(controlState.READY)) {
            throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
        }
        ui.setState(ReturnBookUI.UiState.COMPLETED);
    }

    public void dischargeLoan(boolean isDamaged) {
        if (!state.equals(controlState.INSPECTING)) {
            throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
        }
        library.dischargeLoan(currentLoan, isDamaged);
        currentLoan = null;
        ui.setState(ReturnBookUI.UiState.READY);
        state = controlState.READY;
    }

}
