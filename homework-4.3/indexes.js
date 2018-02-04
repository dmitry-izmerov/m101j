let db;

// for main page
db.posts.createIndex({date: -1});

// for tags query
db.posts.createIndex({tags: 1});

// for permalink query
db.posts.createIndex({permalink: 1});