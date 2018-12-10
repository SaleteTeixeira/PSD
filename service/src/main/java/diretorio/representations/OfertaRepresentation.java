package diretorio.representations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OfertaRepresentation {
    public final float montante;
    public final float taxa;

    @JsonCreator
    public OfertaRepresentation(@JsonProperty("montante") float m, @JsonProperty("taxa") float t) {
        this.montante = m;
        this.taxa = t;
    }
}
