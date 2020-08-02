package library.returnBook;
import library.entities.Book;
import library.entities.Library;
import library.entities.Loan;

public class rETURN_bOOK_cONTROL {

	private ReturnBookUI Ui;
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
		
		this.Ui = uI;
		uI.sEt_sTaTe(ReturnBookUI.uI_sTaTe.READY);
		sTaTe = cOnTrOl_sTaTe.READY;		
	}


	public void bOoK_sCaNnEd(int bOoK_iD) {
		if (!sTaTe.equals(cOnTrOl_sTaTe.READY)) 
			throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
		
		Book cUrReNt_bOoK = lIbRaRy.getBook(bOoK_iD);
		
		if (cUrReNt_bOoK == null) {
			Ui.DiSpLaY("Invalid Book Id");
			return;
		}
		if (!cUrReNt_bOoK.iS_On_loan()) {
			Ui.DiSpLaY("Book has not been borrowed");
			return;
		}		
		CurrENT_loan = lIbRaRy.GeT_Loan_By_BoOkId(bOoK_iD);	
		double OVERDUE_Fine = 0.0;
		if (CurrENT_loan.isOverdue()) 
			OVERDUE_Fine = lIbRaRy.CaLcUlAtE_OVERDUE_FiNe(CurrENT_loan);
		
		Ui.DiSpLaY("Inspecting");
		Ui.DiSpLaY(cUrReNt_bOoK.toString());
		Ui.DiSpLaY(CurrENT_loan.toString());
		
		if (CurrENT_loan.isOverdue()) 
			Ui.DiSpLaY(String.format("\nOverdue fine : $%.2f", OVERDUE_Fine));
		
		Ui.sEt_sTaTe(ReturnBookUI.uI_sTaTe.INSPECTING);
		sTaTe = cOnTrOl_sTaTe.INSPECTING;		
	}


	public void sCaNnInG_cOmPlEtE() {
		if (!sTaTe.equals(cOnTrOl_sTaTe.READY)) 
			throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
			
		Ui.sEt_sTaTe(ReturnBookUI.uI_sTaTe.COMPLETED);		
	}


	public void dIsChArGe_loan(boolean iS_dAmAgEd) {
		if (!sTaTe.equals(cOnTrOl_sTaTe.INSPECTING)) 
			throw new RuntimeException("ReturnBookControl: cannot call dischargeloan except in INSPECTING state");
		
		lIbRaRy.discharge_Loan(CurrENT_loan, iS_dAmAgEd);
		CurrENT_loan = null;
		Ui.sEt_sTaTe(ReturnBookUI.uI_sTaTe.READY);
		sTaTe = cOnTrOl_sTaTe.READY;				
	}


}
