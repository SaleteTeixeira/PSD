package diretorio.resources;

import diretorio.business.*;
import diretorio.representations.EmpresaRepresentation;
import diretorio.representations.EmprestimoRepresentation;
import diretorio.representations.LeilaoRepresentation;
import diretorio.representations.OfertaRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiretorioResource {
    private Diretorio diretorio;

    public DiretorioResource(Diretorio diretorio){
        this.diretorio = diretorio;
    }

    @GET
    @Path("get_empresas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EmpresaRepresentation> getEmpresas() {
        List<EmpresaRepresentation> result = new ArrayList<>();
        List<Empresa> empresas = this.diretorio.getEmpresas();

        for(Empresa emp: empresas){
            List<EmprestimoRepresentation> histEmp = new ArrayList<>();
            List<LeilaoRepresentation> histLei = new ArrayList<>();

            for(Emprestimo e: emp.getHistoricoEmprestimos()){
                EmprestimoRepresentation auxEmp = new EmprestimoRepresentation(e.getEmpresa(), e.getMontante(), e.getTaxa(),
                        e.getMontanteOferecido(), e.getInvestidores());
                histEmp.add(auxEmp);
            }

            for(Leilao l: emp.getHistoricoLeiloes()){
                Map<String, OfertaRepresentation> inv = new HashMap<>();

                for(Map.Entry<String, Oferta> entry: l.getInvestidores().entrySet()){
                    inv.put(entry.getKey(), new OfertaRepresentation(entry.getValue().getMontante(), entry.getValue().getTaxa()));
                }

                LeilaoRepresentation lr = new LeilaoRepresentation(l.getEmpresa(), l.getMontante(), l.getTaxaMaxima(), inv);
                histLei.add(lr);
            }

            EmpresaRepresentation er = new EmpresaRepresentation(emp.getNome(), histEmp, histLei);
            result.add(er);
        }

        return result;
    }

    @GET
    @Path("get_emprestimos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EmprestimoRepresentation> getEmprestimos() {
        List<EmprestimoRepresentation> result = new ArrayList<>();
        List<Emprestimo> emprestimos = this.diretorio.getEmprestimos();

        for(Emprestimo e: emprestimos){
            EmprestimoRepresentation auxEmp = new EmprestimoRepresentation(e.getEmpresa(), e.getMontante(), e.getTaxa(),
                    e.getMontanteOferecido(), e.getInvestidores());
            result.add(auxEmp);
        }

        return result;
    }

    @GET
    @Path("get_leiloes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<LeilaoRepresentation> getLeiloes() {
        List<LeilaoRepresentation> result = new ArrayList<>();
        List<Leilao> leiloes = this.diretorio.getLeiloes();

        for(Leilao l: leiloes){
            Map<String, OfertaRepresentation> inv = new HashMap<>();

            for(Map.Entry<String, Oferta> entry: l.getInvestidores().entrySet()){
                inv.put(entry.getKey(), new OfertaRepresentation(entry.getValue().getMontante(), entry.getValue().getTaxa()));
            }

            LeilaoRepresentation lr = new LeilaoRepresentation(l.getEmpresa(), l.getMontante(), l.getTaxaMaxima(), inv);
            result.add(lr);
        }

        return result;
    }

    @GET
    @Path("get_empresa/{nome}")
    @Produces(MediaType.APPLICATION_JSON)
    public EmpresaRepresentation getEmpresa(@PathParam("nome") String nome) {
        Empresa emp = this.diretorio.getEmpresa(nome);
        List<EmprestimoRepresentation> histEmp = new ArrayList<>();
        List<LeilaoRepresentation> histLei = new ArrayList<>();

        for(Emprestimo e: emp.getHistoricoEmprestimos()){
            EmprestimoRepresentation auxEmp = new EmprestimoRepresentation(e.getEmpresa(), e.getMontante(), e.getTaxa(),
                    e.getMontanteOferecido(), e.getInvestidores());
            histEmp.add(auxEmp);
        }

        for(Leilao l: emp.getHistoricoLeiloes()){
            Map<String, OfertaRepresentation> inv = new HashMap<>();

            for(Map.Entry<String, Oferta> entry: l.getInvestidores().entrySet()){
                inv.put(entry.getKey(), new OfertaRepresentation(entry.getValue().getMontante(), entry.getValue().getTaxa()));
            }

            LeilaoRepresentation lr = new LeilaoRepresentation(l.getEmpresa(), l.getMontante(), l.getTaxaMaxima(), inv);
            histLei.add(lr);
        }

        EmpresaRepresentation er = new EmpresaRepresentation(emp.getNome(), histEmp, histLei);
        return er;
    }

    @GET
    @Path("get_emprestimo/{empresa}")
    @Produces(MediaType.APPLICATION_JSON)
    public EmprestimoRepresentation getEmprestimo(@PathParam("empresa") String empresa) {
        Emprestimo e = this.diretorio.getEmprestimo(empresa);

        EmprestimoRepresentation er = new EmprestimoRepresentation(e.getEmpresa(), e.getMontante(), e.getTaxa(),
                e.getMontanteOferecido(), e.getInvestidores());

        return er;
    }

    @GET
    @Path("get_leilao/{empresa}")
    @Produces(MediaType.APPLICATION_JSON)
    public LeilaoRepresentation getLeilao(@PathParam("empresa") String empresa) {
        Leilao l = this.diretorio.getLeilao(empresa);
        Map<String, OfertaRepresentation> inv = new HashMap<>();

        for(Map.Entry<String, Oferta> entry: l.getInvestidores().entrySet()){
            inv.put(entry.getKey(), new OfertaRepresentation(entry.getValue().getMontante(), entry.getValue().getTaxa()));
        }

        LeilaoRepresentation lr = new LeilaoRepresentation(l.getEmpresa(), l.getMontante(), l.getTaxaMaxima(), inv);

        return lr;
    }

    @PUT
    @Path("add_emprestimo/{nome}_{valor}_{taxa}")
    public Response putEmprestimo(@PathParam("nome") String n, @PathParam("valor") float v, @PathParam("taxa") float t) {
        Emprestimo e = new Emprestimo(n, v, t);
        synchronized (this) {this.diretorio.addEmprestimo(e);}
        return Response.ok().build();
    }

    @PUT
    @Path("add_leilao/{nome}_{valor}_{taxa}")
    public Response putLeilao(@PathParam("nome") String n, @PathParam("valor") float v, @PathParam("taxa") float t) {
        Leilao l = new Leilao(n, v, t);
        synchronized (this) {this.diretorio.addLeilao(l);}
        return Response.ok().build();
    }

    @POST
    @Path("end_emprestimo/{empresa}_{inv}_{montante}")
    public Response endEmprestimo(@PathParam("empresa") String e, @PathParam("inv") List<String> inv, @PathParam("montante") List<Float> m) {
        Map<String, Float> invest = new HashMap<>();

        for(int i=0; i<inv.size(); i++){
            invest.put(inv.get(i), m.get(i));
        }

        synchronized (this) {this.diretorio.endEmprestimo(e, invest);}
        return Response.ok().build();
    }

    @POST
    @Path("end_leilao/{empresa}_{inv}_{montante}_{taxa}")
    public Response endLeilao(@PathParam("empresa") String e, @PathParam("inv") List<String> inv,
                              @PathParam("montante") List<Float> m, @PathParam("taxa") List<Float> t) {
        Map<String, Oferta> invest = new HashMap<>();

        for(int i=0; i<inv.size(); i++){
            Oferta o = new Oferta(m.get(i), t.get(i));
            invest.put(inv.get(i), o);
        }

        synchronized (this) {this.diretorio.endLeilao(e, invest);}
        return Response.ok().build();
    }

    @GET
    @Path("last_leilao/{empresa}")
    public LeilaoRepresentation lastLeilao(@PathParam("empresa") String e) {
        Leilao l = this.diretorio.lastLeilao(e);

        Map<String, OfertaRepresentation> inv = new HashMap<>();

        for(Map.Entry<String, Oferta> entry: l.getInvestidores().entrySet()){
            inv.put(entry.getKey(), new OfertaRepresentation(entry.getValue().getMontante(), entry.getValue().getTaxa()));
        }

        LeilaoRepresentation lr = new LeilaoRepresentation(l.getEmpresa(), l.getMontante(), l.getTaxaMaxima(), inv);

        return lr;
    }

    @GET
    @Path("last_emprestimo/{empresa}")
    public EmprestimoRepresentation lastEmprestimo(@PathParam("empresa") String e) {
        Emprestimo m = this.diretorio.lastEmprestimo(e);

        EmprestimoRepresentation er = new EmprestimoRepresentation(m.getEmpresa(), m.getMontante(), m.getTaxa(),
                m.getMontanteOferecido(), m.getInvestidores());

        return er;
    }
}

