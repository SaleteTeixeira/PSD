package psd.client;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

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
        } catch (final IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean logout(final String username) {
        try {
            final CodedInputStream cis = CodedInputStream.newInstance(this.erlangServer.getInputStream());
            final CodedOutputStream cos = CodedOutputStream.newInstance(this.erlangServer.getOutputStream());
            final Messages.LogoutRequest logout = Messages.LogoutRequest.newBuilder().setType("LogoutRequest").setUsername(username).build();
            this.write(cos, logout.toByteArray());
            final Messages.Reply reply = Messages.Reply.parseFrom(this.read(cis));
            System.out.println(reply.getMessage());
            return reply.getResult();
        } catch (final IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean bidAuction(final String company, final int amount, final double interest) {
        try {
            final CodedInputStream cis = CodedInputStream.newInstance(this.erlangServer.getInputStream());
            final CodedOutputStream cos = CodedOutputStream.newInstance(this.erlangServer.getOutputStream());
            final Messages.AuctionBid bid = Messages.AuctionBid.newBuilder().setType("AuctionBid").setCompany(company).setAmount(amount).setInterest(interest).build();
            this.write(cos, bid.toByteArray());
            final Messages.Reply reply = Messages.Reply.parseFrom(this.read(cis));
            System.out.println(reply.getMessage());
            return reply.getResult();
        } catch (final IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean subscribeFixed(final String company, final int amount) {
        try {
            final CodedInputStream cis = CodedInputStream.newInstance(this.erlangServer.getInputStream());
            final CodedOutputStream cos = CodedOutputStream.newInstance(this.erlangServer.getOutputStream());
            final Messages.FixedSubscription bid = Messages.FixedSubscription.newBuilder().setType("FixedSubscription").setCompany(company).setAmount(amount).build();
            this.write(cos, bid.toByteArray());
            final Messages.Reply reply = Messages.Reply.parseFrom(this.read(cis));
            System.out.println(reply.getMessage());
            return reply.getResult();
        } catch (final IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
