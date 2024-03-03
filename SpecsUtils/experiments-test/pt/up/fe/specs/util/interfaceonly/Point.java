package pt.up.fe.specs.util.interfaceonly;

import pt.up.fe.specs.util.SpecsCheck;

public interface Point {

    int[] coords();

    default int dims() {
        return coords().length;
    }

    default int coord(int index) {
        return coords()[index];
    }

    default double dist(Point p) {
        SpecsCheck.checkArgument(p.dims() == this.dims(), () -> "Diff dimensions");

        double acc = 0;
        for (int i = 0; i < this.dims(); i++) {
            acc += Math.pow(this.coord(i) - p.coord(i), 2);
        }

        return Math.sqrt(acc);
    }

    public static void main(String[] args) {

        Point p1 = () -> new int[] { 1, 3 };
        Point p2 = () -> new int[] { 1, 4 };

        System.out.println("Dist: " + p1.dist(p2));

        test('0');
    }

    public static void test(char a) {

    }

}
