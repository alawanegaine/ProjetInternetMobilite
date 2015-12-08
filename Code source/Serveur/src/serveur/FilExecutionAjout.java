package serveur;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
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
	/**
	 * Socket sur lequel on écoute
	 */
	private DatagramSocket monSocket;
	/**
	 * Le nom de notre fil d'execution
	 */
	private String nomDuFil = "Fil d'éxecution ajout";
	
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
		try {
			monSocket = new DatagramSocket(ServeurInfo.getPortAjoutImage());
			System.out.println(nomDuFil + " est correctement démarré, au port " + ServeurInfo.getPortAjoutImage());
			byte[] buffer = new byte[10000];
			while(ServeurInfo.estEnMarche()){
				DatagramPacket paquet = new DatagramPacket(buffer, buffer.length);
				System.out.println(nomDuFil + " en attente d'une requête");
				monSocket.receive(paquet); //Réception bloquante
				
			}
		} catch (SocketException e) {
			log.log(Level.WARNING, "Problème de création de socket", e);
		} catch (IOException e) {
			log.log(Level.WARNING, "Problème lors du receive(paquet)", e);
		}
	}
}
