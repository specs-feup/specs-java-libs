package pt.up.fe.specs.util.treenode.utils;

import pt.up.fe.specs.util.treenode.TreeNode;
import pt.up.fe.specs.util.treenode.TreeNodeWalker;

public class DottyGenerator<K extends TreeNode<K>> extends TreeNodeWalker<K> {

    // TODO: generation options?

    private final StringBuilder dotty;

    private DottyGenerator() {
        dotty = new StringBuilder();
    }

    public String getDotty() {
        return dotty.toString();
    }

    public static <K extends TreeNode<K>> String buildDotty(K node) {
        var dottyGen = new DottyGenerator<K>();
        dottyGen.generateDotty(node);
        return dottyGen.getDotty();
    }

    @Override
    public void visit(K node) {

        // this node name
        var me = node.toContentString();
        if (me == null || me.isBlank()) {
            me = node.getNodeName();
        }

        var tagname = node.hashCode();

        // my label
        dotty.append(tagname + "[shape = box, label = \"" + me.replace("\n", "\\l") + "\"];\n");

        // my children
        for (var kid : node.getChildren())
            dotty.append(tagname + " -> " + kid.hashCode() + ";\n");

        // visit children
        super.visit(node);
    }

    /*
     * Puts each node in a dotty node and prints everything!
     */
    public void generateDotty(K rootNode) {

        dotty.append("digraph D {\n");
        this.visit(rootNode);
        dotty.append("}\n");
    }

    // TODO: overload generateDotty with output file argument
}
