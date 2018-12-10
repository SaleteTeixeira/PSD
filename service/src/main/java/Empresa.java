import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Empresa {
    private String nome;
    private List<Emprestimo> historicoEmprestimos;
    private List<Leilao> historicoLeiloes;

    public Empresa(){

    }

    public Empresa(String nome){
        this.nome = nome;
        this.historicoEmprestimos = new ArrayList<Emprestimo>();
        this.historicoLeiloes = new ArrayList<Leilao>();
    }

    @JsonProperty
    public String getNome() {
        return nome;
    }

    @JsonProperty
    public List<Emprestimo> getHistoricoEmprestimos() {
        return this.historicoEmprestimos;
    }

    @JsonProperty
    public List<Leilao> getHistoricoLeiloes() {
        return historicoLeiloes;
    }

    public void addEmprestimo(Emprestimo e){
        this.historicoEmprestimos.add(e);
    }

    public void addLeilao(Leilao l){
        this.historicoLeiloes.add(l);
    }
}
