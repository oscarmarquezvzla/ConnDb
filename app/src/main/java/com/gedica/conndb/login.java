package com.gedica.conndb;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

/**
 * Created by gedica on 01/11/2017.
 */



public class login extends Activity {

    public static EditText tfusuario, tfpassword;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.login);
        Button bt = (Button) findViewById(R.id.btlogin);
        bt.setEnabled(true);

        tfusuario = (EditText) findViewById(R.id.tfusuario);
        tfpassword = (EditText) findViewById(R.id.tfpassword);


    }

    public void LoginOnClick(View view){


        String Resultado = null;
        String Usuario = tfusuario.getText().toString();
        String Clave = tfpassword.getText().toString();

        String consulta = "call control_conexion_sap(?,?)";

        //Invoco a clase y funcion que ejecuta comando SQL
        //new Conexion.EjecutarComando(this).execute(consulta);
        //Obtengo el valor de la consulta realizada y almaceno en Resultado.
        try {
            Resultado = new Conexion.EjecutarProcedimiento(this).execute(consulta, Usuario, Clave).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        consulta = "call cargar_lista_centro_almacen()";
        try {
            Resultado = new Conexion.EjecutarProcedimiento(this).execute(consulta).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //Resultado = Conexion.Result;
        if (Resultado!=null) {
            Toast.makeText(login.this, Resultado, Toast.LENGTH_LONG).show();
        }else{
            Intent i = new Intent(login.this, CentroAlmacenGui.class);
            startActivity(i);
        }

        // Intent i = new Intent(this, Buscar.class);
        // startActivity(i);
    }




}