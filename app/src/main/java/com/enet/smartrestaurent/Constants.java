package com.enet.smartrestaurent;

/**
 * Created by asela on 5/20/17.
 */
public class Constants {

    public static final int SUCCESS_RESPONSE = 1;
    public static final String ERROR_RESPONSE = "error";
    public static final String RESPONSE_KEY = "response";
    public static final String ORDER_RECEIVED_ACTION = "order_action";
    public static final String CHECKUPDATES_ACTION = "check_updates_action";
    public static final String ADD_ORDER_ACTION = "add_order_action";
    public static final String ADD_ORDER_MENUS_ACTION = "add_order_menus_action";
    public static final String MQTT_ACTION = "mqtt_action";
    public static final String MQTT_PUBLISH_STATE_ACTION = "mqtt_publish_state";
    public static final String MQTT_CONNECTION_STATE_ACTION = "mqtt_connection_state";
    public static final String MQTT_DISCONNECTED_ACTION = "mqtt_connection_state";
    public static final String MQTT_CONNECTION_FAILED = "mqtt_connection_failed";
    public static final String MQTT_CONNECTION_SUCCESS = "mqtt_connection_success";
    public static final String MQTT_DELIVER_SUCCESS = "mqtt_deliver_success";
    public static final String MQTT_PUBLISH_FAILED = "mqtt_publish_failed";
    public static final String MQTT_NEW_MESSAGE_ACTION = "mqtt_new_message_action";

    public static final String MENU_UPDATE_ACTION = "menu_update_action";
    public static final String CATEGORIES_UPDATE_ACTION = "categories_update_action";
    public static final String EXTENDED_DATA_STATUS = "data_status";
    public static final String COMMUNICATION_ERROR = "error";

    public static final String SERVER_IP = "http://resmng.enetlk.com";
    public static final String API_BASE_URL = SERVER_IP+"/api/api.php/";
    public static final String IMAGE_THUMBS_URL = SERVER_IP+"/assets/images/thumbs/";
    public static final String API_MENU = "forsj3vth_menus";
    public static final String API_CATEGORIES = "forsj3vth_categories";
    public static final String RECORDS_KEY = "records";

    public static final String ORDER_RECEIVED_TOPIC = "silver_ring_order_received";
    public static final String ORDER_COMPLETED_TOPIC = "silver_ring_order_completed";

    public static final int KITCHEN = 12;
    public static final int BAR = 13;

}
