package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    //variable para guardar los botones
    Button botonLogin;
    TextView botonRegistro;
    private FirebaseAuth mAuth;
    //Variables para las cajas de usuario y contraseña
    EditText emailText, passText;
    TextInputLayout emailLayout, passLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Referencias de las cajas
        emailText = findViewById(R.id.cajaCorreo);
        passText = findViewById(R.id.cajaPass);

        emailLayout = findViewById(R.id.layoutCorreo);
        passLayout = findViewById(R.id.layoutPass);

        botonLogin = findViewById(R.id.botonLogin);
        //poner a la escucha
        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Login en firebase
                String email = emailText.getText().toString();
                String password = passText.getText().toString();

                //validarFormulario();
                if(validateMail() && validatePass()){
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Login.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }

            }
        });

        botonRegistro = findViewById(R.id.botonRegistro);
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //crear el usuario en firebase
                String email = emailText.getText().toString();
                String password = passText.getText().toString();

                if(validateMail() && validatePass()){
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        //Si es correcto saco un toast y paso a la siguiente
                                        Toast.makeText(Login.this, "Usuario Registrado", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Login.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                }
            }
        });
    }

    private boolean validateMail() {
        String email = emailText.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            emailLayout.setError(getString(R.string.error_mail));
            emailLayout.requestFocus();
            Toast.makeText(Login.this, "Introduzca un email.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            emailLayout.setErrorEnabled(false);
            return isValidEmail(email);
        }
    }
    private boolean validatePass() {
        String password = passText.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            passLayout.setError(getString(R.string.error_password));
            passLayout.requestFocus();
            Toast.makeText(Login.this, "Introduzca contraseña.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 5) {
            passLayout.setError(getString(R.string.error_short_password));
            passLayout.requestFocus();
            Toast.makeText(Login.this, "La contraseña debe tener al menos " + 5 + " caracteres.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            passLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean isValidEmail(String email) {
        // Patrón para validar el formato de un correo electrónico
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.+[a-zA-Z]{2,4}";

        // Verificar si el correo electrónico coincide con el patrón
        if (email.matches(emailPattern)) {
            return true; // El correo electrónico tiene un formato válido
        } else {
            emailLayout.setError(getString(R.string.error_invalid_email));
            emailLayout.requestFocus();
            Toast.makeText(Login.this, "Introduzca un correo electrónico válido.", Toast.LENGTH_SHORT).show();
            return false; // El correo electrónico no tiene un formato válido
        }
    }
}