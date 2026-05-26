package pk.org.cas.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class nextpage extends AppCompatActivity {

    RecyclerView notes_rv;
    ExtendedFloatingActionButton add_note_fab;
    ImageButton logout_btn;

    SearchView search_et;
    List<Notes> notesList;
    Adaptor adaptor;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nextpage);

        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Notes");
        } else {
            databaseReference = FirebaseDatabase.getInstance().getReference("Notes");
        }

        add_note_fab = findViewById(R.id.add_note_fab);
        notes_rv = findViewById(R.id.notes_rv);
        search_et = findViewById(R.id.search_et);
        logout_btn = findViewById(R.id.logout_btn);

        notesList = new ArrayList<>();
        adaptor = new Adaptor(notesList);

        notes_rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        notes_rv.setAdapter(adaptor);

        loadNotes();

        logout_btn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(nextpage.this, login_page.class));
            finish();
        });

        add_note_fab.setOnClickListener(v -> {
            Intent intent = new Intent(nextpage.this, dataenter.class);
            startActivity(intent);
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

    private void loadNotes() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notesList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Notes note = dataSnapshot.getValue(Notes.class);
                        if (note != null) {
                            note.setId(dataSnapshot.getKey());
                            notesList.add(0, note);
                        }
                    }
                }
                
               

                adaptor.filterList(notesList);
                

                if (!notesList.isEmpty()) {
                    notes_rv.scrollToPosition(0);
                }
                

                android.util.Log.d("Notepad", "Loaded notes count: " + notesList.size());
                android.widget.Toast.makeText(nextpage.this, 
                    "Notes loaded: " + notesList.size(), android.widget.Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                android.widget.Toast.makeText(nextpage.this, 
                    "Database Error: " + error.getMessage(), android.widget.Toast.LENGTH_LONG).show();
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
