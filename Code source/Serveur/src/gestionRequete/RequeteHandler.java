package gestionRequete;

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
import protocole.Point;
import protocole.RequeteMessage;
import protocole.RequetePoints;
import serveur.FilExecutionAjout;

/**
 * Cette classe permet de g�rer les requ�tes qui arrivent sur le serveur (de type RequeteMessage)
 * @author Damien
 */
public class RequeteHandler implements Runnable{
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
	private static final Logger log = Logger.getLogger( FilExecutionAjout.class.getName() );
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
	 * Permet de transmettre une requ�te de r�ponse
	 * @param messageAEnvoyer Le message que l'on veut transmettre
	 * @param addresse L'addresse IP � laquelle on envoit le message
	 * @param port Le port sur lequel on envois le message
	 */
	public void transmettreMessage(RequeteMessage messageAEnvoyer, String addresse, int port) throws UnknownHostException, IOException{
		//Cr�ation du scoket d'envois
		DatagramSocket aSocket = new DatagramSocket();
		try {
			byte[] buffer = RequeteMessage.marshall(messageAEnvoyer);

			//On cr�e le paquet � renvoyer
			DatagramPacket out = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(addresse), port);
			aSocket.send(out);
			System.out.println("R�ponse renvoy�e � l'adresse : "+ addresse + " avec la m�thode : " + messageAEnvoyer.getMethode().toString());
		} catch (SocketException e) {
			log.log(Level.SEVERE, "Envois du message impossible : SocketException",e);
		} catch (IOException e) {
			log.log(Level.SEVERE, "Envois du message impossible : IOException",e);
		}
		finally {
			if (aSocket != null)
				//Si l'envois s'est fait avec succ�s, on ferme le socket
				aSocket.close();
		}
	}

	/**
	 * Permet de transmettre une requ�te contenant la liste des points
	 * @param messageAEnvoyer Le message que l'on veut transmettre
	 * @param addresse L'addresse IP � laquelle on envoit le message
	 * @param port Le port sur lequel on envois le message
	 */
	public void transmettreListePoints(RequetePoints messageAEnvoyer, String addresse, int port) throws UnknownHostException, IOException{
		//Cr�ation du scoket d'envois
		DatagramSocket aSocket = new DatagramSocket();
		try {
			byte[] buffer = RequetePoints.marshall(messageAEnvoyer);

			//On cr�e le paquet � renvoyer
			DatagramPacket out = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(addresse), port);

			aSocket.send(out);
			System.out.println("R�ponse renvoy�e � l'adresse : "+ addresse + " avec la m�thode : envois des points au WebService");
		} catch (SocketException e) {
			log.log(Level.SEVERE, "Envois du message impossible : SocketException",e);
		} catch (IOException e) {
			log.log(Level.SEVERE, "Envois du message impossible : IOException",e);
		}
		finally {
			if (aSocket != null)
				//Si l'envois s'est fait avec succ�s, on ferme le socket
				aSocket.close();
		}
	}  

	/**
	 * La m�thode qui permet de choisir ce que l'on doit faire en fonction du type de requ�te re�ue
	 */
	@Override
	public void run() {
		switch (requete.getMethode()){
		//Cas o� l'on veut ajouter une image 
		case add :
			break;
		//Cas ou l'on veut r�cup�rer les informations relatives � un point
		case getInfosPoint :
			try {
				recupererInfosPoint();
			} catch (IOException e) {
				log.log(Level.SEVERE, "Probl�me sur le port : l'envois de la r�ponse n'a pas eu lieu", e);
			}
			break;
		//Cas ou l'on veut r�cup�rer la liste des points
		case getListePoints :
			try {
				recupererListePoints();
			} catch (NumberFormatException e) {
				log.log(Level.SEVERE, "Probl�me sur le port : l'envois de la r�ponse n'a pas eu lieu", e);
			} catch (UnknownHostException e) {
				log.log(Level.SEVERE, "Probl�me sur le port : l'envois de la r�ponse n'a pas eu lieu", e);
			} catch (IOException e) {
				log.log(Level.SEVERE, "Probl�me sur le port : l'envois de la r�ponse n'a pas eu lieu", e);
			}
			break;
		}
	}

	/**
	 * M�thode permettant de r�cup�rer toutes les informations relatives � un point
	 */
	private void recupererInfosPoint() throws UnknownHostException, IOException {
		//On cr�e la r�ponse
		RequeteMessage reponse = null;
		int idPhoto = -1;
		idPhoto = requete.getIdPhoto();
		if(idPhoto != -1)
			reponse = maBase.getInfosPoint(idPhoto);

		//On envois la r�ponse
		transmettreMessage(reponse, requete.getAdresseIp(), requete.getPort());
	}

	/**
	 * M�thode permettant de r�cup�rer la liste de points dans la base, et de renvoyer la r�ponse
	 */
	private void recupererListePoints() throws NumberFormatException, UnknownHostException, IOException {		
		//On va chercher les info dans notre BDD
		ArrayList<Point> listeDePoints = null;
		Date maDate = requete.getDateAjout();
		if(maDate != null)
			listeDePoints = maBase.getListePoints(maDate);

		//On cr�e notre r�ponse
		RequetePoints messageAEnvoyer = new RequetePoints();
		messageAEnvoyer.setListeDePoints(listeDePoints);

		//On envois la r�ponse
		transmettreListePoints(messageAEnvoyer, requete.getAdresseIp(), requete.getPort());
	}

}
