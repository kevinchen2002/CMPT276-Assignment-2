package cmpt276.assignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import ca.cmpt276.as2.model.Game;
import ca.cmpt276.as2.model.GameManager;
import ca.cmpt276.as2.model.PlayerScore;

public class GameActivity extends AppCompatActivity {

    private final LocalDateTime gameCreationDateTime = LocalDateTime.now();
    private int currentGame = 0;
    SharedPreferences sp;
    private final GameManager gameManager = GameManager.getInstance();

    @SuppressLint("ApplySharedPref")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add New Game");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        this.currentGame = intent.getIntExtra("gamePosition", -1);

        if (currentGame != -1) {
            getSupportActionBar().setTitle("Edit Game");
            previewGameResults();
        }

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("currentGame", currentGame);
        editor.commit();

        formatDateTime();
        setDefaultValues(intent);
        endActivityButton();
        deleteGameButton();
    }

    private void formatDateTime() {
        //As per Brian's video tutorials, redundant casting is safe.
        @SuppressWarnings("RedundantCast") TextView dateCreateText = (TextView) findViewById(R.id.dateCreateText);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDatetime = gameCreationDateTime.format(formatter);
        dateCreateText.setText(formattedDatetime);
    }

    private void setDefaultValues(Intent intent) {
        setDefaultValue(intent.getIntExtra("p1NumCards", 0), R.id.p1NumCards);
        setDefaultValue(intent.getIntExtra("p2NumCards", 0), R.id.p2NumCards);
        setDefaultValue(intent.getIntExtra("p1Sum", 0), R.id.p1Sum);
        setDefaultValue(intent.getIntExtra("p2Sum", 0), R.id.p2Sum);
        setDefaultValue(intent.getIntExtra("p1Wagers", 0), R.id.p1Wagers);
        setDefaultValue(intent.getIntExtra("p2Wagers", 0), R.id.p2Wagers);
    }

    @SuppressLint("SetTextI18n")
    private void setDefaultValue(int valueToSet, int id) {
        //As per Brian's video tutorials, redundant casting is safe.
        @SuppressWarnings("RedundantCast") EditText dataEntry = (EditText) this.findViewById(id);
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

    @SuppressLint("SetTextI18n")
    private void previewGameResults() {
        try {
            int p1Score = PlayerScore.calculatePlayerScore(parseFromTextEntry(R.id.p1NumCards),
                    parseFromTextEntry(R.id.p1Sum),
                    parseFromTextEntry(R.id.p1Wagers));
            int p2Score = PlayerScore.calculatePlayerScore(parseFromTextEntry(R.id.p2NumCards),
                    parseFromTextEntry(R.id.p2Sum),
                    parseFromTextEntry(R.id.p2Wagers));
            //As per Brian's video tutorials, redundant casting is safe.
            @SuppressWarnings("RedundantCast") TextView p1ScoreText = (TextView) findViewById(R.id.p1Score);
            p1ScoreText.setText("" + p1Score);
            @SuppressWarnings("RedundantCast") TextView p2ScoreText = (TextView) findViewById(R.id.p2Score);
            p2ScoreText.setText("" + p2Score);

            String winnerMessage = "Game tied";
            if (p1Score > p2Score) {
                winnerMessage = "Player 1 won.";
            } else if (p2Score > p1Score) {
                winnerMessage = "Player 2 won.";
            }
            //As per Brian's video tutorials, redundant casting is safe.
            @SuppressWarnings("RedundantCast") TextView winnerText = (TextView) findViewById((R.id.winnerText));
            winnerText.setText(winnerMessage);
        } catch (NumberFormatException e){
            //do nothing
        }
    }

    private void endActivityButton() {
        //As per Brian's video tutorials, redundant casting is safe.
        @SuppressWarnings("RedundantCast") Button btn = (Button) findViewById(R.id.end_add_game);
        btn.setOnClickListener(v -> {
            enterCards();
            finish();
        });
    }

    private void deleteGameButton() {
        //As per Brian's video tutorials, redundant casting is safe.
        @SuppressWarnings("RedundantCast") Button btn = (Button) findViewById(R.id.delete_game_btn);
        if (currentGame == -1) {
            btn.setVisibility(View.GONE);
        }
        btn.setOnClickListener(v -> {

            FragmentManager manager = getSupportFragmentManager();
            ConfirmDeleteFragment dialog = new ConfirmDeleteFragment();
            dialog.show(manager, "MessageDialog");

            Log.i("TAG", "just showed dialog");
        });
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        ConfirmBackFragment dialog = new ConfirmBackFragment();
        dialog.show(manager, "MessageDialog");
    }

    @Override
    public boolean onSupportNavigateUp() {
        FragmentManager manager = getSupportFragmentManager();
        ConfirmBackFragment dialog = new ConfirmBackFragment();
        dialog.show(manager, "MessageDialog");
        return true;
    }

    private int parseFromTextEntry(int id) throws NumberFormatException{
        //As per Brian's video tutorials, redundant casting is safe.
        @SuppressWarnings("RedundantCast") EditText textEntry = (EditText) findViewById(id);
        String input = textEntry.getText().toString();
        return Integer.parseInt(input);
    }

    private void enterCards() {
        try {
            int p1NumCards = parseFromTextEntry(R.id.p1NumCards);
            int p2NumCards = parseFromTextEntry(R.id.p2NumCards);
            int p1Sum = parseFromTextEntry(R.id.p1Sum);
            int p2Sum = parseFromTextEntry(R.id.p2Sum);
            int p1Wagers = parseFromTextEntry(R.id.p1Wagers);
            int p2Wagers = parseFromTextEntry(R.id.p2Wagers);

            Game newGame = new Game(gameCreationDateTime);
            newGame.addPlayer(new PlayerScore(0, p1NumCards, p1Sum, p1Wagers));
            newGame.addPlayer(new PlayerScore(1, p2NumCards, p2Sum, p2Wagers));

            if (currentGame == -1) {
                gameManager.addGame(newGame);
            }
            else {
                gameManager.replaceGame(currentGame, newGame);
            }

            Log.i("DemoInitialApp", "Player 1: " + p1NumCards);
            Log.i("DemoInitialApp", "Player 2: " + p2NumCards);
        }
        catch (NumberFormatException nfe) {
            Toast.makeText(getApplicationContext(), "Invalid input - fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteGame(int gameIndex) {
        gameManager.deleteGameAt(gameIndex);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, GameActivity.class);
    }
}