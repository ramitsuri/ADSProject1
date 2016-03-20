package com.ramitsuri.project1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Project {

    private void initialize(Event[] events) {
        RBTree rbTree = RBTree.getInstance();
        try {
            for (Event event : events) {
                rbTree.insert(event);
            }
        }
        catch(Exception ex){

        }
    }

    private void initializeWithSortedArray(Event[] events){
        RBTree rbTree = RBTree.getInstance();
        rbTree.root = recursiveInsert(events, 0, events.length-1);
        boolean isLevelOneBlack = true;
        if(findHeight(events.length)%2 == 0)
            isLevelOneBlack = false;
        colorNodes(rbTree.root, isLevelOneBlack);
    }

    private RBNode recursiveInsert(Event[] events, int start, int end){
        if(start > end){
            return null;
        }
        int mid = (start+end)/2;
        RBNode node = new RBNode(events[mid]);
        node.left = recursiveInsert(events, start, mid-1);
        node.right = recursiveInsert(events, mid+1, end);
        return node;
    }
    private double findHeight(int numberOfNodes){
        return (Math.ceil(Math.log(numberOfNodes+1)/Math.log(2)));
    }
    private void colorNodes(RBNode root, boolean isLevelOneBlack){
        Queue<RBNode> queue = new LinkedList<>();
        queue.add(root);
        root.color = Color.BLACK;
        while(!queue.isEmpty()){
            RBNode node = queue.poll();
            if(node.right!=RBNode.nil && node.right!=null) {
                queue.add(node.right);
                node.right.parent = node;
                if(isLevelOneBlack && node == root)
                    node.right.color = Color.BLACK;
                else
                    node.right.color = !node.color;
            }
            if(node.left!=RBNode.nil && node.left!=null) {
                queue.add(node.left);
                node.left.parent = node;
                if(isLevelOneBlack && node == root)
                    node.left.color = Color.BLACK;
                else
                    node.left.color = !node.color;
            }
        }
    }

    private int increaseCountForID(int id, int increaseBy) {
        RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        if (node != RBNode.nil && node != null) {
            node.setCountForEvent(node.event.count + increaseBy);
            return node.event.getCount();
        }
        else{
            rbTree.insert(new Event(id, increaseBy));
            return increaseBy;
        }
    }

    private int reduceCountForID(int id, int decreaseBy) {
        RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        if (node != RBNode.nil && node != null) {
            if ((node.event.getCount() - decreaseBy) <= 0) {
                rbTree.deleteNode(node);
                return 0;
            }
            else {
                node.setCountForEvent(node.event.count - decreaseBy);
                return node.event.getCount();
            }
        } else
            return 0;
    }

    private int getCountForEventID(int id) {
        RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        if (node != RBNode.nil && node != null)
            return node.event.getCount();
        else
            return 0;
    }

    private ArrayList<Event> getEventsInRange(ArrayList<Event> events, RBNode node, int id1, int id2) {

        if (node == RBNode.nil || node == null)
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

    private RBNode getNextNode(int id, RBNode node, RBNode bestUntilNow ) {
        if(bestUntilNow.event.ID == -1 )
            bestUntilNow = node;
        if(node.event.ID > id && bestUntilNow.event.ID > node.event.ID)
            bestUntilNow = node;
        if(id < node.event.ID){
            if(node.left != RBNode.nil && node.left != null)
                return getNextNode(id, node.left, bestUntilNow);
            else{
                return node;
            }
        }
        else {
            if (node.right != RBNode.nil && node.right != null)
                return getNextNode(id, node.right, bestUntilNow);
            else if (node.event.ID <= id)
                return bestUntilNow;
            else
                return node;
        }
    }

    private RBNode getPreviousNode(int id, RBNode node, RBNode bestUntilNow ) {
        if(node.event.ID < id && bestUntilNow.event.ID < node.event.ID)
            bestUntilNow = node;
        if(id > node.event.ID){
            if(node.right != RBNode.nil && node.right != null)
                return getPreviousNode(id, node.right, bestUntilNow);
            else{
                return node;
            }
        }
        else {
            if(node.left != RBNode.nil && node.left != null)
                return getPreviousNode(id, node.left, bestUntilNow);
            else if(node.event.ID >= id)
                return bestUntilNow;
            else
                return node;
        }
    }

    private Event getNextEvent(int id) {
        /*RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        if (node != RBNode.nil && node != null) {
            node = node.right.left;
            while ((node.left != RBNode.nil && node.left != null) && node.left.event.ID != -1) {
                node = node.left;
            }
            return node.event;
        } else return null;*/
        RBTree rbTree = RBTree.getInstance();
        RBNode rbNode = getNextNode(id, rbTree.root, RBNode.nil);
        return rbNode.event;
    }

    private Event getPreviousEvent(int id) {
        /*RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        if (node != RBNode.nil && node != null) {
            node = node.left.right;
            while ((node.right != RBNode.nil && node.right != null) && node.right.event.ID != -1) {
                node = node.right;
            }
            return node.event;
        } else
            return null;*/
        RBTree rbTree = RBTree.getInstance();
        RBNode rbNode = getPreviousNode(id, rbTree.root, RBNode.nil);
        return rbNode.event;
    }

    private Event[] readFile(String fileName) {

        Event[] events = null;
        try {
            Scanner sc = new Scanner(new File(fileName), "UTF-8");
            //BufferedReader br = new BufferedReader(new FileReader(fileName));
            //int numberOfEvents = Integer.parseInt(br.readLine());
            int numberOfEvents = Integer.parseInt(sc.nextLine());
            events = new Event[numberOfEvents];
            for (int i = 0; i < numberOfEvents; i++) {
                //String line = br.readLine();
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

        /*System.out.println("For reference:");
        System.out.println("Command 'increase <int1> <int2>' increases the count of Event with ID int1 by int2 and prints the new count");
        System.out.println("Command 'reduce <int1> <int2>' reduces the count of Event with ID int1 by int2 and prints the new count");
        System.out.println("Command 'count <int1>' prints the count of Event with ID int1");
        System.out.println("Command 'inrange <int1> <int2>' prints the count of Events falling in the range int1 and int2, both included ");
        System.out.println("Command 'next <int1>' prints the Event with ID which is just next to int1");
        System.out.println("Command 'previous <int1>' prints the Event with ID which is just previous to int1");
        System.out.println("Enter command 'quit' to exit the program");*/

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
                        int id = Integer.parseInt(line.split(" ")[1]);
                        int reduceBy = Integer.parseInt(line.split(" ")[2]);
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
                        Event event = getNextEvent(id);
                        System.out.println(event.ID + " " + event.count);
                        break;
                    }

                    case "previous": {
                        int id = Integer.parseInt(line.split(" ")[1]);
                        Event event = getPreviousEvent(id);
                        System.out.println(event.ID + " " + event.count);
                        break;
                    }

                    case "quit": {
                        System.out.println("Exiting");
                        System.exit(0);
                        break;
                    }
                }
                RBTree.getInstance().printTree(RBTree.getInstance().root);
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
        if (args.length == 1) {
            Project project = new Project();
            String fileToRead = args[0];
            Event[] events = project.readFile(fileToRead);
            project.initialize(events);
            //project.initializeWithSortedArray(events);
            RBTree rbTree = RBTree.getInstance();
            System.out.println("Tree is ready. Input Command.");
            project.printConsole();
        }

        /*Project project = new Project();
        System.out.print(project.findHeight(65532));
        if(project.findHeight(65536)%2 == 0){
         System.out.print("tr");

        }
        else
            System.out.print("fa");*/
    }
}
