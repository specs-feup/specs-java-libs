package pt.up.fe.specs.asmparser;

import com.google.gson.Gson;
import pt.up.fe.specs.asmparser.ast.FieldNode;
import pt.up.fe.specs.asmparser.ast.RuleNode;
import pt.up.fe.specs.asmparser.parser32bit.Asm32bitParser;
import pt.up.fe.specs.util.SpecsIo;

import java.util.*;

public class Isa32bitParser {

    public static void main(String[] args) {
        var isaParser = newInstance(SpecsIo.getResource(() -> "pt/up/fe/specs/binarytranslation/asm/parsing/asm_test.json"));

        //"0110_xx_registerd(5)_1000_opcode(1)_0_imm(15)"
        // 31, 1, 10925
        var decoded = isaParser.parse(Long.parseLong("01100011111100010010101010101101", 2));
        System.out.println(Arrays.toString(decoded));
    }


    private final List<Asm32bitParser> parsers;
    private final Map<String, Integer> fieldsMap;

    private Isa32bitParser(List<Asm32bitParser> parsers, Map<String, Integer> fieldsMap) {
        this.parsers = parsers;
        this.fieldsMap = fieldsMap;
    }


    public static Isa32bitParser newInstance(String jsonContents) {
        var isa = new Gson().fromJson(jsonContents, Map.class);

        //var fields = new LinkedHashSet<>((List<String>) isa.get("fields"));

        // Maps a field to an index
        var fieldsMap = new HashMap<String, Integer>();

        // Index 0 is id of format
        var fieldId = 1;
        for (var field : (List<String>) isa.get("fields")) {
            fieldsMap.put(field, fieldId);
            fieldId++;
        }

//        System.out.println("FIELDS MAP: " + fieldsMap);

        int id = 0;
        var parsers = new ArrayList<Asm32bitParser>();
        for (var format : (List<String>) isa.get("formats")) {

            // Parse format
            var rule = new InstructionFormatParser().parse(format);

            // Verify rule
            verify(rule, format, fieldsMap.keySet());

            // Create parser
            var parser = Asm32bitParser.build(id, rule, fieldsMap);
            parsers.add(parser);
            id++;
        }


        return new Isa32bitParser(parsers, fieldsMap);
    }

    private static void verify(RuleNode rule, String format, Set<String> fields) {
        // Go to all fields, check they are declared
        var invalidField = rule.getDescendants(FieldNode.class).stream()
                .filter(node -> !fields.contains(node.getField()))
                .findFirst()
                .orElse(null);


        if (invalidField != null) {
            throw new RuntimeException("Found undeclared field '" + invalidField.getField() + "' in format rule '" + format + "'");
        }

    }

    public Map<String, Integer> getFieldsMap() {
        return fieldsMap;
    }

    public int[] parse(long instruction) {
        // Iterate over all parsers, looking for one that accepts the instruction
        for (var parser : parsers) {
            var decoded = parser.parse(instruction);

            // Could not decode, skip
            if (decoded == null) {
                continue;
            }

            // Decoded, return
            return decoded;
        }

        throw new RuntimeException("Could not decode instruction 0x" + Long.toString(instruction, 16));
    }

}
