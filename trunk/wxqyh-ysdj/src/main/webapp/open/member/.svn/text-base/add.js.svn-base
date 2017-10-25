/**
 * Created by zengjiaying on 2017/4/28.
 */
$(function () {

    // ======================== 公共部分start ================
    WeixinJS = typeof WeixinJS != 'undefined' || {};
    // 微信扫一扫功能
    function barCode(obj, type) {
        wx.scanQRCode({
            desc: 'scanQRCode desc',
            needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
            scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是一维码，默认二者都有
            success: function (res) {
                var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
                if (type == "imageRadioButton") {
                    $($(obj).next()).val(result);
                } else if (type == "expresslist") {//快递单据扫描
                    result = $($(obj).next()).val() + result + '\n';
                    if (result.length > 500) {
                        return;
                    }
                    $($(obj).next()).val(result);
                    $('textarea.inputStyle').trigger('click');
                } else if (type == "meetingSummary") {
                    scanCodeToSummary(result);
                }
            }
        });
    }
    //微信后退操作
    WeixinJS.back = function () {
        var length = window.history.length;
        //判断当前打开的页面的个数
        if (length < 2) {
            WeixinJSBridge.invoke('closeWindow', {});
        }
        else {
            history.back();
        }
    };
    //关闭网页进入微信窗口
    WeixinJS.goWxWindow = function () {
        WeixinJSBridge.invoke('closeWindow', {});
    };
    // WeixinJS.hideOptionMenu() 隐藏右上角按钮
    WeixinJS.hideOptionMenu = function () {
        document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
            if (typeof WeixinJSBridge != 'undefined') WeixinJSBridge.call('hideOptionMenu');
        });
    };
    // WeixinJS.hideToolbar() 隐藏工具栏
    WeixinJS.hideToolbar = function () {
        document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
            if (typeof WeixinJSBridge != 'undefined') WeixinJSBridge.call('hideToolbar');
        });
    };
    // ======================== 公共部分end ==================

    var FormId = $("#id").val();    // 表单id
    var FormData;
    var wxqrcode = '';
    var isDefault = "";
    var isClick = false;
    var number = 1;

    // 显示表单
    showFormData();

    // 表单提交
    $("#formBtn").on('click', function () {
        commitForm(CommitFormUrl)
    });

    // 判断是否将手机号或微信号作为账号
    $('#weixinNum').add('#mobile').on('blur', function () {
        if (isDefault == "1") {
            var mobile = $("#mobile").val();
            if (mobile !== "") {
                $("#wxUserId").val(mobile);
            } else {
                $("#wxUserId").val($("#weixinNum").val());
            }
        }
    });

    // 验证输入是否正确
    $('#question_detail').on('change', '.textInput', function () {
        validateInput($(this).attr('data-valid'), $(this));
    });

    // 选择部门
    $('#selectDeptId').on('change', function () {
        var obj = this;
        var deptName = obj.options[obj.selectedIndex].innerHTML;
        var deptId = obj.options[obj.selectedIndex].value;
        $("#deptName").val(deptName);
        $("#deptId").val(deptId);
    });

    // 选择性别
    $('body').on('click', '.sexHook li', function () {
        radioSelect(this);
    });

    // 点击显示多选框
    $('#question_detail').on('click', '.mutiCheckBox .textInput', function () {
        var dataId = $(this).parent('.mutiCheckBox ').attr('data-id');
        $(this).addClass('active');
        $('#wrap_main').addClass('none');
        $('#showMutiCheckBox').removeClass('none');
        $('#' + dataId, '#showMutiCheckBox').removeClass('none').addClass('active');
    });
    // 点击多选框的确定和取消按钮
    $('.buttonDiv .cancel').on('click', function () {
        $('#wrap_main').removeClass('none');
        $('#showMutiCheckBox').addClass('none');
        $('#showMutiCheckBox .list .checkbox_hook').addClass('none').removeClass('active');
        $('.mutiCheckBox .textInput').removeClass('active');
    });
    // 点击多选框的确定按钮
    $('.buttonDiv .save').on('click', function () {
        var currentChexkbox = $('input:checked', '#showMutiCheckBox .list .active');
        var s = '';     // 当前已选择的多选框
        $.each(currentChexkbox, function (i, val) {
            s += $(val).val() + ',';
        });
        s = s.slice(0, s.length - 1);
        $('.mutiCheckBox .active').val(s);
        $('.mutiCheckBox .textInput').removeClass('active');
        $('#wrap_main').removeClass('none');
        $('#showMutiCheckBox').addClass('none');
        $('#showMutiCheckBox .list .checkbox_hook').addClass('none').removeClass('active');
    });

    //已提交的页面不能返回到第一页
    $('.i_form-write').click(function () {
        if (!isClick) {
            $('#question_detail').show();
            $('.inter_saoma').hide();
            $('.i_liucheng1').attr('src', resourceURL + '/jsp/wap/images/tab.png');
        }
    });
    $('.i_form-saoma').click(function () {
        $('.inter_saoma').show();
        $('#question_detail').hide();
        $('.i_liucheng1').attr('src', resourceURL + '/jsp/wap/images/tab2.png');
    });

    //含有图片的微信弹框
    function _alert_weui(url, text, buttonText, callback) {
        if (typeof(buttonText) == 'string' && buttonText.split("|").length == 1) {
            buttonText1 = buttonText.split("|")[0];
            var buttonHtml = '<div class="weui_dialog_ft">'
                + '<a href="javascript:;" class="weui_btn_dialog primary">' + buttonText1 + '</a>'
                + '</div>';
        } else {
            if (typeof(buttonText) == 'string' && buttonText.split("|").length >= 2) {
                var buttonText1 = buttonText.split("|")[0];
                var buttonText2 = buttonText.split("|")[1];
            } else if (typeof(buttonText) == 'function' || typeof(buttonText) == 'object' && buttonText['ok'] || typeof(buttonText) == 'object' && buttonText['fail']) {
                callback = buttonText;
                var buttonText1 = "取消";
                var buttonText2 = "确认";
            } else {
                var buttonText1 = "取消";
                var buttonText2 = "确认";
            }
            var buttonHtml = '<div class="weui_dialog_ft">'
                + '<a href="javascript:;" class="weui_btn_dialog default">' + buttonText1 + '</a>'
                + '<a href="javascript:;" class="weui_btn_dialog primary">' + buttonText2 + '</a>'
                + '</div>';
        }
        var boxClass = "weui_dialog_alert custom_dialog";
        var html = '<div class="' + boxClass + '">'
            + '<div class="weui_mask"></div>'
            + '<div class="weui_dialog">'
            + '<div class="weui_dialog_img"><img style="height:170px;width:170px;margin:30px 0 15px;" src="' + url + '"></div>'
            + '<div class="weui_dialog_bd">' + text + '</div>'
            + buttonHtml
            + ' </div>'
            + ' </div>';
        $('body').append(html);
        $('.weui_btn_dialog.primary').off('click').on('click', function () {//确认
            $('.weui_btn_dialog.primary').off('click');
            if (callback && typeof(callback) == 'function') {
                callback();
            } else if (callback && typeof(callback) == 'object' && callback['ok']) {
                callback['ok']();
            }
            $(this).parents('.custom_dialog').remove();
        });
        $('.weui_btn_dialog.default').off('click').on('click', function () {//取消
            if (callback && typeof(callback) == 'object' && callback['fail']) {
                callback['fail']();
            }
            $(this).parents('.custom_dialog').remove();
        });

    }

    // 输入框清空文字
    $('body').on('focus', '.search-input', function (event) {
        var input_colse = $('<span class="input_colse"></span>');
        if ($(this).val().length > 0) {
            $('.input_colse').remove();
            $(this).after(input_colse)
        } else {
            $('.input_colse').remove();
        }
        $('.input_colse').on('click', function () {
            $(this).prev('.search-input').val('');
            $(this).remove();
        })
    });

    // 渲染表单
    function renderForm(memberBaseOptionVOs, optionVOs, memberCustomOptionVOs, custom) {
        if (memberBaseOptionVOs.length) {
            showBaseInputList(memberBaseOptionVOs);
        } else {
            changeCustom(custom);
        }
        if (memberCustomOptionVOs.length) {
            showCustomInputList(optionVOs, memberCustomOptionVOs);
        }
    }

    // 显示默认字段
    function showBaseInputList(data) {
        $.each(data, function (i, val) {
            var base_id, isMust;
            if (val.base_id) {      // 为了兼容之前的字段 config.custom
                if (parseInt(val.isShow)) {
                    base_id = val.base_id;
                    isMust = val.isMust;
                }
            } else {
                base_id = val;
            }
            var isMustClass = parseInt(isMust) ? 'isMust' : '';       // 是否必填
            $('#' + base_id).show().addClass(isMustClass);
            if (base_id === 'account') {
                if (isDefault == "0") {
                    $("#msgSpan").remove();
                }
                $('#account').addClass('isMust')
            } else {
                isDefault = "1";
            }
        });
        if ($('#tel').css('display') === 'none' && $('#weixin').css('display') === 'none') {
            $("#weixin").show();
        }
    }

    // 为了兼容之前的混乱命名，先将之前的命名转换
    function changeCustom(custom) {
        var newVal = {
            "weiXinNumber": "weixin",
            "tel": "tel",
            "a6": "account",
            "a2": "emailId",
            "a4": "deptNo",
            "a3": "sexID",
            "a7": "nikeName",
            "a8": "idCard",
            "a9": "position",
            "b1": "qqNum",
            "b2": "phone",
            "b3": "phone1",
            "b4": "address",
            "b5": "birthday",
            "b6": "birthday2",
            "a1": "company",
            "b7": "remarkId"
        };
        var newCustom = [];
        if (($.inArray("weiXinNumber", custom) === -1) && ($.inArray("tel", custom)) === -1) {
            $("#weixin").show();
        }
        $.each(custom, function (i, val) {
            $.each(newVal, function (j, jval) {
                if (j === val) {
                    return newCustom.push(jval)
                }
            })
        });
        if (newCustom) {
            if (newCustom === '-1') return;
            showBaseInputList(newCustom);
        }
    }

    // 显示自定义字段
    function showCustomInputList(optionVOs, memberCustomOptionVOs) {
        var $el = '';
        var type = '';          // 输入框类型，1：单行输入，2：多行输入，3：为下拉菜单，4：日期选择，5：多选框
        var isMustClass = '';   // 是否必填的类名
        var optionName;         // 字段名称
        var customId;           // 已显示的字段ID
        $.each(optionVOs, function (i, ival) {
            var desVos = ival.desVos;        // 下拉菜单的选项
            var desVosPlaceholder = (desVos.length === 1) ? desVos[0].name : '请输入';
            var $eachCheckBox = '';
            var $eachOption = '';   // 每个下拉选项DOM
            $.each(memberCustomOptionVOs, function (j, jval) {
                customId = jval.customId;
                if (ival.id === customId) {
                    if (parseInt(jval.isShow)) {
                        optionName = ival.optionName;
                        isMustClass = parseInt(jval.isMust) ? ' isMust' : '';
                        type = ival.type;
                        if (type === '1') {
                            $el += '<div class="custom' + isMustClass + '" data-id=' + customId + '>' +
                                '<div class="letter_bar"><span class="optionName">' + optionName + '</span></div>' +
                                '<input class="item-select textInput" type="text" maxlength="50" placeholder="' + desVosPlaceholder + '" style="width:100%">' +
                                '</div>' +
                                '</div>';
                        } else if (type === '2') {
                            $el += '<div class="custom' + isMustClass + '" data-id=' + customId + '>'
                                + '<div class="letter_bar"><span class="optionName">' + optionName + '</span></div>'
                                + '<textarea class="ask_textarea_content item-select textInput" placeholder="' + desVosPlaceholder + '"></textarea>'
                                + '</div>';
                        } else if (type === '3') {
                            // 当下拉框没有选项，又是必填时，改成非必填
                            if (desVos.length === 0 && isMustClass === ' isMust') {
                                isMustClass = ''
                            }
                            $.each(desVos, function (desVosIndex, desVosVal) {
                                $eachOption += '<option value=' + desVosVal.name + '>' + desVosVal.name + '</option>';
                            });
                            $el += '<div class="custom' + isMustClass + '" data-id=' + customId + '>' +
                                '<div class="letter_bar"><span class="optionName">' + optionName + '</span></div>' +
                                '<div class="f-item">' +
                                '<div class="item-text flexbox">' +
                                '<select name="" class="item-select textInput" id=' + customId + '>' +
                                '<option value="">请选择</option>' +
                                $eachOption +
                                '</select>' +
                                '</div>' +
                                '</div>' +
                                '</div>';
                        } else if (type === '4') {
                            $el += '<div class="custom' + isMustClass + '" data-id=' + customId + '>' +
                                '<div class="letter_bar"><span class="optionName">' + optionName + '</span></div>' +
                                '<input class="item-select dateInput textInput" type="text" maxlength="50" placeholder="' + desVosPlaceholder + '">' +
                                '</div>'
                        } else if (type === '5') {
                            desVos = ival.desVos;
                            // 当多选框没有选项，又是必填时，改成非必填
                            if (desVos.length === 0 && isMustClass === ' isMust') {
                                isMustClass = ''
                            }
                            $.each(desVos, function (desVosIndex, desVosVal) {
                                $eachCheckBox += '<label><input value=' + desVosVal.name + ' type="checkbox">' + desVosVal.name + '</label>';
                            });
                            $('#showMutiCheckBox .list').append('<div class="checkbox_hook none" id=' + customId + '><h1>' + optionName + '</h1>' + $eachCheckBox + '</div>');
                            $el += '<div class="custom mutiCheckBox' + isMustClass + '" data-id=' + customId + '>' +
                                '<div class="letter_bar"><span class="optionName">' + optionName + '</span></div>' +
                                '<input type="text" class="item-select textInput" readonly="readonly">' +
                                '</div>'
                        }
                    }
                }
            });
        });

        $('.form_content').append($el);

        // 如果字段必填，则添加 * 符号
        $('.form_content .isMust .letter_bar').prepend('<span style="color: red">*&nbsp;</span>');
    }

    // 日期选择
    function showDate() {
        var opt1 = {
            preset: 'date', //日期
            theme: 'android-holo light', //皮肤样式
            display: 'bottom', //显示方式
            mode: 'scroller', //日期选择模式
            dateFormat: 'mm-dd', // 上面部分的日期格式
            setText: '取消', //确认按钮名称
            cancelText: '确认',//取消按钮名籍我
            dateOrder: 'mmd D', //面板中日期排列格式
            showNow: true,
            nowText: "今天",
            stepMinute: 5,//设置分钟间隔
            dayText: '日', monthText: '月份', yearText: '年份', //面板中年月日文字
            endYear: 2050 //结束年份

        };
        opt.preset = 'date';//调用日历显示  日期
        $('.dateInput').mobiscroll(opt);//日历
        $('.timeInput').mobiscroll(opt1);//日历
    }

    // 获取自定义字段的值
    function getCustomUserList() {
        var customUserList = [];
        var $custom = $('.form_content .custom');
        $.each($custom, function (i, val) {
            customUserList.push({
                'customId': $(val).attr('data-id'),
                'content': $('.textInput', $(val)).val()
            });
        });
        var customUserListVal = JSON.stringify(customUserList);
        $('#question_detail').append($('<input type="hidden" value=' + customUserListVal + ' name="customUserList">'));
    }

    // 验证输入是否正确
    function validateInput(thisId, targetEl) {
        var val = targetEl.val();
        var testObj = {
            "weixinNum": {
                "msg": "微信号只允许字母、数字、下划线和减号，并以字母开头，四位以上",
                "testRexp": /^[a-zA-Z_][a-zA-Z\d_-]{4,}$/
            },
            "mobile": {
                "msg": "手机格式有误，请重新输入",
                "testRexp": /^\d{11}$/
            },
            "email": {
                "msg": "邮箱格式有误，请重新输入",
                "testRexp": /^\w+([-+.]\w+)*@\w+([-.]\w+)*.\w+([-.]\w+)*$/
            },
            "wxUserId": {
                "msg": "账号格式不正确",
                "testRexp": /^\w+[\w-_.@]*$/
            },
            "identity": {
                "msg": "身份证格式不正确,请重新输入",
                "testRexp": null
            }
        };
        if (val === '') return;
        if (testObj[thisId]) {
            var testRexp = testObj[thisId]["testRexp"];
            if ((thisId === "identity" && checkCard(val) !== '0') || (testRexp && !testRexp.test(val))) {
                _alert("提示", testObj[thisId]["msg"], "确认", function () {
                    targetEl.css('color', 'red');
                });
                return true;
            } else {
                targetEl.css('color', '#666');
            }
        }
    }

    function checkValidateInput($el) {
        var obj = ["weixinNum", "mobile", "email", "wxUserId", "identity"];
        var textInput = $('.textInput', $el);
        for (var j = 0, jlen = textInput.length; j < jlen; j++) {
            var thisId = textInput.eq(j).attr('data-valid');
            for (var i = 0, len = obj.length; i < len; i++) {
                var val = obj[i];
                if (thisId === val) {
                    if (validateInput(thisId, textInput.eq(j))) return false
                }
            }
        }
        return true;
    }

    // 检查必填项是否有输入
    function checkIsMustInput(el) {
        var $obj = $('.isMust', $(el));
        for (var i = 0, len = $obj.length; i < len; i++) {
            var $val = $($obj[i]);
            var name = $('.optionName', $val).text();
            if ($('.textInput', $val).val() === '' && $val.hasClass('isMust')) {
                _alert('提示', '请填写' + name, '确认');
                return false;
            }
        }
        if ($('#weixinNum').val() === '' && $('#mobile').val() === '') {
            _alert('提示', '微信号和手机号必须至少填写一个', '确认');
            return false;
        }
        return true;
    }

    function submitSucceed(type) {
        $('.inter_saoma').show();
        $(".inter_nosum").hide();
        $('#question_detail').hide();
        $('.i_liucheng1').attr('src', resourceURL + '/jsp/wap/images/tab2.png');
        if ("1" === type) {//不需要审批
            $(".inter_success1").show();
        } else if ("2" === type) {//需要审批
            $(".inter_success2").show();
        }
    }

    $('body').find("input[type='radio'][checked]").each(function () {
        radioSelect($(this).parent());
    });

    function radioSelect(obj) {
        var self = $(obj),
            context = self.parent(),
            siblings = $('.active', context);
        //判断选项是否已经满了
        siblings.removeClass('active');
        self.addClass('active');
        context.find("input[type='radio']").attr("checked", false);
        self.find("input[type='radio']").attr("checked", true);
        $('.textInput', context).val($('input:checked').val());
    }

    // 显示部门
    function showDept(list) {
        var html = "";
        if (list.length === 1) {
            $("#deptName").val(list[0].dept);
            $("#deptId").val(list[0].deptId);
            html = "<option value='" + list[0].deptId + "' selected='selected'>" + list[0].dept + "</option>";
        }
        if (list.length > 1) {
            for (var i = 0; i < list.length; i++) {
                var template = list[i];
                html += "<option value='" + template.deptId + "'>" + template.dept + "</option>";
            }
        }
        $(".selectDeptId").append(html);
        $('#deptNo').addClass('isMust');
    }

    // 显示表单数据
    function showFormData() {
        showLoading("正在加载中...");
        $.ajax({
            url: "${baseURL}/open/memberAction!getMemberSet.action",
            type: "POST",
            data: {id: FormId},
            dataType: "json",
            success: function (result) {
                if (result.code === "1999" || result.code !== "0") {
                    hideLoading();
                    _alert("提示", result.desc, "确认", function () {
                        WeixinJS.back();
                    });
                    return;
                }
                hideLoading();
                if (result.data.isShowFooter === true) {
                    showFooter(true);
                }
                FormData = result.data;
                var config = FormData.config;
                var memberBaseOptionVOs = FormData.memberBaseOptionVOs;	// 默认字段的全部信息
                var memberCustomOptionVOs = FormData.memberCustomOptionVOs;	// 自定义字段信息
                var optionVOs = FormData.optionVOs;		// 自定义字段所有信息
                var isValid = FormData.isValid;
                if (config) {
                    isDefault = config.isDefault;
                    var custom = config.custom.split(', ');   // 要显示的默认字段

                    if (isValid == "1") {		// 判断是否有效
                        $("#btn_div").show();
                        $("#btn_red").hide();
                        $("#btn_gray").hide();
                    } else {
                        $("#btn_div").hide();
                        $("#btn_red").show();
                        $("#btn_gray").show();
                    }
                    var sTime = FormData.startTime;
                    var eTime = FormData.stopTime;
                    var list = FormData.list;    // 部门列表
                    $("#startTime").html(sTime);
                    $("#stopTime").html(eTime);
                    $("#type").val(config.requestWay);

                    // 渲染表单
                    renderForm(memberBaseOptionVOs, optionVOs, memberCustomOptionVOs, custom);
                    showDate();

                    // 显示部门
                    showDept(list);
                    if ($("#deptId").val() !== "") {
                        $("#selectDeptId").val($("#deptId").val());
                    }
                    var content = config.instruction;
                    $("#detail_title").html(config.orgName);

                    var title = "邀请您加入" + config.orgName;
                    document.title = title;
                    var shareUrl = window.location.href;
                    var logo = FormData.logo;
                    if (null == logo || "" == logo) {
                        logo = resourceURL + "/themes/qw/images/logo/logo400.png";
                    }
                    content = htmlTagcodeToInput(content);

                    $("#content").html(content);

                    setDataForWeixinValue(title, logo, htmlTagReplace(content), shareUrl);
                    wxqrcode = baseURL + "/portal/errcode/errcodeAction!loadImage.action?imgUrl=" + encodeURIComponent(FormData.wxqrcode);
                    $("#inter_img").attr("src", wxqrcode);
                    $(".inter_saoma_tit").html(config.orgName);

                    if (isEdu) {     // 如果是教育版，则渲染添加孩子信息页面
                        renderEduForm(config);
                    }
                }
            },
            error: function () {
                hideLoading();
                _alert("提示", internetErrorMsg, "确认", function () {
                    restoreSubmit();
                });
            }
        });
    }

    // 提交表单数据
    function commitForm(CommitFormUrl) {
        if (!checkValidateInput($('#question_detail'))) return;
        if (!checkIsMustInput('#question_detail')) return;
        getCustomUserList();
        $.ajax({
            url: CommitFormUrl,
            type: "POST",
            data: $("#question_detail").serialize(),
            dataType: "json",
            success: function (result) {
                if (result.code === "0") {
                    isClick = true;
                    var type = $("#type").val();
                    submitSucceed(type);
                } else {
                    $('#question_detail').show();
                    $('.inter_saoma').hide();
                    $('.i_liucheng1').attr('src', resourceURL + '/jsp/wap/images/tab.png');
                    if (result.code === "2011" || result.code === "1999") {
                        _alert_weui(wxqrcode, result.desc, "好的", function () {
                            restoreSubmit();
                        });
                    } else {
                        _alert("提示", result.desc, "确认", function () {
                            restoreSubmit();
                        });
                    }
                }
            },
            error: function () {
                hideLoading();
                _alert("提示", internetErrorMsg, "确认", function () {
                    restoreSubmit();
                });
            }
        });
    }

