package bank.account.api.rest.controllers;

import java.util.Date;
import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import bank.account.api.constants.BankAccountConstants;
import bank.account.api.enums.TransactionType;
import bank.account.api.models.Account;
import bank.account.api.models.AccountTransaction;
import bank.account.api.rest.models.TransactionInput;
import bank.account.api.tools.DateTool;
import bank.account.api.tools.JsonResponseModelImpl;



@RestController
@RequestMapping("/")
public class DepositRestController extends BaseRestController { 
    
    @RequestMapping(value="accounts/{id}/deposit", method = RequestMethod.POST)
    public @ResponseBody JsonResponseModelImpl doDeposit(@RequestBody TransactionInput transaction,
    		@PathVariable(name = "id",required = true) Long accountId) {
        
    	JsonResponseModelImpl jsonResponse = new JsonResponseModelImpl();
        
        try {
            
            double total = 0;
            
            // deposited exceeds the MAX_DEPOSIT_PER_DAY (max per day)
            // using the existing transactions list
            List<AccountTransaction> deposits  = transactionService.findBetweenDatesAndByType(accountId, DateTool.getStartOfDay(new Date()),
            		DateTool.getEndOfDay(new Date()), TransactionType.DEPOSIT);
            
            if (deposits.size() > 0) {
                for (AccountTransaction accountTransaction: deposits) {
                    total+=accountTransaction.getAmount(); 
                }
                if (total  + transaction.getAmount()  > BankAccountConstants.MAX_DEPOSIT_PER_DAY) {
                    jsonResponse.setSuccess(false, "Error", "Deposit for the day should not be more than $100K");
                    jsonResponse.setHttpResponseCode(HttpStatus.SC_NOT_ACCEPTABLE);
                    return jsonResponse;
                }
            }
            
            // deposited exceeds the MAX_DEPOSIT_PER_TRANSACTION (max per transaction)
            if(transaction.getAmount() > BankAccountConstants.MAX_DEPOSIT_PER_TRANSACTION) {                
                jsonResponse.setSuccess(false, "Error", "Deposit per transaction should not be more than $20K");
                jsonResponse.setHttpResponseCode(HttpStatus.SC_NOT_ACCEPTABLE);
                return jsonResponse;
            }
            
            // SuccessfulDeposit
            Account account = accountService.findById(accountId);
           	AccountTransaction accountTransaction = new AccountTransaction(account, TransactionType.DEPOSIT, transaction.getAmount(), new Date());
            double amount  = transactionService.save(accountTransaction);
                
            double newBalance = account.getAmount() + amount;
            account.setAmount(newBalance);
            accountService.save(account);
                
            jsonResponse.setSuccess(true, "", "Deposit sucessfully Transacted");
            jsonResponse.setHttpResponseCode(HttpStatus.SC_OK);
            
        } catch (Exception e) {
            logger.error("exception", e);
            jsonResponse.setSuccess(false, JsonResponseModelImpl.DEFAULT_MSG_TITLE_VALUE, JsonResponseModelImpl.DEFAULT_MSG_NAME_VALUE);
            jsonResponse.setHttpResponseCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return jsonResponse;
        }
        
        return jsonResponse;
    }
    
}
