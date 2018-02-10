// Homework 5.1 - Finding the most frequent author of comments on your blog

// use blog;
db.posts.aggregate([
	{'$unwind': '$comments'},
	{
		'$group': {
			'_id': '$comments.author',
			'count': {'$sum': 1}
		}
	},
	{'$sort': {'count': -1}},
	{'$limit': 1},
	{
		'$project': {
			'_id': 0,
			'author': '$_id'
		}
	}
]);