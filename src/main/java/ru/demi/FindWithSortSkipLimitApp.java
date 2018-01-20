package ru.demi;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class FindWithSortSkipLimitApp {
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();
		MongoDatabase database;
		try (MongoClient client = new MongoClient(new ServerAddress(), options)) {
			database = client.getDatabase("course");
			MongoCollection<Document> col = database.getCollection("findWithSortSkipLimit");

			col.drop();

			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					col.insertOne(new Document("i", i).append("j", j));
				}

			}

			Bson projection = Projections.fields(
				Projections.include("i", "j"),
				Projections.excludeId()
			);
//			Bson sort = new Document("i", 1).append("j", -1);
			Bson sort = Sorts.orderBy(Sorts.ascending("i"), Sorts.descending("j"));

			System.out.println("Find with filter and projection:");
			List<Document> docs = col.find()
				.projection(projection)
				.sort(sort)
				.skip(10)
				.limit(10)
				.into(new ArrayList<>());
			docs.forEach(doc -> System.out.println(doc.toJson()));
		}
    }
}
