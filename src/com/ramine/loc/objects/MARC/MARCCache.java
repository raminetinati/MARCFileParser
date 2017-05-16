package com.ramine.loc.objects.MARC;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import com.ramine.loc.operations.FileOperations;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MARCCache {

	
	HashMap<String, MARCRecord> recordsMap;
	HashMap<String, ArrayList<MARCRecord>> recordsMap_Authors;
	HashMap<String, ArrayList<MARCRecord>> recordsMap_Year;


	ArrayList<MARCRecord> records;
	
	public MARCCache() {
		
		recordsMap = new HashMap<>();
		records = new ArrayList<>();
		recordsMap_Authors = new HashMap<>();
		recordsMap_Year = new HashMap<>();
	}
	
	public ArrayList<MARCRecord> getRecords() {
		return records;
	}
	
	public HashMap<String, MARCRecord> getRecordsMap() {
		return recordsMap;
	}
	
	public HashMap<String, ArrayList<MARCRecord>> getRecordsMap_Year() {
		return recordsMap_Year;
	}	
	
	
	public Boolean loadCachedRecords(){

		try{
			
			FileOperations.loadJSONObjects("json/", records);		
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public HashMap<String, ArrayList<MARCRecord>> getRecordsMap_Authors() {
		return recordsMap_Authors;
	}
	
	
	
	public void indexCachedRecords(){
		
		System.out.println("** INFO *** Starting to index MARC Files by (Tag 200 C)");
		
		for(MARCRecord rcd: records){
			
			for(MARCDataField df : rcd.datafields){
				if(df.tag.equals("260")){
					
					for(MARCDataFieldSubField sf : df.subfields){
						if(sf.code.equals("c")){
							
							String year = sf.content.replace(".", "");
							//System.out.println("Date:"+);
							if(recordsMap_Year.containsKey(year)){
								recordsMap_Year.get(year).add(rcd);
							}else{
								ArrayList<MARCRecord> reco= new ArrayList<>();
								reco.add(rcd);
								recordsMap_Year.put(year, reco);
							}
									
						}
					}
					
				}
				if(df.tag.equals("100")){
					
					for(MARCDataFieldSubField sf : df.subfields){
						if(sf.code.equals("a")){
							String author = sf.content;
							System.out.println("Author:"+sf.content);
							if(recordsMap_Authors.containsKey(author)){
								recordsMap_Year.get(author).add(rcd);
							}else{
								ArrayList<MARCRecord> reco= new ArrayList<>();
								reco.add(rcd);
								recordsMap_Year.put(author, reco);
							}
									
						}
					}
					
				}
			}
			
			
		}
		
		
		
	}
	
	
	/**
	 * These are some methods we are going to be using for the REST server
	 */
	
	
	public JSONObject generateStats(){
		
		JSONObject stats= new JSONObject();
		
		stats.put("total_records", records.size());
		stats.put("date_cache_created", new Date());
		return stats;
		
		
		
	}
	
	public JSONObject recordsFromIndex(int idx){
		
		
			JSONObject toRet = new JSONObject();
			
			JSONObject query = new JSONObject();
			query.put("type", "returnFromIndex");
			query.put("param", "fromIndxPosition");
			query.put("paramVal", idx);
		
			int cnt = 0;
			if(idx<records.size()){
			
				JSONArray res = new JSONArray();
				for(MARCRecord rcd : records.subList(idx, records.size()-1)){
					
					res.add(rcd.jsonRepresentation);
					cnt++;
			}
			
			
			query.put("resultsReturned", cnt);

			toRet.put("query", query);

			toRet.put("results", res);
		
		}
		return toRet;
		
	}
	
	
	public JSONObject recordsFromKeywordSearch(String keyword){
		
		
		JSONObject toRet = new JSONObject();
		
		JSONObject query = new JSONObject();
		query.put("type", "queryKeyword");
		query.put("param", "keyword");
		query.put("paramVal", keyword);
	
		int cnt = 0;
		if(keyword.length()>1){
	
			JSONArray res = new JSONArray();
			for(MARCRecord rcd : records){
				
				if(rcd.jsonRepresentation.toString().toLowerCase().contains(keyword.toLowerCase())){
					res.add(rcd.jsonRepresentation);
					cnt++;
				}
		}
		
		
		query.put("resultsReturned", cnt);

		toRet.put("query", query);

		toRet.put("results", res);
	
	}
	return toRet;
	
}

	public Object recordsFromAuthorName(String keyword) {

		JSONObject toRet = new JSONObject();
		
		JSONObject query = new JSONObject();
		query.put("type", "queryKeyword");
		query.put("param", "keyword");
		query.put("paramVal", keyword);
	
		int cnt = 0;
		if(keyword.length()>1){
	
			JSONArray res = new JSONArray();
			for(Entry<String, ArrayList<MARCRecord>> rcd : recordsMap_Authors.entrySet()){
				
				if(rcd.getKey().toLowerCase().contains(keyword.toLowerCase())){
					
					for(MARCRecord mrd: rcd.getValue()){
						res.add(mrd.jsonRepresentation);
						cnt++;
					}
				}
		}
		
		
		query.put("resultsReturned", cnt);

		toRet.put("query", query);

		toRet.put("results", res);
		}
			
		return toRet;

	}

	public Object recordsFromAuthorYear(String keyword) {

		
		JSONObject toRet = new JSONObject();
		
		JSONObject query = new JSONObject();
		query.put("type", "queryKeyword");
		query.put("param", "keyword");
		query.put("paramVal", keyword);
	
		int cnt = 0;
		if(keyword.length()>1){
	
			JSONArray res = new JSONArray();
			for(Entry<String, ArrayList<MARCRecord>> rcd : recordsMap_Year.entrySet()){
				
				if(rcd.getKey().toLowerCase().contains(keyword.toLowerCase())){
					
					for(MARCRecord mrd: rcd.getValue()){
						res.add(mrd.jsonRepresentation);
						cnt++;
					}
				}
		}
		
		
		query.put("resultsReturned", cnt);

		toRet.put("query", query);

		toRet.put("results", res);
		}
			
		return toRet;

	}
	
}
