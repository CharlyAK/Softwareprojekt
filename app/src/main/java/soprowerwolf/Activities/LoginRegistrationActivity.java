package soprowerwolf.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.R;


public class LoginRegistrationActivity extends AppCompatActivity {

    Button bStartLogin, bStartRegistration, bLogin, bRegistration;
    EditText textUsername, textEMail, textPassword;
    databaseCon Con = new databaseCon();

    GlobalVariables globalVariables = GlobalVariables.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registration);

        globalVariables.setOwnPlayerID(0);

        final ImageButton bStartScreen = (ImageButton)findViewById(R.id.imageButtonStart);
        final TextView openGame = (TextView)findViewById(R.id.textOpenGame);

        bStartLogin = (Button)findViewById(R.id.buttonStartLogin);
        bLogin = (Button)findViewById(R.id.buttonLogin);
        bRegistration = (Button)findViewById(R.id.buttonRegistration);
        bStartRegistration = (Button)findViewById(R.id.buttonStartRegistration);
        textEMail = (EditText)findViewById(R.id.TextEMail);
        textPassword = (EditText)findViewById(R.id.TextPassword);
        textUsername = (EditText)findViewById(R.id.TextUsername);

        assert bStartScreen != null;
        bStartScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bStartScreen.setVisibility(View.INVISIBLE);
                bStartScreen.setEnabled(false);
                assert openGame != null;
                openGame.setVisibility(View.INVISIBLE);
                bStartLogin.setVisibility(View.VISIBLE);
                bStartRegistration.setVisibility(View.VISIBLE);
            }
        });

        bStartLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bStartLogin.setVisibility(View.INVISIBLE);
                bStartRegistration.setVisibility(View.INVISIBLE);
                bLogin.setVisibility(View.VISIBLE);
                textEMail.setVisibility(View.VISIBLE);
                textPassword.setVisibility(View.VISIBLE);
            }
        });

        bStartRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bStartLogin.setVisibility(View.INVISIBLE);
                bStartRegistration.setVisibility(View.INVISIBLE);
                bRegistration.setVisibility(View.VISIBLE);
                textUsername.setVisibility(View.VISIBLE);
                textEMail.setVisibility(View.VISIBLE);
                textPassword.setVisibility(View.VISIBLE);

            }
        });

    }

    public void login (View view)
    {
        if(Con.login(textEMail.getText().toString(), textPassword.getText().toString()))
        {
            Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
            textEMail.setVisibility(View.VISIBLE);
            textEMail.setBackgroundColor(Color.RED);
        }
    }

    public void registration (View view)
    {
        //ToDo: Registrierung
        String name = textUsername.getText().toString();
        String email = textEMail.getText().toString();
        String password = textPassword.getText().toString();

        Con.registration(name, email, password);

        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
