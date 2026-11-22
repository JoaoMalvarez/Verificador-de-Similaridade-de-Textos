// Lê um arquivo de texto inteiro e transforma o conteúdo em um vetor de palavras

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LeitorDeDocumentos {

    // 1. Lê o arquivo inteiro e devolve o conteúdo como uma única String
    public String lerConteudo(String caminhoArquivo) {
        try {
            Path caminho = Paths.get(caminhoArquivo); // Converte o 'caminho' (texto) para um 'Path'
            return Files.readString(caminho, StandardCharsets.UTF_8); // Lê o arquivo todo de uma vez e o "UTF_8" garante que acentos funcionem
        } catch (IOException e) { // Caso não consiga ler (caminho errado, arquivo inexistente etc.)
            System.err.println("Erro ao ler o arquivo: " + caminhoArquivo);
            return ""; // texto vazio, para o programa não quebrar :P
        }
    }

    // 2. Normaliza o texto e separa em palavras (uma palavra por posição do vetor)
    public String[] criadorDoVetor(String texto) {
        texto = texto.toLowerCase();// Coloca tudo em minúsculo
        texto = texto.replaceAll("[^\\p{L}\\p{N}\\s]", " "); // Troca tudo que não é letra, número ou espaço por um espaço
        // Colinha: '\p{L}' = qualquer letra (inclui acentos) | '\p{N}' = número | '\s' = espaço |
        texto = texto.trim(); // Remove espaços sobrando no começo e no fim do texto
        if (texto.isEmpty()) { // Se não sobrou nada, devolve um vetor vazio (evita um vetor com nada dentro)
            return new String[0];
        }
        return texto.split("\\s+"); // Separa o texto em palavras, usando um ou mais espaços como divisor
    }
}