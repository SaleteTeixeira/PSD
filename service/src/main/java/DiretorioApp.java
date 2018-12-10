import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class DiretorioApp extends Application<RestStubConfiguration> {

    public static void main(String[] args) throws Exception {
        new DiretorioApp().run(args);
    }

    @Override
    public void run(RestStubConfiguration config, Environment env) {
        final DiretorioService personService = new DiretorioService(Diretorio.getInstance());
        env.jersey().register(personService);

        env.healthChecks().register("template",
                new RestStubCheck(config.getVersion(), Diretorio.getInstance()));
    }
}