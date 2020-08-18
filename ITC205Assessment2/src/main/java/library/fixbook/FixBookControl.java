package library.fixbook;
import library.entities.Book;
import library.entities.Library;

public class FixBookControl {
	
	private FixBookUI Ui;
	private enum CoNtRoL_StAtE { INITIALISED, READY, FIXING };
	private CoNtRoL_StAtE STATE;
	
	private Library LiBrArY;
	private Book CuRrEnT_BoOk;


	public FixBookControl() {
		this.LiBrArY = Library.getInstance();
		STATE = CoNtRoL_StAtE.INITIALISED;
	}
	
	
	public void setUI(FixBookUI ui) {
		if (!STATE.equals(CoNtRoL_StAtE.INITIALISED)) 
			throw new RuntimeException("FixBookControl: cannot call setUI except in INITIALISED state");
			
		this.Ui = ui;
		ui.setState(FixBookUI.uIState.READY);
		STATE = CoNtRoL_StAtE.READY;		
	}


	public void bookScanned(int BoOkId) {
		if (!STATE.equals(CoNtRoL_StAtE.READY)) 
			throw new RuntimeException("FixBookControl: cannot call bookScanned except in READY state");
			
		CuRrEnT_BoOk = LiBrArY.getBook(BoOkId);
		
		if (CuRrEnT_BoOk == null) {
			Ui.display("Invalid bookId");
			return;
		}
		if (!CuRrEnT_BoOk.isDamaged()) {
			Ui.display("Book has not been damaged");
			return;
		}
		Ui.display(CuRrEnT_BoOk.toString());
		Ui.setState(FixBookUI.uIState.FIXING);
		STATE = CoNtRoL_StAtE.FIXING;		
	}


	public void fixBook(boolean mUsT_FiX) {
		if (!STATE.equals(CoNtRoL_StAtE.FIXING)) 
			throw new RuntimeException("FixBookControl: cannot call fixBook except in FIXING state");
			
		if (mUsT_FiX) 
			LiBrArY.repairBook(CuRrEnT_BoOk);
		
		CuRrEnT_BoOk = null;
		Ui.setState(FixBookUI.uIState.READY);
		STATE = CoNtRoL_StAtE.READY;		
	}

	
	public void scanningComplete() {
		if (!STATE.equals(CoNtRoL_StAtE.READY)) 
			throw new RuntimeException("FixBookControl: cannot call scanningComplete except in READY state");
			
		Ui.setState(FixBookUI.uIState.COMPLETED);		
	}

}
