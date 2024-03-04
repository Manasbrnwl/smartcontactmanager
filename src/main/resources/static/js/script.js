const toggleSidebar = () => {
  if ($(".sidebar").is(":visible")) {
    $(".sidebar").css("display", "none");
    $(".content").css("margin-left", "0%");
    $(".content").css("width", "100%");
  } else {
    $(".sidebar").css("display", "block");
    $(".content").css("margin-left", "15%");
    $(".content").css("width", "85%");
  }
};
