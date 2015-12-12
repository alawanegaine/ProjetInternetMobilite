package serveur;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import data.Database;
import gestionCommunication.RequeteHandler;
import protocole.RequeteMessage;

/**
 * Cette classe va permettre de g�rer les requ�tes d'�change de donn�es de faible poid (avec le WebService)
 * @author Damien
 */
public class FileUdp implements Runnable{
	//////////////////////////////////////////////////////////////////
	//								ATTRIBUTS						//
	//////////////////////////////////////////////////////////////////
	/**
	 * Num�ro de version
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Logger servant � l'affichage des diverses informations
	 */
	private static final Logger log = Logger.getLogger( FileUdp.class.getName() );	
	/**
	 * Socket sur lequel on �coute
	 */
	private DatagramSocket monSocket;
	/**
	 * Le nom de notre fil d'execution
	 */
	private String nomDuFil = "Fil UDP";
	/**
	 * L'objet permettant de faire le lien avec la BDD
	 */
	private Database maBase;
	
	//////////////////////////////////////////////////////////////////
	//								METHODES						//
	//////////////////////////////////////////////////////////////////	
	/**
	 * Constructeur vide (toutes les infos serveur sont disponibles gr�ce � la classe statique "ServeurInfo")
	 */
	public FileUdp(Database bdd) {
		this.maBase = bdd;
	}
	
	/**
	 * Gestion des requ�tes
	 */
	@Override
	public void run() {	
		ExecutorService execute = Executors.newCachedThreadPool(); //On cr�e un pool de thread avec cache (taille variable, 60s to death)
		
		try {
			monSocket = new DatagramSocket(ServeurInfo.getPortUdp());
			System.out.println(nomDuFil + " est correctement d�marr�, au port " + ServeurInfo.getPortUdp());
			byte[] buffer = new byte[10000];
			while(ServeurInfo.estEnMarche()){
				DatagramPacket paquet = new DatagramPacket(buffer, buffer.length);
				System.out.println(nomDuFil + " en attente d'une requ�te");
				monSocket.receive(paquet); //R�ception bloquante
				
				//On d�s�rialise la paquet, on r�cup�re le message
				RequeteMessage requete = RequeteMessage.unmarshall(paquet.getData());
				System.out.println("Requ�te re�u de " + requete.getAdresseIp() + " avec la m�thode " + requete.getMethode());
				
				//On lance le traitement de notre requ�te (Thread-per-request) en l'ajoutant dans notre pool de Thread
				execute.submit(new RequeteHandler(requete, maBase));
			}
		} catch (SocketException e) {
			log.log(Level.WARNING, "Probl�me de cr�ation de socket", e);
		} catch (IOException e) {
			log.log(Level.WARNING, "Probl�me lors du receive(paquet)", e);
		}
	}

}
