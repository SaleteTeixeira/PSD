import com.google.protobuf.InvalidProtocolBufferException;

public class ThreadReply implements Runnable{

    private final Exchange exchange;
    private final byte[] ba;

    ThreadReply(Exchange exchange, byte[] ba){
        this.exchange = exchange;
        this.ba = ba;
    }

    @Override
    public void run() {
        try {
            Messages.Request m = Messages.Request.parseFrom(ba);

            switch (m.getType()){
                case "AuctionBid":
                    Messages.AuctionBid ab = Messages.AuctionBid.parseFrom(ba);
                    this.exchange.licitar_leilao(ab.getUsername(), ab.getCompany(), ab.getAmount(), ab.getInterest());
                    break;
                case "FixedSubscription":
                    Messages.FixedSubscription fs = Messages.FixedSubscription.parseFrom(ba);
                    this.exchange.subscrever_emprestimo(fs.getUsername(), fs.getCompany(), fs.getAmount());
                    break;
                case "Auction":
                    Messages.Auction a = Messages.Auction.parseFrom(ba);
                    this.exchange.criar_leilao(a.getCompany(), a.getAmount(), a.getInterest());
                    break;
                case "FixedLoan":
                    Messages.FixedLoan fl = Messages.FixedLoan.parseFrom(ba);
                    this.exchange.criar_emprestimo(fl.getUsername(), fl.getAmount(), fl.getInterest());
                    break;
                default:
                    this.exchange.answerToServerRequest(false, "Invalid message.");
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}