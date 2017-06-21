//-------------------------------------------------------------------------------------------------------------------
//Shows newRekeningForm on click.
$(document).on("click", "#showNewRekeningForm", function(e){
  $("#newRekeningForm").clearQueue();
  $("#newRekeningForm").slideToggle();
  $("#newRekeningForm input").removeClass("redLine");
  e.preventDefault();
})

//-------------------------------------------------------------------------------------------------------------------
//Function to get individual rekening data.
$(document).on("click", "#rekeningBox", function(){
  $("#perspectiveText").hide();
  var rekeningnr = $(this).find("#rekeningnr").html();
  var uri = "restservices/rekeningen/" + rekeningnr;
  var saved = JSON.parse(window.localStorage.getItem(rekeningnr))

  $("#rekeningContainer").show();
  $("#bijafschriftTable").html("");

  localData = JSON.parse(window.localStorage.getItem(rekeningnr))
  displayRekeningData(localData);
  displayBeleggingData(localData);
})

//-------------------------------------------------------------------------------------------------------------------
//Function to display the rekeningen data.
function displayRekeningData(data){
  $("#bijafschriftTable").html("");
  $("#rekeningTitle").html(data.rekeningnr);
  $("#rekeningSaldo").html("€ " + data.saldo);

  for (var i = 0; i < data.bijafschrift.length; i++) {
    $("#bijafschriftTable").append("<tr><td>" + data.bijafschrift[i].datum + "</td>" +
                                   "<td>€ " + data.bijafschrift[i].bedrag + "</td></tr>");
  };
}

//-------------------------------------------------------------------------------------------------------------------
//Add rekening function.
$(document).on("click", "#addNewRekening", function(){
  var array = [];
  var inputVal = $("#newRekeningForm").find("input");
  var userID =  parseInt(window.sessionStorage.getItem("userID"))

  for (var i = 0; i < inputVal.length; i++) {
    var val = $(inputVal[i]).val();
    if(val != ""){
      array.push(val);
      rekeningnr = array[0];
      depotnaam = array[1];
      saldo = parseFloat(array[2]);
    } else {
      console.log("One or more fields are empty!");
    }
  }

  var rekeningnrCheck = JSON.parse(window.localStorage.getItem(rekeningnr));

  if (rekeningnrCheck == null){
    if($("newDepotNaam").val() != "" && $("#newRekeningnrInput").val() != ""){
      var data = {"userID": userID,
                  "rekeningnr": rekeningnr,
                  "depotnaam": depotnaam,
                  "saldo": saldo
      };

      var JSONdata = JSON.stringify(data);
      var uri = "restservices/rekeningen/" + rekeningnr;

      $.ajax(uri, {
          method: "post",
          data: JSONdata,
          beforeSend: function (xhr) {
            var token = window.sessionStorage.getItem("sessionToken");
            xhr.setRequestHeader( 'Authorization', 'Bearer ' + token);
          },
        success: function(response) {
          $("#newRekeningForm").slideToggle();
          initPage();
        },
      });
    } else {
      $("#newRekeningForm input").addClass("redLine");
    }
  } else {
    $("#newRekeningForm #newRekeningnrInput").addClass("redLine");
  }
})

//-------------------------------------------------------------------------------------------------------------------
//Delete rekening confirmation popup.
$(document).on("click", "#deleteRekening", function(){
  $("#msg").html("Weet u zeker dat u deze rekening wilt verwijderen? <br> (Let op! ook het gekoppelde depot wordt verwijderd!)");
  popUp();
})

//-------------------------------------------------------------------------------------------------------------------
//Delete rekening function.
function deleteRekening(){
  var rekeningnr = $("#deleteRekening").parent().parent().find("h1").html();
  var uri = "restservices/rekeningen/" + rekeningnr;

  $.ajax(uri, {
      method: "delete",
      beforeSend: function (xhr) {
        var token = window.sessionStorage.getItem("sessionToken");
        xhr.setRequestHeader( 'Authorization', 'Bearer ' + token);
      },
    success: function(response) {
      window.location.reload();
    },
    error: function(response) {
      console.log(response);
    }
  });
}
