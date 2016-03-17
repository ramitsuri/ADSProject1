package com.ramitsuri.project1;
/**
 * Created by ramitsuri on 3/13/16.
 */

public class RBTree {


    public RBNode root = RBNode.nil;
    private static RBTree instance = null;

    public static RBTree getInstance(){
        if(instance == null)
            instance = new RBTree();
        return instance;
    }


    public RBNode findNode(int id, RBNode node) {
        if (root == RBNode.nil) {
            return null;
        }

        if (id < node.event.ID) {
            if (node.left != RBNode.nil) {
                return findNode(id, node.left);
            }
        } else if (id > node.event.ID) {
            if (node.right != RBNode.nil) {
                return findNode(id, node.right);
            }
        } else if (id == node.event.ID) {
            return node;
        }
        return null;
    }

    public void insert(Event event) {
        RBNode node = new RBNode(event);
        RBNode temp = root;
        if (root ==RBNode.nil) {
            root = node;
            node.color = Color.BLACK;
            node.parent =RBNode.nil;
        } else {
            node.color = Color.RED;
            while (true) {
                if (node.event.ID < temp.event.ID) {
                    if (temp.left ==RBNode.nil) {
                        temp.left = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.left;
                    }
                } else if (node.event.ID >= temp.event.ID) {
                    if (temp.right ==RBNode.nil) {
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



   public void fixTree(RBNode node) {
        while (node.parent.color == Color.RED) {
            RBNode uncle =RBNode.nil;
            if (node.parent == node.parent.parent.left) {
                uncle = node.parent.parent.right;

                if (uncle !=RBNode.nil && uncle.color == Color.RED) {
                    node.parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.right) {
                    //Double rotation needed
                    node = node.parent;
                    rotateLeft(node);
                }
                node.parent.color = Color.BLACK;
                node.parent.parent.color = Color.RED;
                //if the "else if" code hasn't executed, this
                //is a case where we only need a single rotation
                rotateRight(node.parent.parent);
            } else {
                uncle = node.parent.parent.left;
                if (uncle !=RBNode.nil && uncle.color == Color.RED) {
                    node.parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.left) {
                    //Double rotation needed
                    node = node.parent;
                    rotateRight(node);
                }
                node.parent.color = Color.BLACK;
                node.parent.parent.color = Color.RED;
                //if the "else if" code hasn't executed, this
                //is a case where we only need a single rotation
                rotateLeft(node.parent.parent);
            }
        }
        root.color = Color.BLACK;
    }
   private void rotateLeft(RBNode node) {
        if (node.parent !=RBNode.nil) {
            if (node == node.parent.left) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
            node.right.parent = node.parent;
            node.parent = node.right;
            if (node.right.left !=RBNode.nil) {
                node.right.left.parent = node;
            }
            node.right = node.right.left;
            node.parent.left = node;
        } else {//Need to rotate root
            RBNode right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent =RBNode.nil;
            root = right;
        }
    }

    private void rotateRight(RBNode node) {
        if (node.parent !=RBNode.nil) {
            if (node == node.parent.left) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }

            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right !=RBNode.nil) {
                node.left.right.parent = node;
            }
            node.left = node.left.right;
            node.parent.right = node;
        } else {//Need to rotate root
            RBNode left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent =RBNode.nil;
            root = left;
        }
    }

    private void transplant(RBNode target, RBNode with){
        if(target.parent ==RBNode.nil){
            root = with;
        }else if(target == target.parent.left){
            target.parent.left = with;
        }else
            target.parent.right = with;
        with.parent = target.parent;
    }

    public boolean deleteNodeWithID(int z){
        RBNode node = findNode(z, root);
        if(node == null)
            return false;
        RBNode x;
        RBNode y = node;
        boolean y_original_color = y.color;

        if(node.left ==RBNode.nil){
            x = node.right;
            transplant(node, node.right);
        }else if(node.right ==RBNode.nil){
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

    public RBNode treeMinimum(RBNode subTreeRoot){
        while(subTreeRoot.left !=RBNode.nil){
            subTreeRoot = subTreeRoot.left;
        }
        return subTreeRoot;
    }
}
