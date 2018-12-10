package diretorio.representations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class LeilaoRepresentation {
    public final String empresa;
    public final float montante;
    public final float taxaMaxima;
    public final Map<String, OfertaRepresentation> investidores;

    @JsonCreator
    public LeilaoRepresentation(@JsonProperty("empresa") String e, @JsonProperty("montante") float m,
                                    @JsonProperty("taxaMaxima") float tm,
                                    @JsonProperty("investidores") Map<String, OfertaRepresentation> i) {
        this.empresa = e;
        this.montante = m;
        this.taxaMaxima = tm;
        this.investidores = i;
    }
}
