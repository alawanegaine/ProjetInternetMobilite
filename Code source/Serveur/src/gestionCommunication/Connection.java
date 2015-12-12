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
 * Cette classe permet de g�rer les connexion TCP avec le serveur
 * @author Damien
 */
public class Connection implements Runnable{
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
	private static final Logger log = Logger.getLogger( FileTcp.class.getName() );
	/**
	 * Socket sur lequel on �coute
	 */
	private Socket clientSocket;
	/**
	 * Le canal d'entr�e avec le client
	 */
	private ObjectInputStream input;
	/**
	 * Le canal de sortie
	 */
	private ObjectOutputStream output;
	/**
	 * La classe permettant de faire le lien avec la base de donn�es
	 */
	private Database maBase;

	//////////////////////////////////////////////////////////////////
	//								METHODES						//
	//////////////////////////////////////////////////////////////////
	/**
	 * On �tablit une connexion avec le client
	 * @param Le socket sur lequel on est connect�
	 */
	public Connection(Socket s, Database bdd) {
		clientSocket = s;
		maBase = bdd;
		try {
			input = new ObjectInputStream(clientSocket.getInputStream());
			output = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			log.log(Level.SEVERE, "Probl�me pour r�cup�rer les flux d'entr�e/sortie", e);
		}
	}

	/**
	 * C'est ici que l'on va r�cup�rer l'objet "Requ�teMessage" envoy� par l'utilisateur, et choisir la m�thode apropri�e pour lui r�pondre
	 */
	@Override
	public void run() {
		RequeteMessage requete = null;
		RequeteMessage reponse = null;
		//On regarde si c'est le bon objet qui a �t� envoy�
		try {
			Object objet = input.readObject();
			if(objet instanceof RequeteMessage)
				requete = (RequeteMessage) objet;

			//On g�re l'objet RequeteMessage
			if(requete != null){
				System.out.println(" avec la m�thode " + requete.getMethode().toString());
				//Selon le type de requ�te envoy�
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
			log.log(Level.SEVERE,"Probl�me d'�criture sur le stream",e);
		} catch (ClassNotFoundException e) {
			log.log(Level.SEVERE, "Probl�me avec le readObject()", e);
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
