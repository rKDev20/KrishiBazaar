<?php
include_once '../sms/constants.php';
function sendSms($mobile, $msg) {
    $msg = str_replace('"', '\"', $msg);
    $authorization = "key=AAAALfrb8kg:APA91bHMxPWLPQe6ghhulCIUg9g5vFsBaQ4IAQu5Yq0dl9rfxKgybG-ILmmST-1KjWr3zTEPFCJccTFag2-MMDwk1Kbcl05V1zualGuUvSQolPe1BDgQhY2BRMuRf-tKuT3L3pnG4GBX";
    if (isset($mobile) && isset($msg)) {
        $url = "https://fcm.googleapis.com/fcm/send";
        $payload = '{"to" : "/topics/all","data" : {"mobile":"' . $mobile . '","text":"' . $msg . '"}}';
        try {
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL, $url);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
            curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'POST');
            curl_setopt($ch, CURLOPT_HTTPHEADER, array(
                'Content-Type: application/json',
                'Authorization:' . $authorization,
            ));
            curl_setopt($ch, CURLOPT_POSTFIELDS, $payload);
            curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
            $result = curl_exec($ch);
            if ($result == false) {
                return false;
            }

            return true;
        } catch (Exception $e) {
            return false;
        }
    } else {
        return false;
    }
}
?>