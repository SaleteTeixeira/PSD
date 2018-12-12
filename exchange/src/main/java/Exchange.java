
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Exchange {

    public boolean criar_leilao(String empresa, float montante, float taxaMaxima){
        //if(está no diretorio leilao ativo com essa empresa) return false; (mandar msg mais especifica ao servidor)
        //if(montante n for multiplo de 1000) return false; (mandar msg mais especifica ao servidor)
        //else
            //criar no map (empresa X, montante X, taxaMaxima X, Map vazio de investidores)
            //enviar para o diretorio
            //iniciar thread a contar tempo do leilao para o fechar ao fim de X tempo
                // qd terminal , eliminar do map
                //na thread : se terminar e montante da empresa não tiver sido atingido pelos investidores -> LEILAO FALHA (colocar no diretorio com Map vazio)
                //na thread : se terminar e montante atingido -> melhores ofertas (taxas de juro mais baixas) sao alocadas -> LEILAO SUCESSO (colocar no diretorio com Map preenchido)
                //na thread : informar investidores e empresa de fim de leilao (msg para servidor)
            return true;
    }

    public boolean licitar_leilao(String investidor, String empresa, float montante, float taxa){
        //if(n está no diretorio leilao ativo com essa empresa) return false; (mandar msg mais especifica ao servidor)
        //if(montante n for multiplo de 100) return false; (mandar msg mais especifica ao servidor)
        //else
            //ir buscar leilao ativo da empresa X e add string investidor com X oferta ao MAP desse leilao
            return true;
    }

    public boolean criar_emprestimo(String empresa, float montante, float taxa){
        //if (ja tem emissao a decorrer) return false (mandar msg mais especifica ao servidor)
        //if (leilao com sucesso ja feito)
            //ver qual o mais recente leilao acabado e a sua taxa mais alta alocada dos investidores
            //ver se o mais recente emprestimo o montante desejado foi alcançado
            //if (yes && taxa >= taxaLeilao && montante multiplo de 1000) E if(no && taxa == tavaLeilao*1.1 && montante multiplo de 1000)
                //criar entrada no map emprestimo (empresa, montante, taxa)
                //enviar para o diretorio
                //criar thread com X tempo
                    //qd terminar, eliminar do map
                    //na thread : se for atingido valor antes de X -> terminar
                    //na thread : se tempo acabar e montante de investidores nao atingir montante da empresa -> ENVIAR AO DIRETORIO MAP DOS INVESTIDORES
                    //na thread : terminar -> informar investidores e empresa de fim de leilao (msg para servidor)
                return true;
        //return false; (mandar msg mais especifica ao servidor)
    }

    public boolean subscrever_emprestimo(String investido, String empresa, float montante){
        //if(n está no diretorio emprestimo ativo com essa empresa) return false; (mandar msg mais especifica ao servidor)
        //if(montante n for multiplo de 100) return false; (mandar msg mais especifica ao servidor)
        //else
            //ir buscar emprestimo ativo da empresa X e add string investidor com X montante ao MAP do emprestimo
        return true;
    }


    public List<Leilao> leiloes_atuais(){
        List<Leilao> leiloes = new ArrayList<>();
        String s = sendGet("http://localhost:8080/diretorio/get_leiloes");
        String aux;

        /*TODO 3. para cada entrada aux na string s:*/
            Leilao l = parseLeilao(aux);
            leiloes.add(l);

        return leiloes;
    }

    public Empresa info_emp(String empresa){
        return parseEmpresa(sendGet("http://localhost:8080/diretorio/get_empresa/"+empresa));
    }

    public void his_emp(String empresa){
        //pedir ao diretorio
        //mandar ao servidor
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

    public Emprestimo parseEmprestimo(String result){
        /*TODO 4. processar string para criar emprestimo*/

        Emprestimo emp = new Emprestimo(...);
        return emp;
    }

    public Leilao parseLeilao(String result){
        /*TODO 5. processar string para criar leilao*/
        Leilao l = new Leilao(...);

        return l;
    }

    public Empresa parseEmpresa(String result){
        /*TODO 6. processar string para criar empresa*/
        Empresa emp = new Empresa(...);

        return emp;
    }

    public static void main(String args[]) throws Exception {
        /*TODO 1. protocol bufffers*/
        /*TODO 2. implementar subscrição pelo ZEROMQ*/

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
