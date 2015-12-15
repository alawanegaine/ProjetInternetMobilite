package com.example.johan.essaiscarte;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import protocole.Message;
import protocole.Methode;
import protocole.RequeteMessage;

public class TakeAPicture extends AppCompatActivity implements LocationListener {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;

    //initialisation
    Button PictureButton = null;
    Button SendButton =null;
    ImageView photo;
    TextView titreTV=null;
    EditText titre=null;
    TextView descriptionTV=null;
    EditText description=null;

    String sTitre=null;
    String sDescription=null;

    double latitude;
    double longitude;
    String sLatitude;
    String sLongitude;
    LocationManager lm;

    public static final String SERVERIP="192.168.43.158";
    public static final int SERVERPORT=25000;

    String imageString=null;


    /********* LOCALISATION ********/

    @Override
    public void onProviderEnabled(String provider) {
        //localisation activée
        String msg = String.format(getResources().getString(R.string.provider_enabled), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        //update de la localisation
        latitude = location.getLatitude();
        longitude = location.getLongitude();


        sLatitude = Double.toString(latitude);
        sLongitude = Double.toString(longitude);

    }

    @Override
    public void onProviderDisabled(String provider) {
        //localisation désactivée
        String msg = String.format(
                getResources().getString(R.string.provider_disabled), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    /********* FIN LOCALISATION ********/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_apicture);

        //récupération des éléments de la vue
        PictureButton = (Button) findViewById(R.id.takepicture);
        SendButton =(Button)findViewById(R.id.boutonEnvoyer);
        photo = (ImageView) findViewById(R.id.image);
        titreTV=(TextView)findViewById(R.id.titre);
        titre=(EditText)findViewById(R.id.editTitre);
        descriptionTV=(TextView)findViewById(R.id.description);
        description=(EditText)findViewById(R.id.editDescription);


        PictureButton.setOnClickListener(PictureListener);
        SendButton.setOnClickListener(SendListener);

        sTitre=titre.getText().toString();
        sDescription=description.getText().toString();


    }


    @Override
    protected void onResume() {
        super.onResume();
        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {                // TODO: Consider calling
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
                return;
            }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {                // TODO: Consider calling
            lm.removeUpdates(this);
            return;
        }
    }

    private View.OnClickListener SendListener= new View.OnClickListener(){
        @Override
        public void onClick(View v){
            //avant l'envoi, on vérifie que la localisation est activée et qu'on a au moins un titre pour la photo

            if(!isLocationEnabled(getApplicationContext())){
                Toast.makeText(getApplicationContext(),R.string.error_location, Toast.LENGTH_LONG).show();

            }else if(titre.getText().toString().matches("")){
                Toast.makeText(getApplicationContext(),R.string.error_title, Toast.LENGTH_LONG).show();
            } else if(description.getText().toString().matches("")){
                Toast.makeText(getApplicationContext(),R.string.error_description, Toast.LENGTH_LONG).show();
            }
            else{
                sTitre=titre.getText().toString();
                sDescription=description.getText().toString();
                ServerConnection connection=new ServerConnection(SERVERIP,SERVERPORT);
                connection.execute(sTitre,sDescription,sLatitude,sLongitude,imageString);
            }


        }
    };

    //vérification de l'état de la localisation
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }



    private View.OnClickListener PictureListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //lancement de l'appareil photo
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap=null;
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image enregistrée
                Toast.makeText(this, "Image enregistrée dans :\n" +
                        "ProjetInternet", Toast.LENGTH_LONG).show();
                //on affiche les autres champs
                photo.setVisibility(View.VISIBLE);
                titreTV.setVisibility(View.VISIBLE);
                titre.setVisibility(View.VISIBLE);
                descriptionTV.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                SendButton.setVisibility(View.VISIBLE);
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                } catch (IOException e){
                    e.printStackTrace();
                }
                //on affiche le bitmap de la photo
                photo.setImageBitmap(bitmap);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),R.string.result_canceled, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),R.string.error, Toast.LENGTH_LONG).show();
            }

            File mediaFile = new File(fileUri.getPath());
            byte imageData[] = null;
            FileInputStream imageInFile;
            try {
                imageInFile = new FileInputStream(mediaFile);
                imageData = new byte[(int) mediaFile.length()];
                imageInFile.read(imageData);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            imageString = Base64.encodeToString(imageData, Base64.DEFAULT);
        }

    }


    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }


    private static File getOutputMediaFile(int type){
        File mediaFile=null;
        //On vérifie la carte SD
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "ProjetInternet");
            //on créé le répertoire s'il n'existe pas

            if (! mediaStorageDir.exists()){
                if (! mediaStorageDir.mkdirs()){
                    Log.d("ProjetInternet", "failed to create directory");
                    return null;
                }
            }

            // On créé le nom de l'image avec la date
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            if (type == MEDIA_TYPE_IMAGE){
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "IMG_"+ timeStamp + ".jpg");
            }  else {
                return null;
            }


        }
        return mediaFile;

    }

    public class ServerConnection extends AsyncTask<String,Void,String>{

        //connexion au serveur
        String dstAddress;
        int dstPort;
        String response = "";
        Date date=new Date();

        ServerConnection(String addr, int port){
            dstAddress = addr;
            dstPort = port;
        }
        @Override
        protected String doInBackground(String... params) {
            String check=null;
            Socket socket = null;
            //préparation du message à envoyer au serveur
            RequeteMessage message=new RequeteMessage();
            message.setAdresseIp(SERVERIP);
            message.setPort(SERVERPORT);
            message.setDateAjout(date);
            message.setTitre(sTitre);
            message.setCommentaire(sDescription);
            message.setLatitude(sLatitude);
            message.setLongitude(sLongitude);
            message.setMethode(Methode.add);
            message.setPhoto(imageString);


            try {
                //connexion
                socket = new Socket(dstAddress, dstPort);
                socket.setSoTimeout(2000);


                ObjectOutputStream output= new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                output.writeObject(message);
                output.flush();
                System.out.println();
                Object objet = null;

                objet = input.readObject();

                if(objet instanceof RequeteMessage && objet != null){
                    RequeteMessage requete = (RequeteMessage) objet;
                    check=requete.getMethode().toString();
                } else if(objet == null){
                    //Toast.makeText(this, "Pas d'image", Toast.LENGTH_LONG).show();
                }




            } catch (UnknownHostException e) {
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }
            finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return check;
        }

        protected void onPostExecute(String result){
            // après l'envoi, on affiche une confirmation et on enlève les éléments de la photo qui vient d'être prise
            if (result.matches("ack")){
                Toast.makeText(getApplicationContext(),R.string.send_confirm, Toast.LENGTH_LONG).show();
                photo.setVisibility(View.GONE);
                titreTV.setVisibility(View.GONE);
                titre.setVisibility(View.GONE);
                descriptionTV.setVisibility(View.GONE);
                description.setVisibility(View.GONE);
                SendButton.setVisibility(View.GONE);
            }

        }

    }



}
