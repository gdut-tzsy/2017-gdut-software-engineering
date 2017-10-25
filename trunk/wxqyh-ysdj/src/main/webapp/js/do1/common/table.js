window.onload = function(){
 var oTable = document.getElementById("oTable");
 for(var i=0;i<oTable.rows.length;i++){
	 oTable.rows[i].className = "trBg1";
  if(i%2==0){
	  oTable.rows[i].className = "trBg2";
	  oTable.rows[i].onmouseover=function(){this.className="overcolor"};
	  oTable.rows[i].onmouseout=function(){this.className="trBg2"};
	  } //偶数行时
	  else{	  
	  oTable.rows[i].onmouseover=function(){this.className="overcolor"};
	  oTable.rows[i].onmouseout=function(){this.className="trBg1"};}
 }
}//表格隔行换色
