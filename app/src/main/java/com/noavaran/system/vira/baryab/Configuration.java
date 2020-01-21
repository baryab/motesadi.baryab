package com.noavaran.system.vira.baryab;

public class Configuration {
//    public static final String BASE_URL = "http://www.baryabno.ir";
//    public static final String BASE_URL = "http://148.251.102.210";
//    public static final String BASE_URL = "http://192.168.10.87";


 // public static final String BASE_URL                            = "http://89.39.208.236";
 public static final String BASE_URL                            = "http://baaryab.ir";
 public static final String BASE_WEB_URL                        = BASE_URL + "/web";
 public static final String BASE_IMAGE_URL                      = BASE_URL + "/api";
 public static final String GEO_SERVER_URL                      = "http://89.39.208.236:8080"+
         "/geoserver/baryab/wms?"  +
         "SERVICE=WMS&VERSION=1.1.1"+
         "&REQUEST=GetFeatureInfo&" +
         "QUERY_LAYERS=baryab:map13&LAYERS=baryab:map13"+
         "&INFO_FORMAT=application/json&FEATURE_COUNT=1&X=50&Y=50&SRS=EPSG%3A41001&WIDTH=101&HEIGHT=101&propertyName=ostan,CENTER,OBJECTID";


 //public static final String BASE_API_URL                        = BASE_URL        + "/api/api";
 public static final String BASE_API_URL                       = BASE_URL       + "/api";
 public static final String API_LOGIN                           = BASE_API_URL    + "/login";
 public static final String API_LOGIN_2                         = BASE_API_URL  + "/login";//http://baaryab.ir/api/login
 public static final String API_PROFILE                         = BASE_API_URL + "/operators";
 public static final String API_UPLOAD_PROFILE_PICTURE          = BASE_API_URL + "/operators";
 public static final String API_INIT_DRAFT                      = BASE_API_URL + "/initDraft";//"http://baaryab.ir"
 public static final String API_NEW_LOADING                     = BASE_API_URL + "/Draft";
 public static final String API_EDIT_LOADING                    = BASE_API_URL + "/Draft";
 public static final String API_GET_LOADING                     = BASE_API_URL + "/Draft";
 public static final String API_GET_LOADING_DETAIL              = BASE_API_URL + "/Draft";
 public static final String API_DELETE_LOADING                  = BASE_API_URL + "/Draft";
 public static final String API_GET_CARRIED_LOADING             = BASE_API_URL + "/Draft";
 public static final String API_GET_CARRYING_LOADING            = BASE_API_URL + "/Draft";
 public static final String API_CANCEL_CARRYING_LOADING_REQUEST = BASE_API_URL + "/Draft";
 public static final String API_GET_CARRIED_LOADING_DETAILS     = BASE_API_URL + "/Draft";
 public static final String API_GET_CARRYING_LOADING_DETAILS    = BASE_API_URL + "/Draft";
 public static final String API_LOADING_DRIVERS                 = BASE_API_URL + "/Draft";
 public static final String API_ACCEPT_DRIVERS                  = BASE_API_URL + "/Draft";
 public static final String API_DRIVER_RATING                   = BASE_API_URL + "/Draft";
 public static final String API_LAST_DRIVER_LOCATION            = BASE_API_URL + "/Draft";
 public static final String API_COMMENTS                        = BASE_API_URL + "/Comments";
 public static final String API_CRASH                           = BASE_API_URL + "/Comments";
 public static final String API_LOAD_RATE                       = BASE_API_URL + "/Draft";
 public static final String API_SHARE_APPLICATION               = BASE_API_URL + "/Operators";


 public static final String API_GET_LAST_DRIVER_LOCATION        = "https://api.mylnikov.org/geolocation/cell?v=1.1&data=open";
 public static final String TEMP_PHOTO_FILE_NAME                = "temp_photo.jpg";

 public static final int REQUEST_CODE_GALLERY                   = 0x1;
 public static final int REQUEST_CODE_TAKE_PICTURE              = 0x2;
 public static final int REQUEST_CODE_CROP_IMAGE                = 0x3;
 public static final int REQUEST_CODE_PLACES_INFO               = 1001;
 public static final int REQUEST_CODE_MOBILE_DATA_SETTING       = 1002;
 public static final int REQUEST_CODE_WIFI_SETTING              = 1003;
 public static final int REQUEST_CODE_REFRESH_DATA              = 1004;

 public static final String GOOGLE_PLACE_SEARCH_API_KEY         = "AIzaSyDYDgzeLNTSR3-rBpsaGJdMEK_P_cY33wY";

 public static final String LIMITATION = "10";
 public static final String SUPPORT_CENTER_PHONE_NUMBER         = "+982133333333";
}
