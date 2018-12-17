package diretorio.representations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class EmprestimoRepresentation {
    public final String empresa;
    public final double montante;
    public final double taxa;
    public final double montanteOferecido;
    public final Map<String, Double> investidores;

    @JsonCreator
    public EmprestimoRepresentation(@JsonProperty("empresa") String e, @JsonProperty("montante") double m, @JsonProperty("taxa") double t,
                                    @JsonProperty("montanteOferecido") double mo, @JsonProperty("investidores") Map<String, Double> i) {
        this.empresa = e;
        this.montante = m;
        this.taxa = t;
        this.montanteOferecido = mo;
        this.investidores = i;
    }
}
