package cmpt276.assignment2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ConfirmBackFragment extends AppCompatDialogFragment {
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
