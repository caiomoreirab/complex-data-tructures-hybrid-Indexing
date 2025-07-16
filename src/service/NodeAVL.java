package service;

public class NodeAVL<T> {
    T element;
    NodeAVL<T> left;
    NodeAVL<T> right;
    int height;

    public NodeAVL(T e) {
        this.element = e;
        this.left = null;
        this.right = null;
        this.height = 0;
    }
}