package com.example.johan.essaiscarte;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import protocole.Message;
import protocole.Methode;
import protocole.RequeteMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;

/**
 * Created by Dimitri on 11/12/2015.
 */
public class RecupererPhoto extends AsyncTask<Integer, Void, RequeteMessage> {

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket serveurSocket;
    private WeakReference<DetailPhoto> mActivity = null;

    private int idPhoto;

    public RecupererPhoto(int idPhoto,DetailPhoto detailPhoto) {

            this.idPhoto = idPhoto;


            link(detailPhoto);




    }



    @Override
    protected RequeteMessage doInBackground(Integer... params) {
        try {
            serveurSocket = new Socket(InetAddress.getByName("192.168.43.158"), 25000);
            output = new ObjectOutputStream(serveurSocket.getOutputStream());
            input = new ObjectInputStream(serveurSocket.getInputStream());


            RequeteMessage message = new RequeteMessage();
            message.setIdPhoto(idPhoto);
            message.setMethode(Methode.getInfosPoint);
            output.writeObject(message);
            output.flush();
            System.out.println();
            Object objet = null;

            objet = input.readObject();

            //Gestion de la r�ponse
            if(objet instanceof RequeteMessage && objet != null){
                RequeteMessage requete = (RequeteMessage) objet;
                return requete;
            } else if(objet == null){
                //Toast.makeText(this, "Pas d'image", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if(serveurSocket!=null)
                try {
                    serveurSocket.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    private void link(DetailPhoto pActivity) {
        mActivity = new WeakReference<DetailPhoto>(pActivity);
    }
}
