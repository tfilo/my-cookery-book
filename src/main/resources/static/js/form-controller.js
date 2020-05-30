var loaderSemaphore = 0;
var showLoaderTimer = null;

function showLoader() {
    loaderSemaphore++;
    if (showLoaderTimer) {
        clearInterval(showLoaderTimer);
    }
    showLoaderTimer = setTimeout(_showLoader, 250);
}

function _showLoader() {
    $('#loaderOverlay').show();
}

function hideLoader() {
    loaderSemaphore--;
    if (loaderSemaphore === 0) {
        clearInterval(showLoaderTimer);
        $('#loaderOverlay').hide();
    }   
}

function submitForm(formId, target, url, scroll) {
    showLoader();
    $.ajax({// create an AJAX call...
        data: $('#' + formId).serialize(), // get the form data
        type: $('#' + formId).attr('method'), // GET or POST
        url: url, // the file to call
        success: function (response) { // on success..
            $('#' + target).html(response); // update the DIV
            if (scroll===true) {
                $(window).scrollTop(($('#' + target).offset().top - $("#header").outerHeight()) - $("#searchDiv").outerHeight());
            }
            hideLoader();
        },
        error: function (err) {
            hideLoader();
            processError(err);
        }
    });
    return false; // cancel original event to prevent form submitting
}

function uploadPicture(formId, fileInputId, target, url) {
    showLoader();
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
            hideLoader();
        },
        error: function (err) {
            hideLoader();
            processError(err);
        }
    });
    return false; // cancel original event to prevent form submitting
}

function load(target, url, data, scroll) {
    showLoader();
    $.ajax({
        type: "GET",
        data: data ? data : null,
        url: url,
        success: function (response) { // on success..
            $('#' + target).html(response); // update the DIV
            if (scroll===true) {
                $(window).scrollTop(($('#' + target).offset().top - $("#header").outerHeight()) - $("#searchDiv").outerHeight());
            }
            hideLoader();
        },
        error: function (err) {
            hideLoader();
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
    $('body').scrollTop(($('#error').offset().top - $("#header").outerHeight()) - $("#searchDiv").outerHeight());
    $('#errorMessage').html(message);
    $('#error').show(1000);
    $(window).scrollTop(0);
    
    setTimeout(function() {
        $('#error').hide(1000);
    }, 10000);
}