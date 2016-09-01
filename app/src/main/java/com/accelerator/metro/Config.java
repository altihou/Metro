package com.accelerator.metro;

/**
 * Created by Nicholas on 2016/7/18.
 */
public class Config {

    //Server
    public static final String LOGIN_M="User";
    public static final String LOGIN_ACTION="UserLogin";

    public static final String REGISTER_M="User";
    public static final String REGISTER_ACTION="UserRegister";

    public static final String MINE_M="User";
    public static final String MINE_ACTION="UserInfo";


    public static final String MODIFY_M="User";
    public static final String MODIFY_ACTION="ModifyBaseInfo";

    public static final String RECHARGE_M="Pay";
    public static final String RECHARGE_ACTION="recharge";

    public static final String FEEDBACK_M="FeedBack";
    public static final String FEEDBACK_ACTION="get_user_feedback";


    public static final String MODIFY_LOGIN_M="User";
    public static final String MODIFY_LOGIN_ACTION="login_modify_pwd";

    public static final String COMMIT_ORDER_M="Pay";
    public static final String COMMIT_ORDER_ACTION="before_payment";

    public static final String MODIFY_PAY_M="User";
    public static final String MODIFY_PAY_ACTION="pay_modify_pwd";
    public static final String MODIFY_PAY_CHECK_ACTION="IsExistPayKey";

    public static final String PAY_ORDER_M="Pay";
    public static final String PAY_ORDER_ACTION="ok_payment";

    public static final String ORDER_M="UserOrder";
    public static final String ORDER_ACTION="order_info";

    public static final String ORDER_CANCEL_M="UserOrder";
    public static final String ORDER_CANCEL_ACTION="cancel_order";

    public static final String ORDER_REFUND_M="UserOrder";
    public static final String ORDER_REFUND_ACTION="recede_order";

    public static final String ORDER_DELETE_M="UserOrder";
    public static final String ORDER_DELETE_ACTION="delete_order";

    public static final String SIGNOUT_M="User";
    public static final String SIGNOUT_ACTION="UserLoginOut";

    public static final String AUTO_BUY_M="Auto";
    public static final String AUTO_BUY_ACTION="AutoFindTicket";

    public static final String GET_AUTO_BUY_M="Auto";
    public static final String GET_AUTO_BUY_ACTION="QueryAutoTicket";

    //SP
    public static final String FIRST="FIRST";
    public static final String FIRST_TIME="first_time";

    public static final String NFC_STATE="nfc_state";

    public static final String USER="USER";
    public static final String USER_REFRESH="refresh";
    public static final String USER_PHONE="user_phone";
    public static final String USER_SESSION="user_session";
    public static final String USER_ID="user_id";
    public static final String USER_AVATAR="user_avatar";
    public static final String USER_MONEY="money";
    public static final String USER_NICKNAME="nickname";
    public static final String USER_SEX="sex";


}
