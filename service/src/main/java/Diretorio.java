import java.util.HashMap;
import java.util.Map;

public class Diretorio {
    private Map<String, Empresa> empresas;
    private Map<String, Emprestimo> emprestimos;
    private Map<String, Leilao> leiloes;

    public Diretorio(){
        this.empresas = new HashMap<String, Empresa>();
        this.emprestimos = new HashMap<String, Emprestimo>();
        this.leiloes = new HashMap<String, Leilao>();
    }

    public Map<String, Empresa> getEmpresas() {
        return empresas;
    }

    public Map<String, Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public Map<String, Leilao> getLeiloes() {
        return leiloes;
    }
    
    public void endEmprestimo(String emprestimo, boolean sucesso){
        Emprestimo e = this.emprestimos.get(emprestimo);
        this.emprestimos.remove(emprestimo);

        if(sucesso) this.empresas.get(e.getEmpresa()).addEmprestimo(e);
    }

    public void endLeilao(String leilao, boolean sucesso){
        Leilao l = this.leiloes.get(leilao);
        this.leiloes.remove(leilao);

        if(sucesso) this.empresas.get(l.getEmpresa()).addLeilao(l);
    }
}
