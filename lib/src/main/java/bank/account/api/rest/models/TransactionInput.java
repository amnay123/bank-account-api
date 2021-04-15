package bank.account.api.rest.models;

public class TransactionInput {
    
    private double amount;
    
    public TransactionInput() {}
    
    public TransactionInput(double amount) {
    	this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
