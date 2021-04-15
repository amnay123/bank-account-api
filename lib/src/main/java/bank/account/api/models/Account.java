package bank.account.api.models;

public class Account {

	private Long id;
	private double amount;
	
	public Account() {
		this.amount = 0.0;
	}
	
	public Account(Long id, double amount) {
		this.id = id;
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}
