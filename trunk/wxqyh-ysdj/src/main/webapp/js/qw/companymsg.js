/**
 * 设置logo图片、版权信息、页面title等等的页面属性图标。
 * @author lishengtao
 */

//===============默认信息=============================
var title_model = "企业微信官方推荐移动办公品牌|微信办公|销售管理|培训学习";
var title_qw_manager_coop = title_model; //企业微信应用管理平台
var title_qw_qwweb = title_model; //企业微信应用个人网页版
var title_qw_qwweb_coop = title_model; //企业微信应用个人网页版

var logoUrl = "/manager/images/logo.png";
var copyright = "企微云平台 广东道一信息技术股份有限公司版权所有 2004-"+new Date().getFullYear();
var welcomeImgurl = "/manager/images/welcome_img.png";
var managerLoginurl=qwyURL;

var personwebLogourl = "/qwweb/images/left_logo.png";
var personwebWelcomeurl = "/qwweb/welcom.jsp";

var personwebLoginurl = qwwebURL;
var personwebHelpintroductionurl = "http://mp.weixin.qq.com/s?__biz=MzA3ODk1MDcwMw==&mid=200979651&idx=1&sn=cb07c72aefba5acaba19bd8e84b5eb9d";

//=========
var spaceBuyURL=baseURL+"/qiweipublicity/companysrv/space/space_index.jsp";
var vipURL=baseURL+"/qiweipublicity/companysrv/vip/vip_index.jsp";//购买VIP链接
var spactTixingCount=0;//统计提醒的次数

var isCoop=false;//是不是合作商
var coopType=0;//0-企微用户；1-渠道商；2-金卡
var isShowQw = true;
var baseLogoInfo = {
	title:qwManagerTitle,
	logoURL: baseURL + "/manager/images/logo.png",
	copyright : "企微云平台 广东道一信息技术股份有限公司版权所有 2004-"+new Date().getFullYear(),
	welcomeImgUrl: baseURL + "/manager/images/welcome_img.png",
	managerLoginURL : qwyURL,

	title_qw_qwweb : "企微云平台—企业微信 | 微信企业号官方推荐企业云办公第一品牌 | 微信办公",
	personwebHelpintroductionURL : "http://mp.weixin.qq.com/s?__biz=MzA3ODk1MDcwMw==&mid=200979651&idx=1&sn=cb07c72aefba5acaba19bd8e84b5eb9d",
	personwebLogoURL : baseURL + "/qwweb/images/left_logo.png",
	personwebWelcomeURL : baseURL + "/qwweb/welcom.jsp",
}
//=====================================================


