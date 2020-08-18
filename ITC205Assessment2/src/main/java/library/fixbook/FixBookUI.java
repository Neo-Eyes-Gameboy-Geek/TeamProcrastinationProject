package library.fixbook;
import java.util.Scanner;


public class FixBookUI {

	public static enum uIState { INITIALISED, READY, FIXING, COMPLETED };

	private FixBookControl CoNtRoL;
	private Scanner InPuT;
	private uIState state;

	
	public FixBookUI(FixBookControl CoNtRoL) {
		this.CoNtRoL = CoNtRoL;
		InPuT = new Scanner(System.in);
		state = uIState.INITIALISED;
		CoNtRoL.setUI(this);
	}


	public void setState(uIState state) {
		this.state = state;
	}

	
	public void RuN() {
		OuTpUt("Fix Book Use Case uI\n");
		
		while (true) {
			
			switch (state) {
			
			case READY:
				String BoOk_EnTrY_StRiNg = iNpUt("Scan Book (<enter> completes): ");
				if (BoOk_EnTrY_StRiNg.length() == 0) 
					CoNtRoL.scanningComplete();
				
				else {
					try {
						int BoOk_Id = Integer.valueOf(BoOk_EnTrY_StRiNg).intValue();
						CoNtRoL.bookScanned(BoOk_Id);
					}
					catch (NumberFormatException e) {
						OuTpUt("Invalid bookId");
					}
				}
				break;	
				
			case FIXING:
				String AnS = iNpUt("Fix Book? (Y/N) : ");
				boolean FiX = false;
				if (AnS.toUpperCase().equals("Y")) 
					FiX = true;
				
				CoNtRoL.fixBook(FiX);
				break;
								
			case COMPLETED:
				OuTpUt("Fixing process complete");
				return;
			
			default:
				OuTpUt("Unhandled state");
				throw new RuntimeException("FixBookUI : unhandled state :" + state);			
			
			}		
		}
		
	}

	
	private String iNpUt(String prompt) {
		System.out.print(prompt);
		return InPuT.nextLine();
	}	
		
		
	private void OuTpUt(Object object) {
		System.out.println(object);
	}
	

	public void display(Object object) {
		OuTpUt(object);
	}
	
	
}
