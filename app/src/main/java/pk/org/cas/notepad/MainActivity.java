package pk.org.cas.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button started_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // If user is already logged in, go to login_page to "Unlock" the app
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, login_page.class));
            finish();
            return;
        }

        started_btn = findViewById(R.id.started_btn);

        started_btn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, signup_page.class)));
    }
}