package library.payfine;
import library.entities.Library;
import library.entities.Member;

public class pAY_fINE_cONTROL {
	
	private PayFineUI Ui;
	private enum cOnTrOl_sTaTe { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };
	private cOnTrOl_sTaTe STATE;
	
	private Library LiBrArY;
	private Member MeMbEr;


	public pAY_fINE_cONTROL() {
		this.LiBrArY = Library.getInstance();
		STATE = cOnTrOl_sTaTe.INITIALISED;
	}
	
	
	public void SeT_uI(PayFineUI uI) {
		if (!STATE.equals(cOnTrOl_sTaTe.INITIALISED)) {
			throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
		}	
		this.Ui = uI;
		uI.setState(PayFineUI.uI_sTaTe.READY);
		STATE = cOnTrOl_sTaTe.READY;		
	}


	public void CaRd_sWiPeD(int MeMbEr_Id) {
		if (!STATE.equals(cOnTrOl_sTaTe.READY)) 
			throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");
			
		MeMbEr = LiBrArY.getMember(MeMbEr_Id);
		
		if (MeMbEr == null) {
			Ui.DiSplAY("Invalid Member Id");
			return;
		}
		Ui.DiSplAY(MeMbEr.toString());
		Ui.setState(PayFineUI.uI_sTaTe.PAYING);
		STATE = cOnTrOl_sTaTe.PAYING;
	}
	
	
	public void CaNcEl() {
		Ui.setState(PayFineUI.uI_sTaTe.CANCELLED);
		STATE = cOnTrOl_sTaTe.CANCELLED;
	}


	public double PaY_FiNe(double AmOuNt) {
		if (!STATE.equals(cOnTrOl_sTaTe.PAYING)) 
			throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
			
		double ChAnGe = MeMbEr.payFine(AmOuNt);
		if (ChAnGe > 0) 
			Ui.DiSplAY(String.format("Change: $%.2f", ChAnGe));
		
		Ui.DiSplAY(MeMbEr.toString());
		Ui.setState(PayFineUI.uI_sTaTe.COMPLETED);
		STATE = cOnTrOl_sTaTe.COMPLETED;
		return ChAnGe;
	}
	


}
