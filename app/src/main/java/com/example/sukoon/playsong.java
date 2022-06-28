package com.example.sukoon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContentInfo;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class playsong extends AppCompatActivity {
@Override
protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseek.interrupt();
        }


        ImageView imageView;
        TextView textView;
        ImageView next,prev,play;
        ArrayList<File> songs;
        MediaPlayer mediaPlayer;
        SeekBar seekBar;
        String textcontent;
        int position;
        Thread updateseek;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playsong);

        textView = findViewById(R.id.textView);
        play = findViewById(R.id.pp);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList)bundle.getParcelableArrayList("songlist");
        textcontent = intent.getStringExtra("currentsong");
        textView.setText(textcontent);
        position = intent.getIntExtra("position",0);

        textView.setSelected(true);
        playingsong(position,this);
        imageView = findViewById(R.id.imageView2);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
@Override
public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

@Override
public void onStartTrackingTouch(SeekBar seekBar) {

        }

@Override
public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.seekTo(seekBar.getProgress());
        }
        });

        updateseek = new Thread(){
@Override
public void run() {
        int Currentpos =0;
        try {
        while (Currentpos<mediaPlayer.getDuration()){
        Currentpos = mediaPlayer.getCurrentPosition();
        seekBar.setProgress(Currentpos);
        sleep(800);
        }
        }
        catch (Exception e){
        e.printStackTrace();
        }
        }
        };
        updateseek.start();
        play.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {

        if (mediaPlayer.isPlaying()){
        play.setImageResource(R.drawable.play);
        mediaPlayer.stop();
        }
        else {
        playingsong(position,getApplicationContext());
        }
        }
        });
        prev.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        mediaPlayer.stop();

        mediaPlayer.release();
        if (position!=0){
        position=position-1;
        }
        else {
        position=songs.size()-1;
        }
        playingsong(position,getApplicationContext());
        textcontent = songs.get(position).getName();
        textView.setText(textcontent);
        }

        });
        next.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        mediaPlayer.stop();
        mediaPlayer.release();
        if (position!=songs.size()-1){
        position=position+1;
        }
        else {
        position=0;
        }
        textcontent = songs.get(position).getName();
        textView.setText(textcontent);
        playingsong(position,getApplicationContext());
        }
        });

        }
public void playingsong(int position1, Context context) {
        Uri uri = Uri.parse(songs.get(position1).toString());
        mediaPlayer = MediaPlayer.create(context, uri);
        mediaPlayer.start();
        play.setImageResource(R.drawable.pause);
        seekBar.setMax(mediaPlayer.getDuration());

        }

}








