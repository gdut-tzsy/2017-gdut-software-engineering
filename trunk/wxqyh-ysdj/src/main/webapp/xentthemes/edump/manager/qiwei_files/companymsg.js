/**
 * 设置logo图片、版权信息、页面title等等的页面属性图标。
 * @author lishengtao
 */

//===============默认信息=============================
var title_qw_manager="微信企业号免费应用管理平台";
var title_qw_manager_coop="微信企业号应用管理平台";
var title_qw_qwweb="微信企业号应用个人网页版";
var title_qw_qwweb_coop="微信企业号应用个人网页版";

var logoUrl = "/manager/images/logo.png";
var copyright = "广东道一信息技术股份有限公司版权所有 © 2013-2015";
var welcomeImgUrl = "/manager/images/welcome_img.png";
var managerLoginurl= qwyURL;

var personwebLogourl = "/qwweb/images/left_logo.png";
var personwebWelcomeurl = "/qwweb/welcom.jsp";

var personwebLoginurl = qwwebURL;
var personwebHelpintroductionurl = "http://mp.weixin.qq.com/s?__biz=MzA3ODk1MDcwMw==&mid=200979651&idx=1&sn=cb07c72aefba5acaba19bd8e84b5eb9d";

var updateUrl="http://wbg.do1.com.cn/School/Ask/qitaleibie/2015/0410/248.html";
var spactTixingCount=0;//统计提醒的次数

//=====================================================

//后台获取合作商信息的方法
function loadCompanyMsg(pageType){
	//alert("项目路径:"+baseURL+"____baseURL:"+"${baseURL}");
	
	//判断是个人网页版还是后台
	var myURL="";
	if("head_manager"==pageType||"welcome_manager"==pageType){
		myURL=baseURL+"/cooperation/cooperationAction!getLogoAndCopyright.action";
	}else if("error_manager"==pageType){
	    //获取当前网址
	    //var curWwwPath=window.document.location.href;
	    //获取主机地址之后的目录
	    var pathName=window.document.location.pathname;
	    //获取带"/"的项目名，如：/uimcardprj
	    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
	    if(projectName != "" && projectName != baseURL){
	    	projectName = "";
	    }
		myURL=projectName+"/cooperation/cooperationAction!getLogoAndCopyright.action";
	}else if("error_microphone"==pageType||"error_qwweb"==pageType){
	    //获取当前网址
	    //var curWwwPath=window.document.location.href;
	    //获取主机地址之后的目录
	    var pathName=window.document.location.pathname;
	    //获取带"/"的项目名，如：/uimcardprj
	    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
	    if(projectName != "" && projectName != baseURL){
	    	projectName = "";
	    }
	    myURL=projectName+"/portal/cooperationOpen/cooperationOpenAction!getLogoAndCopyright.action";
	    
	}else if("userlogin_qwwebjsp"==pageType){
		  myURL=baseURL+"/portal/cooperationOpen/cooperationOpenAction!getLogoAndCopyright.action";  
	}else{
		myURL=baseURL+"/portal/cooperationOpen/cooperationOpenAction!getLogoAndCopyright.action";
	}
	
	$('title').html('');
	
	$.ajax({
		url : myURL,
		type : "get",
		dataType : "json",
		success : function(result) {
			if (result.code == "0") {
				//doSet(result.data,pageType);
				var logo=setLogoValue(result.data.logo);
				doSet(logo,pageType);
			}
		},
		error : function() {
			_alert("错误提示", "系统繁忙！");
		}
	}); 
}

function setLogoValue(logo){
	if(logo.logoUrl==""){
		logo.logoUrl=logoUrl;
	}
	if(logo.copyright==""){
		logo.copyright=copyright;
	}
	if(logo.welcomeImgUrl==""){
		logo.welcomeImgUrl=welcomeImgUrl;
	}
	if(logo.personwebLogourl==""){
		logo.personwebLogourl=personwebLogourl;
	}
	if(logo.personwebWelcomeurl==""){
		logo.personwebWelcomeurl=personwebWelcomeurl;
	}
	if(logo.personwebLoginurl==""){
		logo.personwebLoginurl=personwebLoginurl;
	}
	if(logo.personwebHelpintroductionurl==""){
		logo.personwebHelpintroductionurl=personwebHelpintroductionurl;
	}
	return logo;
}

