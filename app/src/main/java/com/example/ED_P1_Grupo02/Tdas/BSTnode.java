package com.example.ED_P1_Grupo02.Tdas;

public class BSTnode<E,K extends Comparable<K>> {
    private E content ;
    private K key ;
    private BSTree<E,K> left ;
    private BSTree<E,K> right ;

    public BSTnode (E content,K key){
        this.content = content ;
        this.key = key ;
        this.left = new BSTree<>();
        this.right = new BSTree<>();

    }

    public E getContent() {

        return content;
    }

    public void setContent(E content) {

        this.content = content;
    }

    public K getKey() {

        return key;
    }

    public void setKey(K key) {

        this.key = key;
    }

    public BSTree<E, K> getLeft() {
        return left;
    }

    public void setLeft(BSTree<E, K> left) {

        this.left = left;
    }

    public BSTree<E, K> getRight() {

        return right;
    }

    public void setRight(BSTree<E, K> right) {

        this.right = right;
    }

    @Override
    public String toString() {
        return "BSTnode{" +
                "content=" + content +
                '}';
    }
}
