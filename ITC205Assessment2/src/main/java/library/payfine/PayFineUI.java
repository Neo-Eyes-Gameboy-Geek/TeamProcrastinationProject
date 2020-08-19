package library.payfine;

import java.util.Scanner;

public class PayFineUI {

    public static enum UIState {
        INITIALISED, READY, PAYING, COMPLETED, CANCELLED
    };

    private PayFineControl control;
    private Scanner input;
    private UIState state;

    public PayFineUI(PayFineControl control) {
        this.control = control;
        input = new Scanner(System.in);
        state = UIState.INITIALISED;
        control.setUI(this);
    }

    public void setState(UIState state) {
        this.state = state;
    }

    public void run() {
        output("Pay Fine Use Case UI\n");

        while (true) {

            switch (state) {

                case READY:
                    String memberCardId = input("Swipe member card (press <enter> to cancel): ");
                    if (memberCardId.length() == 0) {
                        control.cancel();
                        break;
                    }
                    try {
                        int memberId = Integer.valueOf(memberCardId).intValue();
                        control.cardSwiped(memberId);
                    } catch (NumberFormatException numberFormatException) {
                        output("Invalid memberId");
                    }
                    break;

                case PAYING:
                    double payAmount = 0;
                    String payAmountString = input("Enter amount (<Enter> cancels) : ");
                    if (payAmountString.length() == 0) {
                        control.cancel();
                        break;
                    }
                    try {
                        payAmount = Double.valueOf(payAmountString).doubleValue();
                    } catch (NumberFormatException numberFormatException) {
                    }
                    if (payAmount <= 0) {
                        output("Amount must be positive");
                        break;
                    }
                    control.payFine(payAmount);
                    break;

                case CANCELLED:
                    output("Pay Fine process cancelled");
                    return;

                case COMPLETED:
                    output("Pay Fine process complete");
                    return;

                default:
                    output("Unhandled state");
                    throw new RuntimeException("FixBookUI : unhandled state :" + state);

            }
        }
    }

    private String input(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }

    private void output(Object object) {
        System.out.println(object);
    }

    public void display(Object object) {
        output(object);
    }

}
