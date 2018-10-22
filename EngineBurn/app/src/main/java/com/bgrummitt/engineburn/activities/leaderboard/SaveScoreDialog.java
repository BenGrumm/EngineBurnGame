package com.bgrummitt.engineburn.activities.leaderboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bgrummitt.engineburn.R;

public class SaveScoreDialog extends DialogFragment {

    private static final String typeKey = "dialogType";

    private String leaderBoardName;

    private TextView promptTextView;
    private EditText nameInput;
    private Button saveButton;

    /**
     * Function to create a new instance with the parameters passed in a bundle
     * @param type the leaderboard type that the score will be saved to
     * @return the new SaveScoreDialog with the parameters passed as bundle
     */
    public static SaveScoreDialog newInstance(String type){
        // Create a new saveScoreDialog
        SaveScoreDialog saveScoreDialog = new SaveScoreDialog();

        // Create a new bundle
        Bundle args = new Bundle();
        // Put the string into the bundle
        args.putString(typeKey, type);

        // Attach the bundle to the SaveScoreDialog
        saveScoreDialog.setArguments(args);

        // Return dialog
        return saveScoreDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Get the arguments
        Bundle bundle = getArguments();

        // If the bundle is not null get the string from it
        if(bundle != null) {
            leaderBoardName = bundle.getString(typeKey);
        }

        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Create a new layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate with the layout file
        View view = inflater.inflate(R.layout.dialog_save_score, null);

        // Set the view to the inflated layout
        builder.setView(view);

        // Retrieve all of the elements in the view
        promptTextView = view.findViewById(R.id.saveScorePromptTextView);
        nameInput = view.findViewById(R.id.nameEditText);
        saveButton = view.findViewById(R.id.nameSaveButton);

        // Set the buttons onClickListener
        setButtonListener();
        // Set the text in the prompt to the R.string.dialog_name_prompt with the
        // leaderboard name replacing the %s in the string
        promptTextView.setText(String.format(getResources().getString(R.string.dialog_name_prompt), leaderBoardName));

        // Create the dialog
        AlertDialog dialog = builder.create();

        // Show the dialog
        dialog.show();

        // Return the dialog
        return dialog;
    }

    /**
     * Function to set the onClick listener of the save button
     */
    public void setButtonListener(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the activity and cast it to LeaderboardActivity and call onDialogClose with the text from the name input
                ((LeaderboardActivity)SaveScoreDialog.this.getActivity()).OnDialogClose(nameInput.getText().toString());
                // Dismiss the dialog
                SaveScoreDialog.this.dismiss();
            }
        });
    }

}
