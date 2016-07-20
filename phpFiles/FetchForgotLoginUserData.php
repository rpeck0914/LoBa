<?php
    $con=mysqli_connect("mysql1.000webhost.com","a8223871_admin","loba54dev","a8223871_special");
      
    $email = $_POST["email"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM UserDetails WHERE email = ?");
    mysqli_stmt_bind_param($statement, "s", $email);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID, $username, $email, $password, $city, $state, $cityID);
    
    $user = array();
    
    while(mysqli_stmt_fetch($statement)){
        $user["username"] = $username;
        $user["password"] = $password;
    }
    
    echo json_encode($user);
    mysqli_close($con);
?>
