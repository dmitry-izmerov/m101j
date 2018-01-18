package ru.demi;

import spark.Spark;

public class HelloWorldSparkApp {
    public static void main( String[] args ) {
        Spark.get("/", (req, res) -> "hello world from spark");
    }
}
