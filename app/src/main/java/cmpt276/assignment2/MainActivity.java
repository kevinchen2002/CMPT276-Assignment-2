package cmpt276.assignment2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import ca.cmpt276.as2.model.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DemoInitialApp";
    private GameManager gameManager = GameManager.getInstance();

    SharedPreferences sp;

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        ListView gameList = findViewById(R.id.gameListView);
        Group noGames = findViewById(R.id.noGamesGroup);

        gameList.setEmptyView(noGames);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Games Played");

        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);

        floatingActionBtn();

        registerGameClick();

        loadGame();

        showGameList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        showGameList();
    }

    void floatingActionBtn () {
        //Wire up the button to do stuff
        //get the button
        FloatingActionButton btn = findViewById(R.id.btnAddNewGame);
        //set what happens when the user clicks
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Thia is a magic log message");

                //launch the new game activity
                //Intent launchNewGame = new Intent(MainActivity.this, AddGameActivity.class);
                Intent launchNewGame = GameActivity.makeIntent(MainActivity.this);
                startActivity(launchNewGame);
            }
        });
    }

    void showGameList() {

//        List<String> toString = gameManager.getAllGames().stream().map(Game::toString).collect(Collectors.toList());

//        ListView listView = (ListView) findViewById(R.id.gameListView);
//        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, toString);
//        adapter.notifyDataSetChanged();
//        listView.setAdapter(adapter);

        ArrayAdapter<Game> gameAdapter = new MyListAdapter();
        ListView complexGamesView = (ListView) findViewById(R.id.gameListView);
        complexGamesView.setAdapter(gameAdapter);

        saveGame();
    }

    private class MyListAdapter extends ArrayAdapter<Game> {
        public MyListAdapter() {
            super(MainActivity.this, R.layout.game_layout, gameManager.getAllGames());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //get a new view
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.game_layout, parent, false);
            }

            //populate list
            Game currentGame = gameManager.getGameAt(position);

            //fill the view
            ImageView imageView = (ImageView) itemView.findViewById(R.id.game_image);
            if (currentGame.getWinnerCount() > 1) {
                imageView.setImageResource(R.drawable.nowin_icon);
                //TODO: implement method to make this not so long and awful
            } else if (currentGame.getWinnersNumAt(0) == 1) {
                imageView.setImageResource(R.drawable.win1_icon);
            } else {
                imageView.setImageResource(R.drawable.win2_icon);
            }

            //TODO: make gamemanger have an easier to use method here
            TextView scoreView = (TextView) itemView.findViewById(R.id.game_points);
            scoreView.setText(currentGame.getPoints());

            TextView dateView = (TextView) itemView.findViewById(R.id.game_date);
            dateView.setText(currentGame.getDateString());

            return itemView;
        }
    }

    /*
        Code taken from Brian Fraser's Basic ListView Demo
         */
    //TODO: make it so tapping on the item opens a view with the game details instead of showing a toast.
    private void registerGameClick() {
        ListView listView = (ListView) findViewById(R.id.gameListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                GameManager gameList = GameManager.getInstance();
                Game clickedGame = gameList.getGameAt(position);
                int playerNumber = clickedGame.getPlayerCount();

                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("gamePosition", position);
                intent.putExtra("p1NumCards", clickedGame.getPlayers(0).getCardPlayed());
                intent.putExtra("p2NumCards", clickedGame.getPlayers(1).getCardPlayed());
                intent.putExtra("p1Sum", clickedGame.getPlayers(0).getSumOfCards());
                intent.putExtra("p2Sum", clickedGame.getPlayers(1).getSumOfCards());
                intent.putExtra("p1Wagers", clickedGame.getPlayers(0).getNumOfWagers());
                intent.putExtra("p2Wagers", clickedGame.getPlayers(1).getNumOfWagers());

                startActivity(intent);
            }
        });

    }

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
        Log.i(TAG, jsonString);
        editor.putString("gameList", jsonString);
        editor.commit();

    }

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
        if (jsonString != "") {
            //Type deserialization taken from https://stackoverflow.com/questions/5554217/google-gson-deserialize-listclass-object-generic-type
            Type listType = new TypeToken<ArrayList<Game>>() {}.getType();
            gameManager.setAllGames(myGson.fromJson(jsonString, listType));
        }
    }

}