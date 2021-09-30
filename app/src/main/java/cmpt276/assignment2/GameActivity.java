package cmpt276.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;

import ca.cmpt276.as2.model.Game;
import ca.cmpt276.as2.model.GameManager;
import ca.cmpt276.as2.model.PlayerScore;

public class GameActivity extends AppCompatActivity {

    private LocalDateTime gameCreationDateTime = LocalDateTime.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().setTitle("Add New Game");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView dateCreateText = (TextView) findViewById(R.id.dateCreateText);
        dateCreateText.setText(gameCreationDateTime.toString());

        endActivityButton();

        Intent intent = getIntent();
        int p1NumCards = intent.getIntExtra("p1NumCards", 0);


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
            Game testGame = new Game(gameCreationDateTime);
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