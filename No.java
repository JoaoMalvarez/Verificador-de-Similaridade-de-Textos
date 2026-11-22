import java.util.ArrayList;
import java.util.List;

// Nó da árvore AVL: guarda uma similaridade e todos os pares que têm esse valor
public class No {
    public double chave;               // similaridade (chave de ordenação)
    public List<Resultado> pares;      // pares de documentos com essa similaridade
    public No esq;
    public No dir;
    public int altura;                 // altura do nó (folha = 1)

    public No(double chave, Resultado r) {
        this.chave = chave;
        this.pares = new ArrayList<>();
        this.pares.add(r);
        this.esq = null;
        this.dir = null;
        this.altura = 1;
    }
}