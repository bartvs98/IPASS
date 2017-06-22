//-------------------------------------------------------------------------------------------------------------------
//Shows addBeleggingForm on click.
$(document).on("click", "#showAddBeleggingForm", function(e){
  $("#aandeelKoers").removeClass("redLine");
  $("#aantalAandelen").removeClass("redLine");
  $("#aandeelTotaal").removeClass("redLine");
  $("#aandeelNaam").removeClass("redLine");

  if ($("#addBeleggingForm").is(":hidden") == true){
    $("#showAddBeleggingForm").addClass("selectedNewBelegging");
  } else {
    $("#showAddBeleggingForm").removeClass("selectedNewBelegging");
  }

  $("#addBeleggingForm").clearQueue();
  $("#addBeleggingForm").slideToggle();
  e.preventDefault();

  var today = new Date();
  var dd = today.getDate();
  var mm = today.getMonth()+1;

  var yyyy = today.getFullYear();
  if(dd<10){
      dd='0'+dd;
  }
  if(mm<10){
      mm='0'+mm;
  }
  var today = yyyy+'-'+mm+'-'+dd;
  $("#beleggingDate").val(today);
})

//-------------------------------------------------------------------------------------------------------------------
//Shows addKoersForm on click.
$(document).on("click", "#showAddKoersForm", function(){
  $(this).parent().find(".addKoersForm").slideToggle();
})

$(document).on("click", "#showAddKoersForm2", function(){
  $(this).parent().slideToggle();
})

//-------------------------------------------------------------------------------------------------------------------
//Add koers confirmation popup.
$(document).on("click", "#addNewKoers", function(){
  aandeelNaam = $(this).parent().parent().find("#aandeelNaam").text();
  koersDate = $(this).parent().find("#koersDate").val();
  newKoers = parseFloat($(this).parent().find("#newKoers").val());
  totaalNewKoers = parseFloat($(this).parent().find("#totaalNewKoers").val());

  $("#msg").html("Weet u zeker dat u deze koersverandering toe wilt voegen?");
  popUp();
})

//-------------------------------------------------------------------------------------------------------------------
//Function to add koers data.
function addKoers(){
  var data = {"naam": aandeelNaam,
              "datum": koersDate,
              "koers": newKoers,
              "totaal": totaalNewKoers
  };

  var JSONdata = JSON.stringify(data);
  var uri = "restservices/beleggingen/addKoers/" + aandeelNaam;

  $.ajax(uri, {
      method: "post",
      data: JSONdata,
      beforeSend: function (xhr) {
        var token = window.sessionStorage.getItem("sessionToken");
        xhr.setRequestHeader( 'Authorization', 'Bearer ' + token);
      },
    success: function(response) {
      window.location.reload();
    },
  });
}

//-------------------------------------------------------------------------------------------------------------------
//Shows addKoersForm on click.
$(document).on("click", "#showTransactionFrom", function(){
  $(this).parent().find(".transactionForm").slideToggle();
})

//-------------------------------------------------------------------------------------------------------------------
//Transaction confirmation popup.
$(document).on("click", "#addTransaction", function(){

  //Get all needed values to make a transaction.
  transactionID = parseInt($(this).parent().parent().find(".beleggingID").text());
  transactionDate = $(this).parent().find("#transactionDate").val();
  currentAantal = parseInt($(this).parent().parent().find("#aandelenAantal").text());
  currentTotaal = $(this).parent().parent().find("#beleggingTotaal").text();
  currentTotaal = currentTotaal.replace("€ ", "");
  currentTotaal = parseFloat(currentTotaal);
  transactionAantal = parseInt($(this).parent().find("#transactionAantal").val());
  transactionKoers = parseFloat($(this).parent().find("#transactionKoers").val());
  transactionTotaal = parseFloat($(this).parent().find("#transactionTotaal").val());
  rekeningnr = $(this).parent().parent().find("#rekeningNr").text();
  transactionType = $(this).parent().find("input[name=transaction]:checked").val();

  $("#msg").html("Weet u zeker dat u deze transactie wilt doen?");
  popUp();
})

