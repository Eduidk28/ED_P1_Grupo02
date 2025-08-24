package com.example.ED_P1_Grupo02.Tdas;

import java.io.Serializable;

public class Edge <E, V> implements Serializable {
    private Vertex<V, E> source;
    private Vertex<V, E> target;
    private int weight;
    private E data;

    public Edge(Vertex<V, E> source, Vertex<V, E> target, int weight, E data) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        this.data = data;
    }

    public Vertex<V, E> getSource() {
        return source;
    }

    public Vertex<V, E> getTarget() {
        return target;
    }

    public int getWeight() {
        return weight;
    }

    public E getData() {
        return data;
    }

    public void setSource(Vertex<V, E> source) {
        this.source = source;
    }

    public void setTarget(Vertex<V, E> target) {
        this.target = target;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setData(E data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "source=" + source +
                ", target=" + target +
                ", weight=" + weight +
                ", data=" + data +
                '}';
    }
}
