// Homework 5.3
// Who's the easiest grader on campus?

use test;

db.grades.aggregate([
	{'$unwind': '$scores'},
	{
		'$match': {
			'type': {'$ne': 'quiz'}
		}
	},
	{
		'$group': {
			'_id': {'class_id': '$class_id', 'student_id': '$student_id'},
			'avg_score': {'$avg': '$scores.score'}
		}
	},
	{
		'$group': {
			'_id': {'class_id': '$_id.class_id'},
			'avg': {'$avg': '$avg_score'}
		}
	},
	{
		'$sort': {'avg': -1}
	}
]);