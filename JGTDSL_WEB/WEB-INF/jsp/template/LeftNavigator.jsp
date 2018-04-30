<div style="padding:2px 1px 1px 1px; font-size:15px">		    
<div id="menu-accordion">

<s:if test="#session.role=='Super Admin'">
	<section>
		<h3>
			<span class="z-icon"><i class="fa fa-umbrella baseline"> </i>
			</span>			
			Master Data
		</h3>
		<div>
			<div class="list-group">
	  			<a href="javascript:void(0)" onclick="callAction('getUserList.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_user_16.png" />
					User Information
				</a>
				<a href="javascript:void(0)" onclick="callAction('getAreaList.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_area_16.png" />
					Area/Region Information
				</a>
				<a href="javascript:void(0)" onclick="callAction('getCustomerCategoryList.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_category_16.png" />
					Category Information
				</a>
				<a href="javascript:void(0)" onclick="callAction('getTariffList.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_tariff_16.png" />
					Tariff/Rate
				</a>
				<a href="javascript:void(0)" onclick="callAction('getTariffDistribution.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_tariff_16.png" />
					 Tariff/Rate Distribution
				</a>
				<a href="javascript:void(0)" onclick="callAction('getBankList.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_bank_16.png" />
					Bank Information
				</a>
				<a href="javascript:void(0)" onclick="callAction('getBranchList.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_branch_16.png" />
					Branch Information
				</a>
				<a href="javascript:void(0)" onclick="callAction('getAccountList.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_account_16.png" />
					Account Information
				</a>
				<a href="javascript:void(0)" onclick="callAction('getMeterTypeList.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_meter_16.png" />
					Meter Type
				</a>
				<a href="javascript:void(0)" onclick="callAction('getDepositTypeList.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_deposit_16.png" />
					Deposit Type
				</a>
				<a href="javascript:void(0)" onclick="callAction('holidayHome.action')" class="list-group-item">
		  			<img src="/JGTDSL_WEB/resources/images/icons/sb_holiday_16.png" />
		  			Holidays
		  		</a>
			</div>
		</div>	
	</section>
</s:if>

