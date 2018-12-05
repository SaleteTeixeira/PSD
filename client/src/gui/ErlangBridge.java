package gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ErlangBridge {

    private static ErlangBridge instance = null;

    public static ErlangBridge getInstance() throws IOException {
        if (instance == null) {
            instance = new ErlangBridge();
        }
        return instance;
    }

    private final Socket erlangServer;

    private ErlangBridge() throws IOException {
        this.erlangServer = new Socket("localhost", 11111);
    }

    public boolean authenticate(String username, String password, String role) {
        BufferedWriter writer;
        BufferedReader reader;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(this.erlangServer.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(this.erlangServer.getInputStream()));
            writer.write("/login " + username + ' ' + password);
            writer.newLine();
            writer.flush();
            String line = reader.readLine();
            return Boolean.parseBoolean(line);
        } catch (Exception ex) {
            return false;
        }

    }
}
