package ch.epfl.sweng.runpharaa.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ch.epfl.sweng.runpharaa.MainActivity;
import ch.epfl.sweng.runpharaa.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //add listener to the buttons
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.signInBut).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signInBut:
                launchApp();
                break;
        }
    }

    /**
     * Launch the real application after the login
     */
    private void launchApp(){
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);

    }


}
