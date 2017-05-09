package com.ramine.loc.objects.MARC;

import java.util.ArrayList;

public class MARCDataFieldSubField {
	
	
	
	public String code;
	public String content;
	public ArrayList<MARCDataFieldSubField> subfields;
	
	public MARCDataFieldSubField() {
		subfields = new ArrayList<>();
	}
	
}
