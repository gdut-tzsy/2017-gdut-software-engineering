<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>    
<%@include file="/jsp/common/dqdp_common_open.jsp" %>
    <head>
        <meta charset="utf-8">
        <title>邀请您加入企业微信</title>
        <meta name="description" content="">
        <meta name="HandheldFriendly" content="True">
        <meta name="MobileOptimized" content="320">
        <meta name="viewport" id="meta" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta content="telephone=no" name="format-detection" />
        <meta content="email=no" name="format-detection" />
        <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/mobiscroll.2.13.2.min.css?ver=<%=jsVer%>" />
        <link rel="stylesheet" href="${resourceURL}/jsp/wap/css/invitation.css?ver=<%=jsVer%>"/>
	     <script src='${resourceURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
	    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/mobiscroll.2.13.2.min.js"></script>
	    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/detect-jquery.js"></script>
	    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/flipsnap.js"></script>
	    <script type="text/javascript" src="${resourceURL}/jsp/wap/js/wechat.js"></script>
	    <script src='${resourceURL}/jsp/wap/js/common.js?ver=<%=jsVer%>'></script>
	    <script src="${resourceURL}/js/wx/wx.js?ver=<%=jsVer%>"></script>
	    <script src="${resourceURL}/js/3rd-plug/jquery/jquery.form.js"></script>
		<script type="text/javascript" src="${resourceURL}/js/do1/common/checkCard.js?ver=<%=jsVer%>"></script>
    </head>
    <style>
		@media screen and (min-width: 800px) {
	        .interview_wrap1 {background: #e8e8e8;margin-top: 50px;}
	        .bgddd {background-color: #efefef;}
	        .form_content {border: 1px solid #ddd;border-bottom: none}
	        .question_detail {background: #ffffff;width: 600px;margin: 0 auto;padding: 20px 99px;border: 1px solid #dfdfdf;}
	        .inter_saoma {border: 1px solid #ddd; box-sizing: border-box;-moz-box-sizing: border-box;-webkit-box-sizing: border-box;}
	        .i_inter_top img {padding-top: 10px; }
	        .i_inter_top {height: 90px;}
	        .i_form-write {height: 90px;}
	        .i_form-saoma {height: 90px;}
	        .i_inter_top {padding: 15px 2px 0px;border: 1px solid #ddd;border-bottom: none;}
	        .form_btns{padding: 10px 0;}
    	}
    </style>
	<body>
	<div id="wrap_main" class="wrap interview_wrap1">
		<div class="i_inter_top">
            <img src="${resourceURL}/jsp/wap/images/tab.png" alt="" class="i_liucheng1" />
            <span class="i_form-write"></span>
            <span class="i_form-saoma"></span>
        </div>
		<form  id="form" onsubmit="return false;">
			<input type="hidden" name="id" id="id" value="${param.id}" />
			<input type="hidden" name="type" id="type" value=""/>
               <div class="question_detail">
                   <div class="" id="question_detail">
                   <div class="form_btns mt10" id="btn_gray" style="display:none">
						<div class="inner_form_btns pb10">
							<div class="fbtns flexbox">
								<a class="fbtn gray_btn flexItem" href="#">该邀请页面已过期</a>
							</div>
						</div>
					</div>
                   	<h1 id="detail_title" class="title" style="">上海交大电院校友会（卓源e家人）</h1>
                   	<div class="titletxet" id="content">企业微信成员邀请</div>
					<div class="form_content">
						<div id="content2"  style="border-bottom: 1px solid #ddd;">
							<div class="letter_bar">姓名<span style="color: red">&nbsp;*</span>  </div>
							<div class="inner-f-item"> 
								<div class="text_div">
									<input type="text" id="personName" placeholder="请输入" maxlength="30" style="width:100%" value="" valid="{must:true,maxLength:30,fieldType:'pattern',regex:'^.*$',tip:'姓名'}" name="tbQyMemberInfoPO.personName" class="item-select inputStyle textInput search-input">
								</div>
							</div>
							<div id="weixin">
								<div class="letter_bar">微信号（在“微信 > 我”页面第一栏可找到） </div>
								<div class="inner-f-item"> 
									<div class="text_div">
										<input type="text" placeholder="填写微信号可直接关注，跳过手机验证" maxlength="30" style="width:100%" id="weixinNum" onblur="setAccount();" value="" valid="{must:false,maxLength:30,fieldType:'pattern',regex:'^[a-zA-Z_][a-zA-Z\d_-]{4,}$',tip:'微信号'}" name="tbQyMemberInfoPO.weixinNum" class="item-select inputStyle textInput search-input">
									</div>
								</div>
							</div>
							<div class="letter_bar">手机号</div>
							<div class="inner-f-item"> 
								<div class="text_div">
									<input type="tel" maxlength="30" onblur="setAccount();" id="mobile" style="width:100%" placeholder="填写正确手机号码接收关注验证短信" value="" valid="{must:true,maxLength:30,fieldType:'pattern',regex:'^\\d{5,15}$',tip:'手机'}" name="tbQyMemberInfoPO.mobile" class="item-select inputStyle textInput search-input">
								</div>
							</div>
							<div id="account" style="display: none">
								<div class="letter_bar">账号 （成员唯一标识，不支持中文<span id="msgSpan">，默认为手机号或微信号</span>）<span style="color: red">&nbsp;*</span></div>
								<div class="inner-f-item"> 
									<div class="text_div">
										<input type="text" maxlength="100" placeholder="请输入" id="wxUserId" style="width:100%" value="" valid="{must:false,maxLength:100,fieldType:'pattern',regex:'^.*$',tip:'账号'}" name="tbQyMemberInfoPO.wxUserId" class="item-select inputStyle textInput search-input">
									</div>
								</div>
							</div>
							<div id="emailId" style="display: none">
								<div class="letter_bar">邮箱 </div>
								<div class="inner-f-item"> 
									<div class="text_div">
										<input type="text" maxlength="50" placeholder="请输入" style="width:100%" value="" id="email" valid="{must:true,maxLength:50,fieldType:'pattern',regex:'(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)',tip:'邮箱'}" name="tbQyMemberInfoPO.email" class="item-select inputStyle textInput search-input">
									</div>
								</div>
							</div>
							<div id="deptNo">
								<div class="letter_bar">毕业专业<span style="color: red">&nbsp;*</span></div>
								<div class="f-item">
									<div class="inner-f-item item-text flexbox">
										<div class="flexItem">
											<input type="hidden" name="tbQyMemberInfoPO.selectDept" id="deptName" value=""/>
											<select class="flexItem item-select" oldvalue="" onchange="selectChange(this);" name="tbQyMemberInfoPO.selectDeptId" id="deptId">
											</select>
										</div>
									</div>
								</div>
							</div>
							<div id="sexID" style="display: none">
								<div class="letter_bar">性别  </div>
								<div>
									<div class="answer-list radio_btn table">
										<ul class="table-row row3">
											<li class="" quota="" onclick="radioSelect(this);">
												<input type="radio" style="display:none" name="tbQyMemberInfoPO.sex" checked="checked" value="1">
												<div class="xian_option"><i><span></span></i>男</div>
											</li>
											<li class="" quota="" onclick="radioSelect(this);">
												<input type="radio" style="display:none" name="tbQyMemberInfoPO.sex" value="2">
												<div class="xian_option"><i><span></span></i>女</div>
											</li>
										</ul>
									</div>
								</div>
							</div>
							<div id="nikeName" style="display: none">
								<div class="letter_bar">昵称</div>
								<div class="inner-f-item"> 
									<div class="text_div">
										<input type="text" maxlength="100" style="width:100%" placeholder="请输入" value="" valid="{must:false,maxLength:100,fieldType:'pattern',regex:'^.*$',tip:'昵称'}" name="tbQyMemberInfoPO.nickName" class="item-select inputStyle textInput search-input">
									</div>
								</div>
							</div>
							<div id="idCard"  style="display: none">
								<div class="letter_bar">身份证</div>
								<div class="inner-f-item"> 
									<div class="text_div">
										<input type="text" maxlength="30" id="identity" style="width:100%" placeholder="请输入" value="" valid="{must:false,maxLength:30,fieldType:'pattern',regex:'^.*$',tip:'身份证'}" name="tbQyMemberInfoPO.identity" class="item-select inputStyle textInput search-input">
									</div>
								</div>
							</div>	
							<div id="position">
								<div class="letter_bar">毕业年份<span style="color: red">&nbsp;*</span></div>
								<div class="inner-f-item"> 
									<div class="text_div">
										<input type="text" maxlength="50" style="width:100%" placeholder="请输入" value="" valid="{must:false,maxLength:50,fieldType:'pattern',regex:'^.*$',tip:'毕业年份'}" name="tbQyMemberInfoPO.position" id="positionId" class="item-select inputStyle textInput">
									</div>
								</div>
							</div>	
							<div id="qqNum" style="display: none">
								<div class="letter_bar">QQ</div>
								<div class="inner-f-item"> 
									<div class="text_div">
										<input type="number" maxlength="20" style="width:100%" placeholder="请输入" value="" valid="{must:false,maxLength:20,fieldType:'pattern',regex:'^.*$',tip:'QQ'}" name="tbQyMemberInfoPO.qqNum" class="item-select inputStyle textInput search-input">
									</div>
								</div>
							</div>
							<div id="phone" style="display: none">
								<div class="letter_bar">电话</div>
								<div class="inner-f-item"> 
									<div class="text_div">
										<input type="text" maxlength="30" style="width:100%" placeholder="请输入" value="" valid="{must:false,maxLength:30,fieldType:'pattern',regex:'^.*$',tip:'电话'}" name="tbQyMemberInfoPO.shorMobile" class="item-select inputStyle textInput search-input">
									</div>
								</div>
							</div>	
							<div id="phone1" style="display: none">
								<div class="letter_bar">电话2</div>
								<div class="inner-f-item"> 
									<div class="text_div">
										<input type="text" maxlength="30" style="width:100%" placeholder="请输入" value="" valid="{must:false,maxLength:30,fieldType:'pattern',regex:'^.*$',tip:'电话2'}" name="tbQyMemberInfoPO.phone" class="item-select inputStyle textInput search-input">
									</div>
								</div>
							</div>
							<div id="address" style="display: none">
								<div class="letter_bar">所在城市及通信地址</div>
								<div class="inner-f-item"> 
									<div class="text_div">
										<input type="text" maxlength="100" style="width:100%" placeholder="请输入" value="" valid="{must:false,maxLength:100,fieldType:'pattern',regex:'^.*$',tip:'所在城市及通信地址'}" name="tbQyMemberInfoPO.address" class="item-select inputStyle textInput search-input">
									</div>
								</div>
							</div>
							<div id="birthday" style="display: none">
								<div class="letter_bar">阳历生日 </div>
								<div class="inner-f-item"> 
									<div class="text_div">
										<input type="text" maxlength="100" style="width:100%" placeholder="请选择" value="" name="tbQyMemberInfoPO.birthday" class="item-select inputStyle dateInput">
									</div>
								</div>
							</div>
							<div id="birthday2" style="display: none">
								<div class="letter_bar">农历生日</div>
								<div class="inner-f-item"> 
									<div class="text_div">
										<input type="text" maxlength="100" style="width:100%" placeholder="请选择" value="" valid="{must:false,maxLength:100,fieldType:'pattern',regex:'^.*$',tip:'农历生日'}" name="tbQyMemberInfoPO.lunarCalendar" class="item-select inputStyle timeInput">
									</div>
								</div>
							</div>
							<div id="remarkId" style="display: none">
								<div class="letter_bar">工作单位</div>
								<div class="ask_textarea_content">
									<div class="ask_textarea_content_inner">
										<textarea placeholder="请输入工作单位" style="width: 100%; height: 120px; padding: 0px; max-height: 379.8px; min-height: 32px;" valid="{'endTime':'','fieldType':'','lineRow':1,'maxLength':2000,'minLength':0,'must':false,'regex':'','stratTime':'','tip':'多行文字','type':'TextArea'}" name="tbQyMemberInfoPO.remark" class="item-select inputStyle textInput" index="0"></textarea>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="form_btns mt10" id="btn_div" style="display:block">
						<div class="inner_form_btns pb10">
							<div class="fbtns flexbox">
								<a class="fbtn btn flexItem" href="javascript:commitForm('')" id="formBtn">下一步：扫码关注</a>
							</div>
						</div>
					</div>
					
					<div class="form_btns mt10" id="btn_red" style="display:none">
						<div class="inner_form_btns pb10">
							<div class="fbtns flexbox">
								<a class="fbtn gray_btn flexItem" href="#">该邀请页面已过期</a>
							</div>
						</div>
					</div>
					<div id="remind" class="mt20 mb20">
						<div class="suxing"> 
							<i class="fa fa-clock-o"></i>有效期至：<span class="detailStart" id="stopTime" style="width:45%;"></span>
						</div>
					</div>
				</div>
			</div>
		</form>
		<div class="inter_saoma">
               <p class="inter_saoma_tit">上海交大电院校友会（卓源e家人）</p>
              
               <div class="inter_saoma_img">
                   <img id="inter_img" src="${resourceURL}/jsp/wap/images/inter_sao_img.png" height="170" width="170">
               </div>
               
                <div class="inter_nosum mb20">
                   <p class="inter_sum">如果你已提交过资料，可直接扫码关注 </p>
                   <p class="inter_sum2">（微信打开的可直接长按二维码识别）</p>
               </div>
               <div class="inter_success1 mb20" style="display:none;">
                    <p class="inter_sum_succ"><img src="http://res.do1.com.cn/qwy/jsp/wap/images/inter_sum.png" height="20" width="20">资料已提交，请扫码关注</p>
					<p class="inter_sum2">（微信打开的可直接长按二维码识别）</p>
               </div>
               <div class="inter_success2 mb20" style="display:none;">
                    <p class="inter_sum_succ"><img src="http://res.do1.com.cn/qwy/jsp/wap/images/inter_sum.png" height="20" width="20">资料已提交，请扫码关注</p>
					<p class="inter_sum2">（微信打开的可直接长按二维码识别）</p>
					<p class="inter_sum3">关注后不需再操作，请等待管理员审批通过。</p>
               </div>
       	</div>
    </div>
    <%@include file="/open/include/footer.jsp"%>
	<script type="text/javascript">
	var wxqrcode = '';
	var isDefault="";
	//含有图片的微信弹框
	function _alert_weui(url,text,buttonText,callback){
	    
	    if(typeof(buttonText)=='string'&&buttonText.split("|").length==1){
	        buttonText1=buttonText.split("|")[0];
	        var buttonHtml='<div class="weui_dialog_ft">'
	              +'<a href="javascript:;" class="weui_btn_dialog primary">'+buttonText1+'</a>'
	              +'</div>';
	    }else {
	        if(typeof(buttonText)=='string'&&buttonText.split("|").length>=2){
	            var buttonText1=buttonText.split("|")[0];
	            var buttonText2=buttonText.split("|")[1];
	        }else if(typeof(buttonText)=='function'||typeof(buttonText)=='object'&&buttonText['ok']||typeof(buttonText)=='object'&&buttonText['fail']){
	            callback=buttonText;
	            var buttonText1="取消";
	            var buttonText2="确认";
	        }else{
	            var buttonText1="取消";
	            var buttonText2="确认";
	        }
	        var buttonHtml='<div class="weui_dialog_ft">'
	            +'<a href="javascript:;" class="weui_btn_dialog default">'+buttonText1+'</a>'
	            +'<a href="javascript:;" class="weui_btn_dialog primary">'+buttonText2+'</a>'
	            +'</div>';
	    }
	    var boxClass="weui_dialog_alert custom_dialog";
	    var html='<div class="'+boxClass+'">'
	            +'<div class="weui_mask"></div>'
	            +'<div class="weui_dialog">'
	                +'<div class="weui_dialog_img"><img style="height:170px;width:170px;margin-top:30px 0 15px;" src="'+url+'"></div>'
	                +'<div class="weui_dialog_bd">'+text+'</div>'
	                +buttonHtml
	           +' </div>'
	       +' </div>';
	    $('body').append(html);
	    $('.weui_btn_dialog.primary').off('click').on('click',function(){//确认
	    	$('.weui_btn_dialog.primary').off('click');
	        if(callback&&typeof(callback)=='function'){
	           callback(); 
	        }else if(callback&&typeof(callback)=='object'&&callback['ok']){
	            callback['ok']();
	        }
	        $(this).parents('.custom_dialog').remove();
	    });
	    $('.weui_btn_dialog.default').off('click').on('click',function(){//取消
	        if(callback&&typeof(callback)=='object'&&callback['fail']){
	            callback['fail']();
	        }
	        $(this).parents('.custom_dialog').remove();
	    });
	    
	};
		//已提交的页面不能返回到第一页
		var isClick=false;
		$(document).ready(function() {
			 // 输入框清空文字
		    $('.search-input').on('input click',function(event) {
		            var input_colse=$('<span class="input_colse"></span>');
		            if($(this).val().length>0){
		                $('.input_colse').remove();
		                $(this).after(input_colse)
		            }else{
		                $('.input_colse').remove(); }
		            $('.input_colse').on('click',function(){
		                $(this).prev('.search-input').val('');
		                $(this).remove();
		        	})
		    }); 
			
	        $('.i_form-write').click(function() {
	        	if(!isClick){
	        		$('#form').show();
		            $('.inter_saoma').hide();
		            $('.i_liucheng1').attr('src', '${resourceURL}/jsp/wap/images/tab.png');
	        	}
	        })
	        $('.i_form-saoma').click(function() {
	            $('.inter_saoma').show();
	            $('#form').hide();
	            $('.i_liucheng1').attr('src', '${resourceURL}/jsp/wap/images/tab2.png');
	        })
	    })
		
	        $(function(){
	        	var opt1 = {
	        	        preset: 'date', //日期
	        	        theme: 'android-holo light', //皮肤样式
	        	        display: 'bottom', //显示方式 
	        	        mode: 'scroller', //日期选择模式
	        	        dateFormat: 'mm-dd', // 上面部分的日期格式
	        	        setText: '取消', //确认按钮名称
	        	        cancelText: '确认',//取消按钮名籍我
	        	        dateOrder: 'mmd D', //面板中日期排列格式
	        	        showNow: true,  
	        	        nowText: "今天", 
	        	         stepMinute: 5,//设置分钟间隔
	        	        dayText: '日', monthText: '月份', yearText: '年份', //面板中年月日文字
	        	        endYear:2050 //结束年份

	        	    };
	        	opt.preset='date';//调用日历显示  日期
	        	$('.dateInput').mobiscroll(opt);//日历
	    		showLoading("正在加载中...");
	        	$('.timeInput').mobiscroll(opt1);//日历
	    		var id=$("#id").val();
	    		$.ajax({
	    			url:"${baseURL}/open/memberAction!getMemberSet.action",
	    			type:"POST",
	    			data:{id:id},
	    			dataType:"json",
	    			success:function(result){
	    				if(result.code=="1999" || result.code!="0"){
	        				hideLoading();
	        				_alert("提示",result.desc,"确认",function(){WeixinJS.back();});
	    					return;
	    				}
	    				hideLoading();
	    				var config=result.data.config;
	    				if(config){
	    					isDefault=config.isDefault;
	    					var isValid=result.data.isValid;
	    					if(isValid=="1"){
	    						$("#btn_div").show();
	    						$("#btn_red").hide();
	    						$("#btn_gray").hide();
	    					}else{
	    						$("#btn_div").hide();
	    						$("#btn_red").show();
	    						$("#btn_gray").show();
	    					}
	    					var sTime = result.data.startTime;
	    					var eTime = result.data.stopTime;
	    					$("#startTime").html(sTime);
	    					$("#stopTime").html(eTime);
	    					
	    					$("#type").val(config.requestWay);
	    					
	    					$("#deptNo").show();
    						var html="";
    						var list=result.data.list;
    						html += "<option value=''>请选择毕业专业</option>";
    						if(list && list.length>0){
    	        				for(var i=0;i<list.length;i++){
    	        					var template = list[i];
    	        					html += "<option value='"+template.deptId+"'>"+template.dept+"</option>";
    	        				}
    	    				}
    	    				$("#deptId").append(html);
    	    				
	    					var custom=config.custom;
	    					if(custom){
	    						if(custom.indexOf("a2")>-1){//邮箱
		    						$("#emailId").show();
			    				}
	    						if(custom.indexOf("a3")>-1){//性别
		    						$("#sexID").show();
			    				}
	    						if(custom.indexOf("a6")>-1){
		    						if(isDefault=="0"){
		    							$("#msgSpan").remove();
		    						}
		    						$("#account").show();
			    				}else{
			    					isDefault="1";
			    				}
			    				if(custom.indexOf("a7")>-1){
			    					$("#nikeName").show();
			    				}
			    				if(custom.indexOf("a8")>-1){
			    					$("#idCard").show();
			    				}
			    				if(custom.indexOf("b1")>-1){
			    					$("#qqNum").show();
			    				}
			    				if(custom.indexOf("b2")>-1){
			    					$("#phone").show();
			    				}
			    				if(custom.indexOf("b3")>-1){
			    					$("#phone1").show();
			    				}
			    				if(custom.indexOf("b4")>-1){
			    					$("#address").show();
			    				}
			    				if(custom.indexOf("b5")>-1){
			    					$("#birthday").show();
			    				}
			    				if(custom.indexOf("b6")>-1){
			    					$("#birthday2").show();
			    				}
			    				if(custom.indexOf("b7")>-1){
			    					$("#remarkId").show();
			    				}
	    					}
	    					var content=config.instruction;
		    				
		    				var title="邀请您加入"+config.orgName;
		    				document.title=title;
		    				var shareUrl=window.location.href;
		    				var logo=result.data.logo;
		    				if(null==logo || ""==logo){
		    					logo="${resourceURL}/themes/qw/images/logo/logo400.png";
		    				}
		    				
		    				content=htmlTagcodeToInput(content);
		    				$("#content").html(content);
		    				
		    				setDataForWeixinValue(title,logo,htmlTagReplace(content),shareUrl);
	        				wxqrcode = baseURL+"/portal/errcode/errcodeAction!loadImage.action?imgUrl="+ encodeURIComponent(result.data.wxqrcode);
							$("#inter_img").attr("src",wxqrcode);
							/* $(".inter_saoma_tit").html(config.orgName); */
	    				}
	    				
	    			},
	    			error:function(){
	    				hideLoading();
	    				_alert("提示",internetErrorMsg,"确认",function(){restoreSubmit();});
	        		}
	    		});
	        });
	        
	</script>
	<script>
	
       	
    	
    	//提交
    	var submitFlag=false;
    	function commitForm(){
    		if(!submitFlag){
    			submitFlag=true;
    			if($("#personName").val()==""){
    				_alert("提示","请填写姓名","确认",function(){restoreSubmit();});
        			submitFlag=false;
        			return;
        		}
    			if($("#weixinNum").val()!="" && !(/^[a-zA-Z_][a-zA-Z\d_-]{4,}$/).test($("#weixinNum").val())){
        			_alert("提示","微信号只允许字母、数字、下划线和减号，并以字母开头","确认",function(){restoreSubmit();});
        			submitFlag=false;
    	    		return;
    	    	}
        		if($("#mobile").val()!="" && !(/^\d{5,15}$/).test($("#mobile").val())){
        			_alert("提示","手机格式有误，请重新输入","确认",function(){restoreSubmit();});
        			submitFlag=false;
            		return;
            	}
        		if($("#weixinNum").val()!="" && !(/^[a-zA-Z_][a-zA-Z\d_-]{4,}$/).test($("#weixinNum").val())){
        			_alert("提示","微信号只允许字母、数字、下划线和减号，并以字母开头","确认",function(){restoreSubmit();});
        			submitFlag=false;
    	    		return;
    	    	}
        		if($("#email").val()!="" && !(/^\w+([-+.]\w+)*@\w+([-.]\w+)*.\w+([-.]\w+)*$/).test($("#email").val())){
        			_alert("提示","邮箱格式有误，请重新输入","确认",function(){restoreSubmit();});
        			submitFlag=false;
            		return;
            	}
        		if($("#wxUserId").val()==""){
        			_alert("提示","请填写账号","确认",function(){restoreSubmit();});
        			submitFlag=false;
        			return;
        		}
        		if($("#wxUserId").val()!="" && !(/^\w+[\w-_.@]*$/).test($("#wxUserId").val())){
        			_alert("提示","账号格式不正确","确认",function(){restoreSubmit();});
        			submitFlag=false;
        			return;
        		}
        		if($("#wxUserId").val()!="" && $("#wxUserId").val().length>64){
        			_alert("提示","账号长度不能超过64个字符！","确认",function(){restoreSubmit();});
        			submitFlag=false;
        			return;
        		}
        		if($("#deptId").val()==""){
        			_alert("提示","请选择毕业专业","确认",function(){restoreSubmit();});
        			submitFlag=false;
        			return;
        		}
        		if($("#positionId").val()==""){
        			_alert("提示","请输入毕业年份","确认",function(){restoreSubmit();});
        			submitFlag=false;
        			return;
        		}
        		if($("#positionId").val()!="" && !(/^\d{4}$/).test($("#positionId").val())){
        			_alert("提示","毕业年份只允许4位数字 ","确认",function(){restoreSubmit();});
        			submitFlag=false;
        			return;
        		}
				var identity=$("#identity").val();
				if(identity!="" && identity!=null){
					if(checkCard(identity)!='0'){
						_alert("提示","身份证格式不正确,请重新输入","确认",function(){restoreSubmit();});
						submitFlag=false;
						return;
					}
				}
            	  $.ajax({
            		url:"${baseURL}/open/memberAction!ajaxAdd.action",
            		type:"POST",
            		data:$("#form").serialize(),
            		dataType:"json",
            		success:function(result){
            			submitFlag=false;
            			if(result.code=="0"){
            				isClick=true;
            				var type=$("#type").val();
            				submitSucceed(type);
        					//showMsg("","提交成功",1,{ok:function(result){window.location.href="add.jsp?id=${param.id}";}});
            			}else{
            				$('#form').show();
        		            $('.inter_saoma').hide();
        		            $('.i_liucheng1').attr('src', '${resourceURL}/jsp/wap/images/tab.png');
        		            if(result.code=="2011" || result.code=="1999"){
       		            	 _alert_weui(wxqrcode,result.desc,"好的",function(){restoreSubmit();});
	       		            }else{
	       		            	_alert("提示",result.desc,"确认",function(){restoreSubmit();});
	       		            }
            			}
            		},
            		error:function(){
            			hideLoading();
            			submitFlag=false;
            			_alert("提示",internetErrorMsg,"确认",function(){restoreSubmit();});
            		}
            	});
    		}
    	}
    	function submitSucceed(type){
    		 $('.inter_saoma').show();
    		 $(".inter_nosum").hide();
             $('#form').hide();
             $('.i_liucheng1').attr('src', '${resourceURL}/jsp/wap/images/tab2.png');
    		if("1"==type){//不需要审批
    			$(".inter_success1").show();
    		}else if("2"==type){//需要审批
    			$(".inter_success2").show();
    		}
    	}
    	
    	$("#question_detail").find("input[type='radio'][checked]").each(function () {
        	radioSelect($(this).parent());
        });
    	
    	function radioSelect(obj){
    	    var self = $(obj),
    	    context = self.parent().parent(),
    	    siblings = $('.active',context);
    	    var inputself = self.find("input[type='radio']");
    	    var num = self.attr("quota");
    	    //判断选项是否已经满了
    	    siblings.removeClass('active');
    	    self.addClass('active');

    	    context.find("input[type='radio']").attr("checked", false);
    	    self.find("input[type='radio']").attr("checked", true);
    	}
    	function setAccount(){
    		if(isDefault=="1"){
    			var mobile=$("#mobile").val();
        		if(mobile!="" && mobile!=null){
    	    		$("#wxUserId").val(mobile);
        		}else{
        			$("#wxUserId").val($("#weixinNum").val());
        		}
    		}
    	}
    	function selectChange(obj){
    		var deptName=obj.options[obj.selectedIndex].innerHTML;
    		$("#deptName").val(deptName);
    	}
	</script>
	
	<%@include file="/open/include/showMsg.jsp"%>
</body>
</html>