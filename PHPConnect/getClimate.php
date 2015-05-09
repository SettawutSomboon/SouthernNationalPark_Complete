<?php
	$objConnect = mysql_connect("localhost","project3_travel","NstGm3lo");
	//$objConnect = mysql_connect("localhost","root","");
	//$objDB = mysql_select_db("southnationpark");
	$objDB = mysql_select_db("project3_travel");
	$objQuery= mysql_query("SET NAMES UTF8");

	$climateID = $_POST["climateID"];
	$strSQL = "SELECT climate,park_name,image FROM data_southnationpark where 1 AND id = '".$climateID."'";
	$objQuery = mysql_query($strSQL);
	$obResult = mysql_fetch_array($objQuery);
	if($obResult)
	{
		$arr["climate"] = $obResult["climate"];
		$arr["parkname"] = $obResult["park_name"];
		$arr["image"] = $obResult["image"];

	}
	mysql_close($objConnect);

	echo json_encode($arr);
?>