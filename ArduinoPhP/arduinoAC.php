
<?php

include_once("retrofit_config.php");

if(!$con){ echo "Fail DB connection"; }

mysqli_set_charset($con,"utf8");
//arts = explode(' ',$values[0]);
//int $parts[3];
//
        $query = "SELECT * FROM SIGNALAD";
        $res = mysqli_query($con,$query);
        $data = array();

        if($res){
        $res = mysqli_query($con,$query);
                while($row = mysqli_fetch_array($res)) {
                        $data= [
                                        'AIRON' => $row[0],
                                        'AIROFF' => $row[1],
                                        'POWER' => $row[2],
                                        'SETNUM' => $row[3],
                                        'TEMP' => $row[4]];
                }
        $json = json_encode($data,JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE );
                echo $json;
        }
        else{
                echo "SQL ERROR";
                echo mysqli_error($con);
        }
        mysqli_close($con);

?>