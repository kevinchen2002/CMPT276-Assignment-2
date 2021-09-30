package cmpt276.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.cmpt276.as2.model.Game;
import ca.cmpt276.as2.model.GameManager;
import ca.cmpt276.as2.model.PlayerScore;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().setTitle("Add New Game");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        endActivityButton();

    }

    private void endActivityButton() {
        Button btn = (Button) findViewById(R.id.end_add_game);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterCards();
                finish();
            }
        });
    }

    private void enterCards() {
        try {
            EditText p1TextEntry = (EditText) findViewById(R.id.p1NumCards);
            String p1Input = p1TextEntry.getText().toString();
            int p1NumCards = Integer.parseInt(p1Input);

            EditText p2TextEntry = (EditText) findViewById(R.id.p2NumCards);
            String p2Input = p2TextEntry.getText().toString();
            int p2NumCards = Integer.parseInt(p2Input);

            GameManager test = GameManager.getInstance();
            Game testGame = new Game();
            testGame.addPlayer(new PlayerScore(1, 3,5,6));
            testGame.addPlayer(new PlayerScore(2, 3,5,6));
            test.addGame(testGame);

            Log.i("DemoInitialApp", "Player 1: " + p1NumCards);
            Log.i("DemoInitialApp", "Player 2: " + p2NumCards);
        }
        catch (NumberFormatException nfe) {
           //Log.i("uh oh", "bad");
            Toast.makeText(getApplicationContext(), "Invalid input - fill in all fields", Toast.LENGTH_SHORT).show();
        }

    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, GameActivity.class);
    }
}