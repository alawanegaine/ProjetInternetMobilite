package servlet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.InfosServer;

/**
 * Servlet implementation class GetListPoints
 */
@WebServlet("/GetListPoints")
public class GetListPoints extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetListPoints() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public void getListPointFromServer(){
		/* Build request to server and send */
		Request requestGetListMatch = new Request() ;
		Request requestGetListMatchResponse = new Request() ;
		requestGetListMatch.setRequest(true);
		requestGetListMatch.setMethode(Methodes.demandeListMatch);
		requestGetListMatch.setAddress(Inet4Address.getLocalHost().getHostAddress());
		requestGetListMatch.setPort(InfosServer.getPortserverweb());
		
		byte[] dataSend = Request.marshall(requestGetListMatch);
		DatagramPacket dpSend = new DatagramPacket( dataSend, dataSend.length, InetAddress.getByName(InfosServer.getIpserverjava()), InfosServer.getPortserverweb()) ;  		
		DatagramSocket dsSend = new DatagramSocket();
		
		dsSend.send(dpSend);
		
		if(dsSend != null)
			dsSend.close();
			
		/* Now, receive informations */ 
		byte[] dataReceive = new byte[5000] ;
		DatagramPacket dpReceive = new DatagramPacket(dataReceive, dataReceive.length);
		DatagramSocket dsReceive = new DatagramSocket(InfosServer.getPortserverweb());
		dsReceive.setSoTimeout(2000);
		
		dsReceive.receive(dpReceive);
		requestGetListMatchResponse = Request.unmarshall(dpReceive.getData());
		dsReceive.close();
	}

}