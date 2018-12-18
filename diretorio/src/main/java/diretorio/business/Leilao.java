package diretorio.business;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Leilao {
    private String empresa;
    private double montante;
    private double taxaMaxima;
    private Map<String, Oferta> investidores;

    public Leilao(){

    }

    public Leilao(String empresa, double montante, double taxaMaxima){
        this.empresa = empresa;
        this.montante = montante;
        this.taxaMaxima = taxaMaxima;
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
    public double getTaxaMaxima() {
        return taxaMaxima;
    }

    @JsonProperty
    public Map<String, Oferta> getInvestidores() {
        return investidores;
    }

    @JsonProperty
    public void setInvestidores(Map<String, Oferta> inv){
        this.investidores = inv;
    }

    public void addInvestidor(String investidor, Oferta o){
        this.investidores.put(investidor,o);
    }
}
