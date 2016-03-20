package com.ramitsuri.project1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Project {

    private Event[] readFile(String fileName) {
        Event[] events = null;
        try {
            Scanner sc = new Scanner(new File(fileName), "UTF-8");
            int numberOfEvents = Integer.parseInt(sc.nextLine());
            events = new Event[numberOfEvents];
            for (int i = 0; i < numberOfEvents; i++) {
                String line = sc.nextLine();
                Event event = new Event(Integer.parseInt(line.split(" ")[0]), Integer.parseInt(line.split(" ")[1]));
                events[i] = event;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found");
        } catch (Exception ex) {
            System.out.println("Error reading the file");
        }
        return events;
    }

    private void printConsole() {
/*

        System.out.println("For reference:");
        System.out.println("Command 'increase <int1> <int2>' increases the count of Event with ID int1 by int2 and prints the new count");
        System.out.println("Command 'reduce <int1> <int2>' reduces the count of Event with ID int1 by int2 and prints the new count");
        System.out.println("Command 'count <int1>' prints the count of Event with ID int1");
        System.out.println("Command 'inrange <int1> <int2>' prints the count of Events falling in the range int1 and int2, both included ");
        System.out.println("Command 'next <int1>' prints the Event with ID which is just next to int1");
        System.out.println("Command 'previous <int1>' prints the Event with ID which is just previous to int1");
        System.out.println("Enter command 'quit' to exit the program");

*/

        Scanner scanner = new Scanner(System.in);

        while (true) {

            RBTree rbTree = RBTree.getInstance();
            String line = scanner.nextLine();
            String command = line.split(" ")[0];
            try {
                switch (command) {
                    case "increase": {
                        int id = Integer.parseInt(line.split(" ")[1]);
                        int increaseBy = Integer.parseInt(line.split(" ")[2]);
                        System.out.println(rbTree.increaseCountForID(id, increaseBy));
                        break;
                    }
                    case "reduce": {
                        int id = Integer.parseInt(line.split(" ")[1]);
                        int reduceBy = Integer.parseInt(line.split(" ")[2]);
                        System.out.println(rbTree.reduceCountForID(id, reduceBy));
                        break;
                    }

                    case "count": {
                        int id = Integer.parseInt(line.split(" ")[1]);
                        System.out.println(rbTree.getCountForEventID(id));
                        break;
                    }

                    case "inrange": {
                        int idLow = Integer.parseInt(line.split(" ")[1]);
                        int idHigh = Integer.parseInt(line.split(" ")[2]);
                        System.out.println(rbTree.getCountOfEventsInRange(idLow, idHigh));
                        break;
                    }

                    case "next": {
                        int id = Integer.parseInt(line.split(" ")[1]);
                        Event event = rbTree.getNextEvent(id);
                        System.out.println(event.ID + " " + event.count);
                        break;
                    }

                    case "previous": {
                        int id = Integer.parseInt(line.split(" ")[1]);
                        Event event = rbTree.getPreviousEvent(id);
                        System.out.println(event.ID + " " + event.count);
                        break;
                    }

                    case "quit": {
                        System.out.println("Exiting");
                        System.exit(0);
                        break;
                    }
                }
                //RBTree.getInstance().printTree(RBTree.getInstance().root);
                //System.out.println("Enter another command");
            } catch (IndexOutOfBoundsException ex) {
                System.out.println(ex.getCause());
                //printConsole();
            }

            catch(Exception ex){
                System.out.println(ex.getCause());
                //printConsole();
            }
        }
    }

    public static void main(String args[]) {
        RBTree rbTree = RBTree.getInstance();
        if (args.length == 1) {
            Project project = new Project();
            String fileToRead = args[0];
            Event[] events = project.readFile(fileToRead);
            rbTree.initializeWithSortedArray(events);
            //project.initializeWithSortedArray(events);
            //System.out.println("Tree is ready. Input Command.");
            project.printConsole();
        }


    }
}
