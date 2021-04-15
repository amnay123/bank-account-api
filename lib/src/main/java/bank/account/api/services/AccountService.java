package bank.account.api.services;

import bank.account.api.models.Account;

public interface AccountService {	
	Account findById(Long id);
	void save(Account account);
}
