package main.java.library.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class Loan implements Serializable {
    public static enum LoanState {
        CURRENT, OVER_DUE, DISCHARGED
    };

    private int loanId;
    private Book book;
    private Member member;
    private Date date;
    private LoanState state;

    public Loan(int loanId, Book book, Member member, Date dueDate) {
        this.loanId = loanId;
        this.book = book;
        this.member = member;
        this.date = dueDate;
        this.state = LoanState.CURRENT;
    }

    public void checkOverDue() {
        if (state == LoanState.CURRENT
                && Calendar.getCalendarInstance().getDate().after(date)) {
            this.state = LoanState.OVER_DUE;
        }

    }

    public boolean isBookOverdue() {
        return state == LoanState.OVER_DUE;
    }

    public Integer getId() {
        return loanId;
    }

    public Date getDueDate() {
        return date;
    }

    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Loan:  ").append(loanId).append("\n")
                .append("  Borrower ").append(member.GeT_ID()).append(" : ")
                .append(member.GeT_LaSt_NaMe()).append(", ").append(member.GeT_FiRsT_NaMe()).append("\n")
                .append("  Book ").append(book.getId()).append(" : ")
                .append(book.getTitle()).append("\n")
                .append("  DueDate: ").append(simpleDateFormat.format(date)).append("\n")
                .append("  State: ").append(state);
        return stringBuilder.toString();
    }

    public Member getMember() {
        return member;
    }

    public Book getBook() {
        return book;
    }

    public void dischargeLoan() {
        state = LoanState.DISCHARGED;
    }

}
