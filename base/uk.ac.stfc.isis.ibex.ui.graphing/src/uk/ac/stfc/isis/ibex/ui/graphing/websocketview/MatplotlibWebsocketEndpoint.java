package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ClientEndpointConfig;
import jakarta.websocket.ClientEndpointConfig.Configurator;
import jakarta.websocket.CloseReason;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.RemoteEndpoint.Async;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import uk.ac.stfc.isis.ibex.logger.IsisLog;


@ClientEndpoint
public class MatplotlibWebsocketEndpoint extends Endpoint implements Closeable {
	
	private static final Logger LOG = IsisLog.getLogger(MatplotlibWebsocketEndpoint.class);
	private Session session;
	private static final Gson GSON = new Gson();
	
	private final String hostName;
	private final int port;
	private final int figNum;
	private final MatplotlibWebsocketModel model;
	
	public MatplotlibWebsocketEndpoint(MatplotlibWebsocketModel model, String hostName, int port, int figNum) {
		this.hostName = hostName;
		this.port = port;
		this.figNum = figNum;
		this.model = model;
	}
	
	@Override
	public String toString() {
		return String.format("ws://%s:%s", hostName, port, figNum);
	}
	
	public void connect() throws IOException {
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		try {
			LOG.info("Creating connection");
			
			final ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
					.configurator(new Configurator() {
						@Override
						public void beforeRequest(Map<String, List<String>> headers) {
							headers.put("Accept", List.of("*/*"));
							headers.put("Connection", List.of("keep-alive", "Upgrade"));
							headers.put("Pragma", List.of("no-cache"));
						}
					})
					.build();
			session = container.connectToServer(this, config, new URI(String.format("ws://%s:%s/%s/ws", hostName, port, figNum)));
			
			session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {
				@Override
				public void onMessage(ByteBuffer message) {
					model.setImageData(message);
					LOG.info(message);
				}
			});
		} catch (DeploymentException | URISyntaxException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void onOpen(Session session, EndpointConfig config) {
	}
	
	@Override
	public void onClose(Session session, CloseReason closeReason) {
		LOG.info(String.format("Websocket closing because: %s", closeReason));
		model.onConnectionClose();
	}
	
	private void sendProperty(Session session, String type, Map<String, Object> properties) {
		Async remote = session.getAsyncRemote();
		
	    Map<String, Object> propertiesToSend = new HashMap<>(properties);
	    propertiesToSend.put("type", type);
	    propertiesToSend.put("figure_id", Integer.toString(figNum));
	    
		remote.sendText(GSON.toJson(propertiesToSend));
	}

	@Override
	public void close() throws IOException {
		session.close();
	}

	public void forceServerRedraw() {
		sendProperty(session, "supports_binary", Map.of("value", true));
		sendProperty(session, "send_image_mode", Map.of("value", "full"));
		sendProperty(session, "refresh", Collections.emptyMap());
	}
	
	public void canvasResized(int width, int height) {
		sendProperty(session, "resize", Map.of("height", height, "width", width));
		sendProperty(session, "draw", Collections.emptyMap());
		forceServerRedraw();
	}
}
