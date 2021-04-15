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

public class DepositControllerTests {
	
	@Test
	public void givenAccountDepositAmount_whenDepositTransactionIsHappened_then200IsReceived()
	  throws ClientProtocolException, IOException {
	 
	    // Given
		TransactionInput transaction = new TransactionInput(4000); // deposit amount
        Gson gson = new Gson();
        String json = gson.toJson(transaction);
		HttpUriRequest request = new HttpPost( "http://localhost:8080/"+BankAccountConstants.APP_NAME_TEST+"/accounts/"
		+BankAccountConstants.ACCOUNT_ID_TEST+"/deposit/");
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
	givenRequestWithNoAcceptHeader_whenRequestIsExecuted_thenDefaultResponseContentTypeIsJson()
	  throws ClientProtocolException, IOException {
	 
	   // Given
	   String jsonMimeType = "application/json";
	   TransactionInput transaction = new TransactionInput(4000); // deposit amount
       Gson gson = new Gson();
       String json = gson.toJson(transaction);
       HttpUriRequest request = new HttpPost( "http://localhost:8080/"+BankAccountConstants.APP_NAME_TEST+"/accounts/"
		+BankAccountConstants.ACCOUNT_ID_TEST+"/deposit/");
       request.setEntity(new StringEntity(json));
       request.setHeader("Content-Type", "application/json");

	   // When
	   HttpResponse response = HttpClientBuilder.create().build().execute( request );

	   // Then
	   Header contentType = response.getFirstHeader("Content-Type");
	   String mimeType = contentType.getValue();
	   Assert.assertEquals(jsonMimeType, mimeType );
	}
	
	@Test
    public void 
    givenRequestWithMoreThanMaxPerTransaction_whenRequestIsExecuted_thenDefaultResponseIsErrorMessage() throws Exception {
		
		// Given
		TransactionInput transaction = new TransactionInput(21000); // deposit > MAX per Transaction (20k$)
        Gson gson = new Gson();
        String json = gson.toJson(transaction);
        HttpUriRequest request = new HttpPost( "http://localhost:8080/"+BankAccountConstants.APP_NAME_TEST+"/accounts/"
		+BankAccountConstants.ACCOUNT_ID_TEST+"/deposit/");
        request.setEntity(new StringEntity(json));
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");
        
        // When
 	   	HttpResponse response = HttpClientBuilder.create().build().execute( request );
 	   	
 	   	// Then
	 	HttpEntity entity = ((CloseableHttpResponse)response).getEntity();
	 	String result = EntityUtils.toString(entity);
	 	Assert.assertEquals("{\"success\":false,\"messages\":{\"message\":\"Deposit per transaction should not be more than $20K\",\"title\":\"Error\"},\"errors\":{},\"data\":{},\"httpResponseCode\":406}",result);
    }
	
    @Test
    public void testMaxAllowedDepositPerDay_whenRequestIsExecuted_thenDefaultResponseIsErrorMessage() throws Exception {
    	// Given
    	TransactionInput transaction = new TransactionInput(20000);
    	Gson gson = new Gson();
    	String json = gson.toJson(transaction);
    	HttpUriRequest request = new HttpPost( "http://localhost:8080/"+BankAccountConstants.APP_NAME_TEST+"/accounts/"
		+BankAccountConstants.ACCOUNT_ID_TEST+"/deposit/");
    	request.setEntity(new StringEntity(json));
    	request.setHeader("Content-Type", "application/json");
    	request.setHeader("Accept", "application/json");
    	        
    	// When
    	HttpResponse response = HttpClientBuilder.create().build().execute( request );
    	 	   	
    	// Then
    	HttpEntity entity = ((CloseableHttpResponse)response).getEntity();
    	String result = EntityUtils.toString(entity);
    	Assert.assertEquals("{\"success\":false,\"messages\":{\"message\":\"Deposit for the day should not be more than $100K\",\"title\":\"Error\"},\"errors\":{},\"data\":{},\"httpResponseCode\":406}", result);
    }
    
    @Test
    public void testSuccessfulDeposit() throws Exception {
    	// Given
    	TransactionInput transaction = new TransactionInput(5000); 
    	Gson gson = new Gson();
    	String json = gson.toJson(transaction);
    	HttpUriRequest request = new HttpPost( "http://localhost:8080/"+BankAccountConstants.APP_NAME_TEST+"/accounts/"
		+BankAccountConstants.ACCOUNT_ID_TEST+"/deposit/");
    	request.setEntity(new StringEntity(json));
    	request.setHeader("Content-Type", "application/json");
    	request.setHeader("Accept", "application/json");
    	        
    	// When
    	HttpResponse response = HttpClientBuilder.create().build().execute( request );
    	 	   	
    	// Then
    	HttpEntity entity = ((CloseableHttpResponse)response).getEntity();
    	String result = EntityUtils.toString(entity);
    	Assert.assertEquals("{\"success\":true,\"messages\":{\"message\":\"Deposit sucessfully Transacted\",\"title\":\"\"},\"errors\":{},\"data\":{},\"httpResponseCode\":200}", result);
    }

}
