package ru.demi;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;

public class DeleteApp {
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();
		MongoDatabase database;
		try (MongoClient client = new MongoClient(new ServerAddress(), options)) {
			database = client.getDatabase("course");
			MongoCollection<Document> col = database.getCollection("delete");

			col.drop();

			for (int i = 0; i < 10; i++) {
				col.insertOne(new Document("_id", i));
			}

			col.deleteOne(eq("_id", 1));
			col.deleteMany(gt("_id", 4));

			List<Document> docs = col.find().into(new ArrayList<>());
			docs.forEach(doc -> System.out.println(doc.toJson()));
		}
    }
}
