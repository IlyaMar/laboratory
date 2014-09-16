 var app = app || {};

(function () {
	'use strict';

	app.Word = Backbone.Model.extend({
		defaults: {
			forward: '',
			backward: '',
			test_count: 0,
			correct_count: 0,
			completed: false		// marked as already learned
		},
		// Toggle the `completed` state of this todo item.
		toggle: function() {
			console.log('Word toggle');
			this.save({ completed: !this.get('completed') });
		}
	});
  
})();