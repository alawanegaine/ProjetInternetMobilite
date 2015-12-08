package serveur;

import java.util.logging.Logger;

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
	 * Logger servant à l'affichage des diverses informations
	 */
	private static final Logger log = Logger.getLogger(Serveur.class.getName() );	
	private static FilExecutionAjout filExecutionAjout;
	private static FilExecutionRequete filExecutionRequete;
	
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
		//On change l'état du serveur
		ServeurInfo.setEnMarche(true);
		
		//On initialise les fils d'execution
		filExecutionAjout = new FilExecutionAjout();
		filExecutionRequete = new FilExecutionRequete();
		
		//On démarre les fils d'execution
		new Thread(filExecutionAjout).start();
		new Thread(filExecutionRequete).start();
	}
}
