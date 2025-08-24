package com.example.ED_P1_Grupo02.Tdas;

import java.util.LinkedList;

public class BSTree <E,K extends Comparable<K>>{

    private BSTnode<E, K> root;

    public BSTree() {
        this.root = null; }
    public BSTree(BSTnode<E, K> root) {
        this.root = root; }

    public BSTnode<E, K> getRoot() {
        return root; }
    public void setRoot(BSTnode<E, K> root) {
        this.root = root; }

    public boolean isEmpty() {
        return root == null; }


    public void insertNode(E element, K key) {
        BSTnode<E, K> nodo = new BSTnode<>(element, key);
        if (root == null) {
            root = nodo;
            return;
        }
        BSTnode<E, K> current = root;
        BSTnode<E, K> parent = null;

        while (current != null) {
            parent = current;
            if (key.compareTo(current.getKey()) < 0) {
                current = current.getLeft().getRoot();
            } else {
                current = current.getRight().getRoot();
            }
        }

        if (key.compareTo(parent.getKey()) < 0) {
            parent.setLeft(new BSTree<>(nodo));
        } else {
            parent.setRight(new BSTree<>(nodo));
        }
    }


    public BSTnode<E, K> searchNode(K key) {
        BSTnode<E, K> current = root;
        while (current != null) {
            int cmp = key.compareTo(current.getKey());
            if (cmp == 0) return current;
            else if (cmp < 0) current = current.getLeft().getRoot();
            else current = current.getRight().getRoot();
        }
        return null;
    }

    public void inOrden(BSTnode<E, K> nodo, LinkedList<E> lista) {
        if (nodo != null) {
            inOrden(nodo.getLeft().getRoot(), lista);
            lista.add(nodo.getContent());
            inOrden(nodo.getRight().getRoot(), lista);
        }
    }


    public void delete(K key) {
        root = deleteNode(root, key);
    }

    private BSTnode<E, K> deleteNode(BSTnode<E, K> current, K key) {
        if (current == null) return null;

        int cmp = key.compareTo(current.getKey());
        if (cmp < 0) current.setLeft(new BSTree<>(deleteNode(current.getLeft().getRoot(), key)));
        else if (cmp > 0) current.setRight(new BSTree<>(deleteNode(current.getRight().getRoot(), key)));
        else {
            if (current.getLeft().getRoot() == null) return current.getRight().getRoot();
            else if (current.getRight().getRoot() == null) return current.getLeft().getRoot();
            else {
                BSTnode<E, K> minNode = findMin(current.getRight().getRoot());
                current.setKey(minNode.getKey());
                current.setContent(minNode.getContent());
                current.setRight(new BSTree<>(deleteNode(current.getRight().getRoot(), minNode.getKey())));
            }
        }
        return current;
    }

    private BSTnode<E, K> findMin(BSTnode<E, K> nodo) {
        while (nodo.getLeft() != null && nodo.getLeft().getRoot() != null) {
            nodo = nodo.getLeft().getRoot();
        }
        return nodo;
    }
}
