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
                return;
            }
            double sim = comparador.calcularSimilaridade(a, b);
            saida.append("Comparando: ").append(a.getNome()).append(" <-> ").append(b.getNome()).append("\n");
            saida.append(String.format(java.util.Locale.US, "Similaridade calculada: %.2f%n", sim));
            saida.append("Métrica utilizada: Cosseno\n");
            emitir(saida.toString());
            return;
        }

        // ---------- 4. Compara todos os pares e insere na AVL ----------
        ArvoreAVL arvore = new ArvoreAVL();
        int totalPares = 0;
        for (int i = 0; i < docs.size(); i++) {
            for (int j = i + 1; j < docs.size(); j++) {
                double sim = comparador.calcularSimilaridade(docs.get(i), docs.get(j));
                arvore.inserir(new Resultado(docs.get(i).getNome(), docs.get(j).getNome(), sim));
                totalPares++;
            }
        }

        // Travessia decrescente: do mais parecido para o menos parecido
        List<Resultado> decrescente = new ArrayList<>();
        arvore.emOrdemDecrescente(decrescente);

        saida.append("Total de documentos processados: ").append(docs.size()).append("\n");
        saida.append("Total de pares comparados: ").append(totalPares).append("\n");
        saida.append("Função hash utilizada: ").append(nomeHash).append("\n");
        saida.append("Métrica de similaridade: Cosseno\n\n");

        // ---------- 5. Modos lista e topK ----------
        int limite = Integer.MAX_VALUE;      // lista: sem limite de quantidade
        if (modo.equals("topk")) {
            if (n < 4) {
                System.err.println("Modo topK precisa do valor de K: topK 5");
                return;
            }
            try {
                limite = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                System.err.println("K inválido: " + args[3]);
                return;
            }
        } else if (!modo.equals("lista")) {
            System.err.println("Modo inválido: " + args[2] + " (use lista, topK ou busca)");
            return;
        }

        saida.append(String.format(java.util.Locale.US, "Pares com similaridade >= %.2f:%n", limiar));
        int mostrados = 0;
        for (Resultado r : decrescente) {
            if (r.getSimilaridade() < limiar || mostrados >= limite) break;
            saida.append(r).append("\n");
            mostrados++;
        }
        if (mostrados == 0) saida.append("(nenhum par encontrado)\n");

        // Os pares de menor similaridade: pega do fim da lista decrescente (até 3)
        saida.append("\nPares com menor similaridade:\n");
        int inicio = Math.max(0, decrescente.size() - 3);
        for (int i = decrescente.size() - 1; i >= inicio; i--) {
            saida.append(decrescente.get(i)).append("\n");
        }

        emitir(saida.toString());

        // Contagem de rotações (útil para o relatório; vai para o stderr para não poluir a saída)
        RegistroRotacoes reg = arvore.getRegistro();
        System.err.println("[AVL] simples esq: " + reg.simplesEsq + " | simples dir: " + reg.simplesDir
                + " | dupla esq-dir: " + reg.duplaEsqDir + " | dupla dir-esq: " + reg.duplaDirEsq
                + " | altura: " + arvore.altura());
    }

    // Procura um documento pelo nome do arquivo
    private static Documento procurar(List<Documento> docs, String nome) {
        for (Documento d : docs) {
            if (d.getNome().equals(nome)) return d;
        }
        return null;
    }

    // Mostra no terminal E grava em resultado.txt (sem interação manual)
    private static void emitir(String texto) {
        System.out.print(texto);
        try {
            Files.writeString(Paths.get("resultado.txt"), texto, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Erro ao gravar resultado.txt");
        }
    }
}== null) {
                System.err.println("Arquivo não encontrado entre os documentos lidos.");
