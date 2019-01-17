import static java.lang.Thread.sleep;

public class EmprestimoTimer implements Runnable {
    private Exchange exchange;
    private String empresa;

    public EmprestimoTimer(Exchange exchange, String empresa) {
        this.exchange = exchange;
        this.empresa = empresa;
    }

    public void run(){
        try {
            sleep(30000);
            exchange.end_emprestimo(empresa);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
