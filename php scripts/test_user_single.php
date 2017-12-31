<?php


$con=mysqli_connect("localhost","root","","bloodbank");
$username = $_POST['upost'];
//$username = 'pdg';
$sql="select * from `user_table` where username = '$username';";
$rs=mysqli_query($con,$sql);
//print_r(json_encode($rs));

while(($row=mysqli_fetch_assoc($rs))!=null){
    $output[]=$row;
}

// echo $output
//print_r($output);
echo(json_encode($output));
//echo gettype($output);
//echo gettype(json_encode($output));
// print_r(json_encode(array("profile"=>$output)));

?>
