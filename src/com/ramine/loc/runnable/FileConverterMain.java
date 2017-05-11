package com.ramine.loc.runnable;

import java.util.ArrayList;

import com.ramine.loc.objects.MARC.MARCRecord;
import com.ramine.loc.operations.FileOperations;

public class FileConverterMain {

	public static void main(String[] args) {
		ArrayList<MARCRecord> records = new ArrayList<>();
		FileOperations.LoadAndSaveXMLFiles("data", records);
	}

}
