package com.example.easytable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ApartarLugar extends Activity {

    int hora, min;
    String getMin,getHora;

    private Button mHora;
    private ImageButton mAceptar, mRetroceder;
    private EditText mPersonas;

    private static final String KEY_CANT_PERSONAS = "cantidadPersonas";
    private static final String KEY_FECHA = "fecha";
    private static final String KEY_HORA = "hora";
    private static final String KEY_ID_LOCAL = "idLocalReservado";
    private static final String KEY_ID_USUARIO = "idUsuario";
    private static final String KEY_STATUS_RESERVACION = "statusReservacion";
    private static final String KEY_USUARIO = "usuario";

    //Objetos para utilizar las dependencias
    private FirebaseFirestore db;

    //Elemento para poder crear notificaciones
    private NotificationCompat.Builder notificacion;
    private static final String idUnico = "asdf";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_apartar_lugar);

        //Instanciación de Firebase Authentication y de Firebase Firestore
        db = FirebaseFirestore.getInstance();

        mHora = findViewById(R.id.horaButton);
        mPersonas = findViewById(R.id.cantidadPersonasApartarLugarUC);
        mAceptar = findViewById(R.id.hacerReservacionButton);

        mHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                hora = calendar.get(Calendar.HOUR_OF_DAY);
                min = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(ApartarLugar.this, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHora.setText(""+hourOfDay+":"+minute);
                        getHora = ""+hourOfDay;
                        getMin = ""+minute;
                    }
                }, hora, min, true);
                timePickerDialog.show();
            }
        });

        mAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String persona = mPersonas.getText().toString();

                if (!persona.isEmpty() && !getHora.isEmpty() && !getMin.isEmpty()) {
                    new AlertDialog.Builder(ApartarLugar.this)
                            .setTitle("Confirma la informacion")
                            .setMessage("Reservacion para " + persona + " personas a las " + getHora + ":" + getMin)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String id ="channel_1";//id of channel
                                    int importance = NotificationManager.IMPORTANCE_LOW;//The Importance of channel
                                    NotificationChannel channel = new NotificationChannel(id, "123", importance);//Generating channel

                                    Intent i = new Intent(ApartarLugar.this, PrincipalUC.class);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(ApartarLugar.this, 0, i, 0);

                                    NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                                    manager.createNotificationChannel(channel);//Add channel
                                    Notification notification = new Notification.Builder(ApartarLugar.this,id)
                                            .setCategory(Notification.CATEGORY_MESSAGE)
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setContentTitle("Nueva reservacion")
                                            .setContentText( "Reservacion para " + persona + " personas a las " + getHora + ":" + getMin )
                                            .setSmallIcon(R.drawable.checked)
                                            .setContentIntent(pendingIntent)
                                            .setAutoCancel(true)
                                            .build();
                                    manager.notify(1,notification);


                                    //Creacion de la reservacion en la coleccion de reservaciones
                                    String idReserva = UUID.randomUUID().toString();
                                    String idRestaurante = getIntent().getStringExtra("idRestaurante");
                                    String idUsuario = getIntent().getStringExtra("idLogueado");
                                    String usuario = getIntent().getStringExtra("usuario");
                                    String horaString = ""+hora+":"+min+"";
                                    @SuppressLint("SimpleDateFormat") String fecha = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                                    int personaParse = Integer.parseInt(persona);
                                    Map <String, Object> reservacion = new HashMap<>();
                                    reservacion.put(KEY_CANT_PERSONAS, personaParse);
                                    reservacion.put(KEY_FECHA, fecha);
                                    reservacion.put(KEY_HORA, horaString);
                                    reservacion.put(KEY_ID_LOCAL, idRestaurante);
                                    reservacion.put(KEY_ID_USUARIO, idUsuario);
                                    reservacion.put(KEY_STATUS_RESERVACION, 0);
                                    reservacion.put(KEY_USUARIO, usuario);
                                    db.collection("reservaciones").document(idReserva).set(reservacion)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Map <String, Object> reservaActualizada = new HashMap<>();
                                                    reservaActualizada.put("Reserva", true);
                                                    db.collection("usuario").document(idUsuario).update(reservaActualizada)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(ApartarLugar.this, "Good", Toast.LENGTH_SHORT).show();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(ApartarLugar.this, "Error", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                    Toast.makeText(ApartarLugar.this, "Reservacion hecha", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(ApartarLugar.this, "Error al hacer la reservacion", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
                else {
                    Toast.makeText(ApartarLugar.this, "Llena todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
