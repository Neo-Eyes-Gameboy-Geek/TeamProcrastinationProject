package library.borrowbook;
import java.util.ArrayList;
import java.util.List;

import library.entities.Book;
import library.entities.Library;
import library.entities.Loan;
import library.entities.Member;

public class bORROW_bOOK_cONTROL {
	
	private BorrowBookUI uI;
	
	private Library lIbRaRy;
	private Member mEmBeR;
	private enum CONTROL_STATE { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };
	private CONTROL_STATE sTaTe;
	
	private List<Book> pEnDiNg_LiSt;
	private List<Loan> cOmPlEtEd_LiSt;
	private Book bOoK;
	
	
	public bORROW_bOOK_cONTROL() {
		this.lIbRaRy = Library.getInstance();
		sTaTe = CONTROL_STATE.INITIALISED;
	}
	

	public void SeT_Ui(BorrowBookUI Ui) {
		if (!sTaTe.equals(CONTROL_STATE.INITIALISED)) 
			throw new RuntimeException("BorrowBookControl: cannot call setUI except in INITIALISED state");
			
		this.uI = Ui;
		Ui.setState(BorrowBookUI.UIState.READY);
		sTaTe = CONTROL_STATE.READY;		
	}

		
	public void SwIpEd(int mEmBeR_Id) {
		if (!sTaTe.equals(CONTROL_STATE.READY)) 
			throw new RuntimeException("BorrowBookControl: cannot call cardSwiped except in READY state");
			
		mEmBeR = lIbRaRy.getMember(mEmBeR_Id);
		if (mEmBeR == null) {
			uI.displayUi("Invalid memberId");
			return;
		}
		if (lIbRaRy.canMemberBorrow(mEmBeR)) {
			pEnDiNg_LiSt = new ArrayList<>();
			uI.setState(BorrowBookUI.UIState.SCANNING);
			sTaTe = CONTROL_STATE.SCANNING; 
		}
		else {
			uI.displayUi("Member cannot borrow at this time");
			uI.setState(BorrowBookUI.UIState.RESTRICTED); 
		}
	}
	
	
	public void ScAnNeD(int bOoKiD) {
		bOoK = null;
		if (!sTaTe.equals(CONTROL_STATE.SCANNING)) 
			throw new RuntimeException("BorrowBookControl: cannot call bookScanned except in SCANNING state");
			
		bOoK = lIbRaRy.getBook(bOoKiD);
		if (bOoK == null) {
			uI.displayUi("Invalid bookId");
			return;
		}
		if (!bOoK.isAvailable()) {
			uI.displayUi("Book cannot be borrowed");
			return;
		}
		pEnDiNg_LiSt.add(bOoK);
		for (Book B : pEnDiNg_LiSt) 
			uI.displayUi(B.toString());
		
		if (lIbRaRy.getNumberOfLoansRemainingForMember(mEmBeR) - pEnDiNg_LiSt.size() == 0) {
			uI.displayUi("Loan limit reached");
			CoMpLeTe();
		}
	}
	
	
	public void CoMpLeTe() {
		if (pEnDiNg_LiSt.size() == 0) 
			CaNcEl();
		
		else {
			uI.displayUi("\nFinal Borrowing List");
			for (Book bOoK : pEnDiNg_LiSt) 
				uI.displayUi(bOoK.toString());
			
			cOmPlEtEd_LiSt = new ArrayList<Loan>();
			uI.setState(BorrowBookUI.UIState.FINALISING);
			sTaTe = CONTROL_STATE.FINALISING;
		}
	}


	public void CoMmIt_LoAnS() {
		if (!sTaTe.equals(CONTROL_STATE.FINALISING)) 
			throw new RuntimeException("BorrowBookControl: cannot call commitLoans except in FINALISING state");
			
		for (Book B : pEnDiNg_LiSt) {
			Loan lOaN = lIbRaRy.issueLoan(B, mEmBeR);
			cOmPlEtEd_LiSt.add(lOaN);			
		}
		uI.displayUi("Completed Loan Slip");
		for (Loan LOAN : cOmPlEtEd_LiSt) 
			uI.displayUi(LOAN.toString());
		
		uI.setState(BorrowBookUI.UIState.COMPLETED);
		sTaTe = CONTROL_STATE.COMPLETED;
	}

	
	public void CaNcEl() {
		uI.setState(BorrowBookUI.UIState.CANCELLED);
		sTaTe = CONTROL_STATE.CANCELLED;
	}
	
	
}
