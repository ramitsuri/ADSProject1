package com.ramitsuri.project1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

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
        String a = "";
        return node.event;
    }

    public static Event getPreviousEvent(int id){
        RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        node = node.left.right;
        while(node.right!=null && node.right.event.ID != -1){
            node = node.right;
        }
        String a = "";
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
        //int newCount = increaseCountForID(12,3);
        //int newCount2 = reduceCountForID(12,10);
        RBTree rbTree = RBTree.getInstance();
        //RBNode node1 = rbTree.findNode(17, rbTree.root);
        //rbTree.deleteNodeWithID(12);
        //RBNode node = rbTree.findNode(12, rbTree.root);
        System.out.println(getCountOfEventsInRange(13,13));
        getNextEvent(87);
        getPreviousEvent(87);
        String a = "";
    }

}
