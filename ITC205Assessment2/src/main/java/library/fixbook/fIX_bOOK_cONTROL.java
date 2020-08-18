package library.fixbook;
import library.entities.Book;
import library.entities.Library;

public class fIX_bOOK_cONTROL {
	
	private FixBookUI Ui;
	private enum CoNtRoL_StAtE { INITIALISED, READY, FIXING };
	private CoNtRoL_StAtE state;
	
	private Library LiBrArY;
	private Book CuRrEnT_BoOk;


	public fIX_bOOK_cONTROL() {
		this.LiBrArY = Library.getInstance();
		state = CoNtRoL_StAtE.INITIALISED;
	}
	
	
	public void SeT_Ui(FixBookUI ui) {
		if (!state.equals(CoNtRoL_StAtE.INITIALISED)) 
			throw new RuntimeException("FixBookControl: cannot call setUI except in INITIALISED state");
			
		this.Ui = ui;
		ui.setState(FixBookUI.UIState.READY);
		state = CoNtRoL_StAtE.READY;		
	}


	public void BoOk_ScAnNeD(int BoOkId) {
		if (!state.equals(CoNtRoL_StAtE.READY)) 
			throw new RuntimeException("FixBookControl: cannot call bookScanned except in READY state");
			
		CuRrEnT_BoOk = LiBrArY.getBook(BoOkId);
		
		if (CuRrEnT_BoOk == null) {
			Ui.dIsPlAy("Invalid bookId");
			return;
		}
		if (!CuRrEnT_BoOk.isDamaged()) {
			Ui.dIsPlAy("Book has not been damaged");
			return;
		}
		Ui.dIsPlAy(CuRrEnT_BoOk.toString());
		Ui.setState(FixBookUI.UIState.FIXING);
		state = CoNtRoL_StAtE.FIXING;		
	}


	public void FiX_BoOk(boolean mUsT_FiX) {
		if (!state.equals(CoNtRoL_StAtE.FIXING)) 
			throw new RuntimeException("FixBookControl: cannot call fixBook except in FIXING state");
			
		if (mUsT_FiX) 
			LiBrArY.repairBook(CuRrEnT_BoOk);
		
		CuRrEnT_BoOk = null;
		Ui.setState(FixBookUI.UIState.READY);
		state = CoNtRoL_StAtE.READY;		
	}

	
	public void SCannING_COMplete() {
		if (!state.equals(CoNtRoL_StAtE.READY)) 
			throw new RuntimeException("FixBookControl: cannot call scanningComplete except in READY state");
			
		Ui.setState(FixBookUI.UIState.COMPLETED);		
	}

}
