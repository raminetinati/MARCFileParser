package com.ramine.loc.objects.MARC;

import java.util.ArrayList;

public class MARCDataField {
	
	
	
	public String tag;
	public String ind1;
	public String ind2;
	
	public ArrayList<MARCDataFieldSubField> subfields;
	
	public MARCDataField() {
		// TODO Auto-generated constructor stub
		
		subfields = new ArrayList<>();
	}
	
	
	

}
