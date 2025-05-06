package org.suikasoft.GsonPlus;

import com.google.gson.stream.JsonReader;
import pt.up.fe.specs.util.SpecsCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface JsonReaderParser {

    default String nextName(JsonReader reader, String name) {
        try {
            String actualName = reader.nextName();
            SpecsCheck.checkArgument(actualName.equals(name), () -> "Expected name '" + name + "'");
            return actualName;
        } catch (IOException e) {
            throw new RuntimeException("Could not read string from JSON", e);
        }
    }

    default String nextString(JsonReader reader, String name) {
        nextName(reader, name);
        return nextString(reader);
    }

    default String nextString(JsonReader reader) {
        try {
            return reader.nextString();
        } catch (IOException e) {
            throw new RuntimeException("Could not read string from JSON", e);
        }
    }

    default <T> List<T> nextList(JsonReader reader, String name, Function<JsonReader, T> elementParser) {
        nextName(reader, name);
        return nextList(reader, elementParser);
    }

    default <T> List<T> nextList(JsonReader reader, Function<JsonReader, T> elementParser) {

        try {
            var list = new ArrayList<T>();


            reader.beginArray();

            while (reader.hasNext()) {
                list.add(elementParser.apply(reader));
            }

            reader.endArray();

            return list;
        } catch (IOException e) {
            throw new RuntimeException("Could not read list from JSON", e);
        }
    }
}
