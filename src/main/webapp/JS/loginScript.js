//-------------------------------------------------------------------------------------------------------------------
//Press the login button on enterkey press.
$(document).bind('keypress', function(e) {
    if(e.keyCode==13){
         $('#confirmLogin').trigger('click');
     }
});

//-------------------------------------------------------------------------------------------------------------------
//Function to verify login.
$(document).on("click", "#confirmLogin", function(event){
  var array = [];
  var inputVal = $("#loginForm").find("input");

  for (var i = 0; i < inputVal.length; i++) {
    var val = $(inputVal[i]).val();

    if(val != ""){
      array.push(val);
      username = array[0];
      password = array[1];
    }
  }

  if(val != ""){
    var data = {"username": username,
                "password": password
              }

    var JSONdata = JSON.stringify(data);

    $.post("restservices/authentication", JSONdata, function(response) {
      window.sessionStorage.setItem("sessionToken", response);
      window.sessionStorage.setItem("userName", username)
      var uri = "restservices/authentication/getUserID/" + username

      $.ajax(uri, {
          method: "get",
        success: function(response) {
          window.sessionStorage.setItem("userID", response)
          window.location.href = "homepage.html";
        },
      });
    }).fail(function(jqXHR, textStatus, errorThrown) {
      $(".inputSpan").css("color", "red");
      $("#loginForm input").css("border-bottom", "2px solid red");
      $("#msg").html("Verkeerde gebruikersnaam en/of wachtwoord.")
      popUp();
    });
  } else {
    $(".inputSpan").css("color", "red");
    $("#loginForm input").css("border-bottom", "2px solid red");
    $("#msg").html("Voer een gebruikersnaam en wachtwoord in.")
    popUp();
  }
})

//-------------------------------------------------------------------------------------------------------------------
//Popup which can be used for confirmation. (copied from mainScript)
function popUp(){
  $("#popUp").css("display", "block");
  $("#overlay").css("display", "block");

  $(document).on("click", "#confirmButton", function(){
    closePopUp();
  });
}

//-------------------------------------------------------------------------------------------------------------------
//Function to close the popup. (copied from mainScript)
function closePopUp(){
  $("#popUp").css("display", "none");
  $("#overlay").css("display", "none");
  $("#msg").html("");
}
