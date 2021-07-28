package com.example.menstrualproductlocator.NearestSupplyAlgorithm;

public final class VertexDistance<T>
        implements Comparable<VertexDistance<? super T>> {

    private final Vertex<T> vertex;
    private final int distance;

    public VertexDistance(Vertex<T> vertex, int distance) {
        this.vertex = vertex;
        this.distance = distance;
    }

    public Vertex<T> getVertex() {
        return vertex;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public int hashCode() {
        return vertex.hashCode() ^ distance;
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof VertexDistance<?>) {
            VertexDistance<?> edge = (VertexDistance<?>) object;
            return distance == edge.distance && vertex.equals(edge.vertex);
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(VertexDistance<? super T> pair) {
        return this.getDistance() - pair.getDistance();
    }

    @Override
    public String toString() {
        return "Pair with vertex " + vertex + " and distance " + distance;
    }
}
