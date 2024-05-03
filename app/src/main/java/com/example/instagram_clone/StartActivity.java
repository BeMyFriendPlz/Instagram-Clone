package com.example.instagram_clone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class StartActivity extends AppCompatActivity {
    private ImageView iconImage;
    private LinearLayout linearLayout;
    private Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initView();

        // Ẩn LinearLayout
        linearLayout.animate().alpha(0f).setDuration(1);

        // Tạo TranslateAnimation di chuyển lên trên 2000 pixel cho iconImage
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -2000);
        animation.setDuration(2000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MyAnimationListener());
        iconImage.setAnimation(animation);
    }

    private class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            iconImage.clearAnimation();
            iconImage.setVisibility(View.INVISIBLE);
            // Hiện LinearLayout
            linearLayout.animate().alpha(1f).setDuration(1000);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private void initView() {
        iconImage = findViewById(R.id.iconImage);
        linearLayout = findViewById(R.id.linear_layout);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
    }
}