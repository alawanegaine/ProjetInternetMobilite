package serveur;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cette classe va permettre de gérer les requêtes qui demandent un ajout de données dans la base / sur le serveur
 * @author Damien
 *
 */
public class FilExecutionAjout implements Runnable{
	//////////////////////////////////////////////////////////////////
	//								ATTRIBUTS						//
	//////////////////////////////////////////////////////////////////
	/**
	 * Logger servant à l'affichage des diverses informations
	 */
	private static final Logger log = Logger.getLogger( FilExecutionAjout.class.getName() );
	
	//////////////////////////////////////////////////////////////////
	//								METHODES						//
	//////////////////////////////////////////////////////////////////
	/**
	 * Constructeur vide (toutes les infos serveur sont disponibles grâce à la classe statique "ServeurInfo")
	 */
	public FilExecutionAjout() {
	}
	
	/**
	 * Gestion des requêtes
	 */
	@Override
	public void run() {
		System.out.println("Le fil d'execution d'ajout d'image est correctement démarré, au port " + ServeurInfo.getPortAjoutImage());
		while(true);
	}
}
