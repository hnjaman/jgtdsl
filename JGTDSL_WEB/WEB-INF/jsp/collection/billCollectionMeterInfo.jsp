<div class="row-fluid" id="metered_bill_amount">														
								<div class="span12">
									<label style="width: 40%">Bill Amount</label>
									<input type="text" id="billed_amount" name="collection.billed_amount" style="text-align: right;font-weight: bold;color: red;width: 51%;font-size: 15px" readonly="readonly"/>  				
								</div>
							</div>
						
						
							<div class="row-fluid" id="metered_surcharge">														
								<div class="span12">
									<label style="width: 40%">Surcharge Amount</label>
									<input type="text" name=""  id="surcharge_amount" name="collection.surcharge_amount_metered" tabindex="3" style="text-align: right;color: blue;width: 51%" readonly="readonly"/>
								</div>  
								
								
							</div>
							<div class="row-fluid" id="vat_rebate_div">														
								<div class="span12">
									<label style="width: 40%">Vat Rebate</label>
									<input type="text" name="collection.vat_rebate_amount" id="vat_rebate_amount" tabindex="4" style="text-align: right;width: 51%" readonly="readonly"/>
								</div>
							</div>
						
							<div class="row-fluid" id="tax_amount_div">						    
								<div class="span12">
									<label style="width: 40%">Tax Amount/ Chalan No/ Chalan Date</label>
									<input type="text" name="collection.tax_amount" id="tax_amount" tabindex="5" style="text-align: right;width: 15%;"  value="0" onkeyup="recalculateCollection(this.value)"/>
									<input type="text" name="collection.tax_no" id="tax_no" tabindex="2" style="text-align: right;font-weight: bold;color: green;width: 15%;"/>
									<input type="text" name="collection.tax_date" id="tax_date" tabindex="2" style="text-align: right;font-weight: bold;color: green;width: 15%;"/>
								</div>
															
							</div>
							<div class="row-fluid" id="adjustment_div">						    
								<div class="span12">
									<label style="width: 40%">Adjustment</label>
									<input type="text" name="collection.adjustment_amount" id="adjustment_amount" tabindex="5" style="text-align: right;width: 51%"  value="0" readonly="readonly"/>
								</div>	
															
							</div>
							<div class="row-fluid" id="net_payable_amount">
								<div class="span12">
									<label style="width: 40%">Net Payable Amount</label>
									<input type="text" name="collection.payable_amount" id="payable_amount" style="text-align: right;font-weight: bold;color: red;width: 51%" readonly="readonly"/>
								</div>													
							</div>
							<div class="row-fluid" id="collection_div">
								<div class="span12">
									<label style="width: 40%">Collection Amount</label>								
									<input type="text" name="collection.collected_amount" id="collected_amount" tabindex="2" style="text-align: right;font-weight: bold;color: black;width: 51%; background-color: #b3ffff;"/>
								</div>														
							</div>
						