function submitForm(formId, target, url) {
    $.ajax({// create an AJAX call...
        data: $('#' + formId).serialize(), // get the form data
        type: $('#' + formId).attr('method'), // GET or POST
        url: url, // the file to call
        success: function (response) { // on success..
            $('#' + target).html(response); // update the DIV
        }
    });
    return false; // cancel original event to prevent form submitting
}

function submitFormSynchronous(formId, url) {
    $("#" + formId).attr("action", url).submit();
}

function asyncLoadFragment(target, url) {
    $.ajax({
        type: "POST",
        url: url,
        success: function (response) { // on success..
            $('#' + target).html(response); // update the DIV
        }
    });
    return false; // cancel original event to prevent form submitting
}

const debounce = (func, delay) => {
    console.log("debounce");
    let debounceTimer;
    return function() { 
        console.log("debounce");
        const context = this;
        const args = arguments;
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => func.apply(context, args), delay);
    };
};