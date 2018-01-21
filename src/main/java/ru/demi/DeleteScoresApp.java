package ru.demi;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class DeleteScoresApp {
	public static void main(String[] args) {
		MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();
		MongoDatabase database;
		try (MongoClient client = new MongoClient(new ServerAddress(), options)) {
			database = client.getDatabase("students");
			MongoCollection<Document> col = database.getCollection("grades");

			AggregateIterable<Document> iterable = col.aggregate(asList(
				new Document("$match", new Document("type", "homework")),
				new Document(
					"$group", new Document("_id", "$student_id")
						.append("min", new Document("$min", "$score"))
				)
			));


			List<Bson> filters = new ArrayList<>();
			try (MongoCursor<Document> cursor = iterable.iterator()) {
				while (cursor.hasNext()) {
					Document doc = cursor.next();
					Document itemFilter = new Document("student_id", doc.get("_id")).append("score", doc.get("min"));
					filters.add(itemFilter);
 				}
			}

			System.out.println(col.deleteMany(Filters.or(filters)));
		}
	}
}
