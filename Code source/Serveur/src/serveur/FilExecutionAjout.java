package serveur;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cette classe va permettre de g�rer les requ�tes qui demandent un ajout de donn�es dans la base / sur le serveur
 * @author Damien
 *
 */
public class FilExecutionAjout implements Runnable{
	//////////////////////////////////////////////////////////////////
	//								ATTRIBUTS						//
	//////////////////////////////////////////////////////////////////
	/**
	 * Logger servant � l'affichage des diverses informations
	 */
	private static final Logger log = Logger.getLogger( FilExecutionAjout.class.getName() );
	
	//////////////////////////////////////////////////////////////////
	//								METHODES						//
	//////////////////////////////////////////////////////////////////
	/**
	 * Constructeur vide (toutes les infos serveur sont disponibles gr�ce � la classe statique "ServeurInfo")
	 */
	public FilExecutionAjout() {
	}
	
	/**
	 * Gestion des requ�tes
	 */
	@Override
	public void run() {
		System.out.println("Le fil d'execution d'ajout d'image est correctement d�marr�, au port " + ServeurInfo.getPortAjoutImage());
		while(true);
	}
}
