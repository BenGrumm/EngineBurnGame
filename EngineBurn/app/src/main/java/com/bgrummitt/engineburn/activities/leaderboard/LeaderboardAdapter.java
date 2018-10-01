package com.bgrummitt.engineburn.activities.leaderboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bgrummitt.engineburn.R;
import com.bgrummitt.engineburn.activities.character_select.CharacterAdapter;
import com.bgrummitt.engineburn.controller.leaderboard.UserScore;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    final static private String TAG = CharacterAdapter.class.getSimpleName();

    private Context mContext;
    private UserScore[] mUserScore;

    public LeaderboardAdapter(Context context, UserScore[] userScore){
        mContext = context;
        mUserScore = userScore;
    }

    public class LeaderboardViewHolder extends RecyclerView.ViewHolder{

        public TextView position;
        public TextView userName;
        public TextView userScore;

        public LeaderboardViewHolder(View viewItem){
            super(viewItem);

            position = itemView.findViewById(R.id.positionLabel);
            userName = itemView.findViewById(R.id.scoreHolderNameLabel);
            userScore = itemView.findViewById(R.id.scoreLabel);

        }

        public void BindUser(UserScore user){
            position.setText(user.getPosition());
            userName.setText(user.getName());
            userScore.setText(user.getScore());
        }

    }

    @NonNull
    @Override
    public LeaderboardAdapter.LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.position_list_item, parent, false);
        return new LeaderboardAdapter.LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        holder.BindUser(mUserScore[position]);
    }

    @Override
    public int getItemCount() {
        return mUserScore.length;
    }

}