<s:if test="#session.role=='Super Admin' || #session.role=='Manager' || #session.role=='Assistant Manager'">					
	<section>
		<h3>
			<span class="z-icon"><i class="fa fa-users baseline"> </i></span>	
			Customer Management
		</h3>
		<div>
			<div class="list-group">
		  		<a href="javascript:void(0)" onclick="callAction('getCustomerList.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_customer_list_16.png" />
					Customer List
				</a>				
				<a href="javascript:void(0)" onclick="callAction('editCustomerInfoHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_edit_customer_16.png" />
					Edit Customer Info
				</a>	
				<a href="javascript:void(0)" onclick="callAction('ownershipChange.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_change_ownership_16.png" />
					Change Ownership
				</a>										
				<a href="javascript:void(0)" onclick="callAction('meterInformationHome')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_meter_info_16.png" />
					Meter Information
				</a>
				<a href="javascript:void(0)" onclick="callAction('meterRepairmentHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_meter_repair_16.png" />
					Meter Repairment <font style="color: gray;"> [Non-Functional]</font>
					 <!-- callAction('meterRepairmentHome.action') javascript:void(0)  -->
				</a>
				<a href="javascript:void(0)" onclick="callAction('meterReplacementHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_meter_replace_16.png" />
					Meter Change/Replacement
				</a>	
				<a href="javascript:void(0)" onclick="callAction('meterDisconnectionHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_disconnect_metered_16.png" />
					Disconnection(Metered)
				</a>						  
			<!-- 	<a href="javascript:void(0)" onclick="callAction('nonMeterDisconnectionHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_disconnect_nonmetered_16.gif" />
					Disconnection(Non-Metered)
				</a> -->
				<a href="javascript:void(0)" onclick="callAction('meterReconnectionHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_reconnect_metered.png" />
					Reconnection(Metered)
				</a>						  
			<!-- 	<a href="javascript:void(0)" onclick="callAction('nonMeterReconnectionHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_reconnect_nonmetered.png" />
					Reconnection(Non-Metered)
				</a> -->
				<a href="javascript:void(0)" onclick="callAction('loadPressureChangeHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_load_pressure_change_16.png" />
					Load-Pressure Change
				</a>
				<a href="javascript:void(0)" onclick="callAction('burnerQntChangeHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_burner_16.png" />
					Burner Qnt. Increase/Decrease
				</a>
				<a href="javascript:void(0)" onclick="callAction('meterRentChangeHome.action')" class="list-group-item">
		  			<img src="/JGTDSL_WEB/resources/images/icons/sb_mrent_16.png" />
		  			Meter Rent Change
		  		</a>
		  		<a href="javascript:void(0)" onclick="callAction('bankGarantieManagementHome.action')" class="list-group-item">
		  			<img src="/JGTDSL_WEB/resources/images/icons/sb_mrent_16.png" />
		  			BG Management
		  		</a>
		  		<a href="javascript:void(0)" onclick="callAction('customerTypeChangeHome.action')" class="list-group-item">
		  			<img src="/JGTDSL_WEB/resources/images/icons/sb_mrent_16.png" />
		  			Customer Type Change
		  		</a>
		  		
		  		<a href="javascript:void(0)" onclick="callAction('hhvVatRebateChangHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_load_pressure_change_16.png" />
					 Change
				</a>
				
				<a href="javascript:void(0)" onclick="callAction('securityAdjustmentHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_load_pressure_change_16.png" />
					 Security Adjustment
				</a>
		  		
		  		
		  		<!--  <s:if test="#session.role=='Super Admin'">
				<!-- callAction('billCreationHome.action?bill_parameter.isMetered_str=0') -->
			
				<!--<a href="javascript:void(0)" onclick="callAction('hhvVatRebateChangHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_load_pressure_change_16.png" />
					 Change
				</a>
				
				<a href="javascript:void(0)" onclick="callAction('securityAdjustmentHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_load_pressure_change_16.png" />
					 Security Adjustment
				</a>
			  	</s:if> -->
		  		
			</div>
		</div>
</section>
</s:if>

<s:if test="#session.role=='Super Admin' || #session.role=='Manager' || #session.role=='Assistant Manager' || #session.role=='Bank User' ">
		<section>
		<h3>
			<span class="z-icon">
				<i class="fa fa-money baseline"> </i></span>	
						Meter Reading & Billing
		</h3>
		<div>
			<div class="list-group">
				<s:if test="#session.role=='Super Admin' || #session.role=='Manager' || #session.role=='Assistant Manager'">
	  			<a href="javascript:void(0)" onclick="callAction('meterReadingHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_mreading_16.png" />
					Meter Reading Entry
				</a>
				<a href="javascript:void(0)" onclick="callAction('supplyOffHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_supplyoff_16.png" />
					Gas Supply Off Info.
				</a>
				<a href="javascript:void(0)" onclick="callAction('billCreationHome.action?bill_parameter.isMetered_str=1')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_bill_process_16.png" />
					Bill Creation(Metered)
				</a>
				<a href="javascript:void(0)" onclick="callAction('billCreationHome.action?bill_parameter.isMetered_str=0')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_calcualte_16.png" />
					Bill Creation(Non-Metered) <font style="color: gray;"></font>
				</a>
				<a href="javascript:void(0)" onclick="callAction('billCreationHome.action?bill_parameter.isMetered_str=ministry')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_calcualte_16.png" />
					Bill Creation(Ministry) <font style="color: gray;"></font>
				</a>
				<a href="javascript:void(0)" onclick="callAction('billCreationHome.action?bill_parameter.isMetered_str=installment')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_calcualte_16.png" />
					Bill Printing(Installment) <font style="color: gray;"></font>
				</a>
				<a href="javascript:void(0)" onclick="callAction('surchargeCalHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_surcharge_16.png" />
					Surcharge Calculation
				</a>
				<a href="javascript:void(0)" onclick="callAction('editSurcharge.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_surcharge_16.png" />
					Edit Surcharge
				</a>
				<a href="javascript:void(0)" onclick="callAction('billAdjustmentOtherHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_adjustment_16.png" />
					Bill Adjustment/Other Amount
				</a>
				  <a href="javascript:void(0)" onclick="callAction('salesAdjustmentHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_adjustment_16.png" />
					Sales Adjustment(Meter/Non-Meter)
				</a>
				<a href="javascript:void(0)" onclick="callAction('billAdjustmentOtherNonMeterHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_adjusment_non_meter.png" />
					Bill Adjustment(Non-Meter)
				</a>
				</s:if>
				<s:if test="#session.role=='Super Admin' || #session.role=='Manager'">
				<!-- callAction('billCreationHome.action?bill_parameter.isMetered_str=0') -->
			
					<a href="javascript:void(0)" onclick="callAction('installmentBillingHome.action')" class="list-group-item">
					<img src="/JGTDSL_WEB/resources/images/icons/sb_deposit_16.png" />
					Installment Billing
				</a>
				
			  	</s:if>
			 
			  	<!-- callAction('surchargeCalHome.action') -->
				
			  
		</div>
	</div>
	</section>

