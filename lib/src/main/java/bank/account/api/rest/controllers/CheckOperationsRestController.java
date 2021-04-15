package bank.account.api.rest.controllers;

import java.util.Date;
import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import bank.account.api.models.AccountTransaction;
import bank.account.api.tools.DateTool;
import bank.account.api.tools.JsonResponseModel;
import bank.account.api.tools.JsonResponseModelImpl;

@RestController
@RequestMapping(value = "/")
public class CheckOperationsRestController extends BaseRestController {
	
	@RequestMapping(value="accounts/{id}", method = RequestMethod.GET)
    public @ResponseBody JsonResponseModel checkOperations(@PathVariable(name = "id",required = true) Long accountId) {
        
        JsonResponseModel jsonResponse = new JsonResponseModelImpl();
        
        try {
            
            // retrieve account operations
            List<AccountTransaction> operations  = transactionService.findBetweenDatesAndByType(accountId, null,
            		DateTool.getEndOfDay(new Date()), null);
                
            Gson gson = new Gson();
        	String json = gson.toJson(operations);
            jsonResponse.setSuccess(true, "", json);
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
