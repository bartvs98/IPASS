//-------------------------------------------------------------------------------------------------------------------
//When the page is done loading.
if(window.sessionStorage.getItem("sessionToken") != null){
  $(document).ready(function(){
    initPage();

    $("#rekeningContainer").hide();
    $("#holder").hide();
    $("#newRekeningForm").hide();
    $("#addBeleggingForm").hide();
    $("#chartContainer").hide();
    $("#totalChartContainer").hide();
    $("#userInfo").hide();
    $("#userArrow").html("keyboard_arrow_down")
    $("#userName").html(window.sessionStorage.getItem("userName"))

    window.localStorage.clear();

    $(document).tooltip();
  })
} else {
  window.location.href = "../";
}

//-------------------------------------------------------------------------------------------------------------------
//Function which launches on startup and loads all rekeningen.
function initPage(){
  var userID =  window.sessionStorage.getItem("userID")
  var uri = "restservices/rekeningen/" + userID;

  $.ajax(uri, {
      method: "get",
      beforeSend: function (xhr) {
        var token = window.sessionStorage.getItem("sessionToken");
        xhr.setRequestHeader( 'Authorization', 'Bearer ' + token);
      },
    success: function(rekeningen) {
      for (var i = 0; i < rekeningen.length; i++) {
        window.localStorage.setItem(rekeningen[i].rekeningnr, JSON.stringify(rekeningen[i]));

        $("#rekeningen").append("<div style='display:inline-grid;'>" +
                                "<div class='rekeningBox' id='rekeningBox'>" +
                                "<b><div id='rekeningnr'>" + rekeningen[i].rekeningnr +
                                "</div></b>" +
                                "€ " + rekeningen[i].saldo + "</div> " +
                                "<div class='rekeningBox' id='beleggingBox' style='margin-top:0;'>" +
                                rekeningen[i].depotnaam + "</div></div>");
      }
    },
    error: function(response) {
      window.location.href = "../";
    }
  });
}

//-------------------------------------------------------------------------------------------------------------------
//Slides down the userinfo div onclick.
$(document).on("click", "#showUserInfo", function(){
  var hidden = $("#userInfo").is(":hidden");
  $("#userInfo").slideToggle();

  if (hidden == false){
    $("#userArrow").html("keyboard_arrow_down")
  } else {
    $("#userArrow").html("keyboard_arrow_up")
  }
})

//-------------------------------------------------------------------------------------------------------------------
//Function to hide rekeningContainer.
$(document).on("click", "#hideDiv1", function(){
  $("#hideDiv1").parent().parent().hide()
  var hidden1 = $("#chartContainer").is(":hidden")
  var hidden2 = $("#beleggingContainer").is(":hidden")
  var hidden3 = $("#rekeningContainer").is(":hidden")
  var hidden4 = $("#totalChartContainer").is(":hidden")

  if(hidden1 == true && hidden2 == true && hidden3 == true && hidden4 == true){
    $("#perspectiveText").show();
  }
})

//-------------------------------------------------------------------------------------------------------------------
//Function to hide beleggingContainer.
$(document).on("click", "#hideDiv2", function(){
  $("#hideDiv2").parent().parent().parent().hide()
  var hidden1 = $("#chartContainer").is(":hidden")
  var hidden2 = $("#beleggingContainer").is(":hidden")
  var hidden3 = $("#rekeningContainer").is(":hidden")
  var hidden4 = $("#totalChartContainer").is(":hidden")

  if(hidden1 == true && hidden2 == true && hidden3 == true && hidden4 == true){
    $("#perspectiveText").show();
  }
})

//-------------------------------------------------------------------------------------------------------------------
//Function to hide chartContainer.
$(document).on("click", "#hideDiv3", function(){
  $("#hideDiv3").parent().parent().hide()
  $("#showChart").removeClass("selectedChart")
  var hidden1 = $("#chartContainer").is(":hidden")
  var hidden2 = $("#beleggingContainer").is(":hidden")
  var hidden3 = $("#rekeningContainer").is(":hidden")
  var hidden4 = $("#totalChartContainer").is(":hidden")

  if(hidden1 == true && hidden2 == true && hidden3 == true && hidden4 == true){
    $("#perspectiveText").show();
  }
})

//-------------------------------------------------------------------------------------------------------------------
//Function to hide totalChartContainer.
$(document).on("click", "#hideDiv4", function(){
  $("#hideDiv4").parent().parent().hide()
  $("#showChart").removeClass("selectedChart")
  var hidden1 = $("#chartContainer").is(":hidden")
  var hidden2 = $("#beleggingContainer").is(":hidden")
  var hidden3 = $("#rekeningContainer").is(":hidden")
  var hidden4 = $("#totalChartContainer").is(":hidden")

  if(hidden1 == true && hidden2 == true && hidden3 == true && hidden4 == true){
    $("#perspectiveText").show();
  }
})

//-------------------------------------------------------------------------------------------------------------------
//Popup which can be used for confirmation.
function popUp(){
  $("#popUp").css("display", "block");
  $("#overlay").css("display", "block");

  $(document).on("click", "#cancelButton", function(){
    closePopUp();
  });
}

//-------------------------------------------------------------------------------------------------------------------
//Logout popup is called
$(document).on("click", "#logoutButton", function(){
  $("#msg").html("Weet u zeker dat u uit wilt loggen?");
  popUp()
})

//-------------------------------------------------------------------------------------------------------------------
//Actual logout function.
function logout(){
  window.sessionStorage.clear();
  window.localStorage.clear();
  window.location.href = "../";
}

//-------------------------------------------------------------------------------------------------------------------
//Check if user confirms action.
$(document).on("click", "#confirmButton", function(){
  var msg = $("#msg").html();
  if (msg == "Weet u zeker dat u deze belegging toe wilt voegen?"){
    addBelegging();
  }
  if (msg == "Weet u zeker dat u deze rekening wilt verwijderen? <br> (Let op! ook het gekoppelde depot wordt verwijderd!)"){
    deleteRekening();
  }
  if (msg == "Weet u zeker dat u deze belegging wilt verwijderen?"){
    deleteBelegging();
  }
  if (msg == "Weet u zeker dat u uit wilt loggen?"){
    logout();
  }
  if (msg == "Weet u zeker dat u deze koersverandering toe wilt voegen?"){
    addKoers();
  }
  if (msg == "Weet u zeker dat u deze transactie wilt doen?"){
    addTransaction();
  }
});

//-------------------------------------------------------------------------------------------------------------------
//Function to close the popup.
function closePopUp(){
  $("#popUp").css("display", "none");
  $("#overlay").css("display", "none");
  $("#msg").html("");
}

//-------------------------------------------------------------------------------------------------------------------
//Function to generate central chart for all depots.
$(document).on("click", "#showTotalChart", function(){
  var userID =  window.sessionStorage.getItem("userID")
  var uri = "restservices/beleggingen/sumKoersverandering/" + userID;

  $.ajax(uri, {
      method: "get",
      beforeSend: function (xhr) {
        var token = window.sessionStorage.getItem("sessionToken");
        xhr.setRequestHeader( 'Authorization', 'Bearer ' + token);
      },
    success: function(totals) {
      $("#perspectiveText").hide();
      $("#totalChartContainer").show();

      var datasets = [];


      datasets.push({
        label: "Totalen over alle depots",
        data: totals,
        borderColor: "#E040FB",
        backgroundColor: "#E040FB",
        pointBorderColor: "black",
        pointHoverBorderColor: "#E040FB",
        pointBorderWidth: 2,
        fill: false,
        tension: false
      });

      chart(datasets, ".totalChart");
    }
  });
})
