import static java.lang.Thread.sleep;

public class LeilaoTimer implements Runnable {
    private Exchange exchange;
    private String empresa;

    public LeilaoTimer(Exchange e, String emp) {
        this.exchange = e;
        this.empresa = emp;
    }

    public void run(){
        try {
            sleep(60000);

            this.exchange.end_leilao(this.empresa);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
