package org.suikasoft.XStreamPlus;

import static org.assertj.core.api.Assertions.*;
import java.io.File;
import java.math.*;
import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pt.up.fe.specs.util.utilities.StringList;

class ConfigFormatTest {
    static Stream<Object> values() {
        return Stream.of("", " <&>\"\n\t\r\u0000Olá 😀 ", true, false, 0, Integer.MIN_VALUE, Integer.MAX_VALUE,
                Long.MIN_VALUE, Long.MAX_VALUE, (byte) -128, (short) 32767, 1.25f, -0.0, Double.NaN,
                Double.POSITIVE_INFINITY, new BigInteger("123456789012345678901234567890"),
                new BigDecimal("12345678901234567890.0000012300"), new File("../../relative path/é"),
                Optional.empty(), Optional.of(""), Optional.of(new File("../f")),
                Optional.of(new ArrayList<>(List.of("a", 1, "b"))), new ArrayList<>(), new HashMap<>(),
                new ArrayList<>(Arrays.asList("a", 1, "b", null, true, 2L, "a")),
                new HashMap<>(Map.of(7, "seven", "7", 7, true, new File("./a"))),
                new LinkedHashMap<>(Map.of("name", Optional.of(new StringList(List.of("", "a;b", "é"))))),
                new TreeMap<>(Map.of(1, "one", 2, "two")), new HashSet<>(List.of("a", 3)),
                new StringList(List.of("", "a;b", "😀", "<>&")), Kind.ONE,
                new int[] {1, 2, Integer.MIN_VALUE}, new String[] {"a", "", null, "b"},
                new Object[] {"a", 1, "b", null}, new boolean[] {true, false});
    }
    @ParameterizedTest @MethodSource("values")
    void roundTripsWithoutChangingValueOrType(Object original) {
        Object copy = XStreamUtils.from(XStreamUtils.toString(original), Object.class);
        assertThat(copy).isExactlyInstanceOf(original.getClass());
        if (original instanceof StringList strings) {
            assertThat(((StringList) copy).getStringList()).isEqualTo(strings.getStringList());
        } else if (original instanceof Double || original instanceof Float || original instanceof Number) {
            assertThat(copy).isEqualTo(original);
        } else {
            assertThat(copy).usingRecursiveComparison().isEqualTo(original);
        }
    }
    @Test void preservesNullAndRejectsLegacyAndInvalidDocuments() {
        assertThat(XStreamUtils.from(XStreamUtils.toString(null), Object.class)).isNull();
        for (String bad : List.of("<SimpleDataStore/>", "", "{}", "null", "[]",
                "{\"format\":\"specs-config\",\"version\":2,\"value\":null}",
                "{\"format\":\"specs-config\",\"version\":4294967297,\"value\":null}",
                "{\"format\":\"specs-config\",\"version\":1.0,\"value\":null}",
                XStreamUtils.toString(1) + "{}",
                "{\"format\":\"specs-config\",\"version\":1,\"value\":1,\"value\":2}")) {
            assertThatThrownBy(() -> XStreamUtils.from(bad, Object.class)).as(bad)
                    .isInstanceOf(RuntimeException.class);
        }
        assertThatThrownBy(() -> XStreamUtils.from("<SimpleDataStore/>", Object.class))
                .hasMessageContaining("convert-xstream-config");
    }
    @Test void doesNotSkipInvalidBeanFields() {
        String text = XStreamUtils.toString(new Bean());
        assertThatThrownBy(() -> XStreamUtils.from(text.replace("\"count\" : 42", "\"count\" : \"bad\""), Bean.class))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> XStreamUtils.from(text.replace("\"count\" : 42", "\"count\" : null"), Bean.class))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> XStreamUtils.from(text.replace("\"count\"", "\"unknown\""), Bean.class))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @Test void mapsRejectInvalidPairsAndDuplicateKeys() {
        for (String entries : List.of("[[1,2,3]]", "[[1]]", "[[1,2],[1,3]]", "[3]", "{}")) {
            String text = "{\"format\":\"specs-config\",\"version\":1,\"value\":[\"java.util.HashMap\"," + entries + "]}";
            assertThatThrownBy(() -> XStreamUtils.from(text, Object.class)).isInstanceOf(RuntimeException.class);
        }
    }
    @Test void optionalRejectsMalformedData() {
        for (String value : List.of("{}", "{\"present\":1}", "{\"present\":false,\"value\":1}",
                "{\"present\":true}", "{\"present\":true,\"value\":null}")) {
            String text = "{\"format\":\"specs-config\",\"version\":1,\"value\":[\"java.util.Optional\"," + value + "]}";
            assertThatThrownBy(() -> XStreamUtils.from(text, Object.class)).isInstanceOf(RuntimeException.class);
        }
    }
    @Test void seededHeterogeneousCollectionsPreserveOrderAndMapKeyTypes() {
        Random random = new Random(229);
        for (int run = 0; run < 100; run++) {
            var list = new ArrayList<Object>();
            for (int i = 0; i < 50; i++) {
                list.add(switch(random.nextInt(5)) {
                    case 0 -> random.nextInt(); case 1 -> random.nextLong(); case 2 -> "x" + random.nextInt(5);
                    case 3 -> null; default -> random.nextBoolean();
                });
            }
            assertThat(XStreamUtils.copy(list)).isEqualTo(list);
            var map = new LinkedHashMap<Object, Object>();
            for (int i = 0; i < list.size(); i++) map.put(i % 2 == 0 ? i : "key" + i, list.get(i));
            assertThat(XStreamUtils.copy(map)).isEqualTo(map);
        }
    }
    @Test void immutableJdkContainersCanBeSavedWithoutDependingOnTheirPrivateImplementation() {
        for (Object value : List.of(Map.of(), Map.of("x", 7), Collections.singletonMap("x", 7),
                Collections.unmodifiableMap(Map.of("x", 7)), List.of("a", "b"), Collections.emptyList(), Set.of("a", "b"))) {
            Object copy = XStreamUtils.from(XStreamUtils.toString(value), Object.class);
            assertThat(copy).isEqualTo(value);
        }
    }
    @Test void malformedCustomCodecsAndUnsupportedGraphsFailExplicitly() {
        String file = XStreamUtils.toString(new File("x"));
        assertThatThrownBy(() -> XStreamUtils.from(file.replace("\"x\"", "123"), Object.class))
                .isInstanceOf(RuntimeException.class);
        Cycle cycle = new Cycle(); cycle.next = cycle;
        assertThatThrownBy(() -> XStreamUtils.toString(cycle)).isInstanceOf(RuntimeException.class);
    }
    public static class Cycle { public Cycle next; public Cycle() {} }
    public enum Kind { ONE }
    public static class Bean { public int count = 42; public Bean() {} }
}
