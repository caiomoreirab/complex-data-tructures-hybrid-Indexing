# ComplexDataStructures-HybridIndexing

## 📚 Visão Geral do Projeto

Este projeto, desenvolvido como parte da disciplina de Estrutura de Dados II (2025.1) da Universidade Federal do Maranhão (UFMA), apresenta uma implementação de 
uma **Tabela Hash Híbrida** para o gerenciamento eficiente de transações financeiras. O principal desafio e inovação deste sistema reside na sua 
capacidade de indexar dados simultaneamente por diferentes critérios (`id` e `origem`), aplicando estratégias de resolução de colisões e migração
de estruturas de dados de forma dinâmica e adaptativa.

## ✨ Funcionalidades Principais

* **Tabela Hash Híbrida Única:** Gerencia a indexação de transações por dois campos distintos (`id` e `origem`) dentro de uma mesma estrutura central.
* **Indexação por `ID` (Encadeamento):**
    * Utiliza uma lista encadeada (LinkedList) para resolver colisões de IDs, permitindo o armazenamento de múltiplos registros com o mesmo identificador na mesma posição do hash.
* **Indexação por `Origem` (Sondagem Quadrática com Migração Dinâmica):**
    * Em seu estado inicial, emprega endereçamento aberto com sondagem quadrática para resolver colisões.
    * **Auto-Adaptação (Migração para AVL):** Se o número de colisões para uma `origem` específica exceder 3, todos os registros dessa `origem` são automaticamente migrados para uma `Árvore AVL` para otimização de busca e inserção.
    * **Auto-Adaptação (Migração para Rubro-Negra):** Caso uma `Árvore AVL` associada a uma `origem` ultrapasse uma altura de 10, ela é dinamicamente transformada em uma `Árvore Rubro-Negra`, garantindo um balanceamento mais rigoroso para volumes de dados ainda maiores.
* **Busca Avançada:** Suporta a busca de transações por `origem` e por `intervalo` de `timestamp`.
* **Coleta de Estatísticas:** Instrumenta o código para medir e reportar métricas de desempenho essenciais, como número de comparações, atribuições, tempo de execução e contagem de migrações entre estruturas.

## 🚀 Como Executar o Projeto

### Pré-requisitos

* JDK (Java Development Kit) 24 ou superior.
* Um ambiente de desenvolvimento integrado (IDE) como IntelliJ IDEA, Eclipse ou VS Code com extensão Java (Recomendado).

### Passos para Configuração e Execução

1.  **Clone o Repositório:**
    ```bash
    git clone [https://github.com/](https://github.com/)[SeuUsuario]/ComplexDataStructures-HybridIndexing.git
    cd ComplexDataStructures-HybridIndexing
    ```
2.  **Estrutura do Projeto:**
    Certifique-se de que a estrutura de pastas e arquivos está conforme o padrão Java:
    ```
    .
    ├── src
    │   └── main
    │       └── java
    │           ├── app
    │           │   └── PrincipalSimulacao.java
    │           ├── model
    │           │   └── TransacaoRegistro.java
    │           ├── service
    │           │   ├── ArvoreAVL.java
    │           │   ├── ArvoreRubroNegra.java
    │           │   ├── BalancedTree.java
    │           │   ├── GerenciadorHash.java
    │           │   ├── NodeAVL.java
    │           │   ├── NodeRB.java
    │           │   └── TabelaSondagemQuadratica.java
    │           └── util
    │               ├── DatasetGenerator.java
    │               └── Estatisticas.java
    └── # Outras pastas de build (.idea, out, etc. devem ser ignoradas ou removidas para o ZIP de entrega)
    ```
3.  **Gerar o Arquivo de Dados (`transacoes.csv`):**
    * A classe `DatasetGenerator.java` é responsável por criar o arquivo `transacoes.csv`.
    * **No IDE (Recomendado):**
        * Abra o projeto no seu IDE.
        * Navegue até `src/main/java/util/DatasetGenerator.java`.
        * Clique com o botão direito no arquivo e selecione `Run 'DatasetGenerator.main()'`.
        * **Para gerar tamanhos diferentes:** Vá em `Run` -> `Edit Configurations...`, selecione a configuração do `DatasetGenerator` e em `Program arguments`, adicione o número de registros desejado (ex: `1000` para pequeno, `10000` para médio, `100000` para grande).
        * O arquivo `transacoes.csv` será gerado na raiz do seu projeto (no mesmo nível da pasta `src`).
        * **Mova `transacoes.csv`:** Após a geração, arraste ou copie o arquivo `transacoes.csv` da raiz do projeto para a pasta `src/main/java/util/`.
    * **Via Terminal:**
        ```bash
        # Compile a classe geradora
        javac src/main/java/util/DatasetGenerator.java

        # Gere o dataset (ex: 10000 registros, nome padrao transacoes.csv)
        java -cp src/main/java util.DatasetGenerator 10000
        # Mova o transacoes.csv gerado para src/main/java/util/ se ele estiver na raiz
        ```

4.  **Executar o Sistema de Indexação:**
    * A classe `PrincipalSimulacao.java` é o ponto de entrada da aplicação.
    * **No IDE (Recomendado):**
        * Navegue até `src/main/java/app/PrincipalSimulacao.java`.
        * Clique com o botão direito no arquivo e selecione `Run 'PrincipalSimulacao.main()'`.
    * **Via Terminal:**
        ```bash
        # Compile todas as classes (se já não estiverem compiladas pelo IDE)
        javac src/main/java/app/*.java src/main/java/model/*.java src/main/java/service/*.java src/main/java/util/*.java

        # Execute o programa principal
        java -cp src/main/java app.PrincipalSimulacao
        ```
    * A saída do console mostrará as mensagens de migração, os resultados das buscas de exemplo e as estatísticas de desempenho.

## 🛠 Estrutura do Projeto

O projeto segue uma arquitetura modular, organizada nos seguintes pacotes:

* **`app`**: Contém a classe principal de execução da aplicação.
* **`model`**: Define as estruturas de dados para os registros de transações.
* **`service`**: Abriga as implementações das estruturas de dados (Hash, AVL, Rubro-Negra) e a lógica principal de gestão e migração.
* **`util`**: Inclui classes utilitárias para geração de dados de teste e coleta de estatísticas de desempenho.

## 🤝 Contribuição

Este projeto foi desenvolvido individualmente por **Caio Bandeira Moreira** como parte da disciplina de Estrutura de Dados II.
