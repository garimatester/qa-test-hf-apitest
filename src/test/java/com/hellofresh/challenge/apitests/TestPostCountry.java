package com.hellofresh.challenge.apitests;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.hellofresh.challenge.utils.Common;


/**
 * @author Garima
 * This class test post Api Call 
 * 
 *
 */
public class TestPostCountry extends Common{
	
	protected  Logger log = LoggerFactory.getLogger(this.getClass());
	
	@BeforeClass(alwaysRun = true)
    @Parameters("testDataFile")
    protected void setUp(@Optional("src/test/resources/postCountry.json") final String testDataFile) throws IOException {
        super.setUp(testDataFile);
    }
	
	
	
	
	@Test
    public void testAPICallContent() throws Exception {
		callRestService(testData);  //now it is just checking if body is set correctly

     //testContentJSON(testData);

           
    }
	
	
	
}







