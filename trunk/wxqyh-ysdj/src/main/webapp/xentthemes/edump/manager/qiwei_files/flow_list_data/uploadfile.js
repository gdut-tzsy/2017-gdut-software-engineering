/**
 * 上传媒体文件
 * 20150208
 * sunqinghai
 */
  //上传图片完成后的通知
    function wxqyh_showfile(mediaInfos,ulobj,name){
    	if(name==undefined || name ==""){
    		name = "mediaIds";
    	}
    	var _test="";
    	var temp;
    	for(var i=0;i<mediaInfos.length;i++){
    		temp = mediaInfos[0];
    		_test+="<li title=\""+temp.fileName+"\"><input type=\"hidden\" name=\""+name+"\" value=\""+temp.id+"\" />"+
        		"<span onclick=\"doDownloadFile('"+temp.url+"','"+temp.fileName+"');\">"+temp.fileName+"</span><i onclick=\"doDelFileLi(this);\"></i></li>";
    	}
    	ulobj.append(_test);
       	_resetFrameHeight();
    }
    /**
     * 删除li
     * @param $this  点击的a标签的对象
     */
    function doDelFileLi($this){
    	var mediaId = $($($this).siblings("input")[0]).val();
    	if(mediaId){
        	$.ajax({
        		url:baseURL+"/fileupload/fileUploadMgrAction!doDelFile.action?mediaId="+mediaId+'&groupId='+wxqyh_uploadfile.groupId,
        		type:"GET",
        		dataType:"json",
        		success:function(result){
                    if('0'!=result.code){
                    	_alert("错误提示", result.desc);
                    }
                    else{
                    	$($this).parent().remove();
                    }
        		},
                error: function () { _alert("错误提示",  "网络连接失败，请检查网络连接");}
        	});
    	}
    	else{
        	$($this).parent().remove();
    	}
    }
    /**
     * 显示媒体文件
     * @param mediaInfos  文件的list
     */
    function previewFiles(mediaInfos,ulId,name){
    	if(!mediaInfos || mediaInfos.length==0){
    		return;
    	}
    	if(ulId==undefined || ulId ==""){
    		ulId = "medialist";
    	}
    	if(name==undefined || name ==""){
    		name = "mediaIds";
    	}
    	var temp;
    	var _test="";
    	for(var i=0;i<mediaInfos.length;i++){
    		temp = mediaInfos[i];
    		_test+="<li title=\""+temp.fileName+"\"><input type=\"hidden\" name=\""+name+"\" value=\""+temp.id+"\" />"+
        		"<span onclick=\"doDownloadFile('"+temp.url+"','"+temp.fileName+"');\">"+temp.fileName+"</span><i onclick=\"doDelFileLi(this);\"></i></li>";
    	}
       	$("#"+ulId).prepend(_test);
       	_resetFrameHeight();
    }

    /**
     * 上传媒体文件
     * @param fileElementId
     * @param mediaName
     * @param ulobj
     */
    function wxqyh_uploadFile(fileElementId,mediaName,ulobj){
    	showLoading("正在上传.....");
        $.ajaxFileUpload({
            url:baseURL+'/fileupload/fileUploadMgrAction!doUploadFile.action',//需要链接到服务器地址
            secureuri:false,
            fileElementId:fileElementId,                        //文件选择框的id属性
            dataType: 'json',                                              //服务器返回的格式，可以是json
            success: function (data) {
                if('0'==data.code){
                	//显示上传的文件
                	wxqyh_showfile([data.data.mediaInfo],ulobj,mediaName);
                	hideLoading();
                }else{
                	hideLoading();
                	_alert("错误提示", data.desc);
                }
            	
            	//上传完成后都需要重新绑定一下事件
            	wxqyh_uploadfile.unbind();
            	wxqyh_uploadfile.init();
            },
            error: function () {
            	hideLoading();
            	_alert("错误提示",  "网络连接失败，请检查网络连接");
            	//上传完成后都需要重新绑定一下事件
            	wxqyh_uploadfile.unbind();
            	wxqyh_uploadfile.init();
        	}
        });
    }
    /**
     * 下载媒体文件
     * @param url 文件路径
     * @param fileName
     * @param ulobj
     */
    function doDownloadFile(url,fileName){
    	//处理本地服务器和阿里云文件服务器的切换
		if(downFileURL.indexOf("fileUploadOssAction!downOssFile")=='-1'){
			localport=localport.replace(/Struts2\/fileUploadOssAction\!downOssFile.action\?filePath=/g,"");
			window.location.href=localport+downFileURL+encodeURIComponent(url)+"&fileFileName="+encodeURIComponent(fileName);
		}else{
			window.location.href=localport+downFileURL+encodeURIComponent(url)+"&fileFileName="+encodeURIComponent(fileName);
		}
    }
    
    var wxqyh_uploadfile = 
    {
        agent: "",
        groupId: "",
		init: function() {
            $('.uploadFileInput').bind('change', function() {
               	//显示上传中的层
        		//showLoading('图片上传中...');
            	var ulobj = $(this);
            	var file = ulobj.get(0).files[0];
                if (file) {
                	var path = ulobj.get(0).value;
                	var fileExt = path.substr(path.lastIndexOf(".")).toLowerCase();//获得文件后缀名
                	if(fileExt=="" || ".txt.xml.pdf.doc.ppt.xls.docx.pptx.xlsx.mp3.wma.amr.mp4.rar.zip.".indexOf(fileExt+".")<0){
                    	_alert("错误提示",  "只能上传txt,xml,pdf,doc,ppt,xls,docx,pptx,xlsx,mp3,wma,amr,mp4,rar,zip；如需上传其它格式文件请先将其压缩后再上传");
                    	return;
                	}
                    if (file.size > 10480000){//10485760
                    	_alert("错误提示",  "文件大小不能超过10M，请重新选择");
                    	return;
                    }
                }
                else{
                	_alert("错误提示","文件为空，请重新选择");
                	return;
                }
            	var mediaName = ulobj.attr("fileName");
            	var fileElementId = ulobj.attr("id");
            	ulobj = ulobj.prev();
            	wxqyh_uploadFile(fileElementId,mediaName,ulobj);
            });
        },
        unbind: function() {
	        $('.uploadFileInput').unbind('change');
	    }
    };
    /**
     * 判断是否校友会或者企业微信用户
     * @param obj
     */
    function judgeClient(obj){
    	var ua = navigator.userAgent.toLowerCase();
    	var url = window.location.href;
        if((ua.match(/MicroMessenger/i)=="micromessenger" && url.indexOf("wxqyh") >= 0)) {
        	return false;
        }
    	$("#aLink").click();
    	return true;
	}
		