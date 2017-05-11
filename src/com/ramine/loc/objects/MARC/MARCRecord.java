package com.ramine.loc.objects.MARC;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonObject;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MARCRecord {

	public String leader;
	public ArrayList<MARCControlField> controlList;
	public ArrayList<MARCDataField> datafields;
	public 	JSONObject jsonRepresentation;

	public MARCRecord() {
		// TODO Auto-generated constructor stub
		controlList = new ArrayList<>();
		datafields = new ArrayList<>();
				
	}
	
	
	
	
	public void loadJSONFile(JSONObject obj){
		
		
		try{
		
			jsonRepresentation = obj;
			
		leader = obj.getString("leader");
		
		JSONArray ctrList = obj.getJSONArray("controllist");
		
		for(int i=0; i<ctrList.size(); i++){
			
			JSONObject ctr = ctrList.getJSONObject(i);
			
			MARCControlField field = new MARCControlField();
			
			field.content = ctr.getString("content");
			field.tag = ctr.getString("tag");
			
			controlList.add(field);
		}
		
		JSONArray datafld = obj.getJSONArray("datalist");
		
		for(int i=0; i<datafld.size(); i++){
			
			JSONObject ctr = datafld.getJSONObject(i);

			MARCDataField field = new MARCDataField();
			
			field.ind1 = ctr.getString("ind1");
			field.ind2 = ctr.getString("ind2");
			field.tag = ctr.getString("tag");
			
			JSONArray subfields = ctr.getJSONArray("subfields");

			for(int j=0; j<subfields.size(); j++){

				JSONObject sf = subfields.getJSONObject(j);

				MARCDataFieldSubField sub = new MARCDataFieldSubField();
				
				sub.code = sf.getString("code");
				sub.content = sf.getString("content");
				
				field.subfields.add(sub);
				datafields.add(field);
			}	
		}
		}catch(Exception e){
			
			
		}
		
		
		
	}
	
	
	
	
	public JSONObject createJSONRecord(){
		
		JSONObject obj = new JSONObject();
		
		obj.put("leader", leader);
		
		JSONArray cList = new JSONArray();
		for(MARCControlField itm : controlList){
			
			JSONObject ctrlList = new JSONObject();
			ctrlList.put("tag", itm.tag);
			ctrlList.put("content", itm.content);
			cList.add(ctrlList);
			
		}
		
		obj.put("controllist", cList);
		
		JSONArray dList = new JSONArray();
		for(MARCDataField itm : datafields){
			
			JSONObject dataF 			= new JSONObject();
			
			dataF.put("tag", itm.tag);
			dataF.put("ind1", itm.ind1);
			dataF.put("ind2", itm.ind2);
			
			JSONArray subFs = new JSONArray();
			for(MARCDataFieldSubField i : itm.subfields){
				
				JSONObject subfield = new JSONObject();
				subfield.put("code", i.code);
				subfield.put("content", i.content);
				subFs.add(subfield);

			}
			
			dataF.put("subfields", subFs);

			dList.add(dataF);
			
		}
		
		obj.put("datalist", dList);

		
		return obj;
		
	}
	
//	@Override
//	public String toString() {
//		return jsonRepresentation.toString();
//	}
	
	
	
	
	
}
