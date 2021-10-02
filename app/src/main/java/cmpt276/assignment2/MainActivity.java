package cmpt276.assignment2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import ca.cmpt276.as2.model.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

/**
 * MainActivity is the home screen of the app. It shows an empty screen when no games are added and
 * a complex ListView of all the games when there are some. A FAB at the bottom allows for users
 * to add new games.
 * Redundant Cast warnings have been suppressed as Brian has stated in his videos they are safe.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DemoInitialApp";
    private final GameManager gameManager = GameManager.getInstance();
    SharedPreferences sp;

    /**
     * Runs on creation.
     * @param savedInstanceState default argument given by Android.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Games Played");
        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);

        floatingActionBtn();
        registerGameClick();
        loadGame();
        showGameList();
    }

    /**
     * Sets the empty state of the app when there are no games.
     */
    @Override
    public void onContentChanged() {
        super.onContentChanged();

        ListView gameList = findViewById(R.id.gameListView);
        Group noGames = findViewById(R.id.noGamesGroup);
        gameList.setEmptyView(noGames);
    }

    /**
     * Refreshes the game list when returning back to this activity.
     */
    @Override
    protected void onResume() {
        super.onResume();

        showGameList();
    }

    /**
     * Wires the FAB to open the game activity when tapped.
     */
    void floatingActionBtn () {
        FloatingActionButton btn = findViewById(R.id.btnAddNewGame);
        btn.setOnClickListener(v -> {
            Intent launchNewGame = GameActivity.makeIntent(MainActivity.this);
            startActivity(launchNewGame);
        });
    }

    /**
     * Creates an adapter for the gameList to turn it into a LisView.
     */
    void showGameList() {
        ArrayAdapter<Game> gameAdapter = new MyListAdapter();
        //As per Brian's video tutorials, redundant casting is safe.
        @SuppressWarnings("RedundantCast") ListView complexGamesView = (ListView) findViewById(R.id.gameListView);
        complexGamesView.setAdapter(gameAdapter);

        saveGame();
    }

    /**
     * MyListAdapter adapts the content of ArrayList into a complex list view.
     */
    private class MyListAdapter extends ArrayAdapter<Game> {
        public MyListAdapter() {
            super(MainActivity.this, R.layout.game_layout, gameManager.getAllGames());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            Game currentGame = gameManager.getGameAt(position);
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.game_layout, parent, false);
            }

            //As per Brian's video tutorials, redundant casting is safe.
            @SuppressWarnings("RedundantCast") ImageView imageView = (ImageView) itemView.findViewById(R.id.game_image);
            if (currentGame.getWinnerCount() > 1) {
                imageView.setImageResource(R.drawable.nowin_icon);
            } else if (currentGame.getWinnersNumAt(0) == 1) {
                imageView.setImageResource(R.drawable.win1_icon);
            } else {
                imageView.setImageResource(R.drawable.win2_icon);
            }

            //As per Brian's video tutorials, redundant casting is safe.
            @SuppressWarnings("RedundantCast") TextView scoreView = (TextView) itemView.findViewById(R.id.game_points);
            @SuppressWarnings("RedundantCast") TextView dateView = (TextView) itemView.findViewById(R.id.game_date);
            scoreView.setText(currentGame.getPoints());
            dateView.setText(currentGame.getDateString());

            return itemView;
        }
    }

    /**
     * Manages when a user taps a game in the ListView. Starts a new game activity and sends
     * that game's data as an intent to the activity for editing.
     */
    private void registerGameClick() {
        //As per Brian's video tutorials, redundant casting is safe.
        @SuppressWarnings("RedundantCast") ListView listView = (ListView) findViewById(R.id.gameListView);
        listView.setOnItemClickListener((parent, viewClicked, position, id) -> {
            GameManager gameList = GameManager.getInstance();
            Game clickedGame = gameList.getGameAt(position);

            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("gamePosition", position);
            intent.putExtra("p1NumCards", clickedGame.getPlayer(0).getCardPlayed());
            intent.putExtra("p2NumCards", clickedGame.getPlayer(1).getCardPlayed());
            intent.putExtra("p1Sum", clickedGame.getPlayer(0).getSumOfCards());
            intent.putExtra("p2Sum", clickedGame.getPlayer(1).getSumOfCards());
            intent.putExtra("p1Wagers", clickedGame.getPlayer(0).getNumOfWagers());
            intent.putExtra("p2Wagers", clickedGame.getPlayer(1).getNumOfWagers());

            startActivity(intent);
        });
    }

    //TODO: check if this class implementation can remove some repeated code in saveGame and loadGame.
    /**
     * LocalDateTimeJSONReader is used to read and write LocalDateTime objects to JSON files.
     */
    private class LocalDateTimeJSONReader extends TypeAdapter<LocalDateTime> {
        @Override
        public void write(JsonWriter jsonWriter,
                          LocalDateTime localDateTime) throws IOException {
            jsonWriter.value(localDateTime.toString());
        }

        @Override
        public LocalDateTime read(JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString());
        }
    }

    /**
     * Saves the game manager and all of its games to a JSON file on shutdown.
     * TODO: find where Victor got this code from and give credit.
     */
    @SuppressLint("ApplySharedPref")
    void saveGame() {
        SharedPreferences.Editor editor = sp.edit();
        Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }
                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).create();

        String jsonString = myGson.toJson(gameManager.getAllGames());
        Log.i(TAG, jsonString); //TODO: needed?
        editor.putString("gameList", jsonString);
        editor.commit();
    }

    /**
     * Loads JSON file and adds games to GameList on startup.
     */
    void loadGame() {
        Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }
                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).create();

        String jsonString = sp.getString("gameList", "");
        if (!jsonString.equals("")) {
            //Type deserialization taken from https://stackoverflow.com/questions/5554217/google-gson-deserialize-listclass-object-generic-type
            Type listType = new TypeToken<ArrayList<Game>>() {}.getType();
            gameManager.setAllGames(myGson.fromJson(jsonString, listType));
        }
    }
}