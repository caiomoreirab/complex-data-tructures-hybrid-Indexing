package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import model.TransacaoRegistro;
import service.GerenciadorHash;

public class MainSimulacao {
    public static void main(String[] args) {
        // Cria uma instância do gerenciador de hash para processar as transações
        GerenciadorHash sistemaGerenciador = new GerenciadorHash();

        try (BufferedReader leitorArquivo = new BufferedReader(new FileReader("transacoes.csv"))) {
            leitorArquivo.readLine(); // Pula a linha do cabeçalho
            String linhaAtual;

            // Processa cada registro de transação do arquivo CSV
            while((linhaAtual = leitorArquivo.readLine()) != null) {
                String[] elementosRegistro = linhaAtual.split(";");
                String idTransacao = elementosRegistro[0];
                String fonteTransacao = elementosRegistro[1];
                String destinoTransacao = elementosRegistro[2];
                float valorNumerico = Float.parseFloat(elementosRegistro[3].replace(",", "."));
                String momentoTransacao = elementosRegistro[4];

                // Constrói o objeto de registro e o insere no sistema
                TransacaoRegistro novoRegistro = new TransacaoRegistro(idTransacao, fonteTransacao, destinoTransacao, valorNumerico, momentoTransacao);
                sistemaGerenciador.inserirPorIdentificador(novoRegistro);
                sistemaGerenciador.inserirPorOrigem(novoRegistro);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados de transação: " + e.getMessage());
            e.printStackTrace();
        }

        // Demonstração de busca por origem específica
        String fonteAlvo = "ORIG003";
        List<TransacaoRegistro> transacoesEncontradas = sistemaGerenciador.buscarPorOrigem(fonteAlvo);
        System.out.println("\n--- Transações encontradas para a origem: " + fonteAlvo + " ---");
        System.out.println("Total de registros correspondentes: " + transacoesEncontradas.size());

        for(TransacaoRegistro registroProcessado : transacoesEncontradas) {
            System.out.println(registroProcessado);
        }

        // Demonstração de busca por origem dentro de um período de tempo
        String dataInicioPeriodo = "2023-01-01";
        String dataFimPeriodo = "2023-12-31";
        List<TransacaoRegistro> transacoesNoPeriodo = sistemaGerenciador.buscarPorOrigemEIntervalo(fonteAlvo, dataInicioPeriodo, dataFimPeriodo);
        System.out.println("\n--- Transações de " + fonteAlvo + " no intervalo de " + dataInicioPeriodo + " a " + dataFimPeriodo + " ---");
        System.out.println("Total de registros no período: " + transacoesNoPeriodo.size());

        for(TransacaoRegistro registroProcessado : transacoesNoPeriodo) {
            System.out.println(registroProcessado);
        }
        System.out.println("\n--- Estatísticas de Execução ---");
        System.out.println("Comparações: " + sistemaGerenciador.metricas.comparacoes);
        System.out.println("Atribuições: " + sistemaGerenciador.metricas.atribuicoes);
        System.out.println("Tempo Total (ns): " + sistemaGerenciador.metricas.tempoNano);
        System.out.println("Migrações para AVL: " + sistemaGerenciador.metricas.migracoesParaAVL);
        System.out.println("Migrações para RB: " + sistemaGerenciador.metricas.migracoesParaRB);
    }
}