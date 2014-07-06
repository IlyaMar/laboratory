var app = app || {};

(function () {
	'use strict';

	// possible states
	// #/ (all - default)
	// #/active
	// #/completed
	var DictionaryRouter = Backbone.Router.extend({
		routes:{
			'excercise' : 'excercise',
			'*filter': 'setFilter'
		},
		setFilter: function( param ) {
			console.log('TodoRouter setFilter to ' + param)

			// Set the current filter to be used
			app.WordFilter = param || '';
			
			// Trigger a collection filter event, causing hiding/unhiding of Todo view items
			app.Words.trigger('filter');
		},
		excercise : function () {
			this.loadView(new ExerciseView());
		},
		
		loadView : function(view) {
			this.view && (this.view.close ? this.view.close() : this.view.remove());
			this.view = view;
		}
		
	});

	app.DictionaryRouter = new DictionaryRouter();
	Backbone.history.start();

})();
