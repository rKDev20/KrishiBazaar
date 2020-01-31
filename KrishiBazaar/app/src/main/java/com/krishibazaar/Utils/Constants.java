package com.krishibazaar.Utils;

public class Constants {
    public static final String BASE_ADDRESS = "http://192.168.137.1/KrishiBazaar/";
    public static final String GEOCODE_PHP = BASE_ADDRESS + "geocode.php";
    public static final String SEARCH_PHP = BASE_ADDRESS + "search.php";
    public static final String GET_PROFILE_PHP = BASE_ADDRESS + "profile.php";
    public static final String UPDATE_PROFILE_PHP = BASE_ADDRESS + "updateProfile.php";
    public static final String GET_TRANSACTION_PHP = BASE_ADDRESS + "getTransaction.php";

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
    public static final int PENDING = 1;
    public static final int ACCEPTED = 2;
    public static final int REJECTED = 3;
    public static final int SOLD = 4;
}