<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="/JGTDSL_WEB/resources/css/template/report.css" rel="stylesheet" type="text/css" />
<div class="row-fluid">							
	<div class="span3">	
		<div class="report">
		  <div class="box-collection">
		    <ul class="column">
				<s:if test="%{reportType=='sales'}">
				    <script  type="text/javascript">
						navCache("reportHome.action?reportType=sales");
						setTitle("Sales Reports");
					</script>
					
					<li class="boxG" onclick="ajaxLoad('report_input_div','salesReportHome.action')"><span>Sales Statement</span> </li>
					<s:if  test="#session.role=='Super Admin' || #session.role=='Manager'">
					<!--  disabling sales statement summery ~ oct 24 ~ Prince
					<li class="boxG" onclick="ajaxLoad('report_input_div','salesSummaryHome.action')"><span>Sales Summary Statement</span> </li>
					-->
					</s:if>
					<li class="boxG" onclick="ajaxLoad('report_input_div','nonmeterLoadIncreaseHome.action')"><span>Load Change Information</span> </li>
					<li class="boxG" onclick="ajaxLoad('report_input_div','nonmeterReconnectionHome.action')"><span>Reconnection Information</span> </li>			
					<li class="boxG" onclick="ajaxLoad('report_input_div','nonmeterConnectionHome.action')"><span>Connection Information</span> </li>									
					<li class="boxG" onclick="ajaxLoad('report_input_div','nonmeterDisconnectionHome.action')"><span>Disconnection Information</span> </li>
					<li class="boxG" onclick="ajaxLoad('report_input_div','disconnHome.action')"><span>Disconnection Information Details</span> </li>
					<li class="boxG" onclick="ajaxLoad('report_input_div','meterReadingInfoHome.action')"><span>Meter Reading Info.</span> </li>
					<li class="boxG" onclick="ajaxLoad('report_input_div','nonMeterBillingSummeryHome.action')"><span>Non-Meter Billing Summary</span> </li>
					<li class="boxG" onclick="ajaxLoad('report_input_div','vatRebateHome.action')"><span>Income-Tax, Vat-Rebate, Freedom Fighter</span> </li>
					<li class="boxG" onclick="ajaxLoad('report_input_div','billPreviewHome.action')"><span>Bill Preview Report</span> </li>			
				</s:if>
				<s:if test="reportType=='collection'">
					<script  type="text/javascript">
						navCache("reportHome.action?reportType=collection");
						setTitle("Collections Reports");
					</script>
					<li class="boxV" onclick="ajaxLoad('report_input_div','collectionReportHome.action')"><span>Collection Report</span> </li>
					<li class="boxV" onclick="ajaxLoad('report_input_div','printBGExpireListWithIn365Days.action')"><span>BG Expire</span> </li>
					<li class="boxV" onclick="ajaxLoad('report_input_div','collectionStatementReportHome.action')"><span>Collection Statement</span> </li>									
					<li class="boxV" onclick="ajaxLoad('report_input_div','feesCollectionReportHome.action')"><span>Fees Collection</span> </li> 					
					<li class="boxV" onclick="ajaxLoad('report_input_div','bankBookReportHome.action')"><span>Bank Book Report</span> </li>
					<li class="boxV" onclick="ajaxLoad('report_input_div','securityBankBookHome.action')"><span>Security Bank Book Report</span> </li>
					<li class="boxV" onclick="ajaxLoad('report_input_div','securityDepositHome.action')"><span>Security Deposit Info</span> </li>
					<li class="boxV" onclick="ajaxLoad('report_input_div','variousMarginHome.action')"><span>Margin Report</span> </li>
					<li class="boxV" onclick="ajaxLoad('report_input_div','gasConsumptionHome.action')"><span>Gas Consumption Report</span> </li>
					<li class="boxV" onclick="ajaxLoad('report_input_div','totalSecurityHome.action')"><span>Total Security Deposit</span> </li>
					<li class="boxV" onclick="ajaxLoad('report_input_div','areaSecurityHome.action')"><span>Area Wise Security Deposit</span> </li>
					<li class="boxV" onclick="ajaxLoad('report_input_div','categoryWiseGasConsumption.action')"><span>Customer Wise Details Collection</span> </li>
					<li class="boxV" onclick="ajaxLoad('report_input_div','monthlyCollectionCustomerTypeWise.action')"><span>Monthly Collection Customer Type Wise</span> </li>
				</s:if>
				<s:if test="reportType=='journal'">
					<script  type="text/javascript">
						navCache("reportHome.action?reportType=journal");
						setTitle("Journal Reports");
					</script>
					<li class="boxY" onclick="ajaxLoad('report_input_div','salesjvHome.action')"><span>Sales JV</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','purchasejvHome.action')"><span>Purchase JV</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','tdsjvHome.action')"><span>TDS JV</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','bankBookjvHome.action')"><span>BankBook JV</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','securitybankBookjvHome.action')"><span>Security BankBook JV</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','vatRebatejvHome.action')"><span>VAT Rebate JV</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','freedomjvHome.action')"><span>Freedom Fighter JV</span> </li>
				</s:if>
				<s:if test="reportType=='others'">
					<script  type="text/javascript">
						navCache("reportHome.action?reportType=others");
						setTitle("Others Reports");
					</script>
					<li class="boxY" onclick="ajaxLoad('report_input_div','gasPurchaseHome.action')"><span>Gas Purchase info</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','defaulterCustomerInfoHome.action')"><span>Defaulter Customer Info.</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','maximumLoadExceedHome.action')"><span>Maximum Load Exceed info</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','securityDepositRequiredHome.action')"><span>Security Deposit Required Info</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','advancedPaidCustomerInfoHome.action')"><span>Advanced Paid Customer Info.</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','clearnessCertificateHome.action')"><span>Clearance Certification</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','certificationSummeryHome.action')"><span>Certification Summery</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','accountsPayableHome.action')"><span>Accounts Payable</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','balanceSheetHome.action')"><span>Customer Ledger Report</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','yearlyBalanceSheetHome.action')"><span>Balance Statement</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','securityAddjustHome.action')"><span>Security Adjustment Notice</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','jvHome.action')"><span>Journal Voucher Report</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','custListHome.action')"><span>Customer List</span> </li>
					<%-- <li class="boxY" onclick="ajaxLoad('report_input_div','securityNoticeHome.action')"><span>Security Notice Report</span> </li> --%>
				</s:if>
				<s:if test="reportType=='margin'">
					<script  type="text/javascript">
						navCache("reportHome.action?reportType=margin");
						setTitle("Margin Reports");
					</script>
					<li class="boxY" onclick="ajaxLoad('report_input_div','sgHome.action')"><span>SGFL Margin</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','bgHome.action')"><span>BGFCL Margin</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','avalueHome.action')"><span>Asset Value Margin</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','bapexHome.action')"><span>BAPEX Margin</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','distHome.action')"><span>Distribution Margin</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','dwmbHome.action')"><span>DWMB Margin</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','pdfHome.action')"><span>PDF Margin</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','gdfHome.action')"><span>GDF Margin</span> </li>
					<li class="boxY" onclick="ajaxLoad('report_input_div','gtclHome.action')"><span>GTCL Margin</span> </li>
				</s:if>
    		</ul>
  		</div>
	</div>
	</div>
	<div class="span9" style="padding-top: 30px;">
			<div id="report_input_div"></div>
	</div>
</div>


