package com.example.ofrisproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CostomerAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        public final TextView artistName;
        public final TextView songName;
        public final ConstraintLayout mainRow;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this::select);
            view.setOnLongClickListener(this);
            artistName = view.findViewById(R.id.artistName);
            songName = view.findViewById(R.id.songName);
            mainRow = view.findViewById(R.id.mainRow);
        }

        public void select(View v){
            Toast.makeText(itemView.getContext(), "Click "+ getAdapterPosition(), Toast.LENGTH_SHORT).show();
        }


        @Override
        public boolean onLongClick(View view) {
            localDataSet.remove(getAdapterPosition());
            notifyDataSetChanged();
            return true;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.rc_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder,  int position)
    {
        viewHolder.artistName.setText(localDataSet.get(position).getArtistName());
        viewHolder.songName.setText(""+localDataSet.get(position).getSongName());
        if(position%2==0)
            viewHolder.mainRow.setBackgroundColor(Color.parseColor("#4CAF50"));
        else
            viewHolder.mainRow.setBackgroundColor(Color.parseColor("#FFC8E4A9"));
        viewHolder.getAdapterPosition();
    }
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
