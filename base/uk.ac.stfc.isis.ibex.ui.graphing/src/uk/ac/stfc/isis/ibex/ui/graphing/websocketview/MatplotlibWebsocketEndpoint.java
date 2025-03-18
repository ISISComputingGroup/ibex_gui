package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ClientEndpointConfig;
import jakarta.websocket.ClientEndpointConfig.Configurator;
import jakarta.websocket.CloseReason;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.RemoteEndpoint.Async;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;


/**
 * Websocket endpoint for matplotlib.
 */
@ClientEndpoint
public class MatplotlibWebsocketEndpoint extends Endpoint implements Closeable {
	
	private static final Logger LOG = IsisLog.getLogger(MatplotlibWebsocketEndpoint.class);
	private Session session;
	private static final Gson GSON = new Gson();
	
	private final String url;
	private final int figNum;
	private final MatplotlibWebsocketModel model;
	private boolean lastCloseWasAbnormal;
	
	/**
	 * Create a new websocket endpoint.
	 * @param model the matplotlib model
	 * @param url the connection url
	 * @param figNum the figure number
	 */
	public MatplotlibWebsocketEndpoint(MatplotlibWebsocketModel model, String url, int figNum) {
		this.url = url;
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
		return String.format("ws://%s/%d/ws", url, figNum);
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
	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		if (lastCloseWasAbnormal) {
			LOG.info(String.format("Websocket at %s reconnected after abnormal closure", toString()));
			lastCloseWasAbnormal = false;
		}
		session.addMessageHandler(new MessageHandler.Whole<InputStream>() {
			@Override
			public void onMessage(InputStream message) {
				LoggerUtils.logIfExtraDebug(LOG, String.format("binary message from %s", getUrl(), message));
				model.setImageData(message);
			}
		});
		
		session.addMessageHandler(new MessageHandler.Whole<String>() {
			@Override
			public void onMessage(String message) {
				LoggerUtils.logIfExtraDebug(LOG, String.format("string message from %s: %s", getUrl(), message));
				try {
					// Can't deserialize to Map<String, String> because some messages are of type Map<String, List>
					// So have to deserialize to Object initially and then cast later.
				    Map<String, Object> content = GSON.fromJson(message, new TypeToken<Map<String, Object>>() { }.getType());
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
		    	model.setPlotName((String) content.getOrDefault("label", ""));
		    	break;
		    case "image_mode":
		    	model.setIsDiffImage(Objects.equals(content.get("mode"), "diff"));
		    	break;
		    case "message":
		    	model.setPlotMessage((String) content.getOrDefault("message", ""));
		    	break;
		    case "history_buttons":
		    	model.setBackState((Boolean) content.getOrDefault("Back", "false"));
		    	model.setForwardState((Boolean) content.getOrDefault("Forward", "false"));
		    	break;
		    case "navigate_mode":
		    	model.toggleZoomAndPan((String) content.getOrDefault("mode", ""));
		    	break;
		    case "cursor":
		    	model.setCursor((String) content.getOrDefault("cursor", "default"));
		    	break;
		    default:
		    	// No action required
		    	break;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		lastCloseWasAbnormal = (closeReason.getCloseCode() != CloseReason.CloseCodes.NORMAL_CLOSURE);
		if (lastCloseWasAbnormal) {
		    LOG.info(String.format("Websocket at %s closing abnormally because: %s", toString(), closeReason));
		}
		model.onConnectionClose();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@OnError
	public void onError(Session session, Throwable error) {
		LoggerUtils.logErrorWithStackTrace(LOG, error.getMessage(), error);
	}
	
	private void sendProperty(Session session, String type, Map<String, Object> properties) {
		if (!session.isOpen()) {
			return;
		}
		try {
			Async remote = session.getAsyncRemote();
			
		    Map<String, Object> propertiesToSend = new HashMap<>(properties);
		    propertiesToSend.put("type", type);
		    propertiesToSend.put("figure_id", Integer.toString(figNum));
		    
		    
			remote.sendText(GSON.toJson(propertiesToSend));
			LoggerUtils.logIfExtraDebug(LOG, String.format("sent %s to %s", propertiesToSend, getUrl()));
		} catch (RuntimeException e) {
			LoggerUtils.logErrorWithStackTrace(LOG, "Failed to send property to websocket: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		try {
			if (session != null) {
			    session.close();
			}
		} catch (IOException e) {
			LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
		}
	}
	
	private void sendImageModeParameters() {
		// Binary protocol is more efficient and we support it.
		sendProperty(session, "supports_binary", Map.of("value", true));
		
		// We don't want images with transparency, we want full frames.
		sendProperty(session, "send_image_mode", Map.of("value", "full"));
	}

	/**
	 * Forces a server-side redraw and resending of the image over a websocket.
	 */
	public void forceServerRefresh() {
		sendImageModeParameters();
		sendProperty(session, "refresh", Collections.emptyMap());
		sendProperty(session, "draw", Collections.emptyMap());
	}
	
	/**
	 * Sends a "canvas resized" event to the server. This will cause a redraw of the figure to fit into
	 * the new canvas size.
	 * @param width the new width
	 * @param height the new height
	 */
	public void canvasResized(int width, int height) {
		if (width > 0 && height > 0) {
		    sendProperty(session, "resize", Map.of("height", height, "width", width));
		}
		forceServerRefresh();
	}
	
	/**
	 * Sends a cursor event to the server.
	 * @param position the cursor position
	 */
	public void cursorPositionChanged(final MatplotlibCursorPosition position) {
		final Map<String, Object> event = Map.of(
				"x", position.x(), 
				"y", position.y(), 
				"button", 0,
				"buttons", 0,
				"guiEvent", new HashMap<>(), 
				"modifiers", new ArrayList<>()
		);
		
		if (position.inPlot()) {
		    sendProperty(session, "figure_enter", event);
			sendProperty(session, "motion_notify", event);
		} else {
			sendProperty(session, "figure_leave", event);
		}
	}
	
	/**
	 * Sends a navigation event to the server.
	 * @param navType
	 */
	public void navigatePlot(MatplotlibButtonType navType) {
		final Map<String, Object> event = Map.of("name", navType.getWebsocketString());
		sendProperty(session, "toolbar_button", event);
	}
	
	/**
	 * Sends a "mouse button pressed" (on the canvas) event to the server. 
	 * @param position
	 * @param pressType
	 */
	public void notifyButtonPress(final MatplotlibCursorPosition position, MatplotlibPressType pressType) {
	  final Map<String, Object> event = Map.of(
			  "x", position.x(), 
			  "y", position.y(), 
			  "button", 0,
			  "buttons", 0,
			  "guiEvent", new HashMap<>(), 
			  "modifiers", new ArrayList<>()
	  );
	  
	  sendProperty(session, pressType.getWebsocketString(), event);
	}
	
}
