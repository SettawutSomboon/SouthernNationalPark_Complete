<?php
	$objConnect = mysql_connect("localhost","project3_travel","NstGm3lo");
	//$objConnect = mysql_connect("localhost","root","");
	$objDB = mysql_select_db("project3_travel");
	//$objDB = mysql_select_db("southnationpark");
	$objQuery= mysql_query("SET NAMES UTF8");

	$strSQL = "SELECT id,park_name,latitude,longtitude,category_id,province_id,image,address FROM data_southnationpark where 1";
	$objQuery = mysql_query($strSQL);
	$intNumField = mysql_num_fields($objQuery);
	$resultArray = array();
	while($obResult = mysql_fetch_array($objQuery))
	{
		$arrCol = array();
		for($i=0;$i<$intNumField;$i++)
		{
			$arrCol[mysql_field_name($objQuery,$i)] = $obResult[$i];
		}
		array_push($resultArray,$arrCol);
	}
	
	mysql_close($objConnect);
	
	echo json_encode($resultArray);
?>