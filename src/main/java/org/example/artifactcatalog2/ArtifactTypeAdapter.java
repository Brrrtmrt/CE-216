package org.example.artifactcatalog2;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//GSON needs type adapters for object creation
public class ArtifactTypeAdapter extends TypeAdapter<Artifact> {
    private final TypeAdapter<Dimension> dimensionTypeAdapter = new DimensionTypeAdapter();

    @Override
    public void write(JsonWriter out, Artifact artifact) throws IOException {
        out.beginObject();
        out.name("ID").value(artifact.getID());
        out.name("name").value(artifact.getName());
        out.name("category").value(artifact.getCategory());
        out.name("discoveryLocation").value(artifact.getDiscoveryLocation());
        out.name("composition").jsonValue(new Gson().toJson(artifact.getComposition()));
        out.name("civilization").value(artifact.getCivilization());
        out.name("discoveryDate").value(artifact.getDiscoveryDate().toString());
        out.name("currentPlace").value(artifact.getCurrentPlace());
        out.name("dimension");
        dimensionTypeAdapter.write(out, artifact.getDimension());
        out.name("weight").value(artifact.getWeight());
        out.name("tags").jsonValue(new Gson().toJson(artifact.getTags()));
        out.endObject();
    }

    @Override
    public Artifact read(JsonReader in) throws IOException {
        Artifact artifact = new Artifact();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "ID":
                    artifact.setID(in.nextString());
                    break;
                case "name":
                    artifact.setName(in.nextString());
                    break;
                case "category":
                    artifact.setCategory(in.nextString());
                    break;
                case "discoveryLocation":
                    artifact.setDiscoveryLocation(in.nextString());
                    break;
                case "composition":
                    ArrayList<String> compositionList = new Gson().fromJson(in, new TypeToken<List<String>>() {
                    }.getType());
                    artifact.setComposition(compositionList);
                    break;
                case "civilization":
                    artifact.setCivilization(in.nextString());
                    break;
                case "discoveryDate":
                    artifact.setDiscoveryDate(LocalDate.parse(in.nextString()));
                    break;
                case "currentPlace":
                    artifact.setCurrentPlace(in.nextString());
                    break;
                case "dimension":
                    artifact.setDimension(dimensionTypeAdapter.read(in));
                    break;
                case "weight":
                    artifact.setWeight(in.nextLong());
                    break;
                case "tags":
                    ArrayList<String> tagsList = new Gson().fromJson(in, new TypeToken<List<String>>() {
                    }.getType());
                    artifact.setTags(tagsList);
                    break;
            }
        }
        in.endObject();
        return artifact;
    }
}
