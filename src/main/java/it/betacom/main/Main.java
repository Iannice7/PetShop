package it.betacom.main;

import it.betacom.model.Animale;
import it.betacom.model.Cliente;
import it.betacom.service.PetshopService;

public class Main {

	public static void main(String[] args) {

		PetshopService sv = new PetshopService();
		Cliente cliente  = new Cliente();
		Animale animale = new Animale();
		sv.processCSVAndSaveData(cliente,animale);
		sv.stampaReport1();
		sv.stampaReport2();
		sv.close();

	}

}
