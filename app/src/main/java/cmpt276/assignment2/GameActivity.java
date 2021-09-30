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

        Intent intent = getIntent();
        int gameIndex = intent.getIntExtra("gamePosition", -1);
        int p1NumCards = intent.getIntExtra("p1NumCards", 0);
        int p2NumCards = intent.getIntExtra("p2NumCards", 0);
        int p1Sum = intent.getIntExtra("p1Sum", 0);
        int p2Sum = intent.getIntExtra("p2Sum", 0);
        int p1Wagers = intent.getIntExtra("p1Wagers", 0);
        int p2Wagers = intent.getIntExtra("p2Wagers", 0);

        EditText p1TextEntry = (EditText) findViewById(R.id.p1NumCards);
        p1TextEntry.setText(""+p1NumCards);
        p1TextEntry = (EditText) findViewById(R.id.p1Sum);
        p1TextEntry.setText(""+p1Sum);
        p1TextEntry = (EditText) findViewById(R.id.p1Wagers);
        p1TextEntry.setText(""+p1Wagers);

        EditText p2TextEntry = (EditText) findViewById(R.id.p2NumCards);
        p2TextEntry.setText(""+p2NumCards);
        p2TextEntry = (EditText) findViewById(R.id.p2Sum);
        p2TextEntry.setText(""+p2Sum);
        p2TextEntry = (EditText) findViewById(R.id.p2Wagers);
        p2TextEntry.setText(""+p2Wagers);

        if (gameIndex != -1) {
            getSupportActionBar().setTitle("Edit Game");
        }

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

            p1TextEntry = (EditText) findViewById(R.id.p1Sum);
            p1Input = p1TextEntry.getText().toString();
            int p1Sum = Integer.parseInt(p1Input);

            p2TextEntry = (EditText) findViewById(R.id.p2Sum);
            p2Input = p2TextEntry.getText().toString();
            int p2Sum = Integer.parseInt(p2Input);

            p1TextEntry = (EditText) findViewById(R.id.p1Wagers);
            p1Input = p1TextEntry.getText().toString();
            int p1Wagers = Integer.parseInt(p1Input);

            p2TextEntry = (EditText) findViewById(R.id.p2Wagers);
            p2Input = p2TextEntry.getText().toString();
            int p2Wagers = Integer.parseInt(p2Input);

            GameManager test = GameManager.getInstance();
            Game testGame = new Game();
            testGame.addPlayer(new PlayerScore(1, p1NumCards, p1Sum, p1Wagers));
            testGame.addPlayer(new PlayerScore(2, p2NumCards, p2Sum, p2Wagers));
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