package service;

import java.util.List;
import util.Estatisticas;
import service.NodeAVL;

public class ArvoreAVL<T extends Comparable<T>> implements BalancedTree<T> {
    private NodeAVL<T> raiz;
    private Estatisticas metricas;

    public ArvoreAVL(Estatisticas metricas) {
        this.metricas = metricas;
    }

    public void insert(T valorElemento) {
        this.raiz = this.insert(this.raiz, valorElemento);
    }

    private NodeAVL<T> insert(NodeAVL<T> noAtual, T valorElemento) {
        if (noAtual == null) {
            ++this.metricas.atribuicoes;
            return new NodeAVL(valorElemento);
        } else {
            ++this.metricas.comparacoes;
            int comparacao = valorElemento.compareTo((T) noAtual.element);
            ++this.metricas.atribuicoes;
            if (comparacao < 0) {
                noAtual.left = this.insert(noAtual.left, valorElemento);
                ++this.metricas.atribuicoes;
            } else {
                if (comparacao <= 0) {
                    ++this.metricas.comparacoes;
                    return noAtual; // Valor duplicado
                }

                ++this.metricas.comparacoes;
                noAtual.right = this.insert(noAtual.right, valorElemento);
                ++this.metricas.atribuicoes;
            }

            this.atualizarAltura(noAtual);
            return this.balancear(noAtual);
        }
    }

    private void atualizarAltura(NodeAVL<T> noAtual) {
        noAtual.height = 1 + Math.max(this.altura(noAtual.left), this.altura(noAtual.right));
        ++this.metricas.atribuicoes;
    }

    private int altura(NodeAVL<T> noAtual) {
        return noAtual == null ? -1 : noAtual.height;
    }

    private int obterBalanceamento(NodeAVL<T> noAtual) {
        return noAtual == null ? 0 : this.altura(noAtual.left) - this.altura(noAtual.right);
    }

    private NodeAVL<T> balancear(NodeAVL<T> noAtual) {
        int balance = this.obterBalanceamento(noAtual);
        ++this.metricas.atribuicoes;
        if (balance > 1) {
            ++this.metricas.comparacoes;
            if (this.obterBalanceamento(noAtual.left) < 0) {
                ++this.metricas.comparacoes;
                noAtual.left = this.rotacionarEsquerda(noAtual.left);
                ++this.metricas.atribuicoes;
            }
            noAtual = this.rotacionarDireita(noAtual);
            ++this.metricas.atribuicoes;
        } else if (balance < -1) {
            ++this.metricas.comparacoes;
            if (this.obterBalanceamento(noAtual.right) > 0) {
                ++this.metricas.comparacoes;
                noAtual.right = this.rotacionarDireita(noAtual.right);
                ++this.metricas.atribuicoes;
            }
            noAtual = this.rotacionarEsquerda(noAtual);
            ++this.metricas.atribuicoes;
        }
        return noAtual;
    }

    private NodeAVL<T> rotacionarDireita(NodeAVL<T> noY) {
        NodeAVL<T> noX = noY.left;
        NodeAVL<T> subarvoreT2 = noX.right;
        this.metricas.atribuicoes += 2L;

        noX.right = noY;
        noY.left = subarvoreT2;
        this.metricas.atribuicoes += 2L;

        this.atualizarAltura(noY);
        this.atualizarAltura(noX);
        return noX;
    }

    private NodeAVL<T> rotacionarEsquerda(NodeAVL<T> noY) {
        NodeAVL<T> noX = noY.right;
        NodeAVL<T> subarvoreT2 = noX.left;
        this.metricas.atribuicoes += 2L;

        noX.left = noY;
        noY.right = subarvoreT2;
        this.metricas.atribuicoes += 2L;

        this.atualizarAltura(noY);
        this.atualizarAltura(noX);
        return noX;
    }

    public boolean remove(T valorElemento) {
        return false;
    }

    public boolean find(T valorElemento) {
        return this.buscarRecursivo(this.raiz, valorElemento);
    }

    private boolean buscarRecursivo(NodeAVL<T> noAtual, T valorElemento) {
        if (noAtual == null) {
            return false;
        } else {
            int comparacao = valorElemento.compareTo((T) noAtual.element);
            if (comparacao < 0) {
                return this.buscarRecursivo(noAtual.left, valorElemento);
            } else {
                return comparacao > 0 ? this.buscarRecursivo(noAtual.right, valorElemento) : true;
            }
        }
    }

    public int getHeight() {
        return this.altura(this.raiz);
    }

    public void printInOrder() {
        this.imprimirEmOrdemRecursivo(this.raiz);
        System.out.println();
    }

    private void imprimirEmOrdemRecursivo(NodeAVL<T> noAtual) {
        if (noAtual != null) {
            this.imprimirEmOrdemRecursivo(noAtual.left);
            System.out.print(String.valueOf(noAtual.element) + " ");
            this.imprimirEmOrdemRecursivo(noAtual.right);
        }
    }

    public void collectInOrder(List<T> listaColetada) {
        this.coletarEmOrdemRecursivo(this.raiz, listaColetada);
    }

    private void coletarEmOrdemRecursivo(NodeAVL<T> noAtual, List<T> listaColetada) {
        if (noAtual != null) {
            this.coletarEmOrdemRecursivo(noAtual.left, listaColetada);
            listaColetada.add((T) noAtual.element);
            this.coletarEmOrdemRecursivo(noAtual.right, listaColetada);
        }
    }

    public NodeAVL<T> getRoot() {
        return this.raiz;
    }
}