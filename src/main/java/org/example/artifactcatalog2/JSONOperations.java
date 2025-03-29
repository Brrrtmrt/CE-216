package org.example.artifactcatalog2;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;


public class JSONOperations {

    public static boolean importJSON(Path file) throws IOException {

        return false;
    }

    public static boolean exportJSON(Path in, Path out) throws IOException {

        return false;
    }

    //Main JSON File for programs usage
    public static boolean writeJSON(Path path, ArrayList<Artifact> list) {
        File out = path.toFile();
        try (FileWriter fw = new FileWriter(out)) {
            String json = serialize(list);
            fw.write(json);
            fw.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        //TEST object
       /* Artifact art = new Artifact("Foo", "Bar", "ManuScript", "İzmir", new ArrayList<>(Arrays.asList("Test", "MS")), "A", LocalDate.of(2025, 10, 10), "İzmir", new Dimension(10, 10, 10), 10000, new ArrayList<>(Arrays.asList("MS", "value")));
        Artifact art2 = new Artifact("test", "test", "ManuScript", "İzmir", new ArrayList<>(Arrays.asList("Test", "MS")), "A", LocalDate.of(2025, 10, 10), "İzmir", new Dimension(10, 10, 10), 10000, new ArrayList<>(Arrays.asList("MS", "value")));
        Artifact art3 = new Artifact("asddsa", "adsdas", "ManuScript", "İzmir", new ArrayList<>(Arrays.asList("Test", "MS")), "A", LocalDate.of(2025, 10, 10), "İzmir", new Dimension(10, 10, 10), 10000, new ArrayList<>(Arrays.asList("MS", "value")));
        */
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

    public static ArrayList<Artifact> deserializeList(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Artifact.class, new ArtifactTypeAdapter()).registerTypeAdapter(Dimension.class, new DimensionTypeAdapter())
                .create();
        Type artifactListType = new TypeToken<ArrayList<Artifact>>() {
        }.getType();
        return gson.fromJson(json, artifactListType);
    }





}
