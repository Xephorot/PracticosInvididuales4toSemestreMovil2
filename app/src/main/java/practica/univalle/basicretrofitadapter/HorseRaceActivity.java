package practica.univalle.basicretrofitadapter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import practica.univalle.basicretrofitadapter.HorseRace.RaceFunctions;
import practica.univalle.basicretrofitadapter.models.Horse;

public class HorseRaceActivity extends AppCompatActivity {

    private static final int NUM_CABALLOS = 10;
    public boolean raceInProgress = false;
    public boolean buttonsLocked = false;

    public Horse[] horses = new Horse[NUM_CABALLOS];

    private RaceFunctions raceFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horse_race);


        raceFunctions = new RaceFunctions(this);

        LinearLayout layout = findViewById(R.id.horsesLayout);

        Button startRaceButton = findViewById(R.id.startRaceButton);
        Button stopRaceButton = findViewById(R.id.stopRaceButton);
        Button stopSingleHorseByNameButton = findViewById(R.id.stopSingleHorseByNameButton);

        Button stopSingleHorseByNumberButton = findViewById(R.id.stopSingleHorseByNumberButton);;

        stopSingleHorseByNumberButton.setOnClickListener(v -> {
            EditText horseNumberInput = findViewById(R.id.horseNumberInput);
            String horseNumbers = horseNumberInput.getText().toString();
            raceFunctions.stopSingleHorseByNumber(horseNumbers);
        });

        Button resetRaceButton = findViewById(R.id.resetRaceButton);
        resetRaceButton.setOnClickListener(v -> raceFunctions.resetRace());
        resetRaceButton.setEnabled(false);

        startRaceButton.setOnClickListener(v -> raceFunctions.startRace());
        stopRaceButton.setOnClickListener(v -> raceFunctions.stopAllHorses());

        stopSingleHorseByNameButton.setOnClickListener(v -> raceFunctions.stopSingleHorseByName());

        raceFunctions.showNumberOfHorsesDialog();


    }
}
