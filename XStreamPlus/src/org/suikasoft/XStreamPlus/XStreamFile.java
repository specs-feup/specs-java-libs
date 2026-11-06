/* Copyright 2011 SPeCS Research Group. Licensed under the Apache License, Version 2.0. */
package org.suikasoft.XStreamPlus;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.node.ObjectNode;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Historical XML API, now backed by versioned Jackson JSON. Old XML must be
 * converted with scripts/convert-xstream-config.mjs before loading. Bean types
 * need a no-argument constructor or a Jackson creator. Configuration files are
 * trusted local input, like executable command-line options.
 */
public class XStreamFile<T> {
    public static final List<String> reservedAlias = List.of("string", "int");
    private final ObjectXml<T> config;
    private final ConfigModule module = new ConfigModule();
    public boolean useCompactRepresentation;

    public XStreamFile(ObjectXml<T> config) {
        this.config = Objects.requireNonNull(config);
    }

    public static <T> XStreamFile<T> newInstance(ObjectXml<T> config) {
        return new XStreamFile<>(config);
    }

    public void setUseCompactRepresentation(boolean compact) {
        useCompactRepresentation = compact;
    }

    /** Aliases remain API metadata; the new format uses Jackson type identifiers. */
    public void addAlias(String name, Class<?> type) {
        Objects.requireNonNull(type, "Class for alias cannot be null");
    }

    public <V> void registerCodec(Class<V> type, StringCodec<V> codec) {
        module.addCodec(Objects.requireNonNull(type), Objects.requireNonNull(codec));
    }

    private ObjectMapper mapper() {
        return JsonMapper.builder()
                .visibility(PropertyAccessor.ALL, Visibility.NONE)
                .visibility(PropertyAccessor.FIELD, Visibility.ANY)
                .disable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS)
                .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
                .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                .enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION)
                .addModule(module)
                .activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                        ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY)
                .build();
    }

    public String toXml(Object object) {
        if (object != null && !config.getTargetClass().isInstance(object)) {
            SpecsLogs.warn("Object is not compatible with " + config.getTargetClass().getName());
            return null;
        }
        try {
            var mapper = mapper();
            var document = mapper.createObjectNode();
            document.put("format", "specs-config");
            document.put("version", 1);
            // Always use Object as the declared root type, including scalar roots.
            document.set("value", mapper.readTree(mapper.writerFor(Object.class).writeValueAsString(object)));
            return (useCompactRepresentation ? mapper.writer() : mapper.writerWithDefaultPrettyPrinter())
                    .writeValueAsString(document);
        } catch (IOException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Could not serialize configuration", e);
        }
    }

    public T fromXml(String contents) {
        Objects.requireNonNull(contents, "Configuration contents cannot be null");
        if (contents.stripLeading().startsWith("<")) {
            throw new IllegalArgumentException("Legacy XStream XML is no longer supported. Convert this file with "
                    + "scripts/convert-xstream-config.mjs INPUT OUTPUT before loading it.");
        }
        try {
            var mapper = mapper();
            var document = mapper.readTree(contents);
            if (!(document instanceof ObjectNode) || document.size() != 3
                    || !"specs-config".equals(document.path("format").asText())
                    || !document.path("version").isIntegralNumber() || !document.path("version").canConvertToInt() || document.path("version").intValue() != 1
                    || !document.has("value")) {
                throw new IllegalArgumentException("Expected a specs-config document with version 1");
            }
            var value = mapper.readerFor(Object.class).readValue(document.get("value"));
            if (value == null) {
                return null;
            }
            if (!config.getTargetClass().isInstance(value)) {
                SpecsLogs.warn("Configuration is not compatible with " + config.getTargetClass().getName());
                return null;
            }
            return config.getTargetClass().cast(value);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read configuration: " + e.getMessage(), e);
        }
    }
}
