package pk.org.cas.notepad;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class nextpage extends AppCompatActivity {

    RecyclerView notes_rv;
    ExtendedFloatingActionButton add_note_fab;

    SearchView search_et;
    List<Notes> notesList;
    Adaptor adaptor;


    private final ActivityResultLauncher<Intent> addNoteLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    String title = data.getStringExtra("title");
                    String description = data.getStringExtra("description");
                    int color = data.getIntExtra("color", Color.WHITE);
                    long time = data.getLongExtra("time", System.currentTimeMillis());

                    Notes newNote = new Notes(title, description, color, time);
                    notesList.add(0, newNote);

                    adaptor.notifyItemInserted(0);
                    notes_rv.scrollToPosition(0);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nextpage);

        add_note_fab = findViewById(R.id.add_note_fab);
        notes_rv = findViewById(R.id.notes_rv);
        search_et = findViewById(R.id.search_et);

        notesList = new ArrayList<>();
        adaptor = new Adaptor(notesList);

        notes_rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        notes_rv.setAdapter(adaptor);

        add_note_fab.setOnClickListener(v -> {
            Intent intent = new Intent(nextpage.this, dataenter.class);
            addNoteLauncher.launch(intent);
        });

        search_et.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String text) {
        List<Notes> filteredList = new ArrayList<>();
        for (Notes note : notesList) {
            if (note.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                    note.getDescription().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(note);
            }
        }
        adaptor.filterList(filteredList);
    }
}
