package pk.org.cas.notepad;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class forget_password extends AppCompatActivity {

    EditText email_et;
    Button reset_btn;
    TextView back_to_login_tv;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        email_et = findViewById(R.id.forget_email_et);
        reset_btn = findViewById(R.id.reset_btn);
        back_to_login_tv = findViewById(R.id.back_to_login_tv);

        reset_btn.setOnClickListener(v -> {
            String email = email_et.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(forget_password.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(forget_password.this, "Reset link sent to your email check message in spam folder in case you don't see it", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            String errorMsg = "Failed to send reset email";
                            if (task.getException() != null) {
                                errorMsg += ": " + task.getException().getMessage();
                            }
                            Toast.makeText(forget_password.this, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    });
        });

        back_to_login_tv.setOnClickListener(v -> finish());
    }
}