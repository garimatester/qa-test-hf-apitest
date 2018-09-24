package com.hellofresh.challenge.apitests;

import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
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
import com.hellofresh.challenge.testData.Country;

import com.jayway.jsonpath.JsonPath;


/**
 * @author Garima
 * This class test api calls
 * verfies apicallstatuscode
 * verifies returned json content
 * expected values are picked from json file
 *
 */
public class TestGetSelectedCountryInfo extends Common{

	protected  Logger log = LoggerFactory.getLogger(this.getClass());

	@BeforeClass(alwaysRun = true)
	@Parameters("testDataFile")
	protected void setUp(@Optional("src/test/resources/getSelectedCountries.json") final String testDataFile) throws IOException {
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
        Country country = gson.fromJson(rootObj.getAsJsonObject("RestResponse").getAsJsonObject("result"), Country.class);
        String jsonResponse = gson.toJson(country);
        log.info("print json response"+ jsonResponse );
        compareRawResponse(testData, jsonResponse);

}



	private void compareRawResponse(final TestData testData, final String jsonResponse) throws java.text.ParseException {

		Map<String,String> map=testData.getJsonPathAsMap();
		map.forEach((jsonpath,value)->{
			final String expectedjsonPath = jsonpath;
			final String expectedValue = value;
			String currentValue = "";

			try {
				currentValue = JsonPath.read(jsonResponse, expectedjsonPath).toString();
			} catch (final ParseException e) {
				log.info("JsonPath not found: " + expectedjsonPath);
			} catch (final NullPointerException e) {
				log.info("JsonPath not found: " + expectedjsonPath);
			} catch (java.text.ParseException e) {
				log.info("JsonPath not found: " + expectedjsonPath);

			}

			if (currentValue.length() > 0) {
				compareObjectValue(expectedjsonPath, expectedValue, currentValue);
			} else {
				log.info("JsonPath not found: " + expectedjsonPath);
			}



		});
	}
}











