import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Exchange {

    private final CodedOutputStream cos;
    private Map<String,Thread> threadEmprestimo;
    private ReentrantLock lock;

    public Exchange(CodedOutputStream cos){
        this.cos = cos;
        this.threadEmprestimo = new HashMap<>();
        this.lock = new ReentrantLock();
    }

    public boolean criar_leilao(String empresa, double montante, double taxaMaxima) {
        if((montante % 1000) != 0) {
            answerToServerRequest(false,"Failure: your amount is not multiple of 1000.");
            return false;
        }

        Leilao l = parseLeilao(sendGet("http://localhost:8080/diretorio/get_leilao/"+empresa));
        if(l != null) {
            answerToServerRequest(false,"Failure: your company already has an active auction.");
            return false;
        }

        String result = sendPut("http://localhost:8080/diretorio/add_leilao/"+empresa+"/"+montante+"_"+taxaMaxima);
        if(result.equals("ERROR")) {
            answerToServerRequest(false,"Failure: error creating the auction.");
            return false;
        }

        new Thread(new LeilaoTimer(this, empresa));
        answerToServerRequest(true,"Success: auction created with success.");
        return true;
    }

    public boolean licitar_leilao(String investidor, String empresa, double montante, double taxa){
        if((montante % 100) != 0) {
            answerToServerRequest(false,"Failure: your amount is not multiple of 100.");
            return false;
        }

        Leilao l = parseLeilao(sendGet("http://localhost:8080/diretorio/get_leilao/"+empresa));
        if(l == null) {
            answerToServerRequest(false,"Failure: this company does not have an active auction.");
            return false;
        }
        if (taxa > l.getTaxaMaxima()) {
            answerToServerRequest(false,"Failure: your interest is higher than the allowed in the auction ("+l.getTaxaMaxima()+").");
            return false;
        }

        String result = sendPost("http://localhost:8080/diretorio/add_investidor_leilao/"+empresa+"/"+investidor+"/"+montante+"_"+taxa);
        if(result.equals("ERROR")) {
            answerToServerRequest(false,"Failure: error adding you to the auction.");
            return false;
        }

        answerToServerRequest(true,"Success: you were added to the auction.");
        return true;
    }

    public boolean end_leilao(String empresa){
        List<String> investidores = new ArrayList<>();
        List<Double> montantes = new ArrayList<>();
        List<Double> taxas = new ArrayList<>();

        Leilao l = parseLeilao(sendGet("http://localhost:8080/diretorio/get_leilao/"+empresa));

        double montanteAtingido = 0;
        for(Oferta o: l.getInvestidores().values()){
            montanteAtingido += o.getMontante();
        }

        if(montanteAtingido >= l.getMontante()){
            Map<String,Oferta> aux = melhores_ofertas(l.getInvestidores(), l.getMontante(), l.getTaxaMaxima());

            for(Map.Entry<String,Oferta> tmp: aux.entrySet()){
                investidores.add(tmp.getKey());
                montantes.add(tmp.getValue().getMontante());
                taxas.add(tmp.getValue().getTaxa());
            }
        }

        String url = "http://localhost:8080/diretorio/end_leilao/"+empresa+"/investidores?";
        for(int i=0; i<investidores.size(); i++){
            url = url+"inv="+investidores.get(i)+"&m="+montantes.get(i)+"&t="+taxas.get(i);
            if(i<investidores.size()-1) url = url+"&";
        }

        String result = sendPost(url);
        if(result.equals("ERROR")) return false;

        //informar investidores e empresa de fim de leilao (msg para servidor) ZeroMQ

        return true;
    }

    private Map<String,Oferta> melhores_ofertas(Map<String,Oferta> investidores, double montante, double taxaMaxima) {
        Map<String,Oferta> result = new HashMap<>();
        double acumulado = 0, taxa = taxaMaxima, atual=0;
        String nome = "";

        while(acumulado < montante && investidores.size()>0){
            for(Map.Entry<String,Oferta> tmp: investidores.entrySet()){
                if(tmp.getValue().getTaxa() < taxa){
                    taxa = tmp.getValue().getTaxa();
                    atual = tmp.getValue().getMontante();
                    nome = tmp.getKey();
                }
                else if(tmp.getValue().getTaxa() == taxa){
                    if(tmp.getValue().getMontante() > atual){
                        atual = tmp.getValue().getMontante();
                        nome = tmp.getKey();
                    }
                }
            }

            if(!nome.equals("")){
                result.put(nome,investidores.get(nome));
                acumulado += investidores.get(nome).getMontante();
                investidores.remove(nome);
                nome = "";
                taxa = taxaMaxima;
                atual = 0;
            }
        }

        return result;
    }

    public boolean criar_emprestimo(String empresa, double montante, double taxa){
        if((montante % 1000) != 0) {
            answerToServerRequest(false, "Failure: your amount is not multiple of 1000.");
            return false;
        }

        Emprestimo emp = parseEmprestimo(sendGet("http://localhost:8080/diretorio/get_emprestimo/"+empresa));
        if(emp != null) {
            answerToServerRequest(false,"Failure: your company already has an active loan.");
            return false;
        }

        Leilao l = parseLeilao(sendGet("http://localhost:8080/diretorio/last_leilao/"+empresa));
        if(l == null) {
            answerToServerRequest(false,"Failure: your company does not have a successful auction.");
            return false;
        }

        Map<String,Oferta> inv = l.getInvestidores();
        double maiorTaxa = 0;
        for(Oferta o: inv.values()){
            if (o.getTaxa() > maiorTaxa) maiorTaxa = o.getTaxa();
        }

        boolean desejado = true;
        Emprestimo last = parseEmprestimo(sendGet("http://localhost:8080/diretorio/last_emprestimo/"+empresa));
        if(last != null){
            if(last.getMontanteOferecido() < last.getMontante()) desejado = false;
        }

        if((desejado && (taxa >= maiorTaxa)) || (!desejado && (taxa == (1.1 * last.getTaxa())))){
            String result = sendPut("http://localhost:8080/diretorio/add_emprestimo/"+empresa+"/"+montante+"_"+taxa);
            if(result.equals("ERROR")) {
                answerToServerRequest(false,"Failure: error creating the loan.");
                return false;
            }

            Thread t = new Thread(new EmprestimoTimer(this, empresa));

            this.lock.lock();
            this.threadEmprestimo.put(empresa,t);
            this.lock.unlock();

            answerToServerRequest(true,"Success: loan created with success.");
            return true;
        }

        answerToServerRequest(false,"Failure: your interest does not correspond to the requirements, compared to the last loan.");
        return false;
    }

    public boolean subscrever_emprestimo(String investidor, String empresa, double montante){
        if((montante % 100) != 0) {
            answerToServerRequest(false, "Failure: your amount is not multiple of 100.");
            return false;
        }

        Emprestimo e = parseEmprestimo(sendGet("http://localhost:8080/diretorio/get_emprestimo/"+empresa));
        if(e == null) {
            answerToServerRequest(false,"Failure: this company does not have an active fixed loan.");
            return false;
        }

        String result = sendPost("http://localhost:8080/diretorio/add_investidor_emprestimo/"+empresa+"/"+investidor+"/"+montante);
        Emprestimo emp = parseEmprestimo(sendGet("http://localhost:8080/diretorio/get_emprestimo/"+empresa));

        if(emp.getMontanteOferecido() >= emp.getMontante()){
            this.lock.lock();
            if(this.threadEmprestimo.containsKey(empresa)){
                this.threadEmprestimo.get(empresa).interrupt();
                this.threadEmprestimo.remove(empresa);
            }
            this.lock.unlock();
            end_emprestimo(empresa);
        }

        if(result.equals("ERROR")) {
            answerToServerRequest(false,"Failure: error adding your subscription to the loan.");
            return false;
        }

        answerToServerRequest(true,"Success: your subscription was added to the loan.");
        return true;
    }

    public boolean end_emprestimo(String empresa){
        String result = sendPost("http://localhost:8080/diretorio/end_emprestimo/"+empresa);
        if(result.equals("ERROR")) return false;

        //informar investidores e empresa de fim de leilao (msg para servidor) ZeroMQ

        return true;
    }

    public List<Emprestimo> emprestimos_atuais(){
        String s = sendGet("http://localhost:8080/diretorio/get_emprestimos");
        List<Emprestimo> emprestimos = parseEmprestimos(s);
        return emprestimos;
    }

    public List<Leilao> leiloes_atuais(){
        String s = sendGet("http://localhost:8080/diretorio/get_leiloes");
        List<Leilao> leiloes = parseLeiloes(s);
        return leiloes;
    }

    public Empresa info_emp(String empresa){
        return parseEmpresa(sendGet("http://localhost:8080/diretorio/get_empresa/"+empresa));
    }

    public List<String> empresas(){
        List<Empresa> empresas = parseEmpresas(sendGet("http://localhost:8080/diretorio/get_empresas/"));
        List<String> nomes = new ArrayList<>();

        for(Empresa emp: empresas){
            nomes.add(emp.getNome());
        }

        return nomes;
    }

    public String sendGet(String url){
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                return response.toString();
            }
            else if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) return null;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "ERROR";
    }

    public String sendPost(String url){
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return "OK";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "ERROR";
    }

    public String sendPut(String url){
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("PUT");
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                return "OK";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    private List<Emprestimo> parseEmprestimos(String s) {
        List<Emprestimo> emprestimos = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();
            JSONArray json = (JSONArray) parser.parse(s);
            Iterator i = json.iterator();

            while(i.hasNext()){
                Emprestimo emp = parseEmprestimo(i.next().toString());
                emprestimos.add(emp);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return emprestimos;
    }

    private List<Leilao> parseLeiloes(String s) {
        List<Leilao> leiloes = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();
            JSONArray json = (JSONArray) parser.parse(s);
            Iterator i = json.iterator();

            while(i.hasNext()){
                Leilao l = parseLeilao(i.next().toString());
                leiloes.add(l);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return leiloes;
    }

    private List<Empresa> parseEmpresas(String s) {
        List<Empresa> empresas = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();
            JSONArray json = (JSONArray) parser.parse(s);
            Iterator i = json.iterator();

            while(i.hasNext()){
                Empresa emp = parseEmpresa(i.next().toString());
                empresas.add(emp);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return empresas;
    }

    public Emprestimo parseEmprestimo(String result){
        Emprestimo emp = null;

        if(result != null && !result.equals("ERROR")){
            try {
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(result);

                String empresa = (String) json.get("empresa");
                double montante = (double) json.get("montante");
                double taxa = (double) json.get("taxa");
                double montanteOfer = (double) json.get("montanteOferecido");
                Map<String, Double> investidores = (Map<String, Double>) json.get("investidores");

                emp = new Emprestimo(empresa, montante, taxa, montanteOfer, investidores);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return emp;
    }

    public Leilao parseLeilao(String result){
        Leilao l = null;

        if(result != null && !result.equals("ERROR")){
            try {
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(result);

                String empresa = (String) json.get("empresa");
                double montante = (double) json.get("montante");
                double taxaMaxima = (double) json.get("taxaMaxima");
                Map<String, Oferta> investidores = new HashMap<>();

                JSONObject jsonM = (JSONObject) json.get("investidores");
                Iterator<?> i = (Iterator<String>) jsonM.keySet().iterator();

                while(i.hasNext()){
                    String key = (String) i.next();
                    JSONObject value = (JSONObject) jsonM.get(key);

                    double m = (double) value.get("montante");
                    double t = (double) value.get("taxa");
                    Oferta oferta = new Oferta(m,t);

                    investidores.put(key,oferta);
                }

                l = new Leilao(empresa, montante, taxaMaxima, investidores);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return l;
    }

    public Empresa parseEmpresa(String result){
        Empresa emp = null;

        if(result != null && !result.equals("ERROR")){
            try {
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(result);

                String nome = (String) json.get("nome");
                List<Emprestimo> historicoEmprestimos = parseEmprestimos(json.get("historicoEmprestimos").toString());
                List<Leilao> historicoLeiloes = parseLeiloes(json.get("historicoLeiloes").toString());

                emp = new Empresa(nome, historicoEmprestimos, historicoLeiloes);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return emp;
    }

    void answerToServerRequest(boolean bool, String msg) {
        try {
            Messages.Reply m = Messages.Reply.newBuilder().setResult(bool).setMessage(msg).build();

            byte ba[] = m.toByteArray();
            this.cos.writeFixed32NoTag(ba.length);
            this.cos.writeRawBytes(ba);
            this.cos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws Exception {

        /*TODO 1. implementar subscrição pelo ZEROMQ -> end_emprestimo e end_leilao */
        /*TODO 2. NÃO TESTEI AS THREADS DO TEMPO A FUNCIONAR, ERA COMPLICADO SEM O RESTO */

        Socket s = new Socket(InetAddress.getLocalHost(), 11111, InetAddress.getLocalHost(), Integer.parseInt(args[0]));
        CodedInputStream cis = CodedInputStream.newInstance(s.getInputStream());
        CodedOutputStream cos = CodedOutputStream.newInstance(s.getOutputStream());

        Exchange exchange = new Exchange(cos);

        while (true) {
            int len = cis.readRawLittleEndian32();
            byte[] ba = cis.readRawBytes(len);

            new Thread(new ThreadReply(exchange, ba)).start();
        }
    }
}
