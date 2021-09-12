function getAll() {
	$.ajax({
		url: "http://localhost:8080/customerDetails"
	}).then(function(data) {
		$('#customerTable').remove();
		$('body').append(arrayToTable(data));
	});
}

function getCustomerData() {

	$("#customerId").attr("required", true);
	$.ajax({
		url: "http://localhost:8080/customerDetails/" + $('#customerId').val()
	}).then(function(data) {
		$('#customerTable').remove();
		$('body').append(arrayToTable(data));
	});
}

function updateCustomerData() {
	$("#customerId").attr("required", true);
	$("#phoneNumber").attr("required", true);
	$("#status").attr("required", true);
	$("#updateError").html("").addClass("error-msg");
	$.ajax({
		url: 'http://localhost:8080/customerStatus/',
		dataType: 'json',
		type: 'patch',
		contentType: 'application/json',
		data: JSON.stringify({ "status": $("input[name='status']:checked").val(), "customerID": $('#customerId').val(), "phoneNumber": $('#phoneNumber').val() }),
		processData: false,
		success: function(data, textStatus, jQxhr) {
			$('#customerTable').remove();
			$('body').append(arrayToTable(data));
		},
		error: function(jqXhr, textStatus, errorThrown) {
			$("#updateError").html("Error - "+jqXhr.status +"!!").addClass("error-msg");
		}
	});
}

function arrayToTable(tableData) {
	var table = $('<table width="60%" id="customerTable"></table>');
	$(tableData).each(function(i, rowData) {
		var row = $('<tr></tr>');
		$(rowData).each(function(j, cellData) {
			row.append($('<td width="15%" align="center">' + cellData.customerID + '</td>'));
			row.append($('<td width="48%" align="center">' + cellData.phoneNumber + '</td>'));
			row.append($('<td align="center">' + cellData.status + '</td>'));
		});
		table.append(row);
	});
	return table;
}

$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});
	$("#getAll").click(function() {
		getAll();
	});
	$("#getCustomerData").click(function() {
		getCustomerData();
	});
	$("#updateCustomerData").click(function() {
		updateCustomerData();
	});
});