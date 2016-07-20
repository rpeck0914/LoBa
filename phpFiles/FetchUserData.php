<?php
    $con=mysqli_connect("mysql1.000webhost.com","a8223871_admin","loba54dev","a8223871_special");
      
    $username = $_POST["username"];
	$password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM UserDetails WHERE username = ? AND password = ?");
    mysqli_stmt_bind_param($statement, "ss", $username, $password);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID, $username, $email, $password, $city, $state, $cityID);
    
    $user = array();
    
    while(mysqli_stmt_fetch($statement)){
        $user["username"] = $username;
        $user["email"] = $email;
        $user["password"] = $password;
		$user["city"] = $city;
		$user["state"] = $state;
		$user["cityid"] = $cityID;
    }
    
    echo json_encode($user);
    mysqli_close($con);
?>
