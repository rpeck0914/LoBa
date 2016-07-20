<?php
    $con=mysqli_connect("mysql1.000webhost.com","a8223871_admin","loba54dev","a8223871_special");
	
	$stateid = $_POST["stateid"];
	//$stateid = 21;
	
	$statement = mysqli_prepare($con, "SELECT * FROM CityDetails WHERE state_id = ?");
	mysqli_stmt_bind_param($statement, "i", $stateid);
	mysqli_stmt_execute($statement);
	
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $cityID, $cityname, $stateID);
	
	$city = array();
	
	while(mysqli_stmt_fetch($statement)) {
		$city[$cityID] = $cityname;
	}
	
	echo json_encode($city);
	
	 mysqli_close($con);
?>