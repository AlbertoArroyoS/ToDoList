package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    //Variables para altas y consultas de firebase
    FirebaseFirestore miBaseDatos;
    String idUser;

    ListView listViewTareas;
    List<String> listaTareas = new ArrayList<>();
    List<String> listaIdTareas = new ArrayList<>();
    ArrayAdapter<String> mAdapterTareas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Inicializo variables para altas y consultas
        miBaseDatos = FirebaseFirestore.getInstance();
        idUser = mAuth.getCurrentUser().getUid();

        listViewTareas = findViewById(R.id.ListView);


        actualizarUI();
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
                            String tarea = taskEditext.getText().toString();
                            Map<String, Object> miTarea = new HashMap<>();
                            miTarea.put("nombreTarea", tarea);
                            miTarea.put("idUsuario", idUser);

                            //Añadir un nuevo documento con el id generado
                            miBaseDatos.collection("Tareas").add(miTarea);
                            Toast.makeText(MainActivity.this, "Tarea añadida", Toast.LENGTH_LONG).show();

                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .create();
            //activar el cuadro de dialogo para añadir tarea

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
    //Metodo para actualizar los datos en tiempo real de la coleccion

    private void actualizarUI(){
        miBaseDatos.collection("Tareas")
                .whereEqualTo("idUsuario", idUser) //Solo las tareas del usuario logeado
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null){
                            return;
                        }

                        //limpiar las listas
                        listaIdTareas.clear();
                        listaTareas.clear();

                        //rellenar el listview con las listas
                        for(QueryDocumentSnapshot doc: value){
                            listaIdTareas.add(doc.getId());
                            listaTareas.add(doc.getString("nombreTarea"));

                        }

                        //si la lista esta vacia
                        if(listaTareas.size()==0){
                            listViewTareas.setAdapter(null);
                        }else{
                            mAdapterTareas = new ArrayAdapter<>(MainActivity.this,R.layout.item_tarea, R.id.textViewTarea, listaTareas);
                            listViewTareas.setAdapter(mAdapterTareas);
                        }
                    }
                });

    }
    //metodo para borrar la tareas al darle al boton done
    public void borrarTarea(android.view.View view){

        android.view.View parent = (View) view.getParent();
        TextView tareaTextView = parent.findViewById(R.id.textViewTarea);
        String tarea = tareaTextView.getText().toString();
        int posicion = listaTareas.indexOf(tarea);

        miBaseDatos.collection("Tareas").document(listaIdTareas.get(posicion)).delete();

    }

    //metodo para modificar una tarea
    public void modificarTarea(android.view.View view){

        android.view.View parent = (View) view.getParent();
        TextView tareaTextView = parent.findViewById(R.id.textViewTarea);
        String tarea = tareaTextView.getText().toString();
        int posicion = listaTareas.indexOf(tarea);

        final EditText taskEditext = new EditText(this);
        taskEditext.setText(tarea);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Modificar Tarea")
                .setMessage("Que quieres hacer a continuación")
                .setView(taskEditext)
                .setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //añadir la tarea a la base de dato
                        String tarea = taskEditext.getText().toString();
                        Map<String, Object> miTarea = new HashMap<>();
                        miTarea.put("nombreTarea", tarea);
                        miTarea.put("idUsuario", idUser);

                        //Añadir un nuevo documento con el id generado
                      //  miBaseDatos.collection("Tareas").document(listaIdTareas.get(posicion)).delete();
                        miBaseDatos.collection("Tareas").document(listaIdTareas.get(posicion)).update(miTarea);
                      //  miBaseDatos.collection("Tareas").add(miTarea);
                        Toast.makeText(MainActivity.this, "Tarea modificada", Toast.LENGTH_LONG).show();

                    }
                })
                .setNegativeButton("Cancelar", null)
                .create();
        //activar el cuadro de dialogo para añadir tarea

        dialog.show();
       // return true;
    }


}