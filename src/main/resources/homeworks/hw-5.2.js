// Homework 5.2 
// Please calculate the average population of cities in California (abbreviation CA)
// and New York (NY) (taken together) with populations over 25,000.

use test;

db.zips.aggregate([
	{
		'$match': {
			'state': {'$in': ['CA', 'NY']}
		}
	},
	{
		'$group': {
			'_id': {'state': '$state', 'city': '$city'},
			'pop': {'$sum': '$pop'}
		}
	},
	{
		'$match': {
			'pop': {'$gt': 25000}
		}
	},
	{
		'$group': {
			'_id': 'null',
			'avg': {'$avg': '$pop'}
		}
	}
]);

