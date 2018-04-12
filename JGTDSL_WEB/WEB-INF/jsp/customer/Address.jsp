<div class="w-box">
	<div class="w-box-header">
		<h4>Address</h4>
	</div>
	<div class="w-box-content cnt_a">
		<div class="row-fluid">
			<div class="span12">
				<label> Division <m class='man' />
				</label> <select name="address.division_id" id="division_id"
					style="width: 39%;" onchange="fetchSelectBox(district_sbox);">
					<!--<option value="" selected="selected">Select Division</option> -->
					<option value="60">SYLHET</option>
					<!-- 
                                <s:iterator value="%{#application.ALL_DIVISION}" id="divisionList">
                                    <option value="<s:property value="division_id" />" >
                                        <s:property value="division_name" />
                                    </option>
                                </s:iterator>
                                 -->
				</select>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12">
				<label> District <m class='man' />
				</label> <select name="address.district_id" id="district_id"
					style="width: 39%;" onchange="fetchSelectBox(upazila_sbox);">
					<option value="" selected="selected">Select District</option>
				</select>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12">
				<label> Upazila/Thana <m class='man' />
				</label> <select name="address.upazila_id" id="upazila_id"
					style="width: 39%;">
					<option value="" selected="selected">Select Upazila/Thana</option>
				</select>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12">
				<label>Moholla</label> <select
					name="address.zone_id" id="zone_id" style="width: 39%;">
					<option value="" selected="selected">Select Zone</option>
					<s:iterator value="%{#application.ALL_ZONE}" id="zoneList">
						<option value="<s:property value="zone_id" />">
							<s:property value="zone_name" />
						</option>
					</s:iterator>
				</select>
			</div>
		</div>

		<div class="row-fluid">
			<div class="span12">
				<label> Road/House Number <m class='man' />
				</label> <input type="text" name="address.road_house_no" id="road_house_no"
					style="width: 37%;" />
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12">
				<label>Post Office</label> <input type="text"
					name="address.post_office" id="post_office" style="width: 37%;" />
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12">
				<label>Post Code</label> <input type="text" name="address.post_code"
					id="post_code" style="width: 37%;" />
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12">
				<label>Address Line 1</label> <input type="text"
					name="address.address_line1" id="address_line1" style="width: 37%;" />
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12">
				<label>Address Line 2</label> <input type="text"
					name="address.address_line2" id="address_line2" style="width: 37%;" />
			</div>
		</div>
	</div>
</div>