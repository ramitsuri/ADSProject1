package com.ramitsuri.project1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by ramitsuri on 3/15/16.
 */
public class Project {

    public static void initialize(Event[] events){
        RBTree rbTree = RBTree.getInstance();
        for(Event event:events){
         rbTree.insert(event);
        }
    }

    public static int increaseCountForId(int id, int increaseBy){
        RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        node.setCountForEvent(node.event.count + increaseBy);
        return node.event.getCount();
    }

    public static int reduceCountForId(int id, int decreaseBy){
        RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        /*if((node.event.getCount() - decreaseBy) <= 0)
            rbTree.deleteNode(node);
        else*/
            node.setCountForEvent(node.event.count - decreaseBy);
        return node.event.getCount();
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
        int newCount = increaseCountForId(12,3);
        int newCount2 = reduceCountForId(12,3);
        String a = "";
    }

}
