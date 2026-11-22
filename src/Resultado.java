// Resultado: Guarda o resultado da comparação entre dois documentos

public class Resultado {

    private String doc1;           // Nome do primeiro documento
    private String doc2;           // Nome do segundo documento
    private double similaridade;   // Valor entre 0 e 1 (0% e 100% de similaridade entre os dois :D)

    public Resultado(String doc1, String doc2, double similaridade) {
        this.doc1 = doc1;
        this.doc2 = doc2;
        this.similaridade = similaridade;
    }

    public String getDoc1() { return doc1; }
    public String getDoc2() { return doc2; }
    public double getSimilaridade() { return similaridade; }

    // Formata como a saída exige: "doc1.txt <-> doc2.txt = 0.82"
    @Override
    public String toString() {
        return doc1 + " <-> " + doc2 + " = " + String.format("%.2f", similaridade);
    }
}