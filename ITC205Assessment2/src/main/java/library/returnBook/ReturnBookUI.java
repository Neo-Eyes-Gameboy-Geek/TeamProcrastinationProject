package library.returnBook;

import java.util.Scanner;

public class ReturnBookUI {

    public static enum UIState {
        INITIALISED, READY, INSPECTING, COMPLETED
    };

    private ReturnBookControl control;
    private Scanner input;
    private UIState state;

    public ReturnBookUI(ReturnBookControl control) {
        this.control = control;
        input = new Scanner(System.in);
        state = UIState.INITIALISED;
        control.setUI(this);
    }

    public void run() {
        output("Return Book Use Case UI\n");

        while (true) {

            switch (state) {

                case INITIALISED:
                    break;

                case READY:
                    String bookInputString = input("Scan Book (<enter> completes): ");
                    if (bookInputString.length() == 0) {
                        control.scanningComplete();
                    } else {
                        try {
                            int bookId = Integer.valueOf(bookInputString).intValue();
                            control.bookScanned(bookId);
                        } catch (NumberFormatException numberFormatException) {
                            output("Invalid bookId");
                        }
                    }
                    break;

                case INSPECTING:
                    String answer = input("Is book damaged? (Y/N): ");
                    boolean isDamaged = false;
                    answer.toUpperCase();
                    if (answer.equals("Y")) {
                        isDamaged = true;
                    }

                    control.dischargeLoan(isDamaged);

                case COMPLETED:
                    output("Return processing complete");
                    return;

                default:
                    output("Unhandled state");
                    throw new RuntimeException("ReturnBookUI : unhandled state :" + state);
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

    public void setState(UIState state) {
        this.state = state;
    }
}
