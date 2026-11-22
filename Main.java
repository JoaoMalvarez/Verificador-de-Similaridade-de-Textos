/* ============================================
 Projeto e Análise de Algoritmos II
 Verificador de Similaridade de Textos

 Integrantes:
   João Pedro Mazzante Alvarez - 10723837
============================================ */

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // ---------- 1. Validação dos argumentos ----------
        // Uso: java Main <diretorio> <limiar> <modo> [K | arquivo1 arquivo2] [polinomial|multiplicativa]
        if (args.length < 3) {
            System.err.println("Uso: java Main <diretorio> <limiar> <lista|topK|busca> [argumentos]");
            return;
        }

        int n = args.length;                 // quantidade de argumentos "úteis"
        boolean multiplicativa = true;       // função hash padrão
        String ultimo = args[n - 1];
        if (n > 3 && (ultimo.equalsIgnoreCase("polinomial") || ultimo.equalsIgnoreCase("multiplicativa"))) {
            multiplicativa = ultimo.equalsIgnoreCase("multiplicativa");
            n--;                             // o último argumento era a função hash, não conta
        }

        String diretorio = args[0];
        double limiar;
        try {
            limiar = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Limiar inválido: " + args[1]);
            return;
        }
        if (limiar < 0 || limiar > 1) {
            System.err.println("O limiar deve estar entre 0 e 1.");
            return;
        }
        String modo = args[2].toLowerCase();

        // ---------- 2. Leitura dos arquivos .txt ----------
        File pasta = new File(diretorio);
        File[] arquivos = pasta.listFiles((d, nome) -> nome.toLowerCase().endsWith(".txt"));
        if (arquivos == null || arquivos.length == 0) {
            System.err.println("Nenhum arquivo .txt encontrado em: " + diretorio);
            return;
        }
        Arrays.sort(arquivos);               // ordem estável (por nome)

        List<Documento> docs = new ArrayList<>();
        for (File f : arquivos) {
            docs.add(new Documento(f.getPath(), f.getName(), multiplicativa));
        }

        String nomeHash = multiplicativa ? "hashMultiplicativo" : "hashPolinomial";
        ComparadorDeDocumentos comparador = new ComparadorDeDocumentos();
        StringBuilder saida = new StringBuilder();
        saida.append("=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===\n");

        // ---------- 3. Modo busca: compara só dois arquivos ----------
        if (modo.equals("busca")) {
            if (n < 5) {
                System.err.println("Modo busca precisa de dois arquivos: busca doc1.txt doc4.txt");
                return;
            }
            Documento a = procurar(docs, args[3]);
            Documento b = procurar(docs, args[4]);
            if (a == null || b == null) {
                System.err.println("Arquivo não encontrado entre os documentos lidos.");