<?php
$server  ="localhost";
$username_host = "root";
$pass_host = "";
$db = "bloodbank";

$con = new mysqli($server, $username_host, $pass_host, $db);
if($con->connect_error){
    die("Connection Failed");

}

$bloodgrp = $_POST['bloodgrp'];
$quantity = $_POST['quantity'];

$sql = "select * from `bank_table` where `$bloodgrp`>='$quantity' ;" ;
$rs = mysqli_query($con, $sql);
while(($row=mysqli_fetch_assoc($rs))!=null){
    $output[]=$row;
}

//print_r($output);
print(json_encode(array("banks"=>$output)));





if(mysqli_num_rows($rs)==0)
        echo "No banks matching your requirement";
?>

