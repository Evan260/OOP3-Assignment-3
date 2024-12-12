package implementations;

import java.io.Serializable;

/**
 * BSTreeNode.java
 *
 * A class representing a node in a Binary Search Tree (BST).
 * This node holds an element of type E and references to its left and right children.
 * The element in the node must be comparable, as it will be used for tree comparisons.
 * 
 * @param <E> The type of element stored in this node, which must be comparable.
 */
public class BSTreeNode<E extends Comparable<? super E>> implements Serializable {
    private static final long serialVersionUID = 1L;  // Ensures proper deserialization
    private E element;  // The element stored in the node
    private BSTreeNode<E> left;  // Left child node
    private BSTreeNode<E> right;  // Right child node

    /**
     * Constructs a BSTreeNode with a given element, and sets both left and right children to null.
     * 
     * @param element The element to be stored in this node.
     */
    public BSTreeNode(E element) {
        this.element = element;
        this.left = null;
        this.right = null;
    }

    /**
     * Returns the element stored in this node.
     *
     * @return The element stored in the node.
     */
    public E getElement() { 
        return element; 
    }

    /**
     * Sets the element of this node to the specified value.
     *
     * @param element The new element to be stored in the node.
     */
    public void setElement(E element) { 
        this.element = element; 
    }

    /**
     * Returns the left child node of this node.
     *
     * @return The left child node.
     */
    public BSTreeNode<E> getLeft() { 
        return left; 
    }

    /**
     * Sets the left child node of this node to the specified node.
     *
     * @param left The new left child node.
     */
    public void setLeft(BSTreeNode<E> left) { 
        this.left = left; 
    }

    /**
     * Returns the right child node of this node.
     *
     * @return The right child node.
     */
    public BSTreeNode<E> getRight() { 
        return right; 
    }

    /**
     * Sets the right child node of this node to the specified node.
     *
     * @param right The new right child node.
     */
    public void setRight(BSTreeNode<E> right) { 
        this.right = right; 
    }
}
