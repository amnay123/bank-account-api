package bank.account.api.rest.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import bank.account.api.services.AccountService;
import bank.account.api.services.AccountTransactionService;

public class BaseRestController {

	protected final Log logger = LogFactory.getLog(getClass());
    
    protected static final long ACCOUNT_ID = 1L;
    
    @Autowired
    protected AccountService accountService;
    
    @Autowired
    protected AccountTransactionService transactionService; 
    
}
