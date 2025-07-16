package service;

public class NodeRB<AnyType> {
    AnyType element;
    NodeRB<AnyType> parent;
    NodeRB<AnyType> left;
    NodeRB<AnyType> right;
    Cor color;

    NodeRB(AnyType e) {
        this(e, (NodeRB)null, (NodeRB)null, (NodeRB)null, false);
    }

    NodeRB(AnyType e, NodeRB<AnyType> l, NodeRB<AnyType> r, NodeRB<AnyType> p, boolean isBlack) {
        this.element = e;
        this.left = l;
        this.right = r;
        this.parent = p;
        this.color = isBlack ? Cor.PRETO : Cor.VERMELHO;
    }

    public enum Cor {
        VERMELHO,
        PRETO;
    }
}