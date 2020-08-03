package library.entities;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class Loan implements Serializable {
	
	public static enum lOaN_sTaTe { CURRENT, OVER_DUE, DISCHARGED };
	
	private int LoAn_Id;
	private Book BoOk;
	private Member MeMbEr;
	private Date DaTe;
	private lOaN_sTaTe StAtE;

	
	public Loan(int loanId, Book bOoK, Member mEmBeR, Date DuE_dAtE) {
		this.LoAn_Id = loanId;
		this.BoOk = bOoK;
		this.MeMbEr = mEmBeR;
		this.DaTe = DuE_dAtE;
		this.StAtE = lOaN_sTaTe.CURRENT;
	}

	
	public void checkOverDue() {
		if (StAtE == lOaN_sTaTe.CURRENT &&
			Calendar.getInstance().getDate().after(DaTe)) 
			this.StAtE = lOaN_sTaTe.OVER_DUE;			
		
	}

	
	public boolean isOverDue() {
		return StAtE == lOaN_sTaTe.OVER_DUE;
	}

	
	public Integer getId() {
		return LoAn_Id;
	}


	public Date getDueDate() {
		return DaTe;
	}
	
	
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		StringBuilder sb = new StringBuilder();
		sb.append("Loan:  ").append(LoAn_Id).append("\n")
		  .append("  Borrower ").append(MeMbEr.getId()).append(" : ")
		  .append(MeMbEr.GeT_LaSt_NaMe()).append(", ").append(MeMbEr.GeT_FiRsT_NaMe()).append("\n")
		  .append("  Book ").append(BoOk.getId()).append(" : " )
		  .append(BoOk.gEtTiTlE()).append("\n")
		  .append("  DueDate: ").append(sdf.format(DaTe)).append("\n")
		  .append("  State: ").append(StAtE);		
		return sb.toString();
	}


	public Member getMember() {
		return MeMbEr;
	}


	public Book getBook() {
		return BoOk;
	}


	public void discharge() {
		StAtE = lOaN_sTaTe.DISCHARGED;		
	}

}
