$(document).ready(function () {

	console.log(sessionStorage.getItem('isLogin'));
	if (sessionStorage.getItem('isLogin') === null) {
		alert('You are not login yet! Login first!');
		document.location.href = '/Koperasi/login.html';
	}
	
	var valnopin, valnoa, valake, valtglangsur, valbea, valsipin, valnmanggota, valnmkaryawan, valnokar, valpage;
	var loadpinjaman, loadkaryawan;
	const today = new Date().toLocaleDateString().split('/');
	const dtoday = today[2] + '-' + today[0] + '-' + today[1];


	// Format Rupiah
	function toRupiah(angka) {
		var reverse = angka.toString().split('').reverse().join(''),
   			ribuan = reverse.match(/\d{1,3}/g);
   			ribuan = ribuan.join('.').split('').reverse().join('');
 
		// if (ribuan) {
		// 	separator = sisa ? '.' : '';
		// 	rupiah += separator + ribuan.join('.');
		// }

		return "Rp " + ribuan;
	}

	// load data anggota
	function loadPinjaman() {
		loadpinjaman = 1;
		$.ajax({
			url: "/Koperasi/CtPinjaman", 
			method: "GET", 
			dataType: "json",
			success:
			function(data){
				$("#tabelookupPinjaman").dataTable({
					serverside: true,
					processing: true,
					data: data,
					sort: true,
					searching: true,
					paging: true,
					columns: [
						{data: 'nopin', name: 'nopinjaman', type: 'string'},
						{data: 'noa', name: 'noanggota', type: 'string'},
						{data: 'nmang'},
						{data: 'tglpin'},
						{data: null, 'className': 'dt-right', 'mRender': function(o){
							return "<a class='btn btn-warning btn-sm'"
							+ "id = 'btnInsertAnggota'>Insert</a>";
							}
						}
					]
				});

			},
			error: function(err) {
			console.log(err);
			}
		});
	}

	// show data
	function displayAngsur(nopin) {
	    $.post('/Koperasi/CtAngsuran', {
	    	page: "check",
	    	nopin: nopin,
	    }, function (data, status) {
	    	console.log(data);
	    	$('#fangsur').val(data);
	    });
	    // console.log(noa);
	    $.post('/Koperasi/CtPinjaman', {
	        page: "show",
		    nopin: nopin
	     },
	     function(data, status) {
	     	if ($('#fangsur').val() > data.lapin) {
	     		alert("Angsuran Sudah Lunas !!");
	     		clear();
	     	} else {
		     	$('#fanggota').val(data.noa);
		     	$('#fanggotanm').val(data.nmang);
		     	if ($('#fanggota').val() === "") {
		     		alert('Nomor Pinjaman Not Found !')
		     	}
	     		let bea = parseFloat(data.apok) + parseFloat(data.abu);
		     	let sipin = (parseFloat(data.pokpin) + parseFloat(data.bupin)) - (($('#fangsur').val() -1) * bea);
		     	if (data.lapin === $('#fangsur').val()) {
		     		bea = sipin;
		     		$('#peringatan').prop('hidden', false);
		     	}
		     	console.log(bea);
		    	console.log("sipin : " + sipin);
		    	$('#fbea').val(bea);
		    	$('#fsipin').val(sipin);
		    	$('#dbea').val(toRupiah(bea));
		    	$('#dsipin').val(toRupiah(sipin));
	     	}
	     });

	}

	// Lookup data anggota
    $("#btnlup").click(function() {
        $("#modalLookupPinjaman").modal('show');
        if (loadpinjaman != 1) {
            loadPinjaman();
        }
        $("#tabelookupPinjaman tbody").on('click', '#btnInsertAnggota', function() {
            // get nik when clicked btn in the current row
            let baris = $(this).closest('tr');
            let nop = baris.find("td:eq(0)").text();
            $("#fnop").val(nop);
            displayAngsur(nop);
            $("#modalLookupPinjaman").modal("hide");
        });
    });

    // get nama anggota
    $('#fnop').blur(function () {
    	var nop = $('#fnop').val();
    	displayAngsur(nop);
    });

    // load data karyawan
    function loadKaryawan() {
    	loadkaryawan = 1;
		$.ajax({
		    url: "/Koperasi/CtKaryawan", 
		    method: "GET", 
		    dataType: "json",
		    success:
	        function(data){
	            $("#tabelookupkaryawan").dataTable({
	            serverside: true,
	            processing: true,
	            data: data,
	            sort: true,
	            searching: true,
	            paging: true,
	            columns: [
	                    {data: 'nik', name: 'nik', 'type': 'string'},
	                    {data: 'nama'},
	                    {data: null, 'className': 'dt-right', 'mRender': function(o){
	                            return "<a class='btn btn-warning btn-sm'"
	                            + "id = 'btnInsertKaryawan'>Insert</a>";
	                        }
	                    }
	                ]
	            });

	        }
	 	});
	}

	// Lookup data karyawan
    $("#btnluk").click(function() {
        $("#modalLookupKaryawan").modal('show');
        if (loadkaryawan != 1) {
            loadKaryawan();
        }
        $("#tabelookupkaryawan tbody").on('click', '#btnInsertKaryawan', function() {
            // get nik when clicked btn in the current row
            let baris = $(this).closest('tr');
            let no = baris.find("td:eq(0)").text();
            let nama = baris.find("td:eq(1)").text();
            $("#fkaryawan").val(no);
            $("#fkarnm").val(nama);
            $("#modalLookupKaryawan").modal("hide");
        });
    });

    // get nama karyawan
    $('#fkaryawan').blur(function () {
	    var nik = $('#fkaryawan').val();
	    // console.log(noa);
	    $.post('/Koperasi/CtKaryawan', {
	        page: "show",
		    nik: nik
	     },
	     function(data, status) {
	     	$('#fkarnm').val(data.nama);
	     	if ($('#fkarnm').val() === "") {
	     		alert('Nomor Petugas Not Found !')
	     	}
	     });
    });
        

	// get value form field
	function getVal() {
		valnopin = $("#fnop").val();
		valnoa = $("#fanggota").val();
		valtglangsur = $("#ftglpinjam").val();
		valbea = $("#fbea").val();
		valsipin = $("#fsipin").val();
		valnokar = $("#fkaryawan").val();
		valake = $("#fangsur").val();
	}

	// save data
    $("#btnsave").on('click', function(){
		getVal();
		console.log(valnopin);
		console.log(valake);
		console.log(valtglangsur);
		console.log(valbea);
		console.log(valsipin);
		console.log(valpage);

		if (valnopin === "") {
	        alert("Nomor Pinjaman Harus Diisi!!");
	        $("#fnop").focus();
		} else if (valnokar === "") {
			alert("Nomor Karyawan Harus Diisi!!");
	        $("#fkaryawan").focus();
		} else if (valtglangsur === "") {
	        alert("Tanggal Harus Diisi!!");
	        $("#ftglpinjam").focus();
	    } else {
	        $.post("/Koperasi/CtAngsuran", {
				page: valpage,
				nopin: valnopin,
				tglangsur: valtglangsur,
		        sipin: valsipin,
				bea: valbea,
		        nokar: valnokar,
		        angsurke: valake,
        	}, function(data, status) {
                alert(data);
            	location.reload();
            });
    	}
    });

	// procedure add data
    $("#btnadd").click(function(){
        $("#formAngsur").modal('show');
        clear();
        $("#fnop").prop('disabled', false);
    	$("#ftglpinjam").val(dtoday);
        valpage = "add";
        console.log(valpage);
    });

    // procedure edit data
	$("#tabelangsur tbody").on("click", "#btnEdit", function() {
	    $("#formAngsur").modal('show');
	    $("#fnop").prop('disabled', true);
	    valpage = "show";
	    
	    let baris = $(this).closest('tr');
	    let np = baris.find("td:eq(0)").text();
	    let a = baris.find("td:eq(1)").text();
	    $.post('/Koperasi/CtAngsuran', {
	        page: valpage,
		    nopin: np,
		    angsurke: a,
	    },
	    function(data, status) {
		    // console.log(data);
		    $("#fnop").val(data.nopin);
		    $("#fangsur").val(data.ake);
		    $("#fanggota").val(data.noa);
		    $("#fanggotanm").val(data.nmanggota);
		    $("#ftglpinjam").val(data.tglangsur);
		    $("#fbea").val(data.bea);
		    $("#fsipin").val(data.sipin);
		    $("#fkarnm").val(data.nmkar);
		    $("#fkaryawan").val(data.nokar);
		    $("#dsipin").val(toRupiah(data.sipin));
		    $("#dbea").val(toRupiah(data.bea));
	    });
	    valpage = "edit";
    	console.log(valpage);
     });

	// get all data  pinjaman
    $.ajax({
        url: "/Koperasi/CtAngsuran", 
        method: "GET", 
        dataType: "json",
        success: function(data){
        	// console.log(data);
            $("#tabelangsur").dataTable({
	            serverside: true,
	            processing: true,
	            data: data,
	            sort: true,
	            searching: true,
	            paging: true,
	            columns: [
	                {data: 'nopin'},
	                {data: 'ake'},
	                {data: 'noa'},
	                {data: 'nmanggota'},
	                {data: 'bea'},
	                {data: 'sipin'},
	                {data: 'nmkar'},
	                {data: 'isLast', defaultContent: '', className: 'text-center', orderable: false,
	                	render: {
	                		display: function (data, type, row) {
	                			console.log('isLast : ' + row.isLast);
	                			if (row.isLast) {
	                				return "<a class='btn btn-outline-info btn-sm'"
			                        + "id = 'btnEdit' disabled='true'>Edit</a>"
			                        + " | "
			                        + "<a class='btn btn-outline-danger btn-sm' "
			                        + "id='btnDel' disabled='true'>Delete</a>";
	                			}
	                		}
	                	}
	                }
                ]
            });       
        }
    });

    // procedure delete data
	$('#tabelangsur tbody').on('click', '#btnDel', function() {
		// get nik when clicked btn in the current row
		let baris = $(this).closest('tr');
		let np = baris.find("td:eq(0)").text();
		let angsurke = baris.find("td:eq(1)").text();
		valpage = 'delete';
		console.log(np);
		if (confirm(`Anda yakin data  : ${np} akan dihapus ?`)) {
			$.post("/Koperasi/CtAngsuran", {
			page: valpage,
			nopin: np,
			angsurke: angsurke
			},
			function(data, status) {
				// console.log(data);
				alert(data);
				location.reload();
			});

		}
	});

	function clear() {
		$("#fnop").val("");
	    $("#fanggota").val("");
	    $("#fanggotanm").val("");
	    $("#fbea").val("");
	    $("#fsipin").val("");
	    $("#fangsur").val("");
	    $("#fkaryawan").val("");
	    $("#fkarnm").val("");
	}



});