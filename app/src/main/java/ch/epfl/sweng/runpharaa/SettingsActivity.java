package ch.epfl.sweng.runpharaa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.runpharaa.login.LoginActivity;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.sign_out_button).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_out_button:
                signOut();
                break;
            default:
                throw new AssertionError("This button does not exists.");
        }
    }

    private void signOut(){

        FirebaseAuth.getInstance().signOut();
        Intent login = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(login);

        //TODO: handle the google account

        /**mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent login = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(login);
                    }
                });**/


    }
}
