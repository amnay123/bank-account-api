package bank.account.api.web.controllers;

import java.io.IOException;
import java.util.Date;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import bank.account.api.constants.BankAccountConstants;
import bank.account.api.enums.TransactionType;
import bank.account.api.models.Account;
import bank.account.api.models.AccountTransaction;
import bank.account.api.services.AccountServiceImpl;
import bank.account.api.services.AccountTransactionServiceImpl;

public class CheckOperationsControllerTests {
	
	@Test
	public void givenAccountId_whenRequestIsExecuted_then200IsReceived()
	  throws ClientProtocolException, IOException {
	 
	    // Given
		HttpUriRequest request = new HttpGet( "http://localhost:8080/"+BankAccountConstants.APP_NAME_TEST+"/accounts/"
		+BankAccountConstants.ACCOUNT_ID_TEST);
		request.setHeader("Accept", "application/json");
		
	    // When
	    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

	    // Then
	    Assert.assertEquals(httpResponse.getCode(),
	     HttpStatus.SC_SUCCESS);
	}
	
	@Test
	public void 
	givenRequestWithNoAcceptHeader_whenRequestIsExecuted_thenDefaultResponseContentTypeIsJson()
	  throws ClientProtocolException, IOException {
	 
		// Given
		String jsonMimeType = "application/json";
		HttpUriRequest request = new HttpGet( "http://localhost:8080/"+BankAccountConstants.APP_NAME_TEST+"/accounts/"
		+BankAccountConstants.ACCOUNT_ID_TEST);

	   // When
	   HttpResponse response = HttpClientBuilder.create().build().execute( request );

	   // Then
	   Header contentType = response.getFirstHeader("Content-Type");
	   String mimeType = contentType.getValue();
	   Assert.assertEquals(jsonMimeType, mimeType );
	}
	
    @Test
    public void testSuccessfulOperationChecking() throws Exception {
    	// Given
    	AccountTransactionServiceImpl.accountTransactions.clear();
    	Account account = AccountServiceImpl.accounts.get(0);
    	AccountTransaction transaction = new AccountTransaction(account,TransactionType.WITHDRAWAL, 5000, new Date()); 
    	AccountTransactionServiceImpl.accountTransactions.add(transaction);
    	HttpUriRequest request = new HttpGet( "http://localhost:8080/"+BankAccountConstants.APP_NAME_TEST+"/accounts/"
    	+BankAccountConstants.ACCOUNT_ID_TEST);
    	request.setHeader("Accept", "application/json");
    	        
    	// When
    	HttpResponse response = HttpClientBuilder.create().build().execute( request );
    									
    	// Then
    	HttpEntity entity = ((CloseableHttpResponse)response).getEntity();
    	String result = EntityUtils.toString(entity);
    	Gson gson = new Gson();
    	String json = gson.toJson(transaction);
    	Assert.assertEquals("{\"success\":true,\"messages\":{\"message\":\""+json+"\",\"title\":\"\"},\"errors\":{},\"data\":{},\"httpResponseCode\":200}", result);
    }

}
