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
 * Cette classe va permettre de gérer les requêtes d'échange de données de faible poid (avec le WebService)
 * @author Damien
 */
public class FileUdp implements Runnable{
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
	private static final Logger log = Logger.getLogger( FileUdp.class.getName() );	
	/**
	 * Socket sur lequel on écoute
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
	 * Constructeur vide (toutes les infos serveur sont disponibles grâce à la classe statique "ServeurInfo")
	 */
	public FileUdp(Database bdd) {
		this.maBase = bdd;
	}
	
	/**
	 * Gestion des requêtes
	 */
	@Override
	public void run() {	
		ExecutorService execute = Executors.newCachedThreadPool(); //On crée un pool de thread avec cache (taille variable, 60s to death)
		
		try {
			monSocket = new DatagramSocket(ServeurInfo.getPortUdp());
			System.out.println(nomDuFil + " est correctement démarré, au port " + ServeurInfo.getPortUdp());
			byte[] buffer = new byte[10000];
			while(ServeurInfo.estEnMarche()){
				DatagramPacket paquet = new DatagramPacket(buffer, buffer.length);
				System.out.println(nomDuFil + " en attente d'une requête");
				monSocket.receive(paquet); //Réception bloquante
				
				//On désérialise la paquet, on récupère le message
				RequeteMessage requete = RequeteMessage.unmarshall(paquet.getData());
				System.out.println("Requête reçu de " + requete.getAdresseIp() + " avec la méthode " + requete.getMethode());
				
				//On lance le traitement de notre requête (Thread-per-request) en l'ajoutant dans notre pool de Thread
				execute.submit(new RequeteHandler(requete, maBase));
			}
		} catch (SocketException e) {
			log.log(Level.WARNING, "Problème de création de socket", e);
		} catch (IOException e) {
			log.log(Level.WARNING, "Problème lors du receive(paquet)", e);
		}
	}

}
