
package pk.org.cas.notepad;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class datashow extends AppCompatActivity {

    private static final String TAG = "datashow";

    private static final String DATABASE_URL =
            "https://notepad-21952-default-rtdb.asia-southeast1.firebasedatabase.app";

    ImageButton show_back_btn, share_btn;
    Button update_btn;
    EditText show_note_title_tv, show_note_content_tv;
    TextView show_note_date_tv;

    View mainLayout;

    View colorWhite, colorRed, colorYellow, colorPink;
    View colorGreen, colorBlue, colorPurple, colorOrange;
    View colorGranite, colorAqua, colorBrown;

    DatabaseReference databaseReference;
    String noteId;
    int selectedColor;

    private FirebaseAuth firebaseAuth;

    private Bitmap createBitmapFromCustomLayout(
            String title,
            String content,
            String date,
            int color
    ) {
        @SuppressLint("InflateParams")
        View shareView = getLayoutInflater().inflate(
                R.layout.image_card, null
        );

        TextView tvTitle =
                shareView.findViewById(R.id.share_title_tv);

        TextView tvDate =
                shareView.findViewById(R.id.share_date_tv);

        TextView tvContent =
                shareView.findViewById(R.id.share_content_tv);

        LinearLayout bgLayout =
                shareView.findViewById(R.id.card_root);

        tvTitle.setText(title);
        tvDate.setText(date);
        tvContent.setText(content);

        bgLayout.setBackgroundColor(
                color != 0 ? color : Color.WHITE
        );

        int width = (int) (360 * getResources()
                .getDisplayMetrics().density);

        shareView.measure(
                View.MeasureSpec.makeMeasureSpec(
                        width, View.MeasureSpec.EXACTLY
                ),
                View.MeasureSpec.makeMeasureSpec(
                        0, View.MeasureSpec.UNSPECIFIED
                )
        );

        int measuredWidth = shareView.getMeasuredWidth();
        int measuredHeight = shareView.getMeasuredHeight();

        if (measuredWidth <= 0 || measuredHeight <= 0) {
            throw new IllegalStateException(
                    "Unable to measure note image layout"
            );
        }

        shareView.layout(
                0, 0, measuredWidth, measuredHeight
        );

        Bitmap bitmap = Bitmap.createBitmap(
                measuredWidth,
                measuredHeight,
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(bitmap);
        shareView.draw(canvas);

        return bitmap;
    }

    @SuppressLint("ThreadConstraint")
    public void shareNoteAsImage() {

        String title =
                show_note_title_tv.getText().toString();

        String content =
                show_note_content_tv.getText().toString();

        String date =
                show_note_date_tv.getText().toString();

        Bitmap bitmap = null;

        try {
            bitmap = createBitmapFromCustomLayout(
                    title, content, date, selectedColor
            );

            File cachePath = new File(
                    getCacheDir(), "images"
            );

            if (!cachePath.exists() && !cachePath.mkdirs()) {
                throw new IOException(
                        "Unable to create image cache directory"
                );
            }

            File filePath = new File(
                    cachePath, "note_card.png"
            );

            FileOutputStream stream =
                    new FileOutputStream(filePath);
            try {

                boolean compressed = bitmap.compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        stream
                );

                if (!compressed) {
                    throw new IOException(
                            "Unable to save note image"
                    );
                }

                stream.flush();
            } finally {
                stream.close();
            }

            Uri imageUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    filePath
            );

            Intent shareIntent =
                    new Intent(Intent.ACTION_SEND);

            shareIntent.setType("image/png");

            shareIntent.putExtra(
                    Intent.EXTRA_STREAM, imageUri
            );

            shareIntent.addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

            shareIntent.setPackage("com.whatsapp");

            startActivity(shareIntent);

        } catch (ActivityNotFoundException e) {

            Toast.makeText(
                    this,
                    "WhatsApp is not installed",
                    Toast.LENGTH_SHORT
            ).show();

        } catch (IOException | IllegalArgumentException |
                 IllegalStateException e) {

            Log.e(TAG, "Image sharing failed", e);

            Toast.makeText(
                    this,
                    "Picture not shared: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

        } finally {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datashow);

        firebaseAuth = FirebaseAuth.getInstance();

        mainLayout = findViewById(R.id.show_main_layout);

        show_back_btn = findViewById(R.id.show_back_btn);
        update_btn = findViewById(R.id.update_btn);

        show_note_title_tv =
                findViewById(R.id.show_note_title_tv);

        show_note_date_tv =
                findViewById(R.id.show_note_date_tv);

        show_note_content_tv =
                findViewById(R.id.show_note_content_tv);

        share_btn = findViewById(R.id.share_btn);

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

        Intent intent = getIntent();

        if (intent != null) {

            noteId = intent.getStringExtra("id");

            String title =
                    intent.getStringExtra("title");

            String content =
                    intent.getStringExtra("content");

            long dateMillis = intent.getLongExtra(
                    "date", System.currentTimeMillis()
            );

            selectedColor = intent.getIntExtra(
                    "color", 0
            );

            show_note_title_tv.setText(
                    title != null ? title : ""
            );

            show_note_content_tv.setText(
                    content != null ? content : ""
            );

            SimpleDateFormat sdf = new SimpleDateFormat(
                    "MMMM dd, yyyy h:mm a",
                    Locale.getDefault()
            );

            show_note_date_tv.setText(
                    sdf.format(new Date(dateMillis))
            );

            mainLayout.setBackgroundColor(
                    selectedColor != 0
                            ? selectedColor
                            : Color.WHITE
            );
        }

        show_back_btn.setOnClickListener(
                view -> finish()
        );

        share_btn.setOnClickListener(
                view -> shareNoteAsImage()
        );

        setupColorButtons();

        update_btn.setOnClickListener(view -> updateNote());
    }

    private void setupColorButtons() {

        colorWhite.setOnClickListener(v ->
                changeColor(R.color.white));

        colorRed.setOnClickListener(v ->
                changeColor(R.color.note_red));

        colorYellow.setOnClickListener(v ->
                changeColor(R.color.note_yellow));

        colorPink.setOnClickListener(v ->
                changeColor(R.color.note_pink));

        colorGreen.setOnClickListener(v ->
                changeColor(R.color.note_green));

        colorBlue.setOnClickListener(v ->
                changeColor(R.color.note_blue));

        colorPurple.setOnClickListener(v ->
                changeColor(R.color.note_purple));

        colorOrange.setOnClickListener(v ->
                changeColor(R.color.note_orange));

        colorGranite.setOnClickListener(v ->
                changeColor(R.color.granite));

        colorAqua.setOnClickListener(v ->
                changeColor(R.color.Aqua));

        colorBrown.setOnClickListener(v ->
                changeColor(R.color.brown));
    }

    private void changeColor(int colorResource) {

        selectedColor = ContextCompat.getColor(
                this, colorResource
        );

        mainLayout.setBackgroundColor(selectedColor);
    }

    private void updateNote() {

        String updatedTitle =
                show_note_title_tv.getText()
                        .toString().trim();

        String updatedContent =
                show_note_content_tv.getText()
                        .toString().trim();

        if (updatedTitle.isEmpty()) {

            show_note_title_tv.setError(
                    "Title cannot be empty"
            );

            show_note_title_tv.requestFocus();
            return;
        }

        if (noteId == null || noteId.trim().isEmpty()) {

            Toast.makeText(
                    this,
                    "Error: Note ID not found",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String currentUid = firebaseAuth.getUid();

        if (currentUid == null) {

            Toast.makeText(
                    this,
                    "Please login again to update your note",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        update_btn.setEnabled(false);

        // Database structure: notes / UID / noteId
        databaseReference = FirebaseDatabase
                .getInstance(DATABASE_URL)
                .getReference("notes")
                .child(currentUid)
                .child(noteId);

        Map<String, Object> updates = new HashMap<>();

        updates.put("title", updatedTitle);
        updates.put("description", updatedContent);
        updates.put("colour", selectedColor);

        databaseReference.updateChildren(updates)
                .addOnCompleteListener(task -> {

                    update_btn.setEnabled(true);

                    if (task.isSuccessful()) {

                        Toast.makeText(
                                datashow.this,
                                "Note updated successfully",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();

                    } else {

                        Exception exception =
                                task.getException();

                        Log.e(
                                TAG,
                                "Firebase note update failed",
                                exception
                        );

                        String errorMessage =
                                exception != null
                                        ? exception.getMessage()
                                        : "Unknown Firebase error";

                        Toast.makeText(
                                datashow.this,
                                "Update failed: " + errorMessage,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}
