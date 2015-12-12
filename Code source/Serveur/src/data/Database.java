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
 * Classe permettant l'interaction avec la base de données SQL
 * @author Damien
 */
public class Database {
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
	 * Le lien permettant la connexion à la database
	 */
	String url;
	/**
	 * Le nom d'utilisateur pour se connecter
	 */
	String utilisateur;
	/**
	 * Le mot de passe pour la connexion à la base
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
	 * Constructeur par défaut. On va charger le driver nécessaire à la connexion
	 */
	public Database() {
		motDePasse = "password";
		utilisateur = "ServeurApp";
		url = "jdbc:mysql://127.0.0.1:3306/prj_internet_et_mobilite";
		nomBdd = "prj_internet_et_mobilite";
		try {
			Class.forName( "com.mysql.jdbc.Driver" );
		} catch ( ClassNotFoundException e ) {
			log.log(Level.SEVERE, "Impossible de charger le driver pour la connexion à la BDD", e);
		}
	}

	/**
	 * Méthode permettant de récupéré la liste des points ajoutés autour d'une date
	 * @param date La date limite jusqu'à laquelle on veut les points
	 * @return Un tableau points
	 */
	public synchronized ArrayList<Point> getListePoints(java.util.Date date){
		//On crée l'objet que l'on va renvoyer
		ArrayList<Point> maListe = new ArrayList<Point>(); 
		Connection connexion = null;
		PreparedStatement statement = null;
		ResultSet resultat = null;
		try {
			//On établit la connexion à la base
			connexion = DriverManager.getConnection( url, utilisateur, motDePasse );

			//On prépare notre requête
			String sqlQuery = "SELECT * FROM pinpoint WHERE dateAjout >= ?;";
			statement = connexion.prepareStatement(sqlQuery); //Interface permettant la création de requêtes. Utilisation de requêtes préparées pour les protéger 
			statement.setDate(1, new java.sql.Date(date.getTime()));;

			//On éxecute notre requête
			resultat = statement.executeQuery();

			//On met le résultat dans une liste
			try {
				while(resultat.next()){
					maListe.add(new Point(resultat.getString("longitude"),resultat.getString("latitude"),resultat.getInt("id"),resultat.getDate("dateAjout")));
				}
			} catch (SQLException e) {
				log.log(Level.WARNING, "Erreur lors de la récupération des données de la requête", e);
			}
			return maListe;

		} catch ( SQLException e ) {
			log.log(Level.WARNING,"Exception reçue lors de la requête de la liste des points",e);
			//On fermet la connexion à la base
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
	 * Méthode permettant de tester les différentes méthodes de gestion de la BDD
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
		System.out.println("La demande d'info point retourne la photo : " + requete.getTitre() + ", avec le commentaire " + requete.getCommentaire() + ". La photo a été prise le " + requete.getDateAjout());
	}

	/**
	 * Méthode permettant de tester la connexion à la base de donées
	 */
	public void testConnexion(){
		Connection connexion = null;
		try {
			connexion = DriverManager.getConnection( url, utilisateur, motDePasse );
			//Requête à faire vers notre BDD
			System.out.println("Je suis bien connecté à ma BDD ! :D");
		} catch ( SQLException e ) {
			log.log(Level.WARNING,"Connexion impossible à la base de données",e);
		} finally {
			if ( connexion != null )
				try {
					//On ferme la connexion à la BDD
					connexion.close();
				} catch ( SQLException ignore ) {
					log.log(Level.WARNING,"Erreur lors de la fermeture de la connexion",ignore);
				}
		}
	}


	/**
	 * Méthode permettant de récupérer toutes les informations relatives à un point
	 * @param idPhoto L'id du point dans la base (point = photo)
	 * @return un message de réponse
	 */
	public synchronized RequeteMessage getInfosPoint(int idPhoto) {
		//On crée l'objet que l'on va renvoyer
		RequeteMessage reponse = null; 
		ResultSet resultat = null;
		PreparedStatement statement = null;
		Connection connexion = null;
		try {
			//On établit la connexion à la base
			connexion = DriverManager.getConnection( url, utilisateur, motDePasse );

			//On prépare notre requête
			String sqlQuery = "SELECT * FROM pinpoint WHERE id = ?;";
			statement = connexion.prepareStatement(sqlQuery); //Interface permettant la création de requêtes. Utilisation de requêtes préparées pour les protéger 
			statement.setInt(1, idPhoto);

			//On éxecute notre requête
			resultat = statement.executeQuery();

			//On récupère le résultat
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
					System.out.println("Les données du point " + idPhoto + " ont bien été récupérées");
				}
			} catch (SQLException e) {
				log.log(Level.WARNING, "Erreur lors de la récupération des données de la requête", e);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch ( SQLException e ) {
			log.log(Level.WARNING,"Exception reçue lors de la requête de la liste des points",e);
			//On fermet la connexion à la base
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
	 * Méthode permettant d'ajouter une image et toutes ses informations dans la base
	 * @param requete La requête arrivant de l'application, qui contient toutes les infos de l'image
	 * @return un requête d'acknowledgement
	 */
	public synchronized RequeteMessage ajouterPoint(RequeteMessage requete){
		//On crée l'objet que l'on va renvoyer
		RequeteMessage reponse = null; 
		Connection connexion = null;
		PreparedStatement statement = null;
		try {
			//On établit la connexion à la base
			connexion = DriverManager.getConnection( url, utilisateur, motDePasse );

			String url = "C:\\Users\\Damien\\Desktop\\ImageInternetEtMobilite\\" + requete.getTitre() +".bmp";
			try {
				ajoutImageServeur(requete.getPhoto(),url);
			} catch (IOException e) {
				log.log(Level.SEVERE, "Impossible d'enregistrer l'image sur le serveur",e);;
			}

			//On prépare notre requête
			String sqlQuery = "INSERT INTO pinpoint(latitude, longitude, lienImage, titre, commentaire, dateAjout)" +
					"VALUES (?, ?, ?, ?, ?, ?)";
			statement = connexion.prepareStatement(sqlQuery); //Interface permettant la création de requêtes. Utilisation de requêtes préparées pour les protéger 
			statement.setString(1, requete.getLatitude());
			statement.setString(2, requete.getLongitude());
			statement.setString(3, url);
			statement.setString(4, requete.getTitre());
			statement.setString(5, requete.getCommentaire());
			statement.setDate(6, new java.sql.Date(requete.getDateAjout().getTime()));

			//On éxecute notre requête
			int statut = statement.executeUpdate();

			//Si l'insertion s'est bien faite
			reponse = new RequeteMessage();
			if(statut == 1){
				reponse.setMethode(Methode.ack);
				System.out.println("L'ajout du point s'est effectué avec succès");
			}

		} catch ( SQLException e ) {
			log.log(Level.WARNING,"Exception reçue lors de la requête de la liste des points",e);
			//On fermet la connexion à la base
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
	 * Méthode permettant l'enregistrement d'une photo sur le serveur
	 * @param photo L'image à enregistrer, sous forme de String
	 * @param url Là ou l'on souhaite l'enregistrer
	 * @throws IOException Exception levée en cas d'échec de l'enregistrement de l'image
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
