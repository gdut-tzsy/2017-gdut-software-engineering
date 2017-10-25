$(function(){

/*弹窗播放视频*/
var video_mask = $('#mask'),
    video_dialog = $('#video_dialog'),
    video = $('#js-video'),
    video_item = $('#video_item');
$(document).on('click', '#video_item',function () {
    centerDialog(video_dialog);
    var $this = $(this);
    video_mask.show();
    video_dialog.show().addClass('scaleIn');
    video.attr('src', $this.attr('data-video'));
    console.log($this.attr('data-video'));
})

$(window).resize(function () {
    setTimeout(function () {
        centerDialog(video_dialog)
    }, 60)
});

//关闭弹窗视频
video_mask.on('click', function () {
    video_mask.hide();
    video_dialog.hide().removeClass('scaleIn');
    video[0].pause();
})

//垂直居中方法
function centerDialog($item) {
    var itemW = $item.outerWidth(),
        itemH = $item.outerHeight(),
        winW = $(window).width(),
        winH = $(window).height(),
        left = (winW - itemW) / 2,
        top = (winH - itemH) / 2;
    //alert(left);
    $item.css('left', left);
    $item.css('top', top);
}
/*弹窗播放视频*/

})