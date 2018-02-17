package ru.demi;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

public class ReplicaSetTest {
	public static void main(String[] args) throws InterruptedException {

		List<ServerAddress> addresses = Arrays.asList(
			new ServerAddress("localhost", 27017),
			new ServerAddress("localhost", 27018),
			new ServerAddress("localhost", 27019)
		);

		MongoClientOptions options = MongoClientOptions.builder()
			.requiredReplicaSetName("m101")
			.build();

		try (MongoClient client = new MongoClient(addresses, options)) {
			MongoCollection<Document> col = client.getDatabase("course").getCollection("replication");
			col.drop();

			for (int i = 0; i < Integer.MAX_VALUE; i++) {
				try {
					col.insertOne(new Document("_id", i));
					System.out.printf("Inserted document with id: %s.%n", i);
				} catch (MongoException e) {
					System.out.printf("Error inserting document with id: %s, exception message: %s.%n", i, e.getMessage());
				}
				Thread.sleep(500);
			}
		}
	}
}
