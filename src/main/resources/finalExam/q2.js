// Please use the Enron dataset you imported for the previous problem.
// For this question you will use the aggregation framework to figure out pairs of people
// that tend to communicate a lot. To do this, you will need to unwind the To list for each message.

// This problem is a little tricky because a recipient may appear more than once in the To list for a message.
// You will need to fix that in a stage of the aggregation before doing your grouping and counting of (sender, recipient) pairs.

// Which pair of people have the greatest number of messages in the dataset?

use enron;

db.messages.aggregate([
	{
		'$project': {
			'from': '$headers.From',
			'to': '$headers.To',
			'messageId': '$headers.Message-ID'
		}
	},
	{'$unwind': '$to'},
	{
		'$group': {
			'_id': {
				'from': '$from',
				'to': '$to',
				'messageId': '$messageId'
			}
		}
	},
	{
		'$group': {
			'_id': {
				'from': '$_id.from',
				'to': '$_id.to'
			},
			'count': {'$sum': 1}
		}
	},
	{
		'$sort': {'count': -1}
	}
], { allowDiskUse: true });
