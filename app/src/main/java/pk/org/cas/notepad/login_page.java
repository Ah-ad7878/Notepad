package pk.org.cas.notepad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

public class login_page extends AppCompatActivity {

    EditText email_et, password_et;
    Button login_btn;
    TextView signup_tv;
    FirebaseAuth mAuth;
    CheckBox remember_me;

    ImageButton call_btn, biometric_btn;

    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Executor executor;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "login_pref";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASS = "password";
    private static final String KEY_REMEMBER = "is_remembered";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();

        email_et = findViewById(R.id.login_email_et);
        password_et = findViewById(R.id.login_password_et);
        login_btn = findViewById(R.id.login_btn);
        signup_tv = findViewById(R.id.go_to_signup_tv);
        remember_me = findViewById(R.id.remember_me);
        call_btn = findViewById(R.id.call_btn_login);
        biometric_btn = findViewById(R.id.biometric_login_btn);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setupBiometric();

        if (sharedPreferences.getBoolean(KEY_REMEMBER, false)) {
            email_et.setText(sharedPreferences.getString(KEY_EMAIL, ""));
            password_et.setText(sharedPreferences.getString(KEY_PASS, ""));
            remember_me.setChecked(true);
        }
        
        checkBiometricCapability();

        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String no = "+923356945429";
                String message = "Aslam o Alikum i have a problem with my app";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://wa.me/" + no + "?text=" + Uri.encode(message)));
                startActivity(intent);
            }
        });

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
                                if (remember_me.isChecked()) {
                                    editor.putString(KEY_EMAIL, email);
                                    editor.putString(KEY_PASS, password);
                                    editor.putBoolean(KEY_REMEMBER, true);
                                } else {
                                    editor.clear();
                                }
                                editor.apply();

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

        biometric_btn.setOnClickListener(v -> biometricPrompt.authenticate(promptInfo));

    }

    private void setupBiometric() {
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(login_page.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(login_page.this, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                loginWithSavedCredentials();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(login_page.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();
    }

    private void checkBiometricCapability() {
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                biometric_btn.setVisibility(View.VISIBLE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                biometric_btn.setVisibility(View.GONE);
                break;
        }
    }

    private void loginWithSavedCredentials() {
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String password = sharedPreferences.getString(KEY_PASS, "");

        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(login_page.this, "Login Successful via Biometric", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(login_page.this, nextpage.class));
                            finish();
                        } else {
                            Toast.makeText(login_page.this, "Login failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Please login with password first to enable biometric", Toast.LENGTH_SHORT).show();
        }
    }
}
