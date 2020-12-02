$(document).ready(function () {

	console.log(sessionStorage.getItem('isLogin'));
	if (sessionStorage.getItem('isLogin') === null) {
		alert('You are not login yet! Login first!');
		document.location.href = '/Koperasi/login.html';
	}
	
	var valnopin, valnoa, valtglpin, valpokpin, valbupin, vallapin, valapok, valabu, valacp, valpage;
	var loadanggota, loadkaryawan;


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
	function loadAnggota() {
		loadanggota = 1;
		$.ajax({
			url: "/Koperasi/CtAnggota", 
			method: "GET", 
			dataType: "json",
			success:
			function(data){
				$("#tabelookupanggota").dataTable({
					serverside: true,
					processing: true,
					data: data,
					sort: true,
					searching: true,
					paging: true,
					columns: [
						{data: 'noAnggota', name: 'noAnggota', 'type': 'string'},
						{data: 'nama'},
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

	// Lookup data anggota
    $("#btnlua").click(function() {
        $("#modalLookupAnggota").modal('show');
        if (loadanggota != 1) {
            loadAnggota();
        }
        $("#tabelookupanggota tbody").on('click', '#btnInsertAnggota', function() {
            // get nik when clicked btn in the current row
            let baris = $(this).closest('tr');
            let no = baris.find("td:eq(0)").text();
            let nama = baris.find("td:eq(1)").text();
            $("#fanggota").val(no);
            $("#fanggotanm").val(nama);
            $("#modalLookupAnggota").modal("hide");
        });
    });

    // get nama anggota
    $('#fanggota').blur(function () {
	    var noa = $('#fanggota').val();
	    // console.log(noa);
	    $.post('/Koperasi/CtAnggota', {
	        page: "show",
		    noa: noa
	     },
	     function(data, status) {
	     	$('#fanggotanm').val(data.nama);
	     	if ($('#fanggotanm').val() === "") {
	     		alert('Nomor Anggota Not Found !')
	     	}
	     });
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
            $("#facp").val(no);
            $("#fkarnm").val(nama);
            $("#modalLookupKaryawan").modal("hide");
        });
    });

    // get nama karyawan
    $('#facp').blur(function () {
	    var nik = $('#facp').val();
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
		valtglpin = $("#ftglpinjam").val();
		valpokpin = $("#fpokpin").val();
		valbupin = $("#fbupin").val();
		vallapin = $("#flapin").val();
		valapok = $("#fapok").val();
		valabu = $("#fabu").val();
		valacp = $("#facp").val();
	}

	// save data
    $("#btnsave").on('click', function(){
		getVal();
		if (valnopin === "") {
	        alert("Nomor Pinjaman Harus Diisi!!");
	        $("#fnop").focus();
		}
		else if (valtglpin === "") {
	        alert("Tanggal Pinjaman Harus Diisi!!");
	        $("#ftglpinjam").focus();
		} else if (valnoa === "") {
			alert("Nomor Anggota Harus Diisi!!");
	        $("#fanggota").focus();
		} else if (valacp === "") {
			alert("Nomor Petugas Harus Diisi!!");
	        $("#facp").focus();
	    } else if (valpokpin === "") {
			alert("Pokok Pinjaman Harus Diisi!!");
	        $("#fpokpin").focus();
	    } else if (vallapin === "") {
			alert("Lama Pinjaman Harus Diisi!!");
	        $("#flapin").focus();
	    } else {
	        $.post("/Koperasi/CtPinjaman", {
				page: valpage,
				nopin: valnopin,
				noa: valnoa,
				tglpin: valtglpin,
		        lapin: vallapin,
				pokpin: valpokpin,
				bupin: valbupin,
				apok: valapok,
				abu: valabu,
		        acp: valacp

            }, function(data, status) {
                alert(data);
            	location.reload();
        	});
    	}
    });

	// procedure add data
    $("#btnadd").click(function(){
        $("#formPinjam").modal('show');
        clear();
        $("#fnop").prop('disabled', false);
    	$("#ftglpinjam").prop('disabled', false);
        valpage = "add";
        // console.log("add");
    });

    // procedure edit data
	$("#tabelpinjaman tbody").on("click", "#btnEdit", function() {
    $("#formPinjam").modal('show');
    $("#fnop").prop('disabled', true);
    $("#ftglpinjam").prop('disabled', true);
    valpage = "show";
    
    let baris = $(this).closest('tr');
    let np = baris.find("td:eq(0)").text();
    let a = baris.find("td:eq(2)").text();
    let k = baris.find("td:eq(9)").text();
    $.post('/Koperasi/CtPinjaman', {
        page: valpage,
	    nopin: np
     },
     function(data, status) {
	    // console.log(data);
	    $("#fnop").val(data.nopin);
	    $("#fanggota").val(data.noa);
	    $("#fanggotanm").val(a);
	    $("#ftglpinjam").val(data.tglpin);
	    $("#fpokpin").val(data.pokpin);
	    $("#fbupin").val(data.bupin);
	    $("#flapin").val(data.lapin);
	    $("#fapok").val(data.apok);
	    $("#fabu").val(data.abu);
	    $("#facp").val(data.acp);
	    $("#fkarnm").val(k);
	    $("#dpokpin").val(toRupiah(data.pokpin));
	    $("#dbupin").val(toRupiah(data.bupin));
	    $("#dapok").val(toRupiah(data.apok));
	    $("#dabu").val(toRupiah(data.abu));
     });
     valpage = "edit";
    
     });

	// get all data  pinjaman
    $.ajax({
        url: "/Koperasi/CtPinjaman", 
        method: "GET", 
        dataType: "json",
        success: function(data){
        	// console.log(data);
            $("#tabelpinjaman").dataTable({
	            serverside: true,
	            processing: true,
	            data: data,
	            sort: true,
	            searching: true,
	            paging: true,
	            columns: [
	                {'data': 'nopin'},
	                {'data': 'noa'},
	                {'data': 'nmang'},
	                {'data': 'tglpin'},
	                {'data': 'pokpin'},
	                {'data': 'bupin'},
	                {'data': 'lapin'},
	                {'data': 'apok'},
	                {'data': 'abu'},
	                {'data': 'nmpet'},
	                {'data': null, 'className': 'text-center', 'mRender': function(o) {
	                        return "<a class='btn btn-outline-info btn-sm'"
	                        + "id = 'btnEdit'>Edit</a>"
	                        + " | "
	                        + "<a class='btn btn-outline-danger btn-sm' "
	                        + "id='btnDel'>Delete</a>";
	                    }
	                }
                ]
            });       
        }
    });

    // procedure delete data
	$('#tabelpinjaman tbody').on('click', '#btnDel', function() {
		// get nik when clicked btn in the current row
		let baris = $(this).closest('tr');
		let np = baris.find("td:eq(0)").text();
		let nama = baris.find("td:eq(1)").text();
		valpage = 'delete';
		console.log(np);
		if (confirm(`Anda yakin data  : ${np} akan dihapus ?`)) {
			$.post("/Koperasi/CtPinjaman", {
			page: valpage,
			nopin: np
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
	    $("#ftglpinjam").val("");
	    $("#fpokpin").val("");
	    $("#fbupin").val("");
	    $("#flapin").val("");
	    $("#fapok").val("");
	    $("#fabu").val("");
	    $("#facp").val("");
	    $("#fkarnm").val("");
	}

	$('#fnop').blur(function () {
		$.post('/Koperasi/CtPinjaman', {
			page: "show",
			nopin: this.value
		}, function (data, status) {
			if (JSON.stringify(data) != "{}") {
			// console.log(data);
				alert('Nomor Pinjaman : ' + data.nopin + ' already used');
			}
		});
	});

	function hitungPinjaman(lamaPinjam, pokokPinjam, persen) {
		if (lamaPinjam != "" && pokokPinjam != "") {
			let bunga = Math.ceil(lamaPinjam/12 * pokokPinjam * persen/100);
			let apok = Math.ceil(pokokPinjam / lamaPinjam / 100) * 100;
			let abu = Math.ceil(bunga / lamaPinjam / 100) * 100;
			if (!isNaN(bunga)) {
				$('#fbupin').val(bunga);
				$('#dbupin').val(toRupiah(bunga));
			}
			if (!isNaN(apok)) {
				$('#fapok').val(apok);
				$('#dapok').val(toRupiah(apok));
			}
			if (!isNaN(abu)) {
				$('#fabu').val(abu);
				$('#dabu').val(toRupiah(abu));
			}
		}
	}

	$('#fpokpin').keyup(function () {
		$('#dpokpin').val(toRupiah($('#fpokpin').val()));
		hitungPinjaman($('#flapin').val(), this.value, $('#fperbu').val());
	});

	$('#flapin').keyup(function () {
		if (this.value > 12) {
			alert('max month is 12');
		}
		hitungPinjaman(this.value, $('#fpokpin').val(), $('#fperbu').val());
	});

	$('#fperbu').keyup(function () {
		// console.log(this.value);
		hitungPinjaman($('#flapin').val(), $('#fpokpin').val(), this.value);
	});


});