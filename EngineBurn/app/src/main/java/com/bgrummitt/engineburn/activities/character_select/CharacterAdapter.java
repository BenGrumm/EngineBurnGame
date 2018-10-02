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
import com.bgrummitt.engineburn.controller.characters.GameCharacter;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {

    final static private String TAG = CharacterAdapter.class.getSimpleName();

    private Context mContext;
    private GameCharacter[] mCharacterList;
    private int mSelectedCharacterPosition = RecyclerView.NO_POSITION;


    public CharacterAdapter(Context context, GameCharacter[] characters){
        mContext = context;
        mCharacterList = characters;
    }

    public class CharacterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public String name;
        public ImageView mCharacterImage;

        public CharacterViewHolder(View itemView){
            super(itemView);

            mCharacterImage = itemView.findViewById(R.id.characterImageView);

            itemView.setOnClickListener(this);
        }

        /**
         * As recycler views will reuse the same view if the list is long enough binding changes the data in
         * the view to the data of the given character
         * @param character the character that will be displayed in the view
         */
        public void BindCharacter(GameCharacter character){
            name = character.getCharacterName();
            mCharacterImage.setImageResource(character.getIDFullFire());
        }

        /**
         * Function called when the recycler view is clicked
         * @param v the view that holds the current recycler view.
         */
        @Override
        public void onClick(View v) {
            Log.v(TAG, "Clicked " + name);
            //Reload the current item that is highlighted and then change the highlighted position to this one and then reload this view
            notifyItemChanged(mSelectedCharacterPosition);
            mSelectedCharacterPosition = getLayoutPosition();
            notifyItemChanged(mSelectedCharacterPosition);
        }

    }

    /**
     * On creation of the view (individual recycler item) inflate the layout and pass it to the new CharacterViewHolder
     * @param parent the recycler view adding to
     * @param viewType I am using the same view type for every layout item so this will always be the same
     * @return new CharacterViewHolder with view initialised
     */
    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.character_list_item, parent, false);
        return new CharacterViewHolder(view);
    }

    /**
     * When the views item changes as they are recycled when out of screen change the data in the view
     * @param holder the view that needs to be changed
     * @param position the position of the new data in the array to change the data to
     */
    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, int position) {
        holder.BindCharacter(mCharacterList[position]);
        //Set this given holder to the selected one and then set the background to a highlighted color
        holder.itemView.setSelected(mSelectedCharacterPosition == position);
        //The colors are a semi transparent white and a fully transparent when not highlighted (44) semi transparent (00) fully transparent
        holder.itemView.setBackgroundColor(mSelectedCharacterPosition == position ? 0x44FFFFFF : 0x00FFFFFF);
    }

    /**
     * Get the number of items in the recycler view
     * @return number of characters in the recycler view
     */
    @Override
    public int getItemCount() {
        return mCharacterList.length;
    }

    /**
     * Get the currently highlighted character
     * @return the highlighted character
     */
    public GameCharacter getSelectedCharacter(){
        //If there hasn't been a character selected return null else return selected character
        if(mSelectedCharacterPosition == RecyclerView.NO_POSITION)
            return null;
        return mCharacterList[mSelectedCharacterPosition];
    }

}
