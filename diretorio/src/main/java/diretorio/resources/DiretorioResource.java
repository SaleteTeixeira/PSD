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

@Path("diretorio")
public class DiretorioResource {
    private Diretorio diretorio;

    public DiretorioResource(Diretorio diretorio){
        this.diretorio = diretorio;
    }

    @GET
    @Path("/get_empresas")
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
    @Path("/get_emprestimos")
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
    @Path("/get_leiloes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<LeilaoRepresentation> getLeiloes() {
        List<LeilaoRepresentation> result = new ArrayList<>();
        List<Leilao> leiloes = this.diretorio.getLeiloes();

        for(Leilao l: leiloes){
            LeilaoRepresentation lr = getLeilaoRepresentation(l);
            result.add(lr);
        }

        return result;
    }

    @GET
    @Path("/get_empresa/{nome}")
    @Produces(MediaType.APPLICATION_JSON)
    public EmpresaRepresentation getEmpresa(@PathParam("nome") String nome) {
        Empresa emp = this.diretorio.getEmpresa(nome);

        if(emp != null){
            List<EmprestimoRepresentation> histEmp = new ArrayList<>();
            List<LeilaoRepresentation> histLei = new ArrayList<>();

            for(Emprestimo e: emp.getHistoricoEmprestimos()){
                EmprestimoRepresentation auxEmp = new EmprestimoRepresentation(e.getEmpresa(), e.getMontante(), e.getTaxa(),
                        e.getMontanteOferecido(), e.getInvestidores());
                histEmp.add(auxEmp);
            }

            for(Leilao l: emp.getHistoricoLeiloes()){
                LeilaoRepresentation lr = getLeilaoRepresentation(l);
                histLei.add(lr);
            }

            EmpresaRepresentation er = new EmpresaRepresentation(emp.getNome(), histEmp, histLei);
            return er;
        }

        return null;
    }

    @GET
    @Path("/get_emprestimo/{empresa}")
    @Produces(MediaType.APPLICATION_JSON)
    public EmprestimoRepresentation getEmprestimo(@PathParam("empresa") String empresa) {
        Emprestimo e = this.diretorio.getEmprestimo(empresa);

        if(e != null){
            EmprestimoRepresentation er = new EmprestimoRepresentation(e.getEmpresa(), e.getMontante(), e.getTaxa(),
                    e.getMontanteOferecido(), e.getInvestidores());
            return er;
        }

        return null;
    }

    @GET
    @Path("/get_leilao/{empresa}")
    @Produces(MediaType.APPLICATION_JSON)
    public LeilaoRepresentation getLeilao(@PathParam("empresa") String empresa) {
        Leilao l = this.diretorio.getLeilao(empresa);

        if (l != null) {
            LeilaoRepresentation lr = getLeilaoRepresentation(l);
            if (lr != null) return lr;
        }

        return null;
    }


    @GET
    @Path("/last_leilao/{empresa}")
    @Produces(MediaType.APPLICATION_JSON)
    public LeilaoRepresentation lastLeilao(@PathParam("empresa") String e) {
        Leilao l = this.diretorio.lastLeilao(e);

        if (l != null) {
            LeilaoRepresentation leilaoR = getLeilaoRepresentation(l);
            if (leilaoR != null) return leilaoR;
        }

        return null;
    }

    @GET
    @Path("/last_emprestimo/{empresa}")
    @Produces(MediaType.APPLICATION_JSON)
    public EmprestimoRepresentation lastEmprestimo(@PathParam("empresa") String e) {
        Emprestimo m = this.diretorio.lastEmprestimo(e);

        if(m != null){
            EmprestimoRepresentation er = new EmprestimoRepresentation(m.getEmpresa(), m.getMontante(), m.getTaxa(),
                    m.getMontanteOferecido(), m.getInvestidores());

            return er;
        }

        return null;
    }

    @PUT
    @Path("/add_emprestimo/{nome}/{valor}_{taxa}")
    public Response putEmprestimo(@PathParam("nome") String n, @PathParam("valor") double v, @PathParam("taxa") double t) {
        Emprestimo e = new Emprestimo(n, v, t);
        synchronized (this) {this.diretorio.addEmprestimo(e);}
        return Response.ok().build();
    }

    @PUT
    @Path("/add_leilao/{nome}/{valor}_{taxa}")
    public Response putLeilao(@PathParam("nome") String n, @PathParam("valor") double v, @PathParam("taxa") double t) {
        Leilao l = new Leilao(n, v, t);
        synchronized (this) {this.diretorio.addLeilao(l);}
        return Response.ok().build();
    }

    @POST
    @Path("/add_investidor_emprestimo/{empresa}/{investidor}/{valor}")
    public Response addInvestidorEmprestimo(@PathParam("empresa") String e, @PathParam("investidor") String n, @PathParam("valor") double v) {
        synchronized (this) {this.diretorio.addInvestidorEmprestimo(e, n, v);}
        return Response.ok().build();
    }

    @POST
    @Path("/add_investidor_leilao/{empresa}/{investidor}/{valor}_{taxa}")
    public Response addInvestidorLeilao(@PathParam("empresa") String e, @PathParam("investidor") String n, @PathParam("valor") double v, @PathParam("taxa") double t) {
        synchronized (this) {this.diretorio.addInvestidorLeilao(e, n, v, t);}
        return Response.ok().build();
    }

    @POST
    @Path("/end_emprestimo/{empresa}")
    public Response endEmprestimo(@PathParam("empresa") String e) {
        synchronized (this) {this.diretorio.endEmprestimo(e);}
        return Response.ok().build();
    }

    @POST
    @Path("/end_leilao/{empresa}/investidores")
    public Response endLeilao(@PathParam("empresa") String e, @QueryParam("inv") List<String> inv,
                              @QueryParam("m") List<Double> m, @QueryParam("t") List<Double> t) {
        Map<String, Oferta> invest = new HashMap<>();

        for(int i=0; i<inv.size() && i<m.size() && i<t.size(); i++){
            Oferta o = new Oferta(m.get(i), t.get(i));
            invest.put(inv.get(i), o);
        }

        synchronized (this) {this.diretorio.endLeilao(e, invest);}
        return Response.ok().build();
    }

    private LeilaoRepresentation getLeilaoRepresentation(Leilao l) {
        if(l != null){
            Map<String, OfertaRepresentation> inv = new HashMap<>();

            for(Map.Entry<String, Oferta> entry: l.getInvestidores().entrySet()){
                inv.put(entry.getKey(), new OfertaRepresentation(entry.getValue().getMontante(), entry.getValue().getTaxa()));
            }

            LeilaoRepresentation lr = new LeilaoRepresentation(l.getEmpresa(), l.getMontante(), l.getTaxaMaxima(), inv);

            return lr;
        }
        return null;
    }
}

