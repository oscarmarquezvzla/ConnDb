package com.gedica.conndb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class MenuAlmPrincipal extends AppCompatActivity {

    private Button btnClick,btnClick2;
    private String src;
    private String Resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menualmprincipal);
        btnClick = (Button) findViewById(R.id.btn2) ;
        btnClick2 = (Button) findViewById(R.id.btn1) ;
    }

    protected void OnClick(View view){

        //btnClick = (Button) findViewById(R.id.btn2) ;
        //btnClick.setOnClickListener((View.OnClickListener) this);
        if (view == btnClick){
            src = "select centro from centro_almacen";
            try {
                Resultado = new Conexion.EjecutarConsulta(this).execute(src).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            //Resultado = Conexion.Result;
            if (Resultado!=null) {
                Toast.makeText(this, Resultado, Toast.LENGTH_LONG).show();
            }else{
                Intent i = new Intent(this, CentroAlmacenGui.class);
                startActivity(i);
            }


        }else if (view == btnClick2){
            Intent i = new Intent(this, CentroAlmacenGui.class);
            startActivity(i);
        }

    }
}
