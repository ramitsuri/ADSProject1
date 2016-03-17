package com.ramitsuri.project1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Project {

    private void initialize(Event[] events) {
        RBTree rbTree = RBTree.getInstance();
        for (Event event : events) {
            rbTree.insert(event);
        }
    }

    private int increaseCountForID(int id, int increaseBy) {
        RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        if (node != null) {
            node.setCountForEvent(node.event.count + increaseBy);
            return node.event.getCount();
        } else
            return -1;
    }

    private int reduceCountForID(int id, int decreaseBy) {
        RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        if (node != null) {
            if ((node.event.getCount() - decreaseBy) <= 0)
                rbTree.deleteNodeWithID(id);
            else
                node.setCountForEvent(node.event.count - decreaseBy);
            return node.event.getCount();
        } else
            return -1;
    }

    private int getCountForEventID(int id) {
        RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        if (node != null)
            return node.event.getCount();
        else
            return -1;
    }

    private ArrayList<Event> getEventsInRange(ArrayList<Event> events, RBNode node, int id1, int id2) {

        if (node == null)
            return events;
        if (id1 <= node.event.ID)
            getEventsInRange(events, node.left, id1, id2);
        if (id1 <= node.event.ID && node.event.ID <= id2)
            events.add(node.event);
        if (node.event.ID <= id2)
            getEventsInRange(events, node.right, id1, id2);
        return events;
    }

    private int getCountOfEventsInRange(int id1, int id2) {
        RBTree rbTree = RBTree.getInstance();
        ArrayList<Event> events = new ArrayList<>();
        return getEventsInRange(events, rbTree.root, id1, id2).size();
    }

    private Event getNextEvent(int id) {
        RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        if (node != null) {
            node = node.right.left;
            while (node.left != null && node.left.event.ID != -1) {
                node = node.left;
            }
            return node.event;
        } else return null;
    }

    private Event getPreviousEvent(int id) {
        RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        if (node != null) {
            node = node.left.right;
            while (node.right != null && node.right.event.ID != -1) {
                node = node.right;
            }

            return node.event;
        } else
            return null;
    }

    private Event[] readFile(String fileName) {
        Event[] events = null;
        try {

            BufferedReader br = new BufferedReader(new FileReader(fileName));
            int numberOfEvents = Integer.parseInt(br.readLine());
            events = new Event[numberOfEvents];
            for (int i = 0; i < numberOfEvents; i++) {
                String line = br.readLine();
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

        System.out.println("For reference:");
        System.out.println("Command 'increase <int1> <int2>' increases the count of Event with ID int1 by int2 and prints the new count");
        System.out.println("Command 'reduce <int1> <int2>' reduces the count of Event with ID int1 by int2 and prints the new count");
        System.out.println("Command 'count <int1>' prints the count of Event with ID int1");
        System.out.println("Command 'inrange <int1> <int2>' prints the count of Events falling in the range int1 and int2, both included ");
        System.out.println("Command 'next <int1>' prints the Event with ID which is just next to int1");
        System.out.println("Command 'previous <int1>' prints the Event with ID which is just previous to int1");
        System.out.println("Enter command 'quit' to exit the program");

        Scanner scanner = new Scanner(System.in);

        while (true) {


            String line = scanner.nextLine();
            String command = line.split(" ")[0];
            try {
                switch (command) {
                    case "increase": {
                        int id = Integer.parseInt(line.split(" ")[1]);
                        int increaseBy = Integer.parseInt(line.split(" ")[2]);
                        System.out.println(increaseCountForID(id, increaseBy));
                        break;
                    }
                    case "reduce": {
                        String input = line.split(" ")[1];
                        int id = Integer.parseInt(line.split(" ")[1]);
                        int reduceBy = Integer.parseInt(input.split(" ")[1]);
                        System.out.println(reduceCountForID(id, reduceBy));
                        break;
                    }

                    case "count": {
                        int id = Integer.parseInt(line.split(" ")[1]);
                        System.out.println(getCountForEventID(id));
                        break;
                    }

                    case "inrange": {
                        int idLow = Integer.parseInt(line.split(" ")[1]);
                        int idHigh = Integer.parseInt(line.split(" ")[2]);
                        System.out.println(getCountOfEventsInRange(idLow, idHigh));
                        break;
                    }

                    case "next": {
                        int id = Integer.parseInt(line.split(" ")[1]);
                        System.out.println(getNextEvent(id));
                        break;
                    }

                    case "previous": {
                        int id = Integer.parseInt(line.split(" ")[1]);
                        System.out.println(getPreviousEvent(id));
                        break;
                    }

                    case "quit": {
                        System.out.println("Exiting");
                        System.exit(0);
                        break;
                    }
                }
                System.out.println("Enter another command");
            } catch (IndexOutOfBoundsException ex) {
                System.out.println("Command arguments insufficient");
                printConsole();
            }

            catch(Exception ex){
                System.out.println("Could not execute command. Try Again!");
                printConsole();
            }
        }
    }

    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.println("Arguments insufficient");
        } else {
            Project project = new Project();
            String fileToRead = args[0];
            Event[] events = project.readFile(fileToRead);
            project.initialize(events);
            System.out.println("Tree is ready. Input Command.");
            project.printConsole();
        }


    }

}
