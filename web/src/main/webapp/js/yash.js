jQuery(document).ready(function(){
	
	jQuery('.obj-type-selector').keyup(function(){
		var autoComplete = jQuery(this).parent().children('.autocomplete').eq(0);	
		autoComplete.hide();
		var fullUrl = rootUrl + '/admin/search/objtype';
		var value = jQuery(this).val();
		jQuery.ajax({
			'url': fullUrl,
			'data': {'name': value},
			'dataType': 'json',
			'type': 'POST',
			'success': function(data){
				autoComplete.empty();
				var items = [];
				jQuery.each(data, function(key, value){
					items.push('<li>' + value + '</li>');
				});
				autoComplete.append(items.join(''));
				autoComplete.show();
			}
		});
	});
});