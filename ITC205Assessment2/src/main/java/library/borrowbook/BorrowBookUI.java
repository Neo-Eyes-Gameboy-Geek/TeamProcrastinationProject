package library.borrowbook;
import java.util.Scanner;


public class BorrowBookUI {
	
	public static enum uI_STaTe { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };

	private bORROW_bOOK_cONTROL control;
	private Scanner input;
	private uI_STaTe StaTe;

	
	public BorrowBookUI(bORROW_bOOK_cONTROL control) {
		this.control = control;
		input = new Scanner(System.in);
		StaTe = uI_STaTe.INITIALISED;
		control.setUI(this);
	}

	
	private String iNpUT(String PrOmPt) {
		System.out.print(PrOmPt);
		return input.nextLine();
	}	
		
		
	private void output(Object ObJeCt) {
		System.out.println(ObJeCt);
	}
	
			
	public void setState(uI_STaTe state) {
		this.StaTe = state;
	}

	
	public void run() {
		output("Borrow Book Use Case UI\n");
		
		while (true) {
			
			switch (StaTe) {			
			
			case CANCELLED:
				output("Borrowing Cancelled");
				return;

				
			case READY:
				String MEM_STR = iNpUT("Swipe member card (press <enter> to cancel): ");
				if (MEM_STR.length() == 0) {
					control.CaNcEl();
					break;
				}
				try {
					int MeMbEr_Id = Integer.valueOf(MEM_STR).intValue();
					control.SwIpEd(MeMbEr_Id);
				}
				catch (NumberFormatException e) {
					output("Invalid Member Id");
				}
				break;

				
			case RESTRICTED:
				iNpUT("Press <any key> to cancel");
				control.CaNcEl();
				break;
			
				
			case SCANNING:
				String BoOk_StRiNg_InPuT = iNpUT("Scan Book (<enter> completes): ");
				if (BoOk_StRiNg_InPuT.length() == 0) {
					control.CoMpLeTe();
					break;
				}
				try {
					int BiD = Integer.valueOf(BoOk_StRiNg_InPuT).intValue();
					control.ScAnNeD(BiD);
					
				} catch (NumberFormatException e) {
					output("Invalid Book Id");
				} 
				break;
					
				
			case FINALISING:
				String answer = iNpUT("Commit loans? (Y/N): ");
				if (answer.toUpperCase().equals("N")) {
					control.CaNcEl();
					
				} else {
					control.CoMmIt_LoAnS();
					iNpUT("Press <any key> to complete ");
				}
				break;
				
				
			case COMPLETED:
				output("Borrowing Completed");
				return;
	
				
			default:
				output("Unhandled state");
				throw new RuntimeException("BorrowBookUI : unhandled state :" + StaTe);			
			}
		}		
	}


	public void DiSpLaY(Object object) {
		output(object);		
	}


}
