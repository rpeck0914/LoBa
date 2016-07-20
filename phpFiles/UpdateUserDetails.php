<?php
	$con=mysqli_connect("mysql1.000webhost.com","a8223871_admin","loba54dev","a8223871_special");
	
	$username = $_POST["username"];
	$password = $_POST["password"];
	$state = $_POST["state"];
	$city = $_POST["city"];
	$cityid = $_POST["cityid"];
	
	$statement = mysqli_prepare($con, "UPDATE UserDetails SET state = ?, city = ?, city_id = ? WHERE username = ? AND password = ?");
	mysqli_stmt_bind_param($statement, "ssiss", $state, $city, $cityid, $username, $password);
	mysqli_stmt_execute($statement);
	
	mysqli_stmt_close($statement);
	mysqli_close($con);
?>