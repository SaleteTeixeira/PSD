package diretorio.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Diretorio {

    private static Diretorio instance = null;

    public static Diretorio getInstance(){
        if (instance == null){
            instance = new Diretorio();
        }

        return instance;
    }


    private Map<String, Empresa> empresas;
    private Map<String, Emprestimo> emprestimos;
    private Map<String, Leilao> leiloes;

    public Diretorio(){
        this.empresas = new HashMap<String, Empresa>();
        initEmpresas();
        this.emprestimos = new HashMap<String, Emprestimo>();
        this.leiloes = new HashMap<String, Leilao>();
    }

    private void initEmpresas(){
        Empresa e1 = new Empresa("Mango");
        Empresa e2 = new Empresa("Zara");
        Empresa e3 = new Empresa("Kiko");
        Empresa e4 = new Empresa("Parfois");
        Empresa e5 = new Empresa("Asus");

        this.empresas.put("Mango", e1);
        this.empresas.put("Zara", e2);
        this.empresas.put("Kiko", e3);
        this.empresas.put("Parfois", e4);
        this.empresas.put("Asus", e5);
    }

    public List<Empresa> getEmpresas() {
        return new ArrayList<Empresa>(this.empresas.values());
    }

    public List<Emprestimo> getEmprestimos() {
        return new ArrayList<Emprestimo>(this.emprestimos.values());
    }

    public List<Leilao> getLeiloes() {
        return new ArrayList<Leilao>(this.leiloes.values());
    }

    public Empresa getEmpresa(String empresa){
        return this.empresas.get(empresa);
    }

    public Emprestimo getEmprestimo(String empresa){
        return this.emprestimos.get(empresa);
    }

    public Leilao getLeilao(String empresa){
        return this.leiloes.get(empresa);
    }

    public void addEmprestimo(Emprestimo e){
        this.emprestimos.put(e.getEmpresa(), e);
    }

    public void addLeilao(Leilao l){
        this.leiloes.put(l.getEmpresa(), l);
    }

    public void endEmprestimo(String empresa, Map<String, Double> investidores){
        Emprestimo e = this.emprestimos.get(empresa);
        this.emprestimos.remove(empresa);

        if((investidores != null) && (investidores.size() > 0)){
            e.setInvestidores(investidores);
            this.empresas.get(e.getEmpresa()).addEmprestimo(e);
        }
    }

    public void endLeilao(String empresa, Map<String, Oferta> investidores){
        Leilao l = this.leiloes.get(empresa);
        this.leiloes.remove(empresa);

        if((investidores != null) && (investidores.size() > 0)){
            l.setInvestidores(investidores);
            this.empresas.get(l.getEmpresa()).addLeilao(l);
        }
    }

    public Leilao lastLeilao(String empresa){
        return this.empresas.get(empresa).lastLeilao();
    }

    public Emprestimo lastEmprestimo(String empresa){
        return this.empresas.get(empresa).lastEmprestimo();
    }
}
