package org.example.artifactcatalog2;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

//GSON needs type adapters for object creation
public class DimensionTypeAdapter extends TypeAdapter<Dimension> {
    @Override
    public void write(JsonWriter out, Dimension dimension) throws IOException {
        if (dimension == null) {
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name("width").value(dimension.getWidth());
        out.name("length").value(dimension.getLength());
        out.name("height").value(dimension.getHeight());
        out.endObject();
    }

    @Override
    public Dimension read(JsonReader in) throws IOException {
        Dimension dimension = new Dimension();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "width":
                    dimension.setWidth(in.nextLong());
                    break;
                case "length":
                    dimension.setLength(in.nextLong());
                    break;
                case "height":
                    dimension.setHeight(in.nextLong());
                    break;
            }
        }
        in.endObject();
        return dimension;
    }
}
