package com.example.ED_P1_Grupo02.Tdas;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

public class GraphAL<V, E> implements Serializable {
    private static final long serialVersionUID = 256517976409595642L;
    private LinkedList<Vertex<V,E>> vertices;
    private boolean isDirected= true;
    private Comparator<V> cmp;


    public GraphAL(boolean isDirected, Comparator<V> cmp) {
        this.vertices= new LinkedList<>();
        this.isDirected = isDirected;
        this.cmp = cmp;
    }

    public LinkedList<Vertex<V, E>> getVertices() {
        return vertices;
    }

    public void setVertices(LinkedList<Vertex<V, E>> vertices) {
        this.vertices = vertices;
    }

    public boolean isDirected() {
        return isDirected;
    }

    public void setDirected(boolean directed) {
        isDirected = directed;
    }

    public Comparator<V> getCmp() {
        return cmp;
    }

    public void setCmp(Comparator<V> cmp) {
        this.cmp = cmp;
    }

    public Vertex<V,E> getVertexByContent(V content){
        for(Vertex<V,E> v : vertices){
            V c= v.getContent();
            if(this.cmp.compare(content, c)==0){
                return v;
            }
        }
        return null;
    }
    public boolean containsVertex(V content){
        return getVertexByContent(content) != null;
    }
    public boolean addVertex(V content){
        if(content==null || getVertexByContent(content)!=null){
            return false;
        }
        Vertex<V,E> newVertex= new Vertex<>(content);
        this.vertices.add(newVertex);
        return true;
    }
    public boolean removeVertex(V content) {
        Vertex<V, E> toRemove = getVertexByContent(content);
        if (toRemove == null) return false;

        for (Vertex<V, E> v : vertices) {
            v.getEdges().removeIf(edge -> edge.getTarget().equals(toRemove));
        }

        return vertices.remove(toRemove);
    }
    public boolean connect(V content1, V content2, int weight, E data){
        if(content1== null || content2==null){
            return false;
        }
        Vertex<V,E> v1= getVertexByContent(content1);
        Vertex<V,E> v2= getVertexByContent(content2);
        if(v1== null || v2== null){
            return false;
        }
        Edge<E,V> newEdge= new Edge<>(v1, v2, weight, data);
        v1.getEdges().add(newEdge);
        if(!this.isDirected){
            Edge<E,V> reversedEdge= new Edge<>(v2,v1, weight,data);
            v2.getEdges().add(newEdge);
        }
        return true;
    }
    public boolean disconnect(V content1, V content2, E data) {
        if (content1 == null || content2 == null || data == null) {
            return false;
        }

        Vertex<V, E> v1 = getVertexByContent(content1);
        Vertex<V, E> v2 = getVertexByContent(content2);

        if (v1 == null || v2 == null) {
            return false;
        }

        boolean removed = false;

        for (int i = 0; i < v1.getEdges().size(); i++) {
            Edge<E, V> edge = v1.getEdges().get(i);
            if (edge.getTarget().equals(v2) && edge.getData().equals(data)) {
                v1.getEdges().remove(i);
                removed = true;
                break;
            }
        }

        if (removed && !isDirected) {
            for (int i = 0; i < v2.getEdges().size(); i++) {
                Edge<E, V> edge = v2.getEdges().get(i);
                if (edge.getTarget().equals(v1) && edge.getData().equals(data)) {
                    v2.getEdges().remove(i);
                    break;
                }
            }
        }

        return removed;
    }


    public int getDegree(V content) {
        Vertex<V, E> v = getVertexByContent(content);
        if (v == null) return -1;
        return v.getEdges().size();
    }

    public LinkedList<V> getAdjacents(V content) {
        LinkedList<V> adj = new LinkedList<>();
        Vertex<V, E> v = getVertexByContent(content);
        if (v != null) {
            for (Edge<E, V> e : v.getEdges()) {
                adj.add(e.getTarget().getContent());
            }
        }
        return adj;
    }


    public int gradoSalida(V content) {
        Vertex<V, E> v = getVertexByContent(content);
        if (v == null) return -1;
        return v.getEdges().size();
    }


    public int gradoEntrada(V content) {
        Vertex<V, E> v = getVertexByContent(content);
        if (v == null) return -1;

        int count = 0;
        for (Vertex<V, E> other : vertices) {
            for (Edge<E, V> edge : other.getEdges()) {
                if (edge.getTarget().equals(v)) {
                    count++;
                }
            }
        }
        return count;
    }

    public int gradoTotal(V content) {
        return gradoSalida(content) + gradoEntrada(content);
    }

    public LinkedList<V> dijkstra(V sourceContent, V targetContent) {
        Vertex<V, E> source = getVertexByContent(sourceContent);
        Vertex<V, E> target = getVertexByContent(targetContent);
        if (source == null || target == null) {
            return null;
        }
        Map<Vertex<V, E>, Integer> distancia = new HashMap<>();
        Map<Vertex<V, E>, Vertex<V, E>> prev = new HashMap<>();
        for (Vertex<V, E> ver : vertices) {
            distancia.put(ver, Integer.MAX_VALUE);
            prev.put(ver, null);
        }
        distancia.put(source, 0);
        PriorityQueue<Vertex<V, E>> pq = new PriorityQueue<>(Comparator.comparingInt(distancia::get));
        pq.add(source);

        while (!pq.isEmpty()) {
            Vertex<V, E> u = pq.poll();
            if (u.equals(target)) {
                break;
            }

            for (Edge<E, V> edge : u.getEdges()) {
                Vertex<V, E> v = edge.getTarget();
                int newDist = distancia.get(u) + edge.getWeight();

                if (newDist < distancia.get(v)) {
                    distancia.put(v, newDist);
                    prev.put(v, u);
                    pq.add(v);
                }
            }
        }
        LinkedList<V> ruta = new LinkedList<>();
        Vertex<V, E> step = target;

        if (prev.get(step) == null && !step.equals(source)) {
            return null;
        }
        while (step != null) {
            ruta.addFirst(step.getContent());
            step = prev.get(step);
        }
        System.out.println("Distancia total: " + distancia.get(target) + " km");
        return ruta;
    }
}
