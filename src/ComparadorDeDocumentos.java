public class ComparadorDeDocumentos {

    public double calcularSimilaridade(Documento a, Documento b) {
        Tabela_Hash<Integer> freqA = a.getFrequencias();
        Tabela_Hash<Integer> freqB = b.getFrequencias();

        double produto = 0;   // soma de freqA * freqB das palavras em comum
        double somaA = 0;     // soma dos quadrados das frequências de A

        // Percorre as palavras de A
        for (String palavra : freqA.chaves()) {
            int fa = freqA.get(palavra);
            somaA += fa * fa;

            Integer fb = freqB.get(palavra);   // null se B não tem a palavra
            if (fb != null) {
                produto += fa * fb;
            }
        }

        // Percorre as palavras de B só para a norma
        double somaB = 0;
        for (String palavra : freqB.chaves()) {
            int fb = freqB.get(palavra);
            somaB += fb * fb;
        }

        double normaA = Math.sqrt(somaA);
        double normaB = Math.sqrt(somaB);

        if (normaA == 0 || normaB == 0) return 0;   // documento vazio

        return produto / (normaA * normaB);
    }
}