<?php
	$objConnect = mysql_connect("localhost","project3_travel","NstGm3lo");
	//$objConnect = mysql_connect("localhost","root","");
	/*http://ictte-project.com/phpmyadmin/*/
	/*119.59.120.54*/
	$objDB = mysql_select_db("project3_travel");
	//$objDB = mysql_select_db("southnationpark");
	$objQuery= mysql_query("SET NAMES UTF8");
	$_POST["nationalID"] = "1";

	$strnationalID = $_POST["nationalID"];
	$strSQL = "SELECT data_southnationpark.id,park_name,image,province_id,province_name FROM province left join data_southnationpark on (data_southnationpark.province_id = province.id) where 1 AND category_id = '".$strnationalID."'";
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