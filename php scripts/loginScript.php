<?php
$server  ="localhost";
$username_host = "root";
$pass_host = "";
$db = "bloodbank";

$con = new mysqli($server, $username_host, $pass_host, $db);
if($con->connect_error){
    die("Connection Failed");

}

$user = $_POST['username'];
$pass = $_POST['password'];

$sql = "select * from `user_table` where `username`='$user' and `password`='$pass';" ;
$rs = mysqli_query($con, $sql);

if(mysqli_num_rows($rs)>0)
        echo "Login successful";
    else
        echo "Login unsuccessful";
?>