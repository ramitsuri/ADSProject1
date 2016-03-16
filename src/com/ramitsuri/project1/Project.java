package com.ramitsuri.project1;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.security.KeyException;
import java.security.PublicKey;
import java.util.Map;

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

    public void increaseID(int theID, int m){
        RBTree rbTree = RBTree.getInstance();

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
    }

}
