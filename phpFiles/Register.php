<?php
    $con=mysqli_connect("mysql1.000webhost.com","a8223871_admin","loba54dev","a8223871_special");
    
    $username = $_POST["username"];
    $email = $_POST["email"];
    $password = $_POST["password"];
	$city = $_POST["city"];
	$state = $_POST["state"];
	$cityid = $_POST["cityid"];
		
    $statement = mysqli_prepare($con, "INSERT INTO UserDetails (username, email, password, city, state, city_id) VALUES (?, ?, ?, ?, ?, ?)");
    mysqli_stmt_bind_param($statement, "sssssi", $username, $email, $password, $city, $state, $cityid);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_close($statement);
    
    mysqli_close($con);
?>