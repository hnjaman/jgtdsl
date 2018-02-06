package org.jgtdsl.utils;

import java.text.SimpleDateFormat;

public class AC {
	
	public static final String CACHE_NAME = "PCACHE";
	
	public static String STATUS_ERROR="ERROR";
	public static String STATUS_OK="OK";
	
	public static String DIALOG_CAPTION_SUCCESS="Success Confirmation";
	public static String DIALOG_CAPTION_ERROR="Information";
	
	
	public static String MSG_CREATE_OK_PREFIX="Successfully created new ";
	public static String MSG_CREATE_ERROR_PREFIX="Error in creating new ";
	
	public static String MSG_DELETE_OK_PREFIX="Successfully deleted ";
	public static String MSG_DELETE_ERROR_PREFIX="Error in deleting ";
	
	public static String MSG_UPDATE_OK_PREFIX="Successfully updated ";
	public static String MSG_UPDATE_ERROR_PREFIX="Error in updating ";
	
	public static String MST_CUSTOMER_CATEGORY="Customer Category";
	public static String MST_METER_CATEGORY="Meter Category";
	public static String MST_USER="User";
	public static String MST_AREA="Area";
	public static String MST_TARIFF="Tariff";
	public static String MST_BANK="Bank";
	public static String MST_BRANCH="Branch";
	public static String MST_BANK_ACCOUNT="Bank Account";
	public static String MST_METER_TYPE="Meter Type";	
	public static String MST_DEPOSIT_TYPE="Deposit Type";
	public static String MST_ACCOUNT_INFO="Account Information";
	
	public static SimpleDateFormat DF_12_AMPM = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
	public static SimpleDateFormat DF_24 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	
	public static SimpleDateFormat DF_ORACLE = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public final static float P_FACTOR =new Float(14.73);
	public final static float T_FACTOR1 =new Float(288.71);
	public final static float T_FACTOR2 =new Float(273);
	
}
