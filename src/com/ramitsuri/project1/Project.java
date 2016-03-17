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

    public static void initialize(Event[] events){
        RBTree rbTree = RBTree.getInstance();
        for(Event event:events){
         rbTree.insert(event);
        }
    }

    public static int increaseCountForID(int id, int increaseBy){
        RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        node.setCountForEvent(node.event.count + increaseBy);
        return node.event.getCount();
    }

    public static int reduceCountForID(int id, int decreaseBy){
        RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        if((node.event.getCount() - decreaseBy) <= 0)
            rbTree.deleteNodeWithID(id);
        else
            node.setCountForEvent(node.event.count - decreaseBy);
        return node.event.getCount();
    }

    public static int getCountForEventID(int id){
        RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        return node.event.getCount();
    }

    public static ArrayList<Event> getEventsInRange(ArrayList<Event> events, RBNode node, int id1, int id2){

        if(node == null)
            return events;
        if(id1 <= node.event.ID)
            getEventsInRange(events, node.left, id1, id2);
        if(id1 <= node.event.ID && node.event.ID <= id2)
            events.add(node.event);
        if(node.event.ID <= id2)
            getEventsInRange(events, node.right, id1, id2);
        return events;
    }

    public static int getCountOfEventsInRange(int id1, int id2){
        RBTree rbTree = RBTree.getInstance();
        ArrayList<Event> events = new ArrayList<>();
        return getEventsInRange(events, rbTree.root, id1, id2).size();
    }

    public static Event getNextEvent(int id){
        RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        node = node.right.left;
        while(node.left!=null && node.left.event.ID != -1){
            node = node.left;
        }
        return node.event;
    }

    public static Event getPreviousEvent(int id){
        RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        node = node.left.right;
        while(node.right!=null && node.right.event.ID != -1){
            node = node.right;
        }
        return node.event;
    }

    public static Event[] readFile(String fileName){
        Event[] events = null;
        try {

            BufferedReader br = new BufferedReader(new FileReader(fileName));
            int numberOfEvents = Integer.parseInt(br.readLine());
            events = new Event[numberOfEvents];
            for(int i=0;i<numberOfEvents;i++){
                String line = br.readLine();
                Event event = new Event(Integer.parseInt(line.split(" ")[0]), Integer.parseInt(line.split(" ")[1]));
                events[i] = event;
            }
        }

        catch(FileNotFoundException ex){
            System.out.println("File Not Found");
        }
        catch(Exception ex){
            System.out.println("Error reading the file");
        }
        return events;
    }

    public static void main(String args[]){

        Event [] events = readFile(args[0]);
        initialize(events);
        System.out.println("Tree is ready. Input Command.");
        System.out.println("For reference:");
        System.out.println("Command 'increase <int1> <int2>' increases the count of Event with ID int1 by int2 and prints the new count");
        System.out.println("Command 'reduce <int1> <int2>' reduces the count of Event with ID int1 by int2 and prints the new count");
        System.out.println("Command 'count <int1>' prints the count of Event with ID int1");
        System.out.println("Command 'inrange <int1> <int2>' prints the count of Events falling in the range int1 and int2, both included ");
        System.out.println("Command 'next <int1>' prints the Event with ID which is just next to int1");
        System.out.println("Command 'previous <int1>' prints the Event with ID which is just previous to int1");
        System.out.println("Enter command 'quit' to exit the program");

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String command = line.split(" ")[0];
        switch(command){
            case "increase": {
                String input = command.split(" ")[1];
                int id = Integer.parseInt(input.split(" ")[0]);
                int increaseBy = Integer.parseInt(input.split(" ")[1]);
                System.out.println(increaseCountForID(id, increaseBy));
                break;
            }
            case "reduce": {
                String input = command.split(" ")[1];
                int id = Integer.parseInt(input.split(" ")[0]);
                int reduceBy = Integer.parseInt(input.split(" ")[1]);
                System.out.println(reduceCountForID(id, reduceBy));
                break;
            }

            case "count": {
                String input = command.split(" ")[1];
                int id = Integer.parseInt(input);
                System.out.println(getCountForEventID(id));
                break;
            }

            case "inrange":{
                String input = command.split(" ")[1];
                int idLow = Integer.parseInt(input.split(" ")[0]);
                int idHigh = Integer.parseInt(input.split(" ")[1]);
                System.out.println(getCountOfEventsInRange(idLow, idHigh));
                break;
            }

            case "next": {
                String input = command.split(" ")[1];
                int id = Integer.parseInt(input);
                System.out.println(getNextEvent(id));
                break;
            }

            case "previous": {
                String input = command.split(" ")[1];
                int id = Integer.parseInt(input);
                System.out.println(getPreviousEvent(id));
                break;
            }

            case "quit": {
                System.out.println("Exiting");
                System.exit(0);
            }
        }

    }

}
