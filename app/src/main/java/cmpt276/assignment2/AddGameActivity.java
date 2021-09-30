package cmpt276.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AddGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddGameActivity.class);
    }
}