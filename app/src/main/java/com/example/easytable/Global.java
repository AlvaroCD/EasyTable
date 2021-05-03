package com.example.easytable;

import android.app.Application;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Global extends Application {

    private static String mIdUsuario;

    public static String getmIdUsuario() {return mIdUsuario; }

    public static void setmIdUsuario(String mIdUsuario) {
        Global.mIdUsuario = mIdUsuario;
    }



}
