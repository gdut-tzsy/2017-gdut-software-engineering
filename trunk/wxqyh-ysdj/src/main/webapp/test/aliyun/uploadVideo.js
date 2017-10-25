var accessKeyId;
var accessKeySecret;
var secretToken;
var expireTime;
var videoId;
var upLoadFileName;
var upDataSize;
var UploadDataTimers;
var resumeWithTokenTimers;
var uploader = new VODUpload({
    // 文件上传失败
    'onUploadFailed': function (uploadInfo, code, message) {
        clearTimeout(UploadDataTimers);
        log("onUploadFailed: file:" + uploadInfo.file.name + ",code:" + code + ", message:" + message);
    },
    // 文件上传完成
    'onUploadSucceed': function (uploadInfo) {
        clearTimeout(UploadDataTimers);
        log("onUploadSucceed: " + uploadInfo.file.name + ", oss:" + "http://" + uploadInfo.bucket + "." + uploadInfo.endpoint.substring(7) + "/" + uploadInfo.object);
    },
    // 文件上传进度
    'onUploadProgress': function (uploadInfo, totalSize, uploadedSize) {
        upDataSize = uploadedSize;
        log("onUploadProgress:file:" + uploadInfo.file.name + ", fileSize:" + totalSize + ", percent:" + Math.ceil(uploadedSize * 100 / totalSize) + "%");
    },
    // STS临时账号会过期，过期时触发函数
    'onUploadTokenExpired': function () {
        log("onUploadTokenExpired");
        resumeWithToken();
        // 实现时，从新获取STS临时账号用于恢复上传
        // uploader.resumeUploadWithToken(accessKeyId, accessKeySecret, secretToken, expireTime);
    }
});

function uploadDataRecord(){
    $.ajax({
        url : baseURL+"/enclosure/enclosureUpload/enclosureUploadAction!uploadDataRecord.action",
        type : "post",
        data:{"userData":videoId,"fileName":upLoadFileName,"uploadData":upDataSize},
        dataType : "json",
        success : function(result) {
            if (result.code == "0") {

            }
        },
        error : function() {

        }
    });
}
function start() {
    $.ajax({
        url : baseURL+"/enclosure/enclosureUpload/enclosureUploadAction!uploadReady.action",
        type : "post",
        data:{"userData":videoId,"fileName":upLoadFileName},
        dataType : "json",
        success : function(result) {
            if (result.code == "0") {
                accessKeyId = result.data.accessKeyId;
                accessKeySecret = result.data.accessKeySecret;
                secretToken = result.data.secretToken;
                expireTime = result.data.expireTime;
                if (secretToken && secretToken.length > 0 && expireTime.length > 0) {
                    // STS方式，安全但是较为复杂，建议生产环境下使用。
                    // 临时账号过期时，在onUploadTokenExpired事件中，用resumeWithToken更新临时账号，上传会续传。
                    uploader.init(accessKeyId, accessKeySecret, secretToken, expireTime);
                } else {
                    // AK方式，简单但是不够安全，建议测试环境下使用。
                    uploader.init(accessKeyId, accessKeySecret);
                }
                log("start upload.");
                //isFirstAction=false;
                uploader.startUpload();
                UploadDataTimers = setInterval("uploadDataRecord();",3000);
                //resumeWithTokenTimers = setTimeout("beforeTokenFail();",(durationSecond-60)*1000);
            }
        },
        error : function() {
            _alert("错误提示", "系统繁忙！");
        }
    });
}

function stop() {
    log("stop upload.");
    uploader.stopUpload();
    clearTimeout(UploadDataTimers);
}

function resumeWithToken() {
    log("resume upload with token.");
    $.ajax({
        url : baseURL+"/enclosure/enclosureUpload/enclosureUploadAction!tokenExpired.action",
        type : "post",
        dataType : "json",
        success : function(result) {
            if (result.code == "0") {
                accessKeyId = result.data.accessKeyId;
                accessKeySecret = result.data.accessKeySecret;
                secretToken = result.data.secretToken;
                expireTime = result.data.expireTime;
                if (secretToken && secretToken.length > 0 && expireTime.length > 0) {
                    uploader.resumeUploadWithToken(accessKeyId, accessKeySecret, secretToken, expireTime);
                }
            }else if(result.code == "441004"){
                stop();
                _alert("错误提示", "上传数据出错");
            }
        },
        error : function() {
            _alert("错误提示", "系统繁忙！");
        }
    });
}

function beforeTokenFail(){
    log("beforeTokenFail.");
    stop();
    start();
}

function clearList() {
    log("clean upload list.");
    uploader.cleanList();
}

function getList() {
    log("get upload list.");
    var list = uploader.listFiles();
    for (var i = 0; i < list.length; i++) {
        log("file:" + list[i].file.name + ", status:" + list[i].state + ", endpoint:" + list[i].endpoint + ", bucket:" + list[i].bucket + ", object:" + list[i].object);
    }
}

function deleteFile() {
    if (document.getElementById("deleteIndex").value) {
        var index = document.getElementById("deleteIndex").value
        log("delete file index:" + index);
        uploader.deleteFile(index);
    }
}

function cancelFile() {
    if (document.getElementById("cancelIndex").value) {
        var index = document.getElementById("cancelIndex").value
        log("cancel file index:" + index);
        uploader.cancelFile(index);
    }
}

function resumeFile() {
    if (document.getElementById("resumeIndex").value) {
        var index = document.getElementById("resumeIndex").value
        log("resume file index:" + index);
        uploader.resumeFile(index);
    }
}

function clearLog() {
    textarea.options.length = 0;
}

function log(value) {
    if (!value) {
        return;
    }

    var len = textarea.options.length;
    if (len > 0 && textarea.options[len - 1].value.substring(0, 40) == value.substring(0, 40)) {
        textarea.remove(len - 1);
    } else if (len > 25) {
        textarea.remove(0);
    }

    var option = document.createElement("option");
    option.value = value, option.innerHTML = value;
    textarea.appendChild(option);
}

function uuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
}



function testDel(){
    var mediaId = $("#mediaId").val();
    $.ajax({
        url : baseURL+"/enclosure/enclosureAction!testDel.action",
        type : "post",
        data:{"mediaId":mediaId},
        dataType : "json",
        success : function(result) {
            if (result.code == "0") {
                _alert("提示", "删除成功！");
            }
        },
        error : function() {
            _alert("错误提示", "系统繁忙！");
        }
    });
}

