package library.fixbook;
import java.util.Scanner;


public class FixBookUI {

<<<<<<< HEAD
	public static enum uIState { INITIALISED, READY, FIXING, COMPLETED };

	private FixBookControl CoNtRoL;
	private Scanner InPuT;
	private uIState state;

	
	public FixBookUI(FixBookControl CoNtRoL) {
		this.CoNtRoL = CoNtRoL;
		InPuT = new Scanner(System.in);
		state = uIState.INITIALISED;
		CoNtRoL.SeT_Ui(this);
	}


	public void setState(uIState state) {
=======
	public static enum UIState { INITIALISED, READY, FIXING, COMPLETED };

	private fIX_bOOK_cONTROL control;
	private Scanner InPuT;
	private UIState state;

	
	public FixBookUI(fIX_bOOK_cONTROL control) {
		this.control = control;
		InPuT = new Scanner(System.in);
		state = UIState.INITIALISED;
		control.SeT_Ui(this);
	}


	public void setState(UIState state) {
>>>>>>> master
		this.state = state;
	}

	
	public void run() {
		OuTpUt("Fix Book Use Case UI\n");
		
		while (true) {
			
			switch (state) {
			
			case READY:
				String BoOk_EnTrY_StRiNg = iNpUt("Scan Book (<enter> completes): ");
				if (BoOk_EnTrY_StRiNg.length() == 0) 
					control.SCannING_COMplete();
				
				else {
					try {
						int BoOk_Id = Integer.valueOf(BoOk_EnTrY_StRiNg).intValue();
						control.BoOk_ScAnNeD(BoOk_Id);
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
				
				control.FiX_BoOk(FiX);
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
	

	public void dIsPlAy(Object object) {
		OuTpUt(object);
	}
	
	
}
