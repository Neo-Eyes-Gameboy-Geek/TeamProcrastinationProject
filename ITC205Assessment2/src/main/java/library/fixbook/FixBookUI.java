package library.fixbook;

import java.util.Scanner;

public class FixBookUI {

    public static enum uIState {
        INITIALISED, READY, FIXING, COMPLETED
    };

    private FixBookControl CONTROL;
    private Scanner INPUT;
    private uIState STATE;

    public FixBookUI(FixBookControl CONTROL) {
        this.CONTROL = CONTROL;
        INPUT = new Scanner(System.in);
        STATE = uIState.INITIALISED;
        CONTROL.setUI(this);
    }

    public void setState(uIState state) {
        this.STATE = state;
    }

    public void run() {
        output("Fix Book Use Case UI\n");

        while (true) {

            switch (STATE) {

                case READY:
                    String bookEntityString = input("Scan Book (<enter> completes): ");
                    if (bookEntityString.length() == 0) {
                        CONTROL.scanningComplete();
                    } else {
                        try {
                            int bookId = Integer.valueOf(bookEntityString).intValue();
                            CONTROL.bookScanned(bookId);
                        } catch (NumberFormatException e) {
                            output("Invalid bookId");
                        }
                    }
                    break;

                case FIXING:
                    String answer = input("Fix Book? (Y/N) : ");
                    boolean fix = false;
                    if (answer.toUpperCase().equals("Y")) {
                        fix = true;
                    }

                    CONTROL.fixBook(fix);
                    break;

                case COMPLETED:
                    output("Fixing process complete");
                    return;

                default:
                    output("Unhandled state");
                    throw new RuntimeException("FixBookUI : unhandled state :" + STATE);

            }
        }

    }

    private String input(String prompt) {
        System.out.print(prompt);
        return INPUT.nextLine();
    }

    private void output(Object object) {
        System.out.println(object);
    }

    public void display(Object object) {
        output(object);
    }

}
