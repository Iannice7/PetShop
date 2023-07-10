package it.betacom.service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public  class CSVReader {
	
    public static List<CSVRecord> readCSV(String filePath) {
        List<CSVRecord> records = new ArrayList<>();

        try {
            Reader in = new FileReader(filePath);          
            @SuppressWarnings("deprecation")
			Iterable<CSVRecord> parsedRecords = CSVFormat.DEFAULT.withDelimiter(';').parse(in);
            for (CSVRecord record : parsedRecords) {
            	if(record!=null) {
                    records.add(record);
            	}

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }
}
