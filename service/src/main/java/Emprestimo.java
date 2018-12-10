import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Emprestimo {
    private String empresa;
    private float montante;
    private float taxa;
    private float montanteOferecido;
    private Map<String, Float> investidores;

    public Emprestimo(){

    }

    public Emprestimo(String empresa, float valor, float taxa){
        this.empresa = empresa;
        this.montante = valor;
        this.taxa = taxa;
        this.montanteOferecido = -1;
        this.investidores = new HashMap<String, Float>();
    }

    @JsonProperty
    public String getEmpresa() {
        return empresa;
    }

    @JsonProperty
    public float getMontante() {
        return montante;
    }

    @JsonProperty
    public float getTaxa() {
        return taxa;
    }

    @JsonProperty
    public float getMontanteOferecido() {
        return montanteOferecido;
    }

    @JsonProperty
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
