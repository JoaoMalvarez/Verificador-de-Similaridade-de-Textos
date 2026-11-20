/* ============================================
Notas:
    > Colocar os valores dentro do espaço em uma
    lista encadeada
============================================ */

public class Espaco<V> {
    public ListaEncadeada<Par<V>> dados;

    public Espaco() {
        this.dados = new ListaEncadeada<>();
    }

    // Procura a palavra neste espaço; devolve o par ou null
    public Par<V> buscar(String chave) {
        Node<Par<V>> atual = dados.getHead();
        while (atual != null) {
            if (atual.getDado().chave.equals(chave)) {
                return atual.getDado();
            }
            atual = atual.getProx();
        }
        return null;
    }

    public void adicionar(String chave, V valor) {
        Par<V> existente = buscar(chave);
        if (existente != null) {
            existente.valor = valor;          // palavra já existe: atualiza
        } else {
            dados.insertTail(new Par<>(chave, valor));
        }
    }
}