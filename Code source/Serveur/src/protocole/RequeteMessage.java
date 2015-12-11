package protocole;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang3.SerializationUtils;

/**
 * La requ�te qui servira � dialoguer avec le serveur. H�rite de la classe Message
 * @author Damien
 */
public class RequeteMessage extends Message {
	//////////////////////////////////////////////////////////////////
	//								ATTRIBUTS						//
	//////////////////////////////////////////////////////////////////
	/**
	 * Num�ro de version
	 */
	private static final long serialVersionUID = 1L;
	
	//////////////////////////////////////////////////////////////////
	//								METHODES						//
	//////////////////////////////////////////////////////////////////	
	
	/**
	 * Constructeur par d�faut
	 */
	public RequeteMessage() {
		
	}
	
	/**
	 * Permet la d�-s�rialisation de la requ�te
	 * @param requete La requ�te (RequeteMessage) s�rialis�e 
	 * @return L'objet RequeteMessage
	 */
    public static RequeteMessage unmarshall(byte[] requete) {
        return SerializationUtils.deserialize(requete);
	}
    
    /**
     * Permet la s�rialisation de la requ�te
     * @param requete L'objet RequeteMessage
     * @return La requ�te (RequeteMessage) s�rialis�e
     */
	@SuppressWarnings("empty-statement")
	public static byte[] marshall(RequeteMessage requete) {
	        return SerializationUtils.serialize((Serializable) requete);
	}
}
