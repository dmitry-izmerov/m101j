package ru.demi;

import static freemarker.template.Configuration.VERSION_2_3_23;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class HelloWorldFreeMarkerApp {
    public static void main(String[] args) {
        Configuration configuration = new Configuration(VERSION_2_3_23);
        configuration.setClassForTemplateLoading(HelloWorldFreeMarkerApp.class, "/");
    
        try {
            Template template = configuration.getTemplate("hello.ftl");
            Map<String, Object> map = new HashMap<>();
            map.put("name", "Freemarker");
    
            StringWriter writer = new StringWriter();
            template.process(map, writer);
    
            System.out.println(writer);
        } catch (TemplateException | IOException e) {
            LoggerFactory.getLogger(HelloWorldFreeMarkerApp.class).error("Problem with getting of template.", e);
        }
    }
}
