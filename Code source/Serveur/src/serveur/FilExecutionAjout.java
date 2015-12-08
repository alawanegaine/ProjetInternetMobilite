package serveur;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
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
	/**
	 * Socket sur lequel on �coute
	 */
	private DatagramSocket monSocket;
	/**
	 * Le nom de notre fil d'execution
	 */
	private String nomDuFil = "Fil d'�xecution ajout";
	
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
		try {
			monSocket = new DatagramSocket(ServeurInfo.getPortAjoutImage());
			System.out.println(nomDuFil + " est correctement d�marr�, au port " + ServeurInfo.getPortAjoutImage());
			byte[] buffer = new byte[10000];
			while(ServeurInfo.estEnMarche()){
				DatagramPacket paquet = new DatagramPacket(buffer, buffer.length);
				System.out.println(nomDuFil + " en attente d'une requ�te");
				monSocket.receive(paquet); //R�ception bloquante
				
			}
		} catch (SocketException e) {
			log.log(Level.WARNING, "Probl�me de cr�ation de socket", e);
		} catch (IOException e) {
			log.log(Level.WARNING, "Probl�me lors du receive(paquet)", e);
		}
	}
}
