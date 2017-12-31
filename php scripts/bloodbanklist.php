<?php


$con=mysqli_connect("localhost","root","","bloodbank");
$sql="select * from `bank_table`;";
$rs=mysqli_query($con,$sql);

while(($row=mysqli_fetch_assoc($rs))!=null){
    $output[]=$row;
}

//print_r($output);
print(json_encode(array("banks"=>$output)));

?>
