 var app = app || {};

(function () {
	'use strict';

	app.ExerciseView = Backbone.View.extend({
    // generate a new element
	tagName: "div",
	
    events: {
      'keypress #new-word-backward': 'createOnEnter',
      'click #exercise-correct': 'correct',
      'click #toggle-all': 'toggleAllComplete'
    },
	
	// At initialization we bind to the relevant events on the `Todos`
    // collection, when items are added or changed.
    initialize: function() {
		console.log("ExView init")
		this.word = app.Words.last()
		
		$("exercise-app").html(this.el);
		this.render();
		//this.listenTo(app.Words, 'add', this.addOne);
		//app.Words.fetch();
    },

	render: function() {
		this.$el.html("<h1>" + this.word.get('forward') + "</h1>" + 
			'<h1 class="hidden">' + this.word.get('backward') + "</h1>"
		);
	},
	
	correct: function() {
		console.log("ExView correct")
	}
	
	
	nextWord: function() {
		console.log("ExView next")
	}
	
	
  });
	
})();	

