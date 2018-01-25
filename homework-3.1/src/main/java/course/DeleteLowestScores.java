package course;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Comparator;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class DeleteLowestScores {
	public static void main(String[] args) {
		MongoDatabase database;
		try (MongoClient client = new MongoClient()) {
			database = client.getDatabase("school");
			MongoCollection<Document> col = database.getCollection("students");

			try (MongoCursor<Document> cursor = col.find().iterator()) {
				while (cursor.hasNext()) {
					Document document = cursor.next();
					List<Document> scores = (List<Document>) document.get("scores");

					scores.stream()
						.filter(score -> "homework".equals(score.get("type")))
						.min(Comparator.comparingDouble(score -> score.getDouble("score")))
						.ifPresent(scores::remove);

					document.put("scores", scores);

					System.out.println(col.replaceOne(eq("_id", document.get("_id")), document));
				}
			}
		}
	}
}
