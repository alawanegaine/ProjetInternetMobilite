package serveur;

import java.util.logging.Logger;

import data.Database;

/**
 * Cette classe permet de d�marrer nos fils d'execution dans deux threads diff�rents
 * Contient la m�thode main de notre programme
 * @author Damien
 *
 */
public class Serveur {
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
	private static final Logger log = Logger.getLogger(Serveur.class.getName() );	
	/**
	 * Le fil g�rant les requ�tes d'ajout de donn�es dans la base
	 */
	private static FileTcp filExecutionAjout;
	/**
	 * Le fil g�rant les requ�tes demandant des donn�es de la base
	 */
	private static FileUdp filExecutionRequete;
	/**
	 * L'objet permettant de faire le lien avec la BDD
	 */
	private static Database maBase;
	
	//////////////////////////////////////////////////////////////////
	//								METHODES						//
	//////////////////////////////////////////////////////////////////
	
	/**
	 * On lance le serveur
	 * @param args
	 */
	public static void main(String[] args) {
		demarrerServeur();
	}
	
	/**
	 * M�thode statique permettant le d�marrage de notre serveur
	 * On lance les deux fil d'execution
	 * @return 
	 * 
	 */
	private static void demarrerServeur(){	
		//On cr�e l'objet de connexion � la base
		maBase = new Database();
		maBase.runTest();
		
		
		//On change l'�tat du serveur
		System.out.println("D�marrage du serveur");
		ServeurInfo.setEnMarche(true);
		
		//On initialise les fils d'execution
		filExecutionAjout = new FileTcp(maBase);
		filExecutionRequete = new FileUdp(maBase);
		
		//On d�marre les fils d'execution
		new Thread(filExecutionAjout).start();
		new Thread(filExecutionRequete).start();
	}
}
