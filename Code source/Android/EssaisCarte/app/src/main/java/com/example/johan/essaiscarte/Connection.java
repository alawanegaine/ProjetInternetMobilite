package com.example.johan.essaiscarte;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;




import protocole.RequeteMessage;

public class Connection implements Runnable {
	/**
	 * Logger servant � l'affichage des diverses informations
	 */
	private static final Logger log = Logger.getLogger( Connection.class.getName() );
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Socket serveurSocket;
	private RequeteMessage requete;

	/**
	 * Connection avec le serveur
	 * @param socket Le socket sur lequel on est connect� au serveur
	 */
	public Connection(Socket socket, RequeteMessage r) {
			serveurSocket = socket;
			requete = r;

			try {
				output = new ObjectOutputStream(serveurSocket.getOutputStream());
				input = new ObjectInputStream(serveurSocket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
			output.writeObject(requete);
			Object objet = null;
			try {
				objet = input.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			//Gestion de la r�ponse
			if(objet instanceof RequeteMessage && objet != null){
				RequeteMessage requete = (RequeteMessage) objet;

			} else if(objet == null){

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(serveurSocket!=null)
				try {serveurSocket.close();
				}
			catch (IOException e) {
				log.log(Level.SEVERE, "Close failed", e);
			}
		}

	}

}
