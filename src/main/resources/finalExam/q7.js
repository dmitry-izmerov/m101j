// Your task is to write a program to remove every image from the images collection
// that appears in no album.
// Or put another way, if an image does not appear in at least one album,
// it's an orphan and should be removed from the images collection.

// When you are done removing the orphan images from the collection, there should be 89,737 documents in the images collection.

// What are the total number of images with the tag "sunrises" after the removal of orphans?

use test;

var indexes = db.albums.getIndexes();

var hasIndex = indexes.some(function(index) {
	var keys = Object.keys(index.key);
	return keys.length === 1 && index.key.images === 1;
});

print('has index = ' + hasIndex);

if (!hasIndex) {
	db.albums.createIndex({images: 1});	
}

var images = db.images.find();

var forRemove = [];

images.forEach(function(image) {
	var imageId = image._id;

	var count = db.albums.count({
		images: imageId
	});

	if (count === 0) {
		forRemove.push(imageId);
	}
});

print('for remove length: ' + forRemove.length);

if (forRemove.length > 1) {
	db.images.deleteMany({_id: {'$in': forRemove}});
}

print('images count: ' + db.images.count());

var countByTag = db.images.count({tags: 'sunrises'});
print('images count by tag sunrises:' + countByTag);
