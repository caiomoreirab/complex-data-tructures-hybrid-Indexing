package service;

import java.util.List;

public interface BalancedTree<T extends Comparable<T>> {
    void insert(T valorElemento);

    boolean remove(T valorElemento);

    boolean find(T valorElemento);

    int getHeight();

    void printInOrder();

    void collectInOrder(List<T> listaColetada);
}