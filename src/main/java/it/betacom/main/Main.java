package it.betacom.main;
import it.betacom.service.PetshopService;

public class Main {

	public static void main(String[] args) {

		PetshopService sv = new PetshopService();
		sv.processCSVAndSaveData();
		sv.stampaReport1();
		sv.stampaReport2();
		sv.close();
		
		
	}

}
