package com.example.menstrualproductlocator.NearestSupplyAlgorithm;

public class Vertex<T> {

    private T data;

    public Vertex(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof Vertex) {
            return data.equals(((Vertex<?>) object).data);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return data.toString();
    }

}
