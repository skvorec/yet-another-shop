jQuery(document).ready(function(){
    
    jQuery('.need-confirm').click(function(e){
        if(!confirm('Вы уверены?')){
            e.preventDefault();
        }        
    });
	
    jQuery('.obj-type-name-selector').keyup(function(){       
        var hiddenInputWithId = jQuery(this).parent().children('.obj-type-id-selector').eq(0);        
        var autoCompleteDiv = jQuery(this).parent().children('.autocomplete-container').eq(0);        
        var fullUrl = rootUrl + '/admin/search/objtype';
       
        sEntitySelector(jQuery(this), hiddenInputWithId, autoCompleteDiv, fullUrl);        
    });
    
    jQuery('.attr-name-selector').keyup(function(){
        var hiddenInputWithId = jQuery(this).parent().children('.attr-id-selector').eq(0);        
        var autoCompleteDiv = jQuery(this).parent().children('.autocomplete-container').eq(0);        
        var fullUrl = rootUrl + '/admin/search/attribute';
       
        sEntitySelector(jQuery(this), hiddenInputWithId, autoCompleteDiv, fullUrl);        
    });
    
    jQuery('#new-obj-type-button').click(function(e){
        e.preventDefault();
        jQuery.ajax({
            'url': rootUrl + '/admin/attrManager/newObjectType',
            'data' : {
                'name' :jQuery('#new-obj-type-name').val()
            },
            'dataType': 'json',
            'type': 'POST',
            'success': function(data){
                jQuery('.error-message').hide();
                if(!jQuery.isEmptyObject(data)){
                    console.log('data recieved');
                    jQuery.each(data, function(key, value){
                        console.log('key = ' + key + ' value= ' + value);
                        var htmlAsString = '<div>' +
                        '<a class="need-confirm" href="' + rootUrl + '/admin/attrManager/deleteObjType?objtype=' + key + '">' +
                        '<img class="icon-img" src="' + rootUrl + '/img/delete.png"/>'+
                        '</a>'+
                        '<a href="' +  rootUrl + '/admin/attrManager?objtype=' + key + '">' + value +'</a>' +
                        '</div>';                       
                        jQuery(htmlAsString).prependTo(jQuery('#all-obj-types'));
                    });
                } else{
                    jQuery('<div class="error-message"><span>Тип с таким именем уже существует!</span></div>').prependTo(jQuery('#all-obj-types'));
                }
            }
        });
    });
});

function sEntitySelector(jQueryInput, jQueryHiddenInput, autoCompleteDiv, searchUrl){
    jQueryHiddenInput.val('');
    var value = jQueryInput.val();
    if(value != ''){
        autoCompleteDiv.removeClass('hidden');
    }else{
        autoCompleteDiv.addClass('hidden');
    }    
    autoCompleteDiv.css('top:' + jQueryInput.position().top);
    autoCompleteDiv.css('left:'+ jQueryInput.position().left);  
    
    
    jQuery.ajax({
        'url': searchUrl,
        'data': {
            'name': value
        },
        'dataType': 'json',
        'type': 'POST',
        'success': function(data){
            autoCompleteDiv.empty();
            if(!jQuery.isEmptyObject(data)){
                jQuery.each(data, function(key, value){
                    jQuery('<div class="autocomplete-item"></div>').text(value).appendTo(autoCompleteDiv).click(function(){
                        jQueryInput.val(value);
                        jQueryHiddenInput.val(key);
                        autoCompleteDiv.addClass('hidden');
                    });
                });
            }else{
                jQuery('<div class="autocomplete-item"></div>').text('Ничего не найдено').appendTo(autoCompleteDiv);
            }
        }
    });   
}