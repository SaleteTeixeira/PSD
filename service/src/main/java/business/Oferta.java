package business;

public class Oferta{
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