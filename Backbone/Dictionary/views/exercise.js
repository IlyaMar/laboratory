 var app = app || {};

(function () {
	'use strict';

	app.ExerciseView = Backbone.View.extend({
		// generate a new element
		tagName: "div",
		id: "exercise-view",
		
	    template: _.template( $('#exercise-template').html() ),
	    templateComplete: _.template( $('#exercise-complete-template').html() ),

		wordIndex: 0,
		
		events: {
		  'keypress #new-word-backward': 'createOnEnter',
		  'click .correct': 'correct',
		  'click .check': 'check',
		  'click .next': 'nextWord'
		},
		
		initialize: function() {
			console.log("ExerciseView initialize")
			this.wordIndex = 0;		// iterate words from start
			this.render();
			//this.listenTo(app.Words, 'add', this.addOne);
			//app.Words.fetch();
		},

		render: function() {
			if (this.wordIndex < app.Words.size()) {
				var word = app.Words.at(this.wordIndex)
				this.$el.html(this.template( {'forward' : word.get('forward'), 
											  'backward' : word.get('backward') })
											  );
			}
			else {
				this.$el.html(this.templateComplete({'dictionary': 'D1',
													 'asked': 10,
													 'correct': 5} ));
			}
		},
		
		correct: function() {
			console.log("ExView correct");
			var word = app.Words.at(this.wordIndex);
			word.correct_count++;
		},
		
		check: function() {
			console.log("ExView check");
			this.$('.hidden').removeClass('hidden');
		},
		
		nextWord: function() {
			console.log("ExView next");
			var word = app.Words.at(this.wordIndex);
			word.test_count++;
			
			this.wordIndex++;
			this.render();
		}
  });
	
})();	

