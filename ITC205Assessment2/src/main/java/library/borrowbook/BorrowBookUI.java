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

    private String input(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }

    private void output(Object object) {
        System.out.println(object);
    }

    public void setState(UiState state) {
        this.state = state;
    }

    public void runUi() {
        output("Borrow Book Use Case UI\n");

        while (true) {

            switch (state) {

                case CANCELLED:
                    output("Borrowing Cancelled");
                    return;

                case READY:
                    String memberIdString = input("Swipe member card (press <enter> to cancel): ");
                    if (memberIdString.length() == 0) {
                        control.CaNcEl();
                        break;
                    }
                    try {
                        int memberIdNumber = Integer.valueOf(memberIdString).intValue();
                        control.SwIpEd(memberIdNumber);
                    } catch (NumberFormatException nfe) {
                        output("Invalid Member Id");
                    }
                    break;

                case RESTRICTED:
                    input("Press <any key> to cancel");
                    control.CaNcEl();
                    break;

                case SCANNING:
                    String bookIdString = input("Scan Book (<enter> completes): ");
                    if (bookIdString.length() == 0) {
                        control.CoMpLeTe();
                        break;
                    }
                    try {
                        int bookIdNumber = Integer.valueOf(bookIdString).intValue();
                        control.ScAnNeD(bookIdNumber);

                    } catch (NumberFormatException nfe) {
                        output("Invalid Book Id");
                    }
                    break;

                case FINALISING:
                    String answer = input("Commit loans? (Y/N): ");
                    if (answer.toUpperCase().equals("N")) {
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
