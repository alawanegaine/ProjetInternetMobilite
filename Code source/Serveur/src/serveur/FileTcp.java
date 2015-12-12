package serveur;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import data.Database;
import gestionCommunication.*;
import protocole.RequeteMessage;

/**
 * Cette classe va permettre de gérer les requêtes qui demandent un ajout de données dans la base / sur le serveur
 * @author Damien
 *
 */
public class FileTcp implements Runnable{
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
	private static final Logger log = Logger.getLogger( FileTcp.class.getName() );
	/**
	 * Socket sur lequel on écoute
	 */
	private ServerSocket monSocket;
	/**
	 * Le nom de notre fil d'execution
	 */
	private String nomDuFil = "Fil TCP";
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
	public FileTcp(Database bdd) {
		this.maBase = bdd;
	}
	
	/**
	 * Gestion des requêtes
	 */
	@Override
	public void run() {
		ExecutorService execute = Executors.newCachedThreadPool(); //On crée un pool de thread avec cache (taille variable, 60s to death)
		
		try {
			monSocket = new ServerSocket(ServeurInfo.getPortTcp());
			System.out.println(nomDuFil + " est correctement démarré, au port " + ServeurInfo.getPortTcp());
			while(ServeurInfo.estEnMarche()){
				Socket clientSocket = monSocket.accept(); ///Connexion bloquante
				System.out.print("Connexion reçue depuis l'adresse " + clientSocket.getInetAddress());
				
				//On lance le traitement de notre requête (Thread-per-request) en l'ajoutant dans notre pool de Thread
				execute.submit(new Connection(clientSocket, maBase));				
			}
		} catch (SocketException e) {
			log.log(Level.WARNING, "Problème de création de socket", e);
		} catch (IOException e) {
			log.log(Level.WARNING, "Problème lors du receive(paquet)", e);
		}
	}
}