//-------------------------------------------------------------------------------------------------------------------
//Function to post transaction
function addTransaction(){

  //Change post data based on radiobutton.
  if(transactionType == "verkoop"){
    var newAantal = currentAantal - transactionAantal;
    var newTotaal = currentTotaal - transactionTotaal;

    var data = {"id": transactionID,
                "datum": transactionDate,
                "aandelenAantal": newAantal,
                "koers": transactionKoers,
                "totaal": newTotaal,
                "bedrag": transactionTotaal * -1,
                "rekeningnr": rekeningnr
    };
  } else {
    var newAantal = currentAantal + transactionAantal;
    var newTotaal = currentTotaal + transactionTotaal;

    var data = {"id": transactionID,
                "datum": transactionDate,
                "aandelenAantal": newAantal,
                "koers": transactionKoers,
                "totaal": newTotaal,
                "bedrag": transactionTotaal,
                "rekeningnr": rekeningnr
    };
  }

  var JSONdata = JSON.stringify(data);
  var uri = "restservices/beleggingen/verkoop/" + transactionID;

  $.ajax(uri, {
      method: "post",
      data: JSONdata,
      beforeSend: function (xhr) {
        var token = window.sessionStorage.getItem("sessionToken");
        xhr.setRequestHeader( 'Authorization', 'Bearer ' + token);
      },
    success: function(response) {
      window.location.reload();
    },
  });
}

//-------------------------------------------------------------------------------------------------------------------
//Function to get individual depot data.
$(document).on("click", "#beleggingBox", function(){
  $("#perspectiveText").hide();
  var depotnaam = $(this).html();
  var rekeningnr = $(this).parent().find("#rekeningnr").html();
  var uri = "restservices/rekeningen/" + rekeningnr;
  var saved = JSON.parse(window.localStorage.getItem(rekeningnr))

  $("#holder").show();
  $("#beleggingTable").html("");

  localData = JSON.parse(window.localStorage.getItem(rekeningnr));
  displayBeleggingData(localData);
  displayRekeningData(localData);
})

