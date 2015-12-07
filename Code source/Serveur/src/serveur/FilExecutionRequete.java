package serveur;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cette classe va permettre de g�rer les requ�tes qui demandent des donn�es stock�es dans la base / sur le serveur
 * @author Damien
 *
 */
public class FilExecutionRequete implements Runnable{
	//////////////////////////////////////////////////////////////////
	//								ATTRIBUTS						//
	//////////////////////////////////////////////////////////////////
	/**
	 * Logger servant � l'affichage des diverses informations
	 */
	private static final Logger log = Logger.getLogger( FilExecutionRequete.class.getName() );	
	
	//////////////////////////////////////////////////////////////////
	//								METHODES						//
	//////////////////////////////////////////////////////////////////	
	
	/**
	 * Constructeur vide (toutes les infos serveur sont disponibles gr�ce � la classe statique "ServeurInfo")
	 */
	public FilExecutionRequete() {
	}
	
	/**
	 * Gestion des requ�tes
	 */
	@Override
	public void run() {
		System.out.println("Le fil d'execution g�rant les demandes de donn�es est correctement d�marr�, au port " + ServeurInfo.getPortRequeteImage());
		while(true);
	}

}
