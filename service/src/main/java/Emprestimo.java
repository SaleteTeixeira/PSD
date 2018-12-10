import java.util.HashMap;
import java.util.Map;

public class Emprestimo {
    private String empresa;
    private float montante;
    private float taxa;
    private float montanteOferecido;
    private Map<String, Float> investidores;

    public Emprestimo(String empresa, float valor, float taxa){
        this.empresa = empresa;
        this.montante = valor;
        this.taxa = taxa;
        this.montanteOferecido = -1;
        this.investidores = new HashMap<String, Float>();
    }

    public String getEmpresa() {
        return empresa;
    }

    public float getMontante() {
        return montante;
    }

    public float getTaxa() {
        return taxa;
    }

    public float getMontanteOferecido() {
        return montanteOferecido;
    }

    public Map<String, Float> getInvestidores() {
        return investidores;
    }

    public void addInvestidor(String investidor, float valor){
        this.investidores.put(investidor,valor);
        montanteOferecido();
    }

    private void montanteOferecido() {
        this.investidores.values().forEach(v -> this.montanteOferecido += v);
    }
}
