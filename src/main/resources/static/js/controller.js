function submitForm(formId ,url) {
    $.ajax({ // create an AJAX call...
        data: $('#' + formId).serialize(), // get the form data
        type: $('#' + formId).attr('method'), // GET or POST
        url: url, // the file to call
        success: function(response) { // on success..
            $('#content').html(response); // update the DIV
        }
    });
    return false; // cancel original event to prevent form submitting
}