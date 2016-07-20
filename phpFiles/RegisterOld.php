<?php
    $con=mysqli_connect("mysql10.000webhost.com","a4937391_loba","loba54dev","a4937391_loba");
    
    $name = $_POST["name"];
    $username = $_POST["username"];
    $password = $_POST["password"];
	$city = $_POST["city"];
	$state = $_POST["state"];
	$cityid = $_POST["cityid"];
		
    $statement = mysqli_prepare($con, "INSERT INTO UserDetails (name, username, password, city, state, city_id) VALUES (?, ?, ?, ?, ?, ?)");
    mysqli_stmt_bind_param($statement, "sssssi", $name, $username, $password, $city, $state, $cityid);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_close($statement);
    
    mysqli_close($con);
?>
