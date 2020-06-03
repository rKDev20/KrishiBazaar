const root="/KrishiBazaar/web/";
function buttonclick(){
  if(!phonenumber())
  {
    alert("Input valid Phone Number");
    return;
  }
  else {
    var mobile=$("#mobile").val();
    $.ajax({
        type: 'POST',
        contentType : "application/json",
        dataType: "json",
        url: root+'api/generateOtp.php',
        data: JSON.stringify({ mobile: mobile }),
        success: function(data){
            if(data.status!=1)
            failedToSend();
        }
    }).fail( function(xhr, textStatus, errorThrown) {
        failedToSend();
    });
  }
  document.getElementById("otpForm").removeAttribute("hidden");
  document.getElementById("btnid").setAttribute("hidden",true);
}
function loginButton(){
  document.getElementById("form").submit();
}
function phonenumber()
{
  var inputtxt = document.getElementById("mobile").value;
  var phoneno = /^\d{10}$/;
  if(inputtxt.match(phoneno))
  {
      return true;
  }
  else
  {
     return false;
  }
}
