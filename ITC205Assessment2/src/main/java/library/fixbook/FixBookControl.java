package library.fixbook;
import library.entities.Book;
import library.entities.Library;

public class FixBookControl {
	
	private FixBookUI uI;
	private enum CoNtRoL_StAtE { INITIALISED, READY, FIXING };
	private CoNtRoL_StAtE state;
	
	private Library library;
	private Book CuRrEnT_BoOk;

	public FixBookControl() {
		this.library = Library.getInstance();
		state = CoNtRoL_StAtE.INITIALISED;
	}
	
	
	public void SeT_Ui(FixBookUI ui) {
		if (!state.equals(CoNtRoL_StAtE.INITIALISED)) 
			throw new RuntimeException("FixBookControl: cannot call setUI except in INITIALISED state");
			
		this.uI = ui;
		ui.setState(FixBookUI.uIState.READY);
		state = CoNtRoL_StAtE.READY;		
	}


	public void BoOk_ScAnNeD(int BoOkId) {
		if (!state.equals(CoNtRoL_StAtE.READY)) 
			throw new RuntimeException("FixBookControl: cannot call bookScanned except in READY state");
			
		CuRrEnT_BoOk = library.getBook(BoOkId);
		
		if (CuRrEnT_BoOk == null) {
			uI.dIsPlAy("Invalid bookId");
			return;
		}
		if (!CuRrEnT_BoOk.isDamaged()) {
			uI.dIsPlAy("Book has not been damaged");
			return;
		}
		uI.dIsPlAy(CuRrEnT_BoOk.toString());
		uI.setState(FixBookUI.uIState.FIXING);
		state = CoNtRoL_StAtE.FIXING;		
	}


	public void FiX_BoOk(boolean mUsT_FiX) {
		if (!state.equals(CoNtRoL_StAtE.FIXING)) 
			throw new RuntimeException("FixBookControl: cannot call fixBook except in FIXING state");
			
		if (mUsT_FiX) 
			library.repairBook(CuRrEnT_BoOk);
		
		CuRrEnT_BoOk = null;
		uI.setState(FixBookUI.uIState.READY);
		state = CoNtRoL_StAtE.READY;		
	}

	
	public void SCannING_COMplete() {
		if (!state.equals(CoNtRoL_StAtE.READY)) 
			throw new RuntimeException("FixBookControl: cannot call scanningComplete except in READY state");
			
		uI.setState(FixBookUI.uIState.COMPLETED);		
	}

}
