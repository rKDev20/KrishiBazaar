<html>
	<head>
		<title></title>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
		<link rel="stylesheet" type="text/css" href="global.css">
		<script src="../myjs.js"></script>
	</head>
	<body class="bg">

		<section class="container-fluid">
			<div class="container">
				<div class="row">
					<div class="col-md-10 offset=md-1">
						<div class="row">
							<div class="col-md-5 register_left">
								<div class="text">
								<h3 class="text-success">Join Us</h3>
								<p class="text-success">“The farmer is the only man in our economy who buys everything at retail, sells everything at wholesale, and pays the freight both ways.” – John F. Kennedy</p>
							</div>
							</div>
							<form action="../register.php" class="col-md-7 register_right" method="post">
								<h2>Register Here!</h2>
								<div class="form-group">
									<input type="text" class="form-control" placeholder="Name" name="name">
								</div>
								<div class="form-group">
									<input type="text" class="form-control" placeholder="Address" name="address">
								</div>
								<div class="form-group">
									<input type="text" class="form-control" placeholder="Pin Code" name = "pincode">
								</div>
								<div class="form-group">
									<input type="tel" class="form-control" placeholder="Mobile" id="mobile" name="mobile">
								</div>
								<button id = "btnid" type="button" onclick="buttonclick()" class="btn btn-primary">Send OTP</button>
								<div id="otpForm" hidden>
									<div class="form-group">
										<input type="number" class="form-control" placeholder="OTP" name="otp">
									</div>
									<input type="submit" class="btn btn-success" value="Register"/>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</section>

		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
	  	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
	  	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
	</body>
	<?php
	if(isset($_SESSION['message']))
	{
	  $message = $_SESSION['message'];
	  echo "<script type='text/javascript'>alert('$message');</script>";
	  unset($_SESSION['message']);
	}
	?>
