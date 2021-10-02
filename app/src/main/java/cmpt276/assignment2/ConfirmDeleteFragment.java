package cmpt276.assignment2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ConfirmDeleteFragment extends AppCompatDialogFragment {
    SharedPreferences sp;
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
            Log.i("TAG", "You clicked the dialog button");

        };
        return new AlertDialog.Builder(getActivity())
                .setTitle("Are you sure?")
                .setView(v)
                .setPositiveButton(android.R.string.ok, listener)
                .setNegativeButton(android.R.string.cancel, listener)
                .create();
    }
}
