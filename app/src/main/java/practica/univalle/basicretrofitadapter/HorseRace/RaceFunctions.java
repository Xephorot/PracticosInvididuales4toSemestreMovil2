package practica.univalle.basicretrofitadapter.HorseRace;

import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import practica.univalle.basicretrofitadapter.HorseRaceActivity;
import practica.univalle.basicretrofitadapter.R;
import practica.univalle.basicretrofitadapter.models.Horse;
import java.util.concurrent.atomic.AtomicBoolean;


public class RaceFunctions {
    private HorseRaceActivity activity;

    public RaceFunctions(HorseRaceActivity activity) {
        this.activity = activity;
    }

    public void startRace() {
        if (!activity.raceInProgress && !activity.buttonsLocked) {
            activity.raceInProgress = true;
            lockButtons();
            for (Horse horse : activity.horses) {
                new Thread(horse).start();
            }
        }
    }

    public void stopSingleHorseByName() {
        EditText horseNameInput = activity.findViewById(R.id.horseNameInput);
        String horseName = horseNameInput.getText().toString();

        if (!horseName.isEmpty()) {
            boolean horseFound = false;
            for (Horse horse : activity.horses) {
                if (horse.getName().equals(horseName)) {
                    horse.stopRace();
                    horseFound = true;
                    break;
                }
            }
            if (!horseFound) {
                Toast.makeText(activity, "Caballo no encontrado", Toast.LENGTH_SHORT).show();
            } else {
                // Vacía el EditText después de detener el caballo
                horseNameInput.setText("");
            }
        } else {
            Toast.makeText(activity, "Por favor, ingrese el nombre del caballo", Toast.LENGTH_SHORT).show();
        }
    }


    public void showNumberOfHorsesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Ingrese el número de caballos");

        final EditText input = new EditText(activity);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            String inputText = input.getText().toString();
            int numberOfHorses;

            if (inputText.isEmpty()) {
                numberOfHorses = 2;
            } else {
                numberOfHorses = Integer.parseInt(inputText);
            }

            // Asegura que el número mínimo de caballos sea 2
            if (numberOfHorses < 2) {
                numberOfHorses = 2;
            }

            activity.horses = new Horse[numberOfHorses];
            addHorsesAndAssignNames(0); // Inicia la asignación de nombres con el primer caballo (índice 0)
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }



    public void addHorsesAndAssignNames(int horseIndex) {
        if (horseIndex >= activity.horses.length) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Asignar nombre al caballo " + (horseIndex + 1));

        final EditText input = new EditText(activity);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            String horseName = input.getText().toString();

            // Verifica si el nombre del caballo ya existe
            boolean nameExists = false;
            for (Horse horse : activity.horses) {
                if (horse != null && horse.getName().equals(horseName)) {
                    nameExists = true;
                    break;
                }
            }

            if (!nameExists) {
                LinearLayout layout = activity.findViewById(R.id.horsesLayout);
                View horseView = activity.getLayoutInflater().inflate(R.layout.horse_layout, layout, false);
                int horseImageResource = getImageResourceForHorse(horseIndex);
                activity.horses[horseIndex] = new Horse(horseView, horseImageResource, horseName);
                layout.addView(horseView);
                addHorsesAndAssignNames(horseIndex + 1);
            } else {
                Toast.makeText(activity, "El nombre del caballo ya existe, por favor ingrese otro nombre", Toast.LENGTH_SHORT).show();
                addHorsesAndAssignNames(horseIndex);
            }
        });

        // Agrega el botón "Randomizar Nombres" al AlertDialog
        builder.setNeutralButton("Randomizar Nombre", (dialog, which) -> {
            String horseName = "horse" + (horseIndex + 1);
            LinearLayout layout = activity.findViewById(R.id.horsesLayout);
            View horseView = activity.getLayoutInflater().inflate(R.layout.horse_layout, layout, false);
            int horseImageResource = getImageResourceForHorse(horseIndex);
            activity.horses[horseIndex] = new Horse(horseView, horseImageResource, horseName);
            layout.addView(horseView);
            addHorsesAndAssignNames(horseIndex + 1);
        });

        builder.show();
    }

    private int[] horseImageResources = {
            R.drawable.horse1,
            R.drawable.horse2,
            R.drawable.img,
            R.drawable.img_1,
            R.drawable.img_2,
            R.drawable.img_3,
            R.drawable.img_4,
            R.drawable.img_5,
            R.drawable.img_6,
    };

    private int getImageResourceForHorse(int index) {
        if (index < horseImageResources.length) {
            return horseImageResources[index];
        }
        return R.drawable.horse3;
    }
    public void stopSingleHorseByNumber(String horseNumbers) {
        String[] numbers = horseNumbers.split(",");
        for (String number : numbers) {
            try {
                int horseNumber = Integer.parseInt(number.trim()) - 1;
                if (horseNumber >= 0 && horseNumber < activity.horses.length) {
                    Horse horse = activity.horses[horseNumber];
                    if (horse != null) {
                        horse.stopRace();
                    } else {
                        Toast.makeText(activity, "Caballo no encontrado: " + (horseNumber + 1), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Número de caballo inválido: " + (horseNumber + 1), Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(activity, "Entrada inválida: " + number, Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void stopAllHorses() {
        unlockButtons();
        for (Horse horse : activity.horses) {
            if (horse != null) {
                horse.stopRace();
            }
        }
        Button startRaceButton = activity.findViewById(R.id.startRaceButton);
        startRaceButton.setEnabled(false);
    }

    public void resetRace() {
        // Detiene la carrera actual
        stopAllHorses();

        // Reinicia la posición y el estado de los caballos
        for (Horse horse : activity.horses) {
            if (horse != null) {
                horse.reset();
            }
        }

        // Cambia el valor de raceInProgress a false
        activity.raceInProgress = false;

        // Desbloquea los botones
        unlockButtons();
    }
    public void lockButtons() {
        activity.buttonsLocked = true;
        Button startRaceButton = activity.findViewById(R.id.startRaceButton);
        Button resetRaceButton = activity.findViewById(R.id.resetRaceButton);
        startRaceButton.setEnabled(false);
        resetRaceButton.setEnabled(false);
    }
    public void unlockButtons() {
        activity.buttonsLocked = false;
        Button resetRaceButton = activity.findViewById(R.id.resetRaceButton);
        resetRaceButton.setEnabled(false); //Cambiar a True en el examen
        Button startRaceButton = activity.findViewById(R.id.startRaceButton);
        startRaceButton.setEnabled(true);
    }
}
