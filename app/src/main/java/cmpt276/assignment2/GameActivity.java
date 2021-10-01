package cmpt276.assignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

        //TODO: find a way to reduce redundancy in the formatter
        TextView dateCreateText = (TextView) findViewById(R.id.dateCreateText);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDatetime = gameCreationDateTime.format(formatter);
        dateCreateText.setText(formattedDatetime);

        Intent intent = getIntent();
        int gameIndex = intent.getIntExtra("gamePosition", -1);

        setDefaultValue(intent.getIntExtra("p1NumCards", 0), R.id.p1NumCards);
        setDefaultValue(intent.getIntExtra("p2NumCards", 0), R.id.p2NumCards);
        setDefaultValue(intent.getIntExtra("p1Sum", 0), R.id.p1Sum);
        setDefaultValue(intent.getIntExtra("p2Sum", 0), R.id.p2Sum);
        setDefaultValue(intent.getIntExtra("p1Wagers", 0), R.id.p1Wagers);
        setDefaultValue(intent.getIntExtra("p2Wagers", 0), R.id.p2Wagers);


        if (gameIndex != -1) {
            getSupportActionBar().setTitle("Edit Game");
            previewGameResults();
        }

        endActivityButton(gameIndex);

    }

    private void setDefaultValue(int valueToSet, int id) {
        EditText dataEntry = (EditText) this.findViewById(id);
        dataEntry.setText(""+valueToSet);


        dataEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                previewGameResults();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                previewGameResults();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                previewGameResults();
            }
        });
    }

    private void previewGameResults() {
        //FIX: crash due to values being left empty.
        //code that calculates the score

        try {
            int p1Score = PlayerScore.calculatePlayerScore(parseFromTextEntry(R.id.p1NumCards),
                    parseFromTextEntry(R.id.p1Sum),
                    parseFromTextEntry(R.id.p1Wagers));
            int p2Score = PlayerScore.calculatePlayerScore(parseFromTextEntry(R.id.p2NumCards),
                    parseFromTextEntry(R.id.p2Sum),
                    parseFromTextEntry(R.id.p2Wagers));
            TextView p1ScoreText = (TextView) findViewById(R.id.p1Score);
            p1ScoreText.setText("" + p1Score);
            TextView p2ScoreText = (TextView) findViewById(R.id.p2Score);
            p2ScoreText.setText("" + p2Score);


            //show score
            String winnerMessage = "Game tied";
            if (p1Score > p2Score) {
                winnerMessage = "Player 1 won.";
            } else if (p2Score > p1Score) {
                winnerMessage = "Player 2 won.";
            }
            TextView winnerText = (TextView) findViewById((R.id.winnerText));
            winnerText.setText(winnerMessage);
        } catch (NumberFormatException e){
            //do nothing
        }
    }

    private void endActivityButton(int gameIndex) {
        Button btn = (Button) findViewById(R.id.end_add_game);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterCards(gameIndex);
                finish();
            }
        });
    }

    private int parseFromTextEntry(int id) throws NumberFormatException{
        EditText textEntry = (EditText) findViewById(id);
        String input = textEntry.getText().toString();
        return Integer.parseInt(input);

    }

    private void enterCards(int gameIndex) {
        try {

            int p1NumCards = parseFromTextEntry(R.id.p1NumCards);
            int p2NumCards = parseFromTextEntry(R.id.p2NumCards);
            int p1Sum = parseFromTextEntry(R.id.p1Sum);
            int p2Sum = parseFromTextEntry(R.id.p2Sum);
            int p1Wagers = parseFromTextEntry(R.id.p1Wagers);
            int p2Wagers = parseFromTextEntry(R.id.p2Wagers);

            GameManager test = GameManager.getInstance();
            Game testGame = new Game(gameCreationDateTime);
            testGame.addPlayer(new PlayerScore(0, p1NumCards, p1Sum, p1Wagers));
            testGame.addPlayer(new PlayerScore(1, p2NumCards, p2Sum, p2Wagers));

            if (gameIndex == -1) {
                test.addGame(testGame);
            }
            else {
                test.replaceGame(gameIndex, testGame);
            }

            Log.i("DemoInitialApp", "Player 1: " + p1NumCards);
            Log.i("DemoInitialApp", "Player 2: " + p2NumCards);
        }
        catch (NumberFormatException nfe) {
            Toast.makeText(getApplicationContext(), "Invalid input - fill in all fields", Toast.LENGTH_SHORT).show();
        }

    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, GameActivity.class);
    }


}