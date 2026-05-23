package pk.org.cas.notepad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Adaptor extends RecyclerView.Adapter<Adaptor.ProductViewHolder>{

    private List<Notes> notesList;

    public Adaptor(List<Notes> notesList){
        this.notesList = notesList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<Notes> filteredList) {
        this.notesList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_design, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Notes notes = notesList.get(position);
        
        String title = notes.getTitle();
        String desc = notes.getDescription();
        
        holder.note_title_tv.setText(title != null && !title.isEmpty() ? title : "Untitled Note");
        holder.note_desc_tv.setText(desc != null && !desc.isEmpty() ? desc : "No content");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String dateString = sdf.format(new Date(notes.getTime()));
            holder.note_date_tv.setText(dateString);
        } catch (Exception e) {
            holder.note_date_tv.setText("No date");
        }

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, datashow.class);
            intent.putExtra("title", notes.getTitle());
            intent.putExtra("content", notes.getDescription());
            intent.putExtra("date", notes.getTime());
            intent.putExtra("color", notes.getColour());
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(view -> {
            int currentPosition = holder.getBindingAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                Notes noteToDelete = notesList.get(currentPosition);
                String noteId = noteToDelete.getId();

                if (noteId != null) {
                    FirebaseDatabase.getInstance().getReference("Notes")
                            .child(noteId).removeValue()
                            .addOnSuccessListener(aVoid -> Toast.makeText(view.getContext(), "Note Deleted", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(view.getContext(), "Delete Failed", Toast.LENGTH_SHORT).show());
                } else {

                    notesList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                    notifyItemRangeChanged(currentPosition, notesList.size());
                    Toast.makeText(view.getContext(), "Note Deleted Locally", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        });

        int color = notes.getColour();
        if (color != 0 && color != -1) {
            holder.note_card.setCardBackgroundColor(color);
        } else {
            holder.note_card.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
        }
        
        android.util.Log.d("Notepad", "Binding note at " + position + ": " + title);
    }

    @Override
    public int getItemCount() {
        return notesList != null ? notesList.size() : 0;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView note_title_tv, note_desc_tv, note_date_tv;
        MaterialCardView note_card;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            note_title_tv = itemView.findViewById(R.id.note_title_tv);
            note_desc_tv = itemView.findViewById(R.id.note_desc_tv);
            note_date_tv = itemView.findViewById(R.id.note_date_tv);
            note_card = itemView.findViewById(R.id.note_card);
        }
    }
}
