package com.example.easytable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Queja extends AppCompatActivity {

    private EditText mDescripcionQueja;
    private ImageButton mEnviarQueja;

    //Elemento para poder crear notificaciones
    private NotificationCompat.Builder notificacion;
    private static final String idUnico = "asdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_queja);
        
        mDescripcionQueja = findViewById(R.id.queja_Txt);
        mEnviarQueja = findViewById(R.id.enviarQuejaButton);



        
        mEnviarQueja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: CREAR NOTIFICACIONES Y HACER QUE ESTAS LE LLEGUEN SOLO AL ADMINISTRADOR DEL LOCAL)
                NotificationCompat.Builder builder = new NotificationCompat.Builder(Queja.this, idUnico)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Titulo")
                        .setContentText("Contenido de la notificacion")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                Toast.makeText(Queja.this, "Queja enviada", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

//        notificacion.setSmallIcon(R.mipmap.ic_launcher);
//        notificacion.setContentTitle("Titulo");
//        notificacion.setContentText("Descripcion de la notificacion");
//        Intent i = new Intent(Queja.this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(Queja.this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
//        notificacion.setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(idUnico, notificacion.build());