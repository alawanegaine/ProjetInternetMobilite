package serveur;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cette classe va permettre de gérer les requêtes qui demandent des données stockées dans la base / sur le serveur
 * @author Damien
 *
 */
public class FilExecutionRequete implements Runnable{
	//////////////////////////////////////////////////////////////////
	//								ATTRIBUTS						//
	//////////////////////////////////////////////////////////////////
	/**
	 * Logger servant à l'affichage des diverses informations
	 */
	private static final Logger log = Logger.getLogger( FilExecutionRequete.class.getName() );	
	
	//////////////////////////////////////////////////////////////////
	//								METHODES						//
	//////////////////////////////////////////////////////////////////	
	
	/**
	 * Constructeur vide (toutes les infos serveur sont disponibles grâce à la classe statique "ServeurInfo")
	 */
	public FilExecutionRequete() {
	}
	
	/**
	 * Gestion des requêtes
	 */
	@Override
	public void run() {
		System.out.println("Le fil d'execution gérant les demandes de données est correctement démarré, au port " + ServeurInfo.getPortRequeteImage());
		while(true);
	}

}
