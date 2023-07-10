package it.betacom.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.commons.csv.CSVRecord;
import it.betacom.model.Animale;
import it.betacom.model.Cliente;

public class PetshopService {
	String filePath = "C:\\Workspace\\Petshop\\archive\\PetShop_datiE.csv";

	public PetshopService() {}
	public void processCSVAndSaveData(Cliente cliente, Animale animale) {
		
	    // EntityManager 
	    EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Petshop");
	    EntityManager entityManager = emFactory.createEntityManager();
	    entityManager.getTransaction().begin();

	    // Reading the CSV file
	    List<CSVRecord> records = CSVReader.readCSV(filePath); //Acquisisco la lista di ritorno dalla funzione readCSV
	    
	    Map<String, Cliente> existingClienti = new HashMap<>(); //Mappo come chiave una String all'oggetto Cliente che è una HashMap ovvero una lista di valori unici

	    
	    for (int i = 2; i < records.size(); i++) { //Faccio partire il for dal numero 2 per saltare le prime due righe
	    	
	        CSVRecord record = records.get(i); //Assegno all'oggetto CSVRecord il valore di ogni riga di i

	        String nomeCliente = record.get(0);
	        String cognomeCliente = record.get(1);
	        String clienteKey = nomeCliente + " " + cognomeCliente; //Assegno come valore alla chiave della mappa la concatenazione di Nome e Cognome

	        if (existingClienti.containsKey(clienteKey)) { //Controllo che il cliente non sia gia presente, se presente
	            cliente = existingClienti.get(clienteKey); //assegna al valore di cliente ricorrente di lettura quello gia esistente
	        } else {										//Altrimenti compila i campi mancanti qui sotto
	            String cittaCliente = record.get(2);
	            String telefonoCliente = record.get(3);
	            String indirizzoCliente = record.get(4);

	            cliente = new Cliente();				//Costruisco Cliente con i getter e setter
	            cliente.setNome(nomeCliente);
	            cliente.setCognome(cognomeCliente);
	            cliente.setCitta(cittaCliente);
	            cliente.setTelefono(telefonoCliente);
	            cliente.setIndirizzo(indirizzoCliente);

	            entityManager.persist(cliente);
	            existingClienti.put(clienteKey, cliente);	//Assegno alla mappa il valore del cliente corrente e la sua chiave cosi che 
	            											// possa essere riconosciuta nel secondo giro di for
	        }
	        
	        
	        String tipoAnimale = record.get(5);
	        String nomeAnimale = record.get(6);
	        int matricola = 0;
	        String matricolaString = record.get(7);
	        
	        if (matricolaString != null && !matricolaString.isEmpty() && matricolaString.matches("\\d+")) {
	            matricola = Integer.parseInt(matricolaString);  //Questa roba lasciala stare che mi scoccia spiegarla di nuovo.
	        }//INSERIRE GESTIONE DELLA MATRICOLA PK
	        
	        String dataAcquisto = record.get(8);
	        int prezzo = 0;
	        String prezzoString = record.get(9);
	        if (prezzoString != null && !prezzoString.isEmpty() && prezzoString.matches("\\d+")) {
	            prezzo = Integer.parseInt(prezzoString);
	        }else prezzo = 0;

	        animale = new Animale();					//Costruisco Animale con i getter e setter
	        animale.setTipoAnimale(tipoAnimale);
	        animale.setNomeAnimale(nomeAnimale);
	        animale.setMatricola(matricola);
	        animale.setDataAcquisto(dataAcquisto);
	        animale.setPrezzo(prezzo);
	        animale.setCliente(cliente);
	        entityManager.persist(animale);
	        
	        // Richiesta dell'esercizio numero 1 (FORSE PERCHE IL FILE NON C'E)

	        Query queryCliente = entityManager.createQuery("SELECT c.nome, c.cognome, c.telefono, a.nomeAnimale FROM Cliente c JOIN c.animales a");
	        List<Object[]> results = queryCliente.getResultList();
	        for (Object[] row : results) {
	            String nome = (String) row[0];
	            String cognome = (String) row[1];
	            String telefono = (String) row[2];
	            String nome_animale = (String) row[3];
	            
	            System.out.println("Nome: " + nome + "| Cognome: " + cognome + "| Telefono: " + telefono + "| Nome animale: " + nome_animale);
	        }
	        
	    }

	    //Committo tutto sul database e chiuso le factory
	    entityManager.getTransaction().commit();
	    entityManager.close();
	    emFactory.close();
	}
}
