package com.github.abdularis.civsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.abdularis.civ.AvatarImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onCircleImageClick(View view) {
        Toast.makeText(this, "Hello World!", Toast.LENGTH_SHORT).show();
    }

    public void onAClick(View view) {
        AvatarImageView a = (AvatarImageView) view;
        if (a.getState() == AvatarImageView.SHOW_INITIAL) {
            a.setState(AvatarImageView.SHOW_IMAGE);
        } else {
            a.setState(AvatarImageView.SHOW_INITIAL);
        }
    }
}
