public class Documento {

    private String nome;                           // nome do arquivo (como: doc1.txt)
    private Tabela_Hash<Integer> frequencias;      // palavra -> quantas vezes aparece

    private static final String[] LISTA_STOP_WORDS = {
        "a", "o", "e", "é", "de", "da", "do", "das", "dos", "em", "no", "na",
        "nos", "nas", "um", "uma", "uns", "umas", "para", "por", "com", "sem",
        "que", "se", "ao", "aos", "as", "os", "ou", "mas", "como", "mais",
        "foi", "são", "ser", "ter", "seu", "sua", "seus", "suas", "isso", "este",
        "esta", "esse", "essa", "já", "não", "só", "também", "entre"
    };

    // Tabela de stop words: "static" = existe uma só, compartilhada por todos os Documentos
    private static Tabela_Hash<Integer> stopWords = criarStopWords();

    // 1. Carrega as stop words numa Tabela_Hash (valor 1 só marca "esta palavra existe")
    private static Tabela_Hash<Integer> criarStopWords() {
        Tabela_Hash<Integer> tabela = new Tabela_Hash<>(101, false);
        for (String palavra : LISTA_STOP_WORDS) {
            tabela.put(palavra, 1);
        }
        return tabela;
    }

    // 2. Construtor: lê o arquivo, normaliza e preenche a tabela de frequências
    public Documento(String caminhoArquivo, String nome, boolean usarMultiplicativa) {
        this.nome = nome;
        this.frequencias = new Tabela_Hash<>(211, usarMultiplicativa);

        LeitorDeDocumentos leitor = new LeitorDeDocumentos();
        String texto = leitor.lerConteudo(caminhoArquivo);   // arquivo -> String
        String[] palavras = leitor.criadorDoVetor(texto);    // String -> palavras

        for (String palavra : palavras) {
            // get devolve null se a palavra não está na tabela de stop words
            if (stopWords.get(palavra) != null) continue;    // é stop word: pula

            Integer atual = frequencias.get(palavra);
            if (atual == null) {
                frequencias.put(palavra, 1);                 // primeira vez que aparece
            } else {
                frequencias.put(palavra, atual + 1);         // já apareceu: soma 1
            }
        }
    }

    // 3. 'getNome'
    public String getNome() { return nome; }

    // 4. 'getFrequencias'
    public Tabela_Hash<Integer> getFrequencias() { return frequencias; }
}