import java.net.Socket;
import java.util.Map;

public class Exchange {

    private Map<String, Emprestimo> emprestimos;
    private Map<String, Leilao> leiloes;

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


    public void leiloes_atuais(String empresa){
        //mandar ao servidor o map de leiloes
    }

    public void info_emp(String empresa){
        //pedir ao diretorio
        //mandar ao servidor
    }

    public void his_emp(String empresa){
        //pedir ao diretorio
        //mandar ao servidor
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
