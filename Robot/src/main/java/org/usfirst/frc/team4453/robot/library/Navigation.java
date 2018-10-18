package org.usfirst.frc.team4453.robot.library;

public class Navigation {

    public static double calculateDist(double x1, double y1, double x2, double y2){
	double x = x1 - x2;
	double y = y1 - y2;
	double dist = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	return dist;
    }
    
    public static double calculateAngle (double x1, double y1, double x2, double y2){
	double x = x2 - x1;
	double y = y2 - y1;
	double dangle = 90 - Math.toDegrees(Math.atan2(y, x));
	return dangle;
    }
}