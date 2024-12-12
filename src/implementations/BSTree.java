package implementations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import utilities.BSTreeADT;
import utilities.Iterator;

/**
 * BSTree.java
 *
 * A class that implements a Binary Search Tree (BST). The BST is a data structure where each node has at most two children.
 * The elements are stored in a way that ensures the left child of a node is less than the node's element, 
 * and the right child is greater than or equal to the node's element.
 * 
 * @param <E> The type of element stored in this tree, which must be comparable.
 */
public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E>, Serializable {
    private static final long serialVersionUID = 1L;  
    private BSTreeNode<E> root; 
    private int size;  

    /**
     * Constructs an empty BSTree with a null root and size 0.
     */
    public BSTree() {
        root = null;
        size = 0;
    }

    /**
     * Returns the root node of the tree.
     * 
     * @return The root node.
     * @throws NullPointerException If the tree is empty.
     */
    @Override
    public BSTreeNode<E> getRoot() throws NullPointerException {
        if (root == null) throw new NullPointerException("Tree is empty");
        return root;
    }

    /**
     * Returns the height of the tree, defined as the length of the longest path from the root to a leaf.
     * 
     * @return The height of the tree.
     */
    @Override
    public int getHeight() {
        return getHeight(root);
    }

    /**
     * Recursively computes the height of the tree from a given node.
     * 
     * @param node The node to calculate the height from.
     * @return The height of the subtree rooted at the given node.
     */
    private int getHeight(BSTreeNode<E> node) {
        if (node == null) return 0;  // An empty subtree has height 0
        return 1 + Math.max(getHeight(node.getLeft()), getHeight(node.getRight()));
    }

    /**
     * Returns the size (number of elements) of the tree.
     * 
     * @return The size of the tree.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Checks if the tree is empty (i.e., it contains no elements).
     * 
     * @return True if the tree is empty, false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the tree by setting the root to null and size to 0.
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Checks if the tree contains a given element.
     * 
     * @param entry The element to search for in the tree.
     * @return True if the element is present, false otherwise.
     * @throws NullPointerException If the entry is null.
     */
    @Override
    public boolean contains(E entry) throws NullPointerException {
        if (entry == null) throw new NullPointerException("Entry cannot be null");
        return search(entry) != null;
    }

    /**
     * Searches for a given element in the tree.
     * 
     * @param entry The element to search for.
     * @return The node containing the element, or null if not found.
     * @throws NullPointerException If the entry is null.
     */
    @Override
    public BSTreeNode<E> search(E entry) throws NullPointerException {
        if (entry == null) throw new NullPointerException("Entry cannot be null");
        return search(root, entry);
    }

    /**
     * Recursively searches for an element starting from a given node.
     * 
     * @param node The node to start searching from.
     * @param entry The element to search for.
     * @return The node containing the element, or null if not found.
     */
    private BSTreeNode<E> search(BSTreeNode<E> node, E entry) {
        if (node == null) return null;
        
        int comparison = entry.compareTo(node.getElement());
        if (comparison == 0) return node;  
        if (comparison < 0) return search(node.getLeft(), entry);  
        return search(node.getRight(), entry);  
    }

    /**
     * Adds a new element to the tree. Duplicates are not allowed.
     * 
     * @param newEntry The element to be added.
     * @return True if the element was added, false if it was already present.
     * @throws NullPointerException If the entry is null.
     */
    @Override
    public boolean add(E newEntry) throws NullPointerException {
        if (newEntry == null) throw new NullPointerException("Entry cannot be null");
        
        if (root == null) {
            root = new BSTreeNode<>(newEntry);
            size++;
            return true;
        }
        
        return add(root, newEntry);
    }

    /**
     * Recursively adds a new element starting from a given node.
     * 
     * @param node The node to start adding from.
     * @param newEntry The element to be added.
     * @return True if the element was added, false if it was already present.
     */
    private boolean add(BSTreeNode<E> node, E newEntry) {
        int comparison = newEntry.compareTo(node.getElement());
        
        if (comparison == 0) return false;  
        
        if (comparison < 0) {
            if (node.getLeft() == null) {
                node.setLeft(new BSTreeNode<>(newEntry));
                size++;
                return true;
            }
            return add(node.getLeft(), newEntry);  
        } else {
            if (node.getRight() == null) {
                node.setRight(new BSTreeNode<>(newEntry));
                size++;
                return true;
            }
            return add(node.getRight(), newEntry);  
        }
    }

    /**
     * Removes the minimum element (leftmost leaf) from the tree.
     * 
     * @return The node containing the removed element, or null if the tree is empty.
     */
    @Override
    public BSTreeNode<E> removeMin() {
        if (isEmpty()) return null;
        
        BSTreeNode<E> parent = null;
        BSTreeNode<E> current = root;
        
        while (current.getLeft() != null) {
            parent = current;
            current = current.getLeft();
        }
        
        if (parent == null) {
            root = current.getRight(); 
        } else {
            parent.setLeft(current.getRight()); 
        }
        
        size--;
        return current;
    }

    /**
     * Removes the maximum element (rightmost leaf) from the tree.
     * 
     * @return The node containing the removed element, or null if the tree is empty.
     */
    @Override
    public BSTreeNode<E> removeMax() {
        if (isEmpty()) return null;
        
        BSTreeNode<E> parent = null;
        BSTreeNode<E> current = root;
        
        while (current.getRight() != null) {
            parent = current;
            current = current.getRight();
        }
        
        if (parent == null) {
            root = current.getLeft(); 
        } else {
            parent.setRight(current.getLeft());
        }
        
        size--;
        return current;
    }

    /**
     * Returns an inorder iterator for the tree.
     * 
     * @return An iterator for inorder traversal.
     */
    @Override
    public Iterator<E> inorderIterator() {
        return new TreeIterator(TreeTraversalOrder.INORDER);
    }

    /**
     * Returns a preorder iterator for the tree.
     * 
     * @return An iterator for preorder traversal.
     */
    @Override
    public Iterator<E> preorderIterator() {
        return new TreeIterator(TreeTraversalOrder.PREORDER);
    }

    /**
     * Returns a postorder iterator for the tree.
     * 
     * @return An iterator for postorder traversal.
     */
    @Override
    public Iterator<E> postorderIterator() {
        return new TreeIterator(TreeTraversalOrder.POSTORDER);
    }

    /**
     * Enum representing the three types of tree traversal orders: PREORDER, INORDER, and POSTORDER.
     */
    private enum TreeTraversalOrder {
        PREORDER, INORDER, POSTORDER
    }

    /**
     * A private class that implements the Iterator interface for traversing the tree in different orders.
     */
    private class TreeIterator implements Iterator<E> {
        private ArrayList<E> elements;  
        private int currentIndex;  

        /**
         * Constructor that initializes the iterator with a specified traversal order.
         * 
         * @param order The order in which to traverse the tree (preorder, inorder, or postorder).
         */
        public TreeIterator(TreeTraversalOrder order) {
            elements = new ArrayList<>();
            currentIndex = 0;
            
            switch (order) {
                case PREORDER:
                    preorderTraverse(root);
                    break;
                case INORDER:
                    inorderTraverse(root);
                    break;
                case POSTORDER:
                    postorderTraverse(root);
                    break;
            }
        }

        /**
         * Recursively traverses the tree in preorder (root, left, right).
         * 
         * @param node The node to start traversing from.
         */
        private void preorderTraverse(BSTreeNode<E> node) {
            if (node == null) return;
            elements.add(node.getElement());
            preorderTraverse(node.getLeft());
            preorderTraverse(node.getRight());
        }

        /**
         * Recursively traverses the tree in inorder (left, root, right).
         * 
         * @param node The node to start traversing from.
         */
        private void inorderTraverse(BSTreeNode<E> node) {
            if (node == null) return;
            inorderTraverse(node.getLeft());
            elements.add(node.getElement());
            inorderTraverse(node.getRight());
        }

        /**
         * Recursively traverses the tree in postorder (left, right, root).
         * 
         * @param node The node to start traversing from.
         */
        private void postorderTraverse(BSTreeNode<E> node) {
            if (node == null) return;
            postorderTraverse(node.getLeft());
            postorderTraverse(node.getRight());
            elements.add(node.getElement());
        }

        /**
         * Checks if there are more elements in the iteration.
         * 
         * @return True if there are more elements, false otherwise.
         */
        @Override
        public boolean hasNext() {
            return currentIndex < elements.size();
        }

        /**
         * Returns the next element in the iteration.
         * 
         * @return The next element.
         * @throws NoSuchElementException If there are no more elements.
         */
        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext()) throw new NoSuchElementException();
            return elements.get(currentIndex++);
        }
    }
}
