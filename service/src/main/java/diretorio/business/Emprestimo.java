package diretorio.business;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Emprestimo {
    private String empresa;
    private double montante;
    private double taxa;
    private double montanteOferecido;
    private Map<String, Double> investidores;

    public Emprestimo(){

    }

    public Emprestimo(String empresa, double valor, double taxa){
        this.empresa = empresa;
        this.montante = valor;
        this.taxa = taxa;
        this.montanteOferecido = 0;
        this.investidores = new HashMap<>();
    }

    @JsonProperty
    public String getEmpresa() {
        return empresa;
    }

    @JsonProperty
    public double getMontante() {
        return montante;
    }

    @JsonProperty
    public double getTaxa() {
        return taxa;
    }

    @JsonProperty
    public double getMontanteOferecido() {
        return montanteOferecido;
    }

    @JsonProperty
    public void setInvestidores(Map<String, Double> inv){
        this.investidores = inv;
        montanteOferecido();
    }

    @JsonProperty
    public Map<String, Double> getInvestidores() {
        return investidores;
    }

    public void addInvestidor(String investidor, double valor){
        this.investidores.put(investidor,valor);
        montanteOferecido += valor;
        montanteOferecido += valor;
    }

    private void montanteOferecido() {
        this.investidores.values().forEach(v -> this.montanteOferecido += v);
    }
}
