package thread;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;

import protocole.Methode;
import protocole.Point;
import protocole.RequeteMessage;
import protocole.RequetePoints;
import util.InfosServer;

public class GetListPointThread implements Runnable{
	
	public GetListPointThread() {
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	

	public ArrayList<Point> getListPointFromServer() throws IOException{
		/* Build request to server and send */
		RequeteMessage requestGetListPoints= new RequeteMessage() ;
		requestGetListPoints.setMethode(Methode.getListePoints);
		requestGetListPoints.setAdresseIp(Inet4Address.getLocalHost().getHostAddress());
		requestGetListPoints.setPort(InfosServer.getPortserverweb());
		
		byte[] dataSend = RequeteMessage.marshall(requestGetListPoints);
		DatagramPacket dpSend = new DatagramPacket( dataSend, dataSend.length, InetAddress.getByName(InfosServer.getIpserverjava()), InfosServer.getPortserverweb()) ;  		
		DatagramSocket dsSend = new DatagramSocket();
		
		dsSend.send(dpSend);
		
		if(dsSend != null)
			dsSend.close();
			
		/* Now, receive informations */ 
		RequetePoints requestGetListPointsResponse = new RequetePoints();
		byte[] dataReceive = new byte[5000] ;
		DatagramPacket dpReceive = new DatagramPacket(dataReceive, dataReceive.length);
		DatagramSocket dsReceive = new DatagramSocket(InfosServer.getPortserverweb());
		dsReceive.setSoTimeout(2000);
		
		dsReceive.receive(dpReceive);
		requestGetListPointsResponse = RequetePoints.unmarshall(dpReceive.getData());
		dsReceive.close();
		
		return requestGetListPointsResponse.getListeDePoints() ;
	}

}
