<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="sw-basic-step-2">
    <h4 class="StepTitle">Customer's Address Information</h4>
    <div class="row-fluid">
        <div class="span6">
            <div class="w-box">
                <div class="w-box-header">
                    <h4>Address</h4>
                </div>
                <div class="w-box-content cnt_a">
                    <div class="row-fluid">
                        <div class="span12">
                            <label>Division</label>
                            <b>:</b> <s:property value="customer.addressInfo.division_name"/>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <label>District</label>
                            <b>:</b> <s:property value="customer.addressInfo.district_name"/>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <label>Upazila/Thana</label>
                            <b>:</b> <s:property value="customer.addressInfo.upazila_name"/>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <label>Road/House Number</label>
                            <b>:</b> <s:property value="customer.addressInfo.road_house_no"/>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <label>Post Office</label>
                            <b>:</b> <s:property value="customer.addressInfo.post_office"/>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <label>Post Code</label>
                            <b>:</b> <s:property value="customer.addressInfo.post_code"/>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <label>Address Line 1</label>
                            <b>:</b> <s:property value="customer.addressInfo.address_line1"/>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <label>Address Line 2</label>
                            <b>:</b> <s:property value="customer.addressInfo.address_line2"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>