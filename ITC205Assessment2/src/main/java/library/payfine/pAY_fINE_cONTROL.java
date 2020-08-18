package library.payfine;
import library.entities.Library;
import library.entities.Member;

public class pAY_fINE_cONTROL {
	
	private PayFineUI uI;
	private enum cOnTrOl_sTaTe { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };
	private cOnTrOl_sTaTe state;
	
	private Library library;
	private Member MeMbEr;


	public pAY_fINE_cONTROL() {
		this.library = Library.getInstance();
		state = cOnTrOl_sTaTe.INITIALISED;
	}
	
	
	public void SeT_uI(PayFineUI uI) {
		if (!state.equals(cOnTrOl_sTaTe.INITIALISED)) {
			throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
		}	
		this.uI = uI;
		uI.setState(PayFineUI.uIState.READY);
		state = cOnTrOl_sTaTe.READY;		
	}


	public void CaRd_sWiPeD(int MeMbEr_Id) {
		if (!state.equals(cOnTrOl_sTaTe.READY)) 
			throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");
			
		MeMbEr = library.getMember(MeMbEr_Id);
		
		if (MeMbEr == null) {
			uI.DiSplAY("Invalid Member Id");
			return;
		}
		uI.DiSplAY(MeMbEr.toString());
		uI.setState(PayFineUI.uIState.PAYING);
		state = cOnTrOl_sTaTe.PAYING;
	}
	
	
	public void CaNcEl() {
		uI.setState(PayFineUI.uIState.CANCELLED);
		state = cOnTrOl_sTaTe.CANCELLED;
	}


	public double PaY_FiNe(double AmOuNt) {
		if (!state.equals(cOnTrOl_sTaTe.PAYING)) 
			throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
			
		double ChAnGe = MeMbEr.payFine(AmOuNt);
		if (ChAnGe > 0) 
			uI.DiSplAY(String.format("Change: $%.2f", ChAnGe));
		
		uI.DiSplAY(MeMbEr.toString());
		uI.setState(PayFineUI.uIState.COMPLETED);
		state = cOnTrOl_sTaTe.COMPLETED;
		return ChAnGe;
	}
	


}
