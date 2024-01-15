package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Splash extends AppCompatActivity implements Animation.AnimationListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        TextView textoCargando = findViewById(R.id.cargando);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.splash);
        textoCargando.startAnimation(animation);
        animation.setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        //para pasar del splash a la main
        Intent intent = new Intent(Splash.this, Login.class);
        startActivity(intent);
        //terminar el splash
        finish();

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
