package ru.demi;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.gte;


public class AggregationTest {
	public static void main(String[] args) {
		try (MongoClient client = new MongoClient()) {
			MongoDatabase database = client.getDatabase("course");
			MongoCollection<Document> col = database.getCollection("zipcodes");

			// first method
			List<Document> pipeline1 = Arrays.asList(
				new Document(
					"$group",
					new Document("_id", "state")
						.append("totalPop", new Document("$sum", "$pop"))
				),
				new Document(
					"$match",
					new Document("totalPop", new Document("$gte", 10_000_000))
				)
			);

			// second method
			List<Bson> pipeline2 = Arrays.asList(
				group("$state", sum("totalPop", "$pop")),
				match(gte("totalPop", 10_000_000))
			);

			// third method
			List<Document> pipeline3 = Arrays.asList(
				Document.parse("{ $group: { _id: \"$state\", totalPop: { $sum: \"$pop\" } } }"),
				Document.parse("{ $match: { totalPop: { $gte: 10000000 } } }")
			);

			ArrayList<Document> result = col.aggregate(pipeline2).into(new ArrayList<>());
			result.forEach(doc -> System.out.println(doc.toJson()));
		}
	}
}

