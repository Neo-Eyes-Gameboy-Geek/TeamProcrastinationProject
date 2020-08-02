package library.entities;

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
	
	private static final String lIbRaRyFiLe = "library.obj";
	private static final int LoanlImIt = 2;
	private static final int LoanPeriod = 2;
	private static final double FiNe_PeR_DaY = 1.0;
	private static final double maxfinesOwed = 1.0;
	private static final double damageFee = 2.0;
	
	private static Library SeLf;
	private int bOoK_Id;
	private int memberId;
	private int Loan_Id;
	private Date Loan_DaTe;
	
	private Map<Integer, Book> CaTaLoG;
	private Map<Integer, Member> MeMbErS;
	private Map<Integer, Loan> LoanS;
	private Map<Integer, Loan> currentNames;
	private Map<Integer, Book> DaMaGeD_BoOkS;
	

	private Library() {
		CaTaLoG = new HashMap<>();
		MeMbErS = new HashMap<>();
		LoanS = new HashMap<>();
		currentNames = new HashMap<>();
		DaMaGeD_BoOkS = new HashMap<>();
		bOoK_Id = 1;
		memberId = 1;		
		Loan_Id = 1;		
	}

	
	public static synchronized Library getInstance() {		
		if (SeLf == null) {
			Path PATH = Paths.get(lIbRaRyFiLe);			
			if (Files.exists(PATH)) {	
				try (ObjectInputStream LiBrArY_FiLe = new ObjectInputStream(new FileInputStream(lIbRaRyFiLe));) {
			    
					SeLf = (Library) LiBrArY_FiLe.readObject();
					Calendar.getInstance().SeT_DaTe(SeLf.Loan_DaTe);
					LiBrArY_FiLe.close();
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			else SeLf = new Library();
		}
		return SeLf;
	}

	
	public static synchronized void SaVe() {
		if (SeLf != null) {
			SeLf.Loan_DaTe = Calendar.getInstance().getDate();
			try (ObjectOutputStream LiBrArY_fIlE = new ObjectOutputStream(new FileOutputStream(lIbRaRyFiLe));) {
				LiBrArY_fIlE.writeObject(SeLf);
				LiBrArY_fIlE.flush();
				LiBrArY_fIlE.close();	
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	
	public int getBookId() {
		return bOoK_Id;
	}
	
	
	public int getMemberId() {
		return memberId;
	}
	
	
	private int gEt_NeXt_BoOk_Id() {
		return bOoK_Id++;
	}

	
	private int gEt_NeXt_memberId() {
		return memberId++;
	}

	
	private int gEt_NeXt_Loan_Id() {
		return Loan_Id++;
	}

	
	public List<Member> lIsT_MeMbErS() {		
		return new ArrayList<Member>(MeMbErS.values()); 
	}


	public List<Book> lIsT_BoOkS() {		
		return new ArrayList<Book>(CaTaLoG.values()); 
	}


	public List<Loan> lISt_currentNames() {
		return new ArrayList<Loan>(currentNames.values());
	}


	public Member aDd_MeMbEr(String lastName, String firstName, String email, int phoneNo) {		
		Member member = new Member(lastName, firstName, email, phoneNo, gEt_NeXt_memberId());
		MeMbErS.put(member.getId(), member);		
		return member;
	}

	
	public Book aDd_BoOk(String a, String t, String c) {		
		Book b = new Book(a, t, c, gEt_NeXt_BoOk_Id());
		CaTaLoG.put(b.getId(), b);		
		return b;
	}

	
	public Member getMember(int memberId) {
		if (MeMbErS.containsKey(memberId)) 
			return MeMbErS.get(memberId);
		return null;
	}

	
	public Book getBook(int bookId) {
		if (CaTaLoG.containsKey(bookId)) 
			return CaTaLoG.get(bookId);		
		return null;
	}

	
	public int gEt_Loan_LiMiT() {
		return LoanlImIt;
	}

	
	public boolean cAn_MeMbEr_BoRrOw(Member member) {		
		if (member.getNumberOfCurrentLoans() == LoanlImIt ) 
			return false;
				
		if (member.finesOwed() >= maxfinesOwed) 
			return false;
				
		for (Loan Loan : member.getLoans()) 
			if (Loan.isOverdue()) 
				return false;
			
		return true;
	}

	
	public int gEt_NuMbEr_Of_LoanS_ReMaInInG_FoR_MeMbEr(Member MeMbEr) {		
		return LoanlImIt - MeMbEr.getNumberOfCurrentLoans();
	}

	
	public Loan iSsUe_Loan(Book book, Member member) {
		Date dueDate = Calendar.getInstance().getDueDate(LoanPeriod);
		Loan Loan = new Loan(gEt_NeXt_Loan_Id(), book, member, dueDate);
		member.takeOutLoans(Loan);
		book.BoRrOw();
		LoanS.put(Loan.getId(), Loan);
		currentNames.put(book.getId(), Loan);
		return Loan;
	}
	
	
	public Loan GeT_Loan_By_BoOkId(int bookId) {
		if (currentNames.containsKey(bookId)) 
			return currentNames.get(bookId);
		
		return null;
	}

	
	public double CaLcUlAtE_OVERDUE_FiNe(Loan Loan) {
		if (Loan.isOverdue()) {
			long DaYs_OVERDUE = Calendar.getInstance().GeT_DaYs_DiFfErEnCe(Loan.getDueDate());
			double fInE = DaYs_OVERDUE * FiNe_PeR_DaY;
			return fInE;
		}
		return 0.0;		
	}


	public void discharge_Loan(Loan cUrReNt_Loan, boolean iS_dAmAgEd) {
		Member mEmBeR = cUrReNt_Loan.getMember();
		Book bOoK  = cUrReNt_Loan.getBook();
		
		double OVERDUE_FiNe = CaLcUlAtE_OVERDUE_FiNe(cUrReNt_Loan);
		mEmBeR.addFine(OVERDUE_FiNe);	
		
		mEmBeR.dischargeLoans(cUrReNt_Loan);
		bOoK.ReTuRn(iS_dAmAgEd);
		if (iS_dAmAgEd) {
			mEmBeR.addFine(damageFee);
			DaMaGeD_BoOkS.put(bOoK.getId(), bOoK);
		}
		cUrReNt_Loan.discharge();
		currentNames.remove(bOoK.getId());
	}


	public void cHeCk_currentNames() {
		for (Loan Loan : currentNames.values()) 
			Loan.checkOverdue();
				
	}


	public void RePaIr_BoOk(Book cUrReNt_BoOk) {
		if (DaMaGeD_BoOkS.containsKey(cUrReNt_BoOk.getId())) {
			cUrReNt_BoOk.RePaIr();
			DaMaGeD_BoOkS.remove(cUrReNt_BoOk.getId());
		}
		else 
			throw new RuntimeException("Library: repairBook: book is not damaged");
		
		
	}
	
	
}
