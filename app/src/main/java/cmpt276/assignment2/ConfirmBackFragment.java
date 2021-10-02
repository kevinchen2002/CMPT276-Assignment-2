package cmpt276.assignment2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

/**
 * ConfirmBackFragment is used to confirm whether the user wants to exit without saving.
 * It handles the Android back button and the back button on the AppBar.
 */
public class ConfirmBackFragment extends AppCompatDialogFragment {
    /**
     * This opens a dialog prompting the user to confirm their action.
     * The GameActivity finishes if the user presses "Ok"
     * @param savedInstanceState default argument given by Android.
     * @return the dialog box.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.confirm_back_prompt, null);

        DialogInterface.OnClickListener listener = (dialog, which) -> {
            switch(which) {
                case DialogInterface.BUTTON_POSITIVE:
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
