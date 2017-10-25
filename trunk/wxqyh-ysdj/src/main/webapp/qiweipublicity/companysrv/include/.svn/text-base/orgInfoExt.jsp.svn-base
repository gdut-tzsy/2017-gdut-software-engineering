<%@page language="java" contentType="text/html; charset=UTF-8"%>
<div class="commonPop" id="orgInfoExtDiv" style="max-height: 650px; display: none;" style="width:600px;">
	<div class="SS_tit">企业开票信息<i class="closeClass">×</i></div>
	<div class="commonPopMain">
		<form class="form" id="orgInfoExtForm">
			<div class="commonPopItem">
				<span>工商注册全称：</span>
				<div class="input_div" style="position: relative;">
					<input type="text" id="corpNameS" class="form-text inputNum" maxlength="150" style="width: 350px;" placeholder="请输入工商注册全称">
					<span class="aa" style="position: absolute; top: 10px; left: 245px;"></span>
					<div class="tipsBox">
						<i class="fa-question-circle"></i>
						<div class="tipsItem">
							<div class="tipsContent">
								工商注册全称用于保障订单合法有效性和发票开具，请准确填写。设置后不能自行修改，如要修改，请联系企微工作人员
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="commonPopItem">
				<span>纳税人识别号：</span>
				<div class="input_div" style="position: relative;">
					<!--valid="{must:true,length:350,fieldType:'pattern',regex:'^.*$',tip:'纳税人识别号'}"-->
					<input type="text" id="corpTaxIdS" class="form-text inputNum" maxlength="100" style="width: 350px;" placeholder="请输入纳税人识别号">
					<span class="aa" style="position: absolute; top: 10px; left: 245px;"></span>
				</div>
			</div>
			<div class="commonPopItem">
				<span>一般纳税人资格证明：</span>
				<div class="input_div">
					<div class="liBtn w80 xfile">
						<input type="hidden"  id="corpLicensePicS" value="">
						<%--<input type="file" id="imgFile" name="imgFile" class="uploadFileInput onlyBtn">--%>
						<input id="imgFile" type="file" name="imgFile" class="uploadFileInput onlyBtn">
						<label for="imgFile"></label>上传图片
					</div>
					<span class="c999 ml5"><%--建议尺寸为300px*150px；--%>支持jpg/bmp/png</span>
					<div class="selected_personnel clearfix" id="msgPic_div" style="margin:10px 0 0 140px;display:none;">
						<li class="selected_personnel_item">
                                <span class="img">
                                    <img src="">
                                </span>
							<%-- <i onclick="delimp(this)"></i>--%>
						</li>
					</div>
				</div>
			</div>
			<div class="commonPopItem">
				<span>注册地址 - 注册电话：</span>
				<div class="input_div" style="position: relative;">
					<input type="text" id="corpRegAddrS" class="form-text inputNum" maxlength="200" style="width: 250px;" placeholder="请输入注册地址">
					<input type="text" id="corpRegTelS" class="form-text inputNum" maxlength="20" style="width: 150px;" placeholder="请输入注册电话">
					<span class="aa" style="position: absolute; top: 10px; left: 245px;"></span>
				</div>
			</div>
			<div class="commonPopItem">
				<span>开户银行 - 银行账号：</span>
				<div class="input_div" style="position: relative;">
					<input type="text" id="corpBankNameS" class="form-text inputNum" maxlength="150" style="width: 150px;" placeholder="请输入开户银行">
					<input type="text" id="corpBankNoS" class="form-text inputNum" maxlength="40" style="width: 150px;" placeholder="请输入银行账号">
					<span class="aa" style="position: absolute; top: 10px; left: 245px;"></span>
				</div>
			</div>
			<div class="commonPopItem">
				<span>办公地址：</span>
				<div class="input_div" style="position: relative;">
					<input type="text" id="workAddrS" class="form-text inputNum" maxlength="200" style="width: 350px;" placeholder="请输入办公地址">
					<span class="aa" style="position: absolute; top: 10px; left: 245px;"></span>
				</div>
			</div>
			<div class="commonPopItem">
				<span>联系人 - 联系电话：</span>
				<div class="input_div" style="position: relative;">
					<input type="text" id="personNameS" class="form-text inputNum" maxlength="150" style="width: 150px;" placeholder="请输入联系人">
					<input type="text" id="mobileS" class="form-text inputNum" maxlength="300" style="width: 150px;" placeholder="请输入联系电话">
					<span class="aa" style="position: absolute; top: 10px; left: 245px;"></span>
				</div>
			</div>

			<div class="commonPopItem">
				<span>开票类型：</span>
				<div class="input_div" style="position: relative;">
					<label>
						增值税普通发票
						<input type="radio" id="invoiceNormalS" name="invoiceTypeR" value="NORMAL" style="margin-top: 10px;" checked="checked"/>
					</label>
					<label class="ml10">
						增值税专用发票
						<input type="radio" id="invoiceCorpS" name="invoiceTypeR" value="CORP" style="margin-top: 10px;"/>
					</label>
					<input type="hidden" id="invoiceTypeS" value="NORMAL"/>
				</div>
			</div>

			<div class="commonPopItem">
				<span>收件地址：</span>
				<div class="input_div" style="position: relative;">
					<select id="invoicePostProvinceS"></select>
					<select id="invoicePostCityS"></select>
					<select id="invoicePostDistructS"></select>
				</div>
				<span>&nbsp;&nbsp;</span>
				<input type="text" id="invoicePostAddrS" class="form-text inputNum" maxlength="300" style="width: 350px;" placeholder="联系地址">
			</div>

			<div class="commonPopItem">
				<span>收件人 - 电话：</span>
				<div class="input_div" style="position: relative;">
					<input type="text" id="invoicePostPersonNameS" class="form-text inputNum" maxlength="20" style="width: 150px;" placeholder="收件人">
					<input type="text" id="invoicePostMobileS" class="form-text inputNum" maxlength="20" style="width: 150px;" placeholder="收件人电话">
				</div>
			</div>

		</form>
	</div>
	<div class="form-action tc mb20" style="margin-top: -20px;">
		<a href="javascript:saveOrgInfoExt();" id="confirmDivBoxOk" class="btn orangeBtn twoBtn">确定</a>
	</div>
