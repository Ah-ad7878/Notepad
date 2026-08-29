package pk.org.cas.notepad;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.google.mlkit.vision.text.Text;
import android.provider.MediaStore;
import android.Manifest;
import android.net.Uri;
import androidx.core.content.FileProvider;
import android.os.Environment;
import java.io.File;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class dataenter extends AppCompatActivity {

    ImageButton back_btn,ocr_scanner;
    Button save_btn;
    View colorWhite, colorRed, colorYellow, colorPink, colorGreen, colorBlue, colorPurple, colorOrange, colorGranite, colorAqua, colorBrown;
    View mainLayout;
    TextView currentDateTv;
    EditText titleEt, contentEt;

    private int selectedColor;
    private DatabaseReference databaseReference;
    private TextRecognizer textRecognizer;
    private Uri imageUri;

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (imageUri != null) {
                        processImageToText(imageUri);
                    }
                }
            }
    );

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Camera permission is required to scan text", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataenter);

        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Notes");
        } else {
            databaseReference = FirebaseDatabase.getInstance().getReference("Notes");
        }

        mainLayout = findViewById(R.id.main_layout);
        back_btn = findViewById(R.id.back_btn);
        save_btn = findViewById(R.id.save_btn);
        currentDateTv = findViewById(R.id.current_date_tv);
        titleEt = findViewById(R.id.note_title_et);
        contentEt = findViewById(R.id.note_content_et);
        ocr_scanner = findViewById(R.id.ocr_scanner);

        colorWhite = findViewById(R.id.color_white);
        colorRed = findViewById(R.id.color_red);
        colorYellow = findViewById(R.id.color_yellow);
        colorPink = findViewById(R.id.color_pink);
        colorGreen = findViewById(R.id.color_green);
        colorBlue = findViewById(R.id.color_blue);
        colorPurple = findViewById(R.id.color_purple);
        colorOrange = findViewById(R.id.color_orange);
        colorGranite = findViewById(R.id.color_granite);
        colorAqua = findViewById(R.id.color_Aqua);
        colorBrown = findViewById(R.id.color_brown);

        selectedColor = Color.WHITE;

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        ocr_scanner.setOnClickListener(v -> requestPermissionLauncher.launch(Manifest.permission.CAMERA));


        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy h:mm a", Locale.getDefault());
        currentDateTv.setText(sdf.format(new Date()));

        back_btn.setOnClickListener(view -> finish());

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
        colorGranite.setOnClickListener(view -> {
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


        save_btn.setOnClickListener(v -> {
            String title = titleEt.getText().toString().trim();
            String content = contentEt.getText().toString().trim();

            if (title.isEmpty()) {
                Toast.makeText(dataenter.this, "Please enter a title", Toast.LENGTH_SHORT).show();
                return;
            }

            long time = System.currentTimeMillis();
            Intent intent = new Intent();
            intent.putExtra("title", title);
            intent.putExtra("description", content);
            intent.putExtra("color", selectedColor);
            intent.putExtra("time", time);

            // Save to Firebase
            String id = databaseReference.push().getKey();
            Notes note = new Notes(title, content, selectedColor, time);
            if (id != null) {
                note.setId(id);
                databaseReference.child(id).setValue(note)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(dataenter.this, "Note saved", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK, intent);
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(dataenter.this, "Failed to save: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error creating file", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName() + ".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                cameraLauncher.launch(takePictureIntent);
            }
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private void processImageToText(Uri uri) {
        try {
            InputImage image = InputImage.fromFilePath(this, uri);
            textRecognizer.process(image)
                    .addOnSuccessListener(visionText -> {
                        StringBuilder sb = new StringBuilder();
                        for (Text.TextBlock block : visionText.getTextBlocks()) {
                            String blockText = block.getText();
                            String lowerText = blockText.toLowerCase();
                            if (!lowerText.contains("galaxy") && !lowerText.contains("shot with")) {
                                sb.append(blockText).append(" ");
                            }
                        }

                        String resultText = sb.toString().trim();
                        if (!resultText.isEmpty()) {
                            contentEt.append("\n" + resultText);
                        } else {
                            Toast.makeText(this, "No text detected", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to recognize text: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } catch (IOException e) {
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }
}
