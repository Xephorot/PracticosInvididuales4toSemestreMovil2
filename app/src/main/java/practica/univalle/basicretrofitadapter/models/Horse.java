package practica.univalle.basicretrofitadapter.models;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import practica.univalle.basicretrofitadapter.R;

public class Horse implements Runnable {
    private final ImageView horseImage;
    private LinearLayout horsesLayout;
    private static final int MAX_DISTANCE = 1000;
    private boolean isRaceInProgress = true;
    private String name;

    public Horse(View view, int horseImageResource, String name) {
        this.name = name;
        horseImage = view.findViewById(R.id.horseImage);
        horsesLayout = view.findViewById(R.id.racetrackLayout);
        horseImage.setImageResource(horseImageResource);

        TextView horseNameText = view.findViewById(R.id.horseNameText);
        horseNameText.setText(name);
    }

    @Override
    public void run() {
        int distance = 0;
        while (distance < MAX_DISTANCE && isRaceInProgress) {
            try {
                Thread.sleep((long) (Math.random() * 200));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            distance += (int) (Math.random() * 10);
            int finalDistance = Math.min(distance, MAX_DISTANCE);
            float progress = (float) finalDistance / MAX_DISTANCE;

            horseImage.post(() -> {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) horseImage.getLayoutParams();
                params.leftMargin = (int) (progress * (horsesLayout.getWidth() - horseImage.getWidth()));
                horseImage.setLayoutParams(params);

                ProgressBar progressBar = horsesLayout.findViewById(R.id.progressBar);
                int progressPercentage = (int) (progress * 100);
                progressBar.setProgress(progressPercentage);

                TextView percentageText = horsesLayout.findViewById(R.id.percentageText);
                percentageText.setText(progressPercentage + "%");
            });
        }
    }

    public void stopRace() {
        isRaceInProgress = false;
    }

    public String getName() {
        return name;
    }
    public void reset() {
        // Reinicia la posición del caballo
        horseImage.post(() -> {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) horseImage.getLayoutParams();
            params.leftMargin = 0;
            horseImage.setLayoutParams(params);
        });

        // Reinicia el progreso del ProgressBar y el porcentaje del TextView
        horsesLayout.post(() -> {
            ProgressBar progressBar = horsesLayout.findViewById(R.id.progressBar);
            progressBar.setProgress(0);

            TextView percentageText = horsesLayout.findViewById(R.id.percentageText);
            percentageText.setText("0%");
        });

        // Reinicia el estado de la carrera
        isRaceInProgress = true;
    }
}
