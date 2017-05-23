package com.ramine.loc.web;

import static spark.Spark.get;

import com.ramine.loc.objects.MARC.MARCCache;

import net.sf.json.JSONObject;

public class RESTServer {
	
	
	MARCCache cache;
	
	public RESTServer(boolean startWithCache, boolean indexRecords) {
		
		
		cache = new MARCCache();
		
		if(startWithCache){
			System.out.println("***INFO*** - Loading Cached Files!");
			cache.loadCachedRecords();
			System.out.printf("***INFO*** - Loaded %d Cached MARC Files\n",cache.getRecords().size());
			System.out.println("***");
		}
		
		
		if(indexRecords){
			System.out.println("***INFO*** - Indexing Cached Files!");
			cache.indexCachedRecords();
			System.out.printf("***INFO*** - Indexed %d Cached MARC Files\n",cache.getRecordsMap_Authors().size());
			System.out.println("***");
		}
		

		/*
		 * Add all the bindings!
		 */
		getMARCStats();
		getAllResources();
		getResultsFromIndx();
		getResultsFromKeywordSearch();
		getResultsFromAuthorSearch();
		getResultsFromAuthorYearSearch();
		getResultsYearsSearch();
	}
	
	
	
	private void getAllResources() {
		get("/api/resources/", (req, res) -> {
			return "";
        	
        });				
	}
	
	
	private void getMARCStats() {
		get("/api/stats/", (req, res) -> {
			return cache.generateStats().toString(4);
        	
        });				
	}
	

	private void getResultsFromIndx() {
	get("/api/results/:idx", (req, res) -> {

		int index = Integer.parseInt(req.params(":idx"));
		
		if(index>0){
			return cache.recordsFromIndex(index);
		}else{
			return new JSONObject().put("results", new JSONObject());
		}
    });								
	}
	
	
	private void getResultsFromKeywordSearch() {
	get("/api/results/search/:keyword", (req, res) -> {

		String index = req.params(":keyword");
		
		if(index.length()>1){
			return cache.recordsFromKeywordSearch(index);
		}else{
			return new JSONObject().put("results", new JSONObject());
		}
    });								
	}
	
	private void getResultsFromAuthorSearch() {
		get("/api/results/search/author/:keyword", (req, res) -> {

			String index = req.params(":keyword");
			
			if(index.length()>1){
				return cache.recordsFromAuthorName(index);
			}else{
				return new JSONObject().put("results", new JSONObject());
			}
	    });								
		}
	
	
	private void getResultsFromAuthorYearSearch() {
		get("/api/results/search/author/year/:year", (req, res) -> {
			
			String index = req.params(":year");
			if(index.length()>1){
				return cache.recordsFromAuthorYear(index);
			}else{
				return new JSONObject().put("results", new JSONObject());
			}
	    });								
		}
	
	private void getResultsYearsSearch() {
		get("/api/results/search/years/list", (req, res) -> {
			
			return cache.recordsPerYear();
			
	    });								
		}
	
}
