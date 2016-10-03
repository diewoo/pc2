package com.example.jose.pokemong;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import clases.Pokemones;
import conexion.Conexion;
import interfaces.UsuariosService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MisPokemonesActivity extends AppCompatActivity {
    TextView tviNivel;
    TextView tviTipo;
    ImageView img;
    Button butSiguiente;
    Button butAtras;
    int id=0;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_pokemones);
        tviNivel = (TextView) findViewById(R.id.tviNivel);
        tviTipo = (TextView) findViewById(R.id.tviTipo);
        img = (ImageView) findViewById(R.id.img);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
        }

        butSiguiente= (Button) findViewById(R.id.butSiguiente);
        butAtras = (Button) findViewById(R.id.butAtras);

        Conexion conexion = new Conexion();
        Retrofit retrofit = conexion.getConexion();

        UsuariosService usuariosService = retrofit.create(UsuariosService.class);

        usuariosService.getPokemones(id).enqueue(new Callback<List<Pokemones>>() {
            @Override
            public void onResponse(Call<List<Pokemones>> call, Response<List<Pokemones>> response) {
                final List<Pokemones> pokemones = response.body();
                int status = response.code();
                Log.d("MainActivity","Status: " + status);
                cargarInformacion(pokemones);

                butSiguiente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(i==pokemones.size()){
                            Toast.makeText(MisPokemonesActivity.this, "Ya no hay mas pokemones", Toast.LENGTH_SHORT).show();
                        }else{
                            //TODO: Pasa al siguiente pokemon que trajo del webservice
                            i++;
                            cargarInformacion(pokemones);
                        }

                    }
                });


                butAtras.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(i==0){
                            Toast.makeText(MisPokemonesActivity.this, "Presionar >>", Toast.LENGTH_SHORT).show();
                        }else{
                            //TODO: Retrocede al pokemon previo
                            i--;
                            cargarInformacion(pokemones);
                        }

                    }
                });


            }

            @Override
            public void onFailure(Call<List<Pokemones>> call, Throwable t) {
                Log.e("MisPokemonesActivity",t.getMessage());
            }
        });






        Button butMenu = (Button) findViewById(R.id.butMenu);
        butMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MisPokemonesActivity.this,DashboardActivity.class);
                startActivity(intent);
            }
        });


    }

    public void cargarInformacion(List<Pokemones> pokemones){
        URL url = null;
        try {
            url = new URL(pokemones.get(i).getUrl());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            img.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tviNivel.setText(pokemones.get(i).getNivel());
        tviTipo.setText(pokemones.get(i).getTipo());
    }

}
