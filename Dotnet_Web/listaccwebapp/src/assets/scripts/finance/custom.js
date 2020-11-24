$(function () {
    "use strict";
    $(function () {
        //$(".preloader").fadeOut();
    }), jQuery(document).on("click", ".finance-app-layout .mega-dropdown", function (i) {
        i.stopPropagation();
    });
    var i = function () {
        (window.innerWidth > 0 ? window.innerWidth : this.screen.width) < 1170 ? ($("body.finance-app-layout").addClass("mini-sidebar"),
        $(".finance-app-layout .navbar-brand span").hide(), $(".finance-app-layout .scroll-sidebar, .slimScrollDiv").css("overflow-x", "visible").parent().css("overflow", "visible"),
        $(".finance-app-layout .sidebartoggler i").addClass("ti-menu")) : ($("body.finance-app-layout").removeClass("mini-sidebar"),
        $(".finance-app-layout .navbar-brand span").show());
        var i = (window.innerHeight > 0 ? window.innerHeight : this.screen.height) - 1;
        (i -= 70) < 1 && (i = 1), i > 70 && $(".finance-app-layout .page-wrapper").css("min-height", i + "px");
    };
    $(window).ready(i), $(window).on("resize", i), $(".finance-app-layout .sidebartoggler").on("click", function () {
        $("body.finance-app-layout").hasClass("mini-sidebar") ? ($("body.finance-app-layout").trigger("resize"), $(".finance-app-layout .scroll-sidebar, .slimScrollDiv").css("overflow", "hidden").parent().css("overflow", "visible"),
        $("body.finance-app-layout").removeClass("mini-sidebar"), $(".finance-app-layout .navbar-brand span").show()) : ($("body.finance-app-layout").trigger("resize"),
        $(".finance-app-layout .scroll-sidebar, .slimScrollDiv").css("overflow-x", "visible").parent().css("overflow", "visible"),
        $("body.finance-app-layout").addClass("mini-sidebar"), $(".finance-app-layout .navbar-brand span").hide());
    }), $(".finance-app-layout .fix-header .topbar").stick_in_parent({}), $(".finance-app-layout .nav-toggler").click(function () {
        $("body.finance-app-layout").toggleClass("show-sidebar"), $(".finance-app-layout .nav-toggler i").toggleClass("mdi mdi-menu"),
        $(".finance-app-layout .nav-toggler i").addClass("mdi mdi-close");
    }), $(".finance-app-layout .search-box a, .search-box .app-search .srh-btn").on("click", function () {
        $(".finance-app-layout .app-search").toggle(200);
    }), $(".finance-app-layout .right-side-toggle").click(function () {
        $(".finance-app-layout .right-sidebar").slideDown(50), $(".finance-app-layout .right-sidebar").toggleClass("shw-rside");
    }), $(".finance-app-layout .floating-labels .form-control").on("focus blur", function (i) {
        $(this).parents(".finance-app-layout .form-group").toggleClass("focused", "focus" === i.type || this.value.length > 0);
    }).trigger("blur"), $(function () {
        for (var i = window.location, o = $("ul#sidebarnav a").filter(function () {
            return this.href == i;
        }).addClass("active").parent().addClass("active") ; ;) {
            if (!o.is("li")) break;
            o = o.parent().addClass("in").parent().addClass("active");
        }
    }), $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    }), $(function () {
        $('[data-toggle="popover"]').popover();
    }), $(function () {
        $("#sidebarnav").metisMenu();
    }), $(".finance-app-layout .scroll-sidebar").slimScroll({
        position: "left",
        size: "5px",
        height: "100%",
        color: "#dcdcdc"
    }), $(".finance-app-layout .message-center").slimScroll({
        position: "right",
        size: "5px",
        color: "#dcdcdc"
    }), $(".finance-app-layout .aboutscroll").slimScroll({
        position: "right",
        size: "5px",
        height: "80",
        color: "#dcdcdc"
    }), $(".finance-app-layout .message-scroll").slimScroll({
        position: "right",
        size: "5px",
        height: "570",
        color: "#dcdcdc"
    }), $(".finance-app-layout .chat-box").slimScroll({
        position: "right",
        size: "5px",
        height: "470",
        color: "#dcdcdc"
    }), $(".finance-app-layout .slimscrollright").slimScroll({
        height: "100%",
        position: "right",
        size: "5px",
        color: "#dcdcdc"
    }), $("body.finance-app-layout").trigger("resize"), $(".finance-app-layout .list-task li label").click(function () {
        $(this).toggleClass("task-done");
    }), $("#to-recover").on("click", function () {
        $("#loginform").slideUp(), $("#recoverform").fadeIn();
    }), $('a[data-action="collapse"]').on("click", function (i) {
        i.preventDefault(), $(this).closest(".finance-app-layout .card").find('[data-action="collapse"] i').toggleClass("ti-minus ti-plus"),
        $(this).closest(".finance-app-layout .card").children(".finance-app-layout .card-body").collapse("toggle");
    }), $('a[data-action="expand"]').on("click", function (i) {
        i.preventDefault(), $(this).closest(".finance-app-layout .card").find('[data-action="expand"] i').toggleClass("mdi-arrow-expand mdi-arrow-compress"),
        $(this).closest(".finance-app-layout .card").toggleClass("card-fullscreen");
    }), $('a[data-action="close"]').on("click", function () {
        $(this).closest(".finance-app-layout .card").removeClass().slideUp("fast");
    });
});