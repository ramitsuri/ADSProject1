package com.ramitsuri.project1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class RBTree {


    private final boolean RED = false;
    private final boolean BLACK = true;
    private static RBTree instance = null;

    public static RBTree getInstance(){
        if(instance == null)
            instance = new RBTree();
        return instance;
    }

    public class RBNode {
        Event event = new Event(-1,-1);
        boolean color = BLACK;
        RBNode left = nil, right = nil, parent = nil;

        RBNode(Event event) {
            this.event = event;
        }

        public void setCountForEvent(int count){
            this.event.setCount(count);
        }
    }

    private final RBNode nil = new RBNode(new Event(-1,-1));
    public RBNode root = nil;

    //print the tree structure, for debug purposes
    public void printTree(RBNode node) {
        if (node == nil) {
            return;
        }
        printTree(node.left);
        System.out.print(((node.color==RED)?"Color: Red ":"Color: Black ")+"ID: "+node.event.ID+" Parent: "+node.parent.event.ID+"\n");
        printTree(node.right);
    }

    // find a node using the event id
    private RBNode findNode(int id, RBNode node) {
        if (root == nil) {
            return null;
        }

        if (id < node.event.ID) {
            if (node.left != nil) {
                return findNode(id, node.left);
            }
        } else if (id > node.event.ID) {
            if (node.right != nil) {
                return findNode(id, node.right);
            }
        } else if (id == node.event.ID) {
            return node;
        }
        return null;
    }

    //insert a new event in the tree
    private void insert(Event event) {
        RBNode node = new RBNode(event);
        RBNode temp = root;
        if (root == nil) {
            root = node;
            node.color = BLACK;
            node.parent = nil;
        } else {
            node.color = RED;
            while (true) {
                if (node.event.ID < temp.event.ID) {
                    if (temp.left == nil) {
                        temp.left = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.left;
                    }
                } else if (node.event.ID >= temp.event.ID) {
                    if (temp.right == nil) {
                        temp.right = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.right;
                    }
                }
            }
            fixTree(node);
        }
    }

    // fix the tree after an insert operation, ensuring Red Black tree properties
    private void fixTree(RBNode node) {
        while (node.parent.color == RED) {
            RBNode uncle = nil;
            if (node.parent == node.parent.parent.left) {
                uncle = node.parent.parent.right;

                if (uncle != nil && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.right) {
                    node = node.parent;
                    rotateLeft(node);
                }
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
                rotateRight(node.parent.parent);
            } else {
                uncle = node.parent.parent.left;
                if (uncle != nil && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.left) {
                    node = node.parent;
                    rotateRight(node);
                }
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
                rotateLeft(node.parent.parent);
            }
        }
        root.color = BLACK;
    }

    //rotate left to fix tree
    void rotateLeft(RBNode node) {
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
            node.right.parent = node.parent;
            node.parent = node.right;
            if (node.right.left != nil) {
                node.right.left.parent = node;
            }
            node.right = node.right.left;
            node.parent.left = node;
        } else {
            RBNode right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = nil;
            root = right;
        }
    }
    //rotate right to fix tree
    void rotateRight(RBNode node) {
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }

            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != nil) {
                node.left.right.parent = node;
            }
            node.left = node.left.right;
            node.parent.right = node;
        } else {
            RBNode left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = nil;
            root = left;
        }
    }

