/* Copyright 2026 SPeCS Research Group. Licensed under the Apache License, Version 2.0. */
package org.suikasoft.XStreamPlus;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import pt.up.fe.specs.util.parsing.StringCodec;
import pt.up.fe.specs.util.utilities.StringList;

/** Small Jackson adapters for configuration values that have no bean representation. */
final class ConfigModule extends SimpleModule {
    @SuppressWarnings({"unchecked", "rawtypes"})
    ConfigModule() {
        addSerializer((Class) Map.class, new EntriesSerializer());
        addDeserializer((Class) Map.class, new EntriesDeserializer(Map.class));
        addCodec(java.math.BigDecimal.class, StringCodec.newInstance(java.math.BigDecimal::toString, java.math.BigDecimal::new));
        addCodec(java.math.BigInteger.class, StringCodec.newInstance(java.math.BigInteger::toString, java.math.BigInteger::new));
        addCodec(File.class, StringCodec.newInstance(File::getPath, File::new));
        addSerializer(StringList.class, new JsonSerializer<StringList>() {
            @Override public void serialize(StringList value, JsonGenerator g, SerializerProvider p) throws IOException {
                g.writeStartArray();
                for (String item : value.getStringList()) { g.writeString(item); }
                g.writeEndArray();
            }
            @Override public void serializeWithType(StringList value, JsonGenerator g, SerializerProvider p,
                    TypeSerializer t) throws IOException {
                var id = t.writeTypePrefix(g, t.typeId(value, JsonToken.START_ARRAY));
                for (String item : value.getStringList()) { g.writeString(item); }
                t.writeTypeSuffix(g, id);
            }
        });
        addDeserializer(StringList.class, new JsonDeserializer<StringList>() {
            @Override public StringList deserialize(JsonParser p, DeserializationContext c) throws IOException {
                var values = c.readValue(p, String[].class);
                return new StringList(java.util.Arrays.asList(values));
            }
        });
        addSerializer(Optional.class, new JsonSerializer<Optional>() {
            @Override public void serialize(Optional value, JsonGenerator g, SerializerProvider p) throws IOException {
                g.writeStartObject(); write(value, g, p); g.writeEndObject();
            }
            private void write(Optional value, JsonGenerator g, SerializerProvider p) throws IOException {
                g.writeBooleanField("present", value.isPresent());
                if (value.isPresent()) { g.writeFieldName("value"); writeObject(value.get(), g, p); }
            }
            @Override public void serializeWithType(Optional value, JsonGenerator g, SerializerProvider p,
                    TypeSerializer t) throws IOException {
                var id = t.writeTypePrefix(g, t.typeId(value, JsonToken.START_OBJECT));
                write(value, g, p); t.writeTypeSuffix(g, id);
            }
        });
        addDeserializer(Optional.class, new JsonDeserializer<Optional>() {
            @Override public Optional deserialize(JsonParser p, DeserializationContext c) throws IOException {
                var n = c.readTree(p);
                if (!n.path("present").isBoolean() || n.size() != (n.path("present").booleanValue() ? 2 : 1)
                        || (n.path("present").booleanValue() && (!n.has("value") || n.get("value").isNull()))) {
                    return (Optional) c.handleUnexpectedToken(Optional.class, p);
                }
                return n.path("present").booleanValue()
                        ? Optional.of(c.readTreeAsValue(n.get("value"), Object.class)) : Optional.empty();
            }
        });
    }

    @Override public void setupModule(SetupContext context) {
        super.setupModule(context);
        context.addBeanDeserializerModifier(new com.fasterxml.jackson.databind.deser.BeanDeserializerModifier() {
            @Override public JsonDeserializer<?> modifyMapDeserializer(
                    com.fasterxml.jackson.databind.DeserializationConfig config,
                    com.fasterxml.jackson.databind.type.MapType type,
                    com.fasterxml.jackson.databind.BeanDescription description, JsonDeserializer<?> original) {
                return new EntriesDeserializer(type.getRawClass());
            }
        });
    }

    private static void writeObject(Object value, JsonGenerator g, SerializerProvider p) throws IOException {
        if (value == null) { g.writeNull(); return; }
        p.findValueSerializer(value.getClass()).serializeWithType(value, g, p,
                p.findTypeSerializer(p.constructType(Object.class)));
    }

