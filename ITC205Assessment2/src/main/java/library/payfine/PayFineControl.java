package library.payfine;
<<<<<<< HEAD

=======
>>>>>>> master
import library.entities.Library;
import library.entities.Member;

public class PayFineControl {
<<<<<<< HEAD

    private PayFineUI uI;

    private enum ControlState {
        INITIALISED, READY, PAYING, COMPLETED, CANCELLED
    };
    private ControlState state;

    private Library library;
    private Member member;

    public PayFineControl() {
        this.library = Library.getInstance();
        state = ControlState.INITIALISED;
    }

    public void setUI(PayFineUI uI) {
        if (!state.equals(ControlState.INITIALISED)) {
            throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
        }
        this.uI = uI;
        uI.setState(PayFineUI.uIState.READY);
        state = ControlState.READY;
    }

    public void cardSwiped(int memberId) {
        if (!state.equals(ControlState.READY)) {
            throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");
        }

        member = library.getMember(memberId);

        if (member == null) {
            uI.display("Invalid Member Id");
            return;
        }
        String memberName = member.toString();
        uI.display(memberName);
        uI.setState(PayFineUI.uIState.PAYING);
        state = ControlState.PAYING;
    }

    public void cancel() {
        uI.setState(PayFineUI.uIState.CANCELLED);
        state = ControlState.CANCELLED;
    }

    public double payFine(double amount) {
        if (!state.equals(ControlState.PAYING)) {
            throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
        }

        double change = member.payFine(amount);
        if (change > 0) {
            uI.display(String.format("Change: $%.2f", change));
        }

        String memberName = member.toString();
        uI.display(memberName);
        uI.setState(PayFineUI.uIState.COMPLETED);
        state = ControlState.COMPLETED;
        return change;
    }
    
//*███████████████████████████████
/*████╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬████
██╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬██
█╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬█
█╬╬╬███████╬╬╬╬╬╬╬╬╬███████╬╬╬█
█╬╬██╬╬╬╬███╬╬╬╬╬╬╬███╬╬╬╬██╬╬█
█╬██╬╬╬╬╬╬╬██╬╬╬╬╬██╬╬╬╬╬╬╬██╬█
█╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬█
█╬╬╬╬█████╬╬╬╬╬╬╬╬╬╬╬█████╬╬╬╬█
█╬╬█████████╬╬╬╬╬╬╬█████████╬╬█
█╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬╬█
█╬╬╬╬╬╬╬╬╬╬╬╬╬╬█╬╬╬╬╬╬╬╬╬╬╬╬╬╬█
█╬╬╬╬╬╬╬╬╬╬╬╬╬╬█╬╬╬╬╬╬╬╬╬╬╬╬╬╬█
█╬╬╬╬╬╬╬╬╬╬╬╬╬╬█╬╬╬╬╬╬╬╬╬╬╬╬╬╬█
█╬╬╬▓▓▓▓╬╬╬╬╬╬╬█╬╬╬╬╬╬╬▓▓▓▓╬╬╬█
█╬╬▓▓▓▓▓▓╬╬█╬╬╬█╬╬╬█╬╬▓▓▓▓▓▓╬╬█
█╬╬╬▓▓▓▓╬╬██╬╬╬█╬╬╬██╬╬▓▓▓▓╬╬╬█
█╬╬╬╬╬╬╬╬██╬╬╬╬█╬╬╬╬██╬╬╬╬╬╬╬╬█
█╬╬╬╬╬████╬╬╬╬███╬╬╬╬████╬╬╬╬╬█
█╬╬╬╬╬╬╬╬╬╬╬╬╬███╬╬╬╬╬╬╬╬╬╬╬╬╬█
██╬╬█╬╬╬╬╬╬╬╬█████╬╬╬╬╬╬╬╬█╬╬██
██╬╬██╬╬╬╬╬╬███████╬╬╬╬╬╬██╬╬██
██╬╬▓███╬╬╬████╬████╬╬╬███▓╬╬██
███╬╬▓▓███████╬╬╬███████▓▓╬╬███
███╬╬╬╬▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓╬╬╬╬███
████╬╬╬╬╬╬╬╬╬╬███╬╬╬╬╬╬╬╬╬╬████
█████╬╬╬╬╬╬╬╬╬╬█╬╬╬╬╬╬╬╬╬╬█████
██████╬╬╬╬╬╬╬╬███╬╬╬╬╬╬╬╬██████
███████╬╬╬╬╬╬╬███╬╬╬╬╬╬╬███████
████████╬╬╬╬╬╬███╬╬╬╬╬╬████████
█████████╬╬╬╬╬███╬╬╬╬╬█████████
███████████╬╬╬╬█╬╬╬╬███████████
███████████████████████████████
*/
=======
	
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
	

>>>>>>> master

}
