<?php
    $con=mysqli_connect("mysql1.000webhost.com","a8223871_admin","loba54dev","a8223871_special");
	
	$barid = $_POST["barid"];
	$day = $_POST["day"];
	$description = $_POST["description"];
	$addedby = $_POST["addedby"];
	$date = $_POST["date"];
	
	$statement = mysqli_prepare($con, "INSERT INTO BarSpecials (bar_id, dayofweek, special, addedby, dateforspecial) VALUES (?, ?, ?, ?, ?)");
	mysqli_stmt_bind_param($statement, "issss", $barid, $day, $description, $addedby, $date);
	mysqli_stmt_execute($statement);
	
	mysqli_stmt_close($statement);
	
	mysqli_close($con);
?>