package com.hellofresh.challenge.testData;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;



/**
 * @author Garima
 * This class process/read the testData required in test
 * This class is for json file mapping from src/main/resources
 * json files keep expected testData 
 * 
 * #readtestdata in common invokes getInstance(String filename) method
 *  and filename is provided in Test -Setup Method
 *
 */

public class TestData {

	private static TestData testData;

	List<Param> param;

	static class Param {
		String key;
		String value;
	}

	List<SearchWord> searchWords;

	static class SearchWord {

		String alpha2_code;

	}
	List<JsonPath> jsonPath;

	static class JsonPath {

		String key;
		String value;


	}

	public JsonObject getBody() {
		return body;
	}


	public void setBody(JsonObject body) {
		this.body = body;
	}





	JsonObject body;

	public List<Param> getListparam() {
		return param;
	}


	public void setListparam(List<Param> listparam) {
		this.param = listparam;
	}




	public List<SearchWord> getSearchWords() {
		return searchWords;
	}


	public void setSearchWords(List<SearchWord> searchWords) {
		this.searchWords = searchWords;
	}


	public List<JsonPath> getJsonPath() {
		return jsonPath;
	}


	public void setJsonPath(List<JsonPath> jsonPath) {
		this.jsonPath = jsonPath;
	}


	public static TestData getInstance(String filename) {

		Gson gson = new Gson();

		try (Reader reader = new FileReader(filename)) {

			testData = gson.fromJson(reader, TestData.class);

			}
		catch (IOException e) {
			e.printStackTrace();
		}
		return testData;


	}


	/**
	 * Returns value of selected key of the param from json file.
	 *
	 * 
	 */

	public String getParam(final String key) {
		if (param != null) {
			final Param get = param.stream()
					.filter(p -> p.key.equals(key))
					.findFirst().orElse(null);
			if (get != null) {
				return get.value;
			}
		}

		return null;
	}
    
	
	/**
	 * Returns a copy of the params as Map.
	 *
	 * 
	 */
	public Map<String, String> getParams() {
		final Map<String, String> params = new HashMap<>();
		for (final Param e : param) {
			String v = e.value.trim();
			v = v.replaceAll("\\n", "");
			v = v.replaceAll("\\t", "");
			params.put(e.key, v);
		}
		return params;
	}



	public Map<String, String> getSearchWordsAsMap() {
		final Map<String, String> searchwords = new HashMap<>();
		for (final SearchWord e : searchWords) {
			String v = e.alpha2_code.trim();
			v = v.replaceAll("\\n", "");
			v = v.replaceAll("\\t", "");
			searchwords.put(e.alpha2_code, v);
		}
		return searchwords;
	}


	public Map<String, String> getJsonPathAsMap() {
		final Map<String, String> jsonPaths = new HashMap<>();
		for (final JsonPath e : jsonPath) {
			String v = e.value.trim();
			v = v.replaceAll("\\n", "");
			v = v.replaceAll("\\t", "");
			jsonPaths.put(e.key, v);
		}
		return jsonPaths;
	}





	/**
	 * Returns param for given key as int.
	 *
	 * @param key - the key of the param
	 * @return Integer value of param
	 * used in status code pick
	 */
	public int getIntParam(final String key) {
		return Integer.valueOf(getParams().get(key));
	}

	
	
	



}