function doSet(logo,pageType){
	//根据类型设置相应的信息
	if("head_manager"==pageType){//管理后台:manager/head.jsp
		//=============设置head.jsp
		//显示title名字
		if("cooperation"==logo.personType){
			$('title').html(logo.cooperName+"—"+title_qw_manager_coop);
		}else if("mycompany"==logo.personType){
			$('title').html("企微云平台—"+title_qw_manager);
		}else{
			//$('title').html(title_qw_manager);
			$('title').html("企微云平台—"+title_qw_manager);
		}
		//显示免费试用
		if(logo.isTaste=="1"){
			$("#isTaste").show();
		}
		//判断是文件服务器还是自己本机
		var fileUrl="";
		if(logo.personType=="cooperation"){
			fileUrl=compressURL;
		}else{
			fileUrl=baseURL;
		}
		//显示logo
		$("#headLogo").attr("src",fileUrl+logo.logoUrl);
		//判断修改logo的超链接
		if(logo.personType=="cooperation"){
			//$("#logo").attr("href",logo.personwebLoginurl);
			$("#logo").attr("href",logo.managerLoginurl);
			//把超链接去掉
			$("#yyzx_aid").attr("href","#");
			//如果是合作商登陆,记录登录链接到cookie
			setCookies("do1qw@loginType",logo.personType);
			setCookies("do1qw@loginUrl",logo.personwebLoginurl);//保留这个cookie，这里是后台和个人网页版统一时的链接
			setCookies("do1qw@managerLoginurl",logo.managerLoginurl);
			setCookies("do1qw@qwwebLoginurl",logo.personwebLoginurl);
		}
		//非合作商的时候，需要隐藏对应的信息
		if(logo.personType!="cooperation"){
			//显示显示应用中心
			$("#yyzx_spanid").show();
			//显示应用中心的气泡
			if(logo.isAllAgent=="0"){
				
				if(window.localStorage){
					if(localStorage['.yyzx_liid_pop']=='false'){
						$('.yyzx_liid_pop').hide();
					}
					else{
						$("#yyzx_liid_pop").show();
					}
				}
				
			}
			//显示帮助栏目
			$("#bz_spanid").show();
			//显示更新公告
			$("#gxgg_divid").show();
			//显示喇叭
			$("#laba_divid").show();
			//公告轮播				
			setInterval(function(){
				var Sleft=$('.notice')[0].scrollLeft;
				var Swidth=$('.notice_a1')[0].scrollWidth;
				$('.notice').scrollLeft(Sleft+1);
					if(Sleft>=Swidth){
						$('.notice').scrollLeft(0);
					}
			}, 20); 
			//设置VIP有效期
			if(logo.isVIP=='1'){
				$("#isvip").attr("class","vip1");
				$("#vipTime").html("有效期至："+logo.VIPTime);
				$("#isvip").show();
			}else if(logo.isVIP=='0'){
				$("#isvip").attr("class","novip");
				$("#vipTime").html("未开通");
				$("#isvip").show();
			}
			
			//文件空间使用状态
			if(logo.fileStatisticsVO!=null&&logo.fileStatisticsVO!=""){
				//进度条
				if(logo.fileStatisticsVO.canuseSpace>=0){
					//正常状态
					if(logo.fileStatisticsVO.sumSpace>200){
					    var sumSpace= logo.fileStatisticsVO.sumSpace/1024;
						$("#sumSpace_spanId").html("总共空间："+sumSpace.toFixed(2)+"GB"+"&nbsp;&nbsp;<a target=\"_blank\" href="+updateUrl+">升级</a>");
					}else{
						$("#sumSpace_spanId").html("总共空间："+logo.fileStatisticsVO.sumSpace+"MB"+"&nbsp;&nbsp;<a target=\"_blank\" href="+updateUrl+">升级</a>");
					}
					$("#useSpace_spanId").html("已使用空间："+logo.fileStatisticsVO.cannotuseSpace+"MB");
					$("#progress_id").attr("value",logo.fileStatisticsVO.cannotusePercent);
					$("#progress_divId").attr("style","width:"+logo.fileStatisticsVO.cannotusePercent+"%;");
					if(logo.fileStatisticsVO.cannotusePercent>=0 && logo.fileStatisticsVO.cannotusePercent<=20){
						$("#progress_id").attr("class","i20");
					}else if(logo.fileStatisticsVO.cannotusePercent>=90 && logo.fileStatisticsVO.cannotusePercent<=100){
						$("#progress_id").attr("class","i90");
					}else{
						$("#progress_id").attr("class","");
					}
				}else{
					//超过总容量
					$("#progress_id").attr("value","100");
					$("#progress_divId").attr("style","width:100%;");
					$("#progress_id").attr("class","i90");
					$("#useSpace_spanId").html("已使用空间："+logo.fileStatisticsVO.cannotuseSpace+"MB");
					if(logo.fileStatisticsVO.sumSpace>200){
					    var sumSpace= logo.fileStatisticsVO.sumSpace/1024;
						$("#sumSpace_spanId").html("总共空间："+sumSpace.toFixed(2)+"GB"+"&nbsp;&nbsp;<a target=\"_blank\" href="+updateUrl+">升级</a>");
					}else{
						$("#sumSpace_spanId").html("总共空间："+logo.fileStatisticsVO.sumSpace+"MB"+"&nbsp;&nbsp;<a target=\"_blank\" href="+updateUrl+">升级</a>");
					}
					//弹框提醒
					if(logo.spaceTixingCount<2){
						_alert("企微云平台温馨提示",logo.fileStatisticsVO.orgName+"的管理员，你好！系统监测到你的账号文件空间已经超限，请尽快联系企微云平台工作人员升级空间，以免影响系统正常使用。<a target='_blank' href='"+updateUrl+"'>点击获得升级秘籍！</a>");
					}	
				}	
			}else{
				$("#useSpace_divId").hide();
				$("#progress_id").hide();
			}
		}
		
		//=======设置fooder.jsp版权信息
		$("#fooderCopyrightId").html(logo.copyright);
	}else if("welcome_manager"==pageType){//后台管理:manager/welcome.jsp
		if(logo.personType=="cooperation"){
			fileUrl=compressURL;
			//改变欢迎div的css样式
			//$("#welcomeImgDiv_id").css('background-image','url("989.jpg")');
			$("#welcomeImgDiv_id").css('background-image','url('+fileUrl+logo.welcomeImgUrl+')');
			$("#welcomeImgDiv_id").css('height','600px');//设置高度，600px为建议的最大高度(900x600)
			$("#welcomeImgDiv_id").show();
		}else{
			//显示问题信息，显示二维码图片
			$("#erweima_id").show();
			$("#helpList_id").show();
			$("#textdiv_id").show();
			fileUrl=baseURL;		
			var wxqrcode = logo.wxqrcode;
			if(wxqrcode){
				$("#erweima_id").attr("src",wxqrcode);
			}
			else{
				$("#wxqrcode_refresh").show();
			}
			//显示corpId
			$("#corpId_lblid").html(logo.corpId);
			//显示图片div
			$("#welcomeImgDiv_id").show();
		}
	}else if("main_qwweb"==pageType){//个人网页版:qwweb/main.jsp
		//显示title名字
		if("cooperation"==logo.personType){
			$('title').html(logo.cooperName+"—"+title_qw_qwweb_coop);
		}else if("mycompany"==logo.personType){
			$('title').html("企微云平台—"+title_qw_qwweb);
		}else{
			//$('title').html(title_qw_qwweb);
			$('title').html("企微云平台—"+title_qw_qwweb);
		}
		//判断是文件服务器还是自己本机
		var fileUrl="";
		if(logo.personType=="cooperation"){
			fileUrl=compressURL;
		}else{
			fileUrl=baseURL;
		}
		//设置个人网页版logo
		$("#personwebLogourl").attr("src",fileUrl+logo.personwebLogourl);
		//设置版权信息
		$("#fooderCopyrightId").html(logo.copyright);
		
		//判断合作商，设置相对的链接
		if(logo.personType=="cooperation"){
			$("#qwwebFrame").attr("src",logo.personwebWelcomeurl);
			$("#helpurlId").attr("href",logo.personwebHelpintroductionurl);	
			//如果是合作商登陆,记录登陆链接到cookie
			setCookies("do1qw@loginType",logo.personType);
			setCookies("do1qw@loginUrl",logo.personwebLoginurl);//保留这个cookie，这里是后台和个人网页版统一时的链接
			setCookies("do1qw@managerLoginurl",logo.managerLoginurl);
			setCookies("do1qw@qwwebLoginurl",logo.personwebLoginurl);
		}else{
			//个人网页版欢迎地址
			$("#qwwebFrame").attr("src",baseURL+logo.personwebWelcomeurl);
		}
		//帮助信息超链接(绝对地址)
		$("#helpurlId").attr("href",logo.personwebHelpintroductionurl);
	}else if("error_manager"==pageType){//后台管理:manager/error_xxx.html
		//如果不是我们公司登陆，全部隐藏
		if(logo.personType=="mycompany"||logo.personType=="cooperation"){
			$("#backindex_aid").show();
			$("#backindex_aid").attr("href",logo.personwebLoginurl);
		}else{
			$("#backindex_aid").hide();
		}
	}else if("error_microphone"==pageType){//手机端:jsp/wap/tips/error_xxx.html
		//判断合作商，设置返回登录页链接
		if(logo.personType=="cooperation"||logo.personType=="mycompany"){
			$("#backlogin_aId").show();
			$("#backlogin_aId").attr("href",logo.personwebLoginurl);	
		}else{
			("#backlogin_aId").hide();
		}
	}else if("error_qwweb"==pageType){//个人网页版:qwweb/tips/error.html
		//判断合作商，设置返回登录页链接
		if(logo.personType=="mycompany"){
			$("#logoUrl_aId").show();
			$("#manager_aId").show();
			$("#qwIndex_aId").show();
		}else if(logo.personType=="cooperation"){
			$("#logoUrl_aId").hide();
			$("#manager_aId").hide();
			$("#qwIndex_aId").hide();
			$("#logoUrl_aId").attr("href",logo.personwebLoginurl);
			$("#manager_aId").attr("href",logo.personwebLoginurl);
			$("#qwIndex_aId").attr("href",logo.personwebLoginurl);
		}else{
			$("#logoUrl_aId").hide();
			$("#manager_aId").hide();
			$("#qwIndex_aId").hide();
		}
	}else if("userlogin_qwwebjsp"==pageType){//     jsp/addressbook/user_login.jsp
		if("cooperation"==logo.personType){
			indexUrl=logo.personwebLoginurl;
		}
		$("#indexUrl").html(indexUrl);
	}
}


//写入cooke
function setCookies(name,value){
/*	setCookies("do1qw@loginType","cooperation");
	setCookies("do1qw@loginUrl","http://www.baidu.com");
 	//测试cookie
	try {
		var expiresDate = new Date();
		expiresDate.setTime(expiresDate.getTime()+ (30 * 24 * 60 * 60 * 1000));
		$.cookie("do1qw@loginType", "cooperation", {
			path : '/',
			expires : expiresDate
		});
		
		$.cookie("do1qw@loginUrl", "http://www.baidu.com", {
			path : '/',
			expires : expiresDate
		});
	} catch (e) {
	
	}*/
	
	
/* 	   var Days = 30; //此 cookie 将被保存 30 天  
   var expdate   = new Date();  
   expdate.setTime(expdate.getTime() + Days*24*60*60*1000);   
   document.cookie = name + "="+ escape(value) +";expire*="+ expdate.toGMTString()+";path=/";  */
   
   //不指定expire值，让cookie关闭浏览器后失效
   document.cookie = name + "="+ escape(value)+";path=/";  
}

//删除cookie
function delCookies(name){
	setCookies(name,"");
}