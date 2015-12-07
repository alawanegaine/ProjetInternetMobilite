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
	private static int portAjoutImage = 11111;
	private static int portRequeteImage = 22222;
	
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
	
	public static int getPortAjoutImage() {
		return portAjoutImage;
	}
	
	public static int getPortRequeteImage() {
		return portRequeteImage;
	}
}
