package diretorio.resources;

import diretorio.business.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public List<Empresa> getEmpresas() {
        return this.diretorio.getEmpresas();
    }

    @GET
    @Path("get_emprestimos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Emprestimo> getEmprestimos() {
        return this.diretorio.getEmprestimos();
    }

    @GET
    @Path("get_leiloes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Leilao> getLeiloes() {
        return this.diretorio.getLeiloes();
    }

    @GET
    @Path("get_empresa/{nome}")
    @Produces(MediaType.APPLICATION_JSON)
    public Empresa getEmpresa(@PathParam("nome") String nome) {
        return this.diretorio.getEmpresa(nome);
    }

    @GET
    @Path("get_emprestimo/{empresa}")
    @Produces(MediaType.APPLICATION_JSON)
    public Emprestimo getEmprestimo(@PathParam("empresa") String empresa) {
        return this.diretorio.getEmprestimo(empresa);
    }

    @GET
    @Path("get_leilao/{empresa}")
    @Produces(MediaType.APPLICATION_JSON)
    public Leilao getLeilao(@PathParam("empresa") String empresa) {
        return this.diretorio.getLeilao(empresa);
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
    public Response endLeilao(@PathParam("empresa") String e, @PathParam("inv") List<String> inv, @PathParam("montante") List<Float> m, @PathParam("taxa") List<Float> t) {
        Map<String, Oferta> invest = new HashMap<>();

        for(int i=0; i<inv.size(); i++){
            Oferta o = new Oferta(m.get(i), t.get(i));
            invest.put(inv.get(i), o);
        }

        synchronized (this) {this.diretorio.endLeilao(e, invest);}
        return Response.ok().build();
    }

    @POST
    @Path("last_leilao/{empresa}")
    public Leilao lastLeilao(@PathParam("empresa") String e) {
        return this.diretorio.lastLeilao(e);
    }

    @POST
    @Path("last_emprestimo/{empresa}")
    public Emprestimo lastEmprestimo(@PathParam("empresa") String e) {
        return this.diretorio.lastEmprestimo(e);
    }
}

