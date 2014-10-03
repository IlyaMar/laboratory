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
			this.wordsAsked = 1;	// current exercise statistics
			this.wordsCorrect = 0;
			
			this.render();
			app.Words.fetch();
			console.log(app.Words.size())
		},

		render: function() {
			if (this.wordIndex < app.Words.size()) {
				console.log("ExerciseView render 1")
				var word = app.Words.at(this.wordIndex)
				this.$el.html(this.template( {'forward' : word.get('forward'), 
											  'backward' : word.get('backward') })
											  );
			}
			else {
				console.log("ExerciseView render 2")
				this.$el.html(this.templateComplete({'dictionary': 'D1',
													 'asked': this.wordsAsked,
													 'correct': this.wordsCorrect} ));
			}
		},
		
		correct: function() {
			console.log("ExView correct");
			var word = app.Words.at(this.wordIndex);
			word.correct_count++;
			this.wordsCorrect++;
		},
		
		check: function() {
			console.log("ExView check");
			this.$('.hidden').removeClass('hidden');
		},
		
		nextWord: function() {
			console.log("ExView next");
			var word = app.Words.at(this.wordIndex);
			word.test_count++;
			this.wordsAsked++;
			
			this.wordIndex++;	// now iterating is linear, plans to make it random
			this.render();
		}
  });
	
})();	

