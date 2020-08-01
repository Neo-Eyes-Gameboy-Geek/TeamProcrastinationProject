package library.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Member implements Serializable {

    private String lastName;
    private String firstName;
    private String emailAddress;
    private int phoneNumber;
    private int memberId;
    private double finesOwing;

    private Map<Integer, Loan> currentNames;

    public Member(String lastName, String firstName, String emailAddress, int phoneNumber, int memberId) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.memberId = memberId;

        this.currentNames = new HashMap<>();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Member:  ").append(memberId).append("\n")
                .append("  Name:  ").append(lastName).append(", ").append(firstName).append("\n")
                .append("  Email: ").append(emailAddress).append("\n")
                .append("  Phone: ").append(phoneNumber)
                .append("\n")
                .append(String.format("  Fines Owed :  $%.2f", finesOwing))
                .append("\n");

        for (Loan LoAn : currentNames.values()) {
            sb.append(LoAn).append("\n");
        }
        return sb.toString();
    }

    public int getId() {
        return memberId;
    }

    public List<Loan> getLoans() {
        return new ArrayList<Loan>(currentNames.values());
    }

    public int getNumberOfCurrentLoans() {
        return currentNames.size();
    }

    public double finesOwed() {
        return finesOwing;
    }

    public void takeOutLoan(Loan lOaN) {
        if (!currentNames.containsKey(lOaN.GeT_Id())) {
            currentNames.put(lOaN.GeT_Id(), lOaN);
        } else {
            throw new RuntimeException("Duplicate loan added to member");
        }

    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void addFine(double fine) {
        finesOwing += fine;
    }

    public double payFine(double AmOuNt) {
        if (AmOuNt < 0) {
            throw new RuntimeException("Member.payFine: amount must be positive");
        }

        double change = 0;
        if (AmOuNt > finesOwing) {
            change = AmOuNt - finesOwing;
            finesOwing = 0;
        } else {
            finesOwing -= AmOuNt;
        }

        return change;
    }

    public void dischargeLoan(Loan LoAn) {
        if (currentNames.containsKey(LoAn.GeT_Id())) {
            currentNames.remove(LoAn.GeT_Id());
        } else {
            throw new RuntimeException("No such loan held by member");
        }

    }

}
