package com.terminus.message;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.servicebus.TopicClient;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;

@Component
public class MessageSender {

	String connectionString = "connectionString";

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	static final Gson GSON = new Gson();

	@Async
	public CompletableFuture<Void> sendCustomerInfo(int customerId, int phoneNumber, String status) throws Exception {
		TopicClient sendClient = new TopicClient(new ConnectionStringBuilder(connectionString, "entityPath"));

		List<HashMap<String, String>> data = GSON.fromJson("[" + "{'CustomerId':" + customerId + "," + "'PhoneNumber':"
				+ phoneNumber + "," + "'Status':" + status + "}" + "]", new TypeToken<List<HashMap<String, String>>>() {
				}.getType());

		List<CompletableFuture> tasks = new ArrayList<>();
		logger.info("sent Data:" + data.toString());
		for (int i = 0; i < data.size(); i++) {
			final String messageId = Integer.toString(i) + new Date().getTime();
			Message message = new Message(GSON.toJson(data.get(i), Map.class).getBytes(UTF_8));
			message.setContentType("application/json");
			message.setLabel("Customer Details");
			message.setMessageId(messageId);
			message.setTimeToLive(Duration.ofMinutes(5));
			tasks.add(sendClient.sendAsync(message).thenRunAsync(() -> sendClient.closeAsync()));
		}
		return CompletableFuture.allOf(tasks.toArray(new CompletableFuture<?>[tasks.size()]));
	}

}