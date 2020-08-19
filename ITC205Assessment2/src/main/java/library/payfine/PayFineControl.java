package library.payfine;
import library.entities.Library;
import library.entities.Member;

public class PayFineControl {
	
	private PayFineUI uI;
	private enum ControlState { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };
	private ControlState state;
	
	private Library Library;
	private Member Member;


	public PayFineControl() {
		this.Library = Library.getInstance();
		state = ControlState.INITIALISED;
	}
	
	
	public void setUI(PayFineUI uI) {
		if (!state.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
		}	
		this.uI = uI;
		uI.setState(PayFineUI.UIState.READY);
		state = ControlState.READY;		
	}


	public void cardSwiped(int memberId) {
		if (!state.equals(ControlState.READY)) 
			throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");
			
		Member = Library.getMember(memberId);
		
		if (Member == null) {
			uI.display("Invalid Member Id");
			return;
		}
                String memberName = Member.toString();
		uI.display(memberName);
		uI.setState(PayFineUI.UIState.PAYING);
		state = ControlState.PAYING;
	}
	
	
	public void cancel() {
		uI.setState(PayFineUI.UIState.CANCELLED);
		state = ControlState.CANCELLED;
	}

	public double payFine(double amount) {
		if (!state.equals(ControlState.PAYING)) 
			throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
			
		double change = Member.payFine(amount);
		if (change > 0) 
			uI.display(String.format("Change: $%.2f", change));
		
		uI.display(Member.toString());
		uI.setState(PayFineUI.UIState.COMPLETED);
		state = ControlState.COMPLETED;
		return change;
	}
        
}
