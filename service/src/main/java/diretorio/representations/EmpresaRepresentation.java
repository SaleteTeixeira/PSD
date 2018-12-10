package diretorio.representations;

import com.fasterxml.jackson.annotation.*;

import java.util.List;

public class EmpresaRepresentation {
    public final String nome;
    public final List<EmprestimoRepresentation> historicoEmprestimos;
    public final List<LeilaoRepresentation> historicoLeiloes;

    @JsonCreator
    public EmpresaRepresentation(@JsonProperty("nome") String n, @JsonProperty("histEmp") List<EmprestimoRepresentation> e,
                                 @JsonProperty("histLei") List<LeilaoRepresentation> l) {
        this.nome = n;
        this.historicoEmprestimos = e;
        this.historicoLeiloes = l;
    }
}
