package com.gedica.conndb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Gedica-25 on 20-11-2017.
 */

public class MainActivity extends AppCompatActivity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Context context = getApplicationContext();
        if (!Conexion.verificaConexion(this)) {
            Toast.makeText(getBaseContext(),
                    "Error: Comprueba tu conexion a Internet. Deteniendo Aplicación ... ", Toast.LENGTH_SHORT)
                    .show();
            this.finish();
        }

        //LLama la clase Conexion
        Intent i;
        i = new Intent(this, Conexion.class);
        startActivity(i);
    }
}