    // JSON object keys are strings. Entry pairs preserve arbitrary Java keys and their types.
    private static class EntriesSerializer extends JsonSerializer<Map<?, ?>> {
        @Override public void serialize(Map<?, ?> value, JsonGenerator g, SerializerProvider p) throws IOException {
            g.writeStartArray(); write(value, g, p); g.writeEndArray();
        }
        private void write(Map<?, ?> value, JsonGenerator g, SerializerProvider p) throws IOException {
            for (var entry : value.entrySet()) {
                g.writeStartArray(); writeObject(entry.getKey(), g, p); writeObject(entry.getValue(), g, p);
                g.writeEndArray();
            }
        }
        @Override public void serializeWithType(Map<?, ?> value, JsonGenerator g, SerializerProvider p,
                TypeSerializer t) throws IOException {
            // JDK immutable/view maps have no reconstructible constructor. Persist their entries in a stable map type.
            Class<?> type = value.getClass();
            if (type.getName().startsWith("java.util.") && !java.lang.reflect.Modifier.isPublic(type.getModifiers())) {
                type = java.util.LinkedHashMap.class;
            }
            var id = t.writeTypePrefix(g, t.typeId(value, type, JsonToken.START_ARRAY));
            write(value, g, p); t.writeTypeSuffix(g, id);
        }
    }

    private static class EntriesDeserializer extends JsonDeserializer<Map<?, ?>> implements ContextualDeserializer {
        private final Class<?> type;
        EntriesDeserializer(Class<?> type) { this.type = type; }
        @Override public JsonDeserializer<?> createContextual(DeserializationContext c, BeanProperty property)
                throws JsonMappingException {
            var contextual = c.getContextualType();
            return new EntriesDeserializer(contextual == null ? Map.class : contextual.getRawClass());
        }
        @SuppressWarnings("unchecked")
        @Override public Map<?, ?> deserialize(JsonParser p, DeserializationContext c) throws IOException {
            Map<Object, Object> result;
            try {
                result = type == java.util.SortedMap.class || type == java.util.NavigableMap.class ? new java.util.TreeMap<>()
                        : type == Map.class ? new java.util.LinkedHashMap<>()
                        : (Map<Object, Object>) type.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw JsonMappingException.from(p, "Map type needs a public no-argument constructor: " + type, e);
            }
            if (!p.isExpectedStartArrayToken()) { return (Map<?, ?>) c.handleUnexpectedToken(Map.class, p); }
            while (p.nextToken() != JsonToken.END_ARRAY) {
                if (!p.isExpectedStartArrayToken()) { return (Map<?, ?>) c.handleUnexpectedToken(Map.class, p); }
                p.nextToken(); Object key = c.readValue(p, Object.class);
                p.nextToken(); Object value = c.readValue(p, Object.class);
                if (p.nextToken() != JsonToken.END_ARRAY) { return (Map<?, ?>) c.handleUnexpectedToken(Map.class, p); }
                if (result.containsKey(key)) { throw JsonMappingException.from(p, "Duplicate configuration map key: " + key); }
                result.put(key, value);
            }
            return result;
        }
    }

    <T> void addCodec(Class<T> type, StringCodec<T> codec) {
        addSerializer(type, new JsonSerializer<T>() {
            @Override public void serialize(T value, JsonGenerator g, SerializerProvider p) throws IOException {
                g.writeString(codec.encode(value));
            }
            @Override public void serializeWithType(T value, JsonGenerator g, SerializerProvider p,
                    TypeSerializer t) throws IOException {
                WritableTypeId id = t.writeTypePrefix(g, t.typeId(value, JsonToken.VALUE_STRING));
                serialize(value, g, p); t.writeTypeSuffix(g, id);
            }
        });
        addDeserializer(type, new JsonDeserializer<T>() {
            @Override public T deserialize(JsonParser p, DeserializationContext c) throws IOException {
                if (!p.hasToken(JsonToken.VALUE_STRING)) { return (T) c.handleUnexpectedToken(type, p); }
                return codec.decode(p.getText());
            }
        });
    }
}
