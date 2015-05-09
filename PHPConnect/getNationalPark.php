<?php
	//$objConnect = mysql_connect("ictte-project.com","project3_travel","NstGm3lo");
	/*http://ictte-project.com/phpmyadmin/*/
	/*119.59.120.54*/
	//$objDB = mysql_select_db("project3_travel");
	$objConnect = mysql_connect("localhost","root","");
	$objDB = mysql_select_db("southnationpark");
	$objQuery= mysql_query("SET NAMES UTF8");

	$strprovinceid = $_POST["sID"];
	$strSQL = "SELECT * FROM data_southnationpark where 1 AND province_id = '".$strprovinceid."'";
	//$strSQL = "SELECT * FROM data_southnationpark where 1 AND province_id = 3";
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