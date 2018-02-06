package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;

import org.jgtdsl.dto.CollectionDTO;
import org.jgtdsl.dto.GasPurchaseDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.enums.BankAccountTransactionType;
import org.jgtdsl.enums.Month;
import org.jgtdsl.utils.connection.TransactionManager;

public class GasPurchaseService {
	public ResponseDTO saveGasPurchaseInfo(GasPurchaseDTO gasPurchaseinfo)
	{
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		
		

		
		PreparedStatement stmt = null;
		PreparedStatement pid_stmt = null;
		
		ResultSet r = null;
		String pid=null;		
		double bgfcl_ratio;
		double sgfl__ratio;
		double ioc__ratio;
		DecimalFormat format_7_precision = new DecimalFormat("##.0000");
		try
			{
			
			        bgfcl_ratio=Double.valueOf(format_7_precision.format(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
			        sgfl__ratio=Double.valueOf(format_7_precision.format(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
			        ioc__ratio=Double.valueOf(format_7_precision.format(gasPurchaseinfo.getTotal_ioc()/gasPurchaseinfo.getTotal_gtcl()));
				
			        pid_stmt = conn.prepareStatement("Select SQN_PURCHASE_M.NEXTVAL collection_id from dual");
			        r = pid_stmt.executeQuery();
					if (r.next())
						pid=r.getString("collection_id"); 

					String sql_insert_gas_purchase_summary="INSERT INTO GAS_PURCHASE_SUMMARY (PID, MONTH, YEAR,TOTAL_BGFCL, TOTAL_SGFL, TOTAL_IOC, TOTAL_GTCL)  " +
							"VALUES (?,?,?,?,?,?,?)";
					String sql_insert_gas_purchase_bgfcl="INSERT INTO GAS_PURCHASE_BGFCL ( PID, MONTH, YEAR, PW_GVT, PW_PVT, CAP_GVT, CAP_PVT, CNG_GVT, CNG_PVT, IND_GVT, IND_PVT, COMM_GVT, COMM_PVT, DOM_M_GVT, DOM_M_PVT, DOM_NM_GVT, DOM_NM_PVT, FERTILIZER_GVT, FERTILIZER_PVT, TEA_GVT, TEA_PVT)  " +
							"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )" ;
					String sql_insert_gas_purchase_sgfl="INSERT INTO GAS_PURCHASE_SGFL ( PID, MONTH, YEAR, PW_GVT, PW_PVT, CAP_GVT, CAP_PVT, CNG_GVT, CNG_PVT, IND_GVT, IND_PVT, COMM_GVT, COMM_PVT, DOM_M_GVT, DOM_M_PVT, DOM_NM_GVT, DOM_NM_PVT, FERTILIZER_GVT, FERTILIZER_PVT, TEA_GVT, TEA_PVT)  " +
							"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )" ;
					String sql_insert_gas_purchase_ioc="INSERT INTO GAS_PURCHASE_IOC ( PID, MONTH, YEAR, PW_GVT, PW_PVT, CAP_GVT, CAP_PVT, CNG_GVT, CNG_PVT, IND_GVT, IND_PVT, COMM_GVT, COMM_PVT, DOM_M_GVT, DOM_M_PVT, DOM_NM_GVT, DOM_NM_PVT, FERTILIZER_GVT, FERTILIZER_PVT, TEA_GVT, TEA_PVT)  " +
							"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )" ;
					String sql_insert_gas_purchase_info="INSERT INTO GAS_PURCHASE_INFO ( PID, MONTH, YEAR, PW_GVT, PW_PVT, CAP_GVT, CAP_PVT, CNG_GVT, CNG_PVT, IND_GVT, IND_PVT, COMM_GVT, COMM_PVT, DOM_M_GVT, DOM_M_PVT, DOM_NM_GVT, DOM_NM_PVT, FERTILIZER_GVT, FERTILIZER_PVT, TEA_GVT, TEA_PVT)  " +
							"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )" ;
					
					/*-------------------------------------------------------------Should be Comment-------------------------------------------------------------------------------------*/
					
					
					String sql_insert_gas_purchase_category="INSERT INTO GAS_PURCHASE_CAT ( PID, MONTH, YEAR, POWER, CAPTIVE, CNG, INDUSTRY, COMMERCIAL, DOMESTIC, FERTILIZER, TEA)  " +
							"VALUES (?,?,?,?,?,?,?,?,?,?,? )" ;
					
					String sql_insert_total_purchase_bgfcl="INSERT INTO TOTAL_BGFCL_RATIO ( PID, MONTH, YEAR, POWER, CAPTIVE, CNG, INDUSTRY, COMMERCIAL, DOMESTIC, FERTILIZER, TEA)  " +
							"VALUES (?,?,?,?,?,?,?,?,?,?,? )" ;
					
					String sql_insert_total_purchase_sgfl="INSERT INTO TOTAL_SGFL_RATIO ( PID, MONTH, YEAR, POWER, CAPTIVE, CNG, INDUSTRY, COMMERCIAL, DOMESTIC, FERTILIZER, TEA)  " +
							"VALUES (?,?,?,?,?,?,?,?,?,?,? )" ;
					
					
					
					int i=1;
					stmt = conn.prepareStatement(sql_insert_gas_purchase_summary);
					stmt.setString(i++,pid);
					stmt.setString(i++,gasPurchaseinfo.getMonth());
					stmt.setString(i++,gasPurchaseinfo.getYear());					
					stmt.setDouble(i++,gasPurchaseinfo.getTotal_bgfcl());
					stmt.setDouble(i++,gasPurchaseinfo.getTotal_sgfl());
					stmt.setDouble(i++,gasPurchaseinfo.getTotal_ioc());
					stmt.setDouble(i++,gasPurchaseinfo.getTotal_gtcl());		
					stmt.executeUpdate();
					
					i=1;
					stmt = conn.prepareStatement(sql_insert_gas_purchase_bgfcl);
					stmt.setString(i++,pid);
					stmt.setString(i++,gasPurchaseinfo.getMonth());
					stmt.setString(i++,gasPurchaseinfo.getYear());					
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_power_gvt()*bgfcl_ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_power_pvt()*bgfcl_ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_captive_gvt()*bgfcl_ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_captive_pvt()*bgfcl_ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_cng_gvt()*bgfcl_ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_cng_pvt()*bgfcl_ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_industry_gvt()*bgfcl_ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_industry_pvt()*bgfcl_ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_comm_gvt()*bgfcl_ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_comm_pvt()*bgfcl_ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_meter_gvt()*bgfcl_ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_meter_pvt()*bgfcl_ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_nmeter_gvt()*bgfcl_ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_nmeter_pvt()*bgfcl_ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_fertilizer_gvt()*bgfcl_ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_fertilizer_pvt()*bgfcl_ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_tea_gvt()*bgfcl_ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_tea_pvt()*bgfcl_ratio);
					
					stmt.setDouble(i++,Math.round(gasPurchaseinfo.getBgfcl_power_gvt()*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl())));
					
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_power_gvt()*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_power_pvt()*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_captive_gvt()*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_captive_pvt()*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_cng_gvt()*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_cng_pvt()*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_industry_gvt()*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_industry_pvt()*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_comm_gvt()*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_comm_pvt()*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_meter_gvt()*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_meter_pvt()*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_nmeter_gvt()*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_nmeter_pvt()*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_fertilizer_gvt()*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_fertilizer_pvt()*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_tea_gvt()*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_tea_pvt()*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					
					stmt.executeUpdate();
					i=1;
					stmt = conn.prepareStatement(sql_insert_gas_purchase_sgfl);		
					stmt.setString(i++,pid);
					stmt.setString(i++,gasPurchaseinfo.getMonth());
					stmt.setString(i++,gasPurchaseinfo.getYear());					
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_power_gvt()*sgfl__ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_power_pvt()*sgfl__ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_captive_gvt()*sgfl__ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_captive_pvt()*sgfl__ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_cng_gvt()*sgfl__ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_cng_pvt()*sgfl__ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_industry_gvt()*sgfl__ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_industry_pvt()*sgfl__ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_comm_gvt()*sgfl__ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_comm_pvt()*sgfl__ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_meter_gvt()*sgfl__ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_meter_pvt()*sgfl__ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_nmeter_gvt()*sgfl__ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_nmeter_pvt()*sgfl__ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_fertilizer_gvt()*sgfl__ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_fertilizer_pvt()*sgfl__ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_tea_gvt()*sgfl__ratio);
//					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_tea_pvt()*sgfl__ratio);
					
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_power_gvt()*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_power_pvt()*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_captive_gvt()*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_captive_pvt()*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_cng_gvt()*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_cng_pvt()*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_industry_gvt()*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_industry_pvt()*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_comm_gvt()*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_comm_pvt()*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_meter_gvt()*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_meter_pvt()*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_nmeter_gvt()*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_nmeter_pvt()*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_fertilizer_gvt()*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_fertilizer_pvt()*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_tea_gvt()*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_tea_pvt()*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					
					stmt.executeUpdate();
					
					i=1;
					stmt = conn.prepareStatement(sql_insert_gas_purchase_ioc);	
					stmt.setString(i++,pid);
					stmt.setString(i++,gasPurchaseinfo.getMonth());
					stmt.setString(i++,gasPurchaseinfo.getYear());					
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_power_gvt()*ioc__ratio);
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_power_pvt()*ioc__ratio);
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_captive_gvt()*ioc__ratio);
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_captive_pvt()*ioc__ratio);
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_cng_gvt()*ioc__ratio);
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_cng_pvt()*ioc__ratio);
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_industry_gvt()*ioc__ratio);
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_industry_pvt()*ioc__ratio);
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_comm_gvt()*ioc__ratio);
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_comm_pvt()*ioc__ratio);
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_meter_gvt()*ioc__ratio);
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_meter_pvt()*ioc__ratio);
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_nmeter_gvt()*ioc__ratio);
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_nmeter_pvt()*ioc__ratio);
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_fertilizer_gvt()*ioc__ratio);
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_fertilizer_pvt()*ioc__ratio);
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_tea_gvt()*ioc__ratio);
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_tea_pvt()*ioc__ratio);
					
					stmt.executeUpdate();
					
					i=1;
					stmt = conn.prepareStatement(sql_insert_gas_purchase_info);	
					stmt.setString(i++,pid);
					stmt.setString(i++,gasPurchaseinfo.getMonth());
					stmt.setString(i++,gasPurchaseinfo.getYear());					
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_power_gvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_power_pvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_captive_gvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_captive_pvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_cng_gvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_cng_pvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_industry_gvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_industry_pvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_comm_gvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_comm_pvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_meter_gvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_meter_pvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_nmeter_gvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_nmeter_pvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_fertilizer_gvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_fertilizer_pvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_tea_gvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_tea_pvt());
					
					stmt.executeUpdate();
					/*----------------------------------------------------------------------------------------------------For Test-----------------------------------------------------------------------------------*/
					
					
					i=1;
					stmt = conn.prepareStatement(sql_insert_gas_purchase_category);	
					stmt.setString(i++,pid);
					stmt.setString(i++,gasPurchaseinfo.getMonth());
					stmt.setString(i++,gasPurchaseinfo.getYear());					
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_power_gvt()+gasPurchaseinfo.getBgfcl_power_pvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_captive_gvt()+gasPurchaseinfo.getBgfcl_captive_pvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_cng_gvt()+gasPurchaseinfo.getBgfcl_cng_pvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_industry_gvt()+gasPurchaseinfo.getBgfcl_industry_pvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_comm_gvt()+gasPurchaseinfo.getBgfcl_comm_pvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_dom_meter_gvt()+gasPurchaseinfo.getBgfcl_dom_meter_pvt()+gasPurchaseinfo.getBgfcl_dom_nmeter_gvt()+gasPurchaseinfo.getBgfcl_dom_nmeter_pvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_fertilizer_gvt()+gasPurchaseinfo.getBgfcl_fertilizer_pvt());
					stmt.setDouble(i++,gasPurchaseinfo.getBgfcl_tea_gvt()+gasPurchaseinfo.getBgfcl_tea_pvt());
					
					stmt.executeUpdate();
					
					
					i=1;
					stmt = conn.prepareStatement(sql_insert_total_purchase_bgfcl);	
					stmt.setString(i++,pid);
					stmt.setString(i++,gasPurchaseinfo.getMonth());
					stmt.setString(i++,gasPurchaseinfo.getYear());					
					stmt.setDouble(i++,(gasPurchaseinfo.getBgfcl_power_gvt()+gasPurchaseinfo.getBgfcl_power_pvt())*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,(gasPurchaseinfo.getBgfcl_captive_gvt()+gasPurchaseinfo.getBgfcl_captive_pvt())*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,(gasPurchaseinfo.getBgfcl_cng_gvt()+gasPurchaseinfo.getBgfcl_cng_pvt())*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,(gasPurchaseinfo.getBgfcl_industry_gvt()+gasPurchaseinfo.getBgfcl_industry_pvt())*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,(gasPurchaseinfo.getBgfcl_comm_gvt()+gasPurchaseinfo.getBgfcl_comm_pvt())*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,(gasPurchaseinfo.getBgfcl_dom_meter_gvt()+gasPurchaseinfo.getBgfcl_dom_meter_pvt()+gasPurchaseinfo.getBgfcl_dom_nmeter_gvt()+gasPurchaseinfo.getBgfcl_dom_nmeter_pvt())*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,(gasPurchaseinfo.getBgfcl_fertilizer_gvt()+gasPurchaseinfo.getBgfcl_fertilizer_pvt())*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,(gasPurchaseinfo.getBgfcl_tea_gvt()+gasPurchaseinfo.getBgfcl_tea_pvt())*(gasPurchaseinfo.getTotal_bgfcl()/gasPurchaseinfo.getTotal_gtcl()));
					
					stmt.executeUpdate();
					
					
					
					i=1;
					stmt = conn.prepareStatement(sql_insert_total_purchase_sgfl);	
					stmt.setString(i++,pid);
					stmt.setString(i++,gasPurchaseinfo.getMonth());
					stmt.setString(i++,gasPurchaseinfo.getYear());					
					stmt.setDouble(i++,(gasPurchaseinfo.getBgfcl_power_gvt()+gasPurchaseinfo.getBgfcl_power_pvt())*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,(gasPurchaseinfo.getBgfcl_captive_gvt()+gasPurchaseinfo.getBgfcl_captive_pvt())*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,(gasPurchaseinfo.getBgfcl_cng_gvt()+gasPurchaseinfo.getBgfcl_cng_pvt())*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,(gasPurchaseinfo.getBgfcl_industry_gvt()+gasPurchaseinfo.getBgfcl_industry_pvt())*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,(gasPurchaseinfo.getBgfcl_comm_gvt()+gasPurchaseinfo.getBgfcl_comm_pvt())*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,(gasPurchaseinfo.getBgfcl_dom_meter_gvt()+gasPurchaseinfo.getBgfcl_dom_meter_pvt()+gasPurchaseinfo.getBgfcl_dom_nmeter_gvt()+gasPurchaseinfo.getBgfcl_dom_nmeter_pvt())*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,(gasPurchaseinfo.getBgfcl_fertilizer_gvt()+gasPurchaseinfo.getBgfcl_fertilizer_pvt())*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					stmt.setDouble(i++,(gasPurchaseinfo.getBgfcl_tea_gvt()+gasPurchaseinfo.getBgfcl_tea_pvt())*(gasPurchaseinfo.getTotal_sgfl()/gasPurchaseinfo.getTotal_gtcl()));
					
					stmt.executeUpdate();
					
					
					
					
					/*-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
					
				
				
				transactionManager.commit();
				
				response.setMessasge("Successfully saved Gas Purchase information.");
				response.setResponse(true);
				

			} 
			catch (Exception e){
				response.setMessasge(e.getMessage());
				response.setResponse(false);
				e.printStackTrace();
					try {
						transactionManager.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
			}
	 		finally{try{stmt.close();} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 		return response;

	}
	
}
