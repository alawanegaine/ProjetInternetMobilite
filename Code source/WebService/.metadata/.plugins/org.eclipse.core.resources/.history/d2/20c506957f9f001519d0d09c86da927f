package protocole;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang3.SerializationUtils;


/**
 * La requ�te qui sera transmise au WebService. C'est elle qui contiendra la liste des points pr�sents dans la base
 * @author Damien
 */
public class RequetePoints implements Serializable{
	//////////////////////////////////////////////////////////////////
	//								ATTRIBUTS						//
	//////////////////////////////////////////////////////////////////
	/**
	 * L'ensemble des points contenu dans la base depuis une date donn�e
	 */
	private ArrayList<Point> listeDePoints;
	
	//////////////////////////////////////////////////////////////////
	//								METHODES						//
	//////////////////////////////////////////////////////////////////	
	/**
	 * Constructeur par d�faut
	 */
	public RequetePoints() {
		
	}
	
	/**
	 * Permet la d�-s�rialisation de la requ�te
	 * @param requete La requ�te (RequetePoint) s�rialis�e 
	 * @return L'objet RequetePoint
	 */
    public static RequetePoints unmarshall(byte[] requete) {
        return SerializationUtils.deserialize(requete);
	}
    
    /**
     * Permet la s�rialisation de la requ�te
     * @param requete L'objet RequetePoint
     * @return La requ�te (RequetePoint) s�rialis�e
     */
	@SuppressWarnings("empty-statement")
	public static byte[] marshall(RequetePoints requete) {
	        return SerializationUtils.serialize((Serializable) requete);
	}
	
	/**
	 * @return L'ensemble des points contenu dans la base depuis une date donn�e
	 */
	public ArrayList<Point> getListeDePoints() {
		return listeDePoints;
	}
}
