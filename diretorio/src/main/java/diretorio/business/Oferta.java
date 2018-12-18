package diretorio.business;

public class Oferta{
    private double montante;
    private double taxa;

    public Oferta(double montante, double taxa){
        this.montante = montante;
        this.taxa = taxa;
    }

    public double getMontante() {
        return montante;
    }

    public double getTaxa() {
        return taxa;
    }
}