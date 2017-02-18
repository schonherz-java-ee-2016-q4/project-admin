$(document).ready(function () {
    $(window).resize(function () {
        var footerHeight = $('.footer').outerHeight();
        var stickFooterPush = $('.push').height(footerHeight);
        $('.custom_body').css({'marginBottom': '-' + footerHeight + 'px'});
    });

    $(window).resize();
});

function openNav() {
    document.getElementById("mySidenav").style.width = "70%";
    document.body.style.backgroundColor = "rgba(0,0,0,0.4)";
}

function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
    document.body.style.backgroundColor = "rgba(0,0,0,0)";
}