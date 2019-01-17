package main.java;

import java.util.HashMap;
import java.util.Map;

public class Emprestimo {
    private String empresa;
    private double montante;
    private double taxa;
    private double montanteOferecido;
    private Map<String, Double> investidores;

    public Emprestimo(String empresa, double valor, double taxa){
        this.empresa = empresa;
        this.montante = valor;
        this.taxa = taxa;
        this.montanteOferecido = -1;
        this.investidores = new HashMap<>();
    }

    public Emprestimo(String empresa, double valor, double taxa, double montanteOfer, Map<String, Double> inv){
        this.empresa = empresa;
        this.montante = valor;
        this.taxa = taxa;
        this.montanteOferecido = montanteOfer;
        this.investidores = inv;
    }

    public String getEmpresa() {
        return empresa;
    }

    public double getMontante() {
        return montante;
    }

    public double getTaxa() {
        return taxa;
    }

    public double getMontanteOferecido() {
        return montanteOferecido;
    }

    public Map<String, Double> getInvestidores() {
        return investidores;
    }

    public void setInvestidores(Map<String, Double> inv){
        this.investidores = inv;
    }

    public void addInvestidor(String investidor, double valor){
        this.investidores.put(investidor,valor);
        montanteOferecido();
    }

    private void montanteOferecido() {
        this.investidores.values().forEach(v -> this.montanteOferecido += v);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("empresa: ").append(this.empresa).append("; ");
        sb.append("montante: ").append(this.montante).append("; ");
        sb.append("taxa: ").append(this.taxa).append(";  ");
        sb.append("montanteOferecido: ").append(this.montanteOferecido).append("; ");
        sb.append("investidores: ");

        for(Map.Entry<String, Double> entry: this.investidores.entrySet()){
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("  ");
        }
        sb.append("\n");

        return sb.toString();
    }
}
