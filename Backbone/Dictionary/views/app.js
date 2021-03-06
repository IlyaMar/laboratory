 var app = app || {};

(function () {
	'use strict';
 
 // The Application
  // ---------------
  // Our overall **AppView** is the top-level piece of UI.
  app.AppView = Backbone.View.extend({
	tagName: "section",
	id: "dictionary-app",
	 
    template: _.template( $('#dict-template').html() ),
	
	// Our template for the line of statistics at the bottom of the app.
    statsTemplate: _.template( $('#stats-template').html() ),
    
	 // Delegated events for creating new items, and clearing completed ones.
    events: {
      'keypress #new-word-backward': 'createOnEnter',
      'click #clear-completed': 'clearCompleted',
      'click #toggle-all': 'toggleAllComplete'
    },
	
	// At initialization we bind to the relevant events on the `Todos`
    // collection, when items are added or changed.
    initialize: function() {
		console.log('AppView initialize');
	  this.$el.html( this.template() );
      this.allCheckbox = this.$('#toggle-all')[0];
      this.$inputForward = this.$('#new-word-forward');
      this.$inputBackward = this.$('#new-word-backward');
      this.$footer = this.$('#footer');
      this.$main = this.$('#main');

      app.Words.fetch();
	  
	  this.listenTo(app.Words, 'add', this.addOne);
      this.listenTo(app.Words, 'reset', this.addAll);	  
	  this.listenTo(app.Words, 'change:completed', this.filterOne);
      this.listenTo(app.Words, 'filter', this.filterAll);
      this.listenTo(app.Words, 'all', this.render);
	  app.Words.trigger('reset');
    },
    
	 // Rerendering the app just means refreshing the statistics -- the rest
    // of the app doesn't change.
    render: function() {
	  console.log('AppView render')
	
      var completed = app.Words.completed().length;
      var remaining = app.Words.remaining().length;
      if ( app.Words.length ) {
        this.$main.show();
        this.$footer.show();
        this.$footer.html(this.statsTemplate({
          completed: completed,
          remaining: remaining
        }));
        this.$('#filters li a')
          .removeClass('selected')
          .filter('[href="#/' + ( app.WordFilter || '' ) + '"]')
          .addClass('selected');
      } else {
        this.$main.hide();
        this.$footer.hide();
      }
      this.allCheckbox.checked = !remaining;
	  return this
    },
	
	// Add a single todo item to the list by creating a view for it, and
    // appending its element to the `<ul>`.
    addOne: function( todo ) {
		console.log('AppView addOne')
      var view = new app.WordView({ model: todo });
      this.$('#word-table').append( view.render().el );
    },
    
	// Add all items in the **Words** collection at once.
    addAll: function() {
      this.$('#word-table').html('');
      app.Words.each(this.addOne, this);
    },
	
	filterOne : function (todo) {
		todo.trigger('visible');
	},
	
	filterAll : function () {
      app.Words.each(this.filterOne, this);
    },
	
	// Generate the attributes for a new todo item.
    newAttributes: function() {
      return {
        forward: this.$inputForward.val().trim(),
        backward: this.$inputBackward.val().trim(),
        order: app.Words.nextOrder(),
        completed: false
      };
    },
	
	// If you hit return in the main input field, create new Todo model,
    // persisting it to localStorage.
    createOnEnter: function( event ) {
      if ( event.which !== ENTER_KEY ) {
        return;
      }
		if ( !this.$inputForward.val().trim() || !this.$inputBackward.val().trim()) {
			alert('Word incomplete!')
			return;
		}
		
	   var a = this.newAttributes();
 	   console.log('AppView createOnEnter, attrs ' + a);
      app.Words.create(a);
      this.$inputForward.val('');
      this.$inputBackward.val('');
    },

    // Clear all completed todo items, destroying their models.
    clearCompleted: function() {
      _.invoke(app.Words.completed(), 'destroy');
      return false;
    },

    toggleAllComplete: function() {
      var completed = this.allCheckbox.checked;
      app.Words.each(function( todo ) {
        todo.save({
          'completed': completed
        });
      });
    }
  });
	
})();	
