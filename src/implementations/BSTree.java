package implementations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import utilities.BSTreeADT;
import utilities.Iterator;

public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E>, Serializable {
    private static final long serialVersionUID = 1L;
    private BSTreeNode<E> root;
    private int size;

    public BSTree() {
        root = null;
        size = 0;
    }

    @Override
    public BSTreeNode<E> getRoot() throws NullPointerException {
        if (root == null) throw new NullPointerException("Tree is empty");
        return root;
    }

    @Override
    public int getHeight() {
        return getHeight(root);
    }

    private int getHeight(BSTreeNode<E> node) {
        if (node == null) return 0;
        return 1 + Math.max(getHeight(node.getLeft()), getHeight(node.getRight()));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean contains(E entry) throws NullPointerException {
        if (entry == null) throw new NullPointerException("Entry cannot be null");
        return search(entry) != null;
    }

    @Override
    public BSTreeNode<E> search(E entry) throws NullPointerException {
        if (entry == null) throw new NullPointerException("Entry cannot be null");
        return search(root, entry);
    }

    private BSTreeNode<E> search(BSTreeNode<E> node, E entry) {
        if (node == null) return null;
        
        int comparison = entry.compareTo(node.getElement());
        if (comparison == 0) return node;
        if (comparison < 0) return search(node.getLeft(), entry);
        return search(node.getRight(), entry);
    }

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

    @Override
    public Iterator<E> inorderIterator() {
        return new TreeIterator(TreeTraversalOrder.INORDER);
    }

    @Override
    public Iterator<E> preorderIterator() {
        return new TreeIterator(TreeTraversalOrder.PREORDER);
    }

    @Override
    public Iterator<E> postorderIterator() {
        return new TreeIterator(TreeTraversalOrder.POSTORDER);
    }

    private enum TreeTraversalOrder {
        PREORDER, INORDER, POSTORDER
    }

    private class TreeIterator implements Iterator<E> {
        private ArrayList<E> elements;
        private int currentIndex;

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

        private void preorderTraverse(BSTreeNode<E> node) {
            if (node == null) return;
            elements.add(node.getElement());
            preorderTraverse(node.getLeft());
            preorderTraverse(node.getRight());
        }

        private void inorderTraverse(BSTreeNode<E> node) {
            if (node == null) return;
            inorderTraverse(node.getLeft());
            elements.add(node.getElement());
            inorderTraverse(node.getRight());
        }

        private void postorderTraverse(BSTreeNode<E> node) {
            if (node == null) return;
            postorderTraverse(node.getLeft());
            postorderTraverse(node.getRight());
            elements.add(node.getElement());
        }

        @Override
        public boolean hasNext() {
            return currentIndex < elements.size();
        }

        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext()) throw new NoSuchElementException();
            return elements.get(currentIndex++);
        }
    }
}