var template2 = ''
   	+'<li class="ccli" style="display:@Show;height: 45px;"> '
   	+'    <input type="hidden" id="relatives" name="relatives" value="@UserId" /> '
   	+'    <a class="remove_icon" href="javascript:void()" onclick="deleteUser(this,\'cc\',@ArrayValue)" ></a> '
   	+'    <p class="img"> '
   	+'        <img src="@HeadPic" onerror="javascript:replaceUserHeadImage(this);" alt="" /> '
   	+'    </p> '
   	+'    <p class="name">@UserName</p> '
   	+'</li>';
var creator;			//创建人
var status="";
//加载任务详情内容
$(document).ready(function () {
		var id=$("#expressid").val();
    	showLoading();
        $.ajax({
            type:"POST",
            url: baseURL+"/express/expressAction!getDetail.action",
            data:{id:id},
            cache:false,
            dataType:"json",
			success : function(result) {
				if(result.code!='0'){
					hideLoading();
					showMsg("",result.desc,1,{ok:function(result){WeixinJS.back();}});
				}
				var detail = result.data.detail;
				$("#detailPic").attr("src",detail.headPic);
				$("#detailPic").attr("onclick","atThisPersonUtil('"+detail.creator+"','"+detail.personName+"','"+detail.headPic+"')");					
				$("#detailPerson").html(detail.personName);
				var ct = detail.createTime;
				if(ct.substring(0,10)==new Date().Format("yyyy-MM-dd")){
					$("#detailTime").html(ct.substring(11,16));
				}else{
					$("#detailTime").html(detail.createTime);
				}
				$("#detailTitle").html(detail.title);
				
				//显示图片
				var picList = detail.picList;
				if(picList && picList.length>0){
					$("#picdiv").show();
					for(var i=0;i<picList.length;i++){
						$("#topImage").append("<img src=\""+compressURL+picList[i].picPath+"\"/>");
					}
				}
				creator=detail.creator;
				var content=detail.content;
				if(content!=''){
					$("#remarkdiv").show();
					$("#detailContent").html(content);
				}
				//最后的下划线处理
				if(detail.remarks==''){
					$("#remarkdiv").addClass("borderBottommNone");
				}else{
					$("#closediv").addClass("borderBottommNone");
				}
				//显示收件人
				$("#to_user_div").show();
				$("#to_user").html(detail.receiveUser);
				if(detail.trackingNumber){
					$("#expressiddiv").show();//已领取快递显示快递单号
					$("#expresslist").html("<span >"+detail.trackingNumber+"</span>");
				}
				if(detail.receiveRefUser!=""){
					//代领人
					$("#cc_user_div").show();
					$("#cc_user").html(detail.receiveRefUser);
				}
				if(detail.payMoney && detail.payMoney>0){
					$("#pay_money_div").show();//显示到付金额
					$("#pay_money").html(detail.payMoney);
				}
				status=detail.status;
				if(detail.status=='1'){
					if(detail.remarks==''){
						if(detail.lastType=='1'){
							$("#to_user_div").addClass("kuaiti_lq");
						}else if(detail.lastType=='2'){
							$("#cc_user_div").addClass("kuaiti_dl");
						}
					}else{
						//创建人关闭
						$("#closediv").show();
						$("#closeremark").html(detail.remarks);
						$("#closediv").addClass("kuaiti_gb");
					}
					
					$("#isClosediv").show();
					$("#isCloseTime").html(detail.receiveTime);
				}
				hideLoading();
			},
			error : function() {
				hideLoading();
				showMsg("",internetErrorMsg,1,{ok:function(result){WeixinJS.back();}});
			}
	});
});
//返回列表
function combackList(){
	window.location.href=baseURL+"/jsp/wap/express/list.jsp?status="+status;
}