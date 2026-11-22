import java.util.List;

public class ArvoreAVL {
    private No raiz;
    private RegistroRotacoes reg;   // contagem acumulada

    public ArvoreAVL() {
        raiz = null;
        reg = new RegistroRotacoes();
    }

    // ---------- utilitários de altura e balanceamento ----------
    private int altura(No n) {
        return (n == null) ? 0 : n.altura;
    }

    private void atualizarAltura(No n) {
        n.altura = 1 + Math.max(altura(n.esq), altura(n.dir));
    }

    // fator = altura direita - altura esquerda (mesma convenção do seu código)
    private int fator(No n) {
        return (n == null) ? 0 : altura(n.dir) - altura(n.esq);
    }

    // ---------- rotações ----------
    private No rotacaoDireita(No p) {      // caso esquerda-esquerda
        No u = p.esq;
        p.esq = u.dir;
        u.dir = p;
        atualizarAltura(p);
        atualizarAltura(u);
        return u;
    }

    private No rotacaoEsquerda(No p) {     // caso direita-direita
        No z = p.dir;
        p.dir = z.esq;
        z.esq = p;
        atualizarAltura(p);
        atualizarAltura(z);
        return z;
    }

    // ---------- inserção ----------
    // Retorna o registro com as contagens acumuladas
    public RegistroRotacoes inserir(Resultado r) {
        raiz = inserir(raiz, r);
        return reg;
    }

    private No inserir(No p, Resultado r) {
        if (p == null) return new No(r.getSimilaridade(), r);

        double s = r.getSimilaridade();
        if (s < p.chave) {
            p.esq = inserir(p.esq, r);
        } else if (s > p.chave) {
            p.dir = inserir(p.dir, r);
        } else {
            p.pares.add(r);          // mesma similaridade: só entra na lista
            return p;
        }

        atualizarAltura(p);
        int f = fator(p);

        if (f == -2) {                       // pesado à esquerda
            if (fator(p.esq) <= 0) {         // esquerda-esquerda
                reg.simplesDir++;
                return rotacaoDireita(p);
            } else {                         // esquerda-direita
                reg.duplaEsqDir++;
                p.esq = rotacaoEsquerda(p.esq);
                return rotacaoDireita(p);
            }
        }
        if (f == 2) {                        // pesado à direita
            if (fator(p.dir) >= 0) {         // direita-direita
                reg.simplesEsq++;
                return rotacaoEsquerda(p);
            } else {                         // direita-esquerda
                reg.duplaDirEsq++;
                p.dir = rotacaoDireita(p.dir);
                return rotacaoEsquerda(p);
            }
        }
        return p;
    }

    // ---------- travessias ----------
    // Crescente: da menor para a maior similaridade
    public void emOrdem(List<Resultado> saida) {
        emOrdem(raiz, saida);
    }
    private void emOrdem(No n, List<Resultado> saida) {
        if (n == null) return;
        emOrdem(n.esq, saida);
        saida.addAll(n.pares);
        emOrdem(n.dir, saida);
    }

    // Decrescente: da maior para a menor (útil para topK)
    public void emOrdemDecrescente(List<Resultado> saida) {
        emOrdemDecrescente(raiz, saida);
    }
    private void emOrdemDecrescente(No n, List<Resultado> saida) {
        if (n == null) return;
        emOrdemDecrescente(n.dir, saida);
        saida.addAll(n.pares);
        emOrdemDecrescente(n.esq, saida);
    }

    public int altura() { return altura(raiz); }
    public RegistroRotacoes getRegistro() { return reg; }
}