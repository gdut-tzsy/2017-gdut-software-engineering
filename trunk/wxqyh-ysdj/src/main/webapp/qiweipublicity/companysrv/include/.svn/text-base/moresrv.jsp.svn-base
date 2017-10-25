<%@page language="java" contentType="text/html; charset=UTF-8"%>
<!-- 更多企微增值服务 -->
<div class="introduceItemMore">
	<h3>更多企微增值服务</h3>
	<ul class="add-services clearfix">
		<li class="vipIndexIndexClass">
			<i class="add-services-ico9"></i>
			<p class="">银卡VIP</p>
		</li>
		<li class="goldVipIndexIndexClass">
			<i class="add-services-ico10"></i>
			<p class="">金卡VIP</p>
		</li>
		<li class="" id="technology_div">
			<i class="add-services-ico7"></i>
			<p class="">一对一技术服务</p>
		</li>
		<li class="" id="kq_machine_div">
			<i class="add-services-ico4"></i>
			<p class="">企微云考勤机</p>
		</li>
		<li class="" id="hypb_div">
			<i class="add-services-ico11"></i>
			<p class="">企微会议平板</p>
		</li>
		<li class="" id="space_div">
			<i class="add-services-ico5"></i>
			<p class="">企微云空间</p>
		</li>
		<li class="" id="volume_flow_div">
			<i class="add-services-icoVolumeFlow"></i>
			<p class="">音视频容量与流量</p>
		</li>
<%--		<li class="" id="kq_wheel_div">
			<i class="add-services-ico3"></i>
			<p class="">微信智能考勤轮</p>
		</li>--%>
		<li class="didiIndexClass">
			<i class="add-services-ico1"></i>
			<p class="">企业用车</p>
		</li>
		<%--<li class="" id="insurance_div">--%>
			<%--<i class="add-services-ico2"></i>--%>
			<%--<p class="">员工商业保险</p>--%>
		<%--</li>--%>
		<li class="" id="dbhistory_div" style="display:none;">
			<i class="add-services-ico_dbhistory"></i>
			<p class="">历史数据存储服务</p>
		</li>


<%--		<li class="" id="training_div" style="display:none;">
			<i class="add-services-ico6"></i>
			<p class="">上门实战培训</p>
		</li>
		<li class="" id="custom_div" style="display:none;">
			<i class="add-services-ico8"></i>
			<p class="" style="margin-top:15px;">深度定制<br>私有化部署</p>
		</li>--%>

	</ul>
</div>
<script language="javascript" type="text/javascript">
	$(function(){
		var myURL=document.location.href;
		if(myURL.indexOf("technology_index.jsp")>=0){
			$("#technology_div").hide();
		}else if(myURL.indexOf("vip_index.jsp")>=0){
			$(".vipIndexIndexClass").remove();
		}else if(myURL.indexOf("vip_gold_index.jsp")>=0 || myURL.indexOf("vip_single_buy.jsp")>=0){
			$(".goldVipIndexIndexClass").hide();
		}else if(myURL.indexOf("space_index.jsp")>=0){
			$("#space_div").hide();
		}else if(myURL.indexOf("kq_wheel_index.jsp")>=0){
			$("#kq_wheel_div").hide();
		}else if(myURL.indexOf("kq_machine_index.jsp")>=0){
			$("#kq_machine_div").hide();
		}else if(myURL.indexOf("dbhistory")>=0){
			$("#dbhistory_div").hide();
		}else if(myURL.indexOf("conference_note_book.jsp")>=0){
			$("#hypb_div").hide();
		}else if(myURL.indexOf("volume_flow_buy.jsp")>=0){
			$("#volume_flow_div").hide();
		}
	});
</script>