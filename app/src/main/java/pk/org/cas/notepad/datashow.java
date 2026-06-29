package pk.org.cas.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class datashow extends AppCompatActivity {

    ImageButton show_back_btn;
    Button update_btn;
    EditText show_note_title_tv, show_note_content_tv;
    TextView show_note_date_tv;
    View mainLayout;
    View colorWhite, colorRed, colorYellow, colorPink, colorGreen, colorBlue, colorPurple, colorOrange, colorGranite, colorAqua, colorBrown;
    DatabaseReference databaseReference;
    String noteId;
    int selectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datashow);

        mainLayout = findViewById(R.id.show_main_layout);
        show_back_btn = findViewById(R.id.show_back_btn);
        update_btn = findViewById(R.id.update_btn);
        show_note_title_tv = findViewById(R.id.show_note_title_tv);
        show_note_date_tv = findViewById(R.id.show_note_date_tv);
        show_note_content_tv = findViewById(R.id.show_note_content_tv);

        colorWhite = findViewById(R.id.show_color_white);
        colorRed = findViewById(R.id.show_color_red);
        colorYellow = findViewById(R.id.show_color_yellow);
        colorPink = findViewById(R.id.show_color_pink);
        colorGreen = findViewById(R.id.show_color_green);
        colorBlue = findViewById(R.id.show_color_blue);
        colorPurple = findViewById(R.id.show_color_purple);
        colorOrange = findViewById(R.id.show_color_orange);
        colorGranite = findViewById(R.id.show_color_granite);
        colorAqua = findViewById(R.id.show_color_Aqua);
        colorBrown = findViewById(R.id.show_color_Brown);

        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Notes");
        }

        show_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            noteId = intent.getStringExtra("id");
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            long dateMillis = intent.getLongExtra("date", System.currentTimeMillis());
            selectedColor = intent.getIntExtra("color", 0);

            show_note_title_tv.setText(title);
            show_note_content_tv.setText(content);

            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy h:mm a", Locale.getDefault());
            show_note_date_tv.setText(sdf.format(new Date(dateMillis)));

            if (selectedColor != 0) {
                mainLayout.setBackgroundColor(selectedColor);
            }
        }

        colorWhite.setOnClickListener(v -> {
            selectedColor = ContextCompat.getColor(this, R.color.white);
            mainLayout.setBackgroundColor(selectedColor);
        });
        colorRed.setOnClickListener(v -> {
            selectedColor = ContextCompat.getColor(this, R.color.note_red);
            mainLayout.setBackgroundColor(selectedColor);
        });
        colorYellow.setOnClickListener(v -> {
            selectedColor = ContextCompat.getColor(this, R.color.note_yellow);
            mainLayout.setBackgroundColor(selectedColor);
        });
        colorPink.setOnClickListener(v -> {
            selectedColor = ContextCompat.getColor(this, R.color.note_pink);
            mainLayout.setBackgroundColor(selectedColor);
        });
        colorGreen.setOnClickListener(v -> {
            selectedColor = ContextCompat.getColor(this, R.color.note_green);
            mainLayout.setBackgroundColor(selectedColor);
        });
        colorBlue.setOnClickListener(v -> {
            selectedColor = ContextCompat.getColor(this, R.color.note_blue);
            mainLayout.setBackgroundColor(selectedColor);
        });
        colorPurple.setOnClickListener(v -> {
            selectedColor = ContextCompat.getColor(this, R.color.note_purple);
            mainLayout.setBackgroundColor(selectedColor);
        });
        colorOrange.setOnClickListener(v -> {
            selectedColor = ContextCompat.getColor(this, R.color.note_orange);
            mainLayout.setBackgroundColor(selectedColor);
        });
        colorGranite.setOnClickListener(v -> {
            selectedColor = ContextCompat.getColor(this, R.color.granite);
            mainLayout.setBackgroundColor(selectedColor);
        });
        colorAqua.setOnClickListener(view -> {
            selectedColor = ContextCompat.getColor(this, R.color.Aqua);
            mainLayout.setBackgroundColor(selectedColor);
        });
        colorBrown.setOnClickListener(view -> {
            selectedColor = ContextCompat.getColor(this, R.color.brown);
            mainLayout.setBackgroundColor(selectedColor);
        });

        update_btn.setOnClickListener(v -> {
            String updatedTitle = show_note_title_tv.getText().toString().trim();
            String updatedContent = show_note_content_tv.getText().toString().trim();

            if (updatedTitle.isEmpty()) {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (noteId != null && databaseReference != null) {
                databaseReference.child(noteId).child("title").setValue(updatedTitle);
                databaseReference.child(noteId).child("description").setValue(updatedContent);
                databaseReference.child(noteId).child("colour").setValue(selectedColor)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(datashow.this, "Note updated", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(datashow.this, "Update failed", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
