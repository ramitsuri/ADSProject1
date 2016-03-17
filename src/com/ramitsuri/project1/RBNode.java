package com.ramitsuri.project1;

/**
 * Created by ramitsuri on 3/13/16.
 */
public class RBNode {

    public  RBNode left, right, parent;
    Event event = new Event(-1,0);
    boolean color;

    public RBNode(Event event){

        this.color = Color.BLACK;
        this.event = event;
        this.left = RBTree.nil;
        this.right = RBTree.nil;
        this.parent = RBTree.nil;

    }

    public RBNode(Event event, RBNode left, RBNode right, RBNode parent){

        this.color = Color.BLACK;
        this.event = event;
        this.left = left;
        this.right = right;
        this.parent = parent;
    }

    public int compare(RBNode node){
        if(this.event.ID < node.event.ID)
            return -1;
        else if(this.event.ID > node.event.ID)
            return 1;
        else
            return 0;
    }

    public void setCountForEvent(int count){
        this.event.setCount(count);
    }
}
