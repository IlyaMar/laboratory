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
			//"<h1>" + this.word.get('forward') + "</h1>" + 
			//	'<h1 class="hidden">' + this.word.get('backward') + "</h1>"
			//);
		},
		
		correct: function() {
			console.log("ExView correct");
		},
		
		
		nextWord: function() {
			console.log("ExView next");
			this.wordIndex++;
			this.render();
		}
  });
	
})();	

