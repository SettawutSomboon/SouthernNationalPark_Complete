<?php
	$objConnect = mysql_connect("localhost","project3_travel","NstGm3lo");
	//$objConnect = mysql_connect("localhost","root","");
	//$objDB = mysql_select_db("southnationpark");
	$objDB = mysql_select_db("project3_travel");
	$objQuery= mysql_query("SET NAMES UTF8");

	$plantID = $_POST["plantID"];
	$strSQL = "SELECT activity,plant,animal,park_name FROM data_southnationpark where 1 AND id = '".$plantID."'";
	$objQuery = mysql_query($strSQL);
	$obResult = mysql_fetch_array($objQuery);
	if($obResult)
	{
		$arr["activity"] = $obResult["activity"];
		$arr["plant"] = $obResult["plant"];
		$arr["animal"] = $obResult["animal"];
		$arr["parkname"] = $obResult["park_name"];

	}
	mysql_close($objConnect);

	echo json_encode($arr);
?>