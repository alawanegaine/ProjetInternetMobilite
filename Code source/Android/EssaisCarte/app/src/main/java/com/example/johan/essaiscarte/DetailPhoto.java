package com.example.johan.essaiscarte;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import protocole.RequeteMessage;
import org.apache.commons.codec.*;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class DetailPhoto extends AppCompatActivity {

    private TextView titre;
    private TextView description;
    private ImageView image;
    private String response;
    private RecupererPhoto task = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_photo);

        titre = (TextView) findViewById(R.id.titre);
        description = (TextView) findViewById(R.id.description);
        image = (ImageView) findViewById(R.id.image);

        Intent i = getIntent();

        int idPhoto = Integer.parseInt(i.getStringExtra("titre"));

        titre.setText("");
        
        task = new RecupererPhoto(idPhoto,DetailPhoto.this);
        AsyncTask<Integer, Void, RequeteMessage> retour = task.execute();

        try {
            titre.setText(retour.get().getTitre());
            description.setText(retour.get().getCommentaire());
            byte[] decodedString = Base64.decode(retour.get().getPhoto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
