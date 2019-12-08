package com.example.madgenius;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.ScoreViewHolder> {
    class ScoreViewHolder extends RecyclerView.ViewHolder {
        private final TextView scoreItemView;

        private ScoreViewHolder(View itemView) {
            super(itemView);
            scoreItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Score> mScores; // Cached copy of scores

    ScoreListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ScoreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ScoreViewHolder holder, int position) {
        if (mScores != null) {
            Score current = mScores.get(position);
            holder.scoreItemView.setText(current.getUsername() + " " + current.getPoints() + " " + (current.getGameType() ? "Memory" : "Agility"));
        } else {
            // Covers the case of data not being ready yet.
            holder.scoreItemView.setText("No Score");
        }
    }

    void setScores(List<Score> scores){
        mScores = scores;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mScores has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mScores != null)
            return mScores.size();
        else return 0;
    }
}
