package org.example.artifactcatalog2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class JSONOperations {

    //Data.json will be created in program main?, below is only the string of the path it does not actually create the file.
    private static Path databasePath = Paths.get(System.getProperty("user.dir"), "Data.json");

    //This will not create duplicates.
    public static boolean importJSON(Path file) {
        try {
            String json = Files.readString(file);
            ArrayList<Artifact> newList = deserializeList(json);

            if (newList == null) {
                return false;
            }

            ArrayList<Artifact> existingList;
            if (Files.exists(databasePath)) {
                String currJSON = Files.readString(databasePath);
                existingList = deserializeList(currJSON);
                if (existingList == null) {
                    existingList = new ArrayList<>();
                }
            } else {//if no Data.json no existingList
                Path pt = Paths.get(System.getProperty("user.dir"), "Data.json"); //maybe redundant
                Files.createFile(pt);
                writeJSON(databasePath, newList);
                return true;
            }
            //Key is Artifact ID
            Set<Artifact> artifactSet = new HashSet<>(existingList);
            artifactSet.addAll(newList);
            writeJSON(databasePath, new ArrayList<>(artifactSet));
            return true;

        } catch (IOException | JsonParseException e) {
            return false;
        }

    }

    public static boolean exportJSON(Path in, Path out) {

        return false;
    }

    //Main JSON File for programs usage
    //If you throw in same objects in list it will create duplicates.For now...
    public static boolean writeJSON(Path path, ArrayList<Artifact> list) {
        try {
            ArrayList<Artifact> existingList = new ArrayList<>();
            if (Files.exists(path)) {
                String currJSON = Files.readString(path);
                existingList = deserializeList(currJSON);
                if (existingList == null) {
                    existingList = new ArrayList<>();
                }
            }

            existingList.addAll(list);
            String json = serialize(existingList);

            try (FileWriter fw = new FileWriter(path.toFile())) {
                fw.write(json);
                return true;
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        //TEST object
        Artifact art = new Artifact("Foo", "Bar", "ManuScript", "İzmir", new ArrayList<>(Arrays.asList("Test", "MS")), "A", LocalDate.of(2025, 10, 10), "İzmir", new Dimension(10, 10, 10), 10000, new ArrayList<>(Arrays.asList("MS", "value")));
        Artifact art2 = new Artifact("test", "test", "ManuScript", "İzmir", new ArrayList<>(Arrays.asList("Test", "MS")), "A", LocalDate.of(2025, 10, 10), "İzmir", new Dimension(10, 10, 10), 10000, new ArrayList<>(Arrays.asList("MS", "value")));
        Artifact art3 = new Artifact("asddsa", "adsdas", "ManuScript", "İzmir", new ArrayList<>(Arrays.asList("Test", "MS")), "A", LocalDate.of(2025, 10, 10), "İzmir", new Dimension(10, 10, 10), 10000, new ArrayList<>(Arrays.asList("MS", "value")));
        ArrayList<Artifact> list = new ArrayList<>(Arrays.asList(art, art2, art3));
        // writeJSON(databasePath, list);
        // Path pt = Paths.get("Test.json");
        // importJSON(pt);
    }

    @Deprecated
    public static String serialize(Artifact artifact) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Artifact.class, new ArtifactTypeAdapter()).registerTypeAdapter(Dimension.class, new DimensionTypeAdapter())
                .create();
        return gson.toJson(artifact);

    }

    public static String serialize(ArrayList<Artifact> artifact) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Artifact.class, new ArtifactTypeAdapter()).registerTypeAdapter(Dimension.class, new DimensionTypeAdapter())
                .create();
        return gson.toJson(artifact);

    }

    @Deprecated
    public static Artifact deserialize(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Artifact.class, new ArtifactTypeAdapter()).registerTypeAdapter(Dimension.class, new DimensionTypeAdapter())
                .create();
        return gson.fromJson(json, Artifact.class);
    }


    //If json is bad it will make the list null!!
    public static ArrayList<Artifact> deserializeList(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Artifact.class, new ArtifactTypeAdapter()).registerTypeAdapter(Dimension.class, new DimensionTypeAdapter())
                .create();
        Type artifactListType = new TypeToken<ArrayList<Artifact>>() {
        }.getType();
        return gson.fromJson(json, artifactListType);
    }


    public static Path getDb() {
        return databasePath;
    }

    public static void setDb(Path db) {
        JSONOperations.databasePath = db;
    }
}
