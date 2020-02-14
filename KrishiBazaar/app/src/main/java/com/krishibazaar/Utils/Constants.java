package com.krishibazaar.Utils;

public class Constants {
    public static final String BASE_ADDRESS = "http://192.168.137.1/KrishiBazaar/api/";
    public static final String GEOCODE_PHP = BASE_ADDRESS + "geocode.php";
    public static final String SEARCH_PHP = BASE_ADDRESS + "search.php";
    public static final String GET_PROFILE_PHP = BASE_ADDRESS + "profile.php";
    public static final String GET_TRANSACTION_PHP = BASE_ADDRESS + "getTransaction.php";
    public static final String GENERATE_OTP_PHP = BASE_ADDRESS + "generateOtp.php";
    public static final String VERIFY_OTP_PHP = BASE_ADDRESS + "verifyOtp.php";
    public static final String REGISTER_PHP = BASE_ADDRESS + "register.php";
    public static final String GET_PRODUCT_DETAILS_PHP = BASE_ADDRESS + "getProductDetails.php";
    public static final String MAKE_TRANSACTION_PHP = BASE_ADDRESS + "makeTransaction.php";
    public static final String SELL_PRODUCT_PHP = BASE_ADDRESS + "sellProduct.php";
    public static final String DELETE_PRODUCT_PHP = BASE_ADDRESS + "deleteProduct.php";
    public static final String CHANGE_TRANSACTION_STATUS_PHP = BASE_ADDRESS + "changeTransaction.php";
    public static final String LOGOUT_PHP = BASE_ADDRESS + "logout.php";


    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String SUCCESS = "success";
    public static final String ADDRESS = "address";
    public static final String SEARCH = "search";

    //searchApi
    //SEARCH

    public static final String CATEGORY = "category";
    public static final String SIZE = "size";
    public static final String PAGE_OFFSET = "pageOffset";
    //LATITUDE
    //LONGITUDE

    public static final String IMAGE_URL = "image_url";
    public static final String PRODUCT_ID = "product_id";
    public static final String NAME = "name";
    public static final String QUANTITY = "quantity";
    public static final String PRICE = "price";
    public static final String DESCRIPTION = "description";
    public static final String DISTANCE = "distance";

    //profileApi
    public static final String TOKEN = "token";
    public static final String MOBILE = "mobile";
    //ADDRESS
    public static final String PINCODE = "pincode";
    //NAME

    //updateProfileApi
    public static final String STATUS = "status";
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_ERROR = 0;

    //getTransactions
    public static final int OWNED = 0;
    public static final int AVAILABLE = 1;
    public static final int PENDING = 2;
    public static final int ACCEPTED = 3;
    public static final int REJECTED = 4;
    public static final int SOLD = 5;//show buyer that product is no more active
    public static final int DELETED = 6;//show seller that product is no more active



    //sendOtp
    //MOBILE
    //STATUS
    //STATUS_ERROR
    //STATUS_SUCCESS
    public static final String OTP = "otp";
    public static final int STATUS_SUCCESS_NEW = 0;
    public static final int STATUS_SUCCESS_EXIST = 1;
    //TOKEN


    //PRODUCTVIEW
    public static final String FARMER_MOBILE = "farmer_mobile";
    public static final String BUYER_DETAILS = "buyers";
    //BUYERS
    public static final int STATUS_NOT_DECIDED = 0;
    public static final int STATUS_ACCEPTED = 1;
    public static final int STATUS_DENIED = 2;
    public static final String TRANSACTION_ID="tran_id";
    public static final String TIMESTAMP="timestamp";
    public static final String SUB_CATEGORY="sub";

    public static final String FCM="fcm";
}