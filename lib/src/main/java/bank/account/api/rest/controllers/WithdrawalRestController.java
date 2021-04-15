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
import bank.account.api.tools.JsonResponseModel;
import bank.account.api.tools.JsonResponseModelImpl;

@RestController
@RequestMapping(value = "/")
public class WithdrawalRestController extends BaseRestController {
	
	@RequestMapping(value="accounts/{id}/withdrawal", method = RequestMethod.POST)
    public @ResponseBody JsonResponseModel doWithDrawal(@RequestBody TransactionInput transactionInput,
    		@PathVariable(name = "id",required = true) Long accountId) {
        
        JsonResponseModel jsonResponse = new JsonResponseModelImpl();
        
        try {
            
            double total = 0;
            
            // check sufficient amount
            Account account = accountService.findById(accountId);
            double balance = account.getAmount();
            if (transactionInput.getAmount() > balance) {
            	jsonResponse.setSuccess(false, "Error", "You have insufficient amount");
                jsonResponse.setHttpResponseCode(HttpStatus.SC_NOT_ACCEPTABLE);
                return jsonResponse;
            }
            
            
            // withdrawn exceeds the MAX_WITHDRAWAL_PER_DAY (max per day)
            List<AccountTransaction> withdrawals  = transactionService.findBetweenDatesAndByType(accountId, DateTool.getStartOfDay(new Date()),
            		DateTool.getEndOfDay(new Date()), TransactionType.WITHDRAWAL);
            
            if (withdrawals.size() > 0) {
                for (AccountTransaction accountTransaction: withdrawals) {
                    total+=accountTransaction.getAmount(); 
                }
                if (total + transactionInput.getAmount() > BankAccountConstants.MAX_WITHDRAWAL_PER_DAY) {
                    jsonResponse.setSuccess(false, "Error", "Withdrawal per day should not be more than $60K");
                    jsonResponse.setHttpResponseCode(HttpStatus.SC_NOT_ACCEPTABLE);
                    return jsonResponse;
                }
            }
            
            // withdrawn exceeds the MAX_WITHDRAWAL_PER_TRANSACTION (max per transaction)
            if(transactionInput.getAmount() > BankAccountConstants.MAX_WITHDRAWAL_PER_TRANSACTION) {                
                jsonResponse.setSuccess(false, "Error", "Withdrawal per transaction should not be more than $20K");
                jsonResponse.setHttpResponseCode(HttpStatus.SC_NOT_ACCEPTABLE);
                return jsonResponse;
            }
            
            
            AccountTransaction accountTransaction = new AccountTransaction(account, TransactionType.WITHDRAWAL, transactionInput.getAmount(), new Date());
            double amount  = transactionService.save(accountTransaction);
                   
            double newBalance = account.getAmount() - amount;
            account.setAmount(newBalance);
            accountService.save(account);
                
            jsonResponse.setSuccess(true, "", "Withdrawal sucessfully Transacted");
            jsonResponse.setHttpResponseCode(HttpStatus.SC_OK);  
            
        } catch (Exception e) {
            logger.error("exception", e);
            jsonResponse.setSuccess(false, JsonResponseModel.DEFAULT_MSG_TITLE_VALUE, JsonResponseModel.DEFAULT_MSG_NAME_VALUE);
            jsonResponse.setHttpResponseCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return jsonResponse;
        }
        
        return jsonResponse;
    }
	
}
