package com.example.hilos_persistencia_sonido;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MotionEvent;

import static android.media.MediaPlayer.create;

public class Gestion extends Activity {
    private Partida partida;
    private int dificultad;
    private int FPS=30;
    //Al crear hilos hay que utilizar un Manejador (handler)
    private Handler temporizador;
    private int botes;
    MediaPlayer golpeo;
    MediaPlayer fin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        botes=0;

        //Se coge el nivel de dificultad que el usuario ha pulsado
        Bundle extras=getIntent().getExtras();
        dificultad=extras.getInt("DIFICULTAD");

        //Se crea la nueva partida y se carga
        partida=new Partida(getApplicationContext(), dificultad);
        setContentView(partida);
        temporizador=new Handler();
        temporizador.postDelayed(elhilo,2000);

        //Se inicializan los sonidos
        golpeo= MediaPlayer.create(this,R.raw.golpeobalon);
        fin=MediaPlayer.create(this,R.raw.finaljuego);
    }

    private Runnable elhilo=new Runnable() {
        @Override public void run() {
            if (partida.movimientoBola()) fin();
            else {
                partida.invalidate();   //Elimina el contenido de ImageView
                                        //y llama de nuevo a onDraw
                temporizador.postDelayed(elhilo, 1000/FPS);

            }
        }
    };

    public boolean onTouchEvent (MotionEvent evento) {
        int x=(int)evento.getX();
        int y=(int)evento.getY();

        golpeo.start(); //Cada vez que un usuario toque el balón, sonido

        if (partida.toque(x,y)) botes++; //Cada vez que toquemos la pelota, lo contamos

        return false;
    }

    public void fin(){
        temporizador.removeCallbacks(elhilo);

        fin.start(); //Antes de que la partida finalice, aplaudimos

        //Almacenamos la información del número de botes
        //para guardarlo para futuras partidas
        Intent in=new Intent();
        in.putExtra("PUNTUACION",botes*dificultad);
        setResult(RESULT_OK, in);

        finish();   //Destruye la actividad actual
    }
}
