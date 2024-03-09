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

function deleteContact(currentPage, cId) {
  swal(
    {
      title: "Are you sure?",
      text: "You will not be able to recover this file!",
      type: "warning",
      showCancelButton: true,
      confirmButtonColor: "#DD6B55",
      confirmButtonText: "Yes",
      cancelButtonText: "No",
      closeOnConfirm: false,
      closeOnCancel: false,
    },
    function (isConfirm) {
      if (isConfirm) {
        window.location = "/user/contact-delete/" + currentPage + "/" + cId;
      } else {
        swal("Cancelled", "Your file is safe :)", "error");
      }
    }
  );
}

const search = () => {
  // console.log("searching...");
  let query = $("#search-input").val();
  if (query == "") {
    $(".search-result").hide();
  } else {
    // console.log(query);
    let url = `http://localhost:8080/user/search/${query}`;
    fetch(url)
      .then((response) => {
        return response.json();
      })
      .then((data) => {
        let text = `<div class='list-group'>`;
        data.forEach((contact) => {
          text += `<a href='/user/contact/${contact.cId}' class='list-group-item list-group-action'>${contact.cNickName}</a>`;
        });
        text += `</div>`;
        $(".search-result").html(text);
        $(".search-result").show();
      })
      .catch((error) => {
        console.error("There was a problem with the fetch operation: ", error);
      });
  }
};
