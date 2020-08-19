package library.payfine;
import library.entities.Library;
import library.entities.Member;

public class PayFineControl {
	
	private PayFineUI Ui;
	private enum cOnTrOl_sTaTe { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };
	private cOnTrOl_sTaTe state;
	
	private Library LiBrArY;
	private Member MeMbEr;


	public PayFineControl() {
		this.LiBrArY = Library.getInstance();
		state = cOnTrOl_sTaTe.INITIALISED;
	}
	
	
	public void setUI(PayFineUI uI) {
		if (!state.equals(cOnTrOl_sTaTe.INITIALISED)) {
			throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
		}	
		this.Ui = uI;
		uI.setState(PayFineUI.UIState.READY);
		state = cOnTrOl_sTaTe.READY;		
	}


	public void cardSwiped(int MeMbEr_Id) {
		if (!state.equals(cOnTrOl_sTaTe.READY)) 
			throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");
			
		MeMbEr = LiBrArY.getMember(MeMbEr_Id);
		
		if (MeMbEr == null) {
			Ui.display("Invalid Member Id");
			return;
		}
		Ui.display(MeMbEr.toString());
		Ui.setState(PayFineUI.UIState.PAYING);
		state = cOnTrOl_sTaTe.PAYING;
	}
	
	
	public void cancel() {
		Ui.setState(PayFineUI.UIState.CANCELLED);
		state = cOnTrOl_sTaTe.CANCELLED;
	}


	public double payFine(double AmOuNt) {
		if (!state.equals(cOnTrOl_sTaTe.PAYING)) 
			throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
			
		double ChAnGe = MeMbEr.payFine(AmOuNt);
		if (ChAnGe > 0) 
			Ui.display(String.format("Change: $%.2f", ChAnGe));
		
		Ui.display(MeMbEr.toString());
		Ui.setState(PayFineUI.UIState.COMPLETED);
		state = cOnTrOl_sTaTe.COMPLETED;
		return ChAnGe;
	}
	


}