// =================================================================================== 教育版 start
    // 点击按钮 下一步填写孩子信息
    $('#showAndHide').on('click', function () {
        if (!checkValidateInput($('#question_detail'))) return;
        if (!checkIsMustInput('#question_detail')) return;
        if (!checkParent()) return;
        $("#btn_divChildren").hide();
        $("#btn_div").show();
        $("#question_detail").hide();
        $("#question_detail2").show();
    });

    $('.nextHook', "#childrenSubmit").on('click', function () {
        commitChildrenAndParents();
    });

    $('.returnHook', '#childrenSubmit').on('click', function () {
        $("#btn_divChildren").show();
        $("#btn_div").hide();
        $("#question_detail").show();
        $("#question_detail2").hide();
    });

    // 当是教育版，输入孩子信息时
    $('.childrenDivList').on('change', '.textInput', function () {
        validateInput($(this).attr('data-valid'), $(this));
    });

    // 添加另一个孩子信息
    $('#addChildren').on('click', function () {
        if (!checkValidateInput($('#question_detail2'))) return;
        if (!checkIsMustInput('.childrenList.active')) return;

        // 当开启家长身份验证时，验证孩子的姓名，班级，注册的手机号
        if (!checkChildren()) return;
        addNextChildren();
    });

    // 展示隐藏的 孩子信息
    $('.childrenDivList').on('click', '.childrenListTitle', function () {
        var $showObj = $(this).siblings(".childrenListContent");
        $showObj.toggleClass('none');
        $(this).find(".icon-slide").toggleClass("active");
    });

    // 删除孩子信息
    $('.childrenDivList').on('click', '.delete', function (e) {
        e.stopPropagation();
        deleteChildrenList(this);
    });

    // 删除孩子信息
    function deleteChildrenList(obj) {
        // 如果 #question_detail2 孩子信息表单 childrenList.length = 1 ,
        // 则 清空表单数据
        // 如果 childrenList.length > 1 , 则直接删除 remove()
        var currentChildrenList, id;
        var childrenListLen = $('.childrenList').length;
        if (childrenListLen === 1) {
            $('.childrenListTitle').addClass('none');
            $('.childrenListContent').removeClass('none');
            $('#question_detail2')[0].reset();
            $('.textInput', '#question_detail2').val('');
        } else if (childrenListLen > 1) {
            currentChildrenList = $(obj).parents('.childrenList');
            currentChildrenList.remove();
            resetIndex(childrenListLen);
        }
        function resetIndex(childrenListLen) {
            var i;
            for (i = 0; i < childrenListLen; i++) {
                $('.childrenListTitle').each(function (j, jval) {
                    $('.index', $(jval)).text(j + 1);
                    $(jval).parents('.childrenList').attr('id', j);
                })
            }
        }
    }

    // 显示教育版数据
    function renderEduForm(config) {
        var isAllow = config.isAllow;
        var targetType = config.targetType;
        var childrenCustom = config.childrenCustom.split(', ');
        var dataId;
        if ("1" === targetType) {      // 【教育版】用户类型
            $("#targetType").html("家长");
        } else if ("2" === targetType) {
            $("#targetType").html("教师/职工");
        } else if ("3" === targetType) {
            $("#targetType").html("学生");
        }
        if ("1" === targetType && "1" === isAllow) {      // 教育版 是否允许家长填写孩子信息
            $("#btn_div").hide();
            $("#btn_divChildren").show();
        } else {
            $("#btn_div").show();
        }
        $.each(childrenCustom, function (j, jval) {
            $('.childrenDiv', '.childrenList').each(function (i, val) {
                dataId = $(val).attr('data-id');
                if (jval === dataId) {
                    $(val).removeClass('none');
                }
            });
        });
        $('.childrenList').removeClass('none').addClass('active');
    }

    //增加另一个孩子
    function addNextChildren() {
        var $childrenList = $('.childrenList');
        $childrenList.removeClass('active');
        $('.childrenListTitle').removeClass('none');
        $('.childrenListContent').addClass('none');
        var num = Number($('.index', $childrenList.last()).text());

        var childrenList = $childrenList.eq(0).clone();
        $('.textInput', childrenList).val('');
        $('.dateInput', childrenList).removeAttr('id');
        childrenList.removeClass('none').addClass('active').attr('id', num).appendTo('.childrenDivList');
        $('.sexHook .textInput', childrenList).val($('.sexHook input:checked', childrenList).val());
        $('.childrenListContent', childrenList).removeClass('none');
        $('.f-item-title .index', childrenList).text(num + 1);
        $(".icon-slide").toggleClass("active");     // todo:

        showDate();
    }

    // 验证父母是否存在通讯录里
    function checkParent() {
        var flag = true;
        $.ajax({
            url: baseURL + "/open/memberAction!checkUser.action",
            async: false,
            type: "POST",
            data: {
                'id': $("#id").val(),
                'weixinNum': $("#weixinNum").val(),
                'mobile': $("#mobile").val(),
                'email': $("#email").val(),
                'wxUserId': $("#wxUserId").val()
            },
            dataType: "json",
            success: function (result) {
                if (result.code === "0") {

                } else if (result.code === "2011" || result.code === "1999") {
                    _alert_weui(wxqrcode, result.desc, "好的", function () {
                        restoreSubmit();
                    });
                    flag = false;
                } else {
                    _alert("提示", result.desc, "确认", function () {
                        restoreSubmit();
                    });
                    flag = false;
                }
            },
            error: function () {
                _alert("提示", result.desc, "确认", function () {
                    restoreSubmit();
                });
                return false;
            }
        });
        return flag;
    }

    // 检查孩子是否存在通讯录【包含姓名、手机号、班级】
    function checkChildren() {
        var childrenList = $('.childrenList.active');
        var personName = $(".childrenName .textInput", childrenList).val();
        var classId = $(".childrenClass .textInput", childrenList).val();
        var registerPhone = $(".childrenRegisterTel .textInput", childrenList).val();
        var flag = true;
        if (FormData.config.isCheck === "1" && personName !== '' && classId !== '' && registerPhone !== '') {
            $.ajax({
                url: baseURL + "/open/memberAction!checkStudent.action",
                async: false,
                type: "POST",
                data: {'personName': personName, 'classId': classId, 'registerPhone': registerPhone, 'id': FormId},
                dataType: "json",
                success: function (result) {
                    if (result.code === "0") {
                        if ("1" !== result.data.info) {
                            _alert("提示", personName + ' ' + result.data.info, "确认");
                            childrenList.addClass('noFind');    // 如果不存在，则在当前做标记
                            flag = false;
                        } else {
                            childrenList.removeClass('noFind');
                        }
                    } else {
                        _alert("提示", result.desc, "确认", function () {
                            restoreSubmit();
                        });
                        flag = false;
                    }
                },
                error: function () {
                    _alert("提示", internetErrorMsg, "确认", function () {
                        restoreSubmit();
                    });
                }
            });
        }
        return flag;
    }


    function getChildrenData() {
        var listJson = {"list": []};  // list索引为 chilidrenList 的 id 值
        $('.childrenList').removeClass('active');
        $('.childrenList').each(function (i, val) {
            var data = {};
            var $childrenList = $(val);
            var id = $childrenList.attr('id');
            $childrenList.addClass('active');
            $('.childrenDiv', $childrenList).each(function (j, jval) {
                var dataId = $(jval).attr('data-id');
                // 出参入参不统一，转换参数
                if (dataId === 'card') dataId = 'identity';
                if (dataId === 'year') dataId = 'birthday';
                if (dataId === 'tel') dataId = 'mobile';
                if (dataId === 'wx') dataId = 'weixinNum';
                var inputVal = $('.textInput', $(jval)).val();
                if (dataId === 'birthday' && inputVal === '') {
                    inputVal = null;
                }
                data[dataId] = inputVal;
                if (dataId === 'selectDeptId' || dataId === 'personName' || dataId === 'enrolTel') {
                    if (inputVal !== '' && !checkChildren()) return;
                }
                if (dataId === 'selectDeptId') {
                    data['selectDeptId'] = inputVal;
                    data['selectDept'] = $('option:selected', $(jval)).text();
                }
            });
            var currentList = listJson["list"][id];
            if (currentList) {
                $.extend(currentList, data);
            } else {
                listJson["list"].push(data);
            }

        });
        return listJson;
    }

    //提交孩子和家长信息
    function commitChildrenAndParents() {
        if (!checkValidateInput($('#question_detail2'))) return;
        if (!checkIsMustInput('.childrenList')) return;
        var text;
        if ($('.noFind').length) {
            text = $('.childrenName .textInput', $('.noFind').eq(0)).val();
            _alert('提示', '您输入的信息 ' + text + ' 不在系统中，请重新输入', '确认');
            return;
        }
        $("#listJson").val(JSON.stringify(getChildrenData()));
        getCustomUserList();
        $.ajax({
            url: "${baseURL}/open/memberAction!ajaxAddChildrenAndParents.action",
            type: "POST",
            data: $("#question_detail").serialize(),
            dataType: "json",
            success: function (result) {
                if (result.code === "0") {
                    $("#question_detail2").hide();
                    $("#addChildren").hide();
                    $("#childrenSubmit").hide();
                    isClick = true;
                    var type = $("#type").val();
                    submitSucceed(type);
                } else {
                    if (result.code === "2011" || result.code === "1999") {
                        _alert_weui(wxqrcode, result.desc, "好的", function () {
                            restoreSubmit();
                        });
                    } else {
                        _alert("提示", result.desc, "确认", function () {
                            restoreSubmit();
                        });
                    }

                }
            },
            error: function () {
                hideLoading();
                _alert("提示", internetErrorMsg, "确认", function () {
                    restoreSubmit();
                });
            }
        });
    }

// ================================================================================== 教育版 end
});


