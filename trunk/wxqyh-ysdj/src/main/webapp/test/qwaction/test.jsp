<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<%@include file="/jsp/common/dqdp_common_dgu.jsp" %>
    <head>
        <title>接口测试工具-作者：孙青海</title>
		<meta charset="utf-8">
		<meta name="description" content="">
		<meta name="HandheldFriendly" content="True">
		<meta name="MobileOptimized" content="320">
		<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<meta content="telephone=no" name="format-detection" />
		<meta content="email=no" name="format-detection" />
		<link rel="stylesheet" href="${baseURL}/themes/wap/css/base.css?ver=<%=jsVer%>" />
		<link rel="stylesheet" href="${baseURL}/themes/wap/css/add.css?ver=<%=jsVer%>" />
		<link rel="stylesheet" href="${baseURL}/themes/wap/css/choose.css?ver=<%=jsVer%>" />
		<link rel="stylesheet" href="${baseURL}/jsp/wap/css/font-awesome.min.css?ver=<%=jsVer%>" />
		<script src='${baseURL}/js/3rd-plug/jquery-ui-1.8/js/jquery-1.7.2.min.js'></script>
		<script type="text/javascript" src="${baseURL}/jsp/wap/js/detect-jquery.js"></script>
        <script>
			var localStorageData = [];
			$(function(){
				var localStorageStr = localStorage.getItem("localStorageData");
				if (localStorageStr) {
					localStorageData = JSON.parse(localStorageStr);
					$("#action_url").val(localStorageData[0].url);
					$("#post_value").val(localStorageData[0].data);
				}
			});
			String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {
				if (!RegExp.prototype.isPrototypeOf(reallyDo)) {
					return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);
				} else {
					return this.replace(reallyDo, replaceWith);
				}
			}

			/**
			 * 设置缓存
			 * @param url
             * @param data
             */
			function setLocalStorageItem (url, data) {
				var json = {"url":url,"data":data};
				if (localStorageData.length > 0) {
					var last = localStorageData.length;
					if (last >= 30) {
						last = 29;
					}
					for (var i = 0; i < localStorageData.length; i++) {
						if (localStorageData[i].url == url) {
							last = i;
							break;
						}
					}
					for (var i = last; i>0; i--) {
						localStorageData[i] = localStorageData[i-1];
					}
					localStorageData[0] = json;
				}
				else {
					localStorageData[0] = json;
				}
				localStorage.setItem("localStorageData", JSON.stringify(localStorageData));
			}
			function chooseUrl(){
				if (localStorageData.length > 0) {
					var temp = "";
					for (var i = 0; i < localStorageData.length; i++) {
						if (localStorageData[i]) {
							temp += "<li>" + localStorageData[i].url + "</li>";
						}
					}
					$("#chooseUrlUl li").off();
					$("#chooseUrlUl").html(temp);
					$("#chooseUrlUl").show();
					$("#chooseUrlUl li").on('click',function(){
						var index = $(this).index();
						$("#action_url").val(localStorageData[index].url);
						$("#post_value").val(localStorageData[index].data);
					});
				}
			}
			function closeChooseUrl(){
				setTimeout(function () {
					$("#chooseUrlUl").hide();
				}, 200);
			}
        	//提交
        	function commit(){
        		//限制双重提交
        		$(".submitClass").attr("disabled", "disabled");
				$(".submitClass").css("pointer-events","none");

				var url  = $("#action_url").val();
				if($("#action_url").val()==""){
					alert("请输入url");
					return ;
				}
				$("#post_result").val("提交中...");

				var data = $("#post_value").val();
				setLocalStorageItem(url, data);
				data = data.replaceAll(/[\r\n]/g,""); //去掉回车换行
				$.ajax({
					url:url,
        			type:"POST",
					data: data,
	        		dataType:"json",
	        		success:function(result){
	    				$(".submitClass").removeAttr("disabled");
	    				$(".submitClass").css("pointer-events","auto");
						$("#post_result").val(formatJson(result))
	        		},
	        		error:function(){
	    				$(".submitClass").removeAttr("disabled");
	    				$(".submitClass").css("pointer-events","auto");
	        			alert("网络异常");
	        		}
        		});
        	}
			var formatJson = function(json, options) {
				var reg = null,
						formatted = '',
						pad = 0,
						PADDING = '    '; // one can also use '\t' or a different number of spaces

				// optional settings
				options = options || {};
				// remove newline where '{' or '[' follows ':'
				options.newlineAfterColonIfBeforeBraceOrBracket = (options.newlineAfterColonIfBeforeBraceOrBracket === true) ? true : false;
				// use a space after a colon
				options.spaceAfterColon = (options.spaceAfterColon === false) ? false : true;

				// begin formatting...
				if (typeof json !== 'string') {
					// make sure we start with the JSON as a string
					json = JSON.stringify(json);
				} else {
					// is already a string, so parse and re-stringify in order to remove extra whitespace
					json = JSON.parse(json);
					json = JSON.stringify(json);
				}

				// add newline before and after curly braces
				reg = /([\{\}])/g;
				json = json.replace(reg, '\r\n$1\r\n');

				// add newline before and after square brackets
				reg = /([\[\]])/g;
				json = json.replace(reg, '\r\n$1\r\n');

				// add newline after comma
				reg = /(\,)/g;
				json = json.replace(reg, '$1\r\n');

				// remove multiple newlines
				reg = /(\r\n\r\n)/g;
				json = json.replace(reg, '\r\n');

				// remove newlines before commas
				reg = /\r\n\,/g;
				json = json.replace(reg, ',');

				// optional formatting...
				if (!options.newlineAfterColonIfBeforeBraceOrBracket) {
					reg = /\:\r\n\{/g;
					json = json.replace(reg, ':{');
					reg = /\:\r\n\[/g;
					json = json.replace(reg, ':[');
				}
				if (options.spaceAfterColon) {
					reg = /\:/g;
					json = json.replace(reg, ':');
				}

				$.each(json.split('\r\n'), function(index, node) {
					var i = 0,
							indent = 0,
							padding = '';

					if (node.match(/\{$/) || node.match(/\[$/)) {
						indent = 1;
					} else if (node.match(/\}/) || node.match(/\]/)) {
						if (pad !== 0) {
							pad -= 1;
						}
					} else {
						indent = 0;
					}

					for (i = 0; i < pad; i++) {
						padding += PADDING;
					}

					formatted += padding + node + '\r\n';
					pad += indent;
				});

				return formatted;
			};
        </script>
        <style>
        .inputStyle{
	        text-align: left;
			line-height: 18px; 
			border: none;
			font-size: 14px;
			vertical-align: top;
        }
		.dropQuery {
			background: #fff none repeat scroll 0 0;
			border: 1px solid #ddd;
			max-height: 260px;
			overflow: auto;
			padding-bottom: 4px;
			z-index: 3333333;
		}
		.dropQuery li{
			 cursor:pointer;
		 }
		</style>
    </head>
    
    <body>
        <div id="tips_main" class="wrap">
            <div id="main" class="wrap_inner">
                <div class="form-style">
                	<div class="f-item ">
                        <div class="inner-f-item"> 
	                        <div class="flexItem">
                        		<input type="text" id="action_url" onfocus="chooseUrl()" onblur="closeChooseUrl()" value="" maxlength="1000" valid="{must:true,maxLength:1000,fieldType:'pattern',regex:'^.*$',tip:'url'}" id="title" placeholder="请输入url，例：http://localhost:8080/wxqyh/test/qwaction/test.jsp" class="item-select inputStyle maxlength"/>
                        	</div>
							<ul class="dropQuery" id="chooseUrlUl" style="position: absolute; top: 33px; left: 10px; width: 558px; display: none;">
							</ul>
                        </div>
                    </div>
                    <div class="f-item">
                        <div class="inner-f-item">
                            <div class="text_div">
                                <textarea id="post_value" onblur="" cols="30" rows="10" placeholder="请求报文，类似id=123&title=张三" maxlength="1000" class="item-select inputStyle"></textarea>
                            </div>
                        </div>
                    </div>

					
					<!-- 上传媒体文件（手机端页面）引入  end -->
                    <div class="form_btns mt10">
                        <div class="inner_form_btns">
                            <div class="fbtns flexbox"> 
 								<a class="fbtn btn flexItem submitClass" style="margin-left: 5px;" href="javascript:commit()">立即执行</a>
                            </div>
                        </div>
                    </div>
					<div class="f-item">
						<div class="inner-f-item">
							<div class="text_div">
								<textarea id="post_result" onblur="" cols="30" rows="15" placeholder="这里显示结果" maxlength="1000" class="item-select inputStyle"></textarea>
							</div>
						</div>
					</div>
                </div>
            </div>
        </div>
    </body>
</html>