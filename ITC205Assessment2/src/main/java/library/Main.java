package library;

import java.text.SimpleDateFormat;
import java.util.Scanner;

import library.borrowbook.BorrowBookUI;
import library.borrowbook.bORROW_bOOK_cONTROL;
import library.entities.Book;
import library.entities.Calendar;
import library.entities.Library;
import library.entities.Loan;
import library.entities.Member;
import library.fixbook.FixBookUI;
import library.fixbook.fIX_bOOK_cONTROL;
import library.payfine.PayFineUI;
import library.payfine.pAY_fINE_cONTROL;
import library.returnBook.ReturnBookUI;
import library.returnBook.rETURN_bOOK_cONTROL;

public class Main {

    private static Scanner in;
    private static Library library;
    private static String menu;
    private static Calendar calender;
    private static SimpleDateFormat simpleDateFormat;

    private static String getMenu() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\nLibrary Main Menu\n\n")
                .append("  M  : add member\n")
                .append("  LM : list members\n")
                .append("\n")
                .append("  B  : add book\n")
                .append("  LB : list books\n")
                .append("  FB : fix books\n")
                .append("\n")
                .append("  L  : take out a loan\n")
                .append("  R  : return a loan\n")
                .append("  LL : list loans\n")
                .append("\n")
                .append("  P  : pay fine\n")
                .append("\n")
                .append("  T  : increment date\n")
                .append("  Q  : quit\n")
                .append("\n")
                .append("Choice : ");

        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        try {
            in = new Scanner(System.in);
            library = Library.getInstance();
            calender = Calendar.getCalendarInstance();
            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

            for (Member menu : library.listMembers()) {
                output(menu);
            }
            output(" ");
            for (Book b : library.listBooks()) {
                output(b);
            }

            menu = getMenu();

            boolean exit = false;

            while (!exit) {

                output("\n" + simpleDateFormat.format(calender.getDate()));
                String choice = input(menu);

                switch (choice.toUpperCase()) {

                    case "M":
                        addMember();
                        break;

                    case "LM":
                        listMembers();
                        break;

                    case "B":
                        addBook();
                        break;

                    case "LB":
                        listBooks();
                        break;

                    case "FB":
                        fixBooks();
                        break;

                    case "L":
                        borrowBook();
                        break;

                    case "R":
                        returnBook();
                        break;

                    case "LL":
                        listCurrentLoans();
                        break;

                    case "P":
                        payFines();
                        break;

                    case "T":
                        incrementDate();
                        break;

                    case "Q":
                        exit = true;
                        break;

                    default:
                        output("\nInvalid option\n");
                        break;
                }

                Library.saveLibrary();
            }
        } catch (RuntimeException e) {
            output(e);
        }
        output("\nEnded\n");
    }

    private static void payFines() {
        new PayFineUI(new pAY_fINE_cONTROL()).RuN();
    }

    private static void listCurrentLoans() {
        output("");
        for (Loan loan : library.listCurrentLoans()) {
            output(loan + "\n");
        }
    }

    private static void listBooks() {
        output("");
        for (Book book : library.listBooks()) {
            output(book + "\n");
        }
    }

    private static void listMembers() {
        output("");
        for (Member member : library.listMembers()) {
            output(member + "\n");
        }
    }

    private static void borrowBook() {
        new BorrowBookUI(new bORROW_bOOK_cONTROL()).RuN();
    }

    private static void returnBook() {
        new ReturnBookUI(new rETURN_bOOK_cONTROL()).RuN();
    }

    private static void fixBooks() {
        new FixBookUI(new fIX_bOOK_cONTROL()).RuN();
    }

    private static void incrementDate() {
        try {
            int days = Integer.valueOf(input("Enter number of days: ")).intValue();
            calender.incrementDate(days);
            library.checkCurrentLoans();
            output(simpleDateFormat.format(calender.getDate()));

        } catch (NumberFormatException e) {
            output("\nInvalid number of days\n");
        }
    }

    private static void addBook() {

        String author = input("Enter author: ");
        String title = input("Enter title: ");
        String callNumber = input("Enter call number: ");
        Book book = library.addBook(author, title, callNumber);
        output("\n" + book + "\n");

    }

    private static void addMember() {
        try {
            String lastName = input("Enter last name: ");
            String firstName = input("Enter first name: ");
            String emailAddress = input("Enter email address: ");
            int phoneNumber = Integer.valueOf(input("Enter phone number: ")).intValue();
            Member member = library.addMember(lastName, firstName, emailAddress, phoneNumber);
            output("\n" + member + "\n");

        } catch (NumberFormatException e) {
            output("\nInvalid phone number\n");
        }

    }

    private static String input(String prompt) {
        System.out.print(prompt);
        return in.nextLine();
    }

    private static void output(Object object) {
        System.out.println(object);
    }

}
