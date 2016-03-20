package com.ramitsuri.project1;

/**
 * Created by ramitsuri on 3/13/16.

 */
public class Event {
    int ID;
    int count;

    public Event(int ID, int count){
        this.ID = ID;
        this.count = count;
    }

    public int getCount(){
        return count;
    }

    public void setCount(int count){
        this.count = count;
    }

}
