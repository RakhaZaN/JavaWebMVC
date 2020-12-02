$(document).ready(function () {

	console.log(sessionStorage.getItem('isLogin'));
	if (sessionStorage.getItem('isLogin') === null) {
		alert('You are not login yet! Login first!');
		document.location.href = '/Koperasi/login.html';
	}

	var valnik, valnama, valgender, valtmplahir, valtgllahir, valtelepon, valalamat, valpage;

	// get value field form
	function getInVal() {
		valnik = $('#fnik').val();
		valnama = $('#fnama').val();
		valtmplahir = $('#ftemplahir').val();
		valtgllahir = $('#ftgllahir').val();
		valtelepon = $('#ftelepon').val();
		valalamat = $('#falamat').val();
		valgender = $("input[name='gender']:checked");
		if (valgender.length > 0) {
			valgender = valgender.val();
		} else {
			valgender = "";
		}
	}

	// if btn add clicked
	$('#btnadd').on('click', function () {
        clear();
		$('#fnik').prop("disabled", false);
		valpage = "add";
	});

	// if btn edit clicked
	$("#tableKaryawan tbody").on("click", "#btnedit", function () {
		$("#fnik").prop("disabled", true);
		valpage = "show";

		var selected = $(this).closest("tr");
		var nik = selected.find("td:eq(0)").text();

		$.post("/Koperasi/CtKaryawan", {
			page: valpage,
			nik: nik
		}, function (data, status) {
			$('#fnik').val(data.nik);
			$("#fnama").val(data.nama);
			$('#ftemplahir').val(data.tmplahir);
			$('#ftgllahir').val(data.tgllahir);
			$('#ftelepon').val(data.telepon);
			$('#falamat').val(data.alamat);
			$("input[name=gender][value = " + data.gender + "]").prop("checked", true);
		});

		valpage = "edit";
	});

	// if btn delete clicked
	$("#tableKaryawan tbody").on("click", "#btndel", function () {
		var selected = $(this).closest("tr");
		var nik = selected.find("td:eq(0)").text();
		var nama = selected.find("td:eq(1)").text();
		valpage = "delete";

		if (confirm("Want to delete " + nik + " - " + nama + " ?")) {
			$.post("/Koperasi/CtKaryawan", {
			page: valpage,
			nik: nik
			}, function (data, status) {
				alert(data);
				location.reload();
			});
		}
	});

	// jika btn save pada modal di tekan
	$('#btnsave').on('click', function () {
		getInVal();
		if (valnik === "") {
			alert("NIK cannot be empty !");
			$('#fnik').focus();
		} else if (valnama === "") {
			alert("Nama cannot be empty !");
			$('#fnama').focus();
		} else {
			$.post("/Koperasi/CtKaryawan", {
				// parameter
				nik: valnik,
				nama: valnama,
				gender: valgender,
				tmplahir: valtmplahir,
				tgllahir: valtgllahir,
				telepon: valtelepon,
				alamat: valalamat,
				page: valpage
			}, function (data, status) {
				 alert(data);
				 location.reload();
			});
		}
	});
	
	// Mengambil data dari controller
	$.ajax({
		url: "/Koperasi/CtKaryawan",	// url controller.java
		method: "GET",
		dataType: "json",
		success: function (data) {
			$("#tableKaryawan").DataTable({
				serverside: true,
				processing: true,
				data: data,
				sort: true,
				searching: true,
				paging: true,
				columns: [	//	Memasukkan data perbaris sesuai urutan column
				{data: 'nik', name: 'nik', type: 'string'},
				{data: 'nama'},
				{data: 'gender'},
				{data: 'tmplahir'},
				{data: 'tgllahir'},
				{data: 'telepon'},
				{data: null, className: 'text-center', 'mRender': function(o) {
					return "<a class='btn btn-outline-info btn-sm' id='btnedit' "
					+ "data-toggle='modal' data-target='#exampleModalCenter'>Ubah</a> |"
					+" <a class='btn btn-outline-danger btn-sm' id='btndel'>Hapus</a>";
				}}
				]
			});
		}
	});

	function clear() {
		$('#fnik').val("");
		$("#fnama").val("");
		$("input[name='gender']").prop('checked', false);
		$('#ftemplahir').val("");
		$('#ftgllahir').val(null);
		$('#ftelepon').val("");
		$('#falamat').val("");
	}

});