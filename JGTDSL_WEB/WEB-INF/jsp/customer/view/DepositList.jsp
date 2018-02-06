<%@ taglib prefix="s" uri="/struts-tags"%>
<table id="demoTable" style="border: 1px solid #ccc;" cellspacing="0" width="100%">
        <thead>
                <tr>
                        <th sort="serial" style="text-align: center;" width="30%">Date</th>
                        <th sort="description"  style="text-align: left;" width="70%">Deposit Purpose</th>
                </tr>
        </thead>
        <tbody>               
                <s:iterator value="depositList">
                	<tr onclick="$('#depositDetailDiv').html(jsImg.SETTING).load('getDepositDetail.action?deposit_id=<s:property value="deposit_id" />');">
                        <td align="center"><s:property value="deposit_date" /></td>
                        <td><s:property value="str_deposit_purpose" /></td>
                	</tr>					                
                </s:iterator>
        </tbody>
        <tfoot class="nav">
            <tr>
                <td colspan=2>
                        <div class="status"></div>
                </td>
            </tr>
            <tr>
                <td colspan=2>
                        <div class="selectPerPage"></div>
                </td>
            </tr>
            <tr>
                <td colspan=2>
                        <div class="jtps_pagination"></div>                        
                        <div class="paginationTitle">Page</div>
                </td>
            </tr>
                        
            
        </tfoot>
</table>

<script>

                $(document).ready(function () {
                
                        $('#demoTable').jTPS( {perPages:[10,20,'ALL'],scrollStep:1,scrollDelay:30,
                                clickCallback:function () {     
                                        // target table selector
                                        var table = '#demoTable';
                                        // store pagination + sort in cookie 
                                        document.cookie = 'jTPS=sortasc:' + $(table + ' .sortableHeader').index($(table + ' .sortAsc')) + ',' +
                                                'sortdesc:' + $(table + ' .sortableHeader').index($(table + ' .sortDesc')) + ',' +
                                                'page:' + $(table + ' .pageSelector').index($(table + ' .hilightPageSelector')) + ';';
                                            //$('.stubCell').remove(); //Added by Ifti    
                                }
                        });

                        // reinstate sort and pagination if cookie exists
                        var cookies = document.cookie.split(';');
                        for (var ci = 0, cie = cookies.length; ci < cie; ci++) {
                                var cookie = cookies[ci].split('=');
                                if (cookie[0] == 'jTPS') {
                                        var commands = cookie[1].split(',');
                                        for (var cm = 0, cme = commands.length; cm < cme; cm++) {
                                                var command = commands[cm].split(':');
                                                if (command[0] == 'sortasc' && parseInt(command[1]) >= 0) {
                                                        $('#demoTable .sortableHeader:eq(' + parseInt(command[1]) + ')').click();
                                                } else if (command[0] == 'sortdesc' && parseInt(command[1]) >= 0) {
                                                        $('#demoTable .sortableHeader:eq(' + parseInt(command[1]) + ')').click().click();
                                                } else if (command[0] == 'page' && parseInt(command[1]) >= 0) {
                                                        $('#demoTable .pageSelector:eq(' + parseInt(command[1]) + ')').click();
                                                }
                                        }
                                }
                        }

                        // bind mouseover for each tbody row and change cell (td) hover style
                        $('#demoTable tbody tr:not(.stubCell)').bind('mouseover mouseout',
                                function (e) {
                                        // hilight the row
                                        e.type == 'mouseover' ? $(this).children('td').addClass('hilightRow') : $(this).children('td').removeClass('hilightRow');
                                }
                        );
						$('.stubCell').remove(); //Added by Ifti
                });

        </script>          