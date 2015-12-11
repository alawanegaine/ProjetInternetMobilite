package serveur;

import java.util.logging.Logger;

import data.Database;

/**
 * Cette classe permet de démarrer nos fils d'execution dans deux threads différents
 * Contient la méthode main de notre programme
 * @author Damien
 *
 */
public class Serveur {
	//////////////////////////////////////////////////////////////////
	//								ATTRIBUTS						//
	//////////////////////////////////////////////////////////////////
	/**
	 * Numéro de version
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Logger servant à l'affichage des diverses informations
	 */
	private static final Logger log = Logger.getLogger(Serveur.class.getName() );	
	/**
	 * Le fil gérant les requêtes d'ajout de données dans la base
	 */
	private static FileTcp filExecutionAjout;
	/**
	 * Le fil gérant les requêtes demandant des données de la base
	 */
	private static FileUdp filExecutionRequete;
	/**
	 * L'objet permettant de faire le lien avec la BDD
	 */
	private static Database maBase;
	
	//////////////////////////////////////////////////////////////////
	//								METHODES						//
	//////////////////////////////////////////////////////////////////
	
	/**
	 * On lance le serveur
	 * @param args
	 */
	public static void main(String[] args) {
		demarrerServeur();
	}
	
	/**
	 * Méthode statique permettant le démarrage de notre serveur
	 * On lance les deux fil d'execution
	 * @return 
	 * 
	 */
	private static void demarrerServeur(){	
		//On crée l'objet de connexion à la base
		maBase = new Database();
		maBase.runTest();
		
		
		//On change l'état du serveur
		System.out.println("Démarrage du serveur");
		ServeurInfo.setEnMarche(true);
		
		//On initialise les fils d'execution
		filExecutionAjout = new FileTcp(maBase);
		filExecutionRequete = new FileUdp(maBase);
		
		//On démarre les fils d'execution
		new Thread(filExecutionAjout).start();
		new Thread(filExecutionRequete).start();
	}
}
