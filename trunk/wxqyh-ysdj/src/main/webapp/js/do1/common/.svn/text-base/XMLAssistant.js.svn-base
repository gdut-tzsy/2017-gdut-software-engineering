//author Saron
function XMLAssistant() {
    this.head = '<?xml version="1.0" encoding="GBK"?>';
    this.body = "";
    this.parm = [];
    this.parm.push(this.head);
    this.parm.push("<body>");

    /**
     *
     */
    XMLAssistant.prototype.getXML = function() {
        var doc = new ActiveXObject("MSXML.DOMDocument");
        this.parm.push("</body>");
        this.body = this.body + this.parm.join("\n");
        doc.loadXML(this.body);
        return doc;
    };

    /**
     *
     * @param $body
     */
    XMLAssistant.prototype.setBody = function ($body) {
        this.body = $body;
    };
    /**
     *
     * @param $name
     * @param $value
     */
    XMLAssistant.prototype.pushElement = function($name, $value) {
        this.parm.push("<" + $name + "><![CDATA[" + $value + "]]></" + $name + ">");
    };
    /**
     *
     * @param $form
     */
    XMLAssistant.prototype.getByFormId = function($form) {
        var formEle = document.getElementById($form);
        if (formEle) {
            return this.getFormXML(formEle);
        } else {
            alert("当前网页中没有ID为" + $form + "的表单存在！");
            return false;
        }
    };
    /**
     *
     * @param $form
     */
    XMLAssistant.prototype.getByFormName = function($form) {
        var formEle = document.getElementsByName($form);
        if (formEle) {
            return this.getFormXML(formEle[0]);
        } else {
            alert("当前网页中没有名为" + $form + "的表单存在！");
            return false;
        }
    };
    /**
     *
     * @param $form
     */
    XMLAssistant.prototype.getFormXML = function($form) {
        var allEle = $form.all;
        for (var i = 0; i < allEle.length; i++) {
            if ("true" == allEle[i].getAttribute("xhttp_support") && allEle[i].name != "" && allEle[i].name != null) {
                var canPut = true;
                if (allEle[i].tagName == "INPUT") {
                    switch (allEle[i].type) {
                        case "radio":{
                            if (allEle[i].checked) {
                                this.pushElement(allEle[i].name, allEle[i].value);
                            }
                            canPut = false;
                            break;
                        }
                        case "file":{
                            canPut = false;
                            break;
                        }
                        case "checkbox":{
                            if (allEle[i].checked) {
                                this.pushElement(allEle[i].name, allEle[i].value);
                            }
                            canPut = false;
                            break;
                        }
                    }
                }
                if (canPut)
                    this.pushElement(allEle[i].name, allEle[i].value);
            }
        }
        return this.getXML();
    };
}