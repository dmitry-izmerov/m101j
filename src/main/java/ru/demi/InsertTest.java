package ru.demi;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class InsertTest {
	public static void main(String[] args) {
		MongoDatabase db;
		MongoCollection<Document> animals;
		Document animal;
		try (MongoClient client = new MongoClient()) {
			db = client.getDatabase("test");

			animals = db.getCollection("animals");
			animals.drop();

			animal = new Document("animal", "monkey");

			animals.insertOne(animal);
			animal.remove("animal");
			animal.append("animal", "cat");
			animals.insertOne(animal);
			animal.remove("animal");
			animal.append("animal", "lion");
			animals.insertOne(animal);
		}
	}
}