</div>

<script language="JavaScript" type="text/javascript">
	$(document).ready(function(){
		$("#orgInfoExtDiv").find(".closeClass").on("click",function(){
			$('#msgshow_bg',window.top.document).hide();
			$("#orgInfoExtDiv").hide();
		});

		$("#invoiceNormalS").on("click", function(){
			$("#invoiceTypeS").val("NORMAL");
		});
		$("#invoiceCorpS").on("click", function(){
			$("#invoiceTypeS").val("CORP");
		});
	});

	//弹出机构消息
	function goOrgInfoExt(obj, type){
		$('#msgshow_bg',window.top.document).show();
		$("#orgInfoExtDiv", window.top.document).show();
		getOrgInfoExt();
	}

	function getOrgInfoExt(){
		$.ajax({
			type:"POST",
			url: baseURL+"/managesetting/managesettingAction!getOrgInfoExtDetail.action",
			async: false,
			dataType: "json",
			success: function(result){
				var orgInfoExtPo=result.data.orgInfoExtPo;
				if(orgInfoExtPo){
					$("#corpNameS",window.top.document).val(orgInfoExtPo.corpName);
					if(""!=orgInfoExtPo.corpName){
						$("#corpNameS",window.top.document).attr("disabled","disabled");
					}else{
						$("#corpNameS",window.top.document).removeAttr("disabled");
					}
					$("#corpLicensePicS",window.top.document).val(orgInfoExtPo.corpLicensePic);
					$("#corpTaxIdS",window.top.document).val(orgInfoExtPo.corpTaxId);
					$("#corpRegAddrS",window.top.document).val(orgInfoExtPo.corpRegAddr);
					$("#corpRegTelS",window.top.document).val(orgInfoExtPo.corpRegTel);
					$("#corpBankNameS",window.top.document).val(orgInfoExtPo.corpBankName);
					$("#corpBankNoS",window.top.document).val(orgInfoExtPo.corpBankNo);
					$("#workAddrS",window.top.document).val(orgInfoExtPo.workAddr);
					$("#personNameS",window.top.document).val(orgInfoExtPo.personName);
					$("#mobileS",window.top.document).val(orgInfoExtPo.mobile);
					//设置开票项类型吧
					if("NORMAL"==orgInfoExtPo.invoiceType){
						$("#invoiceNormalS").attr('checked','true');
					}else if("CORP"==orgInfoExtPo.invoiceType){
						$("#invoiceCorpS").attr('checked','true');
					}

					//输出发票地址信息
					loadAreaSelect_init("invoicePostProvinceS","invoicePostCityS","invoicePostDistructS","NAME"
					,orgInfoExtPo.postProvince,orgInfoExtPo.postCity,orgInfoExtPo.postDistruct);
					$("#invoicePostAddrS",window.top.document).val(orgInfoExtPo.postAddr);
					$("#invoicePostPersonNameS",window.top.document).val(orgInfoExtPo.postPersonName);
					$("#invoicePostMobileS",window.top.document).val(orgInfoExtPo.postMobile);

					var orgInfoExtDiv=$("#orgInfoExtDiv",window.top.document);
					orgInfoExtDiv.find("#msgPic_div").show();
					orgInfoExtDiv.find("#msgPic_div").find("img").attr("src",compressURL+orgInfoExtPo.corpLicensePic);
				}
			},
			error:function() {
				_alert("提示信息","网络出错，请重试");
			}
		});
	}

	function saveOrgInfoExt(){

/*		var c=new Dqdp;
		if(c.submitCheck("orgInfoExtForm")){
			return;
		}
		return;*/

		var corpName=$("#corpNameS").val();
		var corpLicensePic=$("#corpLicensePicS").val();
		var corpTaxId=$("#corpTaxIdS").val();
		var corpRegAddr=$("#corpRegAddrS").val();
		var corpRegTel=$("#corpRegTelS").val();
		var corpBankName=$("#corpBankNameS").val();
		var corpBankNo=$("#corpBankNoS").val();
		var workAddr=$("#workAddrS").val();
		var personName=$("#personNameS").val();
		var mobile=$("#mobileS").val();

		//设置开票选项
		var invoiceType=$("#invoiceTypeS").val();

		var invoicePostProvince=$("#invoicePostProvinceS").val();
		var invoicePostCity=$("#invoicePostCityS").val();
		var invoicePostDistruct=$("#invoicePostDistructS").val();
		var invoicePostAddr=$("#invoicePostAddrS").val();
		var invoicePostPersonName=$("#invoicePostPersonNameS").val();
		var invoicePostMobile=$("#invoicePostMobileS").val();
		$.ajax({
			type:"POST",
			url: baseURL+"/managesetting/managesettingAction!updateOrgInfoExt.action",
			data:{
				"orgInfoExt.corpName":corpName,
				"orgInfoExt.corpLicensePic":corpLicensePic,
				"orgInfoExt.corpTaxId":corpTaxId,
				"orgInfoExt.corpRegAddr":corpRegAddr,
				"orgInfoExt.corpRegTel":corpRegTel,
				"orgInfoExt.corpBankName":corpBankName,
				"orgInfoExt.corpBankNo":corpBankNo,
				"orgInfoExt.workAddr":workAddr,
				"orgInfoExt.personName":personName,
				"orgInfoExt.mobile":mobile,
				"orgInfoExt.invoiceType":invoiceType,
				"orgInfoExt.postProvince":invoicePostProvince,
				"orgInfoExt.postCity":invoicePostCity,
				"orgInfoExt.postDistruct":invoicePostDistruct,
				"orgInfoExt.postAddr":invoicePostAddr,
				"orgInfoExt.postPersonName":invoicePostPersonName,
				"orgInfoExt.postMobile":invoicePostMobile
			},
			//async: false, //false同步，true异步
			dataType: "json",
			success: function(result){
				$("#overlayDiv",window.top.document).hide();
				$('#msgshow_bg',window.top.document).hide();
				//设置原来的值
				setInvoiceInfo(result.data.tbDqdpOrganizationInfoExt);
				$("#orgInfoExtDiv").hide();
			},
			error:function() {
				_alert("提示信息","网络出错，请重试");
			}
		});
	}

	wxqyh_uploadfile.unbind();
	wxqyh_uploadfile.init();
	//上传图片
	function wxqyh_uploadImage(fileElementId,ulobj){
		showLoading("正在上传.....");
		$.ajaxFileUpload({
			url:baseURL+'/fileupload/fileUploadMgrAction!doUploadImageJudge.action',//需要链接到服务器地址
			data:{
				'agent':""
			},
			secureuri:false,
			fileElementId:fileElementId,                        //文件选择框的id属性
			dataType: 'json',                                   //服务器返回的格式，可以是json
			success: function (data) {
				if('0'==data.code){
					var $customAgentMsgBox=$("#orgInfoExtDiv",window.top.document);
					$customAgentMsgBox.find("#corpLicensePicS").val(data.data.imgurl);
					$customAgentMsgBox.find("#msgPic_div").show();
					$customAgentMsgBox.find("#msgPic_div").find("img").attr("src",compressURL+data.data.imgurl);
					var obj = $(window.top.document);
					obj.find('#upFileId_overlay').hide();
				}else{
					var obj = $(window.top.document);
					obj.find('#upFileId_overlay').hide();
					_alert("错误提示", data.desc);
				}
				//上传完成后都需要重新绑定一下事件
				wxqyh_uploadfile.unbind();
				wxqyh_uploadfile.init();
			},
			error: function () {
				var obj = $(window.top.document);
				obj.find('#upFileId_overlay').hide();
				_alert("错误提示",  "网络连接失败，请检查网络连接");
				//上传完成后都需要重新绑定一下事件
				wxqyh_uploadfile.unbind();
				wxqyh_uploadfile.init();
			}
		});
	}
</script>