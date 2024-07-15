package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import objects.Candidate;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final String candidates = "data/json/candidates.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static List<Candidate> readCandidates() {
        File file = new File(candidates);
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Type taskListType = new TypeToken<List<Candidate>>() {}.getType();
                List<Candidate> tasks  = gson.fromJson(reader, taskListType);
                System.out.println("Candidates loaded from file.");
                return tasks;
            } catch (IOException e) {
                System.out.println("Error loading Candidates: " + e.getMessage());
            }
        } else {
            System.out.println("No Candidates found. Starting with an empty list.");
        }
        return new ArrayList<>();
    }

    public static void saveCandidates(List<Candidate> candidatesList) {
        try (Writer writer = new FileWriter(candidates)) {
            gson.toJson(candidatesList, writer);
            System.out.println("Candidates saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving Candidates: ");
        }
    }
}
