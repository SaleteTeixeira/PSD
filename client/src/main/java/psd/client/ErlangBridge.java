package psd.client;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

class ErlangBridge {

    private static ErlangBridge instance = null;

    static ErlangBridge getInstance() {
        if (instance == null) {
            try {
                instance = new ErlangBridge();
            } catch (final IOException ex) {
                System.out.println("Could not connect socket. Is the erlang server on?");
                System.exit(1);
            }
        }
        return instance;
    }
    
    static void clean() {
        instance = null;
    }

    private final Socket erlangServer;

    private ErlangBridge() throws IOException {
        this.erlangServer = new Socket("localhost", 11111);
        this.erlangServer.setSoTimeout(1000 * 60);
    }

    private byte[] intToByteArrayBigEndian(final int i) {
        final ByteBuffer b = ByteBuffer.allocate(4);
        b.order(ByteOrder.BIG_ENDIAN);
        b.putInt(i);
        return b.array();
    }

    private int ByteArrayBigEndianToInt(final byte[] bytes) {
        final ByteBuffer b = ByteBuffer.wrap(bytes);
        b.order(ByteOrder.BIG_ENDIAN);
        return b.asIntBuffer().get();
    }

    private void write(final CodedOutputStream cos, final byte[] bytes) throws IOException {
        cos.writeRawBytes(this.intToByteArrayBigEndian(bytes.length));
        cos.writeRawBytes(bytes);
        cos.flush();
    }

    private byte[] read(final CodedInputStream cis) throws IOException {
        final int count = this.ByteArrayBigEndianToInt(cis.readRawBytes(4));
        return cis.readRawBytes(count);
    }

    boolean authenticate(final String username, final String password, final String role) {
        try {
            final CodedInputStream cis = CodedInputStream.newInstance(this.erlangServer.getInputStream());
            final CodedOutputStream cos = CodedOutputStream.newInstance(this.erlangServer.getOutputStream());
            final Messages.LoginRequest login = Messages.LoginRequest.newBuilder().setUsername(username).setPassword(password).setRole(role).build();
            this.write(cos, login.toByteArray());
            final Messages.Reply reply = Messages.Reply.parseFrom(this.read(cis));
            System.out.println(reply.getMessage());
            return reply.getResult();
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(1);
            return false;
        }
    }

    void logout(final String username) {
        try {
            final CodedInputStream cis = CodedInputStream.newInstance(this.erlangServer.getInputStream());
            final CodedOutputStream cos = CodedOutputStream.newInstance(this.erlangServer.getOutputStream());
            final Messages.LogoutRequest logout = Messages.LogoutRequest.newBuilder().setType("LogoutRequest").setUsername(username).build();
            this.write(cos, logout.toByteArray());
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        ErlangBridge.clean();
        
    }

    boolean bidAuction(final String username, final String company, final int amount, final double interest) {
        try {
            final CodedInputStream cis = CodedInputStream.newInstance(this.erlangServer.getInputStream());
            final CodedOutputStream cos = CodedOutputStream.newInstance(this.erlangServer.getOutputStream());
            final Messages.AuctionBid bid = Messages.AuctionBid.newBuilder()
                    .setType("AuctionBid")
                    .setUsername(username)
                    .setCompany(company)
                    .setAmount(amount)
                    .setInterest(interest).build();
            this.write(cos, bid.toByteArray());
            final Messages.Reply reply = Messages.Reply.parseFrom(this.read(cis));
            System.out.println(reply.getMessage());
            return reply.getResult();
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(1);
            return false;
        }
    }

    boolean subscribeFixed(final String username, final String company, final int amount) {
        try {
            final CodedInputStream cis = CodedInputStream.newInstance(this.erlangServer.getInputStream());
            final CodedOutputStream cos = CodedOutputStream.newInstance(this.erlangServer.getOutputStream());
            final Messages.FixedSubscription bid = Messages.FixedSubscription.newBuilder()
                    .setType("FixedSubscription")
                    .setUsername(username)
                    .setCompany(company)
                    .setAmount(amount).build();
            this.write(cos, bid.toByteArray());
            final Messages.Reply reply = Messages.Reply.parseFrom(this.read(cis));
            System.out.println(reply.getMessage());
            return reply.getResult();
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(1);
            return false;
        }
    }
    
    boolean createAuction(final String company, final int amount, final double interest) {
        try {
            final CodedInputStream cis = CodedInputStream.newInstance(this.erlangServer.getInputStream());
            final CodedOutputStream cos = CodedOutputStream.newInstance(this.erlangServer.getOutputStream());
            final Messages.Auction bid = Messages.Auction.newBuilder()
                    .setType("Auction")
                    .setCompany(company)
                    .setAmount(amount)
                    .setInterest(interest).build();
            this.write(cos, bid.toByteArray());
            final Messages.Reply reply = Messages.Reply.parseFrom(this.read(cis));
            System.out.println(reply.getMessage());
            return reply.getResult();
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(1);
            return false;
        }
    }
    
    boolean createLoan(final String company, final int amount, final double interest) {
        try {
            final CodedInputStream cis = CodedInputStream.newInstance(this.erlangServer.getInputStream());
            final CodedOutputStream cos = CodedOutputStream.newInstance(this.erlangServer.getOutputStream());
            final Messages.FixedLoan bid = Messages.FixedLoan.newBuilder()
                    .setType("FixedLoan")
                    .setUsername(company)
                    .setAmount(amount)
                    .setInterest(interest).build();
            this.write(cos, bid.toByteArray());
            final Messages.Reply reply = Messages.Reply.parseFrom(this.read(cis));
            System.out.println(reply.getMessage());
            return reply.getResult();
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(1);
            return false;
        }
    }
    
    Messages.AuctionList auctionList() {
        try {
            final CodedInputStream cis = CodedInputStream.newInstance(this.erlangServer.getInputStream());
            final CodedOutputStream cos = CodedOutputStream.newInstance(this.erlangServer.getOutputStream());
            final Messages.Request req = Messages.Request.newBuilder()
                    .setType("AuctionList").build();
            this.write(cos, req.toByteArray());
            final Messages.AuctionList reply = Messages.AuctionList.parseFrom(this.read(cis));
            return reply;
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
    
    Messages.FixedList fixedList() {
        try {
            final CodedInputStream cis = CodedInputStream.newInstance(this.erlangServer.getInputStream());
            final CodedOutputStream cos = CodedOutputStream.newInstance(this.erlangServer.getOutputStream());
            final Messages.Request req = Messages.Request.newBuilder()
                    .setType("FixedList").build();
            this.write(cos, req.toByteArray());
            final Messages.FixedList reply = Messages.FixedList.parseFrom(this.read(cis));
            return reply;
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
    
    Messages.CompanyList companyList() {
        try {
            final CodedInputStream cis = CodedInputStream.newInstance(this.erlangServer.getInputStream());
            final CodedOutputStream cos = CodedOutputStream.newInstance(this.erlangServer.getOutputStream());
            final Messages.Request req = Messages.Request.newBuilder()
                    .setType("CompanyList").build();
            this.write(cos, req.toByteArray());
            final Messages.CompanyList reply = Messages.CompanyList.parseFrom(this.read(cis));
            return reply;
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
    
    Messages.CompanyInfoReply companyInfo(final String ) {
        
    }
}
