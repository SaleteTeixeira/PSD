import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

import java.io.IOException;
import java.net.Socket;

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

    @Override
    public void run()  {
        try {
            while (true) {
                int len = this.cis.readRawLittleEndian32();
                byte[] ba = this.cis.readRawBytes(len);

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
                    default:
                        this.exchange.answerToServerRequest(this.cos,false, "Invalid message.");
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected.");
        }
    }
}