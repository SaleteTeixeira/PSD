package health;

import business.Diretorio;
import com.codahale.metrics.health.HealthCheck;

public class DiretorioHealthCheck extends HealthCheck {
    private final String version;
    private Diretorio diretorio;

    public DiretorioHealthCheck(String version, Diretorio diretorio) {
        this.version = version;
        this.diretorio = diretorio;
    }

    @Override
    protected Result check() throws Exception {
        if (this.diretorio.getEmpresas().size() != 5) {
            return Result.unhealthy("Nenhuma empresa no diretorio! Version: " +
                    this.version);
        }
        return Result.healthy("OK with version: " + this.version +
                ". Número de empresas: " + this.diretorio.getEmpresas().size());
    }
}
