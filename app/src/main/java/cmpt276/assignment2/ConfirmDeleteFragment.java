package cmpt276.assignment2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

/**
 * ConfirmDeleteFragment is used to confirm whether the user wants to delete a selected game.
 * It is called by the delete button, which appears if the user has selected a game.
 */
public class ConfirmDeleteFragment extends AppCompatDialogFragment {
    SharedPreferences sp;

    /**
     * This opens a dialog prompting the user to confirm their action.
     * Should the user confirm the deletion, a SharedPreference is used to retrieve the index.
     * The deleteGame method is then called from GameActivity to remove the selected game.
     * @param savedInstanceState default argument given by Android.
     * @return the dialog box.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.confirm_delete_prompt, null);

        DialogInterface.OnClickListener listener = (dialog, which) -> {
           sp = requireActivity().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
            switch(which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //method to delete
                    int gameIndex = sp.getInt("currentGame", -1);
                    if (gameIndex != -1) {
                        ((GameActivity) requireActivity()).deleteGame(gameIndex);
                    }
                    Toast.makeText(requireActivity().getApplicationContext(), "Game deleted successfully!", Toast.LENGTH_SHORT).show();
                    requireActivity().finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    //don't do anything, just end
                    break;
            }
        };
        return new AlertDialog.Builder(getActivity())
                .setTitle("Are you sure?")
                .setView(v)
                .setPositiveButton(android.R.string.ok, listener)
                .setNegativeButton(android.R.string.cancel, listener)
                .create();
    }
}
