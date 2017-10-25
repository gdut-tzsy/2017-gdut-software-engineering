'use strict';
wx.config({
	debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
	appId: 'wx2958809f12298333', // 必填，公众号的唯一标识
	timestamp: 1483598181, // 必填，生成签名的时间戳
	nonceStr: 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', // 必填，生成签名的随机串
	signature: 'fdd56256e6d4a3956da47bbcedf38d1b7f29bc1b',// 必填，签名，见附录1
	jsApiList: ['onMenuShareTimeline','onMenuShareAppMessage','onMenuShareQQ','onMenuShareWeibo','onMenuShareQZone'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});
var wxData = {
	imgUrl: 'http://wbg.do1.com.cn/WXShare/WXShare.png',
	link: window.location.href,
	desc: document.getElementsByName('description')[0].content,
	title: document.title,
	share:function(){
		//获取“分享到朋友圈”按钮点击状态及自定义分享内容接口
		wx.onMenuShareTimeline({
			title: wxData.title,
			link: wxData.link,
			imgUrl: wxData.imgUrl,
			success: function() {
			},
			cancel: function() {
			}
		});
		//获取“分享给朋友”按钮点击状态及自定义分享内容接口
		wx.onMenuShareAppMessage({
			title: wxData.title,
			desc: wxData.desc,
			link: wxData.link,
			imgUrl: wxData.imgUrl,
			type: '', // 分享类型,music、video或link，不填默认为link
			dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
			success: function() {
			},
			cancel: function() {

			}
		});
		//获取“分享到QQ”按钮点击状态及自定义分享内容接口
		wx.onMenuShareQQ({
		    title: wxData.title, // 分享标题
		    desc: wxData.desc, // 分享描述
		    link: wxData.link, // 分享链接
		    imgUrl: wxData.imgUrl, // 分享图标
		    success: function () {
		       // 用户确认分享后执行的回调函数
		    },
		    cancel: function () {
		       // 用户取消分享后执行的回调函数
		    }
		});
		//获取“分享到腾讯微博”按钮点击状态及自定义分享内容接口
		wx.onMenuShareWeibo({
		    title: wxData.title, // 分享标题
		    desc: wxData.desc, // 分享描述
		    link: wxData.link, // 分享链接
		    imgUrl: wxData.imgUrl, // 分享图标
		    success: function () {
		       // 用户确认分享后执行的回调函数
		    },
		    cancel: function () {
		        // 用户取消分享后执行的回调函数
		    }
		});
		//获取“分享到QQ空间”按钮点击状态及自定义分享内容接口
		wx.onMenuShareQZone({
		    title: wxData.title, // 分享标题
		    desc: wxData.desc, // 分享描述
		    link: wxData.link, // 分享链接
		    imgUrl: wxData.imgUrl, // 分享图标
		    success: function () {
		       // 用户确认分享后执行的回调函数
		    },
		    cancel: function () {
		        // 用户取消分享后执行的回调函数
		    }
		});
	},
};

wx.ready(function() {
	wxData.share();
});
/*wx.error(function(res) {
	alert(JSON.stringify(res));
});*/
