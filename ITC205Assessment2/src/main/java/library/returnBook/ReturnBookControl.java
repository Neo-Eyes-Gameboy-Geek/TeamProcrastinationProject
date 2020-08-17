package library.returnBook;

import library.entities.Book;
import library.entities.Library;
import library.entities.Loan;

public class ReturnBookControl {

    private ReturnBookUI ui;

    private enum ControlState {
        INITIALISED, READY, INSPECTING
    };
    private ControlState state;

    private Library library;
    private Loan currentLoan;

    public ReturnBookControl() {
        this.library = Library.getInstance();
        state = ControlState.INITIALISED;
    }

    public void setUi(ReturnBookUI ui) {
        if (!state.equals(ControlState.INITIALISED)) {
            throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
        }

        this.ui = ui;
        ui.setState(ReturnBookUI.uiState.READY);
        state = ControlState.READY;
    }

    public void bookScanned(int bookId) {
        if (!state.equals(ControlState.READY)) {
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
        String currentBookString = currentBook.toString();
        ui.display(currentBookString);
        String currentLoanString = currentLoan.toString();
        ui.display(currentLoanString);

        if (currentLoan.isBookOverdue()) {
            String fineOutput = String.format("\nOverdue fine : $%.2f", overDueFine);
            ui.display(fineOutput);
        }

        ui.setState(ReturnBookUI.uiState.INSPECTING);
        state = ControlState.INSPECTING;
    }

    public void scanningComplete() {
        if (!state.equals(ControlState.READY)) {
            throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
        }

        ui.setState(ReturnBookUI.uiState.COMPLETED);
    }

    public void dischargeLoan(boolean isDamaged) {
        if (!state.equals(ControlState.INSPECTING)) {
            throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
        }

        library.dischargeLoan(currentLoan, isDamaged);
        currentLoan = null;
        ui.setState(ReturnBookUI.uiState.READY);
        state = ControlState.READY;
    }

}
