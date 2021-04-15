package bank.account.api.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import bank.account.api.models.Account;

@Service("AccountService")
public class AccountServiceImpl implements AccountService {
	
	public static List<Account> accounts = new ArrayList<Account>();
	
	static {
		accounts.add(new Account(1L, 25000.0));
		accounts.add(new Account(2L, 35000.0));
	}

	@Override
	public Account findById(Long id) {
		Account acc = null;
		for (Account account : accounts) {
			if(account.getId().equals(id)) {
				acc = account;
				break;
			}
		}
		return acc;
	}

	@Override
	public void save(Account account) {
		if(account.getId() != null && !account.getId().equals(0)){
			Account acc = findById(account.getId());
			acc.setAmount(account.getAmount());
		}
		else {
			Account acc = accounts.get(accounts.size()-1);
			account.setId(acc.getId()+1);
			accounts.add(account);
		}
	}

}
