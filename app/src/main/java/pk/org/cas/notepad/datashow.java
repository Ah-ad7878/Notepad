package pk.org.cas.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class datashow extends AppCompatActivity {

    ImageButton show_back_btn;
    TextView show_note_title_tv, show_note_date_tv, show_note_content_tv;
    View mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datashow);

        mainLayout = findViewById(R.id.show_main_layout);
        show_back_btn = findViewById(R.id.show_back_btn);
        show_note_title_tv = findViewById(R.id.show_note_title_tv);
        show_note_date_tv = findViewById(R.id.show_note_date_tv);
        show_note_content_tv = findViewById(R.id.show_note_content_tv);

        show_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            long dateMillis = intent.getLongExtra("date", System.currentTimeMillis());
            int color = intent.getIntExtra("color", 0);

            show_note_title_tv.setText(title);
            show_note_content_tv.setText(content);


            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy h:mm a", Locale.getDefault());
            show_note_date_tv.setText(sdf.format(new Date(dateMillis)));


            if (color != 0) {
                mainLayout.setBackgroundColor(color);
            }
        }
    }
}
