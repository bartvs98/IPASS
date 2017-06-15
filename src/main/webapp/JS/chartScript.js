//-------------------------------------------------------------------------------------------------------------------
//Building the chart with the given data.
function chart(datasets, div){
  var ctx = document.querySelector(div).getContext("2d");
  new Chart(ctx, {
  	type: 'line',
  	data: {
  		datasets: datasets
  	},
  	options: {
      animation: {
        duration: 0,
  		},
      tooltips: {
        backgroundColor: "#2196F3",
        bodySpacing: 5
      },
  		responsive: true,
            title:{
                display:true,
                text:"Totale waarde"
            },
  		scales: {
  			xAxes: [{
  				type: "time",
  				display: true,
  				scaleLabel: {
  					display: true,
  					labelString: 'Datum'
  				}
  			}],
  			yAxes: [{
  				display: true,
  				scaleLabel: {
  					display: true,
  					labelString: 'Waarde'
  				},
          stacked: false,
          responsive: true,
          maintainAspectRatio: false
  			}],
  		}
  	}
  });
}
