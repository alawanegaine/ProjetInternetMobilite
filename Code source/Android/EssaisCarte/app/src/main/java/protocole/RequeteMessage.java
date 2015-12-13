package protocole;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

/**
 * La requête qui servira à dialoguer avec le serveur. Hérite de la classe Message
 * @author Damien
 */
public class RequeteMessage extends Message {
	//////////////////////////////////////////////////////////////////
	//								ATTRIBUTS						//
	//////////////////////////////////////////////////////////////////
	/**
	 * Numéro de version
	 */
	private static final long serialVersionUID = 1L;

	//////////////////////////////////////////////////////////////////
	//								METHODES						//
	//////////////////////////////////////////////////////////////////	

	/**
	 * Constructeur par défaut
	 */
	public RequeteMessage() {

	}

	/**
	 * Permet la dé-sérialisation de la requête
	 * @param requete La requête (RequeteMessage) sérialisée 
	 * @return L'objet RequeteMessage
	 */
	public static RequeteMessage unmarshall(byte[] requete) {
		return (RequeteMessage) SerializationUtils.deserialize(requete);
	}

	/**
	 * Permet la sérialisation de la requête
	 * @param requete L'objet RequeteMessage
	 * @return La requête (RequeteMessage) sérialisée
	 */
	@SuppressWarnings("empty-statement")
	public static byte[] marshall(RequeteMessage requete) {
		return SerializationUtils.serialize((Serializable) requete);
	}
}