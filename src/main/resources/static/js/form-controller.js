var loaderSemaphore = 0;
var showLoaderTimer = null;
var tooltipChange = null;

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
    $('#error').html(err.responseText);
    $(window).scrollTop(0);
    $('#error').show(1000);
    
    setTimeout(function() {
        $('#error').hide(1000);
    }, 10000);
}

function copyLink(link, tooltipId, copiedTitle) {
    var textarea = document.createElement("textarea");
    document.body.appendChild(textarea);
    textarea.value = link;
    textarea.select();
    document.execCommand("copy");
    document.body.removeChild(textarea);
  
    $('#' + tooltipId).text(copiedTitle);
}