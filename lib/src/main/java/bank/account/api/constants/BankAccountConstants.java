package bank.account.api.constants;

public class BankAccountConstants {
	// Test Data
	public static final Long ACCOUNT_ID_TEST = 1L;
	public static final String APP_NAME_TEST = "bank-account-api";
	
	// Deposit transaction
	public static final double MAX_DEPOSIT_PER_TRANSACTION  = 20000;
    public static final double MAX_DEPOSIT_PER_DAY = 100000;
    
    // Withdrawal transaction
    public static final double MAX_WITHDRAWAL_PER_TRANSACTION  = 20000;
    public static final double MAX_WITHDRAWAL_PER_DAY = 60000;
}
