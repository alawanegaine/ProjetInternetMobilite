package data;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.sql.PreparedStatement;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.swing.ImageIcon;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.SerializationUtils;

import protocole.Methode;
import protocole.Point;
import protocole.RequeteMessage;
import serveur.FileTcp;

/**
 * Classe permettant l'interaction avec la base de donn�es SQL
 * @author Damien
 */
public class Database {
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
	 * Le lien permettant la connexion � la database
	 */
	String url;
	/**
	 * Le nom d'utilisateur pour se connecter
	 */
	String utilisateur;
	/**
	 * Le mot de passe pour la connexion � la base
	 */
	String motDePasse;
	/**
	 * Le nom de notre base
	 */
	String nomBdd;

	//////////////////////////////////////////////////////////////////
	//								METHODES						//
	//////////////////////////////////////////////////////////////////

	/**
	 * Constructeur par d�faut. On va charger le driver n�cessaire � la connexion
	 */
	public Database() {
		motDePasse = "password";
		utilisateur = "ServeurApp";
		url = "jdbc:mysql://127.0.0.1:3306/prj_internet_et_mobilite";
		nomBdd = "prj_internet_et_mobilite";
		try {
			Class.forName( "com.mysql.jdbc.Driver" );
		} catch ( ClassNotFoundException e ) {
			log.log(Level.SEVERE, "Impossible de charger le driver pour la connexion � la BDD", e);
		}
	}

