package pk.org.cas.notepad;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Adaptor extends RecyclerView.Adapter<Adaptor.ProductViewHolder>{

    private List<Notes> notesList;

    public Adaptor(List<Notes> notesList){
        this.notesList = notesList;
    }

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
        
        holder.note_title_tv.setText(notes.getTitle());
        holder.note_desc_tv.setText(notes.getDescription());

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String dateString = sdf.format(new Date(notes.getTime()));
        holder.note_date_tv.setText(dateString);

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, datashow.class);
            intent.putExtra("title", notes.getTitle());
            intent.putExtra("content", notes.getDescription());
            intent.putExtra("date", notes.getTime());
            intent.putExtra("color", notes.getColour());
            context.startActivity(intent);
        });

        // Correctly set the card background color
        if (notes.getColour() != 0 && notes.getColour() != -1) {
            holder.note_card.setCardBackgroundColor(notes.getColour());
        } else {
            holder.note_card.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
        }
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
