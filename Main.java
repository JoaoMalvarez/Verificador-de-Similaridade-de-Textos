public class Main {
    public static void main(String[] args) {

        // 1. Arquivo 'LeitorDeDocumentos.java'
        LeitorDeDocumentos leitor = new LeitorDeDocumentos();
        String texto = leitor.lerConteudo("doc1.txt");
        String[] palavras = leitor.criadorDoVetor(texto);

        
    }
}