//-------------------------------------------------------------------------------------------------------------------
//Function to display the beleggings data.
function displayBeleggingData(data){
  $("#chartContainer").hide();
  $("#showChart").removeClass("selectedChart")
  $("#beleggingTitle").html(data.depotnaam);
  $("#aandeelContainer").html("")
  $("#beleggingRekeningNr").val(data.rekeningnr);

  //Dynamicly building the same divs depending on the ammount of aandelen. (not verry proud of the way its done)
  for (var i = 0; i < data.belegging.length; i++) {
    $("#aandeelContainer").append('<div class="mainContainer" id="tableDiv">' +
                        "<i id='deleteBelegging' class='material-icons trashBeleggingButton' title='Verwijder belegging'>delete</i>" +
                        '<i id="showAddKoersForm" class="material-icons showFormButton2" title="Nieuwe koersverandering">add</i>' +
                        '<i id="showTransactionFrom" class="material-icons showFormButton2" title="Nieuwe transactie">attach_money</i>' +

                        '<div class="transactionForm" style="padding-bottom:20px;">' +
                        '<span class="inputSpan">Datum:</span><input type="date" id="transactionDate" class="transactionDate">' +
                        '<span class="inputSpan">Aantal:</span><input type=number id="transactionAantal" class="transactionAantal" value=0>' +
                        '<span class="inputSpan">Huidige koers:</span><input type="number" id="transactionKoers" class="transactionKoers" value=0>' +
                        '<span class="inputSpan">Totaal:</span><input type="number" id="transactionTotaal" class="transactionTotaal" value=0>' +
                        '<div class="Radio">' +
                          '<span class="inputSpan">Koop/Verkoop</span>' +
                          '<ul>' +
                            '<li>' +
                              '<input type="radio" id="option1" name="transaction" value="koop" checked>' +
                              '<label for="option1">koop</label>' +
                              '<div class="check"><div class="inside"></div></div>' +
                            '</li>' +
                            '<li>' +
                            '<input type="radio" id="option2" name="transaction" value="verkoop">' +
                              '<label for="option2">Verkoop</label>' +
                              '<div class="check"><div class="inside"></div></div>' +
                            '</li>' +
                          '</ul>' +
                        '</div>' +
                        '<i id="addTransaction" class="material-icons confirmFormButton" title="Transactie doorvoeren">done</i>' +
                        '<i id="showAddKoersForm2" class="material-icons hideDivButton" title="Annuleren">clear</i>' +
                        '</div>' +

                        '<div class="addKoersForm" style="padding-bottom:20px;">' +
                        '<span class="inputSpan">Datum:</span><input type="date" id="koersDate" class="koersDate">' +
                        '<span class="inputSpan">Nieuwe koers:</span><input type="number" id="newKoers" class="newKoers" value=0>' +
                        '<span class="inputSpan">Totaal:</span><input type="number" id="totaalNewKoers" value=0>' +
                        '<i id="addNewKoers" class="material-icons confirmFormButton" title="Toevoegen">done</i>' +
                        '<i id="showAddKoersForm2" class="material-icons hideDivButton" title="Annuleren">clear</i>' +
                        '</div>' +

                        '<table id="aandeelTableHolder" ">' +
                          '<thead>' +
                            '<tr>' +
                              '<th>Datum</th>' +
                              '<th>Naam</th>' +
                              '<th>Aantal</th>' +
                              '<th>Totaal</th>' +
                            '</tr>' +
                          '</thead>' +
                          '<tbody id="beleggingTable">' +
                              "<tr>" +
                                "<td>" + data.belegging[i].datum + "</td>" +
                                "<td id='aandeelNaam'>" + data.belegging[i].naam + "</td>" +
                                "<td id='aandelenAantal'>" + data.belegging[i].aantal + "</td>" +
                                "<td id='beleggingTotaal'>€ " + data.belegging[i].totaal + "</td>" +
                                "<td style='visibility:hidden;' class='beleggingID'>" + data.belegging[i].id + "</td>" +
                                "<td style='visibility:hidden;' id='rekeningNr'>" + data.rekeningnr + "</td>" +
                            '<table style="margin-top:20px;">' +
                              '<thead>' +
                                '<tr>' +
                                  '<th>Datum</th>' +
                                  '<th>Nieuwe Koers</th>' +
                                  '<th>Totaal</th>' +
                                '</tr>' +
                              '</thead>' +
                              '<tbody id="koersTable' + data.belegging[i].id + '"class="test">' +
                              '</tbody>' +
                            '</table>' +
                          '</tbody>' +
                        '</table>' +
                      '</div>');

      //koersveranderings table.
      for (var k = 0; k < data.belegging[i].koersverandering.length; k++) {
        $("#koersTable" + data.belegging[i].id).append("<tr><td>" + data.belegging[i].koersverandering[k].datum + "</td>" +
                               "<td id='" + data.belegging[i].id + "'>" + data.belegging[i].koersverandering[k].koers + "</td>" +
                               "<td>" + data.belegging[i].koersverandering[k].totaal + "</td>" +
                               '<td> <i id="deleteKoersverandering" style="margin:0; font-size:25px;" class="material-icons trashButton">delete</i></td>'+
                               "<td style='visibility:hidden;' id='koersID'>" + data.belegging[i].koersverandering[k].id + "</td></tr>");
      }

      //Building objects for the chart datasets.
      allArray = [];

      $("[id^='koersTable']").each(function(){
        var table = $(this);
        objArray = [];

        table.children("tr").each(function(){
          var obj = {
            x: $(this).children("td:nth-child(1)").text(),
            y: $(this).children("td:nth-child(3)").text()
          }
          objArray.push(obj);
        });

        var tableObj = {
          id: table.attr("id"),
          value: objArray
        };
        allArray.push(tableObj);
      });
  };

  $(".addKoersForm").hide();
  $(".transactionForm").hide();

  //Calculating the total value on keyup.
  $(".newKoers").keyup(function(){
    var koers = $(this).parent().find("#newKoers").val();
    var aantal = $(this).parent().parent().find("#aandelenAantal").text();
    $(this).parent().find("#totaalNewKoers").val(koers * aantal);
  })

  $(".transactionKoers").keyup(function(){
    var koers = $(this).parent().find("#transactionKoers").val();
    var aantal = $(this).parent().find("#transactionAantal").val();
    $(this).parent().find("#transactionTotaal").val(koers * aantal);
  })

  $(".transactionAantal").keyup(function(){
    var koers = $(this).parent().find("#transactionKoers").val();
    var aantal = $(this).parent().find("#transactionAantal").val();
    $(this).parent().find("#transactionTotaal").val(koers * aantal);
  })

  var today = new Date();
  var dd = today.getDate();
  var mm = today.getMonth()+1;

  var yyyy = today.getFullYear();
  if(dd<10){
      dd='0'+dd;
  }
  if(mm<10){
      mm='0'+mm;
  }
  var today = yyyy+'-'+mm+'-'+dd;
  $(".koersDate").val(today);
  $(".transactionDate").val(today);
}

//-------------------------------------------------------------------------------------------------------------------
//Function to determine total value
$(function(){
  $("#aantalAandelen").keyup(function(){
    var koers = $("#aandeelKoers").val();
    var aantal = $("#aantalAandelen").val();
    $("#aandeelTotaal").val(koers * aantal);
  })
})

$(function(){
  $("#aandeelKoers").keyup(function(){
    var koers = $("#aandeelKoers").val();
    var aantal = $("#aantalAandelen").val();
    $("#aandeelTotaal").val(koers * aantal);
  })
})

