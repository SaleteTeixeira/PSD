
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Exchange {

    public boolean criar_leilao(String empresa, double montante, double taxaMaxima){
        if((montante % 1000) != 0) return false;

        Leilao l = parseLeilao(sendGet("http://localhost:8080/diretorio/get_leilao/"+empresa));
        if(l != null) return false;

        String result = sendPut("http://localhost:8080/diretorio/add_leilao/"+empresa+"/"+montante+"_"+taxaMaxima);
        if(result.equals("ERROR")) return false;

        Thread t = new Thread(new LeilaoTimer(this, l.getEmpresa()));

        return true;
    }

    public boolean licitar_leilao(String investidor, String empresa, double montante, double taxa){
        //if(n está no diretorio leilao ativo com essa empresa) return false; (mandar msg mais especifica ao servidor)
        //if(montante n for multiplo de 100) return false; (mandar msg mais especifica ao servidor)
        //else
            //ir buscar leilao ativo da empresa X e add string investidor com X oferta ao MAP desse leilao
            return true;
    }

    public boolean end_leilao(String empresa){
        //se terminar e montante da empresa não tiver sido atingido pelos investidores -> LEILAO FALHA (colocar no diretorio com Map vazio)
        //se terminar e montante atingido -> melhores ofertas (taxas de juro mais baixas) sao alocadas -> LEILAO SUCESSO (colocar no diretorio com Map preenchido)
        //informar investidores e empresa de fim de leilao (msg para servidor)
        return true;
    }

    public boolean criar_emprestimo(String empresa, double montante, double taxa){
        if((montante % 1000) != 0) return false;

        Emprestimo emp = parseEmprestimo("http://localhost:8080/diretorio/get_emprestimo/"+empresa);
        if(emp != null) return false;

        Leilao l = parseLeilao(sendGet("http://localhost:8080/diretorio/last_leilao/"+empresa));
        if(l == null) return false;

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
            if(result.equals("ERROR")) return false;

            Thread t = new Thread(new EmprestimoTimer(this, empresa));

            return true;
        }

        return false;
    }

    public boolean subscrever_emprestimo(String investido, String empresa, double montante){
        //if(n está no diretorio emprestimo ativo com essa empresa) return false; (mandar msg mais especifica ao servidor)
        //if(montante n for multiplo de 100) return false; (mandar msg mais especifica ao servidor)
        //else
            //ir buscar emprestimo ativo da empresa X e add string investidor com X montante ao MAP do emprestimo
        return true;
    }

    public boolean end_emprestimo(String empresa){
        //se for atingido valor antes de X -> terminar      -- VER ONDE INCORPORAR ISTO, TALVEZ NO subscrever_emprestimo
        //verificar se emprestimo ainda está ativo, porque pode ter acabado e thread ainda estar a correr e depois voltar a chamar este metodo e eliminar o emprestimo errado
        //se tempo acabar e montante de investidores nao atingir montante da empresa -> ENVIAR AO DIRETORIO MAP DOS INVESTIDORES
        //terminar -> informar investidores e empresa de fim de leilao (msg para servidor)
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

            if (responseCode == HttpURLConnection.HTTP_OK) {
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

    public Emprestimo parseEmprestimo(String result){
        Emprestimo emp = null;

        if(!result.equals("ERROR")){
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

        if(!result.equals("ERROR")){
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

        if(!result.equals("ERROR")){
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

    public static void main(String args[]) throws Exception {
        Exchange exchange = new Exchange();

        /*TODO 1. protocol bufffers*/
        /*TODO 2. implementar subscrição pelo ZEROMQ*/
        /*TODO 3. logica negocio (metodos acima)*/

        /*Socket s = new Socket("127.0.0.1", 12345);
        CodedInputStream cis = CodedInputStream.newInstance(s.getInputStream());
        //output -> rest para diretorio
        new Thread(new ClientWorker(s)).start();

        while(true){
            int len = cis.readRawLittleEndian32();
            byte[] ba = cis.readRawBytes(len);
            Protos.Mensagem m = Protos.Mensagem.parseFrom(ba);

            //if(m leiloes)
            // pedir info do leilão ao diretorio
            // chamar função dos leiloes

            //if(m emissoes)
            // chamar função dos leiloes

            out.write(m.getMsg());
            out.flush();
        }

        String line;

        while((line = in.readLine()) != null){
            Protos.Mensagem m = Protos.Mensagem.newBuilder().setMsg(line).build();
            byte[] ba = m.toByteArray();
            cos.writeFixed32NoTag(ba.length);
            cos.writeRawBytes(ba);
            cos.flush();
        }

        in.close();*/
    }
}
