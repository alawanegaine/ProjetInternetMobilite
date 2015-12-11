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
import gestionRequete.RequeteHandler;
import protocole.RequeteMessage;

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
	 * Num�ro de version
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Logger servant � l'affichage des diverses informations
	 */
	private static final Logger log = Logger.getLogger( FilExecutionRequete.class.getName() );	
	/**
	 * Socket sur lequel on �coute
	 */
	private DatagramSocket monSocket;
	/**
	 * Le nom de notre fil d'execution
	 */
	private String nomDuFil = "Fil d'�xecution requ�te";
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
	public FilExecutionRequete(Database bdd) {
		this.maBase = bdd;
	}
	
	/**
	 * Gestion des requ�tes
	 */
	@Override
	public void run() {	
		ExecutorService execute = Executors.newCachedThreadPool(); //On cr�e un pool de thread avec cache (taille variable, 60s to death)
		
		try {
			monSocket = new DatagramSocket(ServeurInfo.getPortRequeteImage());
			System.out.println("Le fil d'execution g�rant les demandes de donn�es est correctement d�marr�, au port " + ServeurInfo.getPortRequeteImage());
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
