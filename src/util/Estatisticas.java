package util;

public class Estatisticas {
    public long comparacoes = 0;
    public long atribuicoes = 0;
    public long tempoNano = 0;
    public long migracoesParaAVL = 0;
    public long migracoesParaRB = 0;

    public void reset() {
        this.comparacoes = 0;
        this.atribuicoes = 0;
        this.tempoNano = 0;
        this.migracoesParaAVL = 0;
        this.migracoesParaRB = 0;
    }
}