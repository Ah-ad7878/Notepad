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

public class signup_page extends AppCompatActivity {

    EditText name_et, email_et, password_et;
    Button signup_btn;
    TextView login_tv;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        mAuth = FirebaseAuth.getInstance();

        name_et = findViewById(R.id.name_et);
        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        signup_btn = findViewById(R.id.signup_btn);
        login_tv = findViewById(R.id.login_tv);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_et.getText().toString().trim();
                String password = password_et.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(signup_page.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(signup_page.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(signup_page.this, task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(signup_page.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(signup_page.this, nextpage.class));
                                finish();
                            } else {
                                Toast.makeText(signup_page.this, "Authentication failed: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        login_tv.setOnClickListener(v -> {
            startActivity(new Intent(signup_page.this, login_page.class));
            finish();
        });
    }
}
