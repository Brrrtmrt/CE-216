package org.example.artifactcatalog2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;


import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;


public class JSONOperations {

    //Data.json will be created in program main?, below is only the string of the path it does not actually create the file.

    private static Path databasePath = Paths.get(System.getProperty("user.dir"), "Data.json");
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Artifact.class, new ArtifactTypeAdapter())
            .registerTypeAdapter(Dimension.class, new DimensionTypeAdapter())
            .create();
    private static final Type artifactListType = new TypeToken<ArrayList<Artifact>>() {
    }.getType();

    public static boolean importJSON(Path file) {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            ArrayList<Artifact> newList = gson.fromJson(reader, artifactListType);
            if (newList == null) {
                return false;
            }

            ArrayList<Artifact> existingList = readExistingList();
            Set<Artifact> artifactSet = new LinkedHashSet<>(existingList);
            //check if there is a difference if not do not write.
            boolean flag = artifactSet.addAll(newList);
            if (!flag) {
                return false;
            }
            return writeJSON(databasePath, new ArrayList<>(artifactSet));
        } catch (IOException | JsonParseException e) {
            return false;
        }
    }

    //Need file selector JavaFX
    public static boolean exportJSON(Path out) {

        return false;
    }


    public static boolean writeJSON(Path path, ArrayList<Artifact> list) {
        ArrayList<Artifact> existingList = readExistingList();
        Set<Artifact> artifactSet = new LinkedHashSet<>(existingList);
        boolean flag = artifactSet.addAll(list);
        if (!flag) {
            return false;
        }
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            gson.toJson(new ArrayList<>(artifactSet), writer);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static ArrayList<Artifact> readExistingList() {
        if (Files.exists(databasePath)) {
            try (BufferedReader reader = Files.newBufferedReader(databasePath)) {
                ArrayList<Artifact> existingList = gson.fromJson(reader, artifactListType);
                return existingList != null ? existingList : new ArrayList<>();
            } catch (IOException e) {
                return new ArrayList<>();
            }
        } else {
            try {
                Files.createFile(databasePath);
            } catch (IOException e) {
                return new ArrayList<>();
            }
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        //TEST object
        Artifact art = new Artifact("Foo", "Bar", "ManuScript", "İzmir", new ArrayList<>(Arrays.asList("Test", "MS")), "A", LocalDate.of(2025, 10, 10), "İzmir", new Dimension(10, 10, 10), 10000, new ArrayList<>(Arrays.asList("MS", "value")));
        Artifact art2 = new Artifact("test", "test", "ManuScript", "İzmir", new ArrayList<>(Arrays.asList("Test", "MS")), "A", LocalDate.of(2025, 10, 10), "İzmir", new Dimension(10, 10, 10), 10000, new ArrayList<>(Arrays.asList("MS", "value")));
        Artifact art3 = new Artifact("asddsa", "adsdas", "ManuScript", "İzmir", new ArrayList<>(Arrays.asList("Test", "MS")), "A", LocalDate.of(2025, 10, 10), "İzmir", new Dimension(10, 10, 10), 10000, new ArrayList<>(Arrays.asList("MS", "value")));
        Artifact art4 = new Artifact("asddaaaasa", "adsdas", "ManuScript", "İzmir", new ArrayList<>(Arrays.asList("Test", "MS")), "A", LocalDate.of(2025, 10, 10), "İzmir", new Dimension(10, 10, 10), 10000, new ArrayList<>(Arrays.asList("MS", "value")));
        Artifact art5 = new Artifact("fggfgffg", "adsdas", "ManuScript", "İzmir", new ArrayList<>(Arrays.asList("Test", "MS")), "A", LocalDate.of(2025, 10, 10), "İzmir", new Dimension(10, 10, 10), 10000, new ArrayList<>(Arrays.asList("MS", "value")));
        Artifact art6 = new Artifact("ooooooo", "adsdas", "ManuScript", "İzmir", new ArrayList<>(Arrays.asList("Test", "MS")), "A", LocalDate.of(2025, 10, 10), "İzmir", new Dimension(10, 10, 10), 10000, new ArrayList<>(Arrays.asList("MS", "value")));

        ArrayList<Artifact> list = new ArrayList<>(Arrays.asList(art, art2, art3, art4, art5, art6));

        Path pt = Paths.get("Test.json");
        importJSON(pt);
        // writeJSON(databasePath, list);
    }

    @Deprecated
    public static String serialize(Artifact artifact) {
        return gson.toJson(artifact);

    }

    public static String serialize(ArrayList<Artifact> artifact) {
        return gson.toJson(artifact);

    }

    @Deprecated
    public static Artifact deserialize(String json) {
        return gson.fromJson(json, Artifact.class);
    }


    //If json is bad it will make the list null!!
    public static ArrayList<Artifact> deserializeList(String json) {
        return gson.fromJson(json, artifactListType);
    }


    public static Path getDb() {
        return databasePath;
    }

    public static void setDb(Path db) {
        JSONOperations.databasePath = db;
    }
}
