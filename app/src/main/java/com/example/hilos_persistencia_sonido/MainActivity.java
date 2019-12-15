package com.example.hilos_persistencia_sonido;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    int record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void ayuda(View vista) {

        Intent intencion=new Intent(this, AyudaActividad.class);
        startActivity(intencion);
    }

    public void dificultad (View vista) {
        String dific=(String)((Button)vista).getText();

        int dificultad=1;
        if (dific.equals("Standard")) dificultad=2;
        if (dific.equals("Dificult")) dificultad=3;

        Intent in=new Intent(this, Gestion.class);
        in.putExtra("DIFICULTAD", dificultad);

        //startActivity(in);
        startActivityForResult(in, 1);
    }

    protected void onActivityResult (int peticion, int codigo, Intent puntuacion) {
        if (peticion != 1 || codigo!=RESULT_OK) return;
        int resultado=puntuacion.getIntExtra("PUNTUACION", 0);

        if(resultado>record) {
            record=resultado;
            TextView caja=(TextView)findViewById(R.id.record);
            caja.setText("Record: " + record);

            guardarecord();
        } else {
            String puntuacionpartida="Puntuación: " + resultado + ". No superas el récord";

            Toast mitoast= Toast.makeText(this, puntuacionpartida, Toast.LENGTH_SHORT);
            mitoast.setGravity(Gravity.CENTER, 0,0);
            mitoast.show();
        }



    }

    protected void onResume(){
        super.onResume();

        leerecord();
    }

    private void guardarecord(){
        SharedPreferences datos= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mieditor=datos.edit();
        mieditor.putInt("RECORD", record);
        mieditor.apply();
    }

    private void leerecord(){
        SharedPreferences datos=PreferenceManager.getDefaultSharedPreferences(this);
        record=datos.getInt("RECORD", 0);

        TextView caja=(TextView)findViewById(R.id.record);
        caja.setText("Record:" + record);
    }
}
