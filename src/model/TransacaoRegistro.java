package model;

public class TransacaoRegistro implements Comparable<TransacaoRegistro> {
    String identificador;
    String localOrigem;
    String localDestino;
    float quantia;
    String dataHora;

    public TransacaoRegistro(String identificador, String localOrigem, String localDestino, float quantia, String dataHora) {
        this.identificador = identificador;
        this.localOrigem = localOrigem;
        this.localDestino = localDestino;
        this.quantia = quantia;
        this.dataHora = dataHora;
    }

    public String getIdentificador() {
        return this.identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getOrigem() {
        return this.localOrigem;
    }

    public void setNome(String localOrigem) { // Mantido setNome
        this.localOrigem = localOrigem;
    }

    public String getDestino() {
        return this.localDestino;
    }

    public void setDestino(String localDestino) {
        this.localDestino = localDestino;
    }

    public float getValor() {
        return this.quantia;
    }

    public void setValor(float quantia) {
        this.quantia = quantia;
    }

    public String getTimestamp() {
        return this.dataHora;
    }

    public void setTimestamp(String dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public int compareTo(TransacaoRegistro outroRegistro) {
        return this.identificador.compareTo(outroRegistro.identificador);
    }

    @Override
    public String toString() {
        return this.identificador + "," + this.localOrigem + "," + this.localDestino + "," + this.quantia + "," + this.dataHora;
    }
}