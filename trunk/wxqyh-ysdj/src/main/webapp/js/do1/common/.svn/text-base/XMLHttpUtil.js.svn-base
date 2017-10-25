//author:Saron
if(typeof(HTMLElement)!="undefined" && !window.opera)
{
    HTMLElement.prototype.__defineGetter__("outerHTML",function() { var a = this.attributes, str="<"+this.tagName, i=0;for(;i<a.length;i++) if (a[i].specified) str += " " + a[i].name+'="'+a[i].value+'"'; if (!this.canHaveChildren) return str +" />"; return str+">"+this.innerHTML+"</"+this.tagName+">"; });
    HTMLElement.prototype.__defineSetter__("outerHTML",function(s) { var r = this.ownerDocument.createRange(); r.setStartBefore(this); var df = r.createContextualFragment(s); this.parentNode.replaceChild(df, this); return s; });
    HTMLElement.prototype.__defineGetter__("canHaveChildren",function() { return !/^(area|base|basefont|col|frame|hr|img|br|input|isindex|link|meta|param)$/.test(this.tagName.toLowerCase()); });
    HTMLElement.prototype.insertAdjacentHTML = function(where, html) { var e = this.ownerDocument.createRange(); e.setStartBefore(this); e = e.createContextualFragment(html); switch (where) { case 'beforeBegin': this.parentNode.insertBefore(e, this);break; case 'afterBegin': this.insertBefore(e, this.firstChild); break; case 'beforeEnd': this.appendChild(e); break; case 'afterEnd': if (!this.nextSibling) this.parentNode.appendChild(e); else this.parentNode.insertBefore(e, this.nextSibling); break; } };
    XMLDocument.prototype.loadXML = function(xmlString) { var childNodes = this.childNodes; for (var i = childNodes.length - 1; i >= 0; i--) this.removeChild(childNodes[i]); var dp = new DOMParser(); var newDOM = dp.parseFromString(xmlString, "text/xml"); var newElt = this.importNode(newDOM.documentElement, true); this.appendChild(newElt); };
    if (document.implementation.hasFeature("XPath", "3.0")) {
        XMLDocument.prototype.selectNodes = function(cXPathString, xNode) { if (!xNode) { xNode = this; } var oNSResolver = this.createNSResolver(this.documentElement); var aItems = this.evaluate(cXPathString, xNode, oNSResolver, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null); var aResult = []; for (var i = 0; i < aItems.snapshotLength; i++) { aResult[i] = aItems.snapshotItem(i); } return aResult; };
        Element.prototype.selectNodes = function(cXPathString) { if (this.ownerDocument.selectNodes) { return this.ownerDocument.selectNodes(cXPathString, this); } else { throw "For XML Elements Only";} };
    };
    if( document.implementation.hasFeature("XPath", "3.0") ) {
        XMLDocument.prototype.selectSingleNode = function(cXPathString, xNode) { if (!xNode ) { xNode = this; } var xItems = this.selectNodes(cXPathString, xNode); if (xItems.length > 0 ) { return xItems[0]; } else { return null; } };
        Element.prototype.selectSingleNode = function(cXPathString) { if (this.ownerDocument.selectSingleNode) { return this.ownerDocument.selectSingleNode(cXPathString, this); } else { throw "For XML Elements Only";} };
    };
    HTMLElement.prototype.__defineGetter__("innerText", function() { var anyString = ""; var childS = this.childNodes; for (var i = 0; i < childS.length; i++) { if (childS[i].nodeType == 1) anyString += childS[i].innerText; else if (childS[i].nodeType == 3) anyString += childS[i].nodeValue; } return anyString; });
    HTMLElement.prototype.__defineSetter__("innerText", function(sText) { this.textContent = sText; });
}
function getPrieviousNode($node) {  if (typeof($node) == "undefined"){return null;}if (typeof($node.outerHTML) == "undefined") { return getPrieviousNode($node.previousSibling); } else return $node; }
function getNextNode($node) { if (typeof($node) == "undefined"){return null;} if (typeof($node.outerHTML) == "undefined") { return getNextNode($node.nextSibling); } else return $node; }
function getAvalibleChildNodesNum($node){
    try {
        var num = 0;
        if (typeof($node.outerHTML) == "undefined")return 0;
        for (var i = 0; i < $node.childNodes.length; i++) {
            if (typeof($node.childNodes[i].outerHTML) !="undefined")
            num++;
        }
        return num;
    } catch(e) {
    }
}
function XMLHttpUtil() {
    this.method = "POST"; this.url = ""; this.syn = true; this.processer = ""; this.failProcesser = ""; this.xmlData = null; this.xHttp = null; this.onreadystatechange = null; this.encode = "GBK"; this.decoder = null;
    this.textType = "text/xml";
    this.showWarn = false;//是否显示警示信息，这些信息可能会有用，非必要应将此参数设为true
    this.replaceAll = 1;//是否覆盖所有区域，即包含自身,默认是全覆盖，为0时表示只覆盖子结点
    XMLHttpUtil.prototype.sendGet = function($url, $processer, $syn) {
        this.setUrl($url);
        this.setMethod("GET");
        this.setProcesser($processer);
        if (arguments.length > 2) this.setSyn($syn);
        this.send(this);
    };
    XMLHttpUtil.prototype.sendPost = function($url, $processer, $posFormId, $syn) { if ($url == "")this.setUrl(document.getElementById($posFormId).action); this.setUrl($url); this.setMethod("POST"); this.setProcesser($processer); if (arguments.length > 3) this.setSyn($syn); this.sendFormById($posFormId); };
    XMLHttpUtil.prototype.sendNoFormPost = function($url, $processer, $postData, $syn) { this.setUrl($url); this.setMethod("POST"); this.setProcesser($processer); if (arguments.length > 3) this.setSyn($syn); this.xmlData = $postData; this.send(this); };
    XMLHttpUtil.prototype.send = function($httpInstance) {
        if (this.url == "") {
            alert("请先设置要跳转的页面！");
            return false;
        }
        if (this.method == "POST" && this.xmlData == null) {
            alert("请先将要传递的数据设置好！");
            return false;
        }
        this.xHttp = this.getHTTPObject();
        if ($httpInstance.onreadystatechange == null) {this.xHttp.onreadystatechange = function() {
            if ($httpInstance.xHttp.readyState == 4) {
                if ($httpInstance.xHttp.status == 200) { eval($httpInstance.processer); } else {
                    if (this.showWarn) { alert("请求中发生错误，错误的状态码为：" + $httpInstance.xHttp.status + "\n解决此问题请与管理员联系"); }
                    eval($httpInstance.failProcesser);
                }
            }
        }
        } else {this.xHttp.onreadystatechange = $httpInstance.onreadystatechange; }
        this.xHttp.open(this.method, this.url, this.syn);
        if (this.method == "POST") {
            this.xHttp.setRequestHeader('Content-Length', this.xmlData.length);
            this.xHttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=' + this.encode);
        } else { this.xHttp.setRequestHeader("Content-Type", this.encode); }
        this.xHttp.send(this.xmlData);
    };
    XMLHttpUtil.prototype.setMethod = function ($method) { this.method = $method; };
    XMLHttpUtil.prototype.setOnreadystatechange = function ($onreadystatechange) { this.onreadystatechange = $onreadystatechange; };
    XMLHttpUtil.prototype.setEncode = function ($encode) { this.encode = $encode; };
    XMLHttpUtil.prototype.setDecoder = function ($decoder) { this.decoder = $decoder; };
    XMLHttpUtil.prototype.setTextType = function ($textType) { this.textType = $textType; };
    XMLHttpUtil.prototype.setSyn = function ($syn) { this.syn = $syn; };
    XMLHttpUtil.prototype.setUrl = function($url) { this.url = $url; };
    XMLHttpUtil.prototype.setXmlData = function($xml) { this.xmlData = $xml; };
    XMLHttpUtil.prototype.setProcesser = function($processer) { this.processer = $processer; };
    XMLHttpUtil.prototype.setFailProcesser = function($fail) { this.failProcesser = $fail; };
    XMLHttpUtil.prototype.getResponseJson = function() { return eval('(' + (this.xHttp.responseText) + ')');};
    XMLHttpUtil.prototype.getResponseXML = function() {   return this.xHttp.responseXML;
//        var domObj = (navigator.userAgent.indexOf("MSIE") > 0) ? new ActiveXObject("MSXML.DOMDocument") : document.implementation.createDocument("", "", null);
//        var resonseText = this.xHttp.responseText;
//        try {
//            domObj.loadXML(resonseText);
//        } catch(e) {
//        }
//        return domObj;
    };
    XMLHttpUtil.prototype.sendFormById = function($form) { this.setUrlByFormId($form); this.setXmlData(new XMLAssistant().getByFormId($form)); this.send(this); };
    XMLHttpUtil.prototype.sendFormByName = function($form) { this.setUrlByFormName($form); this.setXmlData(new XMLAssistant().getByFormName($form)); this.send(this); };
    XMLHttpUtil.prototype.getResponseContent = function() { return this.xHttp.responseText; };
    XMLHttpUtil.prototype.getResponseBody = function() { return this.xHttp.responseText; };
    XMLHttpUtil.prototype.setUrlByFormId = function($form) { var formEle = document.getElementById($form); if (formEle) { this.setUrlByForm(formEle); } else { alert("当前网页中没有ID为" + $form + "的表单存在！"); return false; } };
    XMLHttpUtil.prototype.setUrlByFormName = function($form) { var formEle = document.getElementsByName($form); if (formEle) { this.setUrlByForm(formEle[0]); } else { alert("当前网页中没有ID为" + $form + "的表单存在！"); return false; } };
    XMLHttpUtil.prototype.setUrlByForm = function($form) { var url = $form.action; if (this.url == "")this.url = url; };
    /**
     *
     * @param $httpInstance  XMLHttpUtil的一个实例
     * @param $objID            要替换区哉的ID
     * @param $url                 用来替换的输出源
     */
    XMLHttpUtil.prototype.replaceHTML = function($httpInstance, $objID, $url, $replaceAll) {
        var replaceObj = document.getElementById($objID);
        if (undefined == replaceObj) { if (this.showWarn) alert("指定的覆盖区域" + $objID + "不存在！"); return false; }
        if ($url == "") { replaceObj.outerHTML = "<table><tr><td>设置的访问路径为空</td></tr></table>"; return false; }
        if (arguments.length > 3)$httpInstance.replaceAll = $replaceAll;
        if ($httpInstance.xHttp == null)
            $httpInstance.xHttp = $httpInstance.getHTTPObject();
        $httpInstance.xHttp.open("GET", $url, $httpInstance.syn);
        $httpInstance.xHttp.setRequestHeader("Content-Type", "text/xml");
        $httpInstance.xHttp.setRequestHeader("Content-Type", $httpInstance.encode);
        $httpInstance.xHttp.onreadystatechange = function()
        {
            if ($httpInstance.xHttp.readyState == 4) {
                if ($httpInstance.xHttp.status == 200) {
                    if ($httpInstance.getResponseBody() != undefined && $httpInstance.getResponseBody() != "") {
                        if ($httpInstance.decoder != null)
                            if ($httpInstance.replaceAll == "0")
                                replaceObj.innerHTML = bytes2BSTR($httpInstance.getResponseBody());
                            else replaceObj.outerHTML = bytes2BSTR($httpInstance.getResponseBody());
                        else
                            if ($httpInstance.replaceAll == "0")
                                replaceObj.innerHTML = ($httpInstance.getResponseContent());
                            else replaceObj.outerHTML = ($httpInstance.getResponseContent());
                    }
                    eval($httpInstance.processer);
                }
                else {
                    if (this.showWarn) {
                        alert("请求中发生错误，错误的状态码为：" + $httpInstance.xHttp.status + "\n解决此问题请与管理员联系");
                    }
                    eval($httpInstance.failProcesser);
                }
            }
        };
        $httpInstance.xHttp.send(null);
    };

    XMLHttpUtil.prototype.getHTTPObject = function() {
        var xmlhttp = null; var success = false;
        //        var MSXML_XMLHTTP_PROGIDS = new Array(
        //                        'MSXML2.XMLHTTP.4.0',
        //                        'MSXML2.XMLHTTP.3.0',
        //                        'MSXML2.XMLHTTP',
        //                        'Microsoft.XMLHTTP'
        //                        );
        var MSXML_XMLHTTP_PROGIDS = new Array('MSXML2.XMLHTTP', 'Microsoft.XMLHTTP');
        for (var i = 0; i < MSXML_XMLHTTP_PROGIDS.length && !success; i++) { try { xmlhttp = new ActiveXObject(MSXML_XMLHTTP_PROGIDS[i]); success = true; return xmlhttp; } catch (e) { xmlhttp = false; } }
        if (!xmlhttp && typeof XMLHttpRequest != 'undefined') { try { xmlhttp = new XMLHttpRequest(); } catch (e) { xmlhttp = false; } }
        return xmlhttp;
    };

}
