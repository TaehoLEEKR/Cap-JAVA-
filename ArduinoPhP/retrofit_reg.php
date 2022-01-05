<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST')
{
    include_once("retrofit_config.php");

    $userID = $_POST['userID'];
    $name = $_POST['name'];
    $userPW = $_POST['userPW'];
    $phone= $_POST['phone'];

    if($userID == '' || $name == '' || $userPW == '' || $phone == '')
    {
         echo json_encode(array(
            "status" => "false",
            "message" => "필수 인자가 부족합니다")
    );
    }
    else
    {
        $query= "SELECT * FROM userTable WHERE userID='$userID'";
        $result= mysqli_query($con, $query);

        if(mysqli_num_rows($result) > 0){
                echo json_encode(array( "status" => "false","message" => "이미 존재하는 이름입니다") );
        }

        else
        {

                $query = "INSERT INTO userTable (userID,name,userPW,phone) VALUES ('$userID','$name','$userPW','$phone')";

                $query2 = "INSERT INTO app (userID, userPW, name, phone) VALUES ('$userID','$userPW','$name','$phone')";

                if(mysqli_query($con,$query))
                {
                        mysqli_query($con, $query2);
                        $query= "SELECT * FROM userTable WHERE userID='$userID'";
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
                        "message" => "회원가입 성공",
                        "data" => $emparray)
                );

            }
 else
            {
                echo json_encode(
                    "status" => "false",
                    "message" => "에러가 발생했습니다. 다시 시도해 주세요"
                    )
            );

            }
    }
    mysqli_close($con);
    }
}
?>