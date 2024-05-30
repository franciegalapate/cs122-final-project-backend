package prog2.fingrp;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        showChecklist();
    }

    // Method for the main menu
    public static void showChecklist() {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            while (true) {
                System.out.println("\nPress [ENTER] to show Main Menu");
                if (br.readLine() != null) {
                    System.out.println("My Checklist Management\n" +
                            "<1> Show subjects for each school term\n" +
                            "<2> Show subjects with grades for each term\n" +
                            "<3> Enter grades for subjects recently finished\n" +
                            "<4> Edit a course\n" +
                            "<5> Add other courses taken\n" +
                            "<6> Display courses with Grade Point Average\n" +
                            "<7> Display courses in alphabetical order\n" +
                            "<8> Display courses with grades from Highest to Lowest\n" +
                            "<9> Reset information\n" +
                            "<10> Exit program");
                    System.out.print("\nInput a number corresponding to your choice: ");
                    int choice = Integer.parseInt(br.readLine());
                    switch (choice) {
                        case 1:
                            System.out.println("\nYou have selected: [" + choice + ". Show subjects for each school term.]");
                            displayCourses("courselist.dat");
                            break;
                        case 2:
                            System.out.println("\nYou have selected: [" + choice + ". Show subjects with grades for each term.]");
                            displayCoursesWithGrades("courselist_grade.dat");
                            break;
                        case 3:
                            System.out.println("\nYou have selected: [" + choice + ". Enter grades for subjects recently finished.]");
                            enterGrades("courselist_grade.dat");
                            break;
                        case 4:
                            System.out.println("\nYou have selected: [" + choice + ". Edit a course.]");
                            editCourse("courselist_grade.dat");
                            break;
                        case 5:
                            System.out.println("\nYou have selected: [" + choice + ". Add other courses taken.]");
                            addOtherCoursesTaken("courselist_grade.dat");
                            break;
                        case 6:
                            System.out.println("\nYou have selected: [" + choice + ". Display courses with Grade Point Average.]");
                            displayGPA("courselist_grade.dat");
                            break;
                        case 7:
                            System.out.println("\nYou have selected: [" + choice + ". Display courses in alphabetical order.]");
                            displayCoursesAlphabetical("courselist_grade.dat");
                            break;
                        case 8:
                            System.out.println("\nYou have selected: [" + choice + ". Display courses with grades from Highest to Lowest.]");
                            showHighestToLowestGrades("courselist_grade.dat");
                            break;
                        case 9:
                            System.out.println("\nYou have selected: [" + choice + ". Reset information.]");
                            resetData("courselist.dat");
                            break;
                        case 10:
                            System.out.println("\nYou have selected: [" + choice + ". Exit program.]");
                            System.out.println("Exiting program... Program terminated.");
                            System.exit(0);
                            break;
                        default:
                            System.out.println("\nChoice invalid.");
                            break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            System.out.println("\nPlease enter a valid number.");
            showChecklist(); // Calls showChecklist method again if user inputs a non integer value
        }

    }

    // Method for displaying courses
    public static void displayCourses(String filename) throws IOException, ClassNotFoundException{

        System.out.println("Press [ENTER] to show courses. Press [ENTER] again to show next term.");

        // HashMap to store courses grouped by year (key) with inner HashMap for terms (key) and Course objects (values)
        Map<Integer, Map<Integer, List<Course>>> coursesByYear = new HashMap<>();

        // Declaration of object input stream for deserialization and reading of data file of the curriculum
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));

        // BufferedReader for navigating courses per term
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // Read objects from the file until the end of the stream is reached
        try {
            Object obj; // Object variable declaration
            while ((obj = ois.readObject()) != null) { // Loops if object input stream reads an object in the data file
                if (obj instanceof Course) { // Check if the object is an instance of Course type
                    Course course = (Course) obj; // Initializes the object read to the file that was read and typecasts to Course object

                    // Gets the year and term from the Course object for sorting by year and term
                    int year = course.getYear();
                    int term = course.getTerm();

                    /* Map utilization for categorizing by terms within a year
                       - Integer for the key (since the year variable in the Course class is an integer)
                       - List of Course objects for our value
                       - coursesByYear is another map that uses getOrDefault built-in Map interface
                       - getOrDefault takes two arguments: key and defaultValue
                            - Key = year of the course we are retrieving
                            - defaultValue = creates an empty HashMap object if the year does not exist in coursesByYear map */
                    Map<Integer, List<Course>> termMap = coursesByYear.getOrDefault(year, new HashMap<>());

                    /* Creates a List that will hold Course objects
                        - termMap: Map object we declared earlier
                        - getOrDefault method arguments:
                            - Key = term number to search for the list of courses
                            - defaultValue = creates an empty ArrayList object if term does not exist in termMap */
                    List<Course> courseList = termMap.getOrDefault(term, new ArrayList<>());

                    // Add the course to the list for the year and term
                    courseList.add(course);

                    // NOTE: .put(key, value) method updates key-value pairs
                    termMap.put(term, courseList); // Updates the termMap with the modified courseList; Connects the key (term) with updated courseList
                    coursesByYear.put(year, termMap); // Updates coursesByYear map with the modified termMap; Connects the key (year) with updated termMap
                } else {
                    System.out.println("File error.");
                }
            }
        } catch (EOFException e) {
            ois.close(); // Close the streams if reached end of file
        }

        /* Creates an Iterator variable from the Iterator interface for the entries in the map
                - We declare an Iterator object named yearIterator for detecting if the Map still has a next variable
                    - This iterator iterates over the key-value pairs in a map
                    - coursesByYear = our HashMap that groups the courses by year (outer HashMap) and term (inner HashMap)
                    - .entrySet() = returns a set of all the key-value pairs (entries) in the coursesByYear map
                    - .iterator() = returned by entrySet() to get an iterator object. The iterator allows to iterate through the entries in the set one by one.
                - Map.Entry is an interface to access an entry (key-value pair) within a Map collection
                    - Integer: key of the outer map (year)
                    - Map<Integer, List<Course>>: value of the outer map; also the INNER MAP for term with courses (key-value pair)
                        - Integer: Key of the inner map (term)
                        - List<Course>: Value of the inner map (list of Course objects) */
        Iterator<Map.Entry<Integer, Map<Integer, List<Course>>>> yearIterator = coursesByYear.entrySet().iterator();

        // Outer loop for years
        while (yearIterator.hasNext()) { // Loops if yearIterator still has elements in the coursesByYear HashMap

            // Map.Entry object that returns the next value after the iterator cursor
            Map.Entry<Integer, Map<Integer, List<Course>>> yearEntry = yearIterator.next();

            // Map.Entry object gets the value of the yearEntry in the loop and initializes it to termMap
            // termMap is a Map we again use to sort courses by terms in a specific year
            Map<Integer, List<Course>> termMap = yearEntry.getValue();

            // Same concept as yearIterator Map.Entry object
                // - .entrySet() = returns a set of all the key-value pairs (entries) in the coursesByYear map
                // - .iterator() = returned by entrySet() to get an iterator object. The iterator allows to iterate through the entries in the set one by one.
            Iterator<Map.Entry<Integer, List<Course>>> termIterator = termMap.entrySet().iterator();

            // Inner loop for terms
            while (termIterator.hasNext()) { // Loops if termIterator still has elements in the termMap

                // Same concept as yearEntry Map.Entry object
                    // Returns the next value after the iterator cursor
                Map.Entry<Integer, List<Course>> termEntry = termIterator.next();

                List<Course> courseList = termEntry.getValue(); // Gets the value of the termEntry in the loop and assigns it to the CourseList List variable

                if (br.readLine() == null) { // If BufferedReader does not read any objects anymore, break
                    break;
                } else {
                    for (Course course : courseList) { // Enhanced for loop for iterating over each Course object in the courseList for current term
                        System.out.println("\t\t" + course); // Display objects
                    }
                }
            }
        }

    }

    // Method for displaying courses with grades
    public static void displayCoursesWithGrades(String filename) throws IOException, ClassNotFoundException {

        System.out.println("Press [ENTER] to show courses with grades. Press [ENTER] again to show next term.");

        // HashMap to store courses grouped by year (key) with another inner HashMap for terms (key)
        // and CourseWithGrades objects (values)
        Map<Integer, Map<Integer, List<CourseWithGrades>>> coursesWithGradesByYear = new HashMap<>();

        // Open object input stream for the migrated file
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));

        // BufferedReader for navigating courses per term
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            Object obj;
            while ((obj = ois.readObject()) != null) {
                // Check if the object is a CourseWithGrades instance
                if (obj instanceof CourseWithGrades) {
                    CourseWithGrades courseWithGrades = (CourseWithGrades) obj;

                    // Get year and term from the CourseWithGrades object (inherited)
                    int year = courseWithGrades.getYear();
                    int term = courseWithGrades.getTerm();

                    // Inner map for the year
                    Map<Integer, List<CourseWithGrades>> termMap = coursesWithGradesByYear.getOrDefault(year, new HashMap<>());

                    // List courses for the term
                    List<CourseWithGrades> courseWithGradesList = termMap.getOrDefault(term, new ArrayList<>());

                    // Add the course to the list for the year and term
                    courseWithGradesList.add(courseWithGrades);

                    // Update the maps with the modified lists
                    termMap.put(term, courseWithGradesList);
                    coursesWithGradesByYear.put(year, termMap);

                } else {
                    System.out.println("File error.");
                }
            }

        } catch (EOFException e) {
            ois.close(); // Close streams if reached end of file
        } finally {
            ois.close(); // Ensure streams are closed
        }

        // Print courses grouped by year and sorted by term; Same concept logic as nested loops in displayCourses() method
        for (Map.Entry<Integer, Map<Integer, List<CourseWithGrades>>> yearEntry : coursesWithGradesByYear.entrySet()) {
            for (Map.Entry<Integer, List<CourseWithGrades>> termEntry : yearEntry.getValue().entrySet()) {
                if (br.readLine() == null) {
                    break;
                } else {
                    for (CourseWithGrades course : termEntry.getValue()) {
                        System.out.println("\t\t" + course);
                    }
                }
            }
        }

    }

    // Method for grades input
    public static void enterGrades(String filename) throws IOException, ClassNotFoundException {

        // BufferedReader for user input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // HashMap to store CourseWithGrades objects
        HashMap<Integer, CourseWithGrades> courseMap = new HashMap<>();
        // Integer as our key for the hashCode; NOTE: Different object, unique hashCode
        // CourseWithGrades object for our value

        // Load existing courses from file (same logic concept with displayCoursesWithGrades)
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));

        try {
            Object obj; // Variable obj is an Object type

            // This while loop continues if ois.readObject() does not return null
            // obj is initialized as the object being read from readObject method in ois
            while ((obj = ois.readObject()) != null) {

                if (obj instanceof CourseWithGrades) { // Condition checks if obj is an instance of CourseWithGrades class (same instance variables)

                    CourseWithGrades course = (CourseWithGrades) obj; // Casts it to the CourseWithGrades object for reading

                    // This adds the course object to the courseMap
                    courseMap.put(course.hashCode(), course);
                    // .put() method is used to add a key-value pair in the courseMap
                    // Uses hashCode() method for generating a UNIQUE integer "key"
                }
            }
        } catch (EOFException e) {
            ois.close(); // Close streams if reached end of file (same as displayCoursesWithGrades)
        } finally {
            ois.close(); // Ensure streams are closed
        }

        // While loop for grades input
        while (true) {

            // User needs to input the course number of the course they wish to add grades on
            System.out.println("\nEnter the course number (or 'exit' to finish): ");
            String courseNum = br.readLine();

            if (courseNum.equalsIgnoreCase("exit")) {
                break; // If user inputs String "exit" no matter the case, exits loop
            }

            // Find course by iterating through the HashMap
            CourseWithGrades courseToGrade = null; // Initial value for courseToGrade of type courseWithGrades having no courses found YET

            for (CourseWithGrades course : courseMap.values()) { // Loops through all the course objects in the courseMap HashMap
                if (course.getCourseNum().equals(courseNum)) { // Checks if the courseNum from user input matches the one from the list
                    courseToGrade = course; // Assigns the course to courseToGrade variable we initialized before
                    break;
                }
            }

            // If course not found, inform user
            if (courseToGrade == null) {
                System.out.println("Course not found.");
                continue; // Continues to ask user until course number matches the one in the course object in the courseMap HashMap
            }

            // Variables for grade input and initialization of legend (e.g. if user inputs a grade lower than 75, legend becomes "F")
            int grade;
            String legend = "";

            // Get and validate grade input
            while (true) {
                System.out.println("Enter grade for " + courseToGrade.getCourseNum() + ": "); // Prompts user to input and displays the course num from explicit input

                try {

                    grade = Integer.parseInt(br.readLine());

                    if (grade < 65 || grade > 100) { // Grades only range from 65 to 99
                        System.out.println("Invalid grade. Please enter a number between 65 and 100."); // Informs user that grade is not possible LMAO
                    } else {
                        if (grade < 75) { // If grade is valid, check if below 75 (which is the passing grade)
                            legend = "F"; // Legend of the course object becomes "F" for failing grade
                        }
                        break; // Once valid grade has been obtained, break from the loop.
                    }

                } catch (NumberFormatException e) { // Exception handling if user inputs does not input a number
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            // Set the grade and update the course values in the HashMap
            courseToGrade.setTaken(true); // isTaken is now set to true, because there has been a grade set to it.
            courseToGrade.setGrade(grade); // Validated grade setting
            courseToGrade.setLegend(legend); // Legend setting if grade is failing (for now, other legends are not yet implemented)

            System.out.println("Grade successfully entered for " + courseToGrade.getCourseNum() + "."); // Display success message
        }

        // Write updated courses back to the file
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("courselist_grade.dat"));

        // Loops through all the course values in the courseMap HashMap
        for (CourseWithGrades course : courseMap.values()) {
            oos.writeObject(course); // Writes object to the file
        }

        oos.close(); // Close stream
        System.out.println("Grades successfully saved."); // Success message for user assurance HAHAHAHA
    }

    // Method for editing a course (mainly used for course electives)
    // Almost same concept as enterGrades() method, just changing the variables to edit/override
    public static void editCourse(String filename) throws IOException, ClassNotFoundException {

        // BufferedReader for reading user input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        HashMap<String, CourseWithGrades> courseMap = new HashMap<>();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));

        try {

            Object obj;

            while ((obj = ois.readObject()) != null) {
                if (obj instanceof CourseWithGrades) {
                    CourseWithGrades course = (CourseWithGrades) obj;
                    courseMap.put(course.getCourseNum(), course);
                }
            }

        } catch (EOFException e) {
            ois.close();
        } finally {
            ois.close();
        }

        while (true) {

            System.out.println("\nEnter the course number (or 'exit' to finish editing courses)");
            String courseNum = br.readLine();

            if (courseNum.equalsIgnoreCase("exit")) {
                break;
            }

            CourseWithGrades courseList = null;

            for (CourseWithGrades course : courseMap.values()) {
                if (course.getCourseNum().equals(courseNum)) {
                    courseList = course;
                    break;
                }
            }

            if (courseList == null) {
                System.out.println("Course not found.");
                continue;
            }

            String newCourseNum, courseDesc;

            System.out.println("Enter new course number for " + courseList.getCourseNum() + ": ");
            newCourseNum = br.readLine();
            System.out.println("Enter new course description for " + courseList.getCourseDesc() + ": ");
            courseDesc = br.readLine();

            courseList.setCourseNum(newCourseNum);
            courseList.setCourseDesc(courseDesc);
            courseList.setElective(true);

            System.out.println("Successfully edited existing course.");

        }

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("courselist_grade.dat"));

        for (CourseWithGrades course : courseMap.values()) {
            oos.writeObject(course);
        }

        oos.close();
        System.out.println("Courses successfully saved.");

    }

    // Method for adding other courses outside the curriculum
    public static void addOtherCoursesTaken(String filename) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // HashMap to store CourseWithGrades objects for newly added courses
        HashMap<Integer, CourseWithGrades> newCourses = new HashMap<>();

        // HashMap to also store CourseWithGrades objects for deserializing the existing courses from the curriculum
        HashMap<Integer, CourseWithGrades> existingCourses = readCoursesFromFileHashMap(filename);
        // readCoursesFromFile() is another explicit method for deserializing the course objects in data file, and returns a HashMap

        while (true) { // While loop for entering the new course details that is NOT included in the curriculum
            System.out.println("\nEnter all details for the new course (or 'exit' to finish):");

            // Variables declaration
            int year, term, grade, units;
            String courseNum;
            String courseDesc;

            // Loops until a valid integer for the course year is entered
            while (true) {
                System.out.print("Enter year: ");
                try {
                    year = Integer.parseInt(br.readLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            // Loops until a valid integer for the course term is entered
            while (true) {
                System.out.print("Enter term: ");
                try {
                    term = Integer.parseInt(br.readLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            System.out.print("Enter course number: "); // For course number
            courseNum = br.readLine();
            System.out.print("Enter course description: "); // For course description
            courseDesc = br.readLine();

            // Loops until a valid integer for the units of a course is entered
            while (true) {
                System.out.print("Enter units: ");
                try {
                    units = Integer.parseInt(br.readLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            // Loops until a valid integer for the course grade is entered
            while (true) {
                System.out.print("Enter grade: ");
                try {
                    grade = Integer.parseInt(br.readLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            // Create a new CourseWithGrades object that makes the isTaken variable true
            // Initializes the new course object as true because user will not input a course unless taken
            CourseWithGrades newCourse = new CourseWithGrades(year, term, courseNum, courseDesc, units, true, grade, "", false);

            // We also set the outsideCurriculum variable as true (initialized as false for curriculum checklist)
            // outsideCurriculum variable is for the reset() method, which removes the course if it is outside the curriculum
            newCourse.outsideCurriculum = true;

            // .put(key, value) method is used to add key-value pairs in the newCourse HashMap
            // .hashCode() method returns the unique hash code (used for key) for our object (value)
            // In summary, .put(newCourse.hashCode() = key, newCourse = our object value)
            newCourses.put(newCourse.hashCode(), newCourse);

            // Ask user if they want to enter another course
            System.out.print("Do you want to enter another course? (y/n): ");
            String answer = br.readLine().toLowerCase();

            if (!answer.equalsIgnoreCase("y")) {
                break; // If user selects NOT y, exit the loop
            }
        }

        // Object output stream for serializing the file with the newly added course
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));

        // Write the existing courses from file
        for (CourseWithGrades course : existingCourses.values()) {
             oos.writeObject(course);
        }

        // Add the new courses to the file
        for (CourseWithGrades course : newCourses.values()) {
            oos.writeObject(course);
        }

        oos.close(); // Close object output stream

        System.out.println("New course/s successfully added."); // Success message once course has been added

    }

    // Method for displaying GPA
    public static void displayGPA(String filename) throws IOException, ClassNotFoundException {

        System.out.println("Press [ENTER] to show GPA per term. Press [ENTER] again to show next term.\n");

        // Prompt for user input at the end
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String userInput;

        // Open object input stream for the save file
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));

        // Group courses by year and term (Same concept with displayCoursesWithGrades() method)
        Map<String, List<CourseWithGrades>> coursesByTerm = new HashMap<>();

        try {
            Object obj;
            while ((obj = ois.readObject()) != null) {
                if (obj instanceof CourseWithGrades) {
                    CourseWithGrades courseWithGrades = (CourseWithGrades) obj;
                    int year = courseWithGrades.getYear();
                    int term = courseWithGrades.getTerm();

                    // String variable for our HashMap key
                    String key = year + "-" + term;

                    // Retrieves a list of CourseWithGrades objects associated with the key
                    // Uses the get() method on the coursesByTerm map
                    List<CourseWithGrades> courseList = coursesByTerm.get(key);

                    if (courseList == null) { // get() method returns null if key DOES NOT exist in the map
                        courseList = new ArrayList<>(); // New ArrayList for courses in the key (year + term; declared awhile ago)
                        coursesByTerm.put(key, courseList); // This new ArrayList gets stored in the coursesByTerm map with the key
                    }
                    courseList.add(courseWithGrades); // courseWithGrades object gets added to courseList
                } else {
                    System.out.println("File error.");
                }
            }
        } catch (EOFException e) {
            ois.close(); // Close stream if reached end of file
        } finally {
            ois.close(); // Ensure streams are closed
        }

        // Loop for calculating the GPA of the term
        // Iterates over each key-value pairs (initialized to entry) in the coursesByTerm map
        for (Map.Entry<String, List<CourseWithGrades>> entry : coursesByTerm.entrySet()) {

            String key = entry.getKey(); // Gets the key for the current entry
            List<CourseWithGrades> termCourses = entry.getValue(); // Retrieves the value (the list of CourseWithGrades objects) paired with the key

            // Calculate total weighted grade points and total units using stream in the term
            /* PROCESS:
                - Iterates each CourseWithGrades object in the list
                - In every course, we multiply the grades by the units which gets assigned to totalWeightedGradePoitns variable */
            double totalWeightedGradePoints = 0;
            for (CourseWithGrades course : termCourses) {
                totalWeightedGradePoints += course.getGrade() * course.getUnits();
            }

            // Same concept as previous variable; gets the sum of the units in the termCourses list
            double totalUnits = 0;
            for (CourseWithGrades course : termCourses) {
                totalUnits += course.getUnits();
            }

            // Calculate GPA (total grade / total units)
            double gpa;
            if (totalUnits > 0) {
                gpa = totalWeightedGradePoints / totalUnits;
            } else {
                gpa = 0.0;
            }

            // Display courses within the term
            for (CourseWithGrades course : termCourses) {
                System.out.println("\t" + course);
            }

            // Display GPA
            System.out.println("\tGPA: " + gpa + "\n");
        }

        do {
            System.out.println("\nPress [ENTER] to show GPA for the next term, or any other key to exit.");
            userInput = br.readLine();
        } while (userInput != null && userInput.isEmpty()); // Continue until user enters null

    }

    // Method for sorting courses in alphabetical order
    public static void displayCoursesAlphabetical(String filename) throws IOException {

        ArrayList<CourseWithGrades> courses = readCoursesFromFileArrayList(filename); // Read courses from file

        courses.stream()
                .sorted(Comparator.comparing(CourseWithGrades::getCourseDesc))
                .forEach(course -> System.out.printf("%-15s%-50s%-15s%n", course.getCourseNum(), course.getCourseDesc(), course.getUnits()));

    }

    // Method for displaying courses with grades from highest to lowest
    public static void showHighestToLowestGrades(String filename) throws IOException {

        ArrayList<CourseWithGrades> courses = readCoursesFromFileArrayList(filename);

        courses.stream()
                .sorted(Comparator.comparingInt(CourseWithGrades::getGrade).reversed())
                .forEach(course -> System.out.println(course.getCourseNum() + " " + course.getCourseDesc() + " " + course.getUnits() + " " + course.getGrade()));

    }

    // Method to reset data and start from scratch
    public static void resetData(String filename) throws IOException, ClassNotFoundException {

        String newFile = "courselist_grade.dat"; // New file

        // Open streams for reading old data and writing migrated data
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        FileOutputStream fos = new FileOutputStream(newFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        try {
            Object obj;
            while ((obj = ois.readObject()) != null) {
                // Check if the object is a Course instance
                if (obj instanceof Course) {
                    Course oldCourse = (Course) obj;

                    // Create a new CourseWithGrades object with translated data
                    CourseWithGrades newCourse = new CourseWithGrades(
                            oldCourse.getYear(),
                            oldCourse.getTerm(),
                            oldCourse.getCourseNum(),
                            oldCourse.getCourseDesc(),
                            oldCourse.getUnits(),
                            // Set default values (or handle missing data) for new attributes
                            false, // Set isTaken to false (or based on your logic)
                            0, // Set grade to -1 (or based on your logic)
                            "", // Legend for grade input
                            false // Set to false initially since elective courses will be edited by user
                    );

                    // Write the new CourseWithGrades object to the migrated file
                    oos.writeObject(newCourse);
                } else {
                    System.out.println("Warning: Unexpected object type encountered in old file.");
                }
            }

        } catch (EOFException e) {
            // Close streams if reached end of file
            oos.close();
            fos.close();
            ois.close();
            fis.close();
        } finally {
            // Ensure streams are closed even on exceptions
            oos.close();
            fos.close();
            ois.close();
            fis.close();
        }

        System.out.println("Data migration completed. Migrated data written to: " + newFile);
    }

    // Method for reading file and deserializing as HashMap objects; used by addOtherCoursesTaken() method
    private static HashMap<Integer, CourseWithGrades> readCoursesFromFileHashMap(String filename) throws IOException {

        // Creates a HashMap for the course objects
        HashMap<Integer, CourseWithGrades> courses = new HashMap<>();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));

        try {

            while (true) {

                Object obj = ois.readObject(); // Declares an object type variable obj, reads object from file

                if (obj instanceof CourseWithGrades) { // Condition checks if obj is an instance of CourseWithGrades class (same instance variables)

                    CourseWithGrades course = (CourseWithGrades) obj; // Casts it to the CourseWithGrades object for reading

                    // This adds the course object to the courses HashMap
                    courses.put(course.hashCode(), course);
                    // .put() method is used to add a key-value pair in the courseMap
                    // Uses hashCode() method for generating a UNIQUE integer "key"
                }
            }
        } catch (EOFException | ClassNotFoundException e) {
            ois.close(); // Object input stream closes if it reaches the end of file
        } finally {
            ois.close(); // Ensure object input stream is closed
        }

        return courses; // Return courses HashMap as object
    }

    // Method for reading file and deserializing as ArrayList objects; used for collections sort
    private static ArrayList<CourseWithGrades> readCoursesFromFileArrayList(String filename) throws IOException {

        ArrayList<CourseWithGrades> courses = new ArrayList<>(); // Create an ArrayList to store courses

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {

            while (true) {
                Object obj = ois.readObject();
                if (obj instanceof CourseWithGrades) {
                    CourseWithGrades course = (CourseWithGrades) obj;
                    courses.add(course);
                }
            }
        } catch (EOFException e) {
            throw new RuntimeException(e);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return courses;
    }

}
