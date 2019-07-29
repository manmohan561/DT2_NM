package com.example.dt1_normalmode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class GameActivity extends AppCompatActivity {
    private ConnectFourView myConnectFourView;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent= getIntent();
        bundle= intent.getExtras();

        if (bundle!= null) {
            int width= bundle.getInt(MainActivity.EXTRA_WIDTH);
            int height= bundle.getInt(MainActivity.EXTRA_HEIGHT);
            int mode= bundle.getInt(MainActivity.EXTRA_MODE, 0);
            myConnectFourView= new ConnectFourView(this, width, height, mode);
        }
        else myConnectFourView= new ConnectFourView(this, 7, 6, 0);

        setContentView(myConnectFourView);
    }

    @Override
    public void onBackPressed() {
        if (!myConnectFourView.reverse())
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ga_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        if (menuItem.getItemId()== R.id.ga_MenuReset) {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtras(bundle);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
            return true;
        }
        else return super.onOptionsItemSelected(menuItem);
    }
}
