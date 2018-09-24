
Implemented Test Details

## Tools/Lib Details
#### 
Maven,TestNg, gson, apache.commons-lang(for StringUtil and RandomUtil)
slf4j-log4j12(logging), com.jayway.jsonpath.version(for jsonPath read to compare response for jsonpath,see TestGetSelectedCountryInfo)


## run
#### 
navigate to project directory ./qa-test-hf-apitest

run with mvn commands
mvn clean
mvn install
mvn test

## Package Info
#### com.hellofresh.challenge.apitests
contains all the test classes
check src/test/resources  for .json file. each test class using corresponding json file for test input data
and expected outcome
####com.hellofresh.challenge.testData:  
contains pojo for mapping json input data file (TestData.java)and json response(Country.java)

TestData.java: all json files used as testdatainput are mapped to this
Country.java: JsonResponse is deserialized to this class object

####com.hellofresh.challenge.utils: 
QaComparator: custom comparator for comparing actual (from response) and expected from (json files) 



## TestGetAllCountry.java
#### BeforeClass
Parameters are passed as json file: src/test/resources/getAllCountries.json
TestData.java Instance is created for this file in common.java
basePath:http://services.groupkt.com/country/get is given in json file

#### Test steps
testAPICallStatus()
* Created new HttpGet Request for URL http://services.groupkt.com/country/get/all
* Execute Request
*check response statuscode 

testAPICallContent()
* Created new HttpGet Request for URL http://services.groupkt.com/country/get/all
* Execute Request
* search for expected searchWords -> alpha2_code(getAllCountries.json file) in response.


#### Assertions
* verified actual StatusCode is as expected "responseCode"(defined in jsonfile)
* verified actual contentType is as expected "ACCEPT"(defined in jsonfile)
* Verified expected searchWords -> alpha2_code (json file)is found in response

## TestNonExistentCountry.java
#### BeforeClass
Parameters are passed as json file: src/test/resources/getNonExistentCountry.json
TestData.java Instance is created for this file in common.java
basePath:http://services.groupkt.com/country/get is given in json file

#### Test steps
testAPICallStatus()
* Created new HttpGet Request for url(buildpath()in common.java file creating Url)
* Execute Request
*check response statuscode 

testAPICallContent()
* Created new HttpGet Request for Path
* Execute Request


#### Assertions
* verified actual StatusCode is as expected "responseCode"(defined in jsonfile)
* verified actual contentType is as expected "ACCEPT"(defined in jsonfile)
* Verified expected message (corresponding json file)is found in response


## TestGetSelectedCountryInfo.java
#### BeforeClass
Parameters are passed as json file: src/test/resources/getSelectedCountries.json
TestData.java Instance is created for this file in common.java
basePath:http://services.groupkt.com/country/get is given in json file

#### Test steps
testAPICallStatus()
* Created new HttpGet Request for Path
* Execute Request
*check response statuscode 

testAPICallContent()
* Created new HttpGet Request for Path
* Execute Request



#### Assertions
* verified actual StatusCode is as expected "responseCode"(defined in jsonfile)
* verified actual contentType is as expected "ACCEPT"(defined in jsonfile)
* Verified expected jsonPath and values (corresponding json file)is found in response



## TestPostCountry.java
#### BeforeClass
Parameters are passed as json file: src/test/resources/postCountry.json
TestData.java Instance is created for this file in common.java

#### Test steps

testAPICallContent()
* Created new HttpPost Request as per testdata in json file
execution is commented as it does not work




#### Assertions
* verified if body in request is set correctly

