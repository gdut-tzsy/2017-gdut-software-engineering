var _browser=navigator.appName,_hrefLink="javascript:";try{jquery_ui_loded||_loadJqueryUI()}catch(e$$10){_loadJqueryUI()}function _loadJqueryUI(){writeCss(baseURL+"/js/3rd-plug/jquery-ui-1.8/css/smoothness/jquery-ui-1.8.custom.css");writeJs(baseURL+"/js/3rd-plug/jquery-ui-1.8/js/jquery-ui-1.8.custom.min.js")}
function writeJs(a){var b=document,d=document.getElementsByTagName("head")[0],c=b.createElement("script");c.setAttribute("type","text/javascript");c.setAttribute("src",a);c.setAttribute("async",!0);c.onerror=function(){d.removeChild(c)};d.appendChild(c)}
function writeCss(a){var b=document,d=document.getElementsByTagName("head")[0],c=b.createElement("link");c.setAttribute("type","text/css");c.setAttribute("href",a);c.setAttribute("rel","stylesheet");c.setAttribute("async",!0);c.onerror=function(){d.removeChild(c)};d.appendChild(c)}try{var _browser_Version=navigator.appVersion.split(";")[1].replace(/[ ]/g,""),_hrefLink="MSIE6.0"==_browser_Version?"#":"javascript:"}catch(e$$13){_hrefLink="javascript:"}
function _rollbackFrameHeight(){var a=this.frameElement;a&&(a.height=500,_heightSeted=!1)}var _heightSeted=!1;
//"http://api.map.baidu.com/api?v=2.0&ak=N8tEUoq1FsPdMmHFattStreQ"
var _baiduMapUrl = "http://api.map.baidu.com/getscript?v=2.0&ak=N8tEUoq1FsPdMmHFattStreQ&services=&t=20150707171040";
function _resetFrameHeight(){
	var a=this.frameElement;
    a&&(a.height=a.contentDocument?a.contentDocument.documentElement.scrollHeight:
      "MSIE9.0"==_browser_Version?a.document.documentElement.scrollHeight:a.contentWindow.document.body.scrollHeight+0,_heightSeted=!0,a.setAttribute('scrolling','no'))  		 
       if(window!=window.top)
	   	window.parent._resetFrameHeight();
        }
window.document.body.addEventListener("onresize",_resetFrameHeight);

