package psd.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

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

    public boolean authenticate(String username, String password, String role) {
        Messages.Login login = Messages.Login.newBuilder().setUsername(username).setPassword(password).build();
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
        }
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
