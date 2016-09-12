package soprowerwolf.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import soprowerwolf.R;

public class GetRole extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_role);
    }

    public void ready(View view){
        Intent intent = new Intent(GetRole.this, GameActivity.class);
        startActivity(intent);
    }
}
