<?php
    $con=mysqli_connect("mysql10.000webhost.com","a4937391_loba","loba54dev","a4937391_loba");
      
    $username = $_POST["username"];
	$password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM UserDetails WHERE username = ? AND password = ?");
    mysqli_stmt_bind_param($statement, "ss", $username, $password);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID, $name, $username, $password, $city, $state, $cityID);
    
    $user = array();
    
    while(mysqli_stmt_fetch($statement)){
        $user["name"] = $name;
        $user["username"] = $username;
        $user["password"] = $password;
		$user["city"] = $city;
		$user["state"] = $state;
		$user["cityid"] = $cityID;
    }
    
    echo json_encode($user);
    mysqli_close($con);
?>
