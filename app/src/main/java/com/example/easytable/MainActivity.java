
package com.example.easytable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button mLogOut;
    private TextView textView;
    String IdRestaurante = "dss";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();


        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text0);
        IdRestaurante = getIntent().getStringExtra("Restaurante");
        textView.setText(IdRestaurante);

        mLogOut = (Button) findViewById(R.id.LogOutButton);

        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extra = getIntent().getExtras();
                String id = mAuth.getUid();
                DocumentReference doc = db.collection("usuario").document(id);
                doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        Toast.makeText(MainActivity.this, id, Toast.LENGTH_SHORT).show();
                        String tipoUsuario = value.get("tipoDeUsuario").toString();
                        Toast.makeText(MainActivity.this, tipoUsuario, Toast.LENGTH_SHORT).show();
                        if (tipoUsuario.equals("Mesero")) {
                                Map<String, Object> online = new HashMap<>();
                                online.put("online", false);
                                db.collection("usuario").document(id).update(online);
                            Toast.makeText(MainActivity.this, "Cerrado", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, Ingresar.class));
                finish();
            }
        });
    }
}