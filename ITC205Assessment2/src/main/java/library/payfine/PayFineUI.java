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

<<<<<<< HEAD
	public static enum uIState { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };

	private PayFineControl CoNtRoL;
	private Scanner input;
	private uIState state;

	
	public PayFineUI(PayFineControl control) {
		this.CoNtRoL = control;
		input = new Scanner(System.in);
		state = uIState.INITIALISED;
		control.setUI(this);
	}
	
	
	public void setState(uIState state) {
		this.state = state;
	}


	public void RuN() {
		output("Pay Fine Use Case UI\n");
		
		while (true) {
			
			switch (state) {
			
			case READY:
				String Mem_Str = input("Swipe member card (press <enter> to cancel): ");
				if (Mem_Str.length() == 0) {
					CoNtRoL.cancel();
					break;
				}
				try {
					int Member_ID = Integer.valueOf(Mem_Str).intValue();
					CoNtRoL.cardSwiped(Member_ID);
				}
				catch (NumberFormatException e) {
					output("Invalid memberId");
				}
				break;
				
			case PAYING:
				double AmouNT = 0;
				String Amt_Str = input("Enter amount (<Enter> cancels) : ");
				if (Amt_Str.length() == 0) {
					CoNtRoL.cancel();
					break;
				}
				try {
					AmouNT = Double.valueOf(Amt_Str).doubleValue();
				}
				catch (NumberFormatException e) {}
				if (AmouNT <= 0) {
					output("Amount must be positive");
					break;
				}
				CoNtRoL.payFine(AmouNT);
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
=======
    private void output(Object object) {
        System.out.println(object);
    }
>>>>>>> master

    public void display(Object object) {
        output(object);
    }

}
