public class Tabela_Hash<V> {

    private Espaco<V>[] espaco;
    private int tamanho;

    public Tabela_Hash(int tamanho) {
        this.tamanho = tamanho;
        /* Cria o espaço */
        this.espaco = new Espaco[tamanho];
        for(int i = 0; i < m; i++) {
            espaco[i] = new Espaco(i);
        }
    }

    
}