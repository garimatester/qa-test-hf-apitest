package com.hellofresh.challenge.apitests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
//import org.json.JSONException;
//import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hellofresh.challenge.testData.TestData;
import com.hellofresh.challenge.utils.Common;


/**
 * @author Garima
 * This class test api calls
 * verfies apicallstatuscode
 * verifies returned json content
 * expected values are picked from json file
 *
 */
public class TestGetNonExistentCountry extends Common{
	
	protected  Logger log = LoggerFactory.getLogger(this.getClass());
	
	@BeforeClass(alwaysRun = true)
    @Parameters("testDataFile")
    protected void setUp(@Optional("src/test/resources/getNonExistentCountry.json") final String testDataFile) throws IOException {
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

	
	
	

	public void testContentJSON(final TestData testData,String URL) throws ClientProtocolException, IOException, ParserConfigurationException, java.text.ParseException {

		HttpUriRequest request = new HttpGet(URL);
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		// Convert the response to a String format
		String strresult = EntityUtils.toString(httpResponse.getEntity());
        log.info(httpResponse +"http response"+strresult);
         
         final Gson gson = new Gson();
         JsonParser parser = new JsonParser();
         JsonObject rootObj = parser.parse(strresult).getAsJsonObject();  
         
         log.info("message is"+ rootObj.getAsJsonObject("RestResponse").getAsJsonArray("messages"));
         String[] arrMessage = gson.fromJson(rootObj.getAsJsonObject("RestResponse").getAsJsonArray("messages"), String[].class);
         ArrayList<String> listArrMessage = new ArrayList<String>(Arrays.asList(arrMessage));
         compareObjectValue("Non-Existing Country Message", testData.getParam("message"), listArrMessage.get(0));
       
     
	}
}







