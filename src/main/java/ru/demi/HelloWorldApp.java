package ru.demi;

import static freemarker.template.Configuration.VERSION_2_3_23;
import static spark.Spark.halt;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import spark.Spark;

public class HelloWorldApp {
    public static void main(String[] args) {
        Configuration configuration = new Configuration(VERSION_2_3_23);
        configuration.setClassForTemplateLoading(HelloWorldApp.class, "/");
    
        StringWriter writer = new StringWriter();
        Spark.get("/", (req, res) -> {
            try {
                Template template = configuration.getTemplate("hello.ftls");
                Map<String, Object> map = new HashMap<>();
                map.put("name", "Freemarker");
                template.process(map, writer);
            } catch (TemplateException | IOException e) {
                LoggerFactory.getLogger(HelloWorldApp.class).error("Problem with getting of template.", e);
                halt(500);
            }
            return writer;
        });
    }
}
