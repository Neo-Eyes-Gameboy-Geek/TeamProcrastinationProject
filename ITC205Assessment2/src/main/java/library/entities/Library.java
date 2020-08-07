package main.java.library.entities;

import main.java.library.entities.Member;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Library implements Serializable {


    private static final String LIBRARY_FILE = "library.obj";
    private static final int LOAN_LIMIT = 2;
    private static final int LOAN_PERIOD = 2;
    private static final double FINE_PER_DAY = 1.0;
    private static final double MAX_FINES_OWED = 1.0;
    private static final double DAMAGE_FEE = 2.0;

    private static Library self;
    private int bookId;
    private int memberId;
    private int loanId;
    private Date loanDate;

    private Map<Integer, Book> catalog;
    private Map<Integer, Member> members;
    private Map<Integer, Loan> loans;
    private Map<Integer, Loan> currentLoans;
    private Map<Integer, Book> damagedBooks;

    private Library() {
        catalog = new HashMap<>();
        members = new HashMap<>();
        loans = new HashMap<>();
        currentLoans = new HashMap<>();
        damagedBooks = new HashMap<>();
        bookId = 1;
        memberId = 1;
        loanId = 1;
    }

    public static synchronized Library getInstance() {
        if (self == null) {
            Path path = Paths.get(LIBRARY_FILE);
            if (Files.exists(path)) {
                try (ObjectInputStream libraryFile = new ObjectInputStream(new FileInputStream(LIBRARY_FILE));) {

                    self = (Library) libraryFile.readObject();
                    Calendar.getCalendarInstance().setDate(self.loanDate);
                    libraryFile.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                self = new Library();
            }
        }
        return self;
    }

    public static synchronized void saveLibrary() {
        if (self != null) {
            self.loanDate = Calendar.getCalendarInstance().getDate();
            try (ObjectOutputStream libraryFile = new ObjectOutputStream(new FileOutputStream(LIBRARY_FILE));) {
                libraryFile.writeObject(self);
                libraryFile.flush();
                libraryFile.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int getBookId() {
        return bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    private int getNextBookId() {
        return bookId++;
    }

    private int getNextMemberId() {
        return memberId++;
    }

    private int getNextLoanId() {
        return loanId++;
    }

    public List<Member> listMembers() {
        return new ArrayList<Member>(members.values());
    }

    public List<Book> listBooks() {
        return new ArrayList<Book>(catalog.values());
    }

    public List<Loan> listCurrentLoans() {
        return new ArrayList<Loan>(currentLoans.values());
    }

    public Member addMember(String lastName, String firstName, String email, int phoneNo) {
        int nextId = getNextMemberId();
        Member member = new Member(lastName, firstName, email, phoneNo, nextId);
        int memberId = member.getId();
        members.put(memberId, member);
        return member;
    }

    public Book addBook(String author, String title, String callNumber) {
        int nextId = getNextBookId();
        Book book = new Book(author, title, callNumber, nextId);
        int bookId = book.getId();
        catalog.put(bookId, book);
        return book;
    }

    public Member getMember(int memberId) {
        if (members.containsKey(memberId)) {
            return members.get(memberId);
        }
        return null;
    }

    public Book getBook(int bookId) {
        if (catalog.containsKey(bookId)) {
            return catalog.get(bookId);
        }
        return null;
    }

    public int getLoanLimit() {
        return LOAN_LIMIT;
    }

    public boolean canMemberBorrow(Member member) {
        if (member.getNumberOfCurrentLoans()== LOAN_LIMIT) {
            return false;
        }

        if (member.finesOwed()>= MAX_FINES_OWED) {
            return false;
        }

        for (Loan loan : member.getLoans()) {
            if (loan.isBookOverdue()) {
                return false;
            }
        }

        return true;
    }

    public int getNumberOfLoansRemainingForMember(Member member) {
        return LOAN_LIMIT - member.getNumberOfCurrentLoans();
    }

    public Loan issueLoan(Book book, Member member) {
        Date dueDate = Calendar.getCalendarInstance().getDueDate(LOAN_PERIOD);
        int nextId = getNextLoanId();
        Loan loan = new Loan(nextId, book, member, dueDate);
        member.takeOutLoan(loan);
        book.borrowBook();
        int loanId = loan.getId();
        loans.put(loanId, loan);
        int bookId = book.getId();
        currentLoans.put(bookId, loan);
        return loan;
    }

    public Loan getLoanByBookId(int bookId) {
        if (currentLoans.containsKey(bookId)) {
            return currentLoans.get(bookId);
        }

        return null;
    }

    public double calculateOverDueFine(Loan loan) {
        if (loan.isBookOverdue()) {
            Date dueDate = loan.getDueDate();
            long daysOverDue = Calendar.getCalendarInstance().getDaysDifference(dueDate);
            double fine = daysOverDue * FINE_PER_DAY;
            return fine;
        }
        return 0.0;
    }

    public void dischargeLoan(Loan currentLoan, boolean isDamaged) {
        Member member = currentLoan.getMember();
        Book book = currentLoan.getBook();

        double overDueFine = calculateOverDueFine(currentLoan);
        member.addFine(overDueFine);

        member.dischargeLoan(currentLoan);
        book.returnBook(isDamaged);
        if (isDamaged) {
            member.addFine(DAMAGE_FEE);
            int damagedBookId = book.getId();
            damagedBooks.put(damagedBookId, book);
        }
        currentLoan.dischargeLoan();
        int bookId = book.getId();
        currentLoans.remove(bookId);
    }

    public void checkCurrentLoans() {
        for (Loan loan : currentLoans.values()) {
            loan.checkOverDue();
        }

    }

    public void repairBook(Book currentBook) {
        int bookId = currentBook.getId();
        if (damagedBooks.containsKey(bookId)) {
                currentBook.repairBook();
            damagedBooks.remove(bookId);
        } else {
            throw new RuntimeException("Library: repairBook: book is not damaged");
        }

    }
}
