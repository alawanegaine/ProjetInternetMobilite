package data;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.sql.PreparedStatement;
import java.awt.Image;
import java.io.File;
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

import org.apache.commons.lang3.SerializationUtils;

import com.sun.corba.se.spi.orb.ParserData;

import protocole.Methode;
import protocole.Point;
import protocole.RequeteMessage;
import serveur.FilExecutionAjout;

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
	private static final Logger log = Logger.getLogger( FilExecutionAjout.class.getName() );
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
		try {
			//On �tablit la connexion � la base
			Connection connexion = DriverManager.getConnection( url, utilisateur, motDePasse );
			
			//On pr�pare notre requ�te
			String sqlQuery = "SELECT * FROM pinpoint WHERE dateAjout >= ?;";
			PreparedStatement statement = connexion.prepareStatement(sqlQuery); //Interface permettant la cr�ation de requ�tes. Utilisation de requ�tes pr�par�es pour les prot�ger 
			statement.setDate(1, new java.sql.Date(date.getTime()));;

			//On �xecute notre requ�te
			ResultSet resultat = statement.executeQuery();

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
				RequeteMessage reponse = new RequeteMessage(); 
				try {
					//On �tablit la connexion � la base
					Connection connexion = DriverManager.getConnection( url, utilisateur, motDePasse );
					
					//On pr�pare notre requ�te
					String sqlQuery = "SELECT * FROM pinpoint WHERE id = ?;";
					PreparedStatement statement = connexion.prepareStatement(sqlQuery); //Interface permettant la cr�ation de requ�tes. Utilisation de requ�tes pr�par�es pour les prot�ger 
					statement.setInt(1, idPhoto);

					//On �xecute notre requ�te
					ResultSet resultat = statement.executeQuery();

					//On r�cup�re le r�sultat
					try {
						if(resultat.next()){
							reponse.setCommentaire(resultat.getString("commentaire"));
							reponse.setTitre(resultat.getString("titre"));
							reponse.setLatitude(resultat.getString("latitude"));
							reponse.setLongitude(resultat.getString("longitude"));
							reponse.setDateAjout(resultat.getDate("dateAjout"));
							Image image = null; 
							File sourceImage = new File(resultat.getString("lienImage"));
							try {
								image = ImageIO.read(sourceImage);
							} catch (MalformedURLException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
							ImageIcon imageIcon = new ImageIcon(image);
							reponse.setPhoto(imageIcon);
							reponse.setMethode(Methode.infosPoint);
						}
					} catch (SQLException e) {
						log.log(Level.WARNING, "Erreur lors de la r�cup�ration des donn�es de la requ�te", e);
					}
					return reponse;

				} catch ( SQLException e ) {
					log.log(Level.WARNING,"Exception re�ue lors de la requ�te de la liste des points",e);
					//On fermet la connexion � la base
				}
		return null;
	}
}
