package com.example.menstrualproductlocator.NearestSupplyAlgorithm;

public class Edge<T> implements Comparable<Edge<? super T>> {

    private Vertex<T> start;
    private Vertex<T> end;
    private int weight;

    public Edge(Vertex<T> start, Vertex<T> end, int weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    @Override
    public int hashCode() {
        return start.hashCode() ^ end.hashCode() ^ weight;
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof Edge<?>) {
            Edge<?> edge = (Edge<?>) object;
            return weight == edge.weight && start.equals(edge.start) && end.equals(edge.end);
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Edge<? super T> edge) {
        return weight - edge.getWeight();
    }

    public int getWeight() {
        return weight;
    }

    public Vertex<T> getStart() {
        return start;
    }

    public Vertex<T> getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "Edge from " + start + " to " + end + " with weight " + weight;
    }

}
