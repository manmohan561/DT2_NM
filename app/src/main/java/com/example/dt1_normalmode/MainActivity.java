package com.example.dt1_normalmode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private int player1, player2;
    private String gridDimension;

    public final static String EXTRA_WIDTH= "com.example.dt1_normalmode.width";
    public final static String EXTRA_HEIGHT= "com.example.dt1_normalmode.height";
    public final static String EXTRA_MODE= "com.example.dt1_normalmode.mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spPlayer1= findViewById(R.id.spPlayer1);
        Spinner spPlayer2= findViewById(R.id.spPlayer2);
        Spinner spGridDimension= findViewById(R.id.spGridDimension);

        ArrayAdapter<CharSequence> adPlayer1= ArrayAdapter.createFromResource(this, R.array.am_player1, R.layout.spinner_item);
        ArrayAdapter<CharSequence> adPlayer2= ArrayAdapter.createFromResource(this, R.array.am_player2, R.layout.spinner_item);
        ArrayAdapter<CharSequence> adGridDimension= ArrayAdapter.createFromResource(this, R.array.am_GridDimension, R.layout.spinner_item);

        adPlayer1.setDropDownViewResource(R.layout.spinner_item);
        adPlayer2.setDropDownViewResource(R.layout.spinner_item);
        adGridDimension.setDropDownViewResource(R.layout.spinner_item);

        spPlayer1.setAdapter(adPlayer1);
        spPlayer2.setAdapter(adPlayer2);
        spGridDimension.setAdapter(adGridDimension);

        spPlayer1.setOnItemSelectedListener(this);
        spPlayer2.setOnItemSelectedListener(this);
        spGridDimension.setOnItemSelectedListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.am_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        if (menuItem.getItemId()== R.id.amMenuOk) {
            Bundle bundle = calculate();
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtras(bundle);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            return true;
        }
        else return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spPlayer1 : if (position==0)
                player1= 0;
            else player1= position-1;
            return;
            case R.id.spPlayer2 : if (position==0)
                player1= 0;
            else player2= position-1;
            return;
            case R.id.spGridDimension : if (position==0)
                gridDimension= "7x6";
            else gridDimension= parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        switch (parent.getId()) {
            case R.id.spPlayer1 : player1= 0;
                return;
            case R.id.spPlayer2 : player2= 1;
                return;
            case R.id.spGridDimension : gridDimension= "7x6";
        }
    }

    private Bundle calculate() {
        int width, height;
        Bundle bundle= new Bundle();

        int mode;
        if (player1== 0 && player2== 0)
            mode = 0;
        else if (player1== 1 && player2== 0)
            mode = 1;
        else if (player1== 0 && player2==1)
            mode = 2;
        else mode = 3;

        width= Character.getNumericValue(gridDimension.charAt(0));
        height= Character.getNumericValue(gridDimension.charAt(2));

        bundle.putInt(EXTRA_WIDTH, width);
        bundle.putInt(EXTRA_HEIGHT, height);
        bundle.putInt(EXTRA_MODE, mode);

        return bundle;
    }
}
