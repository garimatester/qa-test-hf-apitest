package com.hellofresh.challenge.apitests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hellofresh.challenge.testData.TestData;
import com.hellofresh.challenge.testData.Country;
import com.hellofresh.challenge.utils.Common;


/**
 * @author Garima
 * This class test GET Api Call to getAllCountry
 * verfies apicall statuscode
 * verifies returned json content
 * expected values are picked from json file getAllCountries.json
 * extends to common to read test data file
 *
 */
public class TestGetAllCountry extends Common{
	
	protected  Logger log = LoggerFactory.getLogger(this.getClass());
	private boolean b;
	
	@BeforeClass(alwaysRun = true)
    @Parameters("testDataFile")
    protected void setUp(@Optional("src/test/resources/getAllCountries.json") final String testDataFile) throws IOException {
        super.setUp(testDataFile);
    }
	
	
	@Test
    public void testAPICallStatus() throws Exception {
		   String url=buildPath(testData);
    		
		   testMimeType(url, testData.getParam("Accept"));
           testStatusCode(url, testData.getIntParam("responseCode"));
		   

    			
    }
	
	@Test
    public void testAPICallContent() throws Exception {
	    String url=buildPath(testData);

    	 	testContentJSON(testData, url);

           
    }

	
	public void testContentJSON(final TestData testData,String URL) throws ClientProtocolException, IOException, java.text.ParseException {

		HttpUriRequest request = new HttpGet(URL);
		
        log.info("execute GET request"+request);

		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		String strresult = EntityUtils.toString(httpResponse.getEntity());
        log.info("http response"+strresult);
        log.info("parse string response in java object start"+strresult);
        logPlusDivider();
         final Gson gson = new Gson();
         JsonParser parser = new JsonParser();
         JsonObject rootObj = parser.parse(strresult).getAsJsonObject();         
         Country[] country = gson.fromJson(rootObj.getAsJsonObject("RestResponse").getAsJsonArray("result"), Country[].class);
         ArrayList<Country> list = new ArrayList<Country>(Arrays.asList(country));
         
         log.info("Expected Values are picked as map from testData--see json file in setup");
         Map<String, String>map=testData.getSearchWordsAsMap();   
         map.forEach((alpha2_code,name)->{
        
        log.info("Expected Code:"+ alpha2_code);
        	 
         log.info("Expected Value is filtered from actual list object ");

   		 final Country result = list.stream()
                .filter(p -> p.getAlpha2_code().equals(alpha2_code))
                .sorted((p1, p2) -> p2.getAlpha2_code().compareTo(p1.getAlpha2_code()))
                .findAny()         // If 'findAny' alpha2_code then return found
                .orElse(null); 
   		 
   		 
   		 if(result!=null) {
   			log.info("right country info found");
   			b=true;
   		 }
   		 else {
   			 log.info("right country not  found");
   			 b=false;

   			 
   			 
   		 }
   		 
   		 compareObjectValue("Country Code Found", true, b);
   		 Assert.assertEquals(b, true);
   		 
    	 

        
   		
   	});
         
         
         
         
         
         
         //customUnmarshal(result);
        //JSONObject data = new JSONObject(response);
       // Country obj=Country.get(result) ;
        //log.info("listcountry" + obj.alpha2_code+ obj.alpha3_code+ obj.name);
       // 
        //compareRawResponse(testData, result);
        
	
	
     
	/*private void compareRawResponse(final TestData testCase, final String jsonResponse) throws java.text.ParseException {

    final List<Param> jsonPathes = testData.getListparam();

    log.info("Response " + jsonResponse);

    for (final ExtEntry expectedKeyValue : jsonPathes) {
        final String jsonpath = expectedKeyValue.getEntry("jsonPath");
        final String expectedValue = expectedKeyValue.getEntry("value");
        String currentValue = "";

        try {
            currentValue = JsonPath.read(jsonResponse, jsonpath).toString();
        } catch (final ParseException e) {
            writeToErrorSummary(testCase, "JsonPath not found: " + jsonpath);
        } catch (final NullPointerException e) {
            writeToErrorSummary(testCase, "JsonPath not found: " + jsonpath);
        }

        if (currentValue.length() > 0) {
            compareObjectValue(testCase, jsonpath, expectedValue, currentValue);
        } else {
            writeToErrorSummary(testCase, "JsonPath not found: " + jsonpath);
        }
    
    }

}*/
	
	}
	
	
}







