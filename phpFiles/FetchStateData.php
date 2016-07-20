<?php
    $con=mysqli_connect("mysql1.000webhost.com","a8223871_admin","loba54dev","a8223871_special");
	
	$statement = mysqli_prepare($con, "SELECT * FROM StateDetails");
	mysqli_stmt_execute($statement);
	
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $stateID, $statename);
	
	$states = array();
	
	while(mysqli_stmt_fetch($statement)) {
		$state[$stateID] = $statename;
	}
	
	echo json_encode($state);
	
	mysqli_close($con);
?>