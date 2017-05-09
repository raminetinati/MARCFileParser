package com.ramine.loc.operations;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.ramine.loc.objects.MARC.MARCControlField;
import com.ramine.loc.objects.MARC.MARCDataField;
import com.ramine.loc.objects.MARC.MARCDataFieldSubField;
import com.ramine.loc.objects.MARC.MARCRecord;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
	

public class FileOperations {
	
	
	
	
	
	
	private static final String String = null;




	public static void LoadXMLFile(String filename, ArrayList<MARCRecord> records){
		
		try{
		File fXmlFile = new File(filename);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();

		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

		NodeList nList = doc.getElementsByTagName("record");

		System.out.println("----------------------------");	
		
		
		

		for (int temp = 0; temp < nList.getLength(); temp++) {

			//create some MARC records
			
			MARCRecord record = new MARCRecord();
			
			Node nNode = nList.item(temp);
			

			//System.out.println("\nCurrent Element :" + nNode.getNodeName());

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				
				record.leader = eElement.getElementsByTagName("leader").item(0).getTextContent();
				//System.out.println("Leader : " + record.leader);	
				
				NodeList controlfieldsList = eElement.getElementsByTagName("controlfield");
				
				for (int ctr = 0; ctr < controlfieldsList.getLength(); ctr++) {
				
					Node ctrNode = controlfieldsList.item(ctr);
					if (ctrNode.getNodeType() == Node.ELEMENT_NODE) {

						Element ctrElement = (Element) ctrNode;
						
						MARCControlField ctrField = new MARCControlField();
						ctrField.tag = ctrElement.getAttribute("tag");
						ctrField.content = ctrElement.getTextContent();
						
						//System.out.println("controlfield tag: " + ctrField.tag);
						//System.out.println("controlfield: " + ctrField.content);
						
						record.controlList.add(ctrField);
						
					}
					
				}
				
				NodeList tagList = eElement.getElementsByTagName("datafield");
				
				for (int i = 0; i < tagList.getLength(); i++) {

					Node tagNode = tagList.item(i);
					
					if (tagNode.getNodeType() == Node.ELEMENT_NODE) {
						Element tagElement = (Element) tagNode;
						
						MARCDataField datafield = new MARCDataField();
						
						datafield.ind1 = tagElement.getAttribute("ind1");
						datafield.ind2 = tagElement.getAttribute("ind2");
						datafield.tag = tagElement.getAttribute("tag");
						
						
						//System.out.print("tagID: " + datafield.tag +" ");
						//System.out.print("ind1: " +datafield.ind1 +" ");
						//System.out.println("idn2: "+datafield.ind2 );
					
					
					
							//and now the subfields
							NodeList subfieldList = tagElement.getElementsByTagName("subfield");
							for (int j = 0; j < subfieldList.getLength(); j++) {
		
								Node subfieldNode = subfieldList.item(j);
								
								
								if (subfieldNode.getNodeType() == Node.ELEMENT_NODE) {
									Element subfieldElement = (Element) subfieldNode;

									MARCDataFieldSubField subfield = new MARCDataFieldSubField();
									subfield.code = subfieldElement.getAttribute("code");
									subfield.content = subfieldElement.getTextContent();
									
									//System.out.println("subfield code: " + subfield.code);
									//System.out.println("subfield : " + subfield.content);
									
									datafield.subfields.add(subfield);

								}
							
							}
							
							
							record.datafields.add(datafield);
					}
				}
			}
			
			
			records.add(record);
			
		}	
		
		
		System.out.println("Total Records added in this batch:" +records.size()); 
		
		for(MARCRecord rcd: records){
			
			//System.out.println(rcd.createJSONRecord().toString(4));
		}
		
		 
		
		
		}catch(Exception e){
			
			
			
		}
		
		
		
	}


	public  static void LoadXMLFiles(String dir, ArrayList<MARCRecord> records ) {
		
		File folder = new File(dir);
		
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            //listFilesForFolder(fileEntry);
	        } else {
	            System.out.println(fileEntry.getPath());
	            try {

	        		LoadXMLFile(fileEntry.getPath(), records);

	            } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        
		}
	
	}
	
	
	
	

	public  static void LoadAndSaveXMLFiles(String dir, ArrayList<MARCRecord> records ) {
		
		File folder = new File(dir);
		int pt = 0;
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            //listFilesForFolder(fileEntry);
	        } else {
	            System.out.println(fileEntry.getPath());
	            try {

	        		LoadXMLFile(fileEntry.getPath(), records);
	    			saveJSONtoFile(records, 10000, pt);
	    			pt++;
	        		records.clear();
	        		
	            } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					records.clear();
				}
	        }
	        
		}
	
	}
	
	
	/**
	 * Filewriter to chunk the outputs into small enough files which wuill allow for them to be processsed int he future
	 * 
	 * @param records
	 */
	public static void saveJSONtoFile(ArrayList<MARCRecord> records, int maxRecordsPerFile, int pt) throws Exception{
		
		int cnt = 0;
		FileWriter fw = new FileWriter("output_pt_"+pt+".json");
		BufferedWriter bw = new BufferedWriter(fw);
		for(MARCRecord record : records){
			if(cnt > maxRecordsPerFile){
				pt++;
				cnt = 0;
				bw.close();
				fw.close();
				fw = new FileWriter("output_pt_"+pt+".json");
				bw = new BufferedWriter(fw);	
			}else{
				bw.write(record.createJSONRecord().toString()+"\n");
				cnt++ ;
			}
		
		}
		
		
	}
	
	
	
	
	public static void main(String[] args){
		
		
		

		
		ArrayList<MARCRecord> records = new ArrayList<>();
		LoadAndSaveXMLFiles("data", records);

		
	}

	
}
