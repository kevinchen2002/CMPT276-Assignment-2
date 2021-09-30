package cmpt276.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
        getSupportActionBar().setTitle("Add New Game");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        endActivityButton();
    }

    private void endActivityButton() {
        Button btn = (Button) findViewById(R.id.end_add_game);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddGameActivity.class);
    }
}