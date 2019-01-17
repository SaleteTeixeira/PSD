package main.java;

import java.util.ArrayList;
import java.util.List;

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

    public Empresa(String nome, List<Emprestimo> histEmp, List<Leilao> histLei){
        this.nome = nome;
        this.historicoEmprestimos = histEmp;
        this.historicoLeiloes = histLei;
    }

    public String getNome(){
        return this.nome;
    }

    public void addEmprestimo(Emprestimo e){
        this.historicoEmprestimos.add(e);
    }

    public void addLeilao(Leilao l){
        this.historicoLeiloes.add(l);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("nome: ").append(this.nome).append("\n");
        sb.append("historico_emprestimos -> [\n");
        historicoEmprestimos.forEach(e -> sb.append("emprestimo: ").append(e.toString()));
        sb.append("]\n");
        sb.append("historico_leiloes -> [\n");
        historicoLeiloes.forEach(l -> sb.append("leilao: ").append(l.toString()));
        sb.append("]\n");

        return sb.toString();
    }
}
