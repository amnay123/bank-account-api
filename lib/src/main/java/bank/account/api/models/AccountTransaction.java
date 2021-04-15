package bank.account.api.models;

import java.util.Date;

import bank.account.api.enums.TransactionType;

public class AccountTransaction {

	private Long id;
	private Account account;
	private TransactionType type;
	private double amount;
	private Date date;
	
	public AccountTransaction(Account account, TransactionType type, double amount, Date date) {
		this.account = account;
		this.type = type;
		this.amount = amount;
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
