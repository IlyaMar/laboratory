var app = app || {};

(function () {
  'use strict';

  // Word collection
  var WordList = Backbone.Collection.extend({
    // Reference to this collection's model.
    model: app.Word,
    // Save all of the todo items under the `"todos-backbone"` namespace.
    // Note that you will need to have the Backbone localStorage plug-in
    // loaded inside your page in order for this to work. If testing
    // in the console without this present, comment out the next line
    // to avoid running into an exception.
    localStorage: new Backbone.LocalStorage('words-backbone'),
    
	// Filter down the list of all words that are finished.
    completed: function() {
      return this.filter(function( word ) {
        return word.get('completed');
      });
    },
    
	// Filter down the list to only words that are still not finished.
    remaining: function() {
    // apply allowsus to define the context of this within our function scope
      return this.without.apply( this, this.completed() );
    },
    
	// We keep the Todos in sequential order, despite being saved by unordered
    // GUID in the database. This generates the next order number for new items.
    nextOrder: function() {
      if ( !this.length ) {
        return 1;
      }
      return this.last().get('order') + 1;
    },
	
    // Todos are sorted by their original insertion order.
	comparator: function( todo ) {
      return todo.get('order');
    },
	
	statistics: function() {
		var size = this.size();
		//var 
	    //this.collection.each(function(log) {
			//console.log('log item.', log);
		//});
	
	}
  });
  // Create our global collection of words.
  app.Words = new WordList();
  
})();