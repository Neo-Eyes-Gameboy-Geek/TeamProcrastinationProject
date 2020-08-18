package library.payfine;

import library.entities.Library;
import library.entities.Member;

public class PayFineControl {

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

        uI.display(member.toString());
        uI.setState(PayFineUI.uIState.COMPLETED);
        state = ControlState.COMPLETED;
        return change;
    }

}
