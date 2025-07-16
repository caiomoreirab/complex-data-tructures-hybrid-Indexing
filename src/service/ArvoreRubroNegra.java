package service;

import java.util.List;
import service.NodeRB.Cor;
import util.Estatisticas;

public class ArvoreRubroNegra<T extends Comparable<T>> implements BalancedTree<T> {
    private NodeRB<T> raiz;
    private Estatisticas metricas;

    public ArvoreRubroNegra(Estatisticas metricas) {
        this.metricas = metricas;
    }

    public void insert(T valorElemento) {
        this.raiz = this.insert(this.raiz, valorElemento);
        this.raiz.color = Cor.PRETO;
        ++this.metricas.atribuicoes;
    }

    private NodeRB<T> insert(NodeRB<T> noAtual, T valorElemento) {
        if (noAtual == null) {
            ++this.metricas.atribuicoes;
            return new NodeRB(valorElemento);
        } else {
            int comparacao = valorElemento.compareTo((T) noAtual.element);
            ++this.metricas.atribuicoes;
            ++this.metricas.comparacoes;

            if (comparacao < 0) {
                noAtual.left = this.insert(noAtual.left, valorElemento);
                ++this.metricas.atribuicoes;
                noAtual.left.parent = noAtual;
                ++this.metricas.atribuicoes;
            } else {
                if (comparacao <= 0) {
                    ++this.metricas.comparacoes;
                    return noAtual;
                }

                ++this.metricas.comparacoes;
                noAtual.right = this.insert(noAtual.right, valorElemento);
                ++this.metricas.atribuicoes;
                noAtual.right.parent = noAtual;
                ++this.metricas.atribuicoes;
            }

            if (this.ehVermelho(noAtual.right) && !this.ehVermelho(noAtual.left)) {
                this.metricas.comparacoes += 2L;
                noAtual = this.rotacionarEsquerda(noAtual);
                ++this.metricas.atribuicoes;
            }

            if (this.ehVermelho(noAtual.left) && this.ehVermelho(noAtual.left.left)) {
                this.metricas.comparacoes += 2L;
                noAtual = this.rotacionarDireita(noAtual);
                ++this.metricas.atribuicoes;
            }

            if (this.ehVermelho(noAtual.left) && this.ehVermelho(noAtual.right)) {
                this.metricas.comparacoes += 2L;
                this.inverterCores(noAtual);
                ++this.metricas.atribuicoes;
            }

            return noAtual;
        }
    }

    private boolean ehVermelho(NodeRB<T> noAtual) {
        if (noAtual == null) {
            return false;
        } else {
            return noAtual.color == Cor.VERMELHO;
        }
    }

    private NodeRB<T> rotacionarEsquerda(NodeRB<T> noAtual) {
        NodeRB<T> noRotacao = noAtual.right;
        noAtual.right = noRotacao.left;
        ++this.metricas.atribuicoes;
        if (noRotacao.left != null) {
            noRotacao.left.parent = noAtual;
            ++this.metricas.atribuicoes;
        }

        noRotacao.left = noAtual;
        ++this.metricas.atribuicoes;
        noRotacao.color = noAtual.color;
        noAtual.color = Cor.VERMELHO;
        this.metricas.atribuicoes += 2L;
        return noRotacao;
    }

    private NodeRB<T> rotacionarDireita(NodeRB<T> noAtual) {
        NodeRB<T> noRotacao = noAtual.left;
        noAtual.left = noRotacao.right;
        ++this.metricas.atribuicoes;
        if (noRotacao.right != null) {
            noRotacao.right.parent = noAtual;
            ++this.metricas.atribuicoes;
        }

        noRotacao.right = noAtual;
        ++this.metricas.atribuicoes;
        noRotacao.color = noAtual.color;
        noAtual.color = Cor.VERMELHO;
        this.metricas.atribuicoes += 2L;
        return noRotacao;
    }

    private void inverterCores(NodeRB<T> noAtual) {
        noAtual.color = Cor.VERMELHO;
        if (noAtual.left != null) {
            noAtual.left.color = Cor.PRETO;
        }

        if (noAtual.right != null) {
            noAtual.right.color = Cor.PRETO;
        }
        this.metricas.atribuicoes += (long)(1 + (noAtual.left != null ? 1 : 0) + (noAtual.right != null ? 1 : 0));
    }

    public boolean remove(T valorElemento) {
        return false;
    }

    public boolean find(T valorElemento) {
        return this.buscarRecursivo(this.raiz, valorElemento);
    }

    private boolean buscarRecursivo(NodeRB<T> noAnalise, T valorElemento) {
        if (noAnalise == null) {
            return false;
        } else {
            int comparacao = valorElemento.compareTo((T) noAnalise.element);
            if (comparacao < 0) {
                return this.buscarRecursivo(noAnalise.left, valorElemento);
            } else {
                return comparacao > 0 ? this.buscarRecursivo(noAnalise.right, valorElemento) : true;
            }
        }
    }

    public int getHeight() {
        return this.obterAlturaRecursivo(this.raiz);
    }

    private int obterAlturaRecursivo(NodeRB<T> noAnalise) {
        return noAnalise == null ? -1 : 1 + Math.max(this.obterAlturaRecursivo(noAnalise.left), this.obterAlturaRecursivo(noAnalise.right));
    }

    public void printInOrder() {
        this.imprimirEmOrdemRecursivo(this.raiz);
        System.out.println();
    }

    private void imprimirEmOrdemRecursivo(NodeRB<T> noAnalise) {
        if (noAnalise != null) {
            this.imprimirEmOrdemRecursivo(noAnalise.left);
            System.out.print(String.valueOf(noAnalise.element) + " ");
            this.imprimirEmOrdemRecursivo(noAnalise.right);
        }
    }

    public void collectInOrder(List<T> listaColetada) {
        this.coletarEmOrdemRecursivo(this.raiz, listaColetada);
    }

    private void coletarEmOrdemRecursivo(NodeRB<T> noAnalise, List<T> listaColetada) {
        if (noAnalise != null) {
            this.coletarEmOrdemRecursivo(noAnalise.left, listaColetada);
            listaColetada.add((T) noAnalise.element);
            this.coletarEmOrdemRecursivo(noAnalise.right, listaColetada);
        }
    }

    public NodeRB<T> getRoot() {
        return this.raiz;
    }
}