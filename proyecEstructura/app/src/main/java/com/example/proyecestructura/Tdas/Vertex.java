package com.example.ED_P1_Grupo02.Tdas;

import java.io.Serializable;
import java.util.LinkedList;

public class Vertex <V, E> implements Serializable {

    private V content;
    private LinkedList<Edge<E, V>> edges;


    public Vertex(V content) {
        this.content = content;
        this.edges= new LinkedList<Edge<E,V>>();
    }

    public V getContent() {
        return content;
    }

    public void setContent(V content) {
        this.content = content;
    }

    public LinkedList<Edge<E, V>> getEdges() {
        return edges;
    }

    public void setEdges(LinkedList<Edge<E, V>> edges) {
        this.edges = edges;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vertex<?, ?> other = (Vertex<?, ?>) obj;
        if (content == null) {
            return other.content == null;
        } else {
            return content.equals(other.content);
        }
    }
    @Override
    public int hashCode() {
        return content == null ? 0 : content.hashCode();
    }
    @Override
    public String toString() {
        return "Vertex{" +
                "content=" + content +
                ", edges=" + edges +
                '}';
    }
}