</s:if>	




















<s:if test="#session.role=='Super Admin' || #session.role=='Manager' || #session.role=='Assistant Manager' || #session.role=='Bank User' ">
		<section>
		<h3>
			<span class="z-icon">
				<i class="fa fa-money baseline"> </i></span>	
						Collection
		</h3>
		<div>
			<div class="list-group">
				<a href="javascript:void(0)" onclick="callAction('billCollectionHome.action')" class="list-group-item">
			  		<img src="/JGTDSL_WEB/resources/images/icons/sb_collection_16.png" />
			  		Bill Collection 
			  	(Metered)</a>
			  	<a href="javascript:void(0)" onclick="callAction('advancedCollectionHome.action')" class="list-group-item">
			  		<img src="/JGTDSL_WEB/resources/images/icons/sb_collection_16.png" />
			  		 Collection
			  	(Non-Metered)</a>
			  	
			  	<a href="javascript:void(0)" onclick="callAction('deleteCollection.action')" class="list-group-item">
			  		<img src="/JGTDSL_WEB/resources/images/icons/sb_collection_16.png" />
			  		Delete Collection</a>
			  	
			  	<a href="javascript:void(0)" onclick="callAction('codelessCollectionHome.action')" class="list-group-item">
			  		<img src="/JGTDSL_WEB/resources/images/icons/sb_collection_16.png" />
			  		 Collection
			  	(Codeless)</a>
			  	
			  	
			  	<a href="javascript:void(0)" onclick="callAction('installmentCollectionHome.action')" class="list-group-item">
			  		<img src="/JGTDSL_WEB/resources/images/icons/sb_collection_16.png" />
			  		Installment Bill Collection
			  	</a>
			  	<a href="javascript:void(0)" onclick="callAction('adjustmentCollectionHome.action')" class="list-group-item">
			  		<img src="/JGTDSL_WEB/resources/images/icons/sb_collection_16.png" />
			  		Adjustment Bill Collection
			  	</a>
		</div>
	</div>
	</section>

</s:if>	


























