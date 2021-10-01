package cmpt276.assignment2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import ca.cmpt276.as2.model.*;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DemoInitialApp";
    private final String fileName = "data.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Games Played");
        floatingActionBtn();

        registerGameClick();

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
        GameManager test = GameManager.getInstance();
        Game testGame = new Game();
        testGame.addPlayer(new PlayerScore(1, 3,5,6));
        testGame.addPlayer(new PlayerScore(2, 3,5,6));

        Game testGame2 = new Game();
        testGame2.addPlayer(new PlayerScore(1, 20,5,6));
        testGame2.addPlayer(new PlayerScore(2, 30,5,6));

        List<String> toString = test.getAllGames().stream().map(Game::toString).collect(Collectors.toList());

        ListView listView = (ListView) findViewById(R.id.gameListView);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, toString);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
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
                TextView textView = (TextView) viewClicked;
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

    void createFile() {
        try {
            File foodStorage = new File(fileName);
            if (foodStorage.createNewFile()) {
                System.out.println("File data.json created!");
            }
        } catch (IOException e) {
            System.out.println("Error while creating file");
            e.printStackTrace();
        }
    }

    //PLACE LOADFILE AND WRITEFILE HERE LATER

}