	/**
	 * M�thode permettant de r�cup�r� la liste des points ajout�s autour d'une date
	 * @param date La date limite jusqu'� laquelle on veut les points
	 * @return Un tableau points
	 */
	public synchronized ArrayList<Point> getListePoints(java.util.Date date){
		//On cr�e l'objet que l'on va renvoyer
		ArrayList<Point> maListe = new ArrayList<Point>(); 
		Connection connexion = null;
		PreparedStatement statement = null;
		ResultSet resultat = null;
		try {
			//On �tablit la connexion � la base
			connexion = DriverManager.getConnection( url, utilisateur, motDePasse );

			//On pr�pare notre requ�te
			String sqlQuery = "SELECT * FROM pinpoint WHERE dateAjout >= ?;";
			statement = connexion.prepareStatement(sqlQuery); //Interface permettant la cr�ation de requ�tes. Utilisation de requ�tes pr�par�es pour les prot�ger 
			statement.setDate(1, new java.sql.Date(date.getTime()));;

			//On �xecute notre requ�te
			resultat = statement.executeQuery();

			//On met le r�sultat dans une liste
			try {
				while(resultat.next()){
					maListe.add(new Point(resultat.getString("longitude"),resultat.getString("latitude"),resultat.getInt("id"),resultat.getDate("dateAjout")));
				}
			} catch (SQLException e) {
				log.log(Level.WARNING, "Erreur lors de la r�cup�ration des donn�es de la requ�te", e);
			}
			return maListe;

		} catch ( SQLException e ) {
			log.log(Level.WARNING,"Exception re�ue lors de la requ�te de la liste des points",e);
			//On fermet la connexion � la base
		}finally {
			if ( resultat != null ) {
				try {
					/* On commence par fermer le ResultSet */
					resultat.close();
				} catch ( SQLException ignore ) {
				}
			}
			if ( statement != null ) {
				try {
					/* Puis on ferme le Statement */
					statement.close();
				} catch ( SQLException ignore ) {
				}
			}
			if ( connexion != null ) {
				try {
					/* Et enfin on ferme la connexion */
					connexion.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
		return null;
	}

	/**
	 * M�thode permettant de tester les diff�rentes m�thodes de gestion de la BDD
	 */
	public void runTest() {
		System.out.println("---------------------------------------------------------------");
		System.out.println("Lancement des test de BDD");
		System.out.println("Lancement du test de connexion");
		testConnexion();
		System.out.println("---------------------------------------------------------------");
		System.out.println("Lancement du test de selection de points dans la base (avec date max)");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String dateMaxString = "22/06/2015";
		java.util.Date dateMax = null;
		try {
			dateMax = simpleDateFormat.parse(dateMaxString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ArrayList<Point> listeDePoints = getListePoints(dateMax);
		for(Point p : listeDePoints){
			System.out.println("Id de la photo : " + p.getIdPhoto() + " longitude : " + p.getLongitude() + " latitude : " + p.getLatitude());
		}
		System.out.println("---------------------------------------------------------------");
		RequeteMessage requete = getInfosPoint(1);
		System.out.println("La demande d'info point retourne la photo : " + requete.getTitre() + ", avec le commentaire " + requete.getCommentaire() + ". La photo a �t� prise le " + requete.getDateAjout());
	}

	/**
	 * M�thode permettant de tester la connexion � la base de don�es
	 */
	public void testConnexion(){
		Connection connexion = null;
		try {
			connexion = DriverManager.getConnection( url, utilisateur, motDePasse );
			//Requ�te � faire vers notre BDD
			System.out.println("Je suis bien connect� � ma BDD ! :D");
		} catch ( SQLException e ) {
			log.log(Level.WARNING,"Connexion impossible � la base de donn�es",e);
		} finally {
			if ( connexion != null )
				try {
					//On ferme la connexion � la BDD
					connexion.close();
				} catch ( SQLException ignore ) {
					log.log(Level.WARNING,"Erreur lors de la fermeture de la connexion",ignore);
				}
		}
	}


	/**
	 * M�thode permettant de r�cup�rer toutes les informations relatives � un point
	 * @param idPhoto L'id du point dans la base (point = photo)
	 * @return un message de r�ponse
	 */
	public synchronized RequeteMessage getInfosPoint(int idPhoto) {
		//On cr�e l'objet que l'on va renvoyer
		RequeteMessage reponse = null; 
		ResultSet resultat = null;
		PreparedStatement statement = null;
		Connection connexion = null;
		try {
			//On �tablit la connexion � la base
			connexion = DriverManager.getConnection( url, utilisateur, motDePasse );

			//On pr�pare notre requ�te
			String sqlQuery = "SELECT * FROM pinpoint WHERE id = ?;";
			statement = connexion.prepareStatement(sqlQuery); //Interface permettant la cr�ation de requ�tes. Utilisation de requ�tes pr�par�es pour les prot�ger 
			statement.setInt(1, idPhoto);

			//On �xecute notre requ�te
			resultat = statement.executeQuery();

			//On r�cup�re le r�sultat
			try {
				if(resultat.next()){
					reponse = new RequeteMessage();
					reponse.setCommentaire(resultat.getString("commentaire"));
					reponse.setTitre(resultat.getString("titre"));
					reponse.setLatitude(resultat.getString("latitude"));
					reponse.setLongitude(resultat.getString("longitude"));
					reponse.setDateAjout(resultat.getDate("dateAjout"));

					//Le fichier image
					File sourceImage = new File(resultat.getString("lienImage"));

					// Reading a Image file from file system
					FileInputStream imageInFile = new FileInputStream(sourceImage);
					byte imageData[] = new byte[(int) sourceImage.length()];
					imageInFile.read(imageData);

					// Converting Image byte array into Base64 String
					String imageDataString = encodeImage(imageData);
					reponse.setPhoto(imageDataString);
					imageInFile.close();
					reponse.setMethode(Methode.infosPoint);
					System.out.println("Les donn�es du point " + idPhoto + " ont bien �t� r�cup�r�es");
				}
			} catch (SQLException e) {
				log.log(Level.WARNING, "Erreur lors de la r�cup�ration des donn�es de la requ�te", e);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch ( SQLException e ) {
			log.log(Level.WARNING,"Exception re�ue lors de la requ�te de la liste des points",e);
			//On fermet la connexion � la base
		}finally {
			if ( resultat != null ) {
				try {
					/* On commence par fermer le ResultSet */
					resultat.close();
				} catch ( SQLException ignore ) {
				}
			}
			if ( statement != null ) {
				try {
					/* Puis on ferme le Statement */
					statement.close();
				} catch ( SQLException ignore ) {
				}
			}
			if ( connexion != null ) {
				try {
					/* Et enfin on ferme la connexion */
					connexion.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
		return reponse;
	}

	/**
	 * M�thode permettant d'ajouter une image et toutes ses informations dans la base
	 * @param requete La requ�te arrivant de l'application, qui contient toutes les infos de l'image
	 * @return un requ�te d'acknowledgement
	 */
	public synchronized RequeteMessage ajouterPoint(RequeteMessage requete){
		//On cr�e l'objet que l'on va renvoyer
		RequeteMessage reponse = null; 
		Connection connexion = null;
		PreparedStatement statement = null;
		try {
			//On �tablit la connexion � la base
			connexion = DriverManager.getConnection( url, utilisateur, motDePasse );

			String url = "C:\\Users\\Damien\\Desktop\\ImageInternetEtMobilite\\" + requete.getTitre() +".bmp";
			try {
				ajoutImageServeur(requete.getPhoto(),url);
			} catch (IOException e) {
				log.log(Level.SEVERE, "Impossible d'enregistrer l'image sur le serveur",e);;
			}

			//On pr�pare notre requ�te
			String sqlQuery = "INSERT INTO pinpoint(latitude, longitude, lienImage, titre, commentaire, dateAjout)" +
					"VALUES (?, ?, ?, ?, ?, ?)";
			statement = connexion.prepareStatement(sqlQuery); //Interface permettant la cr�ation de requ�tes. Utilisation de requ�tes pr�par�es pour les prot�ger 
			statement.setString(1, requete.getLatitude());
			statement.setString(2, requete.getLongitude());
			statement.setString(3, url);
			statement.setString(4, requete.getTitre());
			statement.setString(5, requete.getCommentaire());
			statement.setDate(6, new java.sql.Date(requete.getDateAjout().getTime()));

			//On �xecute notre requ�te
			int statut = statement.executeUpdate();

			//Si l'insertion s'est bien faite
			reponse = new RequeteMessage();
			if(statut == 1){
				reponse.setMethode(Methode.ack);
				System.out.println("L'ajout du point s'est effectu� avec succ�s");
			}

		} catch ( SQLException e ) {
			log.log(Level.WARNING,"Exception re�ue lors de la requ�te de la liste des points",e);
			//On fermet la connexion � la base
		}finally {
			if ( statement != null ) {
				try {
					/* on ferme le Statement */
					statement.close();
				} catch ( SQLException ignore ) {
				}
			}
			if ( connexion != null ) {
				try {
					/* Et enfin on ferme la connexion */
					connexion.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
		return reponse;		
	}

	/**
	 * M�thode permettant l'enregistrement d'une photo sur le serveur
	 * @param photo L'image � enregistrer, sous forme de String
	 * @param url L� ou l'on souhaite l'enregistrer
	 * @throws IOException Exception lev�e en cas d'�chec de l'enregistrement de l'image
	 */
	private void ajoutImageServeur(String photo, String url) throws IOException {
		// Converting a Base64 String into Image byte array
		byte[] imageByteArray = decodeImage(photo);
		// Write a image byte array into file system
		FileOutputStream imageOutFile = new FileOutputStream(url);

		imageOutFile.write(imageByteArray);

		imageOutFile.close();
		/*	RenderedImage rendered = null;
		if (image instanceof RenderedImage)
		{
		    rendered = (RenderedImage)image;
		}
		else
		{
		    BufferedImage buffered = new BufferedImage(
		        photo.getIconWidth(),
		        photo.getIconHeight(),
		        BufferedImage.TYPE_INT_RGB
		    );
		    Graphics2D g = buffered.createGraphics();
		    g.drawImage(image, 0, 0, null);
		    g.dispose();
		    rendered = buffered;
		}
		ImageIO.write(rendered, "BMP", new File(url));*/
	}

	/**
	 * Encodes the byte array into base64 string
	 *
	 * @param imageByteArray - byte array
	 * @return String a {@link java.lang.String}
	 */
	public static String encodeImage(byte[] imageByteArray) {
		return new String(Base64.encodeBase64(imageByteArray));
	}

	/**
	 * Decodes the base64 string into byte array
	 *
	 * @param imageDataString - a {@link java.lang.String}
	 * @return byte array
	 */
	public static byte[] decodeImage(String imageDataString) {
		return Base64.decodeBase64(imageDataString.getBytes());
	}
}
