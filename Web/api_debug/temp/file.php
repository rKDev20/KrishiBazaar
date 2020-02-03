<?php

$conn=new mysqli("localhost","root","","pincode");
$file = fopen("IN.txt","r");
$lastIndex=0;
$lastindex2=0;
while(! feof($file)){
	$line= fgets($file);
	$lastIndex=strpos($line,"\t",$lastIndex);
	$lastindex2=strpos($line,"\t",$lastIndex+1);
    $pincode=substr($line,$lastIndex+1,$lastindex2-$lastIndex);
    $lastIndex=$lastindex2;
        
    $lastindex2=strpos($line,"\t",$lastIndex+1);
    $name=substr($line,$lastIndex+1,$lastindex2-$lastIndex);
    $lastIndex=$lastindex2;
	
    
    $lastindex2=strpos($line,"\t",$lastIndex+1);
    $an1=substr($line,$lastIndex+1,$lastindex2-$lastIndex);
    $lastIndex=$lastindex2;

    $lastindex2=strpos($line,"\t",$lastIndex+1);
    $ac1=substr($line,$lastIndex+1,$lastindex2-$lastIndex);
    $lastIndex=$lastindex2;

    $lastindex2=strpos($line,"\t",$lastIndex+1);
    $an2=substr($line,$lastIndex+1,$lastindex2-$lastIndex);
    $lastIndex=$lastindex2;

    $lastindex2=strpos($line,"\t",$lastIndex+1);
    $ac2=substr($line,$lastIndex+1,$lastindex2-$lastIndex);
    $lastIndex=$lastindex2;

    $lastindex2=strpos($line,"\t",$lastIndex+1);
    $an3=substr($line,$lastIndex+1,$lastindex2-$lastIndex);
    $lastIndex=$lastindex2;

    $lastindex2=strpos($line,"\t",$lastIndex+1);
    $ac3=substr($line,$lastIndex+1,$lastindex2-$lastIndex);
    $lastIndex=$lastindex2;

    $lastindex2=strpos($line,"\t",$lastIndex+1);
    $lattitude=substr($line,$lastIndex+1,$lastindex2-$lastIndex);
    $lastIndex=$lastindex2;

    $lastindex2=strpos($line,"\t",$lastIndex+1);
    $longitude=substr($line,$lastIndex+1,$lastindex2-$lastIndex);
    $lastIndex=$lastindex2;

    $lastindex2=strpos($line,"\t",$lastIndex+1);
    $accuracy=substr($line,$lastIndex+1,$lastindex2-$lastIndex);
    $lastIndex=$lastindex2;

    $query = "INSERT INTO record (pinocde,name,an1,ac1,an2,ac2,an3,ac3,lattitude,longitude,accuracy) VALUES ('$pincode', '$name', '$an1', '$ac1', '$an2', '$ac2', '$an3', '$ac3', '$lattitude', '$longitude', '$accuracy');";
    #echo $pincode.",".$name.",".$an1.",".$ac1.",".$an2.",".$ac2.",".$an3.",".$ac3.",".$lattitude.",".$longitude.",".$accuracy;
    if(!mysqli_query($conn, $query)){
        echo mysqli_error($conn);
 	   break;
	}
}
$conn->close();
echo "Success";
?>