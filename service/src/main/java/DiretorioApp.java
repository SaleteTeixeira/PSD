import resources.*;
import business.*;
import health.*;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class DiretorioApp extends Application<DiretorioConfiguration> {
    public static void main(String[] args) throws Exception {
        new DiretorioApp().run(args);
    }

    @Override
    public void initialize(Bootstrap<DiretorioConfiguration> bootstrap) { }

    @Override
    public void run(DiretorioConfiguration configuration, Environment environment) {
        environment.jersey().register(
                new DiretorioResource(Diretorio.getInstance()));
        environment.healthChecks().register("template",
                new DiretorioHealthCheck(configuration.getVersion(), Diretorio.getInstance()));
    }
}