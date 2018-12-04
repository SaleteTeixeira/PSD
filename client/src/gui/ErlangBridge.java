package gui;

import java.io.IOException;
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
        this.erlangServer = new Socket("localhost",11111);
    }
    
    public boolean authenticate(String username, String password, String role) {
        return false;
    }
}
