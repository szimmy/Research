package com.company;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Class which takes in a group of 2D points and creates the smallest volume triangle around them as possible.
 *
 * @author Sean Zimmerman
 * @version 1/30/2017
 */
public class Solver2D {
    // Initialize variables
    private final int DIMENSION = 2;
    private final double[] ORIGIN = {0,0};

    private double furthestDistance;
    private int furthestIndex;
    private int foundEndmembers;

    private List<double[]> data;
    private List<double[]> endmembers = new ArrayList<>(3);
    private List<Integer> endmemberIndicies = new ArrayList<>(3);

    /**
     * Constructor, takes in a set of data to draw a triangle around.
     * @param data A List of float arrays which are each size 2 containing an X and a Y coordinate
     */
    public Solver2D(List<double[]> data) {
        this.data = data;
        this.furthestDistance = 0;
        this.furthestIndex = 0;
        this.foundEndmembers = 0;
    }

    public List<double[]> run() {
        // Do some shit, y'know, algorithm stuffs
        findFurthestPointFromOrigin(data);

        // Register the first endmember
        foundEndmembers++;
        endmembers.add(data.get(furthestIndex));
        endmemberIndicies.add(furthestIndex);

        // Get the index of the point that has the largest angle made with the vector from origin to furthest point
        int tempIndex = findLargestAngleWithFurthestPoint(data, data.get(furthestIndex), furthestIndex);

        // This index is for the second endmember (as of now), so register it
        foundEndmembers++;
        endmembers.add(data.get(tempIndex));
        endmemberIndicies.add(tempIndex);

        // Make line equation with furthest point and origin and determine if the second endmember is positive or negative with respect to this line
        Line furthestPointLine = new Line(ORIGIN, data.get(furthestIndex));
        boolean secondEndmemberPositive = furthestPointLine.isPositive(data.get(tempIndex));

        int tempIndexThirdEndmember = findLargestAngleWithFurthestPoint(data, data.get(furthestIndex), furthestIndex, furthestPointLine, !secondEndmemberPositive);

        // Register the third index
        foundEndmembers++;
        endmembers.add(data.get(tempIndexThirdEndmember));
        endmemberIndicies.add(tempIndexThirdEndmember);

        return endmembers;
    }

    /**
     * Private helper method which finds the List index of the point which is the furthest from the origin.
     * @param data The data to run the algorithm on
     */
    private void findFurthestPointFromOrigin(List<double[]> data) {
        // Iterate through all of the data points
        for(int i = 0; i < data.size(); i++) {
            double temp = getDistance(data.get(i), ORIGIN);
            if(temp > furthestDistance) {
                furthestDistance = temp;
                furthestIndex = i;
            }
        }
    }

    /**
     * Private helper method which calculates the distance between 2 points in 2D space.
     * @param data1 The first point
     * @param data2 The second point
     * @return The distance between the two points
     */
    private double getDistance(double[] data1, double[] data2) {
        double x = data1[0] - data2[0];
        double y = data1[1] - data2[1];

        return Math.sqrt(x*x + y*y);
    }

    /**
     * Private helper method which calculates the length of a vector
     * @param data The vector
     * @return The length of the vector
     */
    private double getDistance(double[] data) {
        return Math.sqrt(data[0]*data[0] + data[1]*data[1]);
    }

    /**
     * Private helper method which finds the index of the point that has the largest angle made with the vector from origin to furthest point
     * @param data The data set
     * @param furthestPoint Array representing the x and y coordinates of the furthest point (point we are finding the angle with). THis point is technically a vector made with the origin
     * @param furthestIndex Index of the furthest point in the data array
     * @return The index of the point that has the largest angle made with the vector from origin to furthest point
     */
    private int findLargestAngleWithFurthestPoint(List<double[]> data, double[] furthestPoint, int furthestIndex) {
        int currentLargestAngleIndex = 0;
        double currentSmallestCos = 1;

        for(int i = 0; i < data.size(); i++) {
            if(i != furthestIndex) { // Do not check the furthest point
                double temp = calculateCosAngle(furthestPoint, data.get(i));
                // IMPROVEMENT: If two points have the same (largest) angle away then take the one that is further from the furthest point, this will work for now
                if(temp < currentSmallestCos) {
                    currentSmallestCos = temp;
                    currentLargestAngleIndex = i;
                }
            }
        }

        return currentLargestAngleIndex;
    }

    /**
     * Private helper method which finds the index of the point that has the largest angle made with the vector from origin to furthest point
     * @param data The data set
     * @param furthestPoint Array representing the x and y coordinates of the furthest point (point we are finding the angle with). THis point is technically a vector made with the origin
     * @param furthestIndex Index of the furthest point in the data array
     * @param line The line from the origin to the furthest point
     * @param positive If we are looking for a point that is positive with respect to line
     * @return The index of the point that has the largest angle made with the vector from origin to furthest point that is on the specified side of the line
     */
    private int findLargestAngleWithFurthestPoint(List<double[]> data, double[] furthestPoint, int furthestIndex, Line line, boolean positive) {
        int currentLargestAngleIndex = 0;
        double currentSmallestCos = 1;

        for(int i = 0; i < data.size(); i++) {
            if(i != furthestIndex) { // Do not check the furthest point
                if(line.isPositive(data.get(i)) == positive) { // Only check points that are on the correct side of the line
                    double temp = calculateCosAngle(furthestPoint, data.get(i));
                    // IMPROVEMENT: If two points have the same (largest) angle away then take the one that is further from the furthest point, this will work for now
                    if(temp < currentSmallestCos) {
                        currentSmallestCos = temp;
                        currentLargestAngleIndex = i;
                    }
                }
            }
        }

        return currentLargestAngleIndex;
    }

    /**
     * Find the angle between two vectors, the first vector is from the origin to a and the second is from a to b
     * @param a The first point
     * @param b The second point
     * @return The cosine of the angle between the two vectors (a - origin) and (b - a)
     */
    private double calculateCosAngle(double[] a, double[] b) {
        double[] vectorB = {b[0] - a[0], b[1] - a[1]};
        double dot = dotProduct(a, vectorB);
        double denom = getDistance(a) * getDistance(vectorB);

        // Cos(a) = (AdotB) / (|A||B|)
        return Math.abs(dot / denom);
    }

    /**
     * Private helper method which does the dot product for 2 vectors a and b
     * @param a The first vector
     * @param b The second vector
     * @return The dot product between the vectors
     */
    private double dotProduct(double[] a, double[] b) {
        return a[0]*b[0] + a[1]*b[1];
    }
}
