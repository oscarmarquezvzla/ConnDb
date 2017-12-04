package com.gedica.conndb;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CentroAlmacenGui extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.centroalmacengui);
        EditText EtUltCenAlm = findViewById(R.id.et1);
        EtUltCenAlm.setText("C300");
    }
    public void Aceptar_clic (View View){
        Intent i = new Intent(CentroAlmacenGui.this, MenuAlmPrincipal.class);
        startActivity(i);

    }
    public void Cancelar_clic (View View){
        this.finish();

    }

}
