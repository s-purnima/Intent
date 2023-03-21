package com.example.intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private View currLayout;
    private EditText usertext;
    private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currLayout = findViewById(R.id.linearlayout);
        displayAlphabetButtons(currLayout);
    }

    private void displayAlphabetButtons(View currLayout){

        Intent intent = new Intent(this,SecondActivity.class);

        for (char c = 'a'; c <= 'z'; c++) {
            Button button = new Button(this);
            button.setText(String.valueOf(c));
            button.setTextColor(getResources().getColor(R.color.white));
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    intent.putExtra("text",button.getText().toString());
                    startActivity(intent);
                }
            });
            if ((currLayout instanceof LinearLayout)) {
                ((LinearLayout) currLayout).addView(button);
            } else {
                ((GridLayout) currLayout).addView(button);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setTitle("Word App");
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //setting up icon
        this.menu = menu;
        setMenuIcon(R.drawable.ic_linear_layout);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_button) {
            if ((currLayout instanceof LinearLayout)) {// Changed layout here
                setContentView(R.layout.activity_main_grid);
                currLayout = findViewById(R.id.gridlayout);
                setMenuIcon(R.drawable.ic_grid_layout);
            } else {
                setContentView(R.layout.activity_main);
                currLayout = findViewById(R.id.linearlayout);
                setMenuIcon(R.drawable.ic_linear_layout);
            }
            displayAlphabetButtons(currLayout);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setMenuIcon(int layoutId){
        MenuItem menuItem = menu.findItem(R.id.action_button);
        Resources res = getResources();
        Drawable drawRequiredLayoutIcon = res.getDrawable(layoutId);
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{drawRequiredLayoutIcon});
        menuItem.setIcon(layerDrawable);
    }

    public void launchSecondActivity(View view){
//        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this,SecondActivity.class);
        intent.putExtra("text",usertext.getText().toString());
        startActivity(intent);
    }
}