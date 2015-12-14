package protocole;

import java.io.Serializable;
import java.util.Date;

import javax.swing.ImageIcon;

/**
 * L'objet de base qui sera envoy� vers/depuis le serveur
 * @author Damien
 */
public class Message implements Serializable{
	//////////////////////////////////////////////////////////////////
	//								ATTRIBUTS						//
	//////////////////////////////////////////////////////////////////
	/**
	 * Num�ro de version
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Le port de celui qui envois la requ�te
	 */
	private int port;
	/**
	 * L'adresse Ip de celui qui envois la requ�te
	 */
	private String adresseIp;
	/**
	 * Le type de requ�te que l'on envois
	 */
	private Methode methode;
	/**
	 * La photo
	 */
	private ImageIcon photo;
	/**
	 * Le titre associ� � la photo 
	 */
	private String titre;
	/**
	 * Le commentaire associ� � la photo
	 */
	private String commentaire;
	/**
	 * La longitude du point associ� � la photo
	 */
	private String longitude;
	/**
	 * La latitude du point associ� � la photo
	 */
	private String latitude;
	/**
	 * La date d'ajout dans la base associ�e � la photo
	 */
	private Date dateAjout;
	
	//////////////////////////////////////////////////////////////////
	//								METHODES						//
	//////////////////////////////////////////////////////////////////	
	
	/**
	 * Constructeur par d�faut
	 */
	public Message() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return L'adresse Ip de celui qui envois la requ�te
	 */
	public String getAdresseIp() {
		return adresseIp;
	}
	
	/**
	 * @param adresseIp L'adresse Ip de celui qui envois la requ�te
	 */
	public void setAdresseIp(String adresseIp) {
		this.adresseIp = adresseIp;
	}
	
	/**
	 * @return Le commentaire associ� � la photo
	 */
	public String getCommentaire() {
		return commentaire;
	}
	
	/**
	 * @param commentaire Le commentaire associ� � la photo
	 */
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	
	/**
	 * @return La date d'ajout dans la base associ�e � la photo
	 */
	public Date getDateAjout() {
		return dateAjout;
	}

	/**
	 * @param dateAjout La date d'ajout dans la base associ�e � la photo
	 */
	public void setDateAjout(Date dateAjout) {
		this.dateAjout = dateAjout;
	}
	
	/**
	 * @return La latitude du point associ� � la photo
	 */
	public String getLatitude() {
		return latitude;
	}
	
	/**
	 * @param latitude La latitude du point associ� � la photo
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * @return La longitude du point associ� � la photo
	 */
	public String getLongitude() {
		return longitude;
	}
	
	/**
	 * @param longitude La longitude du point associ� � la photo
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * @return Le type de requ�te que l'on envois
	 */
	public Methode getMethode() {
		return methode;
	}

	/**
	 * @param methode Le type de requ�te que l'on envois
	 */
	public void setMethode(Methode methode) {
		this.methode = methode;
	}
	
	/**
	 * @return La photo
	 */
	public ImageIcon getPhoto() {
		return photo;
	}
	
	/**
	 * @param photo La photo
	 */
	public void setPhoto(ImageIcon photo) {
		this.photo = photo;
	}
	
	/**
	 * @return Le port de celui qui envois la requ�te
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * @param port Le port de celui qui envois la requ�te
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * @return Le titre associ� � la photo 
	 */
	public String getTitre() {
		return titre;
	}
	
	/**
	 * @param titre Le titre associ� � la photo 
	 */
	public void setTitre(String titre) {
		this.titre = titre;
	}
}
