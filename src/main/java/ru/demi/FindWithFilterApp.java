package ru.demi;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;

public class FindWithFilterApp {
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();
		MongoDatabase database;
		try (MongoClient client = new MongoClient(new ServerAddress(), options)) {
			database = client.getDatabase("course");
			MongoCollection<Document> col = database.getCollection("findWithFilter");

			col.drop();

			for (int i = 0; i < 10; i++) {
				col.insertOne(new Document("x", new Random().nextInt(2)).append("y", new Random().nextInt(100)));
			}

//			Bson filter = new Document("x", 0)
//				.append("y", new Document("$gt", 10).append("$lt", 80));

			Bson filter = and(eq("x", 0), gt("y", 10), lt("y", 80));

			System.out.println("Find with filter:");
			List<Document> docs = col.find(filter).into(new ArrayList<>());
			docs.forEach(doc -> System.out.println(doc.toJson()));

			System.out.println("Counter with filter:");
			System.out.println(col.count(filter));
		}
    }
}
