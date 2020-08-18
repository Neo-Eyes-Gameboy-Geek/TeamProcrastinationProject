package library.returnBook;
import java.util.Scanner;


public class ReturnBookUI {

	public static enum uI_sTaTe { INITIALISED, READY, INSPECTING, COMPLETED };

	private rETURN_bOOK_cONTROL control;
	private Scanner input;
	private uI_sTaTe StATe;

	
	public ReturnBookUI(rETURN_bOOK_cONTROL cOnTrOL) {
		this.control = cOnTrOL;
		input = new Scanner(System.in);
		StATe = uI_sTaTe.INITIALISED;
		cOnTrOL.sEt_uI(this);
	}


	public void run() {		
		oUtPuT("Return Book Use Case UI\n");
		
		while (true) {
			
			switch (StATe) {
			
			case INITIALISED:
				break;
				
			case READY:
				String BoOk_InPuT_StRiNg = input("Scan Book (<enter> completes): ");
				if (BoOk_InPuT_StRiNg.length() == 0) 
					control.sCaNnInG_cOmPlEtE();
				
				else {
					try {
						int Book_Id = Integer.valueOf(BoOk_InPuT_StRiNg).intValue();
						control.bOoK_sCaNnEd(Book_Id);
					}
					catch (NumberFormatException e) {
						oUtPuT("Invalid bookId");
					}					
				}
				break;				
				
			case INSPECTING:
				String answer = input("Is book damaged? (Y/N): ");
				boolean Is_DAmAgEd = false;
				if (answer.toUpperCase().equals("Y")) 					
					Is_DAmAgEd = true;
				
				control.dIsChArGe_lOaN(Is_DAmAgEd);
			
			case COMPLETED:
				oUtPuT("Return processing complete");
				return;
			
			default:
				oUtPuT("Unhandled state");
				throw new RuntimeException("ReturnBookUI : unhandled state :" + StATe);			
			}
		}
	}

	
	private String input(String PrOmPt) {
		System.out.print(PrOmPt);
		return input.nextLine();
	}	
		
		
	private void oUtPuT(Object ObJeCt) {
		System.out.println(ObJeCt);
	}
	
			
	public void DiSpLaY(Object object) {
		oUtPuT(object);
	}
	
	public void sEt_sTaTe(uI_sTaTe state) {
		this.StATe = state;
	}

	
}
