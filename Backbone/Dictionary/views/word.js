 var app = app || {};

(function () {
 'use strict';

 // Todo Item View
  // --------------
  // The DOM element for a word...
  app.WordView = Backbone.View.extend({
    //... is a table row.
    tagName: 'tr',

    // Cache the template function for a single item.
    template: _.template( $('#word-template').html() ),

    // The DOM events specific to an item.
    events: {
		'click .toggle': 'togglecompleted', 
      'dblclick label': 'edit',
      'keypress .edit': 'updateOnEnter',
      'blur .edit': 'close',
		'click .destroy': 'clear'
    },

    // The WordView listens for changes to its model, rerendering. Since there's
    // a one-to-one correspondence between a **Word** and a **WordView** in this
    // app, we set a direct reference on the model for convenience.
    initialize: function() {
		console.log('WordView initialize')
      this.listenTo(this.model, 'change', this.render);
		this.listenTo(this.model, 'destroy', this.remove);
      this.listenTo(this.model, 'visible', this.toggleVisible);

    },

    // Rerenders the titles of the todo item.
    render: function() {
		console.log('WordView render, ' + this.model.get('forward'))
      this.$el.html( this.template( this.model.toJSON() ) );
		this.$el.toggleClass( 'completed', this.model.get('completed') );
		this.toggleVisible();
      this.$input = this.$('.edit');
      return this;
	},

	toggleVisible : function () {
      this.$el.toggleClass( 'hidden',  this.isHidden());
   },
	
	 // Determines if item should be hidden
   isHidden : function () {
		var isCompleted = this.model.get('completed');
		return ( // hidden cases only
			(!isCompleted && app.WordFilter === 'completed')
			|| (isCompleted && app.WordFilter === 'active')
		);
   },

	togglecompleted: function() {
      this.model.toggle();
   },

	// Switch this view into `"editing"` mode, displaying the input field.
    edit: function() {
      this.$el.addClass('editing');
      this.$input.focus();
    },
    
	// Close the `"editing"` mode, saving changes to the todo.
   close: function() {
      var value = this.$input.val().trim();
      if ( value ) {
        this.model.save({ title: value });
      }
		else {
			this.clear()
		}
      this.$el.removeClass('editing');
   },
    
	// If you hit `enter`, we're through editing the item.
    updateOnEnter: function( e ) {
      if ( e.which === ENTER_KEY ) {
        this.close();
      }
    },
	 
	clear: function() {
		console.log('WordView clear called')
      this.model.destroy();
   }
  });
  
})();