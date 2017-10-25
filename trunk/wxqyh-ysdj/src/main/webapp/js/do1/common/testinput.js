$(document).ready(function(){
	$("#testinput").searchinput({text : " 这是测试默认文本"});
	
	$("#testinput1").showinput({inputwidth : $("#testinput1").width(),top　: 20,left : 140,datalist : ["测试文本1","测试文本2","测试文本3","测试文本4","测试文本5"]});
});