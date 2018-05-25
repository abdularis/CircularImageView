package com.github.abdularis.civsample;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.abdularis.civ.AvatarImageView;
import com.github.abdularis.civsample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMyName("Vatar");
        binding.setBgColor(Color.BLUE);
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

    public void onGotoListSampleClick(View view) {
        Intent i = new Intent(this, PersonListActivity.class);
        startActivity(i);
    }
}