//-------------------------------------------------------------------------------------------------------------------
//Function to delete one individual koersverandering.
$(document).on("click", "#deleteKoersverandering", function(){
  koersid = $(this).parent().parent().find("#koersID").text();
  $("#msg").html("Weet u zeker dat u deze koersverandering wilt verwijderen?");
  popUp();
})

function deleteKoersverandering(){
  var uri = "restservices/beleggingen/deleteKoersverandering/" + koersid;

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

//-------------------------------------------------------------------------------------------------------------------
//Function to delete one individual belegging.
$(document).on("click", "#deleteBelegging", function(){
  id = $(this).parent().find(".beleggingID").text();
  idTotaal = $(this).parent().find("#beleggingTotaal").text();
  idTotaal = parseFloat(idTotaal.replace("€ ", ""));
  $("#msg").html("Weet u zeker dat u deze belegging wilt verwijderen?");
  popUp();
})

//-------------------------------------------------------------------------------------------------------------------
//Delete rekening function.
function deleteBelegging(){
  var rekeningnr = $("#beleggingRekeningNr").val();
  var uri = "restservices/beleggingen/deleteBelegging/" + id + "/" + idTotaal + "/" + rekeningnr;

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

//-------------------------------------------------------------------------------------------------------------------
//Add belegging confirmation popup.
$(document).on("click", "#addNewBelegging", function(){
  var koers = $("#aandeelKoers").val();
  var aantal = $("#aantalAandelen").val();
  var totaal = $("#aandeelTotaal").val();
  var naam = $("#aandeelNaam").val();

  if (koers == 0 && aantal == 0 && totaal == 0){
    $("#aandeelKoers").addClass("redLine");
    $("#aantalAandelen").addClass("redLine");
    $("#aandeelTotaal").addClass("redLine");
  } else if (naam == "") {
    $("#aandeelNaam").addClass("redLine");
  } else {
    $("#msg").html("Weet u zeker dat u deze belegging toe wilt voegen?");
    popUp();
  }
});

//-------------------------------------------------------------------------------------------------------------------
//Function to add belegging data.
function addBelegging(){
  var array = [];
  var inputVal = $("#addBeleggingForm").find("input");

  for (var i = 0; i < inputVal.length; i++) {
    var val = $(inputVal[i]).val();
    if(val != ""){
      array.push(val);
      rekeningnr = array[0];
      datum = array[1];
      koers = parseFloat(array[2]);
      aantal = parseInt(array[3])
      totaal = parseFloat(array[4]);
      koop = parseInt("1")
      naam =  array[5];
    } else {
      console.log("One or more fields are empty!");
    }
  }

  if(val != ""){
    var data = {"rekeningnr": rekeningnr,
                "datum": datum,
                "koers": koers,
                "aantal": aantal,
                "totaal": totaal,
                "koop": koop,
                "naam": naam
    };

    var JSONdata = JSON.stringify(data);
    var uri = "restservices/beleggingen/addBelegging/" + rekeningnr;

    $.ajax(uri, {
        method: "post",
        data: JSONdata,
        beforeSend: function (xhr) {
          var token = window.sessionStorage.getItem("sessionToken");
          xhr.setRequestHeader( 'Authorization', 'Bearer ' + token);
        },
      success: function(response) {
        window.location.reload();
      },
    });
  }
}

//-------------------------------------------------------------------------------------------------------------------
//Function to pass datasets to the chart.
$(document).on("click", "#showChart", function(){
  $(".rendementChart").remove();
  $("#chartHolder").append('<canvas style="display:block;" class="rendementChart"></canvas>')
  $("#chartContainer").show();

  if ($("#chartContainer").is(":hidden") == false){
    $("#showChart").addClass("selectedChart")
  }

  var aandelenArray = [];
  var datasets = [];
  var colorArray = ["#76FF03", "#FFC107", "#448AFF", "#651FFF", "#E040FB"]

  $('#beleggingTable tr td:nth-child(2)').each( function(){
   aandelenArray.push($(this).text());
  });

  var uniqueAandeelArray = aandelenArray.filter(function(elem, index, self) {
    return index == self.indexOf(elem);
  })

  // Building the chart datasets.
  for (var i = 0; i < uniqueAandeelArray.length; i++) {
    datasets.push({
      label: uniqueAandeelArray[i],
      data: allArray[i].value,
      borderColor: colorArray[i],
      backgroundColor: colorArray[i],
      pointBorderColor: "black",
      pointHoverBorderColor: colorArray[i],
      pointBorderWidth: 2,
      fill: false,
      tension: false
    });
  }

  chart(datasets, ".rendementChart");
})
