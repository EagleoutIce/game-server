package de.uulm.team020.server.client.controller;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;

/**
 * This is the bare-bones client implementation used by the server-client to
 * connect with a server. It is of the simple redirection kind.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/24/2020
 * 
 * @since 1.1
 */
public class ServerClientSocket extends WebSocketClient {

    private static IMagpie magpie = Magpie.createMagpieSafe("Server-Client");

    private ServerClientController controller;

    public ServerClientSocket(final ServerClientController controller, URI serverUri) {
        super(serverUri);
        this.controller = controller;
        magpie.writeInfo("Client started", "Client");
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        controller.awake();
    }

    @Override
    public void onMessage(String message) {
        controller.handleMessage(message);

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        controller.onClose(code, reason, remote);

    }

    @Override
    public void onError(Exception ex) {
        magpie.writeException(ex, "Serverclient");
    }

}