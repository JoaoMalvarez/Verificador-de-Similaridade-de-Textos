# Verificador de Similaridade de Textos

**Universidade Presbiteriana Mackenzie** — Projeto e Análise de Algoritmos II

> **Integrante:**
>
> - João Pedro Mazzante Alvarez — 10723837

---

## 1. Objetivo

Sistema em Java que identifica o grau de similaridade entre documentos textuais. Esse tipo de tarefa é usado em detecção de plágio acadêmico, recomendação de notícias, organização de grandes volumes de documentos e otimização de motores de busca.

O projeto integra duas estruturas de dados avançadas, ambas implementadas manualmente:

- **Tabela hash**: armazena as palavras de cada documento e suas frequências.
- **Árvore AVL**: mantém os pares de documentos ordenados pelo grau de similaridade.

### Objetivos específicos

- Implementar uma tabela hash para representar os documentos processados.
- Implementar uma árvore AVL para manter ordenados os pares de documentos por similaridade.
- Aplicar e comparar diferentes estratégias de dispersão e balanceamento.
- Analisar experimentalmente o desempenho das estruturas.

---

## 2. Funcionamento geral

1. Leitura de todos os arquivos `.txt` do diretório informado.
2. **Normalização** de cada texto:
   - conversão para minúsculas;
   - remoção de pontuação e caracteres não alfanuméricos;
   - tokenização;
   - remoção de *stop words* (lista pré-definida para o português).
3. Armazenamento das palavras e frequências em uma **tabela hash** por documento.
4. Cálculo da **similaridade** entre todos os pares de documentos.
5. Inserção dos resultados na **árvore AVL**, usando a similaridade como chave.
6. Exibição dos resultados no terminal e gravação em `resultado.txt`.

### Exemplo ilustrativo (não vinculante)

```
doc1.txt: "estrutura de dados é essencial em ciência da computação"
doc2.txt: "dados estruturados são essenciais para a ciência"

doc1: {"estrutura": 1, "dados": 1, "essencial": 1, "ciência": 1, "computação": 1}
doc2: {"dados": 1, "estruturados": 1, "essenciais": 1, "ciência": 1}
```

---

## 3. Requisitos técnicos

### Tabela hash

- Implementação manual, sem `HashMap` ou bibliotecas prontas.
- **Duas funções de dispersão distintas**, definidas e justificadas pelo grupo, para comparação no relatório.
- Estratégia de tratamento de colisões documentada.
- Métodos essenciais: `put(chave, valor)` e `get(chave)`.

### Árvore AVL

- Chave do nó: a **similaridade** (`double`).
- Cada nó guarda uma **lista de pares de documentos** (`Resultado`), pois vários pares podem ter a mesma similaridade.
- Inserção com cálculo do fator de balanceamento e rotações simples e duplas.
- A inserção deve **retornar um registro com a contagem de rotações** (simples esquerda/direita e dupla esquerda/direita).
- Métodos de travessia para listar os pares em ordem.

```
        0.78
       /    \
    0.45    0.90
    /
 0.30
```

### Similaridade

- Métrica escolhida e justificada pelo grupo (Cosseno, Jaccard, Dice etc.).

---

## 4. Arquitetura recomendada

| Classe | Responsabilidade |
|---|---|
| `Main` | Ponto de entrada. Valida argumentos, lê arquivos, cria os `Documento`, compara todos os pares, insere na AVL e gera a saída (terminal e `resultado.txt`). |
| `Documento` | Representa um arquivo e seu conteúdo processado: nome, tabela hash interna (palavra → frequência), leitura e normalização. |
| `HashTable` | Tabela hash genérica manual, com array interno e as duas funções de dispersão. |
| `AVLTree` | Árvore AVL manual com inserção balanceada, contagem de rotações e travessias. |
| `ComparadorDeDocumentos` | Isola o cálculo de similaridade entre dois `Documento`, retornando um `double`. |
| `Resultado` | Estrutura simples com os nomes dos dois documentos e a similaridade. |

---

## 5. Como compilar e executar

Requer **JDK 17 ou superior**.

```bash
javac *.java && java Main <diretorio_documentos> <limiar> <modo> [argumentos_opcionais]
```

| Parâmetro | Descrição |
|---|---|
| `<diretorio_documentos>` | Pasta com os arquivos de texto (ex.: `./documentos/`). |
| `<limiar>` | Valor entre 0 e 1 com a similaridade mínima a ser exibida. |
| `<modo>` | `lista`, `topK` ou `busca`. |

### Modos

- **`lista`**: exibe todos os pares com similaridade acima do limiar.
- **`topK`**: exibe os K pares mais semelhantes (K informado após o modo).
- **`busca`**: compara dois arquivos específicos. O limiar é ignorado (pode ser `0.0`).

### Exemplos

```bash
java Main /documentos 0.75 lista
java Main /documentos 0.8 topk 5
java Main /documentos 0.0 busca doc1.txt doc4.txt
```

Comando usado na correção:

```bash
javac *.java && java Main /documentos 0.7 lista
```

---

## 6. Formato de saída

A saída vai para o terminal **e** para o arquivo `resultado.txt` (nome fixo, no diretório de execução). O programa não pode exigir interação manual.

**Modo lista / topK:**

```
=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===
Total de documentos processados: 5
Total de pares comparados: 10
Função hash utilizada: hashMultiplicativo
Métrica de similaridade: Cosseno

Pares com similaridade >= 0.75:
doc1.txt <-> doc2.txt = 0.82
doc3.txt <-> doc4.txt = 0.79

Pares com menor similaridade:
doc1.txt <-> doc5.txt = 0.12
```

**Modo busca:**

```
=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===
Comparando: doc1.txt <-> doc4.txt
Similaridade calculada: 0.67
Métrica utilizada: Cosseno
```

---

## 7. Relatório técnico (PDF)

1. **Introdução**: contextualização, relevância da detecção de similaridade e objetivos.
2. **Metodologia**:
   - tabela hash, as duas funções de dispersão e o tratamento de colisões;
   - árvore AVL, balanceamento, rotações e a implementação da contagem de rotações;
   - justificativa da métrica de similaridade.
3. **Descrição do algoritmo**: fluxo de dados e pseudocódigo dos algoritmos mais complexos (ex.: inserção na AVL).
4. **Análise experimental**:
   - gráficos e tabelas com tempo médio de execução para diferentes volumes de documentos;
   - comparação da busca na AVL com uma estrutura mais simples (ex.: lista ordenada);
   - distribuição de chaves (colisões por bucket) para as duas funções hash, com discussão de qual foi mais eficaz;
   - número de rotações (simples e duplas) na AVL e o que indicam sobre a ordem de inserção.
5. **Resultados e discussão**:
   - pares de documentos com maior similaridade;
   - impacto de substituir a AVL por outra estrutura, como uma heap (complexidade e adequação);
   - como o pré-processamento ajudou a evitar falsos positivos e que outros passos poderiam ser usados.
6. **Conclusões**:
   - principais desafios técnicos e conceituais (função hash e balanceamento da AVL);
   - descrição detalhada do bug mais desafiador: sintoma, investigação e solução.

O relatório deve ter referências, prints e respostas analíticas.

---

## 8. Estrutura do repositório / entrega

Entrega em **um único `.zip`** pela plataforma da disciplina (enviado por apenas um integrante), contendo:

```
.
├── Main.java
├── Documento.java
├── HashTable.java
├── AVLTree.java
├── ComparadorDeDocumentos.java
├── Resultado.java
├── documentos/          # textos usados nos testes
├── relatorio.pdf
└── README.md
```

---

## 9. Normas e avaliação

- Nome completo e RA de todos os integrantes em um bloco de comentários no cabeçalho de `Main.java`.
- Código claro e comentado, especialmente a função de dispersão, o tratamento de colisões e as rotações da AVL.
- **Apresentação obrigatória**: todos os integrantes explicam qualquer parte do código, justificam decisões e fazem pequenas alterações pedidas pelo professor. A ausência de um integrante implica nota zero para ele.
- Não são aceitos projetos atrasados.

**Critérios:** implementação correta e funcional, clareza e coerência do relatório, originalidade das análises, testes de desempenho e profundidade das discussões, evidências de autoria.

**Penalizações:**

- Projeto copiado ou gerado automaticamente sem explicação: nota 0.
- Programa que não compila ou não executa: nota 0.
- Relatório sem referências, prints ou respostas analíticas: até -2 pontos.
- Entrega fora do formato especificado: -1 ponto.

---

## 10. Extensões opcionais

Só devem ser iniciadas depois que todos os requisitos obrigatórios estiverem completos e testados.

- [ ] Suporte a diferentes idiomas (escolha da lista de stop words).
- [ ] TF-IDF no lugar da frequência simples.
- [ ] Exportação dos resultados em CSV (`resultados.csv`) ou PDF.
- [ ] Visualização da AVL final (arquivo DOT para Graphviz ou texto no console).

---

## 11. Referências sugeridas

- CORMEN, T. H.; LEISERSON, C. E.; RIVEST, R. L.; STEIN, C. *Algoritmos: Teoria e Prática*. 3. ed. Campus/Elsevier, 2012.
- SEDGEWICK, R.; WAYNE, K. *Algorithms*. 4th ed. Addison-Wesley, 2011.
- GOODRICH, M. T.; TAMASSIA, R.; GOLDWASSER, M. H. *Data Structures and Algorithms in Java*. 6th ed. Wiley, 2014.
- MANNING, C. D.; RAGHAVAN, P.; SCHÜTZE, H. *Introduction to Information Retrieval*. Cambridge University Press, 2008.
- JURAFSKY, D.; MARTIN, J. H. *Speech and Language Processing*. 3rd ed. Prentice Hall, 2020.
- BLOCH, J. *Java Efetivo*. 3. ed. Alta Books, 2019.