<s:if  test="#session.role=='Super Admin' || #session.role=='Manager' || #session.role=='Assistant Manager' || #session.role=='Bank User' ">
<section>
	<h3>
		<span class="z-icon">
			<i class="fa fa-bar-chart-o baseline"> </i>
		</span>	
		Reporting
	</h3>
	<div>
		<div class="list-group">
		  <a href="javascript:void(0)" onclick="callAction('reportHome.action?reportType=sales')" class="list-group-item">
		  	<img src="/JGTDSL_WEB/resources/images/icons/sb_report_sales_16.png" />
		  	Sales
		  </a>
		  <a href="javascript:void(0)" onclick="callAction('reportHome.action?reportType=collection')" class="list-group-item">
		  	<img src="/JGTDSL_WEB/resources/images/icons/sb_report_collection_16.png" />
		  	Collection
		  </a>
		  <a href="javascript:void(0)" onclick="callAction('reportHome.action?reportType=journal')" class="list-group-item">
     		<img src="/JGTDSL_WEB/resources/images/icons/sb_demandnote_16.png" />
     		Journal Voucher
   		  </a>		  
		  <a href="javascript:void(0)" onclick="callAction('reportHome.action?reportType=others')" class="list-group-item">
		  	<img src="/JGTDSL_WEB/resources/images/icons/sb_report_others_16.png" />
		  	Others
		  </a>
		  <a href="javascript:void(0)" onclick="callAction('reportHome.action?reportType=margin')" class="list-group-item">
		  	<img src="/JGTDSL_WEB/resources/images/icons/sb_report_others_16.png" />
		  	Margin Payable
		  </a>
		</div>
	</div>
	</section>
</s:if>		
			
<s:if test="#session.role=='Super Admin' || #session.role=='Manager' || #session.role=='Assistant Manager'">			
	<section>
	<h3>
		<span class="z-icon"><i class="fa fa-university"></i></span>	
		Bank Accounts Management
	</h3>
	<div>
		<div class="list-group">
		  <a href="javascript:void(0)" onclick="callAction('bankDepositWithdrawHome.action')" class="list-group-item">						  
		  <img src="/JGTDSL_WEB/resources/images/icons/sb_dollar_16.png" />
		  Bank Deposit/Withdraw</a>
		  <a href="javascript:void(0)" onclick="callAction('transactionAuthorization.action')" class="list-group-item">						  
		  <img src="/JGTDSL_WEB/resources/images/icons/sb_authorization_16.png" />
		  Transaction Authorization</a>
		  <a href="javascript:void(0)" onclick="callAction('bankbook.action')" class="list-group-item">						  
		  <img src="/JGTDSL_WEB/resources/images/icons/sb_authorization_16.png" />
		  Bank Book</a>
		  <a href="javascript:void(0)" onclick="callAction('securityDepositExpList.action')" class="list-group-item">						  
		  <img src="/JGTDSL_WEB/resources/images/icons/sb_security_deposit_exp_16.png" />
		  Security Depsoit Exp. List</a>
		  <a href="javascript:void(0)" onclick="callAction('iBankingXlsUploadHome.action')" class="list-group-item">						  
		  <img src="/JGTDSL_WEB/resources/images/icons/sb_security_deposit_exp_16.png" />
		  iBanking Xls Upload</a>
		  <a href="javascript:void(0)" onclick="callAction('addAdjustmentAccountPayableHome.action')" class="list-group-item">						  
		  <img src="/JGTDSL_WEB/resources/images/icons/sb_security_deposit_exp_16.png" />
		  Adjustment(Account Payable)</a>
		  
		  
		</div>
	</div>
	</section>
	<section>
	<h3>
		<span class="z-icon">
			<i class="fa fa-university"></i>
		</span>	
		Correction
	</h3>
	<div>
		<div class="list-group">
		  <a href="javascript:void(0)" onclick="callAction('journalVoucher.action')" class="list-group-item">						  
		  <img src="/JGTDSL_WEB/resources/images/icons/sb_dollar_16.png" />
		  Journal Voucher</a>						  
		</div>
	</div>
	</section>
</s:if>
	

