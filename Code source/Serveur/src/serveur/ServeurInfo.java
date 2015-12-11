package serveur;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServeurInfo {
	//////////////////////////////////////////////////////////////////
	//								ATTRIBUTS						//
	//////////////////////////////////////////////////////////////////
	/**
	 * Logger servant à l'affichage des diverses informations
	 */
	private static final Logger log = Logger.getLogger(ServeurInfo.class.getName() );	
	private static String serveurIp;
	private static int portUdp = 15000;
	private static int portTcp = 25000;
	private static boolean enMarche = false;
	
	//////////////////////////////////////////////////////////////////
	//								METHODES						//
	//////////////////////////////////////////////////////////////////
	
	public static String getServeurIp() {
		try {
            serveurIp = InetAddress.getLocalHost().getHostAddress();
	    } catch (UnknownHostException ex) {
	        log.log(Level.SEVERE, null, ex);
	    }
		return serveurIp;
	}
	
	public static int getPortTcp() {
		return portTcp;
	}
	
	public static int getPortUdp() {
		return portUdp;
	}
	
	public static void setEnMarche(boolean b){
		enMarche = b;
	}
	
	public static boolean estEnMarche(){
		return enMarche;
	}
}
