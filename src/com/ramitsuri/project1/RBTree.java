package com.ramitsuri.project1;

import java.util.Scanner;

/**
 * Created by ramitsuri on 3/13/16.
 */

public class RBTree {

    private final RBNode nil = new RBNode(new Event(-1,0));
    private RBNode root = nil;

    private RBNode findNode(RBNode nodeToFind, RBNode node){
        int compare = nodeToFind.compare(node);

        if(root == nil)
            return null;

        if(compare < 0){
            if(node.left != nil)
                return findNode(nodeToFind, node.left);
        }
        else if(compare > 0){
            if(node.right != nil)
                return findNode(nodeToFind, node.right);
        }
        else if(compare == 0)
            return node;

        return null;
    }

    
    private void insert(RBNode node){
        RBNode temp = root;
        if(root == nil){
            root = node;
            node.color = Color.BLACK;
            node.parent = nil;
        }
        else{
            node.color = Color.RED;
            while(true){
                if(node.compare(temp)<0){
                    if(temp.left == nil){
                        temp.left = node;
                        node.parent = temp;
                        break;
                    }
                    else
                        temp = temp.left;
                }
                else if(node.compare(temp) == 0 || node.compare(temp) > 0){
                    if(temp.right == nil){
                        temp.right = node;
                        node.parent = temp;
                        break;
                    }
                    else
                        temp = temp.right;
                }
            }
            fixTree(node);
        }
    }


    private void fixTree(RBNode node){
        while(node.parent.color == Color.RED){
            RBNode uncle = nil;
            if(node.parent == node.parent.parent.left){
                uncle = node.parent.parent.right;
                if(uncle != nil && uncle.color == Color.RED){
                    node.parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    node = node.parent.parent;
                    continue;
                }
                if(node == node.parent.right){
                    node = node.parent;
                    rotateLeft(node);
                }
                node.parent.color = Color.BLACK;
                node.parent.parent.color = Color.RED;
                rotateRight(node.parent.parent);
            }
            else{
                uncle = node.parent.parent.left;
                if(uncle != nil && uncle.color == Color.RED){
                    node.parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    node = node.parent.parent;
                    continue;
                }
                if(node == node.parent.left){
                    node = node.parent;
                    rotateRight(node);
                }
                node.parent.color = Color.BLACK;
                node.parent.parent.color = Color.RED;
                rotateLeft(node.parent.parent);
            }
        }
        root.color = Color.BLACK;
    }

    private void rotateRight(RBNode node) {
        if (node.parent != nil) {
            if (node == node.parent.left)
                node.parent.left = node.left;
            else
                node.parent.right = node.left;

            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != nil)
                node.left.right.parent = node;
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

    private void rotateLeft(RBNode node) {
        if (node.parent != nil) {
            if (node == node.parent.left)
                node.parent.left = node.right;
            else
                node.parent.right = node.right;

            node.right.parent = node.parent;
            node.parent = node.right;
            if (node.right.left != nil)
                node.right.left.parent = node;
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

    void transplant(RBNode target, RBNode with){
        if(target.parent == nil)
            root = with;
        else if(target == target.parent.left)
            target.parent.left = with;
        else
            target.parent.right = with;
        with.parent = target.parent;
    }

    boolean delete(RBNode z){
        if((z = findNode(z, root))==null)
            return false;
        RBNode x;
        RBNode y = z;
        boolean y_original_color = y.color;

        if(z.left == nil){
            x = z.right;
            transplant(z, z.right);
        }else if(z.right == nil){
            x = z.left;
            transplant(z, z.left);
        }else{
            y = treeMinimum(z.right);
            y_original_color = y.color;
            x = y.right;
            if(y.parent == z)
                x.parent = y;
            else{
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if(y_original_color==Color.BLACK)
            deleteFixup(x);
        return true;
    }

    void deleteFixup(RBNode x){
        while(x!=root && x.color == Color.BLACK){
            if(x == x.parent.left){
                RBNode w = x.parent.right;
                if(w.color == Color.RED){
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == Color.BLACK && w.right.color == Color.BLACK){
                    w.color = Color.RED;
                    x = x.parent;
                    continue;
                }
                else if(w.right.color == Color.BLACK){
                    w.left.color = Color.BLACK;
                    w.color = Color.RED;
                    rotateRight(w);
                    w = x.parent.right;
                }
                if(w.right.color == Color.RED){
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.right.color = Color.BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            }else{
                RBNode w = x.parent.left;
                if(w.color == Color.RED){
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == Color.BLACK && w.left.color == Color.BLACK){
                    w.color = Color.RED;
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == Color.BLACK){
                    w.right.color = Color.BLACK;
                    w.color = Color.RED;
                    rotateLeft(w);
                    w = x.parent.left;
                }
                if(w.left.color == Color.RED){
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.left.color = Color.BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = Color.BLACK;
    }

    void deleteTree(){
        root = nil;
    }


    public void printTree(RBNode node) {
        if (node == nil) {
            return;
        }
        printTree(node.left);
        System.out.print(((node.color==Color.RED)?"Color: Red ":"Color: Black ")+"Key: "+node.event.ID+" Parent: "+node.parent.event.ID+"\n");
        printTree(node.right);
    }

    RBNode treeMinimum(RBNode subTreeRoot){
        while(subTreeRoot.left!=nil){
            subTreeRoot = subTreeRoot.left;
        }
        return subTreeRoot;
    }


    public void consoleUI() {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("\n1.- Add items\n"
                    + "2.- Delete items\n"
                    + "3.- Check items\n"
                    + "4.- Print tree\n"
                    + "5.- Delete tree\n");
            int choice = scan.nextInt();

            int item;
            RBNode node;
            switch (choice) {
                case 1:
                    item = scan.nextInt();
                    while (item != -999) {
                        node = new RBNode(new Event(item, item));
                        insert(node);
                        item = scan.nextInt();
                    }
                    printTree(root);
                    break;
                case 2:
                    item = scan.nextInt();
                    while (item != -999) {
                        node = new RBNode(new Event(item, item));
                        System.out.print("\nDeleting item " + item);
                        if (delete(node)) {
                            System.out.print(": deleted!");
                        } else {
                            System.out.print(": does not exist!");
                        }
                        item = scan.nextInt();
                    }
                    System.out.println();
                    printTree(root);
                    break;
                case 3:
                    item = scan.nextInt();
                    while (item != -999) {
                        node = new RBNode(new Event(item, item));
                        System.out.println((findNode(node, root) != null) ? "found" : "not found");
                        item = scan.nextInt();
                    }
                    break;
                case 4:
                    printTree(root);
                    break;
                case 5:
                    deleteTree();
                    System.out.println("Tree deleted!");
                    break;
            }
        }
    }


    public static void main(String args[]){
        /*Event event = new Event(-1,0);
        final RBNode nil = new RBNode(event);

        RBNode nil2 = new RBNode(new Event(-1,0));
        System.out.println(nil.compare(nil2));*/

        RBTree rbt = new RBTree();
        rbt.consoleUI();

    }

}