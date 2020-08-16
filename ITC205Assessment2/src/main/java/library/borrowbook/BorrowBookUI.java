package library.borrowbook;

import java.util.Scanner;

public class BorrowBookUI {

    public static enum UiState {
        INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED
    };

    private bORROW_bOOK_cONTROL control;
    private Scanner input;
    private UiState state;

    public BorrowBookUI(bORROW_bOOK_cONTROL control) {
        this.control = control;
        input = new Scanner(System.in);
        state = UiState.INITIALISED;
        control.SeT_Ui(this);
    }

    private String input(String PrOmPt) {
        System.out.print(PrOmPt);
        return input.nextLine();
    }

    private void output(Object ObJeCt) {
        System.out.println(ObJeCt);
    }

    public void setState(UiState StAtE) {
        this.state = StAtE;
    }

    public void runUi() {
        output("Borrow Book Use Case UI\n");

        while (true) {

            switch (state) {

                case CANCELLED:
                    output("Borrowing Cancelled");
                    return;

                case READY:
                    String MEM_STR = input("Swipe member card (press <enter> to cancel): ");
                    if (MEM_STR.length() == 0) {
                        control.CaNcEl();
                        break;
                    }
                    try {
                        int MeMbEr_Id = Integer.valueOf(MEM_STR).intValue();
                        control.SwIpEd(MeMbEr_Id);
                    } catch (NumberFormatException e) {
                        output("Invalid Member Id");
                    }
                    break;

                case RESTRICTED:
                    input("Press <any key> to cancel");
                    control.CaNcEl();
                    break;

                case SCANNING:
                    String BoOk_StRiNg_InPuT = input("Scan Book (<enter> completes): ");
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
                    String AnS = input("Commit loans? (Y/N): ");
                    if (AnS.toUpperCase().equals("N")) {
                        control.CaNcEl();

                    } else {
                        control.CoMmIt_LoAnS();
                        input("Press <any key> to complete ");
                    }
                    break;

                case COMPLETED:
                    output("Borrowing Completed");
                    return;

                default:
                    output("Unhandled state");
                    throw new RuntimeException("BorrowBookUI : unhandled state :" + state);
            }
        }
    }

    public void displayUi(Object object) {
        output(object);
    }

}
