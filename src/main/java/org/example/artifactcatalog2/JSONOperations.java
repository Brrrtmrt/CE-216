package org.example.artifactcatalog2;

import com.google.gson.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class JSONOperations {

    public static void main(String[] args) {
        //TEST object
        Artifact art=new Artifact("Foo","Bar", "ManuScript","İzmir",new ArrayList<>(Arrays.asList("Test","MS")),"A", LocalDate.of(2025,10,10),"İzmir",new Dimension(10,10,10),10000,new ArrayList<>(Arrays.asList("MS","value")));

        //Serializing process
        JSONOperations jsonObj = new JSONOperations();
        String res = jsonObj.serialize(art);
        System.out.println(res);

        //Deserializing process
        Artifact a = jsonObj.deserialize(res);
        System.out.println(a);

    }

    public String serialize(Artifact artifact) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Artifact.class, new ArtifactTypeAdapter()).registerTypeAdapter(Dimension.class, new DimensionTypeAdapter())
                .create();
        return gson.toJson(artifact);

    }

    public Artifact deserialize(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Artifact.class, new ArtifactTypeAdapter()).registerTypeAdapter(Dimension.class, new DimensionTypeAdapter())
                .create();
        return gson.fromJson(json, Artifact.class);
    }
}
