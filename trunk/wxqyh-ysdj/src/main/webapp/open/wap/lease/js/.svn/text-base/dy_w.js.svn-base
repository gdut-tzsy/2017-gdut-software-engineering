function setHeight(elm,elm2){
    $(elm).height($(elm2).outerHeight());
}

function addOn(elm){
    if($(elm).hasClass('on')){
        $(elm).removeClass('on');
    }else{
        $(elm).addClass('on')
    }
}

function showDialog(flag,el){
   if(flag == true){
       $(el).show();
   }else if(flag == false){
       $(el).hide();
   }
}
//删除li
function delLi(elm){
  $(elm).parents('li').remove();
}
//添加地址
function addlocation(){
   var str = '<li class="record_item flexbox">'
       str+=    '<div class="flexCenter"><span class="tip_box grey">收</span></div>'
       str+=    '<div class="flexItem f15 pl10">'
       str+=      '<div class="c666">收件人：<span class="c999">请选择</span></div>'
       str+=      '<div class="c666 f13 pt5">地址：<span class="c999">请选择</span></div>'
       str+=    '</div>'
       str+=    '<div class="flexCenter pl5" onclick="delLi(this)"><div class="delli_btn">删除</div></div>'
       str+= '</li>'
   $('#js_location').append(str)
}


/**
  * JS链接跳转
*/
function hrefTo(url){
  location.href = url
}
/**
    *底部弹出层 
    *Power by smallsea
    @param flag 是否显示弹窗
    @param tt 弹窗标题
    @param ifm 弹窗页面的选择器
    @method callback 回调方法
  *============用法示例============*
  //打开的时候
  viewModal(true,'这里是弹窗标题','#modal',{
    open:function(){
      console.log('打开了弹窗');
    }
  });
  //关闭的时候
  viewModal(false,'','#modal',{
    close:function(){
      console.log('关闭了弹窗');
    }
  });
 */
function viewModal(flag,tt,ifm,callback){
  var t;
    if(flag){
      clearTimeout(t);
      if ($('#mask').length) {
          $('#mask').show();
      }else{
        $('body').append('<div id="mask"></div>');
      };
        $('[data-role="parent-page"]').hide();
        $(ifm).show(0,function(){
          $(ifm).removeClass('modal_out');
          $(ifm).addClass('modal-in');
        })
        $('.js-title').text(tt);
      if (callback && typeof(callback) === "object" && callback['open']) {
        callback['open']();
      };
    }else if(flag == false){
      $('[data-role="parent-page"]').show();
      $(ifm).removeClass('modal-in');
      $(ifm).addClass('modal_out');
      t = setTimeout(function(){
          $(ifm).hide()
      },300)
      $('#mask').hide();
      if (callback && typeof(callback) === "object" && callback['close']) {
          callback['close']();
      };
    }
}

/*重置选择框*/
function resetPage(elm){
  $(elm).find('input').val('');
  $(elm).find('.checked_box span').removeClass('on')
}

/*添加商品*/
function addShop(elm){
  var num = $(elm).siblings('.num_ipt').val();
  $(elm).siblings().removeClass('filter');
  $(elm).siblings('.num_ipt').val(++num);
}
/*减少商品*/
function reduceShop(elm){
  var num = $(elm).siblings('.num_ipt').val();
  if(num > 0){
    $(elm).siblings('.num_ipt').val(--num);
    if(num==0){
      $(elm).addClass('filter');
    }else{
      $(elm).parent().removeClass('filter');
    }
  }
}

/**
  * 滚动加载（需要注意的是，有滚动条才能滚动，如果页面禁止了默认滑动，则滚动不了）
  * @param wrap要滚动的元素
  * @param callback滚动到底的回调函数
*/
function scrollLoadData(wrap,callback){
  var wrap = $(wrap);
  var nScrollHight = 0; //滚动距离总长(注意不是滚动条的长度)
  var nScrollTop = 0;   //滚动到的当前位置
  var nDivHight = wrap.height();
  wrap.scroll(function(){
    nScrollHight = $(this)[0].scrollHeight;
    nScrollTop = $(this)[0].scrollTop;
    if(nScrollTop + nDivHight >= nScrollHight){
      if(callback && typeof callback === "function"){
          console.log("滚动条到底部了")
          callback();
      } 
    }else{
      console.log('nDivHight滚动元素的高度是:'+nDivHight
        +'\n'+'nScrollTop滚动的距离是'+nScrollTop
        +'\n'+'nScrollHight滚动条的高度是'+nScrollHight);
    }
  });
}


/*微信端大图预览*/
//onImgLoad(img) 数据成功获得之后调用
var imgsSrc = [];  
function reviewImage(src) {  
    if (typeof window.WeixinJSBridge != 'undefined') {  
        WeixinJSBridge.invoke('imagePreview', {  
            'current' : src,  
            'urls' : imgsSrc  
        });  
    }  
}  
function onImgLoad(img) {  
    var imgs = document.querySelectorAll(img);  
    for( var i=0,l=imgs.length; i<l; i++ ){  
        var img = imgs.item(i);  
        var src = img.getAttribute('data-src') || img.getAttribute('src');
        console.log('\n'+src);  //src需要为绝对路径
        if( src ){  
            imgsSrc.push(src);  
            (function(src){  
                if (img.addEventListener){  
                    img.addEventListener('click', function(){  
                        reviewImage(src);  
                    });  
                }else if(img.attachEvent){  
                    img.attachEvent('click', function(){  
                        reviewImage(src);  
                    });  
                }  
            })(src);  
        }  
    }  
}
// onImgLoad('.show_list img');
// var args=new Array("swiper-3.3.1.jquery.min.js");
// importDoc(args);
// //函数可以批量引入多个js、css
// function importDoc(arguments){ 
//     for( var i=0; i<arguments.length; i++ )
//     { 
//         var file = arguments[i]; 
//         if (file.match(/.*\.js$/)) 
//            document.write('<script type="text/javascript" src="' + file + '"></script>'); 
//         else if(file.match(/.*\.css$/))
//            document.write('<link rel="stylesheet" href="'+file+'" type="text/css" />');
//     } 
// }
/*微信端大图预览*/
//onImgLoad(img) 数据成功获得之后调用
// $('body').on('click','img[data-preview="1"]',function(){
//     var array = [];
//     var init = function(){
//       for(var i = 0 ,len= $('body').find('img[data-preview="1"]').length;i<len;i++){
//           array[i]=$('body').find('img[data-preview="1"]').eq(i).attr('src')
//       }
//     }
//     init();
//     console.log(array)
//     var index = $(this).index();
//     showImage(array);
//  })

//  /**
//   * show original image
//   */
// function showImage(array) {
//   var str ='<div class="overlay" id="overlayImage"></div>'
//       str+='<div class="commentBtnBoxBg"></div>'
//       str+='<div class="img_preview swiper-container swiper-container-horizontal" id="loading_image_div">'
//       str+= '<div class="inner_img_preview swiper-wrapper">'
      
//       for(var i = 0,len=$(array).length;i<len;i++){
//           str+=    '<div class="swiper-slide"><img  id="src_image"  src="'+array[i]+'" alt=""/></div>'
//       }
//       str+= '</div>'
//       str+= '<div class="swiper-pagination"></div>'
//       str+='</div>'
  
//   $('body').append(str);

//    var previewImgSwiper = new Swiper('#loading_image_div',{
//       pagination : '.swiper-pagination',
//   });

//   // $('#src_image,#overlayImage').on('click',function(){
//   //   $('#overlayImage').nextAll().remove();
//   //   $('#overlayImage').remove();
//   // })
// }