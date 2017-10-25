var var_57a3d3ae613b4778a90b4fe725e883ba;
var pageSize = 10;//每页显示个数

        var refreshEvent = {ok: function () {
            func_57a3d3ae613b4778a90b4fe725e883ba(var_57a3d3ae613b4778a90b4fe725e883ba.currPage);
              _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2');loadPageList();
        }
        };

       
        function func_57a3d3ae613b4778a90b4fe725e883ba(pageIndex) {
        	pageSize = $("#pageSize").val();
            if(!pageSize || pageSize == "" ){
             pageSize = 10;
            } else{
            	if(!(/^([0-9]*[1-9][0-9]*)$/).test(pageSize)){
            		_alert("信息提示层", "每页显示几条只能输入正整数");
            		return;
            	}
	    		if(parseInt(pageSize)>maxPageSize){
	    			 _alert("错误提醒", "每页显示的条数不能超过"+maxPageSize+"!");
	        		 return;
	    		}
	    	} 
        	var typeId=$("#taskType").find("option:selected").val();
	        $('#57a3d3ae613b4778a90b4fe725e883ba').ajaxSubmit({
	            dataType: 'json',
	            data: {page: pageIndex,pageSize:pageSize, 'dqdp_csrf_token': dqdp_csrf_token,'typeId':typeId},
	            async: false,
	            success: function (result) {
	                if ("0" == result.code) {
	                    var_57a3d3ae613b4778a90b4fe725e883ba = result.data;
	                } else {
	                	_alert("信息提示层", "网络异常");
	                    /*$("#tip").error({title: "信息提示层", content: result.desc, button: [
	                        {text: "确定", events: "test"},
	                        {text: "取消", events: "test"}
	                    ]});*/
	                }
	            },
	            error: function () {
	                _alert("信息提示层", "网络异常");
	            }
	        });
        }
        
        function onChangeSize(){
            func_57a3d3ae613b4778a90b4fe725e883ba(1);
            _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  
            _redraw('af612ff2d20641c6b2a6d920f1bd8fc2');
            loadPageList();
           }
        
        function doDetail(id){
	    	var top=(window.screen.height-560)/2;
	        var left=(window.screen.width-350)/2;
	        window.open(baseURL+'/manager/express/detail.jsp?id='+id, 'newwindow', 'left='+left+',top='+top+',height=580, width=350, toolbar=no, menubar=no, resizable=yes,location=no, status=no,scrollbars=no,directories=no,menubar=no');
	    }
        function keyDown13(){
        	var event = arguments.callee.caller.arguments[0] || window.event;
        	if(event.keyCode == 13){//判断是否按了回车，enter的keycode代码是13，想看其他代码请猛戳这里。
        		func_57a3d3ae613b4778a90b4fe725e883ba(1); 
        		_redraw('cc94a9ec68ff4d93a7fd5934c581b647'); 
        		_redraw('af612ff2d20641c6b2a6d920f1bd8fc2');
        		loadPageList();
        	}
        }

        function loadPageList(){
            $('#pageList').html('');
            var maxPage = var_57a3d3ae613b4778a90b4fe725e883ba.maxPage;
            var currPage = var_57a3d3ae613b4778a90b4fe725e883ba.currPage;
            var totalRows = var_57a3d3ae613b4778a90b4fe725e883ba.totalRows;
            $('#pageList').append("");
            //没有记录
            if(maxPage <= 1 ){
            	$('#pageList').append("<li class='shownum'>每页显示<input type='text' id='pageSize' onchange='onChangeSize();' onkeydown='keyDown13();' onkeyup=\"value=value.replace(/[^\\d]/g,'')\"  value='"+pageSize+"'>条</li>");
            	$('#pageList').append("<li><a>总计"+totalRows+"条</a></li>");
                $('#pageList').append("<li class=\"disabled pager-prev\"><a  href=\"#\">上一页</a></li><li class=\"disabled pager-next\"><a href=\"#\">下一页</a></li>");
            }else{
            	$('#pageList').append("<li class='shownum'>每页显示<input type='text' id='pageSize' onchange='onChangeSize();' onkeydown='keyDown13();' onkeyup=\"value=value.replace(/[^\\d]/g,'')\"  value='"+pageSize+"'>条</li>");
            	$('#pageList').append("<li><a>总计"+totalRows+"条</a></li>");
                //有记录
                if(currPage == 1){
                    $('#pageList').append("<li class=\"disabled pager-prev\"><a href=\"#\">上一页</a></li>");
                }else{
                    $('#pageList').append("<li class=\"pager-prev\"><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba("+currPage+"-1);   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList();\">上一页</a></li>");
                }

                if(maxPage < 11){
                    for (var i=1; i<= maxPage; i++) {
                        if(currPage == i){
                            $('#pageList').append("<li class=\"active\"><span>"+i+"</span></li>");
                        }else{
                            $('#pageList').append("<li><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba("+i+");   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList(); \">"+i+"</a></li>");
                        }
                    }
                }else{
                    if(currPage <= 5){
                        for (var i=1; i<= 8; i++) {
                            if(currPage == i){
                                $('#pageList').append("<li class=\"active\"><span>"+i+"</span></li>");
                            }else{
                                $('#pageList').append("<li><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba("+i+");   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList(); \">"+i+"</a></li>");
                            }
                        }
                        $('#pageList').append("<li><span>...</span></li>");
                        $('#pageList').append("<li><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba("+maxPage+");   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList(); \">"+maxPage+"</a></li>");
                    }else if(maxPage - currPage <= 4 ){
                        $('#pageList').append("<li><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba(1);   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList(); \">1</a></li>");
                        $('#pageList').append("<li><span>...</span></li>");
                        for (var i =  currPage - 3; i<= maxPage; i++) {
                            if(currPage == i){
                                $('#pageList').append("<li class=\"active\"><span>"+i+"</span></li>");
                            }else{
                                $('#pageList').append("<li><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba("+i+");   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList(); \">"+i+"</a></li>");
                            }
                        }
                    }else{
                        $('#pageList').append("<li><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba(1);   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList(); \">1</a></li>");
                        $('#pageList').append("<li><span>...</span></li>");
                        for (var i = currPage -3; i<= currPage+3; i++) {
                            if(currPage == i){
                                $('#pageList').append("<li class=\"active\"><span>"+i+"</span></li>");
                            }else{
                                $('#pageList').append("<li><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba("+i+");   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList(); \">"+i+"</a></li>");
                            }
                        }
                        $('#pageList').append("<li><span>...</span></li>");
                        $('#pageList').append("<li><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba("+maxPage+");   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList(); \">"+maxPage+"</a></li>");
                    }

                }

                if(currPage == maxPage){
                    $('#pageList').append("<li class=\"disabled pager-next\"><a href=\"#\">下一页</a></li>");
                }else{
                    $('#pageList').append("<li class=\"pager-next\"><a href=\"javascript:func_57a3d3ae613b4778a90b4fe725e883ba("+currPage+"+1);   _redraw('cc94a9ec68ff4d93a7fd5934c581b647');  _redraw('af612ff2d20641c6b2a6d920f1bd8fc2'); loadPageList(); \">下一页</a></li>");
                }

            }
            $('.lasttd').on({
				mouseover:function(){
					if(navigator.userAgent.toUpperCase().indexOf("MSIE")>-1){				
						$(this).find('.lasttdpop').css({'display':'block'});$(this).children('.contact_action').addClass('after');					
					}else if(navigator.userAgent.toUpperCase().indexOf("SAFARI")>-1){
						$(this).find('.lasttdpop').css({'display':'-webkit-flex'});$(this).children('.contact_action').addClass('after');
					}
					else{
						$(this).find('.lasttdpop').css({'display':'flex'});$(this).children('.contact_action').addClass('after');}},
				mouseout:function(){$(this).find('.lasttdpop').hide();$(this).children('.contact_action').attr('class','contact_action');}
			});  
            _resetFrameHeight();
        }
        
        function doExpert(){
        	//必须选择一个时间段
        	var startTime=$("#f_startTimes").val();
        	var endTime=$("#f_endTime").val();
        	if(startTime==""||endTime==""){
        		_alert("导出提示", "请在【高级搜索】设置导出数据的创建时间段");
        	}else{
        		if(exportDaysLimit(startTime,endTime)==false){
        			_alert("导出提示", exportDaysDesc);
        		}else{
        			if(var_57a3d3ae613b4778a90b4fe725e883ba.totalRows>0){
        				showLoading("正在导出.....");
        				createReport('express');
        				hideLoading();
        	 		}else{
        	 			_alert("提示","搜索结果为空，不支持导出");
        	 		}
        		}
        	}
        }
        
        function createReport(reportType){
        	$("#57a3d3ae613b4778a90b4fe725e883ba").ajaxSubmit({  
                    type: 'post',
                    data: {'reportType':reportType},
                    dataType: 'json',
                    url: baseURL+"/report/reportAction!checkReport.action" ,  
                    success: function(data){
                    	if(data.code == '0'){
                    		if(data.data.useReport){
                    			if(data.data.fileName || data.data.fileName == "undefined"){
                    				_confirm("提示", "已找到相同查询条件的报表：【"+data.data.fileName+"】是否重新导出？", null, {ok:function(){
                    					exportReport('express');
                    				}});
                    			}else{
                    				exportReport('express');
                    			}
                    			
                    		}else{
                    			document.getElementById("57a3d3ae613b4778a90b4fe725e883ba").action = baseURL+"/express/expressAction!exportExpress.action";
					        	document.getElementById("57a3d3ae613b4778a90b4fe725e883ba").submit();
					        	document.getElementById("57a3d3ae613b4778a90b4fe725e883ba").action = baseURL+"/express/expressAction!ajaxPageSearch.action";
				        	}
                    	}
                    },
                    error: function(XmlHttpRequest, textStatus, errorThrown){  
                   		document.getElementById("57a3d3ae613b4778a90b4fe725e883ba").action = baseURL+"/express/expressAction!exportExpress.action";
			        	document.getElementById("57a3d3ae613b4778a90b4fe725e883ba").submit();
			        	document.getElementById("57a3d3ae613b4778a90b4fe725e883ba").action = baseURL+"/express/expressAction!ajaxPageSearch.action";
		        	}  
                }); 
        }
        function exportReport(reportType){
			//提交导出
			$("#57a3d3ae613b4778a90b4fe725e883ba").ajaxSubmit({ 
				type: 'post',
				data: {'reportType' : reportType},
				dataType: 'json',
				url: baseURL+"/report/reportAction!createReportTask.action" ,  
				success: function(data){
					if(data.code == '0'){
						_alert("提示", "操作成功，请稍后到<a href='"+baseURL+"/manager/report/reportTask_main.jsp'>导出报表管理(点击跳转)</a>处下载对应的导出文件："+data.data.fileName);
					}else{
						_alert("提示", "导出失败，请联系管理员...");
					}
		      	}
			});
		}
        
        

        $(document).ready(function () {
        	loadPageList();
        });
        
        function checkdate(){
    		func_57a3d3ae613b4778a90b4fe725e883ba(1);
    		_redraw('cc94a9ec68ff4d93a7fd5934c581b647'); 
    		_redraw('af612ff2d20641c6b2a6d920f1bd8fc2');
    		loadPageList();
    	} 
        
        
        