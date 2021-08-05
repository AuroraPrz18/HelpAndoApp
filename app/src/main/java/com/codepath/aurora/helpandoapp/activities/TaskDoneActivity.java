package com.codepath.aurora.helpandoapp.activities;

import android.animation.Animator;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.codepath.aurora.helpandoapp.R;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.emitters.StreamEmitter;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class TaskDoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_done);
        // Hide action bar in android
        getSupportActionBar().hide();
        LottieAnimationView animationView = findViewById(R.id.ivAnimation);
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        // Konfetti
        KonfettiView vKonfetti = findViewById(R.id.viewKonfetti);
        vKonfetti.build()
                .addColors(Color.YELLOW, Color.RED, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 500)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(3000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(14, 5f))
                .setPosition(-50f, vKonfetti.getWidth() + 50f, -50f, -50f)
                .streamFor(1000, StreamEmitter.INDEFINITE);
    }
}