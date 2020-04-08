<?php

define('WELCOME_MSG', 'What would you like to do?\n Send 1 for uploading an ad.\nSend 2 for deleting an ad.');
define('ENTER_NAME','What do you want to sell?');
define('ENTER_DESCRIPTION', 'Please describe the "%s" you want to sell. You can say what quality it is, what type it is, how old it is, or anything else.');
define('ENTER_LOCATION','Enter your pincode so that others can locate you.');
define('ENTER_QUANTITY', 'Enter the quantity is kilograms you wish to sell');
define('ENTER_PRICE','Enter rate per kilograms you want to sell at.');
define('SUCCESS_AD_POSTED','Thank you! Your product has been put up for sale. Your product id is %d. Use this id for future reference');


define('ENTER_PRODUCT_ID_REMOVE','Enter product id of advertisement you want to delete.');
define('ENTER_REMOVE_CONFIRMATION','Are you sure you want to delete your product %s with id %d. Enter Y/N for yes/no');
define('PRODUCT_REMOVED', 'Your product has successfully been removed');

define('ENTER_PRODUCT_ID_DETAILS', 'Enter product id of advertisement you want to see details');


define('WRONG_INPUT','You have entered a wrong input. Please enter right input.');
define('WRONG_LOCATION','You have entered an invalid pincode. Please enter again.');
define('WRONG_INPUT_REMOVE_ITEM', 'You have entered wrong product id. Please enter again.');
define('WRONG_INPUT_REMOVE_CONFIRMATION', 'You have entered a wrong input. Please enter Y/N.');


define('WRONG_INPUT_TRANSACTIONS', 'Your input cannot be understood. Please reply in this  format : "[transaction id] REJECT" OR "[transaction id] ACCEPT"');
define('NO_TRANSACTION_FOUND',"No such transaction is found. Please check and try again.");
define('TRANSACTION_ALREADY_ACCEPTED',"You have already accepted the request. You cannot change it again.");
define('TRANSACTION_ALREADY_REJECTED',"You have already rejected the request. You cannot change it again.");
define('ACCEPTED_SUCCESS','You have successfuly accepted the request. We will share your contact details with the buyer.');
define('REJECTED_SUCCESS','You have successfuly rejected the request. We will NOT share your contact details with the buyer.');

define('NEW_TRANSACTION','New request for %s from %s, %.2f kms away. The request id is %d. Reply "%d ACCEPT" or "%d REJECT" to accept/reject the request');
define('NEW_TRANSACTION_PROPOSED_RATE','New request for %s from %s, %.2f kms away at ₹%.2f. The resquest id is %d. Reply "%d ACCEPT" or "%d REJECT" to accept/reject the request');

define('TRANSACTION_ACCEPTED','Your request for "%s" has been accepted. Sellers mobile number is +91%d.');
define('TRANSACTION_REJECTED','Your request for "%s" has been rejected');
?>