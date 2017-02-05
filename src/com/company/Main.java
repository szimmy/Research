package com.company;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ArrayList<double[]> data = new ArrayList<>();

        double[] point1 = {10, 0};
        double[] point2 = {2, 3};
        double[] point3 = {0, 6};
        double[] point4 = {0, -4};


        data.add(point1);
        data.add(point2);
        data.add(point3);
        data.add(point4);

        Solver2D solver = new Solver2D(data);

        List<double[]> endmembers = solver.run();

        System.out.println("Printing Endmembers: ");
        for(double[] d : endmembers) {
            System.out.println(d[0] + ", " + d[1]);
        }

        data.clear();

        double[] pointa = {6, 6};
        double[] pointb = {4, 3};
        double[] pointc = {0, 4};
        double[] pointd = {4, 0};

        data.add(pointa);
        data.add(pointb);
        data.add(pointc);
        data.add(pointd);

        solver = new Solver2D(data);

        endmembers = solver.run();

        System.out.println("Printing Endmembers: ");
        for(double[] d : endmembers) {
            System.out.println(d[0] + ", " + d[1]);
        }
    }
}
