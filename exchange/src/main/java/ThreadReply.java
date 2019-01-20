import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ThreadReply implements Runnable{

    private final Socket cliS;
    private final CodedInputStream cis;
    private final CodedOutputStream cos;
    private final Exchange exchange;

    ThreadReply(Socket cliS, Exchange exchange) throws IOException {
        this.cliS = cliS;
        this.cis = CodedInputStream.newInstance(cliS.getInputStream());
        this.cos = CodedOutputStream.newInstance(cliS.getOutputStream());
        this.exchange = exchange;
    }

    private int ByteArrayBigEndianToInt(final byte[] bytes) {
        final ByteBuffer b = ByteBuffer.wrap(bytes);
        b.order(ByteOrder.BIG_ENDIAN);
        return b.asIntBuffer().get();
    }

    @Override
    public void run()  {
        try {
            while (true) {
                final int count = this.ByteArrayBigEndianToInt(cis.readRawBytes(4));
                byte[] ba = this.cis.readRawBytes(count);

                Messages.Request m = Messages.Request.parseFrom(ba);

                switch (m.getType()){
                    case "AuctionBid":
                        Messages.AuctionBid ab = Messages.AuctionBid.parseFrom(ba);
                        this.exchange.licitar_leilao(this.cos, ab.getUsername(), ab.getCompany(), ab.getAmount(), ab.getInterest());
                        break;
                    case "FixedSubscription":
                        Messages.FixedSubscription fs = Messages.FixedSubscription.parseFrom(ba);
                        this.exchange.subscrever_emprestimo(this.cos, fs.getUsername(), fs.getCompany(), fs.getAmount());
                        break;
                    case "Auction":
                        Messages.Auction a = Messages.Auction.parseFrom(ba);
                        this.exchange.criar_leilao(this.cos, a.getCompany(), a.getAmount(), a.getInterest());
                        break;
                    case "FixedLoan":
                        Messages.FixedLoan fl = Messages.FixedLoan.parseFrom(ba);
                        this.exchange.criar_emprestimo(this.cos, fl.getUsername(), fl.getAmount(), fl.getInterest());
                        break;
                    case "AuctionList":
                        this.exchange.leiloes_atuais(this.cos);
                        break;
                    case "FixedList":
                        this.exchange.emprestimos_atuais(this.cos);
                        break;
                    case "CompanyInfoRequest":
                        Messages.CompanyInfoRequest cir = Messages.CompanyInfoRequest.parseFrom(ba);
                        this.exchange.info_emp(this.cos, cir.getCompany());
                        break;
                    default:
                        this.exchange.answerToServerRequest(this.cos,false, "Invalid message.");
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected.");
        }
    }
}