package library.borrowbook;

import java.util.Scanner;

public class BorrowBookUI {

    public static enum UIState {
        INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED
    };

    private BorrowBookControl control;
    private Scanner input;
    private UIState state;

    public BorrowBookUI(BorrowBookControl control) {
        this.control = control;
        input = new Scanner(System.in);
        state = UIState.INITIALISED;
        control.setUI(this);
    }

    private String input(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }

    private void output(Object object) {
        System.out.println(object);
    }

    public void setState(UIState state) {
        this.state = state;
    }

    public void runUI() {
        output("Borrow Book Use Case UI\n");

        while (true) {

            switch (state) {

                case CANCELLED:
                    output("Borrowing Cancelled");
                    return;

                case READY:
                    String memberIdString = input("Swipe member card (press <enter> to cancel): ");
                    if (memberIdString.length() == 0) {
                        control.cancelBorrow();
                        break;
                    }
                    try {
                        int memberIdNumber = Integer.valueOf(memberIdString).intValue();
                        control.swipeCard(memberIdNumber);
                    } catch (NumberFormatException numberFormatException) {
                        output("Invalid Member Id");
                    }
                    break;

                case RESTRICTED:
                    input("Press <any key> to cancel");
                    control.cancelBorrow();
                    break;

                case SCANNING:
                    String bookIdString = input("Scan Book (<enter> completes): ");
                    if (bookIdString.length() == 0) {
                        control.completeBorrow();
                        break;
                    }
                    try {
                        int bookIdNumber = Integer.valueOf(bookIdString).intValue();
                        control.scanBook(bookIdNumber);

                    } catch (NumberFormatException numberFormatException) {
                        output("Invalid Book Id");
                    }
                    break;

                case FINALISING:
                    String answer = input("Commit loans? (Y/N): ");
                    if (answer.toUpperCase().equals("N")) {
                        control.cancelBorrow();

                    } else {
                        control.CommitLoans();
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

    public void displayUI(Object object) {
        output(object);
    }

}
