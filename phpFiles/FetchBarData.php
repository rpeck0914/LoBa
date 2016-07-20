<?php
    $con=mysqli_connect("mysql1.000webhost.com","a8223871_admin","loba54dev","a8223871_special");
	
	$barid = $_POST["barid"];
	
	$statement = mysqli_prepare($con, "SELECT * FROM BarDetails WHERE bar_id = ?");
	mysqli_stmt_bind_param($statement, "i", $barid);
	mysqli_stmt_execute($statement);
	
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $barID, $cityID, $barname, $baraddress, $barzipcode, $phone);
	
	$bars = array();
	
	while(mysqli_stmt_fetch($statement)) {
		$bars["name"] = $barname;
		$bars["address"] = $baraddress;
		$bars["zipcode"] = $barzipcode;
		$bars["phone"] = $phone;
	}
	
	echo json_encode($bars);
	
	mysqli_close($con);
?>