package diretorio.representations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class EmprestimoRepresentation {
    public final String empresa;
    public final float montante;
    public final float taxa;
    public final float montanteOferecido;
    public final Map<String, Float> investidores;

    @JsonCreator
    public EmprestimoRepresentation(@JsonProperty("empresa") String e, @JsonProperty("montante") float m, @JsonProperty("taxa") float t,
                                 @JsonProperty("montante_oferecido") float mo, @JsonProperty("investidores") Map<String, Float> i) {
        this.empresa = e;
        this.montante = m;
        this.taxa = t;
        this.montanteOferecido = mo;
        this.investidores = i;
    }
}
