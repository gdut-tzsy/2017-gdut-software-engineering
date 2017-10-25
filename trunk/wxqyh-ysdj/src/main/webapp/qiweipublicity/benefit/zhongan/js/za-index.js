var priList = [[68,68],[188,258],[248,380],[368,570],[398,528],[518,718],[578,840],[698,1030]];
	
	//var priList = [50,299,470];
	
    BUI.use(['bui/slider','bui/overlay','bui/form','bui/tooltip'],function (Slider,Overlay,Form,Tooltip) {
    	var sliList = new Array();
        var slider1 = new Slider.Slider({
            render : '#m-slider',
            max:500,
            min:3,
            value : 100
        });
        slider1.render();
        slider1.on('change',function(ev){
            $(ev.target.get("render")).find(".x-slider-tip").html(ev.value + "人");
            var sum = parseInt(ev.value,10) * priList[0]; 
            $("#pricesum").html("￥"+ rendMoney(sum));
        });
        sliList.push(slider1);

        var slider2 = new Slider.Slider({
            render : '#m-slider1',
            max:500,
            min:3,
            value : 100
        });
        slider2.render();
        slider2.on('change',function(ev){
            $(ev.target.get("render")).find(".x-slider-tip").html(ev.value + "人");
            var sum = parseInt(ev.value,10) * priList[1]; 
            $(ev.target.get("render")).find(".x-slider-txt").html(rendMoney(sum) + "元");

            var sum1 = parseInt(sliList[2].get("value"),10) * priList[2]; 

            $("#pricesum1").html("￥"+ rendMoney(sum+sum1));
        });

        sliList.push(slider2);

        var slider3 = new Slider.Slider({
            render : '#m-slider2',
            max:500,
            min:0,
            value : 20
        });
        slider3.render();
        slider3.on('change',function(ev){
            $(ev.target.get("render")).find(".x-slider-tip").html(ev.value + "人");
            var sum = parseInt(ev.value,10) * priList[2]; 
            $(ev.target.get("render")).find(".x-slider-txt").html(rendMoney(sum) + "元");

            var sum1 = parseInt(sliList[1].get("value"),10) * priList[1]; 

            $("#pricesum1").html("￥"+ rendMoney(sum+sum1));
        });
        sliList.push(slider3);

        $(".x-slider-handle").each(function(i,dom){
            $(dom).append("<span class='x-slider-tip'>"+ sliList[i].get("value") +"人</span>");
            if(i > 0){
                var sum = parseInt(sliList[i].get("value"),10) * priList[i]; 
                $(dom).append("<span class='x-slider-txt'>"+ rendMoney(sum) +"元</span>");

            }
        });
        
        
        fnHotInit();

        var scrollTop = $(window).scrollTop();
        if(scrollTop > 500){
            $("#zahottop").show(300);
        }else{
            $("#zahottop").hide(300);
        }
        
        $(window).scroll(function(){
            var scrollTop = $(window).scrollTop();
            if(scrollTop > 500){
                $("#zahottop").show(300);
            }else{
                $("#zahottop").hide(300);
            }
        });

        $('a[href*=#]').click(function(){
            if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
                var $target = $(this.hash);
                $target = $target.length && $target || $('[name=' + this.hash.slice(1) + ']');
                if ($target.length) {
                    $('html,body').animate({
                        scrollTop: $target.offset().top
                    }, 800);
                }else{
                    $('body,html').animate({scrollTop:0},1000);
                }
                return false;
            }
        });
		
        
        var productType=0;
        var msgTxt="您的需求已提交，我们会尽快与您联系。<br/>如有任何疑问也可拨打400-999-9595联系我们。谢谢!"
        // 人工投保 弹窗
        var Insdialog = new Overlay.Dialog({
            title:'投保审核',
            contentId:'m-dig',
            buttons:[{
            	text:'确定',
                elCls : 'u-btn write g-wid120',
                handler : function(){
                	zaToolsForm.valid();
                	if(zaToolsForm.isValid()){
    	                
    	  				$.ajax({
    	                    type: "POST",
    	                    dataType: "json",
    	                    url: "/open/eportal/manualUnderwriteAjax/customize",
    	                    data: {
    	                        "companyName": $('#companyName').val(),
    	                        "contactName": $('#contactName').val(),
    	                        "contactPhone": $('#contactPhone').val(),
    	                        "notes": $('#notes').val() == "请输入您的需求" ? "" : $('#notes').val()
    	                    },
    	                    success:function(){
    	                    	dhtml = '<br/><br/><div><div class="x-icon me-success"></div><span class="me-bd">' + msgTxt + '</span></div><br/><br/>';
    	                    	dialog = new Overlay.Dialog({
    	                            title: '提示',
    	                            elCls: "za-dialog-message",
    	                            bodyContent: dhtml,
    	                            buttons:[
    	                                     {
    	                                         text:'我知道了',
    	                                         elCls : 'u-btn write g-wid140',
    	                                         handler : function(){
    	                                             this.close();
    	                                         }
    	                                     }
    	                                 ],
    	                            closeAction: "destroy",
    	                            success: function() {
    	                                this.close();
    	                            }
    	                        });
    	                    	
    	                    	dialog.show();
    	                    }
    	                });
    	            	
    	                this.close();
                	}
                }
            }]
        });

        Insdialog.on("show",function(){
        	$(".bui-stdmod-footer").css({"textAlign":"center","borderTop":"none"});
        });
        
//        $(".m-adv-buy").bind("click",function(){
//        	var link = "eportal/order/insure1";
//        	postRequest(link, {index : tc_index});
//        });
        
        /*
        $(".m-adv-buy").eq(1).bind("click",function(){
            var num2 = parseInt(slider2.get("value"),10); //升级版A年龄人数     
            var num3 = parseInt(slider3.get("value"),10); //升级版B年龄人数
            if((num3 / (num2+num3) >= 0.3 || num2 + num3 > 200)){
            	productType = 0;

            	var msgcon='<p>尊敬的客户您好，您的投保单尚需人工审核。请在下方</p><p class="g-mar-t15">留下您的联系方式，我们的客服会尽快致电联系您。</p>'
            	var msgcon1='<div class="f-fl"><img width="72" height="72" src="../resource/images/enterprise/ent_success.png"></div>';
            	
            	$("#m-dig-con").html(msgcon);
            	$("#m-dig-con").html(msgcon).removeClass("g-mar-t30");
            	if($("#m-dig-con").prev(".f-fl").length==0){
            		$("#m-dig-con").before(msgcon1);
            	}
            	Insdialog.set("title","投保审核");
            	
            	Insdialog.show();
            }else{
            	window.location.href="/eportal/order/insure1?index=1";
            }
        });
        */
        $(".m-adv-buy2").bind("click",function(){
        	$("#m-dig-fm").find(":text").val('');
        	$("#m-dig-fm").find("textarea").val('请输入您的需求').css("color","#ccc");
        	productType=1;

        	
        	var msgcon ='<p>请填写以下信息，我们的专业健康管家会第一时间联系您！</p>';
        	$("#m-dig-con").html(msgcon).addClass("g-mar-t30");
        	$("#m-dig-con").prev(".f-fl").remove();
        	Insdialog.set("title","方案定制");
        	Insdialog.show();
        });
        
        /*
        //点击购买
        $("#m-buy").bind("click",function(){
            var index = $(".m-adv-item").eq(0).hasClass("active") ? 0 : 1;        
            var num = parseInt(slider1.get("value"),10); //基础版人数     
            var num2 = parseInt(slider2.get("value"),10); //升级版A年龄人数     
            var num3 = parseInt(slider3.get("value"),10); //升级版B年龄人数
            
            if(index == 1 && (num3/(num2+num3) >= 0.3 || num2+num3>200)){
            	Insdialog.show();
            	
            }else{
                if(index==0){
                	window.location.href="/eportal/order/insure1?index="+index;
                }else if(index==1){
                	window.location.href="/eportal/order/insure1?index="+index;
                }
            }            
        })
        */
        
        //立即购买跳转效果
        $(".m-sld .u-btn").on("click",function(){
            sroTop();
        });
       
        $(".btn-orange").on("click",function(){
            sroTop();
        });
    });
    
    
    function sroTop(){
        var windowh = $(window).height();
        var itemh = 700;

        var top = $(".m-adv-buy").eq(0).offset().top;
        var th = 67 + 48 + 551 + 68;
        $('body,html').animate({ scrollTop: th},800);
        //if(windowh > itemh){
        //    $('body,html').animate({ scrollTop: top - itemh - 80},800);
        //}else{
        //    $('body,html').animate({ scrollTop: top - windowh + 80 },800);
        //}
    }
    
    
    /*数字千分符*/  
    function rendMoney(v) {  
        if(isNaN(v)){  
            return v;  
        }  
        v = (Math.round((v - 0) * 100)) / 100;  
        v = (v == Math.floor(v)) ? v + ".00" : ((v * 10 == Math.floor(v * 10)) ? v  
                + "0" : v);  
        v = String(v);  
        var ps = v.split('.');  
        var whole = ps[0];  
        var r = /(\d+)(\d{3})/;  
        while (r.test(whole)) {  
            whole = whole.replace(r, '$1' + ',' + '$2');  
        }  
        v = whole;  
          
        return v;  
    }  
    function zaloginInit() {
        if ($("#logininfo").length) {
            $.ajax({
                type: "POST",
                dataType: "json",
                url: "open/common/check_login_screen/check_login_dto.json",
                ajaxStart:function(){},
                ajaxComplete:function(){},
                success: function(data) {
                    var $loginObj = $("#logininfo");
                    if (data) {
                        if (data.loginName == undefined) {
                        	$loginObj.html('<span>欢迎您，<a href="/account/myAccount"  title="点击进入我的账户"  class="s-lk-tp g-mar-lr5">' + data.phone + '</a><a  id="loginOut" onClick="loginOut();" href="javascript:;" class="s-lku">退出</a>');
                        	//$loginObj.html('<span>欢迎您，<a href="/account/myAccount"  title="点击进入我的账户">' + data.phone + '</a></span><span class="g-mar-lr5">&nbsp;</span><a  id="loginOut" onClick="loginOut();" href="javascript:;">退出</a>');
                            //$loginObj.html('<div class="logined">欢迎你，<a href="/account/myAccount" title="点击进入我的账户" class="za-index-link-yhm">' + data.phone + '</a>&#12288;<input type="button" id="loginOut" onClick="loginOut();" value="退出" class="za-index-link-green"></div>');
                        } else {
                        	$loginObj.html('<span>欢迎您，<a href="/account/myAccount"  title="点击进入我的账户"  class="s-lk-tp g-mar-lr5">' + data.loginName + '</a><a  id="loginOut" onClick="loginOut();" href="javascript:;" class="s-lku">退出</a>');
                        	//$loginObj.html('<span>欢迎您，<a href="/account/myAccount"  title="点击进入我的账户">' + data.loginName + '</a></span><span class="g-mar-lr5">&nbsp;</span><a  id="loginOut" onClick="loginOut();" href="javascript:;">退出</a>');
                            //$loginObj.html('<div class="logined">欢迎你，<a href="/account/myAccount" title="点击进入我的账户" class="za-index-link-yhm">' + data.loginName + '</a>&#12288;<input type="button" id="loginOut" onClick="loginOut();" value="退出" class="za-index-link-green"></div>');
                        }
                    } else {
                    	$loginObj.html('<a href="/open/member/login" class="g-mar-lr5 s-lk" rel="nofollow">登录</a><div class="m-hd-line "></div><a href="/open/member/register" class="g-mar-lr5 s-lk" rel="nofollow">注册</a>');
                    	//$loginObj.html('<span><a href="/open/member/login">登录</a><span class="g-mar-lr5">|</span><a href="/open/member/register">注册</a></span>');
                        //$loginObj.html('<div class="unlogin"><a href="/open/member/login" class="za-a-login">登录</a><a href="/open/member/register" class="">注册</a></div>');
                    }
                }
            });
        }
        if($("#enterpriseLoginInfo").length){
        	$.ajax({
        		type:"POST",
        		dataType: "json",
        		url: "open/common/check_login_screen/check_enterprise_login.json",
        		success: function(data) {
                    var $loginObj = $("#enterpriseLoginInfo");
                    if (data) {
                    	$loginObj.html('<span>欢迎您，<a href="/eportal/enterpriseAccount"  title="点击进入企业账户"  class="s-lk-tp g-mar-lr5">' + data.email + '</a><a  id="loginOut" onClick="enterpriseLoginOut();" href="javascript:;" class="s-lku">退出</a>');
                    } else {
                    	$loginObj.html('<a href="/open/eportal/login" class="g-mar-lr5 s-lk">登录</a><div class="m-hd-line "></div><a href="/open/eportal/register" class="g-mar-lr5 s-lk">注册</a>');
                    }
                }
        	});
        }
    }
    function getPrice(){
    	var sum = parseInt($('#insNum1').val(),10) * priList[tc_index][0];
        var sum1 = parseInt($('#insNum2').val(),10) * priList[tc_index][1]; 
        if(isNaN(sum))
       	 sum = 0;
        if(isNaN(sum1))
       	 sum1 = 0;
        $("#pricesum1").html("￥"+ rendMoney(sum+sum1));
    }
    var tc_index = 0;
    $(function(){
    	$('.insNum').keyup(function(){
    		$(this).val($(this).val().replace(/\D/g,''));
    		if($(this).val()>500)
    			$(this).val(500);
    		if($(this).val()<0)
    			$(this).val(0);
    		
    		getPrice();
    	})
  		/* $('#banner').unslider({
  			dots: true,
            delay: 4000
        }); */
        $("#bigage").hide();
  		$('.tc-ul li').live("hover",function(){
  			var smallCost,bigCost;
  			var small1 = 68,small2 = 120,small3 = 180,small4 = 330;
  			var big1 = 68,big2 = 190,big3 = 312,big4 = 460;
  			$('.tc-ul li').removeClass('active');
  			$(this).addClass('active');
  			tc_index = $(this).index();
  			$('.m-adv-item2').removeClass('active');
  			$('.m-adv-item2').eq(0).addClass('active');
  			
  			
  			$('#bigage').show();
  			$('#age18').html('18-39周岁');
  			$('#ageperson').html('人');
			$('#ageperson').show();
			
			
  			if(tc_index==0){
  				$('.m-adv-item2').eq(0).addClass('active');
  				$("#costExplain1").hide();
  				$("#costExplain2").hide();
  				smallCost = small1;
  				bigCost = big1;
  				$('#bigage').hide();
  				$('#age18').html('人数：');
  				$('#ageperson').hide();
  			}

  			if(tc_index==1){
  				$('.m-adv-item2').eq(1).addClass('active');
  				$("#costExplain1").show();
  				$("#costExplain2").show();
  				smallCost = small1 + small2;
  				bigCost = big1 + big2;
  			}
  			else if(tc_index==2){
  				$('.m-adv-item2').eq(2).addClass('active');
  				$("#costExplain1").show();
  				$("#costExplain2").show();
  				smallCost = small1 + small3;
  				bigCost = big1 + big3;
  			}
  			else if(tc_index==3){
  				$('.m-adv-item2').eq(1).addClass('active');
  				$('.m-adv-item2').eq(2).addClass('active');
  				$("#costExplain1").show();
  				$("#costExplain2").show();
  				smallCost = small1 + small2 + small3;
  				bigCost = big1 + big2 + big3;
  			}
  			else if(tc_index==4){
  				$('.m-adv-item2').eq(3).addClass('active');
  				$("#costExplain1").show();
  				$("#costExplain2").show();
  				smallCost = small1 + small4;
  				bigCost = big1 + big4;
  			}
  			else if(tc_index==5){
  				$('.m-adv-item2').eq(1).addClass('active');
  				$('.m-adv-item2').eq(3).addClass('active');
  				$("#costExplain1").show();
  				$("#costExplain2").show();
  				smallCost = small1 + small2 + small4;
  				bigCost = big1 + big2 + big4;
  			}
  			else if(tc_index==6){
  				$('.m-adv-item2').eq(3).addClass('active');
  				$('.m-adv-item2').eq(2).addClass('active');
  				$("#costExplain1").show();
  				$("#costExplain2").show();
  				smallCost = small1 + small3 + small4;
  				bigCost = big1 + big3 + big4;
  			}
  			else if(tc_index==7){
  				$('.m-adv-item2').eq(1).addClass('active');
  				$('.m-adv-item2').eq(2).addClass('active');
  				$('.m-adv-item2').eq(3).addClass('active');
  				$("#costExplain1").show();
  				$("#costExplain2").show();
  				smallCost = small1 + small2 + small3 + small4;
  				bigCost = big1 + big2 + big3 + big4;
  			}
  			$("#pri1").text(smallCost);
  			$("#smallCost").text(smallCost);
			$("#bigCost").text(bigCost);
  			getPrice();
  		})
  		
  		$('.gray-img li').on("click",function(){
  			$('.gray-img li').removeClass('active');
  			$(this).addClass('active');
  			var index = $(this).index()+1;
  			$('.s-fc-tp').hide();
			$('.show'+index).show();
  		})
  	})
