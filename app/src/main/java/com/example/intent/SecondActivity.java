package com.example.intent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class SecondActivity extends AppCompatActivity {
    private HttpsURLConnection connection;

    public SecondActivity(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        String letter = getIntent().getStringExtra("text");
        SecondActivity sa = this;
        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int responseCode = getWord(letter);
                if(responseCode == 200){
                    try{
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while((line = reader.readLine()) != null){
                            response.append(line);
                        }
                        reader.close();

                        JSONArray jsonArray;
                        try{
                            jsonArray = new JSONArray(response.toString());
                            int limitwordNo = 5;
                            for(int i = 0; i < limitwordNo; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String word = jsonObject.getString("word");
                                if(word.length() > 1){
                                    display_buttons(word);
                                }else{
                                    limitwordNo += 1;
                                }
                            }
                        }catch(JSONException e){
                            Toast.makeText(sa,e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }catch(IOException e){
                        Toast.makeText(sa,e.toString(),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.d(SecondActivity.class.getSimpleName(),"Something went wrong");
                }
            }
        });
        newThread.start();
    }

    public void display_buttons(String word){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String url = "https://www.google.com/search?q="+word+" meaning";
        LinearLayout layout = (LinearLayout) findViewById(R.id.secondActivityLinearlayout);
        Button button = new Button(this);
        button.setText(String.valueOf(word));
        button.setTextColor(getResources().getColor(R.color.white));
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layout.addView(button);
            }
        });
    }

    public int getWord(String letter){
        String urlString = "https://api.datamuse.com/words?sp="+letter+"*";
        URL url;
        Log.d(SecondActivity.class.getSimpleName(),"urlString is: "+urlString);
        try{
            url = new URL(urlString);
            connection = (HttpsURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            return connection.getResponseCode();
        }catch(MalformedURLException e){
            return 404;
        }catch(IOException e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        }
        return 0;
    }
}