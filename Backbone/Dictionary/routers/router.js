var app = app || {};

(function () {
	'use strict';

	// possible states
	// #/ (all - default)
	// #/active
	// #/completed
	var DictionaryRouter = Backbone.Router.extend({
		routes: {
			'' : 'default',
			'exercise' : 'exercise',
			'*filter': 'setFilter'
		},
		
		initialize: function() {
			console.log('DictionaryRouter initialize')
		},
		
		default: function() {
			this.loadView(new app.AppView());
			document.body.appendChild( this.view.el );
		},
		
		setFilter: function( param ) {
			console.log('DictionaryRouter setFilter to ' + param)

			// Set the current filter to be used
			app.WordFilter = param || '';
			
			// Trigger a collection filter event, causing hiding/unhiding of Todo view items
			app.Words.trigger('filter');
		},
		
		exercise : function () {
			var v = new app.ExerciseView();
			this.loadView(v);
			document.body.appendChild( this.view.el );
		},
		
		loadView : function(view) {
			console.log('DictionaryRouter loadView, view el #' + view.el.id)
			this.view && (this.view.close ? this.view.close() : this.view.remove());
			this.view = view;
		}
		
	});

	app.DictionaryRouter = new DictionaryRouter();
	Backbone.history.start();

})();
