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

/**
 * GameActivity handles the screen for adding, editing, and deleting games. Six text fields allow
 * user to enter data about the game and those fields can be edited to change data about a current
 * game. Has save and delete buttons at the bottom and prompts when the user deletes a game or
 * leaves the activity without saving.
 * Redundant Cast warnings have been suppressed as Brian has stated in his videos they are safe.
 */
public class GameActivity extends AppCompatActivity {
    private final LocalDateTime gameCreationDateTime = LocalDateTime.now();
    private int currentGame = 0;
    private final GameManager gameManager = GameManager.getInstance();
    SharedPreferences sp;

    /**
     * Runs on creation of activity.
     * @param savedInstanceState default argument given by Android.
     */
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

        //uses shared preferences to pass deleting information to fragment
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("currentGame", currentGame);
        editor.commit();

        formatDateTime();
        initializeTextFields(intent);
        saveGameButton();
        previewGameResults();
        deleteGameButton();
    }

    /**
     * Formats the current date and time and places it in the proper text box.
     */
    private void formatDateTime() {
        //code learned from https://mkyong.com/java8/java-8-how-to-format-localdatetime/
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDatetime = gameCreationDateTime.format(formatter);

        //As per Brian's video tutorials, redundant casting is safe.
        @SuppressWarnings("RedundantCast") TextView dateCreateText = (TextView) findViewById(R.id.dateCreateText);
        dateCreateText.setText(formattedDatetime);
    }

    /**
     * Uses setEditTextAttributes to assign attributes to the 6 EditTexts
     * @param intent //TODO: @Kevin pls explain what this means
     */
    private void initializeTextFields(Intent intent) {
        setEditTextAttributes(intent.getIntExtra("p1NumCards", 0), R.id.p1NumCards);
        setEditTextAttributes(intent.getIntExtra("p2NumCards", 0), R.id.p2NumCards);
        setEditTextAttributes(intent.getIntExtra("p1Sum", 0), R.id.p1Sum);
        setEditTextAttributes(intent.getIntExtra("p2Sum", 0), R.id.p2Sum);
        setEditTextAttributes(intent.getIntExtra("p1Wagers", 0), R.id.p1Wagers);
        setEditTextAttributes(intent.getIntExtra("p2Wagers", 0), R.id.p2Wagers);
    }

    /**
     * Sets fields intent extras and attaches listener to given EditTexts.
     * @param valueToSet value to set EditText by default.
     * @param id of the EditText to set default value and attach listener.
     */
    @SuppressLint("SetTextI18n")
    private void setEditTextAttributes(int valueToSet, int id) {
        //As per Brian's video tutorials, redundant casting is safe.
        @SuppressWarnings("RedundantCast") EditText dataEntry = (EditText) this.findViewById(id);
        if (currentGame != -1) {
            dataEntry.setText(""+valueToSet);
        }

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

    /**
     * Reads the user input in the specified EditText
     * @param id the ID of the EditText you want to read data from.
     * @return integer of the text the user input.
     * @throws NumberFormatException if trying to read but no user input.
     */
    private int parseFromTextEntry(int id) throws NumberFormatException{
        //As per Brian's video tutorials, redundant casting is safe.
        @SuppressWarnings("RedundantCast") EditText textEntry = (EditText) findViewById(id);
        String input = textEntry.getText().toString();
        return Integer.parseInt(input);
    }

    /**
     * Calculates scores of players based on user input into fields and shows them on the screen.
     * If any values are not filled in yet then show a "-" for score.
     */
    @SuppressLint("SetTextI18n")
    private void previewGameResults() {
        int p1Score = 0;
        int p2Score = 0;
        //As per Brian's video tutorials, redundant casting is safe.
        @SuppressWarnings("RedundantCast") TextView p1ScoreText = (TextView) findViewById(R.id.p1Score);
        @SuppressWarnings("RedundantCast") TextView p2ScoreText = (TextView) findViewById(R.id.p2Score);
        @SuppressWarnings("RedundantCast") TextView winnerText = (TextView) findViewById((R.id.winnerText));
        try {
            p1Score = PlayerScore.calculatePlayerScore(parseFromTextEntry(R.id.p1NumCards),
                    parseFromTextEntry(R.id.p1Sum),
                    parseFromTextEntry(R.id.p1Wagers));

            p1ScoreText.setText("" + p1Score);
        } catch (NumberFormatException e){
            p1ScoreText.setText("-");
        }

        try {
            p2Score = PlayerScore.calculatePlayerScore(parseFromTextEntry(R.id.p2NumCards),
                    parseFromTextEntry(R.id.p2Sum),
                    parseFromTextEntry(R.id.p2Wagers));
            p2ScoreText.setText("" + p2Score);
        } catch (NumberFormatException e) {
            p2ScoreText.setText("-");
        }

        if ((p1ScoreText.getText() == "-" || p2ScoreText.getText() == "-")) {
            winnerText.setText("");
        } else {
            String winnerMessage = "Game tied";
            if (p1Score > p2Score) {
                winnerMessage = "Player 1 won.";
            } else if (p2Score > p1Score) {
                winnerMessage = "Player 2 won.";
            }
            winnerText.setText(winnerMessage);
        }
    }

    /**
     * Wires up save button to calculate score and save game when tapped.
     */
    private void saveGameButton() {
        //As per Brian's video tutorials, redundant casting is safe.
        @SuppressWarnings("RedundantCast") Button btn = (Button) findViewById(R.id.end_add_game);
        btn.setOnClickListener(v -> {
            saveGame();
            finish();
        });
    }

    /**
     * Wires up the delete button to delete a game when tapped. Hides button if new game is being
     * added.
     */
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

    /**
     * Prompts user for exit confirmation if Android back button is used.
     */
    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        ConfirmBackFragment dialog = new ConfirmBackFragment();
        dialog.show(manager, "MessageDialog");
    }

    /**
     * Prompts use for exit confirmation if back button on action bar is tapped.
     * @return true.
     */
    @Override
    public boolean onSupportNavigateUp() {
        FragmentManager manager = getSupportFragmentManager();
        ConfirmBackFragment dialog = new ConfirmBackFragment();
        dialog.show(manager, "MessageDialog");
        return true;
    }

    //TODO: does it have to quit when an invalid input is given or should it stay on the same screen to give the user a chance to re-enter data?
    /**
     * Reads all the user input and attempts to save as a new Game. Gives toast and exits if any
     * input is invalid
     */
    private void saveGame() {
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
            Toast.makeText(getApplicationContext(), "Game saved successfully!", Toast.LENGTH_SHORT).show();
        }
        catch (NumberFormatException nfe) {
            Toast.makeText(getApplicationContext(), "Invalid input - fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    //TODO: Could this not be called in deleteGameButton or is that not how it works
    /**
     * Deletes game from game manager at the specified index.
     * @param index of the game you want to delete
     */
    public void deleteGame(int index) {
        gameManager.deleteGameAt(index);
    }

    //TODO: javadoc for this because I don't understand it.
    public static Intent makeIntent(Context context) {
        return new Intent(context, GameActivity.class);
    }
}