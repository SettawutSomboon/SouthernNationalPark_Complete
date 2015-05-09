<?php
	//$objConnect = mysql_connect("ictte-project.com","project3_travel","NstGm3lo");
	$objConnect = mysql_connect("localhost","root","");
	/*http://ictte-project.com/phpmyadmin/*/
	/*119.59.120.54*/
	//$objDB = mysql_select_db("project3_travel");
	$objDB = mysql_select_db("southnationpark");
	$objQuery= mysql_query("SET NAMES UTF8");

	$strSQL = "SELECT * FROM province where 1";
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