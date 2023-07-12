package it.betacom.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	
	String filePath = ".\\archive\\PetShop_datiE.csv";

	public PetshopService() {}

	public void processCSVAndSaveData(Cliente cliente, Animale animale) {

		// EntityManager
		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Petshop");
		EntityManager entityManager = emFactory.createEntityManager();
		entityManager.getTransaction().begin();

		// Reading the CSV file
		List<CSVRecord> records = CSVReader.readCSV(filePath); // Acquisisco la lista di ritorno dalla funzione readCSV

		Map<String, Cliente> existingClienti = new HashMap<>(); // Mappo come chiave una String all'oggetto Cliente che
																// è una HashMap ovvero una lista di valori unici

		for (int i = 2; i < records.size(); i++) { // Faccio partire il for dal numero 2 per saltare le prime due righe

			CSVRecord record = records.get(i); // Assegno all'oggetto CSVRecord il valore di ogni riga di i

			String nomeCliente = record.get(0);
			String cognomeCliente = record.get(1);
			String clienteKey = nomeCliente + " " + cognomeCliente; // Assegno come valore alla chiave della mappa la
																	// concatenazione di Nome e Cognome

			if (existingClienti.containsKey(clienteKey)) { // Controllo che il cliente non sia gia presente, se presente
				cliente = existingClienti.get(clienteKey); // assegna al valore di cliente ricorrente di lettura  quello
															// gia esistente
			} else { // Altrimenti compila i campi mancanti qui sotto
				String cittaCliente = record.get(2);
				String telefonoCliente = record.get(3);
				String indirizzoCliente = record.get(4);

				cliente = new Cliente(); // Costruisco Cliente con i getter e setter
				cliente.setNome(nomeCliente);
				cliente.setCognome(cognomeCliente);
				cliente.setCitta(cittaCliente);
				cliente.setTelefono(telefonoCliente);
				cliente.setIndirizzo(indirizzoCliente);

				entityManager.persist(cliente);
				existingClienti.put(clienteKey, cliente); // Assegno alla mappa il valore del cliente corrente e la sua
															// chiave cosi che															// possa essere riconosciuta nel secondo giro di for
			}

			String tipoAnimale = record.get(5);
			String nomeAnimale = record.get(6);
			int matricola = 0;
			String matricolaString = record.get(7); 

			if (matricolaString != null && !matricolaString.isEmpty() && matricolaString.matches("\\d+")) {
				matricola = Integer.parseInt(matricolaString); 
																
			} // INSERIRE GESTIONE DELLA MATRICOLA PK 

			String dataAcquisto = record.get(8);
			int prezzo = 0;
			String prezzoString = record.get(9);
			if (prezzoString != null && !prezzoString.isEmpty() && prezzoString.matches("\\d+")) {
				prezzo = Integer.parseInt(prezzoString);
			} else
				prezzo = 0;

			animale = new Animale(); // Costruisco Animale con i getter e setter
			animale.setTipoAnimale(tipoAnimale);
			animale.setNomeAnimale(nomeAnimale);
			animale.setMatricola(matricola);
			animale.setDataAcquisto(dataAcquisto);
			animale.setPrezzo(prezzo);
			animale.setCliente(cliente);
			entityManager.persist(animale);

		}

		// REPORT 1
		/*
		 * Cliente: Marco Rossi .... Data - matricola animale1 - nome - prezzo di
		 * vendita
		 */
		Query queryReport1 = entityManager.createQuery("SELECT c.nome, c.cognome, c.telefono, a.dataAcquisto, a.matricola, a.nomeAnimale, a.prezzo FROM Cliente c JOIN c.animales a");
		@SuppressWarnings("unchecked")
		List<Object[]> resultsReport1 = queryReport1.getResultList();
		String path = ".\\archive\\reports";
		Path directoryPath = Paths.get(path);

		if (!Files.exists(directoryPath)) {
			try {
				Files.createDirectories(directoryPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (Object[] row : resultsReport1) {
			String nome = (String) row[0];
			String cognome = (String) row[1];
			String telefono = (String) row[2];
			String data_acquisto = (String) row[3];
			int matricola = (int) row[4];
			String nomeAnimale = (String) row[5];
			int prezzo = (int) row[6];
			String report1FilePath = path + "\\" + nome + "_" + cognome + ".txt";
			try (PrintWriter writer = new PrintWriter(new FileWriter(report1FilePath, true))) {
				writer.printf("Nome: " + nome + " ");
				writer.printf("Cognome: " + cognome + " ");
				writer.printf("Telefono: " + telefono + "\n");
				writer.printf("Data di acquisto: " + data_acquisto + " ");
				writer.printf("matricola: " + matricola + " ");
				writer.printf("Nome animale: " + nomeAnimale + " ");
				writer.printf("prezzo: " + prezzo + "\n");
				writer.println(); // Aggiunge una riga vuota

				System.out.println(
						"I dati sono stati aggiunti al file di testo per " + nome + " " + cognome + " |Report1");
			} catch (IOException e) {
				System.out.println(
						"Si è verificato un errore durante l'aggiunta al file di testo per " + nome + " " + cognome);
				e.printStackTrace();
			}

		}

		// REPORT 2
		/*
		 * Report2: lista degli animali venduti ordinati per data di acquisto e per ogni
		 * acquisto i dati del cliente Data di acquisto - Animale1 -nome -matricola -
		 * nome del cliente- cellulare
		 */

		Query queryReport2 = entityManager
				.createQuery("SELECT a.dataAcquisto, a.nomeAnimale, a.matricola, c.nome, c.cognome, c.telefono\r\n"
						+ "FROM Cliente c\r\n" + "JOIN c.animales a\r\n" + "ORDER BY a.dataAcquisto ASC");
		@SuppressWarnings("unchecked")
		List<Object[]> resultsReport2 = queryReport2.getResultList();
		if (!Files.exists(directoryPath)) {
			try {
				Files.createDirectories(directoryPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String report2FilePath = path + "\\ListaVenditeTotali.txt";
		try (PrintWriter writer = new PrintWriter(new FileWriter(report2FilePath))) {
			for (Object[] row : resultsReport2) {
				String nome = (String) row[3];
				String cognome = (String) row[4];
				String telefono = (String) row[5];
				String data_acquisto = (String) row[0];
				int matricola = (int) row[2];
				String nomeAnimale = (String) row[1];

				if (nomeAnimale.isEmpty()) {
					nomeAnimale = "vuoto";
				}

				writer.printf("Nome: " + nome + " ");
				writer.printf("Cognome: " + cognome + " ");
				writer.printf("Telefono: " + telefono + "\n");
				writer.printf("Data di acquisto: " + data_acquisto + " ");
				writer.printf("matricola: " + matricola + " ");
				writer.printf("Nome animale: " + nomeAnimale + " \n");
				writer.println(); 

			}
			System.out.println("Il file di report 2 è stato creato: " + report2FilePath);
		} catch (IOException e) {
			System.out.println("Si è verificato un errore durante la creazione del file di report 2.");
			e.printStackTrace();
		}

		// Committo tutto sul database e chiuso le factory
		entityManager.getTransaction().commit();
		entityManager.close();
		emFactory.close();
	}

}