//后台获取合作商信息的方法
function loadCompanyMsg(pageType){
	//判断是个人网页版还是后台
	var myURL="";
	if("head_manager"==pageType||"welcome_manager"==pageType ||"flow_group_list_manager"==pageType){
		myURL=baseURL+"/cooperation/cooperationAction!getLogoAndCopyright.action";
	}else if("error_manager"==pageType){
	    //获取当前网址
	    //获取主机地址之后的目录
	    var pathName=window.document.location.pathname;
	    //获取带"/"的项目名，如：/uimcardprj
	    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
	    if(projectName != "" && projectName != baseURL){
	    	projectName = "";
	    }
		myURL=projectName+"/cooperation/cooperationAction!getLogoAndCopyright.action";
	}else if("error_microphone"==pageType||"error_qwweb"==pageType||"reimbursement"==pageType){
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
	
	$('title').html(''); //先设置标题为空
	
	$.ajax({
		url : myURL,
		type : "POST",
		dataType : "json",
		success : function(result) {
			if (result.code == "0") {
				var logo=setLogoValue(result.data.logo);
				//var logo=result.data.logo;
				doSet(logo,pageType);
			}
		},
		error : function() {
			//_alert("错误提示", "系统繁忙！");
		}
	}); 
}

function setLogoValue(logo){
	var cooperationSettingVO = logo.cooperationSettingVO;
	if(cooperationSettingVO && cooperationSettingVO.status=="1"){
		isCoop=true;
		if(cooperationSettingVO.type=="1"){
			coopType=1;
		}else if(cooperationSettingVO.type=="2"){
			coopType=2;
		}
		if("1"!=cooperationSettingVO.isShowQw){
			isShowQw = false;
		}
	}

	//使用自定义显示，如果没有设置，用默认的
	if(!isCoop || cooperationSettingVO.logoUrl==""){
		logo.logoUrl=logoUrl;
	}else{
		logo.logoUrl=cooperationSettingVO.logoUrl
	}

	if(!isCoop || cooperationSettingVO.copyright==""){
		logo.copyright=copyright;
	}else{
		logo.copyright=cooperationSettingVO.copyright;
	}

	if(!isCoop || cooperationSettingVO.welcomeImgurl==""){
		logo.welcomeImgurl=welcomeImgurl;
	}else{
		logo.welcomeImgurl=cooperationSettingVO.welcomeImgurl;
	}

	if(!isCoop || cooperationSettingVO.personwebLogourl==""){
		logo.personwebLogourl=personwebLogourl;
	}else{
		logo.personwebLogourl=cooperationSettingVO.personwebLogourl;
	}

	if(!isCoop || cooperationSettingVO.personwebWelcomeurl=="" || cooperationSettingVO.type=="2"){
		logo.personwebWelcomeurl=personwebWelcomeurl;
	}else{
		logo.personwebWelcomeurl=cooperationSettingVO.personwebWelcomeurl;
	}

	if(!isCoop || cooperationSettingVO.personwebLoginurl==""){
		logo.personwebLoginurl=personwebLoginurl;
	}else{
		logo.personwebLoginurl=cooperationSettingVO.personwebLoginurl;
	}

	if(!isCoop || cooperationSettingVO.managerLoginurl==""){
		logo.managerLoginurl=managerLoginurl;
	}else{
		logo.managerLoginurl=cooperationSettingVO.managerLoginurl;
	}

	if(!isCoop || cooperationSettingVO.personwebHelpintroductionurl==""){
		logo.personwebHelpintroductionurl=personwebHelpintroductionurl;
	}else{
		logo.personwebHelpintroductionurl=cooperationSettingVO.personwebHelpintroductionurl;
	}
	return logo;
}

function doSet(logo,pageType){
	//根据类型设置相应的信息
	if("head_manager"==pageType){//管理后台:manager/head.jsp
		wxqyh_corpId=logo.corpId;
		//=============设置head.jsp==============
		//显示title名字
		if(isCoop){
			$('title').html(logo.cooperationSettingVO.cooperName+"—"+title_qw_manager_coop);
		}else{
			$('title').html(qwManagerTitle);
		}
		//显示超管
		if(logo.managerLevelCode=="superManager"){
			$("#managerLevelDesc").html("[超管]");
			$("#managerLevelDesc").show();
		}
		//显示免费试用
		if(logo.isTaste=="1"){
			$("#isTaste").show();
		}
		//显示logo
		if(logo.logoUrl.indexOf(logoUrl)<0){
			$("#headLogo").attr("src",compressURL+logo.logoUrl);
		}else{
			$("#headLogo").attr("src",baseURL+logo.logoUrl);
		}

		//判断修改logo的超链接
		if(isCoop){
			$("#logo").attr("href",logo.managerLoginurl);
			//如果是合作商登陆,记录登录链接到cookie
			setCookies("do1qw@loginType",logo.personType);
			setCookies("do1qw@coopType",logo.cooperationSettingVO.type);
			setCookies("do1qw@loginUrl",logo.personwebLoginurl);//保留这个cookie，这里是后台和个人网页版统一时的链接
			setCookies("do1qw@managerLoginurl",logo.managerLoginurl);
			setCookies("do1qw@qwwebLoginurl",logo.personwebLoginurl);
		}

		//控制显示人资和培训
		if(isCoop && logo.cooperationSettingVO.type=="1"){
			//渠道商需要判断是否托管了人资和考试
			//考试
			if(logo.examinationTrust ||  logo.learnonlineTrust){
				$("#examination").find("a").eq(0).html("云课堂");
				$("#examination").show();
			}else{
				$("#examination").hide();
			}
			//人资
			if(logo.hrmanagementTrust){
				$("#hr").show();
			}else{
				$("#hr").hide();
			}
		}else{
			$("#hr").show();
			$("#examination").show();
		}

		//设置VIP链接
		wxqyhConfig.ready(function(){
			if(isQuDao()){
				$("#vipTop").html("VIP");
				$("#isvip,#vipTop").attr("href","javascript:doNothing();");
			}else{
				if(!isVipSilver() || (isVipSilver() && wxqyhConfig.myInterfaceList && wxqyhConfig.myInterfaceList.length<=0)){
					//如果是非VIP，或者银卡而又没有金卡特权，跳转到VIP购买
					$("#isvip,#vipTop").attr("href", vipUrl);
				}else{
					$("#isvip,#vipTop").attr("href", goldVipUrl);
				}
			}
            $('#CorpId span').text(wxqyhConfig.orgConfigInfo.corpId);
        });

		//设置VIP有效期
		if(logo.isVIP=='1'){
			if(1==logo.vipGrade){
				//银卡VIP
				wxqyhConfig.ready(function(){
					//如果拥有金卡权限，显示成金色 && wxqyhConfig.myInterfaceList.length>0
					if(wxqyhConfig.myInterfaceList && wxqyhConfig.myInterfaceList.length>0 && isVipGold(interfaceCode.INTERFACE_CODE_PRIME)){
						$("#isvip").attr("class","vipLogo vipGold");
						if(!isCoop){
							$("#vipTop").html("金卡VIP");
						}
						$("#vipTop").attr("class","vipGold1");
					}else{
						$("#isvip").attr("class","vipLogo vip1");
						if(!isCoop){
							$("#vipTop").html("银卡VIP");
						}
						$("#vipTop").attr("class","vipTop");
					}
				});
				$("#vipTime").html("有效期至："+logo.VIPTime);
			}else if(2<=logo.vipGrade){
				//金卡以上
				$("#isvip").attr("class","vipLogo vipGold");
				if(!isCoop){
					$("#vipTop").html("金卡VIP");
				}
				$("#vipTop").attr("class","vipGold1");
			}else{
				//普通会员
				$("#isvip").attr("class","vipLogo novip");
				if(!isCoop){
					$("#vipTop").html("银卡VIP");
				}
				$("#vipTop").attr("class","novip1");
			}

			$("#vipTime").html("有效期至："+logo.VIPTime);
			$("#isvipWrap").show();
			//快过期提醒方法
			wxqyhConfig.ready(function(){
				expirationRemind(logo);
			});
		}else if(logo.isVIP=='0'){
			//已过期
			if(logo.VIPTime!=null && logo.VIPTime!=""){
				var curTime=new Date().Format("yyyy-MM-dd");
				if(dateCompare(logo.VIPTime,curTime)){
					$("#isvip").attr("class","vipLogo novip");
					if(!isCoop){
						$("#vipTop").html("银卡VIP");
					}
					$("#vipTop").attr("class","novip1");
					$("#vipTime").attr("class","red");
					$("#vipTime").html("已过期："+logo.VIPTime);
					$("#isvipWrap").show();
				}
			}else{
				//普通会员
				$("#isvip").attr("class","vipLogo novip");
				$("#vipTop").attr("class","novip1");
				if(isCoop){
					$("#vipTime").html("未开通");
				}else{
					$("#vipTop").html("银卡VIP");
					$("#vipTime").html("未开通"+"&nbsp;&nbsp;<a target=\"_blank\" href="+vipURL+">[ 立即开通 ]</a>");
				}
				$("#isvipWrap").show();
			}
		}
		//金卡接口
		if(logo.myInterfaceList && logo.myInterfaceList.length>0){
			var ifHtml="";
			for(var i=0;i<logo.myInterfaceList.length;i++){
				if(i==0){
					ifHtml = ifHtml + "<span>" + logo.myInterfaceList[i] + "</span>";
				}else{
					ifHtml = ifHtml + "<br/>" + "<span>" + logo.myInterfaceList[i] + "</span>";
				}
			}
			$("#goldIfDiv").append(ifHtml);
			if(!(isCoop && logo.cooperationSettingVO.type=="1")){
				$("#goldIfDiv").show();
			}
		}
		//文件空间使用状态
		if(logo.fileStatisticsVO!=null && logo.fileStatisticsVO!=""){
			//进度条
			var buyBtnHtml="";
			if(!isCoop){
				buyBtnHtml="&nbsp;&nbsp;<a target=\"_blank\" href="+spaceBuyURL+">[ 升级 ]</a>";
			}
			wxqyhConfig.ready(function(){
				if(wxqyhConfig.is_qiweiyun){
					if(logo.fileStatisticsVO.canuseSpace>=0){
						//正常状态
						if(logo.fileStatisticsVO.sumSpace>200){
							var sumSpace= logo.fileStatisticsVO.sumSpace/1024;
							$("#sumSpace_spanId").html("总空间："+sumSpace.toFixed(2)+"GB"+buyBtnHtml);
						}else{
							$("#sumSpace_spanId").html("总空间："+logo.fileStatisticsVO.sumSpace+"MB"+buyBtnHtml);
						}
						$("#useSpace_spanId").html("已使用："+logo.fileStatisticsVO.cannotuseSpace+"MB");
						$("#progress_id").attr("value",logo.fileStatisticsVO.cannotusePercent);
						$("#progress_divId").attr("style","width:"+logo.fileStatisticsVO.cannotusePercent+"%;");
						if(logo.fileStatisticsVO.cannotusePercent>=0 && logo.fileStatisticsVO.cannotusePercent<=20){
							$("#progress_id").attr("class","i20");
						}else if(logo.fileStatisticsVO.cannotusePercent>=90 && logo.fileStatisticsVO.cannotusePercent<=100){
							$("#progress_id").attr("class","i90");
							//弹框提醒
							if(logo.spaceTixingCount<2){
								if(isCoop){
									//90%时每次登录只提示一次
									_alert("超限提示","空间使用量已超过90%，为了不影响正常使用，请尽快购买存储空间。");
								}else{
									//90%时每次登录只提示一次
									_alert("企微云平台空间超限提示","空间使用量已超过90%，为了不影响正常使用，请尽快购买存储空间，<br/><a target='_blank' href='"+spaceBuyURL+"'>点击链接购买。</a>");
								}
							}
						}else{
							$("#progress_id").attr("class","");
						}
					}else{
						//超过总容量
						$("#progress_id").attr("value","100");
						$("#progress_divId").attr("style","width:100%;");
						$("#progress_id").attr("class","i90");
						$("#useSpace_spanId").html("已使用："+logo.fileStatisticsVO.cannotuseSpace+"MB");
						if(logo.fileStatisticsVO.sumSpace>200){
							var sumSpace= logo.fileStatisticsVO.sumSpace/1024;
							$("#sumSpace_spanId").html("总空间："+sumSpace.toFixed(2)+"GB"+buyBtnHtml);
						}else{
							$("#sumSpace_spanId").html("总空间："+logo.fileStatisticsVO.sumSpace+"MB"+buyBtnHtml);
						}

						var overPercent = parseInt(logo.fileStatisticsVO.cannotuseSpace) / parseInt(logo.fileStatisticsVO.sumSpace);
						if(overPercent>=2){
							if(isCoop){
								_alert("空间超限提示","空间使用量已超过"+((overPercent * 100).toFixed(0))+"%，为了不影响正常使用，请尽快购买存储空间。");
							}else{
								_alert("企微云平台空间超限提示","空间使用量已超过"+((overPercent * 100).toFixed(0))+"%，为了不影响正常使用，请尽快购买存储空间，<br/><a target='_blank' href='"+spaceBuyURL+"'>点击链接购买！</a>");
								$("#imMsgBox").find(".modal-del").hide();
								$("#imMsgBox").find("#imMsgBoxEnterBut").click(function(){window.location.href="http://qy.do1.com.cn/qwy/qiweipublicity/companysrv/space/space_index.jsp"});
							}
						}else{
							//超过容量每次提醒
							if(isCoop){
								_alert("空间超限提示",logo.fileStatisticsVO.orgName+"的管理员，你的账号存储空间已经超限，请尽快购买或者升级存储空间，以免影响系统正常使用。");
							}else{
								_confirm("企微云平台存储空间超限提示",logo.fileStatisticsVO.orgName+"的管理员，你的账号存储空间已经超限，请尽快购买或者升级存储空间，以免影响系统正常使用。<a target='_blank' href='"+spaceBuyURL+"'>点击获得升级秘籍！</a>"
									,"立即购买|稍后再说",{"ok":function(){
										window.open(baseURL+"/qiweipublicity/companysrv/space/space_index.jsp");
									},"fail":function(){

									}});
							}
						}
					}
				}
			});
		}else{
			$("#useSpace_divId").hide();
			$("#progress_id").hide();
		}
		//=======设置fooder.jsp版权信息
		$("#fooderCopyrightId").html(logo.copyright);
	}else if("welcome_manager"==pageType){//后台管理:manager/welcome.jsp
		if(isCoop && logo.welcomeImgurl.indexOf("welcome_img.png")<0){ //&& logo.cooperationSettingVO.type=="1"
			$(".QWInfoClass").hide();
			$("#welcomeImgDiv_id").css('background-image','url('+compressURL+logo.welcomeImgurl+')');
			$("#welcomeImgDiv_id").css('height','600px');//设置高度，600px为建议的最大高度(900x600)
			$("#welcomeImgDiv_id").show();
		}else{
			$(".QWInfoClass").show();
			//显示问题信息，显示二维码图片
			$("#erweima_id").show();
			$("#helpList_id").show();
			$("#textdiv_id").show();
			$("#welcomeloadDetailId").show();
			var wxqrcode = logo.wxqrcode;
			if(wxqrcode){
				wxqrcode = baseURL+"/portal/errcode/errcodeAction!loadImage.action?imgUrl="+ encodeURIComponent(wxqrcode);
				$("#erweima_id").attr("src",wxqrcode);
			}
			else{
				$("#wxqrcode_refresh").show();
			}
			//显示corpId
			$("#corpId_lblid").html(logo.corpId);
			//显示图片div
			$("#welcomeImgDiv_id").show();
			wxqyhConfig.ready(function() {
				if (wxqyhConfig.is_qiweiyun) {
					//显示VIP过期提醒
					//如果VIP创建时间少于7天，不提醒
					var endTime=logo.VIPTime;
					var curTime=new Date().Format("yyyy-MM-dd");
					var vipStartDay=logo.VIPStartDay
					//var vipStartDay="2016-08-12";
					var iDaysBeginVip=GetDateDiff(curTime,vipStartDay);
					if(iDaysBeginVip>6){//VIP开始时间大于7天才提醒
						var iDays=GetDateDiff(curTime,endTime);
						if(iDays<=60){
							var date1=new Date(endTime.replace(/-/g, "\/"));
							$("#vipNoteTipsSpan").html("您的"+getVipName()+"即将在"+date1.getFullYear()+"年"+(date1.getMonth()+1)+"月"+date1.getDate()+"日到期");
							$("#vipNoteDiv").show();
						}
					}
				}
			});
		}
	}else if("main_qwweb"==pageType){//个人网页版:qwweb/main.jsp
		//显示title名字
		if(isCoop){
			$('title').html(logo.cooperationSettingVO.cooperName+"—"+title_qw_qwweb_coop);
		}else{
			$('title').html("企微云平台—"+title_qw_qwweb);
		}
		//判断是文件服务器还是自己本机
		var fileUrl="";
		if(isCoop){
			fileUrl=compressURL;
		}else{
			fileUrl=baseURL;
		}

		//设置个人网页版logo
		var logoSrc;
		if(logo.personwebLogourl==personwebLogourl){
			logoSrc=baseURL+logo.personwebLogourl
		}else{
			logoSrc=fileUrl+logo.personwebLogourl
		}
		$("#personwebLogourl").attr("src",logoSrc);
		//设置版权信息
		$("#fooderCopyrightId").html(logo.copyright);
		
		//判断合作商，设置相对的链接
		if(isCoop){
			if(logo.cooperationSettingVO.type=="1" && logo.personwebWelcomeurl.indexOf(personwebWelcomeurl)<0){
				$("#qwwebFrame").attr("src",logo.personwebWelcomeurl);
			}else{
				$("#qwwebFrame").attr("src",baseURL+logo.personwebWelcomeurl);
			}
			$("#helpurlId").attr("href",logo.personwebHelpintroductionurl);	
			//如果是合作商登陆,记录登陆链接到cookie
			setCookies("do1qw@loginType",logo.personType);
			setCookies("do1qw@coopType",logo.cooperationSettingVO.type);
			setCookies("do1qw@loginUrl",logo.personwebLoginurl);//保留这个cookie，这里是后台和个人网页版统一时的链接
			setCookies("do1qw@managerLoginurl",logo.managerLoginurl);
			setCookies("do1qw@qwwebLoginurl",logo.personwebLoginurl);
		}else{
			//个人网页版欢迎地址
			$("#qwwebFrame").attr("src",baseURL+logo.personwebWelcomeurl);
		}
		//帮助信息超链接(绝对地址)
		$("#helpurlId").attr("href",logo.personwebHelpintroductionurl);
		//显示公司名称
		if(logo.companyName){
			$("#companyName").html(logo.companyName);
			$("#companyName").attr("title",logo.companyName);
		}
	}else if("error_manager"==pageType){//后台管理:manager/error_xxx.html
		//如果不是我们公司登录，全部隐藏
		if(logo.personType=="mycompany" || logo.personType=="cooperation"){
			$("#backindex_aid").show();
			$("#backindex_aid").attr("href",logo.personwebLoginurl);
		}else{
			$("#backindex_aid").hide();
		}
	}else if("error_microphone"==pageType){//手机端:jsp/wap/tips/error_xxx.html
		//判断合作商，设置返回登录页链接
		if(logo.personType=="cooperation" || logo.personType=="mycompany"){
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
		}else if(isCoop){
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
		if(isCoop){
			indexUrl=logo.personwebLoginurl;
		}
		$("#indexUrl").html(indexUrl);
	}else if("reimbursement"==pageType){//	jsp/reimbursement/add.jsp,edit.jsp
		if(logo.personType=="mycompany"){
			$("#howVIP_project").show();
			$("#howVIP_subject").show();
		}else if(isCoop){
			$("#howVIP_project").hide();
			$("#howVIP_subject").hide();
		}else{
			$("#howVIP_project").show();
			$("#howVIP_subject").show();
		}
	}else if("flow_group_list_manager"==pageType){
		if(isCoop){
			$("#vipTipProJ_dviId").show();
		}else if(logo.personType=="cooperation"){
			$("#vipTipProJ_dviId").hide();
		}else{
			$("#vipTipProJ_dviId").show();
		}
	}
}


//写入cooke
function setCookies(name,value){
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

//快过期提醒方法
function expirationRemind(logo){
	var endTime=logo.VIPTime;
	var curTime=new Date().Format("yyyy-MM-dd");
	//vip快过期时倒数30天、15天、7天分别提示一次
	var iDays=GetDateDiff(curTime,endTime);
	if(parseInt(iDays)==30 || parseInt(iDays)==15 || parseInt(iDays)==7){
		var str=endTime.split("-");
		var year=str[0];
		var month=str[1];
		var day=str[2];
		if(logo.hasRecord=="0"){
			var vipNotifyMsg ="亲爱的管理员，您的"+getVipName()+"将在"+iDays+"天后于"+year+"年"+month+"月"+day+"日到期，到期后相关VIP功能将受到限制，为避免影响您的正常工作，提醒您提前续期VIP资格。";
			if(isQuDao()){
				_alert("温馨提示", vipNotifyMsg, function(){
					xufeiCallBack();
				})
			}else{
				_confirm("温馨提示",vipNotifyMsg,"关闭|马上续期"
					,{ok:function(result){saveRemindRecord();},fail:function(result){xufeiCallBack();}}
				);
			}
		}
	}else if(parseInt(iDays)==0){
		if(logo.hasRecord=="0"){
			var vipNotifyMsg ="亲爱的管理员，你的"+getVipName()+"资格将在今日到期，为避免影响您的正常工作，提醒您提前续期VIP资格。";
			if(isQuDao()){
				_alert("温馨提示", vipNotifyMsg, function(){
					xufeiCallBack();
				})
			}else{
				_confirm("温馨提示",vipNotifyMsg,"关闭|马上续期"
					,{ok:function(result){saveRemindRecord();},fail:function(result){xufeiCallBack();}}
				);
			}
		}
	}
}

//续费回调//如何成为银卡VIP用户
function  xufeiCallBack(){
	saveRemindRecord();
	if(!isQuDao()){
		window.open(vipUrl);
	}
}

//打开vip链接:vip
function  openVIPURL(){
	if(!isQuDao()){
		window.open(vipURL);
	}
}

//打开vip链接，跳转到续费//如何成为银卡VIP用户
function openXufeiURL(){
	//注意大小写有区别
	if(!isQuDao()){
		window.open(vipUrl);
	}
}

//计算2个时间的相隔天数
function GetDateDiff(startDate,endDate)  
{  
    var startTime = new Date(Date.parse(startDate.replace(/-/g,   "/"))).getTime();     
    var endTime = new Date(Date.parse(endDate.replace(/-/g,   "/"))).getTime();     
    var dates = Math.abs((startTime - endTime))/(1000*60*60*24);     
    return  dates;    
}

//保存提醒记录
function saveRemindRecord(){
	$.ajax({
		url:baseURL+"/vip/vipAction!ajaxAddRemindRecords.action",
		type:"POST",
		data:"",
		dataType:"json",
		success:function(result){
			hideLoading();
			if(result.code=="0"){
				
			}else{
				_alert("提示",result.desc);
			}
		},
		error:function(){
			hideLoading();
			_alert("提示","获取失败","确认",function(){restoreSubmit();});
		}
	});
}

function dateCompare(startdate,enddate)   
{   
	var arr=startdate.split("-");    
	var starttime=new Date(arr[0],arr[1],arr[2]);    
	var starttimes=starttime.getTime();   
	  
	var arrs=enddate.split("-");    
	var lktime=new Date(arrs[0],arrs[1],arrs[2]);    
	var lktimes=lktime.getTime();   
	  
	if(starttimes>=lktimes)    
	{   
	return false;   
	}   
	else  
	return true;   
}

//获取VIP等级名称
function getVipName(){
	var vipName = "VIP";
	if(wxqyhConfig.vip_grade ==1){
		vipName ="银卡VIP";
	}else if(wxqyhConfig.vip_grade>1){
		vipName ="金卡VIP";
	}
	return vipName;
}
