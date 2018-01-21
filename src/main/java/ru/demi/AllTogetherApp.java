package ru.demi;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.bson.Document;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.io.IOException;
import java.io.StringWriter;

import static freemarker.template.Configuration.VERSION_2_3_23;
import static spark.Spark.halt;

public class AllTogetherApp {
	public static void main(String[] args) {
		Configuration configuration = new Configuration(VERSION_2_3_23);
		configuration.setClassForTemplateLoading(AllTogetherApp.class, "/");

		MongoClient client = new MongoClient();
		MongoDatabase db = client.getDatabase("course");
		MongoCollection<Document> collection = db.getCollection("hello");
		collection.drop();

		collection.insertOne(new Document("name", "mongodb"));

		StringWriter writer = new StringWriter();
		Spark.get("/", (req, res) -> {
			try {
				Template template = configuration.getTemplate("hello.ftl");

				Document document = collection.find().first();

				template.process(document, writer);
			} catch (TemplateException | IOException e) {
				LoggerFactory.getLogger(AllTogetherApp.class).error("Problem with getting of template.", e);
				halt(500);
			}
			return writer;
		});
	}
}
