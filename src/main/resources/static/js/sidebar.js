function openSidebar() {
    document.getElementById("mySidebar").style.display = "block";
}

function closeSidebar() {
    document.getElementById("mySidebar").style.display = "none";
}

$(document).ready(function () {
    $("#categoryLinks a").click(function (event) {
        event.preventDefault();
        var id = $(this).attr('href');
        $('html, body').animate({
            scrollTop: $(id).offset().top - $("#header").height()
        }, 1000);
    });
});