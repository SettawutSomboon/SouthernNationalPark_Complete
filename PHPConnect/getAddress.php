<?php
	$objConnect = mysql_connect("localhost","project3_travel","NstGm3lo");
	//$objConnect = mysql_connect("localhost","root","");
	//$objDB = mysql_select_db("southnationpark");
	$objDB = mysql_select_db("project3_travel");
	$objQuery= mysql_query("SET NAMES UTF8");

	$addressID = $_POST["addreID"];
	$strSQL = "SELECT address,phone,park_name,latitude,longtitude FROM data_southnationpark where 1 AND id = '".$addressID."'";
	$objQuery = mysql_query($strSQL);
	$obResult = mysql_fetch_array($objQuery);
	if($obResult)
	{
		$arr["address"] = $obResult["address"];
		$arr["phone"] = $obResult["phone"];
		$arr["parkname"] = $obResult["park_name"];
		$arr["latitude"] = $obResult["latitude"];
		$arr["longtitude"] = $obResult["longtitude"];

	}
	mysql_close($objConnect);

	echo json_encode($arr);
?>