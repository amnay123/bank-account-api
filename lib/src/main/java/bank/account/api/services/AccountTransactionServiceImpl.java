package bank.account.api.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import bank.account.api.enums.TransactionType;
import bank.account.api.models.Account;
import bank.account.api.models.AccountTransaction;

@Service("AccountTransactionService")
public class AccountTransactionServiceImpl implements AccountTransactionService {
	
	public static List<AccountTransaction> accountTransactions = new ArrayList<AccountTransaction>();
	
	static {
		Account firstAccount = AccountServiceImpl.accounts.get(0);
		accountTransactions.add(new AccountTransaction(firstAccount, TransactionType.DEPOSIT, 5000, new Date()));
		accountTransactions.add(new AccountTransaction(firstAccount, TransactionType.DEPOSIT, 4000, new Date()));
	}

	@Override
	public List<AccountTransaction> findBetweenDatesAndByType(Long id, Date from, Date to, TransactionType type) {
		List<AccountTransaction> accountTransactionsResult = new ArrayList<>();
		for (AccountTransaction accountTransaction : accountTransactions) {
			if((from != null && type != null && 
					accountTransaction.getAccount().getId().equals(id) &&
					accountTransaction.getType().equals(type) && 
					accountTransaction.getDate().compareTo(from) >= 0 && 
					accountTransaction.getDate().compareTo(to) <= 0) ||
					accountTransaction.getAccount().getId().equals(id))
				accountTransactionsResult.add(accountTransaction);
		}
		return accountTransactionsResult;
	}

	@Override
	public double save(AccountTransaction accountTransaction) {
		accountTransactions.add(accountTransaction);
		return accountTransaction.getAmount();
	}

}