function _doEmpty(){}var _pageInitSignl=0,eventAfterDraw=null,eventBeforDraw=null;
function Dqdp(){this.permissions=null;Dqdp.prototype.submitCheck=function(a){var a=$("#"+a+" [valid]"),b=!0,d=null;$.each(a,function(a,e){var f="",g=$(e).attr("valid"),h=$(e).val(),g=eval("("+g+")");g.must&&(_isNullValue(h)||_isNull(h))?(b=_appendErrorTip(e,g.tip+"不能为空"),f=g.tip+"不能为空"):_removeErrorTip(e);if(h){g.length&&_strlength(h)>g.length&&(f="只能输入"+g.length+"个英文字符或一半长度的汉字");switch(g.fieldType){case "datetime":f=checkDate(h,"yyyy-MM-dd HH:mm:ss");break;case "date":f=checkDate(h,"yyyy-MM-dd");
break;case "certId":f=checkCertID(h);break;case "pattern":RegExp(g.regex,"g").test(h)||(f="格式不正确");break;case "func":eval(g.regex)||(f="格式不正确")}""==f?_removeErrorTip(e):_appendErrorTip(e,g.tip+f);""!=f&&(b=!1,d||(d=e))}});d&&d.focus();_resetFrameHeight();return b};Dqdp.prototype.initPage=function(){eventBeforDraw&&eventBeforDraw.call(eventBeforDraw,null);_initCMD();_resetFrameHeight();eventAfterDraw&&eventAfterDraw.call(eventAfterDraw,null)}}
function _redraw(a,b){eventBeforDraw&&eventBeforDraw.call(eventBeforDraw,null);0<arguments.length?(1==arguments.length?(_initElementValue(a,!1),_buildEditor(a,!1)):(_initElementValue(a,b),_buildEditor(a,b)),_showOn(),_filtePermission(a),_rebuildSelect(a),_rebuildDictInputByAttr("checkbox",a),_rebuildDictInputByAttr("radio",a),_bindDictParent(a),_bindEvent(),_undisabled(a)):_initCMD();eventAfterDraw&&eventAfterDraw.call(eventAfterDraw,null)}
function _initCMD(){_initElementValue();_showOn();_filtePermission();_rebuildSelect();_rebuildDictInputByAttr("checkbox");_rebuildDictInputByAttr("radio");_bindDictParent();_buildEditor();_bindEvent();_undisabled()}function _regEventBeforDraw(a){eventBeforDraw=a}function _regEventAfterDraw(a){eventAfterDraw=a}function _showOn(){var a=$("[showOn]");$.each(a,function(a,d){var c=$(d);try{eval(c.attr("showOn"))?c.show():c.hide()}catch(e){c.hide()}})}
function _bindEvent(){var a=$("[keySearch]");$.each(a,function(){this.onkeyup=function(){13==(event?event:window.event?window.event:null).keyCode&&eval($(this).attr("keySearch"))}})}function _appendErrorTip(a,b){null==$(".error",$(a).parent()).html()&&$(a).parent().append("<p class='error'><font color='red'>("+b+")</font></p>");return!1}function _removeErrorTip(a){null!=$(".error",$(a).parent()).html()&&$(".error",$(a).parent()).remove();return!1}
function _getDictDesc(a,b,d){var c=_dqdp_dict[a];if(b)for(var e=b.split("."),f=0;f<e.length;f++)for(var g in c)for(var h in c[g])h==e[f]&&(c=c[g][h].child);if(3==arguments.length)for(g in c)for(h in c[g])if(h==d)return c[g][h].desc;return c}
function _undisabled(a){var b=$(1==arguments.length?"#"+a+"  [_disabled]":"[_disabled]");$.each(b,function(a,c){$(c).attr("disabled","true"==$(c).attr("_disabled")?!0:!1)});b=$(1==arguments.length?"#"+a+"  [_readonly]":"[_readonly]");$.each(b,function(a,c){$(c).attr("readonly","true"==$(c).attr("_readonly")?!0:!1)})}
function _bindDictParent(a){var b=$(1==arguments.length?"#"+a+"  [parentDict]":"[parentDict]");$.each(b,function(a,c){var b=$(this),f=$("#"+b.attr("parentDict"));$(f).live("change",function(){for(var a="",d="",f=$(b);void 0!=f.attr("parentDict")&&null!=f.attr("parentDict")&&""!=f.attr("parentDict");){f=$("#"+f.attr("parentDict"));a=f.val()+(0<a.length?".":"")+a;if(""==a)break;if(void 0==f.attr("parentDict")||""==f.attr("parentDict")||null==f.attr("parentDict"))var d=f.attr("dictType"),j=f.attr("dictItem"),
a=j+(""==j?"":".")+a}!_isNullValue(a)&&""!=a?(f=$(this).attr("param"),_isNullValue(f)?c.setDictData(_getDictDesc(d,a)):(a=_doGetDataSrc(baseURL+"/dictmgr/dictmgr!queryDictByParent.action","searchValue['_value']="+a+"&searchValue['_dictType']="+d+"&"+f),null!=a&&c.setDictData(a[d])),d=b.attr("defaultValue"),!_isNullValue(d)&&""!=d&&b.attr("value",d)):c.setDictData(null)});f.change()})}
function _rebuildSelect(a){var b=$(1==arguments.length?"#"+a+"  select":"select");$.each(b,function(a,c){var b=$(c),f=b.attr("dictType"),g=b.attr("dictItem"),h=b.attr("param"),i=b.attr("defaultValue");$.extend(c,{setDictData:function(a){_cleanSelect(b);b.html("");if(null!=a){var c=b.attr("defaultTip"),c=_isNullValue(c)?"<option value=''>请选择</option>":""==c?"":"<option value=''>"+c+"</option>";b.append(c);for(var d in a)for(var f in a[d])b.append("<option value='"+f+"'"+(f==i?"selected":"")+">"+a[d][f].desc+
"</option>")}}});_isNullValue(f)||(_isNullValue(h)?this.setDictData(_getDictDesc(f,g)):(g=_doGetDataSrc(baseURL+"/dictmgr/dictmgr!queryDict.action","searchValue['_dictType']="+f+"&"+h),null!=g&&this.setDictData(g[f])))})}function _isNullValue(a){return void 0==a||null==a}function _cleanSelect(a){$.each($("[parentDict="+a.attr("id")+"]"),function(a,d){var c=$(d);_cleanSelect(c);try{d.setDictData(null)}catch(e){}})}
function _filtePermission(a){var b=$(1==arguments.length?"#"+a+"  [permission]":"[permission]");$.each(b,function(a,c){var b=$(c).attr("permission");""==b||_dqdp_permissions[b]||("true"==$(c).attr("readonlyWhenNoPermission")?$(c).attr("readonly","readonly"):$(c).remove())})}
function _rebuildDictInputByAttr(a,b){var d=$(1!=arguments.length?"#"+b+" :"+a:":"+a);$.each(d,function(){var b=$(this),d=b.attr("dictType"),f=b.attr("dictItem"),g=b.attr("defaultValue"),h=b.attr("name"),i=b.attr("param");if(!d)return!0;g&&""!=g&&(g=g.split(","));this.setDictData=function(d){if(null!=d){var e=$(b.parent()),f=b.attr("onclick");$("[name='"+h+"']").remove();for(var i in d){for(var m in d[i])var n="";for(var o in g)g[o]==m&&(n="checked='checked'");e.append("<input style='padding-right: 3px' type=\""+
a+'" '+(null==f?"":'onclick="'+f+'"')+'name="'+h+"\" value='"+m+"' "+n+">"+d[i][m].desc+"&nbsp;&nbsp;")}}};_isNullValue(i)?null!=d&&this.setDictData(_getDictDesc(d,f)):(f=_doGetDataSrc(baseURL+"/dictmgr/dictmgr!queryDict.action","searchValue['_dictType']="+d+"&"+i),null!=f&&this.setDictData(f[d]));null!=g&&b.val(g)})}var _eleTemplateStore=new HashMap;
function _resetElement(a){if(1>arguments.length)for(var b=_eleTemplateStore.keys(),d=0;d<b.length;d++)$("#"+b[d]).html(_eleTemplateStore.get(b[d]));else b=_eleTemplateStore.get(a),null!=b&&void 0!=b&&""!=b&&$("#"+a).html(b)}function _destoryPage(){_eleTemplateStore.clear()}$(window).unload(function(){_destoryPage()});var _editors=new HashMap;
function _buildEditor(a,b){var d=null,d=0<arguments.length?$("#"+a+(b?" ":"")+"[editor]"):$("[editor]");$.each(d,function(){var a=$(this),b=a.text(),d=eval("("+a.attr("editor")+")"),g=KindEditor.create("#"+a.attr("id"),d.param);g.html(b);d.readonly&&g.readonly();_editors.put(a.attr("id"),g)})}
function _initElementValue(a,b){var d=null,d=0<arguments.length?$("#"+a+(b?" ":"")+"[dataSrc]"):$("[dataSrc]");$.each(d,function(a){var b=$(this),d=b.attr("id");if(null==d||void 0==d)b.attr("id","_id_div_"+a),d=b.attr("id");_resetElement(d);_eleTemplateStore.put(d,b.html());var a=b.attr("dataSrc"),c=[];_isNull(a)||(-1<a.indexOf("local:")?(a=eval(a.substr(6,a.length)),c.push(a),_doEleValueDispathc(b,a,c),_resetFrameHeight()):$.ajax({type:"post",data:{dqdp_csrf_token:dqdp_csrf_token},url:a,dataType:"json",
async:!1,success:function(a){"0"==a.code?(a=a.data,c.push(a),_doEleValueDispathc(b,a,c),_resetFrameHeight()):_alert("错误提示",a.desc)},error:function(){_alert("错误提示","通讯故障")}}))});_showOn();try{_resetFrameHeight(document.documentElement.scrollHeight)}catch(c){}}var _varReg=/@(\{|\%7B)([^@]*?(@(\{|\%7B)[^@].*?(\}|\%7D)|.)*?)(\}|\%7D)/,_expReg=/^#(.*?)#/,_jSmartSupport=!1;
function _replaceVar(a,b,d){for(var c=a.html(),e;null!=(e=_varReg.exec(c));){var f;if(null!=(f=_expReg.exec(e[2]))){_jSmartSupport||($("<script><\/script>").attr({src:baseURL+"/js/3rd-plug/jsrender/jsrender.js",type:"text/javascript",id:"load"}).appendTo($("head").remove("#loadscript")),_jSmartSupport=!0);for(var g=f[1];null!=(f=_varReg.exec(g));)var h=_replaceHTMLVar(f[2],b,d),g=g.replace(f[0],h);$.templates("xxx","{{"+g+"}}");f=$.render.xxx(b)}else f=_replaceHTMLVar(e[2],b,d);c=c.replace(e[0],f)}c!=
a.html()&&a.html(c)}function _replaceHTMLVar(a,b,d){for(var c;null!=(c=_varReg.exec(a));)var e=_replaceHTMLVar(c[2],b,d),a=a.replace(c[0],e);var f,a=a.split(".");for(c=0;c<a.length;c++)if(f=""==a[c]?d[d.length-c-2]:0==c?b[a[c]]:f[a[c]],null==f||void 0==f)f="";return f}
function _doEleValueDispathc(a,b,d){$("[listSourceBackup=false]",a).remove();var c=$("[name]",a);$.each(c,function(){for(var a=$(this),c=a.attr("name"),g;null!=(g=_varReg.exec(c));){var h=_replaceHTMLVar(g[2],b,d),c=c.replace(g[0],h);alert(c)}var c=c.split("."),i;for(g=0;g<c.length;g++)if(i=""==c[g]?d[d.length-g-2]:0==g?b[c[g]]:i[c[g]],null==i||void 0==i)i="";if(null==i||void 0==i||"array"!=$.type(i))_doElementValueSet(this,i);else if(c=a.parent(),"true"==c.attr("listSourceBackup")&&"true"!=a.attr("listSourceBackup"))a.remove();
else if("SELECT"==a[0].tagName)_doElementValueSet(a[0],i);else if(void 0!=a.attr("merge")&&!_isNull(a.attr("merge")))_doElementValueSet(a[0],i.join(a.attr("merge")));else{h=a.attr("class");g=null;null!=h&&-1<h.indexOf("|")&&(g=h.split("|"));for(h=0;h<i.length;h++){void 0==i[h]._index&&(i[h]._index=""+(h+1));var j=a.clone();j.html(j.html().replace("\\#_rpa_\\#","@{"));j.attr("listSourceBackup","false");if(0<$("[name]",j).length||-1<j.html().indexOf("@{")){for(var l=[],k=0;k<d.length;k++)l[k]=d[k];
l.push(i[h]);_doEleValueDispathc(j,i[h],l)}else _doElementValueSet(j[0],i[h]);c.append(j);null!=g&&j.attr("class",g[h%g.length]);j.show()}a.remove()}});_replaceVar(a,b,d)}
function _doElementValueSet(a,b){var d=$(a);if("INPUT"==a.tagName||"TEXTAREA"==a.tagName||"SELECT"==a.tagName)if("TEXTAREA"==a.tagName&&d.attr("editor"))d.html(b);else{switch(d.attr("type")){case "radio":case "checkbox":d.val()==b?d.attr("checked",!0):d.removeAttr("checked");d.attr("defaultValue",b);break;default:if(null==b||void 0==b)b=d.attr("defaultValue");d.attr("value",b)}if("SELECT"==a.tagName)if("array"==$.type(b)){d.html("");for(var c=0;c<b.length;c++){var e=d.attr("keyForSelect"),e=b[c][e],
f=b[c][d.attr("descForSelect")];d.append("<option value='"+e+"'"+(e==d.attr("defaultValue")?"selected":"")+">"+f+"</option>")}}else""!=(d.attr("defaultTip")||!_isNullValue(b))&&d.attr("defaultValue",b)}else"IMG"==a.tagName?d.attr("src",d.attr("src")+b):(c=d.attr("charLength"),null==b||void 0==b?b=d.attr("defaultValue"):void 0!=c&&null!=c&&""!=c&&(d.attr("title",b),b=_getStrByLen(b,c)),d.text(b))}
function _alert(a,b,d){var c=$("#id_dialog"),e=null;2<arguments.length&&null!=d&&(e=d);c[0]||($(document.body).append("<div id=\"id_dialog\" style='width: 427px'><div>"),c=$("#id_dialog"));c.html("<table   border='0' cellspacing='0' cellpadding='0'><tr><td><table  border='0' align='center' cellpadding='0' cellspacing='0'><tr><td rowspan='2' valign='top' class='tipsImg'><img src='"+baseURL+"/themes/default/images/icon/iconTips.png' /></td><td valign='top' >"+b+"</td></tr><tr><td align='center' valign='middle'></td></tr></table></td></tr></table>").dialog({resizable:!1,
overflow:"hidden",position:"center",modal:!0,title:a,minHeight:180,minWidth:320,buttons:{"关闭":function(){$(this).dialog("close");e&&e.call(e,null,null)}}});_resetFrameHeight()}function _pageGo(a,b,d){a<b?_alert("错误提示","超出最大页数"):1>b?_alert("错误提示","超出最小页数"):eval(d+"("+b+")")}$(document).ready(function(){_rollbackFrameHeight();(new Dqdp).initPage();_resetFrameHeight();});
function _doCheck(a,b){$.each($("[name="+b+"]"),function(b,c){$(c).attr("disabled")||("checked"==$(a).attr("checked")?$(c).attr("checked","checked"):$(c).removeAttr("checked"))})}function _doGetChooseRow(a){var b="";$.each($("#"+a).find("[name=ids]"),function(a,c){"checked"==$(this).attr("checked")&&(0<b.length&&(b+=","),b+=$(c).val())});return b}
function _doEdit(){var a=_doGetChooseRow("ids");if(0==a.length)return _alert("提示","请选择要编辑的数据"),!1;if(-1<a.indexOf(","))return _alert("错误提示","请不要选择多条数据"),!1;document.location.href=$("#"+$divId).attr("editUrl")+"?dqdp_csrf_token="+dqdp_csrf_token+"&id="+a}function _doAdd(a){document.location.href=$("#"+a).attr("addUrl")+"?dqdp_csrf_token="+dqdp_csrf_token}
function _doDel(a,b,t){var d=_doGetChooseRow(a);if(0==d.length)return _alert("提示","请选择要删除的数据"),!1;confirm((3==arguments.length?t:"删除数据后将无法恢复，确认要删除吗？"))&&(1==arguments.length?(_doCommonPost($("#"+a).attr("delUrl"),"ids="+d),doSearch(1)):_doCommonPost($("#"+a).attr("delUrl"),"ids="+d,b))}function _doSignlDel(a,b,d,t){$("#"+a+" [name=ids][value="+b+"]").attr("checked","true");$("#"+a+" [name=ids][value!="+b+"]").removeAttr("checked");if(3==arguments.length)_doDel(a,d);else if(4==arguments.length)_doDel(a,d,t);else _doDel(a);}
function _doSignlEdit(a,b){$("#"+a+" [name=ids][value="+b+"]").attr("checked","true");$("#"+a+" [name=ids][value!="+b+"]").removeAttr("checked");var d=_doGetChooseRow(a);if(0==d.length)return _alert("提示","请选择要编辑的数据"),!1;document.location.href=$("#"+a).attr("editUrl")+"?dqdp_csrf_token="+dqdp_csrf_token+"&id="+d}
function _doSignlView(a,b){$("#"+a+" [name=ids][value="+b+"]").attr("checked","true");$("#"+a+" [name=ids][value!="+b+"]").removeAttr("checked");var d=_doGetChooseRow(a);if(0==d.length)return _alert("提示","请选择要查看的数据"),!1;document.location.href=$("#"+a).attr("viewUrl")+"?dqdp_csrf_token="+dqdp_csrf_token+"&id="+d}
function _doCommonSubmit(a,b,d){$(":button").attr("disabled",!0);for(var c=0;c<_editors.values().length;c++)_editors.values()[c].sync();c=new Dqdp;if(!b||null==b)b={};b.dqdp_csrf_token=dqdp_csrf_token;c.submitCheck(a)?$("#"+a).ajaxSubmit({dataType:"json",data:b,async:!1,forceSync:!0,success:function(a){$(":button").attr("disabled",!1);"0"==a.code?_alert("提交结果",a.desc,d.ok):_alert("提交结果",a.desc,d.fail)},error:function(){$(":button").attr("disabled",!1);_alert("错误提示","通讯故障",d.error)}}):$(":button").attr("disabled",
!1)}function _doGetDataSrc(a,b){var d=null;if(!b||null==b)b={};b.dqdp_csrf_token=dqdp_csrf_token;$.ajax({type:"post",url:a,data:b,dataType:"json",async:!1,success:function(a){"0"==a.code?d=a.data:_alert("错误提示",a.desc)},error:function(){_alert("错误提示","通讯故障")}});return d}
function _doCommonPost(a,b,d){if(!b||null==b)b={};b.dqdp_csrf_token=dqdp_csrf_token;$.ajax({type:"POST",url:a,data:b,async:!1,success:function(a){_alert("提交结果",a.desc);2<arguments.length&&("0"==a.code&&d&&d.ok&&d.ok.call(d.ok,null,null),"0"!=a.code&&d&&d.fail&&d.fail.call(d.fail,null,null))},error:function(){_alert("错误提示","通讯故障");d.fail.call(d.fail,null,null)},dataType:"json"})}
function ListTable(a){this.data=a.data;this.title=a.title;this.trStyle=a.trStyle;this.trevent=a.trevent;this.operations=a.operations;this.checkableColumn=a.checkableColumn;ListTable.prototype.setTitle=function(a){this.title=a};ListTable.prototype.setData=function(a){this.data=a};ListTable.prototype.createList=function(b){for(var d="<thead> <tr>",c=0;c<this.title.length;c++)d=""!=this.checkableColumn&&this.title[c].isCheckColunm?d+("<th width="+this.title[c].width+'><input type="checkbox" onclick="_doCheck(this,\'ids\')"></th>'):
this.title[c].isOperationColumn?d+("<th width="+this.title[c].width+"> 操作 </th>"):d+("<th width="+this.title[c].width+">"+this.title[c].showName+"</th>");var d=d+"</tr> </thead>",e="<tbody >";if(null==this.data||void 0==this.data||1>this.data.length)e+='<tr  class="'+this.trStyle[c%2]+'" ><td colspan="'+this.title.length+'">查询结果为空</td></tr>';else for(c=0;c<this.data.length;c++){void 0==this.data[c]._index&&(this.data[c]._index=""+(c+1));for(var f=this.data[c],e=e+('<tr  class="'+this.trStyle[c%2]+
'" '),e=e+">",g=0;g<this.title.length;g++)if(this.title[g].isCheckColunm)var h=this.title[g].checkAble,h=h?h.call(h,c,f)?"":"disabled='true'":"",e=e+('<td class="tc" width='+this.title[g].width+'><input type="checkbox" name="ids" '+h+' value="'+this.data[c][a.checkableColumn]+'"></td>');else if(this.title[g].isOperationColumn)e+='<td class="tdOpera  tc"  width='+this.title[g].width+">",$.each(this.operations,function(a,b){if((!b.permission||_dqdp_permissions[b.permission])&&(!b.condition||b.condition.call(b.condition,
c,f)))e+='<a href="'+_hrefLink+'">'+b.name+"</a>&nbsp;&nbsp;"}),e+="</td>";else{var h=this.title[g].name,i=this.data[c],j=this.title[g].emptyValue;_isNullValue(j)&&(j="&nbsp;");_isNullValue(i[h])?e+='<td class="tc" dataType="data">'+j+"</td>":(!this.title[g].noSafe&&"string"==typeof i[h]&&(i[h]=i[h].replace('"',"&quot;"),i[h]=i[h].replace("<","&lt;"),i[h]=i[h].replace(">","&gt;")),j=this.title[g].href&&(!this.title[g].linkPermission||_dqdp_permissions[this.title[g].linkPermission]),e+='<td class="tc" dataType="data">'+
(j?'<a type="link" titleIndex="'+g+'" href="'+_hrefLink+'">':"")+_getListShowByType(this.title[g].length?_getStrByLen(i[h],this.title[g].length):i[h],this.title[g].showType)+(j?"</a>":"")+"</td>")}e+="</tr>"}e+="</tbody>";$("#"+b).html('<table class="tableCommon" width="100%" border="0" cellspacing="0" cellpadding="0">'+d+e+"</table>");if(!(null==this.data||void 0==this.data||1>this.data.length)){var l=this.title;$.each(this.data,function(a,d){var c=$("#"+b).find("tr:eq("+(a+1)+") [type=link]");$.each(c,
function(a,b){var c=$(b),e=l[c.attr("titleIndex")].href;c.click(function(){e.call(e,a,d)})})});var k=this.trevent;$.each(this.data,function(a,d){for(var c in k){var e=k[c];$("#"+b).find("tr:eq("+(a+1)+")").bind(c,function(){e.call(e,a,d)})}});k=this.operations;$.each(this.data,function(a,d){var c=$("#"+b).find("tr:eq("+(a+1)+") td:last-child a");$.each(c,function(a,b){$(this).click(function(){$.each(k,function(a,c){c.name==$(b).text()&&k[a].event.call(k[a].event,a,d)})})})});try{_resetFrameHeight(document.documentElement.scrollHeight)}catch(p){}}}}
function _getListShowByType(a,b){switch(b){case "image":return a=a.replace(" ",""),0>a.indexOf("http://")&&0>a.indexOf("https://")&&(a=baseURL+"/"+a),void 0==b?"<img src='' alt=''/>":"<img src='"+a+"' alt=''/>";default:return void 0==a?"&nbsp;":a}}
function Pager(a){this.totalPages=a.totalPages;this.currentPage=a.currentPage;this.funcName=a.funcName;Pager.prototype.createPageBar=function(a){if(1>this.totalPages){$("#"+a).html('<a href="#" >上一页</a>|<a href="#" >下一页</a>|&nbsp;共0页<span class="font048"></span>');try{_resetFrameHeight(document.documentElement.scrollHeight)}catch(d){}}else{var c;c=""+('<a href="javascript:'+this.funcName+"("+(this.currentPage-1)+')" >上一页</a>|');c+='<a href="javascript:'+this.funcName+"("+(this.currentPage+1)+')" >下一页</a>|';
c+="&nbsp;共"+this.totalPages+'页<span class="font048">第 <input class="form24px border999" type="text" id="id_'+a+'_page" value="'+this.currentPage+'"/> 页</span>';c+='<input class="btnQuery" type="button" value="转到" onclick="var goPage=$(\'#id_'+a+"_page').val();if(!_checkPager("+this.totalPages+",goPage))return;"+this.funcName+'(goPage)"/>';$("#"+a).html(c);try{_resetFrameHeight(document.documentElement.scrollHeight)}catch(e){}}}}
function _checkPager(a,b){return!/^\d+$/.test(b)?(_alert("错误提示","页数格式不正确"),!1):b>a?(_alert("错误提示","超出最大页"),!1):!0}
var regYyyy_mm_dd_A=/^(\d{4})-(\d{1,2})-(\d{1,2})$/,regDateTime=/^(\d{4})-(\d{1,2})-(\d{1,2})\s(\d{1,2}):(\d{1,2}):(\d{1,2})$/,regChineseY_m_d=/^(\d{4})年(\d{1,2})月(\d{1,2})$/,regSlashY_m_d=/^(\d{4})\/(\d{1,2})\/(\d{1,2})$/,regSlashYmd=/^(\d{4})(\d{1,2})(\d{1,2})$/,sDateFormatA="yyyy-mm-dd",sDateFormatB="yyyy年mm月dd日",sDateFormatC="yyyy/mm/dd",MONTH_LENGTH=[31,28,31,30,31,30,31,31,30,31,30,31],LEAP_MONTH_LENGTH=[31,29,31,30,31,30,31,31,30,31,30,31];
function _isNull(a){"object"==typeof a&&(a=a.toString());return""==a||""==a.replace(/\s+/g,"")?!0:!1}function checkCertID(a){var b=!0,a=a.replace(/(^\s+)|(\s+$)/g,"");if(""==a)return"身份证号格式错误";if(15==a.length||18==a.length)15==a.length?(year="19"+a.substring(6,8),month=a.substring(8,10),day=a.substring(10,12)):(year=a.substring(6,10),month=a.substring(10,12),day=a.substring(12,14)),b=checkDate(year+month+day,"yyyymmdd");else return"不应为"+a.length+"位，请纠正\n";return""==b?"":"身份证号中的日期错误"}
function checkDate(a,b){if(""!=a){var d=null;null==b&&(b=sDateFormatA);if("yyyy-mm-dd"==b)d=regYyyy_mm_dd_A;else if("yyyy年mm月dd日"==b)d=regChineseY_m_d;else if("yyyy/mm/dd"==b)d=regSlashY_m_d;else if("yyyymmdd"==b)d=regSlashYmd;else if("yyyy-MM-dd HH:mm:ss"==b)d=regDateTime;else return"正确的格式应为:"+b+"!\n";if(!d.test(a))return"应为日期类型!";var c=a.match(d),d=c[1],e=c[2],c=c[3];return 1>d||9999<d||1>e||12<e||1>c||c>getMonthDay(e-1,d)?"中的日期有误!":""}return""}
function checkTime(a,b){var d=document.getElementsByName(a)[0].value,c=document.getElementsByName(b)[0].value,d=d.replace("-","/"),c=c.replace("-","/"),d=new Date(d),c=new Date(c),e=new Date,e=e.getFullYear()+"/"+(e.getMonth()+1)+"/"+e.getDate(),e=new Date(e);return d.getTime()<e.getTime()?"开始时间不能早于当天":c.getTime()<e.getTime()?"结束时间不能早于当天":d.getTime()>c.getTime()?"开始时间不能早于结束时间":!0}function getMonthDay(a,b){return isLeapYear(b)?LEAP_MONTH_LENGTH[a]:MONTH_LENGTH[a]}
function isLeapYear(a){return 0==a%4&&(0!=a%100||0==a%400)}function _strlength(a){for(var b=a.length,d=b,c=0;c<b;c++)(0>a.charCodeAt(c)||255<a.charCodeAt(c))&&d++;return d}function _getStrByLen(a,b){if(""==a)return"";for(var d=_strlength(a),c="",e=0,f=0;e<b;f++){e++;(0>a.charCodeAt(f)||255<a.charCodeAt(f))&&e++;if(e>d)break;if(e<=b+1&&(c+=a.charAt(f),e>b||e==b&&b!=d))c+="......"}return c}
function DqdpSelect(a){this.data=a.data;this.selectValue=a.selectValue;this.selectName=a.selectName;DqdpSelect.prototype.createSelect=function(a){for(var d=this.selectName,c=this.selectValue,e=this.data,f=0;f<e.length;f++)$("#"+a).append("<option value='"+e[f][c]+"'>"+e[f][d]+"</option>")}}
function HashMap(){var a=0,b={};this.put=function(d,c){this.containsKey(d)||a++;b[d]=c};this.get=function(a){return this.containsKey(a)?b[a]:null};this.remove=function(d){this.containsKey(d)&&delete b[d]&&a--};this.containsKey=function(a){return a in b};this.containsValue=function(a){for(var c in b)if(b[c]==a)return!0;return!1};this.values=function(){var a=[],c;for(c in b)a.push(b[c]);return a};this.keys=function(){var a=[],c;for(c in b)a.push(c);return a};this.size=function(){return a};this.clear=
function(){a=0;b={}}}
jQuery.fn.floatdiv=function(a){var b=!1;$.browser.msie&&"6.0"==$.browser.version&&($("html").css("overflow-x","auto").css("overflow-y","hidden"),b=!0);$("body").css({margin:"0px",padding:"0 10px 0 10px",border:"0px",height:"100%",overflow:"auto"});return this.each(function(){var d;if(void 0==a||a.constructor==String)switch(a){case "rightbottom":d={right:"0px",bottom:"0px"};break;case "leftbottom":d={left:"0px",bottom:"0px"};break;case "lefttop":d={left:"0px",top:"0px"};break;case "righttop":d={right:"0px",
top:"0px"};break;case "middle":var c=0,e;self.innerHeight?(d=self.innerWidth,e=self.innerHeight):document.documentElement&&document.documentElement.clientHeight?(d=document.documentElement.clientWidth,e=document.documentElement.clientHeight):document.body&&(d=document.body.clientWidth,e=document.body.clientHeight);d=d/2-$(this).width()/2;c=e/2-$(this).height()/2;d={left:d+"px",top:c+"px"};break;default:d={right:"0px",bottom:"0px"}}else d=a;$(this).css("z-index","9999").css(d).css("position","fixed");
b&&(void 0!=d.right&&(null==$(this).css("right")||""==$(this).css("right"))&&$(this).css("right","18px"),$(this).css("position","absolute"))})};


