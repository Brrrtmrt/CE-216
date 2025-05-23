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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class JSONOperations {

    //Data.json will be created in program main?, below is only the string of the path it does not actually create the file.

    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private static Path databasePath = Paths.get(System.getProperty("user.dir"), "Data.json");
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Artifact.class, new ArtifactTypeAdapter())
            .registerTypeAdapter(Dimension.class, new DimensionTypeAdapter())
            .create();
    private static final Type artifactListType = new TypeToken<ArrayList<Artifact>>() {
    }.getType();

    public static boolean importJSON(Path file) {
        ArrayList<Artifact> newList;//no need for lock
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            newList = gson.fromJson(reader, artifactListType);
            if (newList == null) {
                return false;
            }
        } catch (IOException | JsonParseException e) {
            return false;
        }
        ArrayList<Artifact> existingList = readExistingList();
        lock.writeLock().lock();
        try {
            Set<Artifact> artifactSet = new LinkedHashSet<>(existingList);
            boolean flag = artifactSet.addAll(newList);
            if (!flag) {
                return false;
            }
            return writeJSON(databasePath, new ArrayList<>(artifactSet)); //TODO: nocheckwritejson?
        } finally {
            lock.writeLock().unlock();
        }
    }

    //Need file selector JavaFX
    public static boolean exportJSON(ArrayList<Artifact> selectedArtifacts) {
        /*
        SELECT ARTIFACTS FROM GUI
        SELECT OUT FILE FROM JAVAFX FILE SELECTOR
        CALL THIS WITH THOSE
         */
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        String date = LocalDateTime.now().format(formatter);
        Path out = Paths.get(System.getProperty("user.dir"), "Export " + date + ".json");
        return noCheckWriteJSON(out, selectedArtifacts);
    }


    public static boolean writeJSON(Path path, ArrayList<Artifact> list) {
        ArrayList<Artifact> existingList = readExistingList(); //release read lock
        lock.writeLock().lock();
        try {
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
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static boolean noCheckWriteJSON(Path path, ArrayList<Artifact> list) {
        lock.writeLock().lock();
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            gson.toJson(list, writer);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            lock.writeLock().unlock();
        }

    }

    public static ArrayList<Artifact> readExistingList() {
        lock.readLock().lock();
        try {
            if (Files.exists(databasePath)) {
                try (BufferedReader reader = Files.newBufferedReader(databasePath)) {
                    ArrayList<Artifact> existingList = gson.fromJson(reader, artifactListType);
                    return existingList != null ? existingList : new ArrayList<>();
                } catch (IOException e) {
                    return new ArrayList<>();
                }
            }
        } finally {
            lock.readLock().unlock();
        }

        lock.writeLock().lock();
        try {
            if (!Files.exists(databasePath)) {
                try {
                    Files.createFile(databasePath);
                } catch (IOException e) {
                    return new ArrayList<>();
                }
            }
            return new ArrayList<>();
        } finally {
            lock.writeLock().unlock();
        }
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
