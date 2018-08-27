package com.bgrummitt.engineburn.activities.character_select;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bgrummitt.engineburn.R;

import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {

    final static private String TAG = CharacterAdapter.class.getSimpleName();

    private Context mContext;
    private Characters[] mCharacterList;

    public CharacterAdapter(Context context, Characters[] characters){
        mContext = context;
        mCharacterList = characters;

        CharacterSelectionActivity CharacterActivity = (CharacterSelectionActivity) context;
    }

    public class CharacterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public String name;
        public ImageView mCharacterImage;

        public CharacterViewHolder(View itemView){
            super(itemView);

            mCharacterImage = itemView.findViewById(R.id.characterImageView);

            itemView.setOnClickListener(this);
        }

        public void BindCharacter(Characters character){
            name = character.getCharacterName();
            mCharacterImage.setImageResource(character.getIDFullFire());
        }

        @Override
        public void onClick(View v) {
            Log.v(TAG, "Clicked " + name);
        }
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.character_list_item, parent, false);
        CharacterViewHolder viewHolder = new CharacterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, int position) {
        holder.BindCharacter(mCharacterList[position]);
    }

    @Override
    public int getItemCount() {
        return mCharacterList.length;
    }

}
