Quill.register('modules/counter', function (quill, options) {
    var container = document.querySelector(options.container);
    quill.on('text-change', function () {
        var text = quill.getText();
        if (options.unit === 'word') {
            container.innerText = text.split(/\s+/).length + ' words';
        } else {
            container.innerText = (text.length - 1) + ' characters';
        }
    });
});

Quill.register('modules/textarea', function (quill, options) {
    var container = document.querySelector(options.container);
    var containerText = document.querySelector(options.containerText);
    quill.on('text-change', function () {
        var json = JSON.stringify(quill.getContents());
        container.setAttribute('value', json);
        containerText.setAttribute('value', quill.getText());
    });
});

Quill.register('modules/clipboard', QuillPasteSmart, true);

function getQuillEditConfiguration(counterId, inputTargetId) {
    return {
        modules: {
            toolbar: [
                ['bold', 'italic', 'underline'],
                [{'list': 'ordered'}, {'list': 'bullet'}],
                [{'indent': '-1'}, {'indent': '+1'}],
                [{'align': []}],
                ['clean']
            ],
            counter: {
                container: counterId,
                unit: 'character'
            },
            textarea: {
                container: inputTargetId,
                containerText: inputTargetId + "Text"
            }
        },
        theme: 'snow'
    };
}

var quillReadOnlyConfiguration = {
    readOnly: true,
    modules: {
        toolbar: false
    },
    theme: 'snow'
};