$(function(){
	$(window).scroll(zascrooll);
	zascrooll();  	
});
function zascrooll(){
	var scrollTop = $(window).scrollTop();
	if (scrollTop > 100){
		$(".za-pro-mflowdeploy").fadeIn(500);
		if(scrollTop > 666){
			$("#za-pro-top").addClass("za-pro-top");
		}else{
			$("#za-pro-top").removeClass("za-pro-top");
		}
	}
	else
	{
		$(".za-pro-mflowdeploy").fadeOut(500);
	}
	
}

$(function(){
	
	function showGuidePop(index){
		$('.za-mask').show();
		$('.za-nt-pop').show();
		$('.tc-list2 li').eq(index).trigger('click');;
	}
	
	$('.up_icon').click(function(){
		$(".up_bei").toggleClass("dis_block");	
	});
	
	$('#za-b-guide a').on('click',function(){
		var _this = $(this);
		_this.addClass('active').siblings().removeClass('active');
		showGuidePop(_this.index());
	});
	
	$('.za-nt-pop .btn-close').on('click',function(){
		$('.za-mask').hide();
		$('.za-nt-pop').hide();
	});
	
});
function getParam(paramName){
	paramValue = "";
	isFound = false;
	if (window.location.search.indexOf("?") == 0 && window.location.search.indexOf("=")>1){
		arrSource = decodeURIComponent(window.location.search).substring(1,window.location.search.length).split("&");
		i = 0;
		while (i < arrSource.length && !isFound){
			if (arrSource[i].indexOf("=") > 0){
				 if (arrSource[i].split("=")[0].toLowerCase()==paramName.toLowerCase()){
					paramValue = arrSource[i].split("=")[1];
					isFound = true;
				 }
			}
			i++;
		}
	}
	return paramValue;
}