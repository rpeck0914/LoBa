<?php
    $con=mysqli_connect("mysql1.000webhost.com","a8223871_admin","loba54dev","a8223871_special");
	
	$barid = $_POST["barid"];
	
	//$barid = "1";
	
	$statement = mysqli_prepare($con, "SELECT * FROM BarSpecials WHERE bar_id = ?");
	mysqli_stmt_bind_param($statement, "s", $barid);
	mysqli_stmt_execute($statement);
	
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $specialId, $barId, $dayofweek, $special, $addedby, $dateforspecial);
	
	$barspecials = array();
	
	while(mysqli_stmt_fetch($statement)) {
		$barspecials[$specialId] = array("dayofweek" => $dayofweek,
										 "special" => $special,
										 "addedby" => $addedby,
										 "dateforspecial" => $dateforspecial);
	}
	
	echo json_encode($barspecials);
	
	mysqli_close($con);
?>