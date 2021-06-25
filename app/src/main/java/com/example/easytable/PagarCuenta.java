package com.example.easytable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class PagarCuenta extends AppCompatActivity {

    public static final String PAYPAL_CLIENT_ID = "AUM8W9KU49DmhJENE37MV5vTKeIfwJpOIh2jTYH1KhXrLhuYkkEZiDhG7yJxLuL-f0GpL1OCz3za6ITP";
    private static final int PAYPAL_REQUEST_CODE = 7171;
    private FirebaseFirestore db;

    //Esta configuracion es para utilizar cuenta sandbox para pruebas
    private static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId(PAYPAL_CLIENT_ID);

    //TODO: REALIZAR ESTA PARTE DE LOS PAGOS
    private TextView mMontoPagar;
    private Button mPagar;
    String monto;

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_pagar_cuenta);

        //Iniciar servicio paypal
        Intent i = new Intent(PagarCuenta.this, PayPalService.class);
        i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(i);

        mMontoPagar = findViewById(R.id.montoPagarTxt);
        mPagar = findViewById(R.id.pagarButton);

        db = FirebaseFirestore.getInstance();

        String idCuenta = getIntent().getStringExtra("idCuenta");

        DocumentReference doc = db.collection("cuenta").document(idCuenta);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String montoPagar = value.get("montoPagar").toString();
               // mMontoPagar.setText(montoPagar);
            }
        });


        mPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procesarPago();
            }
        });
    }//

    private void procesarPago() {
        //Recibir el monto
        //monto = mMontoPagar.getText().toString();
        monto = "0.01";
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(monto)), "MXN", "Pagado", PayPalPayment
                    .PAYMENT_INTENT_SALE);
        //Enviar parametros
        Intent i = new Intent(PagarCuenta.this, PaymentActivity.class);
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
                        startActivity(new Intent(PagarCuenta.this, PagoExitoso.class).putExtra("PaymentDetails", paymentDetails)
                        .putExtra("montoPagado", monto));
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