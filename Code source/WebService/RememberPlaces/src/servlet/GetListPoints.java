package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import protocole.Data;
import protocole.Methode;
import protocole.Point;
import protocole.RequeteMessage;
import protocole.RequetePoints;
import thread.GetListPointThread;
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
		Data data = new Gson().fromJson(request.getReader(), Data.class);

		GetListPointThread getListPoint = new GetListPointThread(data.getLatitude(),data.getLongitude(),data.getKmMax(),data.getDateMax()) ;
		Thread monThread = new Thread(getListPoint);
		monThread.start();
		
		while(monThread.getState() != Thread.State.TERMINATED) {}
				
		System.out.println("Sorti de boucle !" + getListPoint.getPointAGarder().size());
		for (Point p : getListPoint.getPointAGarder()) {
			System.out.println("Point gardé : "+p.getLatitude()+", "+p.getLongitude() + " le " + p.getDateAjout());
		}
		System.out.println("Après debug");
		/* build a JSON file and send to the client */
		Gson gson2 = new Gson(); 
        JsonObject myObj = new JsonObject();
        JsonElement listPointObj = gson2.toJsonTree(getListPoint.getPointAGarder());
        if(getListPoint.getPointAGarder() == null){
            myObj.addProperty("success", false);
        }
        else {
            myObj.addProperty("success", true);
        }
        myObj.add("listPoints", listPointObj);
		response.getWriter().println(myObj);
		response.getWriter().close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
