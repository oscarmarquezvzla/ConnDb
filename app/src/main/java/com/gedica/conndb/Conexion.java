package com.gedica.conndb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by gedica on 01/11/2017.
 */

public class Conexion extends Activity {
    public static Connection connection = null;
    public static String Result = "";

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);
       /* if (!verificaConexion(this)) {
            Toast.makeText(getBaseContext(),
                    "Mensaje: Comprueba tu conexión a Internet. Deteniendo Búsqueda ... ", Toast.LENGTH_SHORT)
                    .show();
            this.finish();
        } else {

            new Conexions().execute();
        }*/
        new Conexions().execute();
        //       MyAdapter adapter = new MyAdapter(this, obtDatosJSON());
//        setListAdapter(adapter);

    }

    // <Recibe, envia al onprogres, retorna>
    public class Conexions extends AsyncTask<Void, Integer, Boolean> {

        private Exception exception;
        private Context mContext;
        private Boolean Tunnel = true;
        ProgressDialog progressDialog = ProgressDialog.show(Conexion.this, " ", "Conectando...");

        protected Boolean doInBackground(Void... Params) {



            // Load the Oracle JDBC driver
            //  String driverName = "oracle.jdbc.OracleDriver";
            String driverName = "oracle.jdbc.driver.OracleDriver"; //SDK API 25
            try {
                Class.forName(driverName);
            }catch(ClassNotFoundException e){
                //tv.setText("No puede ubicar el driver o clase " +     e.getMessage());
                e.getStackTrace();
                return false;

            }
            // Create a connection to the database
            String serverName = "192.168.0.100";
            String serverPort = "1521";
            String sid = "xe";
            String url = "jdbc:oracle:thin:@" + serverName + ":" + serverPort + ":" + sid;
            String username = "system";
            String password = "cerebro";
            //Tunnel SSH

            if (Tunnel) {
                try {
                    //String driverName2 = "oracle.jdbc.OracleDriver";
                    //Class.forName(driverName2);
                    String serverName2 = "localhost";
                    String serverPort2 = "1521";
                    String sid2 = "xe";
                    String url2 = "jdbc:oracle:thin:@" + serverName2 + ":" + serverPort2 + ":" + sid2;
                    //String url2 = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
                    doSshTunnel("sapmovilqa", "S4pm0v1l", "200.188.145.22", 1414, "127.0.0.1", 1521, 1521);

                    connection = DriverManager.getConnection(url2, "sapmovilqa", "S4pm0v1l");
                } catch (Exception e) {
                    e.printStackTrace();

                    return false;
                }
            } else {
                try {
                    connection = DriverManager.getConnection(url, username, password);
                    //tv.setText("Conexion Establecida WIIIII");
                } catch(SQLException e){
                    //tv.setText("No se ha podido conectar " +     e.getMessage());
                    e.getStackTrace();
                    return false;
                }
            }
            return true;

        }
        @Override
        protected void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO: check this.exception
            // TODO: do something with the feed
            progressDialog.dismiss();
            //Button bt = (Button) findViewById(R.id.btlogin);
            if (result) {
                Toast.makeText(Conexion.this, "Conexión Realizada!", Toast.LENGTH_LONG).show();
                //bt.setEnabled(true);
                Intent i = new Intent(Conexion.this, login.class);
                startActivity(i);


            }else{
                Toast.makeText(Conexion.this, "Conexión Fallida!", Toast.LENGTH_LONG).show();

                //bt.setEnabled(false);

            }
        }
    }


    public static class EjecutarQuerySimple extends AsyncTask<String, Integer, String>{
        Context mContext;
        public EjecutarQuerySimple(MenuAlmPrincipal context) {
            super();
            mContext = context;
        }
        @Override
        protected String doInBackground(String... Params){


            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(Params[0]);


                if (rs.next()){
                    Result = rs.getString(1);
                }
                rs.close();
                st.close();

            }catch (SQLException e) {
                //tv.setText("No se ha podido conectar " +     e.getMessage());
                e.getStackTrace();
                Result = e.getMessage();
            }

            return Result;
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }




    }


    public static class EjecutarProcedimiento extends AsyncTask<String, Integer, String>{
        Context mContext;
        public EjecutarProcedimiento(Context context) {
            super();
            mContext = context;
        }
        @Override
        protected String doInBackground(String... Params){
            final List titles = new ArrayList();
            Result = null;
            int i = 0;

            try {
                //Statement st = connection.createStatement();
                PreparedStatement ps = connection.prepareCall(Params[0]);
                int e = Params.length;

                for (i=1; i < e ;i++) {
                    ps.setString(i,Params[i]);
                }



                //ps.setString(1,Params[1]);
                //ps.setString(2,Params[2]);
                ps.execute();
                //  ResultSet rs = (ResultSet) ps.getResultSet();
                // while (rs.next()) {
                //    titles.add(rs.getString(1));
                //}

                ps.close();


            }catch (SQLException e) {
                //tv.setText("No se ha podido conectar " +     e.getMessage());
                e.getStackTrace();
                Result = e.getMessage();
                //  Toast.makeText(mContext,Result,Toast.LENGTH_LONG).show();
            }

            return Result;
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }




    }



    public static boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // No sólo wifi, también GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        // este bucle debería no ser tan ñapa
        for (int i = 0; i < 2; i++) {
            // ¿Tenemos conexión? ponemos a true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }

    private static void doSshTunnel( String strSshUser, String strSshPassword, String strSshHost, int nSshPort, String strRemoteHost, int nLocalPort, int nRemotePort ) throws JSchException
    {
        final JSch jsch = new JSch();
        Session session = jsch.getSession( strSshUser, strSshHost, nSshPort );
        session.setPassword( strSshPassword );

        final Properties config = new Properties();
        config.put( "StrictHostKeyChecking", "no" );
        session.setConfig( config );

        session.connect();
        System.out.println(session.setPortForwardingL(nLocalPort, strRemoteHost, nRemotePort));
    }

    public static class EjecutarConsulta extends AsyncTask<String, Integer, String>{
        Context mContext;
        public EjecutarConsulta(MenuAlmPrincipal context) {
            super();
            mContext = context;
        }
        @Override
        protected String doInBackground(String... Params){


            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(Params[0]);

                while(rs.next()) {
                    for(int i=1; i<=rs.getFetchSize(); i++) {
                        Result = rs.getString(1);

                    }
                    Result+="\n";
                    return Result;
                }

                rs.close();
                st.close();

            }catch (SQLException e) {
                e.getStackTrace();
                Result = e.getMessage();
            }

            return Result;
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }




    }
}