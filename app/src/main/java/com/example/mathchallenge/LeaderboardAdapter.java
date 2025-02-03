package com.example.mathchallenge;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<Integer> highScores;
    private List<String> playerNames;

    public LeaderboardAdapter(List<Integer> highScores, List<String> playerNames) {
        this.highScores = highScores;
        this.playerNames = playerNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rankText.setText((position + 4) + "."); // Since this starts at 4th place
        holder.nameText.setText(playerNames.get(position));
        holder.scoreText.setText(String.valueOf(highScores.get(position)));

        // Apply fade-in animation
        holder.itemView.setAlpha(0f);
        holder.itemView.animate().alpha(1f).setDuration(500).start();

        // Reset visibility and styles for default ranks
        holder.rankText.setVisibility(View.VISIBLE);
        holder.medalIcon.setVisibility(View.GONE);
        holder.nameText.setTextColor(Color.BLACK);
        holder.scoreText.setTextColor(Color.BLACK);
    }

    @Override
    public int getItemCount() {
        return highScores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView medalIcon;
        TextView rankText, nameText, scoreText;

        public ViewHolder(View itemView) {
            super(itemView);
            rankText = itemView.findViewById(R.id.rank_text);
            nameText = itemView.findViewById(R.id.name_text);
            scoreText = itemView.findViewById(R.id.score_text);
            medalIcon = itemView.findViewById(R.id.medal_icon);
        }
    }

    public void updateScores(List<Integer> newScores, List<String> newNames) {
        highScores.clear();
        highScores.addAll(newScores);
        playerNames.clear();
        playerNames.addAll(newNames);
        notifyDataSetChanged();
    }
}


