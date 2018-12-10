package psd.client;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ErlangBridge {

    private static ErlangBridge instance = null;

    public static ErlangBridge getInstance() {
        if (instance == null) {
            try {
                instance = new ErlangBridge();
            } catch (IOException ex) {
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
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(i);
        byte[] bytes = b.array();
        return bytes;
    }

    public boolean authenticate(String username, String password, String role) {
        try {
            CodedInputStream cis = CodedInputStream.newInstance(this.erlangServer.getInputStream());
            CodedOutputStream cos = CodedOutputStream.newInstance(this.erlangServer.getOutputStream());
            Messages.Login login = Messages.Login.newBuilder().setUsername(username).setPassword(password).build();
            /*login.writeDelimitedTo(this.erlangServer.getOutputStream());
            return false;*/
            byte[] bytes = login.toByteArray();
            byte[] delim = intToByteArrayBigEndian(bytes.length);
            System.out.println("Writing login array length: " + bytes.length);
            cos.writeRawBytes(delim);
            System.out.println("Writing login bytes");
            cos.writeRawBytes(bytes);
            cos.flush();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        /*Messages.Login login = Messages.Login.newBuilder().setUsername(username).setPassword(password).build();
        byte[] ba = login.toByteArray();
        try {

            login.writeDelimitedTo(this.erlangServer.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        try {
            Messages.LoginResponse response = Messages.LoginResponse.parseDelimitedFrom(this.erlangServer.getInputStream());
            System.out.println(response.getMessage());
            return response.getResult();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }*/
    }

    public boolean logout(String username) {
        Messages.Logout logout = Messages.Logout.newBuilder().setUsername(username).build();
        try {
            logout.writeDelimitedTo(this.erlangServer.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        try {
            Messages.LogoutResponse response = Messages.LogoutResponse.parseDelimitedFrom(this.erlangServer.getInputStream());
            System.out.println(response.getMessage());
            return response.getResult();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
