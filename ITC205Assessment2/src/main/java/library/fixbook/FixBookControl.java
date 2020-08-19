package library.fixbook;
import library.entities.Book;
import library.entities.Library;

public class FixBookControl {
	
	private FixBookUI uI;
	private enum CoNtRoL_StAtE { INITIALISED, READY, FIXING };
	private CoNtRoL_StAtE state;
	
	private Library library;
	private Book CuRrEnT_BoOk;


<<<<<<< HEAD:ITC205Assessment2/src/main/java/library/fixbook/FixBookControl.java
	public FixBookControl() {
		this.library = Library.getInstance();
=======
	public fIX_bOOK_cONTROL() {
		this.LiBrArY = Library.getInstance();
>>>>>>> master:ITC205Assessment2/src/main/java/library/fixbook/fIX_bOOK_cONTROL.java
		state = CoNtRoL_StAtE.INITIALISED;
	}
	
	
	public void SeT_Ui(FixBookUI ui) {
		if (!state.equals(CoNtRoL_StAtE.INITIALISED)) 
			throw new RuntimeException("FixBookControl: cannot call setUI except in INITIALISED state");
			
<<<<<<< HEAD:ITC205Assessment2/src/main/java/library/fixbook/FixBookControl.java
		this.uI = ui;
		ui.setState(FixBookUI.uIState.READY);
=======
		this.Ui = ui;
		ui.setState(FixBookUI.UIState.READY);
>>>>>>> master:ITC205Assessment2/src/main/java/library/fixbook/fIX_bOOK_cONTROL.java
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
<<<<<<< HEAD:ITC205Assessment2/src/main/java/library/fixbook/FixBookControl.java
		uI.dIsPlAy(CuRrEnT_BoOk.toString());
		uI.setState(FixBookUI.uIState.FIXING);
=======
		Ui.dIsPlAy(CuRrEnT_BoOk.toString());
		Ui.setState(FixBookUI.UIState.FIXING);
>>>>>>> master:ITC205Assessment2/src/main/java/library/fixbook/fIX_bOOK_cONTROL.java
		state = CoNtRoL_StAtE.FIXING;		
	}


	public void FiX_BoOk(boolean mUsT_FiX) {
		if (!state.equals(CoNtRoL_StAtE.FIXING)) 
			throw new RuntimeException("FixBookControl: cannot call fixBook except in FIXING state");
			
		if (mUsT_FiX) 
			library.repairBook(CuRrEnT_BoOk);
		
		CuRrEnT_BoOk = null;
<<<<<<< HEAD:ITC205Assessment2/src/main/java/library/fixbook/FixBookControl.java
		uI.setState(FixBookUI.uIState.READY);
=======
		Ui.setState(FixBookUI.UIState.READY);
>>>>>>> master:ITC205Assessment2/src/main/java/library/fixbook/fIX_bOOK_cONTROL.java
		state = CoNtRoL_StAtE.READY;		
	}

	
	public void SCannING_COMplete() {
		if (!state.equals(CoNtRoL_StAtE.READY)) 
			throw new RuntimeException("FixBookControl: cannot call scanningComplete except in READY state");
			
<<<<<<< HEAD:ITC205Assessment2/src/main/java/library/fixbook/FixBookControl.java
		uI.setState(FixBookUI.uIState.COMPLETED);		
=======
		Ui.setState(FixBookUI.UIState.COMPLETED);		
>>>>>>> master:ITC205Assessment2/src/main/java/library/fixbook/fIX_bOOK_cONTROL.java
	}

}
