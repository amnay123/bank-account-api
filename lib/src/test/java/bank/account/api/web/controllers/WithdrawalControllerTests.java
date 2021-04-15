package bank.account.api.web.controllers;


import java.io.IOException;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import bank.account.api.constants.BankAccountConstants;
import bank.account.api.rest.models.TransactionInput;

public class WithdrawalControllerTests {

	@Test
	public void givenAccountWithdrawalAmount_whenWithdrawalTransactionIsHappened_then200IsReceived()
	  throws ClientProtocolException, IOException {
	 
	    // Given
		TransactionInput transaction = new TransactionInput(4000); // withdrawal amount
        Gson gson = new Gson();
        String json = gson.toJson(transaction);
		HttpUriRequest request = new HttpPost( "http://localhost:8080/"+BankAccountConstants.APP_NAME_TEST+"/accounts/"
        +BankAccountConstants.ACCOUNT_ID_TEST+"/withdrawal/");
		request.setEntity(new StringEntity(json));
		request.setHeader("Content-Type", "application/json");
		request.setHeader("Accept", "application/json");

	    // When
	    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

	    // Then
	    Assert.assertEquals(httpResponse.getCode(),
	     HttpStatus.SC_SUCCESS);
	}
	
	@Test
	public void 
	givenWithdrawalRequestWithNoAcceptHeader_whenWithdrawalRequestIsExecuted_thenDefaultResponseContentTypeIsJson()
	  throws ClientProtocolException, IOException {
	 
	   // Given
	   String jsonMimeType = "application/json";
	   TransactionInput transaction = new TransactionInput(4000); // withdrawal amount
       Gson gson = new Gson();
       String json = gson.toJson(transaction);
       HttpUriRequest request = new HttpPost( "http://localhost:8080/"+BankAccountConstants.APP_NAME_TEST+"/accounts/"
       +BankAccountConstants.ACCOUNT_ID_TEST+"/withdrawal/");
       request.setHeader("Content-Type", "application/json");
	   request.setEntity(new StringEntity(json));
       
	   // When
	   HttpResponse response = HttpClientBuilder.create().build().execute( request );

	   // Then
	   Header contentType = response.getFirstHeader("Content-Type");
	   String mimeType = contentType.getValue();
	   Assert.assertEquals(jsonMimeType, mimeType );
	}
	
	@Test
    public void 
    givenWithdrawalRequestWithExceedsCurrentBalance_whenWithdrawalRequestIsExecuted_thenCheckJsonMessage() throws Exception {
		
		// Given
		TransactionInput transaction = new TransactionInput(60000); // withdrawal exceeds current balance
		Gson gson = new Gson();
		String json = gson.toJson(transaction);
		HttpUriRequest request = new HttpPost( "http://localhost:8080/"+BankAccountConstants.APP_NAME_TEST+"/accounts/"
        +BankAccountConstants.ACCOUNT_ID_TEST+"/withdrawal/");
		request.setEntity(new StringEntity(json));
		request.setHeader("Content-Type", "application/json");
		request.setHeader("Accept", "application/json");
        
		// When
		HttpResponse response = HttpClientBuilder.create().build().execute( request );
		
		// Then
		HttpEntity entity = ((CloseableHttpResponse)response).getEntity();
	 	String result = EntityUtils.toString(entity);
	 	Assert.assertEquals("{\"success\":false,\"messages\":{\"message\":\"You have insufficient amount\",\"title\":\"Error\"},\"errors\":{},\"data\":{},\"httpResponseCode\":406}", result);
		
	}
	
	@Test
    public void 
    givenWithdrawalRequestWithMaxAmountPerDay_whenWithdrawalRequestIsExecuted_thenCheckJsonMessage() throws Exception {
		
		// Given
		TransactionInput transaction = new TransactionInput(11000); // Withdrawal > MAX amount per day
		Gson gson = new Gson();
		String json = gson.toJson(transaction);
		HttpUriRequest request = new HttpPost( "http://localhost:8080/"+BankAccountConstants.APP_NAME_TEST+"/accounts/"
		+BankAccountConstants.ACCOUNT_ID_TEST+"/withdrawal/");
		request.setEntity(new StringEntity(json));
		request.setHeader("Content-Type", "application/json");
		request.setHeader("Accept", "application/json");
        
		// When
		HttpResponse response = HttpClientBuilder.create().build().execute( request );
				
		// Then
		HttpEntity entity = ((CloseableHttpResponse)response).getEntity();
		String result = EntityUtils.toString(entity);
		Assert.assertEquals("{\"success\":false,\"messages\":{\"message\":\"Withdrawal per day should not be more than $50K\",\"title\":\"Error\"},\"errors\":{},\"data\":{},\"httpResponseCode\":406}", result);
		
	}
	
	@Test
    public void 
    givenWithdrawalRequestWithMaxAmountPerTransaction_whenWithdrawalRequestIsExecuted_thenCheckJsonMessage() throws Exception {
		
		// Given
		TransactionInput transaction = new TransactionInput(6000); // Withdrawal > MAX amount per transaction
		Gson gson = new Gson();
		String json = gson.toJson(transaction);
		HttpUriRequest request = new HttpPost( "http://localhost:8080/"+BankAccountConstants.APP_NAME_TEST+"/accounts/"
		+BankAccountConstants.ACCOUNT_ID_TEST+"/withdrawal/");
		request.setEntity(new StringEntity(json));
		request.setHeader("Content-Type", "application/json");
		request.setHeader("Accept", "application/json");
		        
		// When
		HttpResponse response = HttpClientBuilder.create().build().execute( request );
						
		// Then
		HttpEntity entity = ((CloseableHttpResponse)response).getEntity();
		String result = EntityUtils.toString(entity);
		Assert.assertEquals("{\"success\":false,\"messages\":{\"message\":\"Withdrawal per transaction should not be more than $20K\",\"title\":\"Error\"},\"errors\":{},\"data\":{},\"httpResponseCode\":406}", result);
		
	}
	
	@Test
    public void testSuccessfulWithdrawal_whenWithdrawalRequestIsExecuted_thenCheckJsonMessage() throws Exception {
		
		// Given
		TransactionInput transaction = new TransactionInput(1000); 
		Gson gson = new Gson();
		String json = gson.toJson(transaction);
		HttpUriRequest request = new HttpPost( "http://localhost:8080/"+BankAccountConstants.APP_NAME_TEST+"/accounts/"
		+BankAccountConstants.ACCOUNT_ID_TEST+"/withdrawal/");
		request.setEntity(new StringEntity(json));
		request.setHeader("Content-Type", "application/json");
		request.setHeader("Accept", "application/json");
				        
		// When
		HttpResponse response = HttpClientBuilder.create().build().execute( request );
								
		// Then
		HttpEntity entity = ((CloseableHttpResponse)response).getEntity();
		String result = EntityUtils.toString(entity);
		Assert.assertEquals("{\"success\":true,\"messages\":{\"message\":\"Withdrawal sucessfully Transacted\",\"title\":\"\"},\"errors\":{},\"data\":{},\"httpResponseCode\":200}", result);
		
	}	

}
