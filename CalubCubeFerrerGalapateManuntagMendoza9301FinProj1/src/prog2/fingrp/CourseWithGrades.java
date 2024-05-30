/**
 * This class is subclass of the Course reference class, and it inherits the attributes then adds the following variables:
 *      isTaken - boolean variable to determine if the Course object is taken; initialized as false for later modifications.
 *      grade - if the isTaken variable is true, return the grade value instead.
 */

package prog2.fingrp;

import java.io.Serializable;

public class CourseWithGrades extends Course implements Serializable {

    boolean isTaken = false, outsideCurriculum = false, elective = false;
    int grade;
    String legend;

    public CourseWithGrades(int year, int term, String courseNum, String courseDesc, int units, boolean isTaken, int grade, String legend, boolean elective) {
        super(year, term, courseNum, courseDesc, units);
        this.isTaken = isTaken;
        this.grade = grade;
        this.legend = legend;
        this.elective = elective;
    }

    public CourseWithGrades() {

    }

    public boolean isTaken() {
        return isTaken;
    }

    public void setTaken(boolean taken) {
        isTaken = taken;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public boolean isOutsideCurriculum() {
        return outsideCurriculum;
    }

    public void setOutsideCurriculum(boolean outsideCurriculum) {
        this.outsideCurriculum = outsideCurriculum;
    }

    public boolean isElective() {
        return elective;
    }

    public void setElective(boolean elective) {
        this.elective = elective;
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
                ", isTaken=" + isTaken +
                ", grade=" + grade +
                ", legend='" + legend + '\'' +
                ", outsideCurriculum=" + outsideCurriculum +
                ", elective=" + elective +
                '}';
    }
}
