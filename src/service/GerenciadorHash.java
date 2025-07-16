package service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import model.TransacaoRegistro;
import util.Estatisticas;


public class GerenciadorHash {
    private static final int CAPACIDADE_HASH_ID = 512;
    private List<LinkedList<TransacaoRegistro>> tabelaEncadeadaID = new ArrayList(CAPACIDADE_HASH_ID);
    private TabelaSondagemQuadratica<String, Object> tabelaSondagemOrigem;
    public Estatisticas metricas = new Estatisticas();

    public GerenciadorHash() {
        for(int i = 0; i < CAPACIDADE_HASH_ID; ++i) {
            this.tabelaEncadeadaID.add(new LinkedList());
            ++this.metricas.atribuicoes;
        }
        this.tabelaSondagemOrigem = new TabelaSondagemQuadratica();
    }

    public void inserirPorIdentificador(TransacaoRegistro registro) {
        long tempoInicial = System.nanoTime();
        int posicaoHash = this.calcularHash(registro.getIdentificador(), CAPACIDADE_HASH_ID);
        ((LinkedList)this.tabelaEncadeadaID.get(posicaoHash)).add(registro);
        this.metricas.tempoNano += System.nanoTime() - tempoInicial;
    }

    public List<TransacaoRegistro> buscarPorId(String id) {
        long tempoInicial = System.nanoTime();
        int posicaoHash = this.calcularHash(id, CAPACIDADE_HASH_ID);
        List<TransacaoRegistro> listaRegistros = (List)this.tabelaEncadeadaID.get(posicaoHash);
        List<TransacaoRegistro> listaResultados = new ArrayList();

        for(TransacaoRegistro itemAtual : listaRegistros) {
            ++this.metricas.comparacoes;
            if (itemAtual.getIdentificador().equals(id)) {
                listaResultados.add(itemAtual);
                ++this.metricas.atribuicoes;
            }
        }

        this.metricas.tempoNano += System.nanoTime() - tempoInicial;
        return listaResultados;
    }

    public void inserirPorOrigem(TransacaoRegistro registro) {
        long tempoInicial = System.nanoTime();
        Object entradaEncontrada = this.tabelaSondagemOrigem.get(registro.getOrigem());
        ++this.metricas.comparacoes;
        if (entradaEncontrada == null) {
            List<TransacaoRegistro> lista = new ArrayList();
            lista.add(registro);
            ++this.metricas.atribuicoes;
            this.tabelaSondagemOrigem.put(registro.getOrigem(), lista);

        } else if (entradaEncontrada instanceof List) {
            List<TransacaoRegistro> lista = (List)entradaEncontrada;
            lista.add(registro);
            ++this.metricas.atribuicoes;
            ++this.metricas.comparacoes;
            if (lista.size() > 3) {
                System.out.println("Migrando para AVL: " + registro.getOrigem());
                ++this.metricas.migracoesParaAVL;
                ArvoreAVL<TransacaoRegistro> arvoreAVL = new ArvoreAVL(this.metricas);

                for(TransacaoRegistro itemAtual : lista) {
                    arvoreAVL.insert(itemAtual);
                    ++this.metricas.atribuicoes;
                }
                this.tabelaSondagemOrigem.put(registro.getOrigem(), arvoreAVL);
            } else {
                this.tabelaSondagemOrigem.put(registro.getOrigem(), lista);
            }
        } else if (entradaEncontrada instanceof ArvoreAVL) {
            ArvoreAVL<TransacaoRegistro> arvoreAVL = (ArvoreAVL)entradaEncontrada;
            arvoreAVL.insert(registro);
            ++this.metricas.atribuicoes;
            ++this.metricas.comparacoes;
            if (arvoreAVL.getHeight() > 10) {
                System.out.println("Migrando para RB: " + registro.getOrigem());
                ++this.metricas.migracoesParaRB;
                ArvoreRubroNegra<TransacaoRegistro> arvoreRB = new ArvoreRubroNegra(this.metricas);
                List<TransacaoRegistro> registrosColetados = new ArrayList();
                arvoreAVL.collectInOrder(registrosColetados);

                for(TransacaoRegistro itemAtual : registrosColetados) {
                    arvoreRB.insert(itemAtual);
                    ++this.metricas.atribuicoes;
                }
                this.tabelaSondagemOrigem.put(registro.getOrigem(), arvoreRB);
            }
        } else if (entradaEncontrada instanceof ArvoreRubroNegra) {
            ArvoreRubroNegra<TransacaoRegistro> arvoreRB = (ArvoreRubroNegra)entradaEncontrada;
            arvoreRB.insert(registro);
            ++this.metricas.atribuicoes;
            ++this.metricas.comparacoes;
        }

        this.metricas.tempoNano += System.nanoTime() - tempoInicial;
    }

    public List<TransacaoRegistro> buscarPorOrigem(String origem) {
        long tempoInicial = System.nanoTime();
        Object entradaEncontrada = this.tabelaSondagemOrigem.get(origem);
        List<TransacaoRegistro> listaResultados = new ArrayList();
        ++this.metricas.comparacoes;

        if (entradaEncontrada == null) {
            return listaResultados;
        } else {
            if (entradaEncontrada instanceof List) {
                listaResultados.addAll((List)entradaEncontrada);
                ++this.metricas.atribuicoes;
            } else if (entradaEncontrada instanceof ArvoreAVL) {
                ((ArvoreAVL)entradaEncontrada).collectInOrder(listaResultados);
            } else if (entradaEncontrada instanceof ArvoreRubroNegra) {
                ((ArvoreRubroNegra)entradaEncontrada).collectInOrder(listaResultados);
            }

            this.metricas.tempoNano += System.nanoTime() - tempoInicial;
            return listaResultados;
        }
    }

    public List<TransacaoRegistro> buscarPorOrigemEIntervalo(String origem, String inicioPeriodo, String fimPeriodo) {
        long tempoInicial = System.nanoTime();
        Object entradaEncontrada = this.tabelaSondagemOrigem.get(origem);
        List<TransacaoRegistro> listaResultados = new ArrayList();
        ++this.metricas.comparacoes;


        if (entradaEncontrada == null) {
            return listaResultados;
        } else {
            List<TransacaoRegistro> registrosColetados = new ArrayList();
            ++this.metricas.comparacoes;
            if (entradaEncontrada instanceof List) {
                registrosColetados.addAll((List)entradaEncontrada);
            } else if (entradaEncontrada instanceof ArvoreAVL) {
                ((ArvoreAVL)entradaEncontrada).collectInOrder(registrosColetados);
            } else if (entradaEncontrada instanceof ArvoreRubroNegra) {
                ((ArvoreRubroNegra)entradaEncontrada).collectInOrder(registrosColetados);
            }

            for(TransacaoRegistro itemAtual : registrosColetados) {
                String timestampAtual = itemAtual.getTimestamp();
                ++this.metricas.comparacoes;
                if (timestampAtual.compareTo(inicioPeriodo) >= 0 && timestampAtual.compareTo(fimPeriodo) <= 0) {
                    listaResultados.add(itemAtual);
                    ++this.metricas.atribuicoes;
                }
            }

            this.metricas.tempoNano += System.nanoTime() - tempoInicial;
            return listaResultados;
        }
    }

    private int calcularHash(String chaveEntrada, int capacidadeHash) {
        return Math.abs(chaveEntrada.hashCode()) % capacidadeHash;
    }
}