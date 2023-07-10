package it.betacom.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
	    List<CSVRecord> records = CSVReader.readCSV(filePath);
	    
	    Map<String, Cliente> existingClienti = new HashMap<>();

	    for (int i = 2; i < records.size(); i++) {
	        CSVRecord record = records.get(i);

	        String nomeCliente = record.get(0);
	        String cognomeCliente = record.get(1);
	        String clienteKey = nomeCliente + " " + cognomeCliente;

	        if (existingClienti.containsKey(clienteKey)) {
	            cliente = existingClienti.get(clienteKey);
	        } else {
	            String cittaCliente = record.get(2);
	            String telefonoCliente = record.get(3);
	            String indirizzoCliente = record.get(4);

	            cliente = new Cliente();
	            cliente.setNome(nomeCliente);
	            cliente.setCognome(cognomeCliente);
	            cliente.setCitta(cittaCliente);
	            cliente.setTelefono(telefonoCliente);
	            cliente.setIndirizzo(indirizzoCliente);

	            entityManager.persist(cliente);
	            existingClienti.put(clienteKey, cliente);
	        }

	        String tipoAnimale = record.get(5);
	        String nomeAnimale = record.get(6);
	        int matricola = 0;
	        String matricolaString = record.get(7);
	        if (matricolaString != null && !matricolaString.isEmpty() && matricolaString.matches("\\d+")) {
	            matricola = Integer.parseInt(matricolaString);
	        }
	        String dataAcquisto = record.get(8);
	        int prezzo = 0;
	        String prezzoString = record.get(9);
	        if (prezzoString != null && !prezzoString.isEmpty() && prezzoString.matches("\\d+")) {
	            prezzo = Integer.parseInt(prezzoString);
	        }

	        animale = new Animale();
	        animale.setTipoAnimale(tipoAnimale);
	        animale.setNomeAnimale(nomeAnimale);
	        animale.setMatricola(matricola);
	        animale.setDataAcquisto(dataAcquisto);
	        animale.setPrezzo(prezzo);
	        animale.setCliente(cliente);
	        entityManager.persist(animale);

	        System.out.println(cliente);
	        System.out.println(animale);
	    }

	    entityManager.getTransaction().commit();
	    entityManager.close();
	    emFactory.close();
	}


}
