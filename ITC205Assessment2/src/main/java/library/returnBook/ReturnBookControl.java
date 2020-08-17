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

    public void setUi(ReturnBookUI uI) {
        if (!state.equals(ControlState.INITIALISED)) {
            throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
        }

        this.ui = uI;
        uI.sEt_sTaTe(ReturnBookUI.uI_sTaTe.READY);
        state = ControlState.READY;
    }

    public void bookScanned(int bOoK_iD) {
        if (!state.equals(ControlState.READY)) {
            throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
        }

        Book cUrReNt_bOoK = library.getBook(bOoK_iD);

        if (cUrReNt_bOoK == null) {
            ui.DiSpLaY("Invalid Book Id");
            return;
        }
        if (!cUrReNt_bOoK.isOnLoan()) {
            ui.DiSpLaY("Book has not been borrowed");
            return;
        }
        currentLoan = library.getLoanByBookId(bOoK_iD);
        double Over_Due_Fine = 0.0;
        if (currentLoan.isBookOverdue()) {
            Over_Due_Fine = library.calculateOverDueFine(currentLoan);
        }

        ui.DiSpLaY("Inspecting");
        ui.DiSpLaY(cUrReNt_bOoK.toString());
        ui.DiSpLaY(currentLoan.toString());

        if (currentLoan.isBookOverdue()) {
            ui.DiSpLaY(String.format("\nOverdue fine : $%.2f", Over_Due_Fine));
        }

        ui.sEt_sTaTe(ReturnBookUI.uI_sTaTe.INSPECTING);
        state = ControlState.INSPECTING;
    }

    public void scanningComplete() {
        if (!state.equals(ControlState.READY)) {
            throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
        }

        ui.sEt_sTaTe(ReturnBookUI.uI_sTaTe.COMPLETED);
    }

    public void dischargeLoan(boolean iS_dAmAgEd) {
        if (!state.equals(ControlState.INSPECTING)) {
            throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
        }

        library.dischargeLoan(currentLoan, iS_dAmAgEd);
        currentLoan = null;
        ui.sEt_sTaTe(ReturnBookUI.uI_sTaTe.READY);
        state = ControlState.READY;
    }

}
