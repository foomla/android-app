package org.android;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.gson.Gson;
import org.joda.time.DateTime;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Transform exercises from json to csv.
 * <p>
 * TODO: "id" : 78,
 */
public class App {

    public static void main(String[] args) {
        System.out.println("Start transformation");

        try {
            final File exerciseFile = new File(args[0]);
            ExerciseList exercises = fromJson(exerciseFile);

            System.out.println("Found " + exercises.size() + " exercises");

            toCSV(exerciseFile.getParentFile(), exercises);
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: File not found: " + args[0]);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("ERROR: Export to CSV failed");
            e.printStackTrace();
        }
    }

    private static ExerciseList fromJson(File exerciseFile) throws FileNotFoundException {
        return new Gson().fromJson(new FileReader(exerciseFile), ExerciseList.class);
    }

    private static void toCSV(File directory, ExerciseList exercises) throws IOException {
        File csvFile = new File(directory, "exercises.csv");

        CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFile), ',', '"');
        csvWriter.writeAll(exercises.toList());
    }

    private static class ExerciseList extends ArrayList<Exercise> {

        List<String[]> toList() {
            return this.stream().map(Exercise::toArray).collect(Collectors.toList());
        }
    }

    private static class Exercise {
        String id;
        String note;
        String trainingFocus;
        String exerciseStatus;
        String setting;
        List<String> trainingPhases;
        String auxiliaryMaterial;
        int minPlayers;
        int maxPlayers;
        String schedule;
        List<String> ageClasses;
        String objective;
        String title;

        String[] toArray() {
            return new String[] {
                    DateTime.now().toString("dd.MM.yyyy HH:mm:ss"),
                    title,
                    trainingPhases.stream().collect(Collectors.joining(",")),
                    trainingFocus,
                    ageClasses.stream().collect(Collectors.joining(",")),
                    setting,
                    schedule, // Beschreibung
                    objective,
                    auxiliaryMaterial,
                    note,
                    "http://foomla.dnobel.de/exercises/images/exercise_" + id + ".png",
                    String.valueOf(minPlayers),
                    exerciseStatus
            };
        }
    }
}
