package com.example.easytable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class
PagarAdeudo extends AppCompatActivity {

    public static final String PAYPAL_CLIENT_ID = "AUM8W9KU49DmhJENE37MV5vTKeIfwJpOIh2jTYH1KhXrLhuYkkEZiDhG7yJxLuL-f0GpL1OCz3za6ITP";
    private static final int PAYPAL_REQUEST_CODE = 7171;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    //Esta configuracion es para utilizar cuenta sandbox para pruebas
    private static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId(PAYPAL_CLIENT_ID);

    private TextView mMontoPagar;
    private Button mPagar;
    String monto;

    String idRestaurante;

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_pagar_adeudo);


        //Iniciar servicio paypal
        Intent i = new Intent(PagarAdeudo.this, PayPalService.class);
        i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(i);

        mMontoPagar = findViewById(R.id.montoPagarAdeudoTxt);
        mPagar = findViewById(R.id.pagarAdeudoButton);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        idRestaurante = getIntent().getStringExtra("idRestaurante");


        mMontoPagar.setText("$25 MXN");


        mPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procesarPago();
            }
        });

    }


    private void procesarPago() {
        //Recibir el monto
        monto = "25";
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(monto)), "MXN", "Pagado", PayPalPayment
                .PAYMENT_INTENT_SALE);
        //Enviar parametros
        Intent i = new Intent(PagarAdeudo.this, PaymentActivity.class);
        i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        i.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(i, PAYPAL_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == PAYPAL_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirmation != null){
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(PagarAdeudo.this, PagoExitoso.class).putExtra("PaymentDetails", paymentDetails)
                                .putExtra("montoPagado", monto)
                                .putExtra("idRestaurante", idRestaurante));
                        Map <String, Object> noAdeudo = new HashMap<>();
                        noAdeudo.put("Adeudo", false);
                        String idUsuario = mAuth.getUid();
                        db.collection("usuario").document(idUsuario).update(noAdeudo)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(PagarAdeudo.this, "Adeudo Pagado", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PagarAdeudo.this, "Error al pagar el adeudo", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
                Toast.makeText(this, "Invalido", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}