package com.example.menstrualproductlocator.NearestSupplyAlgorithm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class FindNearestSupply {

    public static <T> Map<Vertex<T>, Integer> FindNearestSupply(Vertex<T> start,
                                                        Graph<T> graph) {

        Set<Vertex<T>> vertices = graph.getVertices();
        Map<Vertex<T>, Integer> map = new HashMap<>();
        HashSet<Vertex<T>> visitedSet = new HashSet<>();
        PriorityQueue<VertexDistance<T>> queue = new PriorityQueue<>();

        for (Vertex<T> vertex : graph.getVertices()) {
            map.put(vertex, Integer.MAX_VALUE);
        }
        queue.add(new VertexDistance<>(start, 0));
        while (!queue.isEmpty() && visitedSet.size() < graph.getVertices().size()) {
            VertexDistance<T> vd = queue.remove();
            if (!visitedSet.contains(vd.getVertex())) {
                visitedSet.add(vd.getVertex());

                if (vd.getDistance() < map.get(vd.getVertex())) {
                    map.put(vd.getVertex(), vd.getDistance());
                }

                Map<Vertex<T>, List<VertexDistance<T>>> adjList = graph.getAdjacentList();
                List<VertexDistance<T>> adjacent = adjList.get(vd.getVertex());
                for (VertexDistance<T> x : adjacent) {
                    if (!visitedSet.contains(x.getVertex())) {
                        queue.add(new VertexDistance<>(x.getVertex(), x.getDistance() + vd.getDistance()));
                    }
                }
            }
        }
        return map;
    }

    public static <T> String getShortestWalkingTime(Vertex<T> start,
                                       Graph<T> graph) {
        Map<Vertex<T>, Integer> map = FindNearestSupply(start, graph);
        map.remove(start);
        Map.Entry<Vertex<T>, Integer> minDistance = null;
        for (Map.Entry<Vertex<T>, Integer> entry : map.entrySet()) {
            if (minDistance == null || minDistance.getValue() > entry.getValue()) {
                minDistance = entry;
            }
        }
        return minDistance.getValue().toString();
    }

    public static <T> Vertex<T> getShortestVertex(Vertex<T> start, Graph<T> graph) {
        Map<Vertex<T>, Integer> map = FindNearestSupply(start, graph);
        map.remove(start);
        Map.Entry<Vertex<T>, Integer> minDistance = null;
        for (Map.Entry<Vertex<T>, Integer> entry : map.entrySet()) {
            if (minDistance == null || minDistance.getValue() > entry.getValue()) {
                minDistance = entry;
            }
        }
        return minDistance.getKey();
    }
}
