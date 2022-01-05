<?php
include_once ("retrofit_config.php");
//if($con) {echo "1";} else{ echo"0";}
if ($_SERVER['REQUEST_METHOD'] == 'POST' || $_SERVER['REQUEST_METHOD'] == 'GET' )
{
    $userID = $_POST['userID'];
    $userPW = $_POST['userPW'];

    if( $userID == '' || $userPW == '')
    {
        echo json_encode(array(
            "status" => "false",
            "message" => "Parameter missing!"
        ));
    }
    else
    {
        $query= "SELECT * FROM userTable WHERE userID='$userID' AND userPW='$userPW'";
        $result= mysqli_query($con, $query);

        if(mysqli_num_rows($result) > 0)
        {
            $query= "SELECT * FROM userTable WHERE userID='$userID' AND userPW='$userPW'";
            $result= mysqli_query($con, $query);
            $emparray = array();
            if(mysqli_num_rows($result) > 0)
            {
                while ($row = mysqli_fetch_assoc($result))
                {
                   $emparray[] = $row;
                }
            }
            echo json_encode(
                array(
                    "status" => "true",
                    "message" => "login success",
                    "data" => $emparray
                )
            );
            }
            else
            {
                echo json_encode(
                    array(
                        "status" => "false",
                        "message" => "ID or PW fail")
                    );
            }
            mysqli_close($con);
    }
}
else
{
    echo json_encode(
        array(
            "status" => "false",
            "message" => "ERROR"
        )
