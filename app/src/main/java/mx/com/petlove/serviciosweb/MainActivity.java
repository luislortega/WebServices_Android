package mx.com.petlove.serviciosweb;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //Variables que se utilizan
    Button consultar, consultarID, insertar, borrar, actualizar;
    EditText id, nombre, direccion;
    TextView resultado;
    ObtenerWebService hiloConexion;
    String identificador, direccObtenida, nombreObtenido;

    //IP de mi URL
    String IP = "http://petlove.hol.es/";
    //Opciones de URL
    String GET = IP + "/Obtener_alumnos.php";
    String GET_BY_ID = IP + "/Obtener_alumno_por_id.php";
    String UPDATE = IP + "/Actualizar_alumno.php";
    String DELETE = IP + "/Borrar_alumno.php";
    String INSERT = IP + "/Insertar_alumno.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Asigno los Button
        consultar = (Button) findViewById(R.id.button1);
        consultarID = (Button) findViewById(R.id.button2);
        insertar = (Button) findViewById(R.id.button3);
        borrar = (Button) findViewById(R.id.button4);
        actualizar = (Button) findViewById(R.id.button5);
        //Asino los EditText
        id = (EditText) findViewById(R.id.txtID);
        nombre = (EditText) findViewById(R.id.txtNombre);
        direccion = (EditText) findViewById(R.id.txtDirec);
        //Asino el TextView
        resultado = (TextView) findViewById(R.id.lblResultado);

        //Asino el OnClickListener a los botones
        consultar.setOnClickListener(this);
        consultarID.setOnClickListener(this);
        insertar.setOnClickListener(this);
        borrar.setOnClickListener(this);
        actualizar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                //Accion para el boton consultar
                hiloConexion = new ObtenerWebService();
                hiloConexion.execute(GET,"1");

                break;
            case R.id.button2:
                //Accion para el boton consultar por ID

                hiloConexion = new ObtenerWebService();
                String cadenaLlamada = GET_BY_ID + "?idalumno=" + id.getText().toString();

                hiloConexion.execute(cadenaLlamada,"2");

                break;
            case R.id.button3:
                //Accion para el boton insertar

                hiloConexion = new ObtenerWebService();
                nombreObtenido = nombre.getText().toString();
                direccObtenida = direccion.getText().toString();

                hiloConexion.execute(INSERT,"3", nombreObtenido, direccObtenida);

                break;
            case R.id.button4:
                //Accion para el boton borrar

                identificador = id.getText().toString();

                hiloConexion = new ObtenerWebService();
                hiloConexion.execute(DELETE,"4", identificador);

                break;
            case R.id.button5:
                //Accion para el boton actualizar

                identificador = id.getText().toString();
                nombreObtenido = nombre.getText().toString();
                direccObtenida = direccion.getText().toString();

                hiloConexion = new ObtenerWebService();
                hiloConexion.execute(UPDATE,"5", identificador, nombreObtenido, direccObtenida);
                break;
        }
    }

    public class ObtenerWebService extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            resultado.setText(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null;
            String devuelve = "";

            if (params[1] == "1") { // OBTENER TODOS LOS ALUMNOS

                try {
                    url = new URL(cadena);
                    HttpURLConnection conexion = (HttpURLConnection) url.openConnection(); //Abro la conexion
                    conexion.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            "(Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    int respuesta = conexion.getResponseCode();
                    StringBuilder resultado = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {

                        InputStream in = new BufferedInputStream(conexion.getInputStream()); //Preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in)); //Intruzco el inputStrem en un buffered

                        /**
                         * Hago un StringBuilder que convierte el BufferedReader en un  String.
                         */

                        String linea;

                        while ((linea = reader.readLine()) != null) {
                            resultado.append(linea);
                        }

                        //Creamos un objeto JSONObject para poder acceder a los atributos (Campos) del objeto
                        JSONObject respuestaJSON = new JSONObject(resultado.toString());
                        String resultadoJSON = respuestaJSON.getString("estado"); //Results es el nombre del campo en el JSON

                        if (resultadoJSON == "1") {

                            JSONArray alumnosJSON = respuestaJSON.getJSONArray("alumnos");
                            for (int i = 0; i < alumnosJSON.length(); i++) {
                                devuelve = devuelve + alumnosJSON.getJSONObject(i).getString("idalumno") + " " +
                                        alumnosJSON.getJSONObject(i).getString("nombre") + " " +
                                        alumnosJSON.getJSONObject(i).getString("direccion") + "\n";
                            }

                        } else{
                            devuelve = "No hay alumnos";
                        }

                    }

                } catch (Exception ex) {

                    Toast.makeText(getApplicationContext(), "Error: " + ex, Toast.LENGTH_SHORT).show();

                }

                return devuelve;

            }else if (params[1] == "2") { //OBTENER ALUMNO POR ID

                try {
                    url = new URL(cadena);
                    HttpURLConnection conexion = (HttpURLConnection) url.openConnection(); //Abro la conexion
                    conexion.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            "(Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    int respuesta = conexion.getResponseCode();
                    StringBuilder resultado = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {

                        InputStream in = new BufferedInputStream(conexion.getInputStream()); //Preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in)); //Intruzco el inputStrem en un buffered

                        /**
                         * Hago un StringBuilder que convierte el BufferedReader en un  String.
                         */

                        String linea;

                        while ((linea = reader.readLine()) != null) {
                            resultado.append(linea);
                        }

                        //Creamos un objeto JSONObject para poder acceder a los atributos (Campos) del objeto
                        JSONObject respuestaJSON = new JSONObject(resultado.toString());
                        String resultadoJSON = respuestaJSON.getString("estado"); //Results es el nombre del campo en el JSON

                        if (resultadoJSON == "1") {

                            devuelve = devuelve + respuestaJSON.getJSONObject("alumno").getString("idalumno") + " " +
                                    respuestaJSON.getJSONObject("alumno").getString("nombre") + " " +
                                    respuestaJSON.getJSONObject("alumno").getString("direccion");

                        } else {
                            devuelve = "No hay alumnos";
                        }

                    }

                } catch (Exception ex) {

                    Toast.makeText(getApplicationContext(), "Error: " + ex, Toast.LENGTH_SHORT).show();

                }

                return devuelve;

            }else if (params[1] == "3") { //INSERTAR ALUMNO

                try {

                    HttpURLConnection urlConn;

                    DataOutputStream printout;
                    DataInputStream input;
                    url = new URL(cadena);
                    urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setDoInput(true);
                    urlConn.setDoOutput(true);
                    urlConn.setUseCaches(false);
                    urlConn.setRequestProperty("Content-Type", "application/json");
                    urlConn.setRequestProperty("Accept", "application/json");
                    urlConn.connect();
                    //Creo el objeto JSON y le envio los parametros por POST
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("nombre", params[2]);
                    jsonParam.put("direccion", params[3]);
                    //Envio los parametros post
                    OutputStream os = urlConn.getOutputStream();
                    BufferedWriter write = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8")
                    );
                    write.write(jsonParam.toString());
                    write.flush(); //envia
                    write.close(); //cierra el flush

                    int respuesta = urlConn.getResponseCode();

                    StringBuilder resultado = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){
                        String linea;
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(urlConn.getInputStream())
                        );
                        /**
                         * Hace el buffer reader para leer la respuesta
                         */
                        while ((linea = br.readLine()) != null){
                            resultado.append(linea);
                        }

                        JSONObject respuestaJSON = new JSONObject(resultado.toString());

                        String resultadoJSON = respuestaJSON.getString("estado");

                        if (resultadoJSON == "1"){
                            devuelve = "Insertado correctamente";
                        }else if (resultadoJSON == "2"){
                            devuelve = "No se puso insertar";
                        }
                    }
                }catch (Exception ex){

                    Toast.makeText(getApplicationContext(), "Error: "+ex, Toast.LENGTH_SHORT).show();

                }

                return devuelve;

            }else if (params[1] == "4") { //BORRAR ALUMNO POR ID

                try {

                    HttpURLConnection urlConn;

                    DataOutputStream printout;
                    DataInputStream input;
                    url = new URL(cadena);
                    urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setDoInput(true);
                    urlConn.setDoOutput(true);
                    urlConn.setUseCaches(false);
                    urlConn.setRequestProperty("Content-Type", "application/json");
                    urlConn.setRequestProperty("Accept", "application/json");
                    urlConn.connect();
                    //Creo el objeto JSON
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("idalumno", params[2]);
                    //Envio los parametros post
                    OutputStream os = urlConn.getOutputStream();
                    BufferedWriter write = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8")
                    );
                    write.write(jsonParam.toString());
                    write.flush(); //envia
                    write.close(); //cierra el flush

                    int respuesta = urlConn.getResponseCode();

                    StringBuilder resultado = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){
                        String linea;
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(urlConn.getInputStream())
                        );
                        /**
                         * Hace el buffer reader para leer la respuesta
                         */
                        while ((linea = br.readLine()) != null){
                            resultado.append(linea);
                        }

                        JSONObject respuestaJSON = new JSONObject(resultado.toString());

                        String resultadoJSON = respuestaJSON.getString("estado");

                        if (resultadoJSON == "1"){
                            devuelve = "Elminacion correcta";
                        }else{
                            devuelve = "No se pudo eliminar";
                        }
                    }
                }catch (Exception ex){

                    Toast.makeText(getApplicationContext(), "Error: "+ex, Toast.LENGTH_SHORT).show();

                }

                return devuelve;

            }else if (params[1] == "5") { //ACTUALIZAR ALUMNO

                try {

                    HttpURLConnection urlConn;

                    DataOutputStream printout;
                    DataInputStream input;
                    url = new URL(cadena);
                    urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setDoInput(true);
                    urlConn.setDoOutput(true);
                    urlConn.setUseCaches(false);
                    urlConn.setRequestProperty("Content-Type", "application/json");
                    urlConn.setRequestProperty("Accept", "application/json");
                    urlConn.connect();
                    //Creo el objeto JSON
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("idalumno", params[2]);
                    jsonParam.put("nombre", params[3]);
                    jsonParam.put("direccion", params[4]);
                    //Envio los parametros post
                    OutputStream os = urlConn.getOutputStream();
                    BufferedWriter write = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8")
                    );
                    write.write(jsonParam.toString());
                    write.flush(); //envia
                    write.close(); //cierra el flush

                    int respuesta = urlConn.getResponseCode();

                    StringBuilder resultado = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){
                        String linea;
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(urlConn.getInputStream())
                        );
                        /**
                         * Hace el buffer reader para leer la respuesta
                         */
                        while ((linea = br.readLine()) != null){
                            resultado.append(linea);
                        }

                        JSONObject respuestaJSON = new JSONObject(resultado.toString());

                        String resultadoJSON = respuestaJSON.getString("estado");

                        if (resultadoJSON == "1"){
                            devuelve = "Actualizacion correcta";
                        }else{
                            devuelve = "No se pudo actualziar";
                        }
                    }
                }catch (Exception ex){

                    Toast.makeText(getApplicationContext(), "Error: "+ex, Toast.LENGTH_SHORT).show();

                }

                return devuelve;

            }

            return null;
        }
    }
}
