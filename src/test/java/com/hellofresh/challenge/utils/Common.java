package com.hellofresh.challenge.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.hellofresh.challenge.testData.TestData;

public class Common extends QaComparator{


	protected  Logger log = LoggerFactory.getLogger(this.getClass());
	private URIBuilder uriBuilder;
	protected static TestData testData;
	protected static String url;

	
	
	
	

    /**
     * A divider for logs consisting of plus signs.
     */
    private static final String LOG_DIVIDER_PLUS = "++++++++++++++++++++++++++++++++++++++++++++++++++";

    /**
     * A divider for logs consisting of minus signs.
     */
    private static final String LOG_DIVIDER_MINUS = "--------------------------------------------------";

   
   



	/**
	 * Reads the test data from a file.
	 *
	 * @param testDataFile Name of the xml file. When an empty string is passed, the name is build using the
	 *            instantiating class + .xml.
	 */


	@BeforeClass
	@Parameters({ "testdatafile" })
	protected void setUp(String testDataFile) throws  IOException{
		readTestData(testDataFile);
		//buildPath(testData);
	}


	protected void readTestData(String testDataFile) {


		try {
			log.info("Common::readTestData()");
			if (testDataFile == null || testDataFile.length() < 1) {
				log.info("No test data file is created");
			}
			testData=TestData.getInstance(testDataFile);
			log.info("+ Running the test " + testDataFile + " against: " +""+ testData);
			 logPlusDivider();



		} catch (final Exception ex) {
			java.util.logging.Logger.getLogger(Common.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			Assert.fail("could not read test data: " + testDataFile);
		}
	}

	
	
	/**
     * Logs a row of plus signs, for prettier logs.
     */
    protected void logPlusDivider() {
        log.info(LOG_DIVIDER_PLUS);
    }
    
    
    /**
     * Logs a row of minus signs, for prettier logs.
     */
    protected void logMinusDivider() {
        log.info(LOG_DIVIDER_MINUS);
    }

    
    
public  void testStatusCode(String URL ,int responseCode) throws ClientProtocolException, IOException {

		
		log.info("api url being accessed is"+ URL);
		HttpUriRequest request = new HttpGet(URL);
		log.info("http request is"+ request);
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		AssertJUnit.assertEquals(httpResponse.getStatusLine().getStatusCode(),responseCode);
		}

	
	public static void testMimeType(String restURL, String expectedMimeType) throws ClientProtocolException, IOException {

		HttpUriRequest request = new HttpGet(restURL);
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

		AssertJUnit.assertEquals(expectedMimeType,ContentType.getOrDefault(httpResponse.getEntity()).getMimeType());
		}
	
	
	
	
	 /**
     * Convenience method to build the resource path for a {@link Test}. The path is read from the {@link TestData},
     * split by '/' and variables are replaces by their values from the same TestData.
     *
     * @param testData
     * @return the resource path
     */
    protected String buildPath(final TestData testData) {

        final StringBuilder path = new StringBuilder();
        path.append(testData.getParam("basePath"));
        log.info("basepath" + testData.getParam("basePath"));
        //path.append("/");
        

        // read from default param
        String resPath = testData.getParam("resPath");
        
        log.info("path" + testData.getParam("resPath"));

        final String[] resPathParts = StringUtils.split(resPath, "/");
        for (String res : resPathParts) {
            if (StringUtils.startsWithIgnoreCase(res, "${")) {
                res = StringUtils.substring(res, 2, -1);
                log.info("res"+ res + testData.getParam(res));
                String value = testData.getParam(res);
                res = value;
            }
            String separator = "/";
            if (StringUtils.startsWith(res, separator)) {
                separator = "";
            }
            path.append(separator);
            path.append(res);
        }
        log.info("path is"+ path);
        url=path.toString();
        
        return url;

       // return substitute(path.toString());
    }
    
    
    /**
    *
    * @param restCall
    * @return
    * @throws URISyntaxException
    * @throws IOException
    * @throws NumberFormatException
    * @throws InterruptedException
    */
   protected HttpResponse callRestService(final TestData testData) throws URISyntaxException, IOException, NumberFormatException, InterruptedException {
       uriBuilder = new URIBuilder();
       uriBuilder.setScheme(testData.getParam("serviceScheme"));
       uriBuilder.setHost(testData.getParam("serviceHost"));
       uriBuilder.removeQuery();
       CloseableHttpClient httpClient = HttpClientBuilder.create().build();

       final String httpMethod = testData.getParam("httpMethod");

       final String path = buildPath(testData);

      
       final HttpResponse response = executeHttpClient(path, httpMethod, testData);
       return response;
   }
   
   
   
   
   
   protected HttpResponse executeHttpClient(final String path, final String httpMethod,
           final TestData restCall) throws URISyntaxException, IOException {
       
	   final URI uri = uriBuilder.setPath(path).build();

      
       HttpResponse response = null;

       if ("POST".equalsIgnoreCase(httpMethod)) {
    	   String body = getBody(testData);
           log.info("post body length"+ body.length());
           
           final HttpPost httpPost = new HttpPost(uri);
           if (StringUtils.isNotBlank(body)) {
               httpPost.setEntity(new StringEntity(body, "UTF-8"));
           }
           log.info("post request is"+ httpPost.getEntity());
           Assert.assertEquals(httpPost.getEntity().getContentLength(), body.length());
           //response = httpClient.execute(httpPost);
       } else if ("GET".equalsIgnoreCase(httpMethod)) {
    	   log.info("get url"+ uri);
           final HttpGet httpGet = new HttpGet(uri);
   		response = HttpClientBuilder.create().build().execute(httpGet);

       } else {
           log.info( "No or unknown httpMethod specified. Must be POST, GET. Actual: " + httpMethod);
       }

       return response;
   }

   
   
   /**
    * Get the request body from the provided test case by param "body" if provided.
    *
    * @param TestData
    * @return the request body or null
    */
   private String getBody(final TestData testData) {
	   
	  
       JsonObject bodyobj = testData.getBody();

       String body =bodyobj.toString();
       
       log.info("jsonResponsein body"+ body);
       
       String res = "";
       if (StringUtils.isBlank(body)) {
           return null;
       }
       while (body.length() > 0) {
           int start = StringUtils.indexOf(body, "${");
           int end = 0;
           if (start > 0) {
               end = StringUtils.indexOf(body.substring(start), "}");
           }
           if (start > 0 && end > 0) {
               end = end + start;
               start = start + 2;
               String var = body.substring(start, end);
               String orgVar = "${" + var + "}";
               
               String value = testData.getParam(var);
               
               if (StringUtils.isNotBlank(value)) {
                   if (value.endsWith("NULL")) {
                       log.info("Body: Replacing variable '" + orgVar + "' with empty value.");
                       orgVar = "";
                   } else {
                       log.info("Body: Replacing variable '" + orgVar + "' with value'" + value + "'.");
                       orgVar = value;
                   }
               }

               res += body.substring(0, start - 2) + orgVar;
               body = body.substring(end + 1);
           } else {
               res += body;
               body = "";
           }
       }
       return res;
   }



	


}
