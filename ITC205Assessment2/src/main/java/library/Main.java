package library;

import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;

import library.borrowbook.BorrowBookUI;
import library.borrowbook.BorrowBookControl;
import library.entities.Book;
import library.entities.Calendar;
import library.entities.Library;
import library.entities.Loan;
import library.entities.Member;
import library.fixbook.FixBookUI;
import library.fixbook.FixBookControl;
import library.payfine.PayFineUI;
import library.payfine.PayFineControl;
import library.returnBook.ReturnBookUI;
import library.returnBook.ReturnBookControl;

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

            for (Member member : library.listMembers()) {
                output(member);
            }
            output(" ");
            for (Book book : library.listBooks()) {
                output(book);
            }

            menu = getMenu();

            boolean exit = false;

            while (!exit) {
                Date calenderDate = calender.getDate();
                output("\n" + simpleDateFormat.format(calenderDate));
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
        PayFineControl payFineControl = new PayFineControl();
        new PayFineUI(payFineControl).run();
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
        BorrowBookControl borrowBookControl = new BorrowBookControl();
        new BorrowBookUI(borrowBookControl).runUI();
    }


    private static void returnBook() {
        ReturnBookControl returnBookControl = new ReturnBookControl();
        new ReturnBookUI(returnBookControl).run();
    }

    private static void fixBooks() {
        FixBookControl fixBookControl = new FixBookControl();
        new FixBookUI(fixBookControl).run();
    }

    private static void incrementDate() {
        try {
            String numOfDays = input("Enter number of days: ");
            int days = Integer.valueOf(numOfDays).intValue();
            calender.incrementDate(days);
            library.checkCurrentLoans();
            Date calenderDate = calender.getDate();
            output(simpleDateFormat.format(calenderDate));

        } catch (NumberFormatException numberFormatException) {
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
            String phoNumString = input("Enter phone number: ");
            int phoneNumber = Integer.valueOf(phoNumString).intValue();
            Member member = library.addMember(lastName, firstName, emailAddress, phoneNumber);
            output("\n" + member + "\n");

        } catch (NumberFormatException numberFormatException) {
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
