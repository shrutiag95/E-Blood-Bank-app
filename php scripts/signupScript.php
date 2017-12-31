<?php

$con=mysqli_connect("localhost","root","","bloodbank");
    $name = $_POST['name'];
	$email = $_POST['email'];
	$username=$_POST['uname'];
    	$password=$_POST['pass'];
	$phone= $_POST['phone'];
	$address = $_POST['add'];
	$sex = $_POST['sex'];
	$dob = $_POST['dob'];
	
  


    
	
$sql = "INSERT INTO `user_table` (`name`, `email`, `password`, `phone`, `address`, `gender`, `bloodgroup`, `uid`, `username`, `credits`, `last_donated`, `dob`) VALUES
		 ('$name', '$email', '$password', '$phone', '$address', '$sex', 'NULL', 'verified', '$username', '25', 'NULL', '$dob');";
    

	$rs=mysqli_query($con,$sql);
	if($rs)
		print"Registered";
	else
		print"Error";



?>


