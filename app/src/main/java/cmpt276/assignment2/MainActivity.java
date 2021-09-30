package cmpt276.assignment2;

import androidx.appcompat.app.AppCompatActivity;
import ca.cmpt276.as2.model.*;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DemoInitialApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Wire up the button to do stuff
        //get the button
        FloatingActionButton btn = findViewById(R.id.btnAddNewGame);
        //set what happens when the user clicks
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Thia is a magic log message");
                Toast.makeText(getApplicationContext(), "Stuff has been done", Toast.LENGTH_SHORT).show();

            }
        });

        GameManager test = new GameManager();
        Game testGame = new Game();
        testGame.addPlayer(new PlayerScore(1, 3,5,6));
        testGame.addPlayer(new PlayerScore(2, 3,5,6));

        test.addGame(testGame);

        List<String> toString = test.getAllGames().stream().map(Game::toString).collect(Collectors.toList());

        ListView listView = (ListView) findViewById(R.id.gameListView);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, toString);
        listView.setAdapter(adapter);
    }


}