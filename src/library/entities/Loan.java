package library.entities;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class Loan implements Serializable {
	
	public static enum loanState { CURRENT, OVERDUE, DISCHARGED };
	
	private int loadId;
	private Book book;
	private Member member;
	private Date date;
	private loanState state;

	
	public Loan(int loanId, Book book, Member member, Date dueDate) {
		this.loadId = loanId;
		this.book = book;
		this.member = member;
		this.date = dueDate;
		this.state = loanState.CURRENT;
	}

	
	public void cHeCk_OvEr_DuE() {
		if (state == loanState.CURRENT &&
			Calendar.getInstance().getDate().after(date)) 
			this.state = loanState.OVERDUE;			
		
	}

	
	public boolean isOverdue() {
		return state == loanState.OVERDUE;
	}

	
	public Integer getId() {
		return loadId;
	}


	public Date GeT_DuE_DaTe() {
		return date;
	}
	
	
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		StringBuilder sb = new StringBuilder();
		sb.append("Loan:  ").append(loadId).append("\n")
		  .append("  Borrower ").append(member.getId()).append(" : ")
		  .append(member.getLastName()).append(", ").append(member.getFirstName()).append("\n")
		  .append("  Book ").append(book.getId()).append(" : " )
		  .append(book.gEtTiTlE()).append("\n")
		  .append("  DueDate: ").append(sdf.format(date)).append("\n")
		  .append("  State: ").append(state);		
		return sb.toString();
	}


	public Member gEt_MeMbEr() {
		return member;
	}


	public Book gEt_BoOk() {
		return book;
	}


	public void DiScHaRgE() {
		state = loanState.DISCHARGED;		
	}

}
