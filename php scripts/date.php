<?php
$con=mysqli_connect("localhost","root","","bloodbank");
$username=$_POST['username'];

$sql="select `last_donated` from `user_table` where `username`='$username';" ;
$rs=mysqli_query($con,$sql);

$row=mysqli_fetch_assoc($rs);
$db_date=$row['last_donated'];
$curr_date=date("Y-m-d");

$diff = abs(strtotime($curr_date) - strtotime($db_date));

$days= floor($diff/(60*60*24));

if($days>=90)
	echo "Eligible";
else
	echo "Not";


?>