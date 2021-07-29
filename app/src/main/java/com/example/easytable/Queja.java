package com.example.easytable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Queja extends AppCompatActivity {

    private EditText mDescripcionQueja;
    private ImageButton mEnviarQueja;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
            @RequiresApi(api = Build.VERSION_CODES.O)       //Esta linea sirve para poder indicar el nivel de API de android necesaria para mostrar notificaciones
            @Override
            public void onClick(View v) {

                String idMesa = getIntent().getStringExtra("idMesa");
                String idRestaurante = getIntent().getStringExtra("idRestaurante");

                String id ="channel_1";//id of channel
                String numeroMesa = "3";//Description information of channel
                String problema = mDescripcionQueja.getText().toString();
                int importance = NotificationManager.IMPORTANCE_LOW;//The Importance of channel
                NotificationChannel channel = new NotificationChannel(id, "123", importance);//Generating channel
                Intent i = new Intent(Queja.this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(Queja.this, 0, i, 0);
//Adding attributes to channel
//channel.enableVibration(true); vibration
//channel.enableLights(true); prompt light
                NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                manager.createNotificationChannel(channel);//Add channel
                Notification notification = new Notification.Builder(Queja.this,id)
                        //Note that there is an additional parameter id, which refers to the ID of the configured Notification Channel.
                        //You can try it on your own and then configure the generation above this line of code after you run it once.
                        //Code comment can also run successfully if the parameter id is changed directly to "channel_1"
                        //But we can't change it to something else like "channel_2".
                        .setCategory(Notification.CATEGORY_MESSAGE)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Mesa: "+numeroMesa)
                        .setContentText(problema)
                        .setSmallIcon(R.drawable.checked)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();
                manager.notify(1,notification);

                String idUsuario = mAuth.getUid();
                db.collection("usuario").document(idUsuario).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                            }
                        });
                String idQueja = UUID.randomUUID().toString();
                Map<String, Object> queja = new HashMap<>();
                queja.put("quejaCliente", problema);
                queja.put("mesa", idMesa);
                queja.put("idDelLocal", idRestaurante);
                db.collection("quejas").document(idQueja).set(queja)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Queja.this, "Queja enviada al administrador", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                Toast.makeText(Queja.this, "Presionado", Toast.LENGTH_SHORT).show();
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