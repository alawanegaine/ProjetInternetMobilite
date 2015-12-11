package gestionCommunication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import data.Database;
import protocole.Methode;
import protocole.Point;
import protocole.RequeteMessage;
import protocole.RequetePoints;
import serveur.FileTcp;

/**
 * Cette classe permet de gérer les requêtes qui arrivent sur le serveur depuis le WebService
 * @author Damien
 */
public class RequeteHandler implements Runnable{
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
	 * Le message que l'on veut traiter
	 */
	private RequeteMessage requete;
	/**
	 * L'objet permettant de faire le lien avec la BDD
	 */
	private Database maBase;

	//////////////////////////////////////////////////////////////////
	//								METHODES						//
	//////////////////////////////////////////////////////////////////	
	/**
	 * Constructeur de base
	 * @param r Le message que l'on veut traiter
	 */
	public RequeteHandler(RequeteMessage r, Database bdd) {
		this.requete = r;
		this.maBase = bdd;
	}

	/**
	 * Permet de transmettre une requête contenant la liste des points
	 * @param messageAEnvoyer Le message que l'on veut transmettre
	 * @param addresse L'addresse IP à laquelle on envoit le message
	 * @param port Le port sur lequel on envois le message
	 */
	public void transmettreListePoints(RequetePoints messageAEnvoyer, String addresse, int port) throws UnknownHostException, IOException{
		//Création du scoket d'envois
		DatagramSocket aSocket = new DatagramSocket();
		try {
			byte[] buffer = RequetePoints.marshall(messageAEnvoyer);

			//On crée le paquet à renvoyer
			DatagramPacket out = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(addresse), port);

			aSocket.send(out);
			System.out.println("Réponse renvoyée à l'adresse : "+ addresse + " avec la méthode : envois des points au WebService");
		} catch (SocketException e) {
			log.log(Level.SEVERE, "Envois du message impossible : SocketException",e);
		} catch (IOException e) {
			log.log(Level.SEVERE, "Envois du message impossible : IOException",e);
		}
		finally {
			if (aSocket != null)
				//Si l'envois s'est fait avec succès, on ferme le socket
				aSocket.close();
		}
	}  

	/**
	 * La méthode qui permet de choisir ce que l'on doit faire en fonction du type de requête reçue
	 */
	@Override
	public void run() {
		if(requete.getMethode() == Methode.getListePoints){
			try {
				recupererListePoints();
			} catch (NumberFormatException e) {
				log.log(Level.SEVERE, "Problème sur le port : l'envois de la réponse n'a pas eu lieu", e);
			} catch (UnknownHostException e) {
				log.log(Level.SEVERE, "Problème sur le port : l'envois de la réponse n'a pas eu lieu", e);
			} catch (IOException e) {
				log.log(Level.SEVERE, "Problème sur le port : l'envois de la réponse n'a pas eu lieu", e);
			}
		}
	}

	/**
	 * Méthode permettant de récupérer la liste de points dans la base, et de renvoyer la réponse
	 */
	private void recupererListePoints() throws NumberFormatException, UnknownHostException, IOException {		
		//On va chercher les info dans notre BDD
		ArrayList<Point> listeDePoints = null;
		Date maDate = requete.getDateAjout();
		if(maDate != null)
			listeDePoints = maBase.getListePoints(maDate);

		//On crée notre réponse
		RequetePoints messageAEnvoyer = new RequetePoints();
		messageAEnvoyer.setListeDePoints(listeDePoints);

		//On envois la réponse
		transmettreListePoints(messageAEnvoyer, requete.getAdresseIp(), requete.getPort());
	}

}
