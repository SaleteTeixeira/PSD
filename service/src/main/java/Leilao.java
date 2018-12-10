import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Leilao {
    private String ID;
    private String empresa;
    private float montante;
    private float taxaMaxima;
    private Map<String, Oferta> investidores;

    public Leilao(String ID, String empresa, float montante, float taxaMaxima){
        this.ID = ID;
        this.empresa = empresa;
        this.montante = montante;
        this.taxaMaxima = taxaMaxima;
        this.investidores = new HashMap<>();
    }

    @JsonProperty
    public String getID() {
        return ID;
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
    public float getTaxaMaxima() {
        return taxaMaxima;
    }

    @JsonProperty
    public Map<String, Oferta> getInvestidores() {
        return investidores;
    }

    public void addInvestidor(String investidor, Oferta o){
        this.investidores.put(investidor,o);
    }
}
