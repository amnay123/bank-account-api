package bank.account.api.services;

import java.util.Date;
import java.util.List;

import bank.account.api.enums.TransactionType;
import bank.account.api.models.AccountTransaction;

public interface AccountTransactionService {
	List<AccountTransaction> findBetweenDatesAndByType(Long id, Date from, Date to, TransactionType type);
	double save(AccountTransaction accountTransaction);
}
