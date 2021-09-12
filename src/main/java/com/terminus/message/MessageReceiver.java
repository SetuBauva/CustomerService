package com.terminus.message;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import com.microsoft.azure.servicebus.MessageHandlerOptions;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.SubscriptionClient;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import com.terminus.controller.CustomerController;

@Component
public class MessageReceiver {

	@Autowired
	private CustomerController customerController;

	String connectionString = "connectionString";

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	static final Gson GSON = new Gson();

	@Autowired
	private MessageReceiver registerReceiver;

	@PostConstruct
	public void init() {
		SubscriptionClient subscription1Client;
		try {
			subscription1Client = new SubscriptionClient(new ConnectionStringBuilder(connectionString, "entityPath"),
					ReceiveMode.PEEKLOCK);
			registerReceiver.registerMessageHandlerOnClient(subscription1Client);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ServiceBusException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void registerMessageHandlerOnClient(SubscriptionClient receiveClient) throws Exception {

		IMessageHandler messageHandler = new IMessageHandler() {
			Map jsonMessage = null;

			public CompletableFuture<Void> onMessageAsync(IMessage message) {

				if (message.getContentType() != null && message.getContentType().contentEquals("application/json")) {

					byte[] body = message.getBody();
					jsonMessage = GSON.fromJson(new String(body, UTF_8), Map.class);

					logger.info("Service Bus Message received with CustomerId = "
							+ (jsonMessage != null ? jsonMessage.get("CustomerId") : "") + "\nPhoneNumber: "
							+ (jsonMessage != null ? jsonMessage.get("PhoneNumber") : "") + "\nStatus: "
							+ (jsonMessage != null ? jsonMessage.get("Status") : ""));
					customerController.receiveMessage(jsonMessage.get("CustomerId").toString(),
							jsonMessage.get("PhoneNumber").toString(), jsonMessage.get("Status").toString());
				}
				return receiveClient.completeAsync(message.getLockToken());
			}

			public void notifyException(Throwable throwable, ExceptionPhase exceptionPhase) {
				logger.error(exceptionPhase + "-" + throwable.getMessage());
			}
		};

		receiveClient.registerMessageHandler(messageHandler,
				new MessageHandlerOptions(1, false, Duration.ofMinutes(1)));
	}
}
