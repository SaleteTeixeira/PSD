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
            final Auth.Request login = Auth.Request.newBuilder().setType("Login").setUsername(username).setPassword(password).build();
            final byte[] bytes = login.toByteArray();
            this.write(cos, login.toByteArray());
            final Auth.Reply reply = Auth.Reply.parseFrom(this.read(cis));
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
            final Auth.Request logout = Auth.Request.newBuilder().setType("Logout").setUsername(username).build();
            final byte[] bytes = logout.toByteArray();
            this.write(cos, logout.toByteArray());
            final Auth.Reply reply = Auth.Reply.parseFrom(this.read(cis));
            System.out.println(reply.getMessage());
            return reply.getResult();
        } catch (final IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
