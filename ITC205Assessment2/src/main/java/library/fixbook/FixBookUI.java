package library.fixbook;

import java.util.Scanner;

public class FixBookUI {

    public static enum UIState {
        INITIALISED, READY, FIXING, COMPLETED
    };

    private FixBookControl control;
    private Scanner input;
    private UIState state;

    public FixBookUI(FixBookControl control) {
        this.control = control;
        input = new Scanner(System.in);
        state = UIState.INITIALISED;
        control.SeT_Ui(this);
    }

    public void setState(UIState state) {
        this.state = state;
    }

    public void run() {
        output("Fix Book Use Case UI\n");

        while (true) {

            switch (state) {

                case READY:
                    String bookEntityString = input("Scan Book (<enter> completes): ");
                    if (bookEntityString.length() == 0) {
                        control.SCannING_COMplete();
                    } else {
                        try {
                            int bookId = Integer.valueOf(bookEntityString).intValue();
                            control.BoOk_ScAnNeD(bookId);
                        } catch (NumberFormatException numberFormatException) {
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

                    control.FiX_BoOk(fix);
                    break;

                case COMPLETED:
                    output("Fixing process complete");
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

}
