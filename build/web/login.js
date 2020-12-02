$(document).ready(function () {
console.log(sessionStorage.getItem('isLogin'));
	if (sessionStorage.getItem('isLogin')) {
		alert('You are already login');
		document.location.href = '/Koperasi/';
	}

	var valid, valpass;

	function getVal() {
		valid = $('#inId').val();
		valpass = $('#inPass').val();
	}

	$('#btnLogin').click(function () {
		getVal();
		if (valid == "") {
			alert('Input Your Id First !!');
		} else if (valpass == "") {
			alert('Input Your Password !!');
		} else {
			$.post('/Koperasi/CtUser', {
				page: 'login',
				userid: valid,
				pass: valpass
			}, function (data, status) {
				console.log(data);
				if (data === "failed") {
					alert('ID or Password is wrong !!');
					$('#inId').focus();
				} else if (data.aktif === "T") {
					alert('This Account was Not Active!');
					alert('Please Login with Active Account!');
					$('#inId').focus();
				} else {
					sessionStorage.setItem('isLogin', true);
					sessionStorage.setItem('dt', JSON.stringify(data));
					alert('Login Successfuly');
					document.location.href = '/Koperasi/';
				}
			});
		}
	});

});