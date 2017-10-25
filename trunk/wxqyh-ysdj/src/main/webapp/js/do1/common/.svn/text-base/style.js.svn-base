

var lastObj = null;
var lastcolr = "";
var chooseID = "";
var id = "";
var operType = "";
function doListClick($obj, $id, $operType) {
    //  if (event.ctrlKey) {
    //
    //  }
    //  else {
    if (lastObj != null) {
        lastObj.style.backgroundColor = lastcolr;
    }
    lastcolr = $obj.style.backgroundColor;
    if ($obj.className == "tr_table_desc")
        $obj.style.backgroundColor = "#FF9900";
    if ($obj.className == "tr_list" || $obj.className == "tr_list1")
        $obj.style.backgroundColor = "#ff9900";//FFFFCC FFFF00
    lastObj = $obj;
    if (typeof($id) == "undefined") id = "";
    else id = $id;
    if (typeof($operType) == "undefined") operType = "";
    else operType = $operType;
    //  }
}

var activeWindow = null;

function openChildWindow($url, $name, $parm) {
    if (activeWindow != null) {
        try {
            activeWindow.close();
        } catch(e) {

        }
        activeWindow = window.open($url, $name, $parm);
        activeWindow.focus();
    } else {
        activeWindow = window.open($url, $name, $parm);
    }
}


function doSearchInputClick($input, $msg) {
    if ($input.value == $msg) {
        $input.value = "";
        $input.style.color = "#000000";
    }
}

function doSearchInputBlue($input, $msg) {
    if ($input.value == "") {
        $input.value = $msg;
        $input.style.color = "#cccccc";
    }
}

//隐藏页面里的所有select标签
function doHiddenSelect() {
    var tags = document.all;
    if (tags) {
        for (var i = 0; i < tags.length; i++) {
            if ("true" == tags[i].getAttribute("hiddenAble"))
                tags[i].style.visibility = "hidden";
        }
    }
}

//显示页面里的所有select 标签
function doShowSelect() {
    var tags = document.all;
    if (tags) {
        for (var i = 0; i < tags.length; i++) {
            if ("true" == tags[i].getAttribute("hiddenAble"))
                tags[i].style.visibility = "visible";
        }
    }
}


/**------------------------------------------------改变元素的显示状态-----------------------------------------------------*/
function doChangeDisplayByName($names, $clear) {
    var names = $names.split(",");
    for (var i = 0; i < names.length; i++)
        doChangeDisplay(document.getElementsByName(names[i]), $clear);
}

function doChangeDisplayById($ids, $clear) {
    var ids = $ids.split(",");
    for (var i = 0; i < ids.length; i++)
        doChangeDisplay(new Array(document.getElementById(ids[i])), $clear);
}

