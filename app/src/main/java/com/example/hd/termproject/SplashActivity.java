package com.example.hd.termproject;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity{
    FrameLayout titleFrame;
    ImageView titleImage;
    int screenWidth;
    public static SplashActivity usedForFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        usedForFinish = SplashActivity.this;

        titleFrame = (FrameLayout)findViewById(R.id.activity_splash);
        titleImage = (ImageView)findViewById(R.id.title_animation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        startTitleAnimation();
    }

    private void startTitleAnimation() {
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(titleImage, "translationX", 1000, 0);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(titleImage,"alpha",1.0f,0.0f);
        alphaAnimator.setStartDelay(2000);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(positionAnimator,alphaAnimator);

        animatorSet.setDuration(1000);
        animatorSet.start();
        animatorSet.addListener(animatorListener);
    }

    Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }
    };
}
