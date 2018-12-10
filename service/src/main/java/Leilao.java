import java.util.HashMap;
import java.util.Map;

public class Leilao {

    private class Oferta{
        private float montante;
        private float taxa;

        public Oferta(float montante, float taxa){
            this.montante = montante;
            this.taxa = taxa;
        }

        public float getMontante() {
            return montante;
        }

        public float getTaxa() {
            return taxa;
        }
    }

    private String empresa;
    private float montante;
    private float taxaMaxima;
    private Map<String, Oferta> investidores;

    public Leilao(String empresa, float montante, float taxaMaxima){
        this.empresa = empresa;
        this.montante = montante;
        this.taxaMaxima = taxaMaxima;
        this.investidores = new HashMap<>();
    }

    public String getEmpresa() {
        return empresa;
    }

    public float getMontante() {
        return montante;
    }

    public float getTaxaMaxima() {
        return taxaMaxima;
    }

    public Map<String, Oferta> getInvestidores() {
        return investidores;
    }

    public void addInvestidor(String investidor, float montante, float taxa){
        Oferta o = new Oferta(montante, taxa);
        this.investidores.put(investidor,o);
    }
}
