package ru.demi;

import com.mongodb.client.MongoCursor;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;

public class FindApp {
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();
		MongoDatabase database;
		try (MongoClient client = new MongoClient(new ServerAddress(), options)) {
			database = client.getDatabase("test");
			MongoCollection<Document> collection = database.getCollection("col");

			collection.drop();

			for (int i = 0; i < 10; i++) {
				collection.insertOne(new Document("value", i));
			}

			System.out.println("Find one:");
			System.out.println(collection.find().first().toJson());

			System.out.println("Find all with into:");
			collection.find().into(new ArrayList<>()).forEach(doc -> System.out.println(doc.toJson()));

			System.out.println("Find all with iteration:");
			try (MongoCursor<Document> cursor = collection.find().iterator()) {
				while (cursor.hasNext()) {
					System.out.println(cursor.next().toJson());
				}
			}

			System.out.println("Count:");
			System.out.println(collection.count());
		}
    }
}
