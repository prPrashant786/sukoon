package com.example.sukoon;

import static androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Pair;
import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView view2 ;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        ArrayList<File> mysongs = fetchsongs(Environment.getExternalStorageDirectory());
//                        ArrayList<String> mysongs = fetchsongs();
                        String[] items = new String[mysongs.size()];

                        for (int i=0;i<mysongs.size();i++){
                            items[i] = mysongs.get(i).getName().replace(".mp3","");
//                            items[i] = mysongs.get(i);
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,items){
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent){
                                // Get the Item from ListView
                                View view = super.getView(position, convertView, parent);

                                // Initialize a TextView for ListView each Item
                                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                                tv.setTransitionName("new act");

                                // Set the text color of TextView (ListView Item)
                                tv.setTextColor(Color.RED);
                                view.setBackground(getContext().getDrawable(R.drawable.listview_item_border));


                                // Generate ListView Item using TextView
                                view2 = tv;
                                return view;
                            }
                        };
                        listView.setAdapter(arrayAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(MainActivity.this,playsong.class);
                                String currsong = listView.getItemAtPosition(position).toString();
                                intent.putExtra("songlist",mysongs);
                                intent.putExtra("currentsong",currsong);
                                intent.putExtra("position",position);

                                Pair[] pairs = new Pair[1];
                                pairs[0] = new Pair<View,String>(view2,"new act");

                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);

                                startActivity(intent,options.toBundle());
                            }
                        });

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                    }
                })
                .check();

    }
    public ArrayList<File> fetchsongs(File file){
//    public ArrayList<String> fetchsongs(){

        ArrayList arrayList = new ArrayList();
        File [] songs = file.listFiles();

        if (songs!=null){
            for(File myfile : songs){
                if (!myfile.isHidden() && myfile.isDirectory()){
                    arrayList.addAll(fetchsongs(myfile));
                }
                else {
                    if ((myfile.getName().endsWith(".mp3") || myfile.getName().endsWith(".wav")) && !myfile.getName().startsWith(".")){
                        arrayList.add(myfile);
                    }
                }
            }
        }
        return arrayList;
    }
}