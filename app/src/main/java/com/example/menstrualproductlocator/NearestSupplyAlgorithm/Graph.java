package com.example.menstrualproductlocator.NearestSupplyAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph<T> {

    private Set<Vertex<T>> vertices;
    private Set<Edge<T>> edges;
    private Map<Vertex<T>, List<VertexDistance<T>>> adjacentList;

    public Graph(Set<Vertex<T>> vertices, Set<Edge<T>> edges) {

        this.vertices = new HashSet<>(vertices);
        this.edges = new HashSet<>(edges);
        adjacentList = new HashMap<>();
        for (Vertex<T> vertex : vertices) {
            adjacentList.put(vertex, new ArrayList<>());
        }

        for (Edge<T> edge : edges) {
            if (adjacentList.containsKey(edge.getStart())) {
                adjacentList.get(edge.getStart()).add(
                        new VertexDistance<>(edge.getEnd(), edge.getWeight()));
            }
        }
    }

    public Set<Vertex<T>> getVertices() {
        return vertices;
    }

    public Set<Edge<T>> getEdges() {
        return edges;
    }

    public Map<Vertex<T>, List<VertexDistance<T>>> getAdjacentList() {
        return adjacentList;
    }
}
