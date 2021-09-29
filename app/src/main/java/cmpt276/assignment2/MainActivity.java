package cmpt276.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DemoInitialApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Wire up the button to do stuff
        //get the button
        Button btn = findViewById(R.id.btnDoStuff);
        //set what happens when the user clicks
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Thia is a magic log message");
                Toast.makeText(getApplicationContext(), "Stuff has been done", Toast.LENGTH_SHORT).show();

            }
        });
    }
}