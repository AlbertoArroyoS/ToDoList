package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }
    //Para que aparezca el menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //doy funcionalidad a cada uno de los botones

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.mas){
            final EditText taskEditext = new EditText(this);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Nueva tarea")
                    .setMessage("Que quieres hacer a continuación")
                    .setView(taskEditext)
                    .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //añadir la tarea a la base de datos
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .create();
            //activar el cuadro de dialogo para añadir tarea
            Toast.makeText(this, "Tarea añadida", Toast.LENGTH_LONG).show();
            dialog.show();
            return true;
        }
        if (item.getItemId()==R.id.logout){
            //cierre de sesion
            mAuth.signOut();
            //pasar al login al cerrar sesion
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }

    }
}