<s:if test="#session.role=='Super Admin' || #session.role=='Manager' || #session.role=='Assistant Manager'">
	<section>
	<h3>
		<span class="z-icon">
			<i class="fa fa-gears baseline"> </i>
		</span>	
		Administration
	</h3>
	<div>
		<div class="list-group">
		  <a href="javascript:void(0)" onclick="callAction('cacheMonitorHome.action')" class="list-group-item">Cache Monitor</a>
		  <a href="javascript:void(0)" onclick="callAction('changePasswordHome.action')" class="list-group-item">Change Password</a>
		</div>
	</div>
	</section>
</s:if>

<s:if test="#session.role=='Super Admin' || #session.role=='Manager' || #session.role=='Assistant Manager'">
	<section>
	<h3>
		<span class="z-icon">
			<i class="fa fa-gears baseline"> </i>
		</span>	
		SMS
	</h3>
	<div>
		<div class="list-group">
		  <a href="javascript:void(0)" onclick="callAction('smsHome.action')" class="list-group-item">SMS Service</a>
		</div>
	</div>
	</section>
</s:if>

<s:if test="#session.role=='Sales User'">
<section>
<h3>
	<span class="z-icon">
		<i class="fa fa-users baseline"> </i>
	</span>	
	Customer Management
</h3>
<div>
	<div class="list-group">
		  <a href="javascript:void(0)" onclick="callAction('newCustomer.action')" class="list-group-item">
		  		<img src="/JGTDSL_WEB/resources/images/icons/sb_customer_add_16.png" />
		  		Create New Customer
		  </a>
		  <!-- 
		  <a href="javascript:void(0)" onclick="callAction('newlyAppliedCustomerList.action')" class="list-group-item">
		  		<img src="/JGTDSL_WEB/resources/images/icons/sb_demandnote_customerList_16.png" />
		  		Newly Applied Customer List
		  </a>
		  
		  <a href="javascript:void(0)" onclick="callAction('demandNoteDataEntry.action')" class="list-group-item">
		  		<img src="/JGTDSL_WEB/resources/images/icons/sb_demandnote_entry_16.png" />
		  		Demand Note Data Entry
		  </a>
		   
		  <a href="javascript:void(0)" onclick="callAction('demandNoteDownloadHome.action')" class="list-group-item">
		  		<img src="/JGTDSL_WEB/resources/images/icons/sb_demandnote_16.png" />
		  		Download Demand Note
		  </a>
		  -->
		   <a href="javascript:void(0)" onclick="callAction('getCustomerListSales.action')" class="list-group-item">
		  		<img src="/JGTDSL_WEB/resources/images/icons/sb_customer_list_16.png" />
		  		Customer List
		  </a>
    </div>
</div>
</section>
<s:if test="#session.role=='Super Admin'">	
	<section>
	<h3>
		<span class="z-icon">
			<i class="fa fa-gears baseline"> </i>
		</span>	
		Account Settings
	</h3>
	<div>
		<div class="list-group">
		  <a href="javascript:void(0)" onclick="callAction('accountInfo.action')" class="list-group-item">Account Information</a>
		  <a href="javascript:void(0)" onclick="callAction('changePasswordHome.action')" class="list-group-item">Change Password</a>
		  <a href="javascript:void(0)" onclick="callAction('themeSelection.action')" class="list-group-item">Set Default Theme</a>
		</div>
	</div>
	</section>
</s:if>
<!-- 
<section>
<h3>
	<span class="z-icon">
		<i class="fa fa-gears baseline"> </i>
	</span>	
	Account Settings
</h3>
<div>

	<div class="list-group">
	  <a href="javascript:void(0)" onclick="callAction('accountInfo.action')" class="list-group-item">Account Information</a>
	  <a href="javascript:void(0)" onclick="callAction('changePasswordHome.action')" class="list-group-item">Change Password</a>
	  <a href="javascript:void(0)" onclick="callAction('themeSelection.action')" class="list-group-item">Set Default Home</a>
	</div>
</div>
</section>
 -->
 
</s:if>
</div>
</div>
