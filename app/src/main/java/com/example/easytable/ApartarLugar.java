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
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ApartarLugar extends Activity {

    int hora, min;
    String getMin,getHora;

    private Button mHora;
    private ImageButton mAceptar, mRetroceder;
    private EditText mPersonas;

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
                            .setTitle("Comfirma la informacion")
                            .setMessage("Reservacion para " + persona + " personas a las " + getHora + ":" + getMin)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String id ="channel_1";//id of channel
                                    int importance = NotificationManager.IMPORTANCE_LOW;//The Importance of channel
                                    NotificationChannel channel = new NotificationChannel(id, "123", importance);//Generating channel

                                    Intent i = new Intent(ApartarLugar.this, MainActivity.class);
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
