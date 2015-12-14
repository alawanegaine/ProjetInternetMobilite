package protocole;

import java.io.Serializable;
import java.util.Date;

/**
 * L'objet qui sera renvoyé au WebService
 * @author Damien
 */
public class Point implements Serializable{
	//////////////////////////////////////////////////////////////////
	//								ATTRIBUTS						//
	//////////////////////////////////////////////////////////////////
	/**
	 * Numéro de version
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * La longitude du point
	 */
	private String longitude;
	/**
	 * La latitude du point
	 */
	private String latitude;
	/**
	 * L'id de l'enregistrement dans la base
	 */
	private int idPhoto;
	/**
	 * La date d'ajout de l'enregistrement dans la base
	 */
	private Date dateAjout;
	
	//////////////////////////////////////////////////////////////////
	//								METHODES						//
	//////////////////////////////////////////////////////////////////		
	/**
	 * Constructeur de notre objet point
	 * @param lo La longitude du point
	 * @param la La latitude du point
	 * @param id L'id de l'enregistrement dans la base
	 * @param d La date d'ajout de l'enregistrement dans la base
	 */
	public Point(String lo, String la, int id, Date d) {
		this.longitude = lo;
		this.latitude = la;
		this.idPhoto = id;
		this.dateAjout = d;
	}
	
	/**
	 * @return La date d'ajout de l'enregistrement dans la base
	 */
	public Date getDateAjout() {
		return dateAjout;
	}
	
	/**
	 * @return L'id de l'enregistrement dans la base
	 */
	public int getIdPhoto() {
		return idPhoto;
	}
	
	/**
	 * @return La latitude du point
	 */
	public String getLatitude() {
		return latitude;
	}
	
	/**
	 * @return La longitude du point
	 */
	public String getLongitude() {
		return longitude;
	}
}
