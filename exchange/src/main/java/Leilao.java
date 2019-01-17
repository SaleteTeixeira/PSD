import java.util.HashMap;
import java.util.Map;

public class Leilao {
    private String empresa;
    private double montante;
    private double taxaMaxima;
    private Map<String, Oferta> investidores;

    public Leilao(String empresa, double montante, double taxaMaxima){
        this.empresa = empresa;
        this.montante = montante;
        this.taxaMaxima = taxaMaxima;
        this.investidores = new HashMap<>();
    }

    public Leilao(String empresa, double montante, double taxaMaxima, Map<String, Oferta> inv){
        this.empresa = empresa;
        this.montante = montante;
        this.taxaMaxima = taxaMaxima;
        this.investidores = inv;
    }

    public String getEmpresa() {
        return empresa;
    }

    public double getMontante() {
        return montante;
    }

    public double getTaxaMaxima() {
        return taxaMaxima;
    }

    public Map<String, Oferta> getInvestidores() {
        return investidores;
    }

    public void setInvestidores(Map<String, Oferta> inv){
        this.investidores = inv;
    }

    public void addInvestidor(String investidor, Oferta o){
        this.investidores.put(investidor,o);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("empresa: ").append(this.empresa).append("; ");
        sb.append("montante: ").append(this.montante).append("; ");
        sb.append("taxaMaxima: ").append(this.taxaMaxima).append(";  ");
        sb.append("investidores: ");

        for(Map.Entry<String, Oferta> entry: this.investidores.entrySet()){
            sb.append(entry.getKey()).append(": ");

            Oferta o = entry.getValue();
            sb.append("m-").append(o.getMontante()).append(" t-").append(o.getTaxa()).append("  ");
        }
        sb.append("\n");

        return sb.toString();
    }
}
