
function showLeft(){
	if(document.getElementById("left").style.display=='none'){
		document.getElementById("left").style.display='';
		document.getElementById("showhide_btn").style.backgroundImage='url(../../../themes/default/images/arrown.gif)';

	} 
	else{
		document.getElementById("left").style.display='none';
		document.getElementById("showhide_btn").style.backgroundImage='url(../../../themes/default/images/arrowb.gif)';
	}
	
}


function dyniframesize(down) { 
	var pTar = null; 
	var app=navigator.appName;
	if (document.getElementById){ 
		pTar = document.getElementById(down); 
		} 
		else{ 
		eval('pTar = ' + down + ';'); 
		} 
		if (pTar && !window.opera){ 
			if (app.indexOf('Microsoft') == -1){//火狐的
					pTar.height = pTar.contentDocument.body.offsetHeight+20 
					
			}else{
					pTar.style.height = pTar.Document.body.scrollHeight+20; 
			}
		} 
} 

