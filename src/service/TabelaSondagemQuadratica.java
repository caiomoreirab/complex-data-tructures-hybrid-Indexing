package service;

public class TabelaSondagemQuadratica<Key, Value> {
    private int quantidadeElementos;
    private int capacidadeTabela = 512;
    private Key[] chaves;
    private Value[] valores;

    public TabelaSondagemQuadratica() {
        this.chaves = (Key[])(new Object[this.capacidadeTabela]);
        this.valores = (Value[])(new Object[this.capacidadeTabela]);
    }

    public TabelaSondagemQuadratica(int novaCapacidade) {
        this.capacidadeTabela = novaCapacidade;
        this.chaves = (Key[])(new Object[this.capacidadeTabela]);
        this.valores = (Value[])(new Object[this.capacidadeTabela]);
    }

    private int calcularHash(Key chaveBusca) {
        return (chaveBusca.hashCode() & Integer.MAX_VALUE) % this.capacidadeTabela;
    }

    private void redimensionar(int novaCapacidade) {
        TabelaSondagemQuadratica<Key, Value> novaTabela = new TabelaSondagemQuadratica<Key, Value>(novaCapacidade);

        for(int indiceAtual = 0; indiceAtual < this.capacidadeTabela; ++indiceAtual) {
            if (this.chaves[indiceAtual] != null) {
                novaTabela.put(this.chaves[indiceAtual], this.valores[indiceAtual]);
            }
        }
        this.chaves = novaTabela.chaves;
        this.valores = novaTabela.valores;
        this.capacidadeTabela = novaTabela.capacidadeTabela;
    }

    public void put(Key chaveBusca, Value valorArmazenar) {
        if (this.quantidadeElementos >= this.capacidadeTabela / 2) {
            this.redimensionar(2 * this.capacidadeTabela);
        }

        int indiceAtual = this.calcularHash(chaveBusca);

        for(int tentativa = 1; this.chaves[indiceAtual] != null; ++tentativa) {
            if (this.chaves[indiceAtual].equals(chaveBusca)) {
                this.valores[indiceAtual] = valorArmazenar;
                return;
            }
            indiceAtual = (indiceAtual + tentativa * tentativa) % this.capacidadeTabela;
        }

        this.chaves[indiceAtual] = chaveBusca;
        this.valores[indiceAtual] = valorArmazenar;
        ++this.quantidadeElementos;
    }

    public Value get(Key chaveBusca) {
        int indiceAtual = this.calcularHash(chaveBusca);

        for(int tentativa = 1; this.chaves[indiceAtual] != null; ++tentativa) {
            if (this.chaves[indiceAtual].equals(chaveBusca)) {
                return (Value)this.valores[indiceAtual];
            }
            indiceAtual = (indiceAtual + tentativa * tentativa) % this.capacidadeTabela;
        }

        return null;
    }

    public boolean contains(Key chaveBusca) {
        return this.get(chaveBusca) != null;
    }

    public void delete(Key chaveBusca) {
        if (this.contains(chaveBusca)) {
            int indiceAtual = this.calcularHash(chaveBusca);

            int tentativa;
            for(tentativa = 1; !chaveBusca.equals(this.chaves[indiceAtual]); ++tentativa) {
                indiceAtual = (indiceAtual + tentativa * tentativa) % this.capacidadeTabela;
            }

            this.chaves[indiceAtual] = null;
            this.valores[indiceAtual] = null;
            --this.quantidadeElementos;
            indiceAtual = (indiceAtual + tentativa * tentativa) % this.capacidadeTabela;

            for(int passoRehash = 1; this.chaves[indiceAtual] != null; ++passoRehash) {
                Key chaveParaRehash = (Key)this.chaves[indiceAtual];
                Value valorParaRehash = (Value)this.valores[indiceAtual];
                this.chaves[indiceAtual] = null;
                this.valores[indiceAtual] = null;
                --this.quantidadeElementos;
                this.put(chaveParaRehash, valorParaRehash);
                indiceAtual = (indiceAtual + passoRehash * passoRehash) % this.capacidadeTabela;
            }

            if (this.quantidadeElementos > 0 && this.quantidadeElementos <= this.capacidadeTabela / 8) {
                this.redimensionar(this.capacidadeTabela / 2);
            }
        }
    }
}