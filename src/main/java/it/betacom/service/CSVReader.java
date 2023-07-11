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
			Iterable<CSVRecord> parsedRecords = CSVFormat.DEFAULT.withDelimiter(';').parse(in); //Assegna ad un oggetto Iterabule il valore di ogni record letto
            for (CSVRecord record : parsedRecords) {
            	if(record!=null) {
                    records.add(record);  //e successivamente lo aggiunge alla Lista record che contiene gli oggetti di tipo CSVRecord
            	}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;		//Ritorna la lista dei record letti dal file
    }
    
}
