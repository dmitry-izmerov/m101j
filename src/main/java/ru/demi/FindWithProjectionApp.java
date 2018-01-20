package ru.demi;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;

public class FindWithProjectionApp {
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();
		MongoDatabase database;
		try (MongoClient client = new MongoClient(new ServerAddress(), options)) {
			database = client.getDatabase("course");
			MongoCollection<Document> col = database.getCollection("findWithProjection");

			col.drop();

			for (int i = 0; i < 10; i++) {
				col.insertOne(new Document("x", new Random().nextInt(2))
					.append("y", new Random().nextInt(100))
					.append("i", i)
				);
			}

			Bson filter = and(eq("x", 0), gt("y", 10), lt("y", 80));
//			Bson projection = new Document("y", 1).append("i", 1);
			Bson projection = Projections.fields(
				Projections.include("y", "i"),
				Projections.excludeId()
			);

			System.out.println("Find with filter and projection:");
			List<Document> docs = col.find(filter)
				.projection(projection)
				.into(new ArrayList<>());
			docs.forEach(doc -> System.out.println(doc.toJson()));
		}
    }
}
