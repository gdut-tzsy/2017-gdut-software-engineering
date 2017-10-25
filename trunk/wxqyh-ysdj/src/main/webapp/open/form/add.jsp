<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>    
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
    <head>
        <meta charset="utf-8">
        <title>填写表单</title>
        <meta name="description" content="">
        <meta name="HandheldFriendly" content="True">
        <meta name="MobileOptimized" content="320">
        <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta content="telephone=no" name="format-detection" />
        <meta content="email=no" name="format-detection" />
         <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
        <meta name="description" id="descriptionId" content="">
        <meta name="shareImgUrl" id="shareImgUrlId" content="">
        <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/ku.css?ver=<%=jsVer%>" />
        <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/mobiscroll.2.13.2.min.css?ver=<%=jsVer%>" />
        <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/font-awesome.min.css?ver=<%=jsVer%>" />
		<script src='${resourceURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
		<script type="text/javascript" src="${resourceURL}/jsp/wap/js/mobiscroll.2.13.2.min.js"></script>
        <script type="text/javascript" src="${resourceURL}/jsp/wap/js/detect-jquery.js"></script>
        <script type="text/javascript" src="${resourceURL}/jsp/wap/js/flipsnap.js"></script>
        <script type="text/javascript" src="${resourceURL}/jsp/wap/js/wechat.js"></script>
		<script src='${resourceURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
		<script src="${resourceURL}/js/3rd-plug/jquery/jquery.form.js"></script>
	     <script src="${resourceURL}/js/wx/wx.js?ver=<%=jsVer%>"></script>
		<!-- 上传媒体文件（手机端页面）引入  start -->
	    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/uploadfile.js?ver=<%=jsVer%>"></script>
	    
        <script type="text/javascript" src="${resourceURL}/themes/wap/js/form/form.js?ver=<%=jsVer%>"></script>
		<!-- 上传媒体文件（手机端页面）引入  end -->
	    <script type="text/javascript" src="${apiMapBaidu}"></script>
		<script type="text/javascript" src="${convertorBaidu}"></script>
		<script type="text/javascript" src="${baseURL}/themes/qw/js/qrcode.js"></script>
    </head>    <style>
	    /*放在本页*/
		@media screen and (min-width: 800px){
		    .question_detail {background: #ffffff;width: 600px;margin: 50px auto 0 auto;}
		    .sForm{background-color: #efefef}
		    .sForm .form_btns{border-top: none}
		}
    </style>
	<body class="sForm">
	<div id="wrap_main" class="wrap">
		<div id="main" class="wrap_inner">
			<form  id="form" onsubmit="return false;" class="p5">
				<input type="hidden" name="id" id="id" value="${param.id}" />
				<input type="hidden" name="definition_id" id="definition_id" value="" />
				<input type="hidden" name="status" id="status" value="" />
               	<input type="hidden" id="longitude" name="longitude" value=""/>
                <input type="hidden" id="latitude" name="latitude" value=""/>
                <input type="hidden" id="formTitle" name="formTitle" value="" />
				<%--<input type="hidden" id="instanceTitle" name="instanceTitle"/>--%>
                <div class="question_detail">
                    <div class="" id="question_detail">
						<div class="new_title_box" id="ttitle">
							<textarea rows="1" class="inputStyle title" id="instanceTitle" maxlength="100" name="instanceTitle" valid="{must:true,maxLength:100,tip:'表单标题'}" placeholder="请填写标题"></textarea>
						</div>
						<div class="inner-f-item pt0" id="content" style="display:none"></div>
						<div class="suxing" id="form_time" style="display:none">
							<i class="fa fa-clock-o"></i><span id="formStop"></span>
						</div>
						<div class="form_btns mt10" style="display:none" id="timeout_div"></div>
						<div class="form_content">
							<div id="content2"></div>
							
							<div id="imageDiv" style="display:none">
								<div class="letter_bar">上传图片<font color="red" id="isFileMust_font" style="display: none">*</font></div>
								<div class="f-item">
									<div class="loadImg mb10 clearfix" id="upPicDiv"
										style="padding-left: 10px;">
										<div class="f-add-user-list">
											<ul id="imglist" class="impression">
												<li class="f-user-add" id="addimage"style="margin-bottom: 14px">
													<input id="imageFileInput" class="imageFileInput" accept="${imgFilterExp}" type="file"/></li>
												<li class="f-user-remove" show="0" onclick="removeImage(this,'imglist')"></li>
											</ul>
										</div>
									</div>
								</div>
							</div>
						</div>
						<!-- 上传媒体文件（手机端页面）引入  start -->
						<div class="form-style" id="medialist" style="display:none;">
							<div class="letter_bar file_top"><span class="file_top_tit">附件(0)</span><span class="file_top_btn" style="display:none;">
			               		<input type="file" name="file" id="fileFile1" fileName="mediaIds" class="upload_file_input" /><i>+</i>上传</span>
			            	</div>
						</div>
						<!-- 上传媒体文件（手机端页面）引入  end -->

						<div class="form_btns mt10" id="btn_div" style="display:none">
							<div class="inner_form_btns pb10">
								<div class="fbtns flexbox">
									<a class="fbtn btn flexItem" href="javascript:commitForm('1')">立即提交</a>
								</div>
							</div>
						</div>
					</div>
					<div class="submitSucceed" id="submitSucceed" style="display:none">
						<div class="submitSucceed_main">
							<p class="submitSucceed_icon"></p>
							<p style="padding-top: 30px;" class="mt20 c666 fz18">表单提交成功，感谢您的参与。</p>
							<%--<a class="mt10 mb20 c999 iblock" href="add.jsp?id=${param.id}">再次填写</a>--%>
							<a class="mt10 mb20 c999 iblock" href="javascript:returnWrite()">再次填写</a>
						</div>
					</div>
					<div class="submitSucceed" style="display: none" id="submitSucceedVip">
						<div class="submitSucceed_main">
							<p class="submitSucceed_icon"></p>
							<p class="mt20 c666 fz18">表单提交成功，感谢您的参与</p>
							<p><a class="mt10 mb20 c999 iblock" href="javascript:returnWrite()">再次填写</a></p>
							<p><a href="javascript:searchFormResult();" class="mt50 btn_green fz16">查看填单结果</a></p>
							<p class="mt10 c666">到结果页面收藏页面或将页面 <a href="javascript:showDialog();" style="color:#32cd32">发送到邮箱</a></p>
						</div>
					</div>
				</div>
			</form>
			 <%@include file="/open/include/footer.jsp"%>
		</div>
	</div>

	<div class="weui_dialog_confirm sentMail_dialog" id="sendEmail" style="display:none">
		<div class="weui_mask"></div>
		<div class="weui_dialog">
			<div class="weui_dialog_hd tleft"><strong class="weui_dialog_title">发送结果页面到邮箱</strong></div>
			<div class="weui_dialog_bd">
				<input type="text"  class="formInput" name="email" placeholder="请输入正确的邮箱" style="font-size:14px;">
				<p style="color:#ff3333;line-height:24px;display:none" class="mail_error">邮箱格式不正确</p>
			</div>
			<div class="weui_dialog_ft"><a href="javascript:closeDialog();" class="weui_btn_dialog default">取消</a><a href="javascript:checkMail();" class="weui_btn_dialog primary">确认</a></div>
		</div>
	</div>
	<%--用于获取简易二维码的src--%>
	<div class="weixin-qrcode pl20" style="display:none">
		<div class="weixin-qrcode-content" id="showQrcodeId"></div>
		<p>微信扫码打开页面</p>
		<b><em></em></b>
	</div>
	<script type="text/javascript">
		var tbFormWorkOrderId="";
		var isFileMust="";
		checkJsApi_image = false;
		var templateObj = {};
		var url="";//生成简易二维码的url
		var src="";

		$(window).on('load resize',function(){
			if($(window).width()>=800){
			    $('.submitSucceed').height($(window).height()-256);
			}else{
				$('.submitSucceed').height($(window).height()-56);
			}
		});
	    var chooseBoxOption=[];//审批流程选择表
	        $(function(){
	        	wxqyh.agent="form";//应用code
	    		showLoading("正在加载中...");
	    		var id=$("#id").val();
	    		$.ajax({
	    			url:"${baseURL}/open/openFormAction!getFrom.action",
	    			type:"POST",
	    			data:{id:id},
	    			dataType:"json",
	    			success:function(result){
	    				if(result.code=="1999" || result.code!="0"){
	        				hideLoading();
	    					showMsg("",result.desc,1,{ok:function(result){WeixinJS.back();}});
	    					return;
	    				}
	    				
	    				if(result.data.isShowFooter==true){
	                    	showFooter(true);
	                    }
	    				
	    				var detailsPO=result.data.detailsPO;
	    				window.currentFormId=detailsPO.definitionVersionsId;
	    				var controlPO=result.data.controlPO;
						definitionId=controlPO.definitionId;
	    				if(result.data.equationPOList){
	    					var equationPOList=result.data.equationPOList;
	    					for(var i=0;i<equationPOList.length;i++){
	    						equationList.push(equationPOList[i].equationval+"|"+equationPOList[i].equationKeys+"|"+equationPOList[i].equation);
	    					}
	    				}

						if(result.data.statisticsFieldList){
							statisticsList=result.data.statisticsFieldList;
						}
		    			quotaJson = result.data.quota;
						$("#definition_id").val(result.data.id);

	    				var title="";
	    				if(detailsPO.title!=null &&detailsPO.title!=""){
	    					$("#formTitle").val(detailsPO.title);
	    					title = detailsPO.formName;
	    				}else{
	    					$("#formTitle").val(detailsPO.formName);
		    				title = detailsPO.formName;
	    				}
						if(controlPO.isUpdaeTitle==0){
							$("#instanceTitle").attr("readonly",true);
						}
	    				//分享信息
	    				$("#detail_title").show();
		    			document.title = title;
						var logo = result.data.logo;
						logo = ((!logo || logo=="")?"${resourceURL}/themes/qw/images/logo/logo400.png":logo);
						setDataForWeixinValue(title,logo,"["+result.data.orgName+"]创建了自定义数据表单，邀请你参与",window.location.href);
	    				if(detailsPO.content!=null &&detailsPO.content!=""){
	    					var content=detailsPO.content.replace(/@fileweb@/g,localport);
	    					$("#content").html(checkURL(content,false));
							videoPlayback();
	    					$("#content").show();
	    				}
	    				if(controlPO.stopTime!=null && controlPO.stopTime!=""){
	        				$("#formStop").html("截止时间："+controlPO.stopTime);
	    					$("#form_time").show();
	    				}
	    				//如果表单未开始填写
	    				if(result.data.timebefore){
		    				$("#timeout_div").html("<div class=\"inner_form_btns\"><div class=\"timeout-bar\">表单开始填写时间："+controlPO.startTime+"</div></div>");
		    				$("#timeout_div").show();
		    				hideLoading();
		    				return;
	    				}
	    				if(controlPO.isPic=="1"){
	    					if("1"==controlPO.isFileMust){
	    						isFileMust="1";
	    						$("#isFileMust_font").show();
	    					}
	    					$("#imageDiv").show();				//显示图片
	    				}
					    wxqyh_uploadfile.agent="form";//应用code
						if(controlPO.formPhotoSet&&"0"==controlPO.formPhotoSet){//是否允许选择图片
							chooseImageTypes=['album','camera'];
						}else{
							chooseImageTypes=['camera'];
						}
					     //水印
	    				if(result.data.isImgWatermark){
	    					wxqyh_uploadfile.orderId=id;
	    					$("#uploadImgOrderId").val(wxqyh_uploadfile.orderId)
	    				}
	    				var filetype = 1;
						var isNeedBindingAccessory=false;//是否需要去初始化文件上传代码
						if(controlPO.isFile=="1"){
							isCanUploadFile=true;
							isNeedBindingAccessory=true;
							$("#medialist").show();
							filetype = 2;
						}
						if(result.data.mediaList.length>0){
							$("#medialist").show();
							previewFiles(result.data.mediaList,"medialist","mediaIds",filetype);
						}
	    				$("#content2").html(result.data.mobileHtml);
	    				$('textarea.inputStyle').trigger('click');
	    				$("#content2").find(".form_item_notes").each(function(i) {
	    					 //  和$("#orderedlist li")一样效果  
	    					 $(this).html(checkURL($(this).html(),false));  
	    				});
	    				formInit();
						//表单标题模板代码
						var instanceTitleTemplate = result.data.instanceTitleTemplate;
						var filedArry = [];
						if(instanceTitleTemplate){
							var arry = instanceTitleTemplate.match(/{[^}]+}/g);
							if(arry&&arry.length>0){
								$.each(arry,function(i,str){
									templateObj[str] = '';
									filedArry.push(str.substring(1,str.length-1));
								});
							}else{
								instanceTitleTemplate = instanceTitleTemplate || result.data.detailsPO.formName;
								$('[name="instanceTitle"]').val(instanceTitleTemplate.replace(new RegExp('&nbsp;','g'),''));
							}
						}else{
							$('[name="instanceTitle"]').val(result.data.detailsPO.formName);
						}
						//表单标题模板事件监听
						if(filedArry.length>0){
							$.each(filedArry,function(i,str){
								$('[name="searchValue.'+str+'"]').on('input',function(){
									templateObj['{'+str+'}'] = this.value;
									var ctitle = instanceTitleTemplate;
									for(k in templateObj){
										ctitle = ctitle.replace(new RegExp(k,"g"),templateObj[k]);
									}
									var ctitle = ctitle.replace(new RegExp('&nbsp;','g'),'').substring(0,100);
									$('[name="instanceTitle"]').val(ctitle).trigger('input');
								})
								setTimeout(function(){
									$('[name="searchValue.'+str+'"]').trigger('input');
								},600);
							});
						}

						if(!isNeedBindingAccessory){//如果没有要上传的附件，然后检查字段里面是否有需要上传的附件字段
							$("#content2").find(".file_top").each(function(i) {//查看是否有附件字段
								isNeedBindingAccessory=true;
							});
						}
						if(isNeedBindingAccessory) {
							bindingAccessory();
						}
						$(".imageFileInput").off("change");
						$(".imageFileInput").off("click");
						seajs.use(['register'], function(register) {
							registerObject = register;
							registerObject.init();
						});
	    				getProvince();
						$('textarea.inputStyle').trigger('click');
	    				$("#btn_div").show();
	    				hideLoading();
	    				 var max_height=win_height*0.6;//超级表单的textarea高度自适应
	    				 var max_height=win_height*0.6;
	    			     $('body').append($('#inputDiv').clone().attr('id','cloneinputDiv'));
	    		    	 $('body').on('input click','.wrap textarea.inputStyle',function(){//textarea高度自适应
	    		    		 	var index=$('.wrap textarea.inputStyle').index(this);
	    		    		 	if($('body .cloneTextarea'+index+'').length==0){
	    		    		 		var cloneTextarea=$(this).clone().removeAttr('id rows').addClass('cloneTextarea cloneTextarea'+index+'').css({'font-size':$(this).css('fontSize'),'height':$(this).height()});
	    		    		 		$('body').append(cloneTextarea);
	    		    		 	};
	    		    		 	var w=$(this).width();
	    		    		 	$('.cloneTextarea'+index+'').val($(this).val()).css('width',w);
	    		    		 	if($('.cloneTextarea'+index+'')){
	    		    		 		var h=$('.cloneTextarea'+index+'')[0].scrollHeight;
	    		        			h=h>max_height?max_height:h;
	    		        			$(this).height(h);
	    		    		 	}
	    		    	 });
	    			},
	    			error:function(){
	    				hideLoading();
	    				showMsg("",internetErrorMsg,1);
	        		}
	    		});
	        });

	</script>
	<script>
	var template1 = ''
    	+'<li class="toli" style="display:@Show;height: 45px;margin-bottom: 20px"> '
    	+'    <input type="hidden" name="incharges" value="@UserId" /> '
    	+'    <a class="remove_icon" href="javascript:void()" onclick="deleteUser(this,\'to\',@ArrayValue)" ></a> '
    	+'    <p class="img"> '
    	+'        <img src="@HeadPic" onerror="javascript:replaceUserHeadImage(this);" alt="" /> '
    	+'    </p> '
    	+'    <p class="name">@UserName</p> '
    	+'</li>';
	var template2 = ''
    	+'<li class="ccli" style="display:@Show;height: 45px;margin-bottom: 20px"> '
    	+'    <input type="hidden" name="relatives" value="@UserId" /> '
    	+'    <a class="remove_icon" href="javascript:void()" onclick="deleteUser(this,\'cc\',@ArrayValue)" ></a> '
    	+'    <p class="img"> '
    	+'        <img src="@HeadPic" onerror="javascript:replaceUserHeadImage(this);" alt="" /> '
    	+'    </p> '
    	+'    <p class="name">@UserName</p> '
    	+'</li>';
    	var template3 = ''
        	+'<li class="ccli" style="display:@Show;height: 45px;"> '
        	+'    <a class="remove_icon" href="javascript:void()"></a> '
        	+'    <p class="img"> '
        	+'        <img src="@HeadPic" onerror="javascript:replaceUserHeadImage(this);" alt="" /> '
        	+'    </p> '
        	+'    <p class="name">@UserName</p> '
        	+'</li>';
       	
    var quotaJson;

    	//删除全部图片
    	function removeAllPic(){
    		var html = '<div class="f-add-user-list"><ul id="imglist" class="impression">'
						+'</ul></div><div class="f-add-user-list"><ul><li class="f-user-add" id="addimage">'
						+' <input id="imageFileInput" type="file"></li></ul></div>';
        	$("#upPicDiv").html(html);
    	}
    	
    	//提交
    	function commitForm(status){
    		if($("#instanceTitle").val()==""){
    			showMsg("","请填写标题 ",1);
    			return;
    		}
    		 $("#status").val(status);
    		var isOk = false;
    		if(status == "0"){
    			isOk = true;
    		}
    		else{
    			if(isFileMust=="1"){
    				var imageUrls=document.getElementsByName("imageUrls");
    				if(imageUrls==undefined||imageUrls.length==0){
    				 showMsg("","请上传图片 ",1);
           			return ;
    				}
    			}
            	var dqdp = new DqdpForm();
            	isOk = dqdp.submitCheck("form");
    		}
    	 
        	if (isOk) {
	    		showLoading("正在提交...");
	    		
	        	$.ajax({
	        		url:"${baseURL}/open/openFormAction!addFroms.action",
	        		type:"POST",
	        		data:$("#form").serialize(),
	        		dataType:"json",
	        		success:function(result){
	        			$("#imgFileLoadDiv").hide();
	        			hideLoading();
	        			if(result.code=="0"){
							tbFormWorkOrderId=result.data.tbFormWorkOrderPOId;
							var isSearchForm = result.data.isSearchForm;
							url=result.data.url;
	        				submitSucceed(isSearchForm);
	    					//showMsg("","提交成功",1,{ok:function(result){window.location.href="add.jsp?id=${param.id}";}});
	        			}else{
	            			showMsg("",result.desc,1);
	        			}
	        		},
	        		error:function(){
	        			hideLoading();
	        			showMsg("",internetErrorMsg,1);
	        		}
	        	});
        	}
        	else{
        		//恢复提交按钮的点击事件
        		restoreSubmit();
        	}
    	}
		//生成简易二维码
		function easyQrCode(){
			if(!url||url==""){
				url="${openURL}/open/form/search_open_form.jsp?id="+tbFormWorkOrderId;
			}
			var qrcode = new QRCode(document.getElementById("showQrcodeId"), {
			});
			qrcode.makeCode(url);
		}
    	function submitSucceed(isSearchForm){
    		$("#question_detail").hide();
			if( isSearchForm =="1"){
				$("#submitSucceedVip").show();
			}else{
				$("#submitSucceed").show();
			}
    	}
		///收藏页面
		function showDialog() {
			$(".sentMail_dialog").show();
			/*生成二维码*/
			easyQrCode();
		}

		function closeDialog() {
			$(".sentMail_dialog").hide();
		}

		function checkMail() {
			var email = $('input[name="email"]').val();
			if (email == "") {
				$('.mail_error').show();
				$('.mail_error').html('请输入邮箱地址');
				return ;
			}
			if (!(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/).test(email)) {
				$('.mail_error').show();
				$('.mail_error').html('邮箱格式不正确');
				return ;
			}
			src=$('img[alt="Scan me!"]').attr("src");
			$.ajax({
				url:"${baseURL}/open/openFormAction!sendEmail.action",
				type:"POST",
				data:{"id":tbFormWorkOrderId,
						"emailAddress": email,
						"src":src},
				dataType:"json",
				success:function(result){
					if(result.code=="0"){
						//隐藏发送邮箱提示框
						$("#sendEmail").hide();
						//提示发送成功
						showMsg("","发送成功，请查看邮箱");
					}else{
						$('.mail_error').show();
						$('.mail_error').html('发送失败,请重新发送');
					}
				},
				error:function(){
					//隐藏发送邮箱提示框
					$("#sendEmail").hide();
					//提示发送失败
					showMsg("","发送失败")
				}
			});

		}
		//跳到结果页面
		function searchFormResult(){
			document.location.href = "${baseURL}/open/form/search_open_form.jsp?id="+tbFormWorkOrderId;
		}
		function returnWrite(){
			document.location.href = "${baseURL}/open/form/add.jsp?id=${param.id}";
		}
	</script>
	
	<%@include file="/open/include/showMsg.jsp"%>
	<%@include file="/open/include/uploadImage.jsp"%>
    <script type="text/javascript" src="${baseURL}/themes/wap/js/form/form.js?ver=<%=jsVer%>"></script>
    <script type="text/javascript">
     isOpen=true;
    </script>
</body>
</html>