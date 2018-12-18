package diretorio.representations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OfertaRepresentation {
    public final double montante;
    public final double taxa;

    @JsonCreator
    public OfertaRepresentation(@JsonProperty("montante") double m, @JsonProperty("taxa") double t) {
        this.montante = m;
        this.taxa = t;
    }
}
