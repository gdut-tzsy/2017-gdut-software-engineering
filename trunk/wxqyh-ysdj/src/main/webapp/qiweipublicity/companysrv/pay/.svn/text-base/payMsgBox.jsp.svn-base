<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!-- 支付密码输入 -->
<script src='${baseURL}/themes/manager/companysrv/js/pwd.js?ver=<%=jsVer%>'></script>
<div class="EditPay none" id="payPwdInputBox">
    <div class="Box-til">
        <i class="Box-til-close"></i>
    </div>
    <div class="Box-main">
        <p class="fz22 mb30">请输入支付密码</p>
        <div class="item">
            <span class="">支付金额：</span>
            <span class="Amount" id="payInputAmount">￥1,000.00</span>
        </div>
        <div class="item mb0">
            <span class="">支付密码：</span>
                  <span class="clearfix" id="passW" style="position:relative;">
                    <input type="password" class="psw" />
                    <input type="password" class="psw" />
                    <input type="password" class="psw" />
                    <input type="password" class="psw" />
                    <input type="password" class="psw" />
                    <input type="password" class="psw" />
                  </span>
        </div>
        <div class="clearfix ml173 fz14">
            <span class="c999" id="inputPwdTips">请输入6位数字密码</span>
            <a href="${baseURL}/qiweipublicity/experience2/mail_Retrieve1.jsp?resetType=PAY" target="_blank" class="orange ml30">忘记密码？</a>
        </div>
    </div>
    <div class="Box-btn">
        <input class="ml17" type="button" value="确认支付" id="payPwdInputBox_ok">
        <input type="button" style="display:none;"  id="jcAccountPayBtn">
    </div>
</div>

<!--支付结果弹窗-->
<div class="new_popBox w500 none" id="payResultBox">
    <i class="clossClass" style="display:none;" id="clossXId">X</i>
    <div class="mt50 con">
        <img class="resultIMG" src="" alt="" />
        <p class="fz18 mt5 mb30 resultClass"style="color:#53d553"></p>
       <input style="display:none;" type="button" value="查看订单" id="btnGoOrderList" class="o-btn mr20 w100 OKClass" onclick="goOrderList();"/>
       <%-- <input style="display:none;" type="button" value="开发票" id="btn_goInvoiceReq" class="b-btn w100" onclick="goInvoice();" />--%>
        <input style="display:none;" type="button" value="确定" class="b-btn w100 FailClass" id="resultOk"/>
    </div>
    <div class="tcenter mb50"></div>
</div>

<script language="JavaScript">
    $(function(){
        //绑定关闭按钮
        $(".clossClass").on("click",function(){
            $("#overlayDiv").hide();
            $(this).parent().hide();
        });
        $(".Box-til-close").on("click",function(){
            $("#overlayDiv").hide();
            $(this).parent().parent().hide();
        });

        pswinput("passW");
    });

    //跳转到vip主页
    function goVipIndex(){
        document.location.href=baseURL+"/qiweipublicity/companysrv/vip/vip_index.jsp";
    }

    //跳转到订单列表
    function goOrderList(){
        document.location.href=baseURL+"/manager/companysrv/order_list.jsp";
    }

    //跳转到开票页面
    function  goInvoice(){
        var invoiceContent = "${invoiceContent}";
        document.location.href=baseURL+"/manager/companysrv/invoice_request.jsp" +
                "?orderId="+$("#orderId").val()+"&payWay="+changePayWayIntValue($("#payWay").val())+"&amount="+$("#amount").val()+"&sourceType=1&invoiceContent="+encodeURIComponent(invoiceContent);
    }

    //将支付方式value转成code
    function changePayWayIntValue(payWay){
        if("ACCOUNT"==payWay){
            return "0";
        }
        if("WECHAT"==payWay){
            return "1";
        }
        if("ALI"==payWay){
            return "2";
        }
        if("UNIONBANK"==payWay){
            return "3";
        }
        if("NONE"==payWay){
            return "4";
        }
        if("WECHATPHONE"==payWay){
            return "5";
        }
        if("TAOBAO"==payWay){
            return "90";
        }
        if("GUANGFA"==payWay){
            return "91";
        }
        if("MINSHENG"==payWay){
            return "92";
        }
        if("HAND_ALI"==payWay){
            return "93";
        }
        if("HAND_WECHAT"==payWay){
            return "94";
        }
        if("HAND"==payWay){
            return "99";
        }
        return payWay;
    }

    //显示支付结果框
    function showPayResult(result,msg){
        if(result=="OK"){
            //$("#payResultBox").find(".OKClass").show();
            $("#payResultBox").find(".FailClass").hide();
            $("#payResultBox").find(".resultIMG").attr("src","../../../themes/manager/companysrv/images/icon_success.png");
            if(msg){
                $("#payResultBox").find(".resultClass").html("支付成功"+"<br/>"+msg);
            }else{
                $("#payResultBox").find(".resultClass").html("支付成功");
            }

            var orderId = $("#orderId").val();
            $.ajax({
                type:"POST",
                url: baseURL+"/trademgr/trademgrAction!schOrderInvoice.action?orderId="+orderId,
                dataType: "json",
                success: function(result){
                    if ("0" == result.code) {
                        if("0"==result.data.invoiceStatus){
/*                            var goInvoiceReqFunction = setInterval(function(){
                                clearInterval(goInvoiceReqFunction);
                                goInvoice();
                            },1000); //1秒钟后跳转*!/*/
                            goInvoice();
                        }else{
                            $("#btnGoOrderList").show();
                        }
                    }else{
                        $("#btnGoOrderList").show();
                    }
                },
                error:function(){
                    _alert("","网络错误",1);
                }
            });

        }else{
            $("#payResultBox").find(".OKClass").hide();
            $("#payResultBox").find(".FailClass").show();
            $("#payResultBox").find(".resultIMG").attr("src","../../../themes/manager/companysrv/images/icon_fail.png");
            $("#payResultBox").find(".resultClass").html("支付失败");
            if(msg){
                $("#payResultBox").find(".resultClass").html("支付失败"+"<br/>"+msg);
            }else{
                $("#payResultBox").find(".resultClass").html("支付失败");
            }
        }
        $("#overlayDiv").show();
        $("#payResultBox").show();
    }

    //显示支付密码输入框
    function showPayInputBox(money){
        $("#payInputAmount").html(money);
        $("#overlayDiv").show();
        $("#payPwdInputBox").show();
    }

</script>
