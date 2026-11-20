import java.util.ArrayList;
import java.util.List;

// Tabela hash genérica feita à mão: chave (palavra) -> valor V.
// Colisões são resolvidas por encadeamento: cada espaço guarda uma lista encadeada.
public class Tabela_Hash<V> {
    private Espaco<V>[] arm;            // os espaços (buckets) da tabela
    private int m;                      // quantidade de espaços
    private int elementos;              // palavras distintas guardadas
    private boolean usarMultiplicativa; // escolhe a função hash (true = multiplicativa, false = polinomial)

    // Construtor: cria a tabela com m espaços e define qual função hash será usada
    @SuppressWarnings("unchecked") // o aviso aparece porque Java não permite criar array de tipo genérico direto
    public Tabela_Hash(int m, boolean usarMultiplicativa) {
        this.m = m;
        this.usarMultiplicativa = usarMultiplicativa;
        // Cria um array "cru" de Espaco e converte para Espaco<V>[] (generics e arrays não combinam)
        this.arm = (Espaco<V>[]) new Espaco[m];
        // Cada posição do array começa com um espaço vazio (com sua lista encadeada vazia)
        for (int i = 0; i < m; i++) {
            arm[i] = new Espaco<>();
        }
    }

    // 1. Polinomial (base 31).
    private int hashPolinomial(String chave) {
        int h = 0;
        for (int i = 0; i < chave.length(); i++) { // percorre a palavra letra por letra
            // "charAt": devolve a letra que está na posição i da String (como no Java o 'char' é um número, a conta funciona)
            // Nota: multiplicar 'h' por '31' faz a posição de cada letra importar
            h = 31 * h + chave.charAt(i);   // pode estourar o int (overflow) e ficar negativo; é aceitável aqui
        }
        // "floorMod": calcula o resto (assim como o '%'), mas nunca devolve negativo enquanto m for positivo
        return Math.floorMod(h, m);
    }

    // 2. Multiplicativa (constante de Knuth, 2^32 / proporção áurea).
    private int hashMultiplicativa(String chave) {
        // 1 - Transforma a palavra em número (como no polinomial, só que na base 131)
        long k = 0;
        for (int i = 0; i < chave.length(); i++) {
            // O "& 0xFFFFFFFFL" corta o número para caber em 32 bits (equivale a mod 2^32)
            k = (k * 131 + chave.charAt(i)) & 0xFFFFFFFFL;
        }

        // 2 - Multiplica pela constante de Knuth para embaralhar os bits, de modo que palavras parecidas caiam em índices diferentes
        long h = (k * 2654435769L) & 0xFFFFFFFFL;

        // 3 - regra de três para converter h (0 a 2^32) em índice (0 a m-1).
        // ">>> 32" divide por 2^32 e o (int) converte o long para int
        return (int) ((h * m) >>> 32);
    }

    // 3. Seletor de índice: escolhe qual função hash usar
    private int indice(String chave) {
        return usarMultiplicativa ? hashMultiplicativa(chave) : hashPolinomial(chave);
        /* Basicamente o que eu fiz em uma linha:

        if (usarMultiplicativa) {
            return hashMultiplicativa(chave);
        }
        return hashPolinomial(chave);

        */
    }

    // 4. 'put': insere a palavra com o valor, se a palavra já existe, só atualiza o valor
    public void put(String chave, V valor) {
        // Descobre em qual espaço a palavra deve ficar
        Espaco<V> espaco = arm[indice(chave)];
        // Procura a palavra na lista daquele espaço (pode ter outras palavras por colisão)
        Par<V> existente = espaco.buscar(chave);
        if (existente != null) {
            existente.valor = valor;                          // se já existe, atualiza
        } else {
            espaco.dados.insertHead(new Par<>(chave, valor)); // se não existe, adiciona no começo da lista
            elementos++;                                      // conta mais uma palavra distinta
        }
    }

    // 5. 'get': devolve o valor associado à palavra, ou null se ela não existir
    public V get(String chave) {
        // Vai direto ao espaço certo e procura só dentro dele
        Par<V> par = arm[indice(chave)].buscar(chave);
        return par == null ? null : par.valor;
    }

    // 6. Para o comparador percorrer todas as palavras
    public List<String> chaves() {
        List<String> lista = new ArrayList<>();
        // Passa por todos os espaços da tabela
        for (int i = 0; i < m; i++) {
            // Pega o primeiro nó da lista encadeada do espaço
            Node<Par<V>> atual = arm[i].dados.getHead();
            // Anda nó por nó até o fim da lista, guardando cada palavra
            while (atual != null) {
                lista.add(atual.getDado().chave);
                atual = atual.getProx();
            }
        }
        return lista;
    }

    // 7. Para a análise de colisões do relatório
    public int[] tamanhoDosBuckets() {
        int[] tamanhos = new int[m];
        // tamanhos[i] = quantas palavras estão no espaço i
        // (quanto mais parecidos os valores, melhor a função hash espalhou)
        for (int i = 0; i < m; i++) {
            tamanhos[i] = arm[i].dados.getSize();
        }
        return tamanhos;
    }

    // 8. Quantidade de palavras distintas guardadas na tabela
    public int getElementos() { return elementos; }
}