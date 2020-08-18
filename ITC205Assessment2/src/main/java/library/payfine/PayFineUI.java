package library.payfine;
import java.util.Scanner;


public class PayFineUI {


	public static enum uI_sTaTe { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };

	private pAY_fINE_cONTROL CONTROL;
	private Scanner input;
	private uI_sTaTe STATE;

	
	public PayFineUI(pAY_fINE_cONTROL control) {
		this.CONTROL = control;
		input = new Scanner(System.in);
		STATE = uI_sTaTe.INITIALISED;
		control.SeT_uI(this);
	}
	
	
	public void setState(uI_sTaTe state) {
		this.STATE = state;
	}


	public void run() {
		output("Pay Fine Use Case UI\n");
		
		while (true) {
			
			switch (STATE) {
			
			case READY:
				String Mem_Str = input("Swipe member card (press <enter> to cancel): ");
				if (Mem_Str.length() == 0) {
					CONTROL.CaNcEl();
					break;
				}
				try {
					int Member_ID = Integer.valueOf(Mem_Str).intValue();
					CONTROL.CaRd_sWiPeD(Member_ID);
				}
				catch (NumberFormatException e) {
					output("Invalid memberId");
				}
				break;
				
			case PAYING:
				double AmouNT = 0;
				String Amt_Str = input("Enter amount (<Enter> cancels) : ");
				if (Amt_Str.length() == 0) {
					CONTROL.CaNcEl();
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
				CONTROL.PaY_FiNe(AmouNT);
				break;
								
			case CANCELLED:
				output("Pay Fine process cancelled");
				return;
			
			case COMPLETED:
				output("Pay Fine process complete");
				return;
			
			default:
				output("Unhandled state");
				throw new RuntimeException("FixBookUI : unhandled state :" + STATE);			
			
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
			

	public void DiSplAY(Object object) {
		output(object);
	}


}
