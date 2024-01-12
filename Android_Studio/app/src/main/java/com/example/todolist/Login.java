package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    //variable para guardar los botones
    Button botonLogin;
    TextView botonRegistro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        botonLogin = findViewById(R.id.botonLogin);
        //poner a la escucha
        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Para que pase del login a la main
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });

        botonRegistro = findViewById(R.id.botonRegistro);
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //crear el usuario en firebase

                //Si es correcto saco un toast y paso a la siguiente
                Toast.makeText(Login.this, "Usuario Registrado", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}