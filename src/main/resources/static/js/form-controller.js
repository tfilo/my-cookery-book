function submitForm(formId, target, url) {
    $.ajax({// create an AJAX call...
        data: $('#' + formId).serialize(), // get the form data
        type: $('#' + formId).attr('method'), // GET or POST
        url: url, // the file to call
        success: function (response) { // on success..
            $('#' + target).html(response); // update the DIV
            $('body').scrollTop(($('#' + target).offset().top - $("#header").height()) - $("#searchDiv").height());
        },
        error: function (err) {
            processError(err);
        }
    });
    return false; // cancel original event to prevent form submitting
}

function asyncLoadFragment(target, url) {
    $.ajax({
        type: "POST",
        url: url,
        success: function (response) { // on success..
            $('#' + target).html(response); // update the DIV
            $('body').scrollTop(($('#' + target).offset().top - $("#header").height()) - $("#searchDiv").height());
        },
        error: function (err) {
            processError(err);
        }
    });
    return false; // cancel original event to prevent form submitting
}

function uploadPicture(formId, fileInputId, target, url) {
    var form = $('#' + formId)[0];
    var formData = new FormData(form);
    formData.append('picture', $('#' + fileInputId)[0].files[0]);

    $.ajax({
        type: "POST",
        data: formData,
        url: url,
        contentType: false,
        processData: false,
        success: function (response) { // on success..
            $('#' + target).html(response); // update the DIV
            $('body').scrollTop(($('#' + target).offset().top - $("#header").height()) - $("#searchDiv").height());
        },
        error: function (err) {
            processError(err);
        }
    });
    return false; // cancel original event to prevent form submitting
}

function load(target, url, data) {
    $.ajax({
        type: "GET",
        data: data ? data : null,
        url: url,
        success: function (response) { // on success..
            $('#' + target).html(response); // update the DIV
            $('body').scrollTop(($('#' + target).offset().top - $("#header").height()) - $("#searchDiv").height());
        },
        error: function (err) {
            processError(err);
        }
    });
}

const debounce = (func, delay) => {
    let debounceTimer;
    return function() { 
        const context = this;
        const args = arguments;
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => func.apply(context, args), delay);
    };
};

function processError(err) {
    var message;
    if (err && err.responseJSON && err.responseJSON.message) {
        message = err.responseJSON.message;
    } else {
        message = "General error!";
    }
    $('body').scrollTop(($('#error').offset().top - $("#header").height()) - $("#searchDiv").height());
    $('#errorMessage').html(message);
    $('#error').show(1000);
    $('#error').focus();
    
    setTimeout(function() {
        $('#error').hide(1000);
    }, 5000);
}