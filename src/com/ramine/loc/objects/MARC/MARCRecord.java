package com.ramine.loc.objects.MARC;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MARCRecord {

	public String leader;
	public ArrayList<MARCControlField> controlList;
	public ArrayList<MARCDataField> datafields;
	
	public MARCRecord() {
		// TODO Auto-generated constructor stub
		controlList = new ArrayList<>();
		datafields = new ArrayList<>();
				
	}
	
	
	
	
	
	
	public JSON createJSONRecord(){
		
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
			
			JSONObject dataF = new JSONObject();
			
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
	
	
	
	
	
}
