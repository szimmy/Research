package com.company;

/**
 * A class to represent a line.
 *
 * @author Sean Zimmerman
 */
public class Line {

    private boolean isVertical = false;
    private double m = 0;
    private double x1;
    private double y1;

    /**
     * Creates a line given 2 points
     */
    public Line(double[] point1, double[] point2) {
        if(point1[0] != point2[0]) {
            m = (point2[1] - point1[1]) / (point2[0] - point1[0]);
        } else {
            isVertical = true;
        }

        x1 = point1[0];
        y1 = point1[1];
    }

    /**
     * Determine if a given point is "positive" or "negative" with respect to the line (positive meaning above or to the left)
     * @param point The given point
     * @return If the point is to the left or above the given line
     */
    public boolean isPositive(double[] point) {
        if(isVertical) { // Vertical Line
            // If the point is to the left of the vertical line
            return point[0] <= x1;
        } else if(m == 0) { // Horizontal Line
            // If the point is above the horizontal line
            return point[1] >= y1;
        } else { // Normal Line
            // Plug in the given x and compare how the y is to the result (if it is higher than it is above the line)
            double tempY = value(point[0]);
            return point[1] >= tempY;
        }
    }

    /**
     * Calculate the value of the function at a given x
     * @param x The x value to evaluate at
     * @return The y value of the line
     */
    public double value(double x) {
        return m * (x - x1) + y1;
    }
}
