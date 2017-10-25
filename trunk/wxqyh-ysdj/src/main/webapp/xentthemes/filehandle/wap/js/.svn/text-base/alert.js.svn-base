/* 
* @Author: Marte
* @Date:   2017-06-14 17:09:43
* @Last Modified by:   Marte
* @Last Modified time: 2017-06-22 09:26:36
*/
/**确认框弹出层 
     * 示例
    //_alert('自定义标题','内容内容');
    //_alert('自定义标题','内容内容'，function(){console.log('确认回调函数')});
    //_alert('自定义标题','内容内容',{'ok':function(){console.log('确认回调函数')},'fail':function(){console.log('取消回调函数')}})//如果没有buttonText参数,默认 取消|确认
    //_alert('自定义标题','内容内容',"确认",{'ok':function(){console.log('确认回调函数')},'fail':function(){console.log('取消回调函数')}})
    //_alert('自定义标题','内容内容',"取消|确认",{'ok':function(){console.log('确认回调函数')},'fail':function(){console.log('取消回调函数')}})
*/
function alert_(title,text,buttonText,callback){
    var title=title||"提示";
    if(typeof(buttonText)=='string'&&buttonText.split("|").length==1){
        buttonText1=buttonText.split("|")[0];
        var buttonHtml='<div class="weui_dialog_ft1">'
              +'<a href="javascript:;" class="weui_btn_dialog1 primary1">'+buttonText1+'</a>'
              +'</div>';
        var boxClass="weui_dialog_alert1 custom_dialog1";
    }else {
        if(typeof(buttonText)=='string'&&buttonText.split("|").length>=2){
            var buttonText1=buttonText.split("|")[0];
            var buttonText2=buttonText.split("|")[1];
        }else if(typeof(buttonText)=='function'||typeof(buttonText)=='object'&&buttonText['ok']||typeof(buttonText)=='object'&&buttonText['fail']){
            callback=buttonText;
            var buttonText1="取消";
            var buttonText2="确认";
        }else{
            var buttonText1="取消";
            var buttonText2="确认";
        }
        var buttonHtml='<div class="weui_dialog_ft1">'
            +'<a href="javascript:;" class="weui_btn_dialog1 default1">'+buttonText1+'</a>'
            +'<a href="javascript:;" class="weui_btn_dialog1 primary1">'+buttonText2+'</a>'
            +'</div>';
        var boxClass="weui_dialog_confirm1 custom_dialog1";
    }
    var html='<div class="'+boxClass+'">'
            +'<div class="weui_mask1"></div>'
            +'<div class="weui_dialog1">'
                +'<div class="weui_dialog_hd1"><strong class="weui_dialog_title1">'+title+'</strong></div>'
                +'<div class="weui_dialog_bd1">'+text+'</div>'
                +buttonHtml
           +' </div>'
       +' </div>';
    $('body').append(html);
   
    //移除dialog
    var removeDialog = function(){
        setTimeout(function(){
            $('.custom_dialog1').remove();
        },200)
    }
    //class状态切换
    var _toggleClass = function(active){
        if (active == "in") {
            setTimeout(function(){
                $('.weui_dialog1').addClass('pop_in')
            },60)
         }else if(active == "out"){
            $('.weui_dialog1').removeClass('pop_in').addClass('pop_out');
        };
    }
    _toggleClass("in");
    $('.weui_btn_dialog1.primary1').off('click').on('click',function(){//确认
      $('.weui_btn_dialog1.primary1').off('click');
        if(callback&&typeof(callback)=='function'){
           callback(); 
        }else if(callback&&typeof(callback)=='object'&&callback['ok']){
            callback['ok']();
        }
        _toggleClass("out");
        removeDialog();
    });
    $('.weui_btn_dialog1.default1').off('click').on('click',function(){//取消
        if(callback&&typeof(callback)=='object'&&callback['fail']){
            callback['fail']();
        }
        _toggleClass("out");
        removeDialog();
    });    
}; 

