package gestionCommunication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.fabric.xmlrpc.base.Data;

import data.Database;
import protocole.RequeteMessage;
import serveur.FileTcp;

/**
 * Cette classe permet de gérer les connexion TCP avec le serveur
 * @author Damien
 */
public class Connection implements Runnable{
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
	private Socket clientSocket;
	/**
	 * Le canal d'entrée avec le client
	 */
	private ObjectInputStream input;
	/**
	 * Le canal de sortie
	 */
	private ObjectOutputStream output;
	/**
	 * La classe permettant de faire le lien avec la base de données
	 */
	private Database maBase;

	//////////////////////////////////////////////////////////////////
	//								METHODES						//
	//////////////////////////////////////////////////////////////////
	/**
	 * On établit une connexion avec le client
	 * @param Le socket sur lequel on est connecté
	 */
	public Connection(Socket s, Database bdd) {
		clientSocket = s;
		maBase = bdd;
		try {
			input = new ObjectInputStream(clientSocket.getInputStream());
			output = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			log.log(Level.SEVERE, "Problème pour récupérer les flux d'entrée/sortie", e);
		}
	}

	/**
	 * C'est ici que l'on va récupérer l'objet "RequêteMessage" envoyé par l'utilisateur, et choisir la méthode apropriée pour lui répondre
	 */
	@Override
	public void run() {
		RequeteMessage requete = null;
		RequeteMessage reponse = null;
		//On regarde si c'est le bon objet qui a été envoyé
		try {
			Object objet = input.readObject();
			if(objet instanceof RequeteMessage)
				requete = (RequeteMessage) objet;

			//On gère l'objet RequeteMessage
			if(requete != null){
				System.out.println(" avec la méthode " + requete.getMethode().toString());
				//Selon le type de requête envoyé
				switch(requete.getMethode()){
				case getInfosPoint :
					reponse = maBase.getInfosPoint(requete.getIdPhoto());
					output.writeObject(reponse);
					break;
				case add :
					reponse = maBase.ajouterPoint(requete);
					output.writeObject(reponse);
					break;
				}
			}
		} catch (IOException e) {
			log.log(Level.SEVERE,"Problème d'écriture sur le stream",e);
		} catch (ClassNotFoundException e) {
			log.log(Level.SEVERE, "Problème avec le readObject()", e);
		}

		//On ferme la connexion avec le socket
		finally {
			try {
				clientSocket.close();
			}
			catch (IOException e) {
				log.log(Level.SEVERE, "Close failed", e);
			}
		}
	}
}
