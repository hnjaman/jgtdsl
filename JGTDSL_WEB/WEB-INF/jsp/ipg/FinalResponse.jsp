<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  

<script type="text/javascript" src="/JGTDSL_WEB/resources/js/template/jquery-latest.js"></script>
 
<form>
Transaction Status: <input type="text"  value="<s:property value='txnStatus' />" style="width: 56%;"/><br>
Transaction ID from software: <input type="text"  value="<s:property value='transID' />" style="width: 56%;" /><br>
Transaction ID from Bank: <input type="text"  value="<s:property value='ipgTrxID' />" style="width: 56%;" /><br>
Error Msg: <input type="text"  value="<s:property value='error_msg' />" style="width: 66%;" /><br>
Card No: <input type="text"  value="<s:property value='card_no' />" style="width: 56%;" /><br>
Card Holder Name: <input type="text"  value="<s:property value='card_name'/>" style="width: 56%;" /><br>
</form>

<!-- 
<c:choose>
    <c:when test="${txnStatus == 'SUCCESS'}">
        <h3>jstl success txnStatus</h3>  
        <br />
    </c:when>    
    <c:otherwise>
      <h3>jstl failed txnStatus</h3>
        <br />
    </c:otherwise>
</c:choose>
 -->

<c:choose>
    <c:when test="${ipgResponse.txnStatus == 'SUCCESS'}">
        <h1 style="color: #009F72;font-size: 40px;text-align: center;margin-top: 150px">Payment Success <img src="./resources/images/ipg/success.png" style="width: 50px;"/></h1>
        <br />
    </c:when>    
    <c:otherwise>
      <h1 style="color: #f04251;font-size: 40px;text-align: center;margin-top: 150px">Payment Failed <img src="./resources/images/ipg/failed.png" style="width: 50px;"/></h1>
        <br />
    </c:otherwise>
</c:choose>

<!--  

    <s:if test="{txnStatus=='SUCCESS'}">
        success**
        <br />
    </s:if>    
    <s:else>
       failed**
        <br />
    </s:else>



<s:if test="{ipgResponse.txnStatus == 'SUCCESS'}">
Success
</s:if>
<s:else>
Failed
</s:else>

-->