//宽度自适应
$(function(){
    var container =  $('#container').width();
    var aside_first =  $('#aside_first').width();
    var chooseDept = $('#chooseDept').width();
    // var left_w =  $('#left_w').width();
    // var right_w = chooseDept - left_w -99;
    var mian = $('#container').width() - $('#aside_first').width() - 20;
    //alert(container);
  
    //$('#right_w').width(right_w);
})

//高度自适应
$(function(){
     var container = $('#container').height();
     var main = $('#main').height();

     var cPage_h = $('.cPage').height()+200;
     if (container < cPage_h) {
    	 $('#container').height(cPage_h);
     }

})

/*if (typeof window.addEventListener != 'undefined') {
	  window.addEventListener('load',_resetFrameHeight,false);
	  window.addEventListener('click',_resetFrameHeight,false);  
	} else {
	  window.attachEvent('onload',_resetFrameHeight)
	   document.attachEvent('onclick',_resetFrameHeight)
	  
	}*/
$(window).on('load click',function(){
	setTimeout(function(){
		_resetFrameHeight();
	},300)
})
//正在加载中
$(function(){
	$('.aloading').on('click',function(){
			$('#main',window.top.document).append($('<div class="loadingBg_text" style="background: url('+baseURL+'/manager/images/logoloading.gif) no-repeat scroll center center ;height: 100px;left: 600px;padding-bottom: 20px;position: absolute;text-align: center; top: 180px;width: 250px;z-index: 1101;"></div>'));
	})
	if(window==window.top){
			//$('#main').append($('<div class="loadingBg_text" style="background: url('+baseURL+'/manager/images/logoloading.gif) no-repeat scroll center center ;height: 100px;left: 550px;padding-bottom: 20px;position: absolute;text-align: center; top: 180px;width: 250px;z-index: 1101;"></div>'));
			$('.tabs-primary a').on('click',function(){
					$('#main').append($('<div class="loadingBg_text" style="background: url('+baseURL+'/manager/images/logoloading.gif) no-repeat scroll center center ;height: 100px;left: 550px;padding-bottom: 20px;position: absolute;text-align: center; top: 180px;width: 250px;z-index: 1101;"></div>'));
				})	
	}
})
if(window!=window.top){
	$('.loadingBg_text',window.top.document).fadeOut(100);
	$('.loadingBg_text',window.top.document).remove();
}

var secretImg="/themes/default/images/qw/secret.jpg";//默认保密图片
var noCoverImg="/manager/images/img271,123.png";//默认的没有封面的图片
var coverImg="";//原来的封面图片，//这里必定是上传的