function doChangeDisplay($elems, $clear) {
    if ($elems) {
        for (var i = 0; i < $elems.length; i++) {
            if ("none" == $elems[i].style.display)
                $elems[i].style.display = "block";
            else {
                $elems[i].style.display = "none";
                if ($clear) {
                    var inputEls = $elems[i].getElementsByTagName("input");
                    var selectEls = $elems[i].getElementsByTagName("select");
                    var textEls = $elems[i].getElementsByTagName("textarea");
                    for (var j = 0; j < textEls.length; j++)textEls[j].value = "";
                    for (var j = 0; j < selectEls.length; j++)selectEls[j].selectedIndex = 0;
                    for (var j = 0; j < inputEls.length; j++) {
                        inputEl = inputEls[j];
                        switch (inputEl.type) {
                            case "text":{
                                inputEl.value = "";
                                break;
                            }
                            case "hidden":{
                                inputEl.value = "";
                                break;
                            }
                            case "checkbox":{
                                inputEl.checked = false;
                                break;
                            }
                            case "radio":{
                                inputEl.checked = false;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
/**-----------------------------------------------------------------------------------------------------------------------------------------*/

/**
 * 传入input对象和默认值，如果值没有改变，则隐藏
 * @param $input
 * @param $defaultValue
 */
function doInputValueHidden($input, $defaultValue) {
    if ($input.value == $defaultValue)$input.value = "";

}

/**
 * 与上一方法相反
 * @param $input
 * @param $defaultValue
 */
function doInputValueShow($input, $defaultValue) {
    if ($input.value == "")$input.value = $defaultValue;

}

function readOnlyAll() {
    var els = document.forms[0].elements;
    var e = null;
    for (var i = 0; i < els.length; i++) {
        e = els[i];
        if (e.tagName == "INPUT") {
            if (e.type == "button") {
                if (e.getAttribute("can-readonly") != "false") {
                    e.disabled = true;
                }
            }
            else {
                if (e.type == "radio" || e.type == "checkbox") {
                    e.disabled = true;
                } else {
                    e.style.backgroundColor = "#cccccc";
                    e.readOnly = "true";
                }
                //        e.isContentEditable=true;
            }
        }
        if (e.tagName == "TEXTAREA") {
            e.style.backgroundColor = "#cccccc";
            e.readOnly = "true";
        }
        if (e.tagName == "SELECT") {
            e.style.backgroundColor = "#cccccc";
            reDoSelect(e);
        }
    }
    var imgEle = document.forms[0].getElementsByTagName("img");
    for (var i = 0; i < imgEle.length; i++) {
        e = imgEle[i];
        if (e.src.indexOf("calendar.gif") > -1)e.style.display = "none";
    }
}


function reDoSelect(selectObj) {
    var value = "";
    var text = "";
    var so = selectObj.options;
    for (var i = 0; i < so.length; i++) {
        if (so[i].selected)
        {
            value = so[i].value;
            text = so[i].text;
        }
    }
    while (so.hasChildNodes()) {
        so.removeChild(so.firstChild);
    }
    var option = document.createElement("OPTION");
    so.appendChild(option);
    option.value = value;
    option.text = text;
    option.selected = true;
}

function disableAllButton() {
    var allForms = document.forms;
    for (var f = 0; f < allForms.length; f++) {
        var els = allForms[f].elements;
        for (var i = 0; i < els.length; i++) {
            var e = els[i];
            if (e.tagName == "INPUT") {
                if (e.type == "button")
                    e.disabled = true;
            }
        }
    }
}

function reBuildPage() {
    var allForms = document.forms;
    for (var f = 0; f < allForms.length; f++) {
        var els = allForms[f].getElementsByTagName("TD");
        for (var i = 0; i < els.length; i++) {
            var e = els[i];
            if (e.getAttribute("must_caution") == "true") {
                e.innerHTML = e.innerHTML + "<font color='red'>*</font>";
            }
        }
        els = allForms[f].getElementsByTagName("INPUT");
        appendLimit(els);
        els = allForms[f].getElementsByTagName("TEXTAREA");
        appendLimit(els);
    }
}

function appendLimit($els) {
    if ($els)
        for (i = 0; i < $els.length; i++) {
            var e = $els[i];
            if (e.getAttribute("charLimt") > 0) {
                eventObjMap.put(e.name, e);
                e.onkeyup = checkInputSize;
                e.onchange = checkInputSize;
            }
        }
}

//window.attachEvent("onload", doHiddenSelect);
window.attachEvent("onload", reBuildPage);

function getTopFrame($win) {
    var pw = getWindowOpener($win);
    if (undefined != pw)
        $win = getTopFrame(pw);
    return $win;
}
function getWindowOpener($win) {
    return $win.opener;
}

var gzpm_topFrame = getTopFrame(this).top.topFrame;

var currSearchCheckbox;
function doSearchCheckClick() {
    var tagURL = getSearchTargeURL(currSearchCheckbox);
    if (gzpm_topFrame) {
        gzpm_topFrame.setSearchStatus(currSearchCheckbox.checked, tagURL);
    }
}
function initSerachCheck() {
    var searchCheckbox = document.getElementById("gzpm_search_checkbox");
    var tagURL;
    if (searchCheckbox) {
        currSearchCheckbox = searchCheckbox;
        searchCheckbox.attachEvent("onclick", doSearchCheckClick);
        tagURL = getSearchTargeURL(currSearchCheckbox);
    }
    if (gzpm_topFrame && gzpm_topFrame.getSearchStatus(tagURL)) {

        currSearchCheckbox.checked = true;
        doShowMoreSearchItem(currSearchCheckbox);
    }
}
function getSearchTargeURL($cbx) {
    var tagURL;
    if ((tagURL = $cbx.getAttribute("nextURL")) == "" || $cbx.getAttribute("nextURL") == null)tagURL = this.document.location;
    return tagURL;
}
//window.attachEvent("onload", initSerachCheck);

function setListHeight() {
    var div_sl = document.getElementById("div_all_document");
    if (div_sl)
        div_sl.style.height = document.body.clientHeight - 60;
    var div_noPage = document.getElementById("div_all_document_noPage");
    if (div_noPage)
        div_noPage.style.height = document.body.clientHeight - 30;
//    var div_s = document.getElementById("control_div_list_area");
    //    if (div_s)
    //        div_s.style.height = document.body.clientHeight - 130;
}
//window.attachEvent("onload", setListHeight);

function showListStyle() {
    var div_table = document.getElementsByTagName('table') ;
    if (div_table) {
        for (var i = 0; i < div_table.length; i++) {
            if (div_table[i].getAttribute("need2Style") == "true") {
                var trEles = div_table[i].getElementsByTagName("tr");
                if (trEles) {
                    for (var j = 1; j < trEles.length; j++) {
                        if (j % 2 == 0) {
                            trEles[j].className = "list_td2";
                            trEles[j].onmousemove = function() {
                                this.className = 'list_td0';
                            }
                            trEles[j].onmouseout = function() {
                                this.className = 'list_td2';
                            }
                        }
                        else {
                            trEles[j].className = "list_td";
                            trEles[j].onmousemove = function() {
                                this.className = 'list_td0';
                            }
                            trEles[j].onmouseout = function() {
                                this.className = 'list_td';
                            }
                        }
                    }
                }
            }
        }
    }

}
//window.attachEvent("onload", showListStyle);
function _sjs_doCheckAll() {
    var evenObj = event.srcElement;
    var stat = evenObj.checked;
    var appendObjName = evenObj.getAttribute("checkName");
    var shoudCheckObjs = document.getElementsByName(appendObjName);
    if (shoudCheckObjs) {
        for (var i = 0; i < shoudCheckObjs.length; i++) {
            shoudCheckObjs[i].checked = stat;
        }
    }
}
function appendCheckAll() {
    var inputEls = document.getElementsByTagName("input");
    if (inputEls) {
        for (var i = 0; i < inputEls.length; i++) {
            var inputEl = inputEls[i];
            switch (inputEl.type) {
                case "checkbox":{
                    if (inputEl.getAttribute("checkAll") == "true") {
                        inputEl.onclick = _sjs_doCheckAll;
                    }
                }
            }
        }
    }
}

window.attachEvent("onload", appendCheckAll);

function previewImg(filePath)
{
	if(filePath==''){
	
	}
	else{
	    //新的预览代码，支持 IE6、IE7。
	    var img = document.getElementById("img");
	    img.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = filePath;
    }
}

function openLink(link,w,h){
	if (link!=""){
		var url= link;// + ".jsp";
		var winW=w;
		var winH=h;
		var winX=(window.screen.width-winW)/2;
		var winY=(window.screen.height-winH)/2;
		var openArticle=window.open(url,'','left='+winX+',top='+winY+',width='+winW+',height='+winH+',toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
		openArticle.focus();
	} 
}