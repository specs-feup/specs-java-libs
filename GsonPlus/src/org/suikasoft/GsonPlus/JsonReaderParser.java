package org.suikasoft.GsonPlus;

import com.google.gson.stream.JsonReader;
import pt.up.fe.specs.util.SpecsCheck;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public interface JsonReaderParser {

    default Object nextValue(JsonReader reader, String name) {
        nextName(reader, name);
        return nextValue(reader);
    }

    //    default Optional<Object> nextValueTry(JsonReader reader) {
    default Object nextValue(JsonReader reader) {
        try {
            var next = reader.peek();
            return switch (next) {
                case STRING -> Optional.of(reader.nextString());
                case BOOLEAN -> Optional.of(reader.nextBoolean());
                case NUMBER -> Optional.of(reader.nextDouble());
                case BEGIN_ARRAY -> Optional.of(nextList(reader));
                case BEGIN_OBJECT -> Optional.of(nextObject(reader));
                //case NULL -> nextNull(reader); // If lenient mode is active nulls can appear after trailing comma
                default -> throw new RuntimeException("Case not defined at " + reader.getPath() + ": " + next);
            };

        } catch (IOException e) {
            throw new RuntimeException("Could not read value from JSON", e);
        }
    }

    default Optional<Object> nextNull(JsonReader reader) {
        try {
            reader.nextNull();
            return Optional.empty();
        } catch (IOException e) {
            throw new RuntimeException("Could not read value from JSON", e);
        }
    }

    /*
        default Object nextValue(JsonReader reader) {
            return nextValueTry(reader).get();
        }
    */
    default String nextName(JsonReader reader) {
        try {
            return reader.nextName();
        } catch (IOException e) {
            throw new RuntimeException("Could not read string from JSON", e);
        }
    }

    default String nextName(JsonReader reader, String name) {
        var actualName = nextName(reader);
        SpecsCheck.checkArgument(actualName.equals(name), () -> "Expected name '" + name + "'");
        return actualName;
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

    default List<Object> nextList(JsonReader reader) {
        try {
            var list = new ArrayList<>();


            reader.beginArray();

            while (reader.hasNext()) {
                list.add(nextValue(reader));
                //nextValueTry(reader).ifPresent(list::add);
            }

            reader.endArray();

            return list;
        } catch (IOException e) {
            throw new RuntimeException("Could not read list from JSON", e);
        }
    }

    default Map<String, Object> nextObject(JsonReader reader) {
        try {
            var map = new HashMap<String, Object>();


            reader.beginObject();

            while (reader.hasNext()) {
/*
                // To handle malformed jsons
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    break;
                }
*/
                var key = reader.nextName();
                //var value = nextValueTry(reader).orElseThrow(() -> new RuntimeException("Expected an object after '" + key + "'"));
                var value = nextValue(reader);
                map.put(key, value);
            }

            reader.endObject();

            return map;
        } catch (IOException e) {
            throw new RuntimeException("Could not read list from JSON", e);
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

    /**
     * Assumes we are inside an object, and there are only key-value strings until the end
     *
     * @param reader
     * @return
     */
    default public Map<String, String> fillMap(JsonReader reader) {
        try {
            var attrs = new HashMap<String, String>();
            while (reader.hasNext()) {

                var key = reader.nextName();
                var value = nextString(reader);

                attrs.put(key, value);
            }

            return attrs;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default public String getString(Map<String, Object> data, String key) {
        if (!data.containsKey(key)) {
            throw new RuntimeException("Could not find key '" + key + "': " + data);
        }

        return data.get(key).toString();
    }
}
