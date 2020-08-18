package library.returnBook;
import library.entities.Book;
import library.entities.Library;
import library.entities.Loan;

public class rETURN_bOOK_cONTROL {

	private ReturnBookUI uI;
	private enum cOnTrOl_sTaTe { INITIALISED, READY, INSPECTING };
	private cOnTrOl_sTaTe sTaTe;
	
	private Library lIbRaRy;
	private Loan CurrENT_loan;
	

	public rETURN_bOOK_cONTROL() {
		this.lIbRaRy = Library.getInstance();
		sTaTe = cOnTrOl_sTaTe.INITIALISED;
	}
	
	
	public void sEt_uI(ReturnBookUI uI) {
		if (!sTaTe.equals(cOnTrOl_sTaTe.INITIALISED)) 
			throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
		
		this.uI = uI;
		uI.sEt_sTaTe(ReturnBookUI.uIState.READY);
		sTaTe = cOnTrOl_sTaTe.READY;		
	}


	public void bOoK_sCaNnEd(int bOoK_iD) {
		if (!sTaTe.equals(cOnTrOl_sTaTe.READY)) 
			throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
		
		Book cUrReNt_bOoK = lIbRaRy.getBook(bOoK_iD);
		
		if (cUrReNt_bOoK == null) {
			uI.DiSpLaY("Invalid Book Id");
			return;
		}
		if (!cUrReNt_bOoK.isOnLoan()) {
			uI.DiSpLaY("Book has not been borrowed");
			return;
		}		
		CurrENT_loan = lIbRaRy.getLoanByBookId(bOoK_iD);	
		double Over_Due_Fine = 0.0;
		if (CurrENT_loan.isBookOverdue()) 
			Over_Due_Fine = lIbRaRy.calculateOverDueFine(CurrENT_loan);
		
		uI.DiSpLaY("Inspecting");
		uI.DiSpLaY(cUrReNt_bOoK.toString());
		uI.DiSpLaY(CurrENT_loan.toString());
		
		if (CurrENT_loan.isBookOverdue()) 
			uI.DiSpLaY(String.format("\nOverdue fine : $%.2f", Over_Due_Fine));
		
		uI.sEt_sTaTe(ReturnBookUI.uIState.INSPECTING);
		sTaTe = cOnTrOl_sTaTe.INSPECTING;		
	}


	public void sCaNnInG_cOmPlEtE() {
		if (!sTaTe.equals(cOnTrOl_sTaTe.READY)) 
			throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
			
		uI.sEt_sTaTe(ReturnBookUI.uIState.COMPLETED);		
	}


	public void dIsChArGe_lOaN(boolean iS_dAmAgEd) {
		if (!sTaTe.equals(cOnTrOl_sTaTe.INSPECTING)) 
			throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
		
		lIbRaRy.dischargeLoan(CurrENT_loan, iS_dAmAgEd);
		CurrENT_loan = null;
		uI.sEt_sTaTe(ReturnBookUI.uIState.READY);
		sTaTe = cOnTrOl_sTaTe.READY;				
	}


}
