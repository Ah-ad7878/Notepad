package pk.org.cas.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class login_page extends AppCompatActivity {

    EditText email_et, password_et;
    Button login_btn;
    TextView signup_tv;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();

        email_et = findViewById(R.id.login_email_et);
        password_et = findViewById(R.id.login_password_et);
        login_btn = findViewById(R.id.login_btn);
        signup_tv = findViewById(R.id.go_to_signup_tv);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_et.getText().toString().trim();
                String password = password_et.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(login_page.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(login_page.this, task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(login_page.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(login_page.this, nextpage.class));
                                finish();
                            } else {
                                Toast.makeText(login_page.this, "Login failed: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        signup_tv.setOnClickListener(v -> {
            startActivity(new Intent(login_page.this, signup_page.class));
            finish();
        });
    }
}
