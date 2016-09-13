package soprowerwolf.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import soprowerwolf.R;

public class LetsPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lets_play);
    }

    public void letsPlay(View view){
        Intent intent = new Intent(LetsPlayActivity.this, GameActivity.class);
        startActivity(intent);
    }
}
