package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
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
import com.google.gson.reflect.TypeToken;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;


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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getUrl();
	}
	
	private String getUrl() {
		return String.format("ws://%s:%d/%d/ws", hostName, port, figNum);
	}
	
	/**
	 * Connect to the websocket server.
	 * @throws IOException if could not connect
	 */
	public void connect() throws IOException {
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		try {			
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
			session = container.connectToServer(this, config, new URI(getUrl()));
		} catch (DeploymentException | URISyntaxException e) {
			throw new IOException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onOpen(Session session, EndpointConfig config) {
		session.addMessageHandler(new MessageHandler.Whole<InputStream>() {
			@Override
			public void onMessage(InputStream message) {
				model.setImageData(message);
			}
		});
		
		session.addMessageHandler(new MessageHandler.Whole<String>() {
			@Override
			public void onMessage(String message) {
				try {
				    Map<String, Object> content = GSON.fromJson(message, new TypeToken<Map<String, Object>>() {}.getType());
				    handleJsonMessage(content);
				} catch (Exception e) {
					LoggerUtils.logErrorWithStackTrace(LOG, "error parsing server message " + message, e);
				}
			}
		});
	}
	
	private void handleJsonMessage(final Map<String, Object> content) {
		final var type = (String) content.get("type");

		switch (type) {
		    case "figure_label":
		    	model.setPlotName((String) content.get("label"));
		    	break;
		    case "image_mode":
		    case "draw":
		    	// No action required
		    	break;
		    default:
		    	break;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClose(Session session, CloseReason closeReason) {
		LOG.info(String.format("Websocket at %s closing because: %s", toString(), closeReason));
		for (var h : session.getMessageHandlers()) {
			session.removeMessageHandler(h);
		}
		model.onConnectionClose();
	}
	
	private void sendProperty(Session session, String type, Map<String, Object> properties) {
		Async remote = session.getAsyncRemote();
		
	    Map<String, Object> propertiesToSend = new HashMap<>(properties);
	    propertiesToSend.put("type", type);
	    propertiesToSend.put("figure_id", Integer.toString(figNum));
	    
		remote.sendText(GSON.toJson(propertiesToSend));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void close() {
		try {
			session.close();
		} catch (IOException e) {
			LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
		}
		model.onConnectionClose();
	}
	
	private synchronized void sendImageModeParameters() {
		// Binary protocol is more efficient and we support it.
		sendProperty(session, "supports_binary", Map.of("value", true));
		
		// We don't want images with transparency, we want full frames each time.
		sendProperty(session, "send_image_mode", Map.of("value", "full"));
	}

	/**
	 * Forces a server-side redraw and resending of the image over a websocket.
	 */
	public synchronized void forceServerRefresh() {
		sendImageModeParameters();
		sendProperty(session, "draw", Collections.emptyMap());
		sendProperty(session, "refresh", Collections.emptyMap());
	}
	
	/**
	 * Sends a "canvas resized" event to the server. This will cause a redraw of the figure to fit into
	 * the new canvas size.
	 * @param width the new width
	 * @param height the new height
	 */
	public synchronized void canvasResized(int width, int height) {
		if (width > 0 && height > 0) {
		    sendProperty(session, "resize", Map.of("height", height, "width", width));
		}
		forceServerRefresh();
	}
}
