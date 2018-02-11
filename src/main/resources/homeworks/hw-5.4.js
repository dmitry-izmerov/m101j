// Homework 5.4
// Preferred Cities to Live!
// In this problem you will calculate the number of people who live in a zip code 
// in the US where the city starts with one of the following characters:
// B, D, O, G, N or M .

use test;

db.zips.aggregate([
	{
		'$project': {
			'state': 1,
			'city': 1,
			'first_char': {'$substr': ['$city', 0, 1]},
			'pop': 1
		}
	},
	{
		'$match': {
			'first_char': {'$in': ['B', 'D', 'O', 'G', 'N', 'M']}
		}
	},
	{
		'$group': {
			'_id': null,
			'sum': {'$sum': '$pop'}
		}
	}
]);