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

    public Empresa getEmpresa(String empresa){
        return this.empresas.get(empresa);
    }

    public Emprestimo getEmprestimo(String emprestimo){
        return this.emprestimos.get(emprestimo);
    }

    public Leilao getLeilao(String leilao){
        return this.leiloes.get(leilao);
    }

    public void addEmprestimo(Emprestimo e){
        this.emprestimos.put(e.getID(), e);
    }

    public void addLeilao(Leilao l){
        this.leiloes.put(l.getID(), l);
    }

    public void endEmprestimo(String emprestimo, Map<String, Float> investidores){
        Emprestimo e = this.emprestimos.get(emprestimo);
        this.emprestimos.remove(emprestimo);

        if(investidores.size() > 0){
            investidores.keySet().forEach(k -> e.addInvestidor(k, investidores.get(k)));
            this.empresas.get(e.getEmpresa()).addEmprestimo(e);
        }
    }

    public void endLeilao(String leilao, Map<String, Oferta> investidores){
        Leilao l = this.leiloes.get(leilao);
        this.leiloes.remove(leilao);

        if(investidores.size() > 0){
            investidores.keySet().forEach(k -> l.addInvestidor(k, investidores.get(k)));
            this.empresas.get(l.getEmpresa()).addLeilao(l);
        }
    }
}
