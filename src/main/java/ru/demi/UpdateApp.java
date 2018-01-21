package ru.demi;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Updates.inc;

public class UpdateApp {
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();
		MongoDatabase database;
		try (MongoClient client = new MongoClient(new ServerAddress(), options)) {
			database = client.getDatabase("course");
			MongoCollection<Document> col = database.getCollection("update");

			col.drop();

			for (int i = 0; i < 10; i++) {
				col.insertOne(new Document("_id", i).append("x", i).append("y", true));
			}

			col.replaceOne(eq("x", 2), new Document("x", 22).append("updated", true));
//			col.updateOne(eq("x", 3), new Document("$set", new Document("x", 23).append("updated", true)));
			col.updateOne(eq("x", 3), Updates.combine(Updates.set("x", 23), Updates.set("updated", true)));

			// upsert
			col.updateOne(eq("_id", 10), Updates.combine(Updates.set("x", 23), Updates.set("updated", true)), new UpdateOptions().upsert(true));

			col.updateMany(gte("x", 6), inc("x", 2));

			List<Document> docs = col.find().into(new ArrayList<>());
			docs.forEach(doc -> System.out.println(doc.toJson()));
		}
    }
}