//delete tree, for debug
    void deleteTree(){
        root = nil;
    }

    // transplant a node with another node
    void transplant(RBNode target, RBNode with){
        if(target.parent == nil){
            root = with;
        }else if(target == target.parent.left){
            target.parent.left = with;
        }else
            target.parent.right = with;
        with.parent = target.parent;
    }

    // delete a given node
    boolean deleteNode(RBNode node){
        if(node == null)return false;
        RBNode x;
        RBNode y = node;
        boolean y_original_color = y.color;

        if(node.left == nil){
            x = node.right;
            transplant(node, node.right);
        }else if(node.right == nil){
            x = node.left;
            transplant(node, node.left);
        }else{
            y = treeMinimum(node.right);
            y_original_color = y.color;
            x = y.right;
            if(y.parent == node)
                x.parent = y;
            else{
                transplant(y, y.right);
                y.right = node.right;
                y.right.parent = y;
            }
            transplant(node, y);
            y.left = node.left;
            y.left.parent = y;
            y.color = node.color;
        }
        if(y_original_color==BLACK)
            deleteFixup(x);
        return true;
    }

    // fix tree after a delete operation, to maintain Red Black tree properties
    void deleteFixup(RBNode x){
        while(x!=root && x.color == BLACK){
            if(x == x.parent.left){
                RBNode w = x.parent.right;
                if(w.color == RED){
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == BLACK && w.right.color == BLACK){
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.right.color == BLACK){
                    w.left.color = BLACK;
                    w.color = RED;
                    rotateRight(w);
                    w = x.parent.right;
                }
                if(w.right.color == RED){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            }else{
                RBNode w = x.parent.left;
                if(w.color == RED){
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == BLACK && w.left.color == BLACK){
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == BLACK){
                    w.right.color = BLACK;
                    w.color = RED;
                    rotateLeft(w);
                    w = x.parent.left;
                }
                if(w.left.color == RED){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    // find node with minimum event ID
    private RBNode treeMinimum(RBNode subTreeRoot){
        while(subTreeRoot.left!=nil){
            subTreeRoot = subTreeRoot.left;
        }
        return subTreeRoot;
    }

    // find node with max event ID
    private RBNode treeMaximum(RBNode subTreeRoot){
        while(subTreeRoot.right!=nil){
            subTreeRoot = subTreeRoot.right;
        }
        return subTreeRoot;
    }

    // initialize the tree using a sorted array
    public void initializeWithSortedArray(Event[] events){

        boolean isLevelOneBlack = true;
        if(findHeight(events.length)%2 == 0)
            isLevelOneBlack = false;
        root = recursiveInsert(events, 0, events.length-1, isLevelOneBlack);
        colorNodes(root, isLevelOneBlack);
    }

    // insert recursively the middle element from array and make elements on the eft as its left subtree and on right as right subtree
    private RBNode recursiveInsert(Event[] events, int start, int end, boolean isLevelOneBlack){
        if(start > end){
            return nil;
        }
        int mid = end - (end - start)/2;
        RBNode node = new RBNode(events[mid]);


        node.left = recursiveInsert(events, start, mid-1, isLevelOneBlack);
        node.left.parent = node;
       /* if(node.left.parent.parent.event.ID == -1 && isLevelOneBlack)
            node.left.color = RED;
        else*/
            //node.left.color = !node.color;

        node.right = recursiveInsert(events, mid+1, end, isLevelOneBlack);
        node.right.parent = node;
        /*if(node.right.parent.parent.event.ID == -1 && isLevelOneBlack)
            node.right.color = RED;
        else*/
            //node.right.color = !node.color;
        return node;
    }

    // find height of the tree using the number of nodes in the tree
    private double findHeight(int numberOfNodes){
        return (Math.ceil(Math.log(numberOfNodes+1)/Math.log(2)));
    }

    //color the nodes iteratively after insertion into BST
    private void colorNodes(RBNode root, boolean isLevelOneBlack){
        Queue<RBNode> queue = new LinkedList<>();
        queue.add(root);
        root.color = BLACK;
        while(!queue.isEmpty()){
            RBNode node = queue.poll();
            if(node.right!= nil && node.right!=null) {
                queue.add(node.right);
                //node.right.parent = node;
                if(isLevelOneBlack && node == root)
                    node.right.color = BLACK;
                else
                    node.right.color = !node.color;
            }
            if(node.left!= nil && node.left!=null) {
                queue.add(node.left);
                //node.left.parent = node;
                if(isLevelOneBlack && node == root)
                    node.left.color = BLACK;
                else
                    node.left.color = !node.color;
            }
        }
    }

    // increase the count for an event, or insert if the Event is not present
    public int increaseCountForID(int id, int increaseBy) {
        RBNode node = findNode(id, root);
    if (node != null) {
            node.setCountForEvent(node.event.count + increaseBy);
            return node.event.getCount();
        }
        else{
            insert(new Event(id, increaseBy));
            return increaseBy;
        }
    }
    // decrease the count for an event, or delete if the Event count <0
    public int reduceCountForID(int id, int decreaseBy) {
        RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        if (node != null) {
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

    // get count for en event given its ID
    public int getCountForEventID(int id) {
        RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        if (node != null)
            return node.event.getCount();
        else
            return 0;
    }

    // get events in range
    private int getEventsInRange(RBNode node, int id1, int id2) {

        if (node == nil || node == null)
            return 0;
        if (id1 <= node.event.ID && node.event.ID <= id2)
            return node.event.getCount() + getEventsInRange(node.left, id1, id2) + getEventsInRange(node.right, id1, id2);

        else if (id1 > node.event.ID)
            return getEventsInRange(node.right, id1, id2);

        else
            return getEventsInRange(node.left, id1, id2);
    }

    // get count for events in range
    public int getCountOfEventsInRange(int id1, int id2) {
        RBTree rbTree = RBTree.getInstance();
        return getEventsInRange(rbTree.root, id1, id2);
    }

    // get next node of an Event with given ID
    private RBNode getNextNode(int id, RBNode node, RBNode bestUntilNow, RBNode max) {
        if(id >= max.event.ID)
            return new RBNode(new Event(0,0));
        if(bestUntilNow.event.ID == -1 )
            bestUntilNow = node;
        if(node.event.ID > id && bestUntilNow.event.ID > node.event.ID)
            bestUntilNow = node;
        if(id < node.event.ID){
            if(node.left != nil && node.left != null)
                return getNextNode(id, node.left, bestUntilNow, max);
            else{
                return node;
            }
        }
        else {
            if (node.right != nil && node.right != null)
                return getNextNode(id, node.right, bestUntilNow, max);
            else if (node.event.ID <= id)
                return bestUntilNow;
            else
                return node;
        }
    }

    // get previous node of an Event with given ID
    private RBNode getPreviousNode(int id, RBNode node, RBNode bestUntilNow, RBNode min) {
        if(id <= min.event.ID)
            return new RBNode(new Event(0,0));
        if(node.event.ID < id && bestUntilNow.event.ID < node.event.ID)
            bestUntilNow = node;
        if(id > node.event.ID){
            if(node.right != nil && node.right != null)
                return getPreviousNode(id, node.right, bestUntilNow, min);
            else{
                return node;
            }
        }
        else {
            if(node.left != nil && node.left != null)
                return getPreviousNode(id, node.left, bestUntilNow, min);
            else if(node.event.ID >= id)
                return bestUntilNow;
            else
                return node;
        }
    }

    // get next event with given ID or 0 0 if not present
    public Event getNextEvent(int id) {
        /*RBTree rbTree = RBTree.getInstance();
        RBNode node = rbTree.findNode(id, rbTree.root);
        if (node != RBNode.nil && node != null) {
            node = node.right.left;
            while ((node.left != RBNode.nil && node.left != null) && node.left.event.ID != -1) {
                node = node.left;
            }
            return node.event;
        } else return null;*/
        RBNode maxNode = treeMaximum(root);
        RBNode rbNode = getNextNode(id, root, nil, maxNode);
        return rbNode.event;
    }

    // get previous event with given ID or 0 0 if not present
    public Event getPreviousEvent(int id) {
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
        RBNode minNode = treeMinimum(root);
        RBNode rbNode = getPreviousNode(id, root, nil, minNode);
        return rbNode.event;
    }
}
