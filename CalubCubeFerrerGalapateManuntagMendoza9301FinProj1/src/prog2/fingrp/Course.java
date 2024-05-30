/**
 * Reference Class: Used for the curriculum checklist
 */

package prog2.fingrp;

import java.io.*;

public class Course implements Serializable {

    // Instance variables declaration
    int year;
    int term;
    String courseNum;
    String courseDesc;
    int units;

    // Constructor with parameters
    public Course(int year, int term, String courseNum, String courseDesc, int units) {
        this.year = year;
        this.term = term;
        this.courseNum = courseNum;
        this.courseDesc = courseDesc;
        this.units = units;
    }

    // Default constructor
    public Course() {

    }

    // Getters and setters
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    // Overridden toString method for output testing
    @Override
    public String toString() {
        return "Course{" +
                "year=" + year +
                ", term=" + term +
                ", courseNum='" + courseNum + '\'' +
                ", courseDesc='" + courseDesc + '\'' +
                ", units=" + units +
                '}';
    }
}
