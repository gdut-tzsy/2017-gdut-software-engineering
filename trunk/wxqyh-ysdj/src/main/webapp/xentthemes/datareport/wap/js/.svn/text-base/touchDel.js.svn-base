/*手势滑动删除 start*/
   //设置css
  function _setTouchCss(){
    $('.js-item').css({
        'width':$('#section_card').width() - 20,
    });
    $('#section_card li').css({
        'width':$('.js-item').outerWidth() + $('.touch_item').width(),
        'transition':'.3s ease',
        'webkitTransition':'.3s ease',
    });
  }

  //滑动
  function Slider(elm){
      this.touch;
      this.startX;
      this.startY;
      this.moveX;
      this.moveY;
      this.touchX;
      this.touchY;
      this.x;
      this.li = document.querySelectorAll(elm);
      _this = this;
      for (var i = 0; i < this.li.length; i++) {
          this.li[i].addEventListener('touchstart',_this.touchstart,false);
          this.li[i].addEventListener('touchmove',_this.touchmove,false);
          this.li[i].addEventListener('touchend',_this.touchend,false);
      };
  }

  Slider.prototype.touchstart = function(e){
      console.log('touchstart');
      _this.touch = e.touches[0];
      _this.startX = _this.touch.pageX;
      _this.startY = _this.touch.pageY;
      $('#section_card li').css({
        'transform':'translateX(0)',
        'webkitTransform':'translateX(0)',
      });
  }

  Slider.prototype.touchmove = function(e){
      console.log('move');
          _this.touch = e.touches[0];
          _this.moveX = _this.touch.pageX;
          _this.moveY = _this.touch.pageY;
          _this.touchX = _this.moveX - _this.startX;
        if (Math.abs(_this.moveX - _this.startX) > Math.abs(_this.moveY - _this.startY)){
            e.preventDefault();
            $(this).css({
              'transform':'translateX('+_this.touchX+'px)',
              'webkitTransform':'translateX('+_this.touchX+'px)'
              });
        }else {
          return;
        }
  }

  Slider.prototype.touchend = function(e){
    _this.x = Math.abs(_this.moveX - _this.startX);
    if (_this.x > 30 && (_this.moveX - _this.startX) < 0){
      console.log('end');
      $(this).css({
          'transform':'translateX(-'+$('.touch_item').width()+'px)',
          'webkitTransform':'translateX(-'+$('.touch_item').width()+'px)'
      });
    }else if((_this.moveX - _this.startX) > 10){
      $(this).css({
          'transform':'translateX(0)',
          'webkitTransform':'translateX(0)'
      });
    }
  }

  //删除
  $('.touch_del').on('click',function(e){
      e.stopPropagation();
      e.preventDefault();
      // $(this).parents('li').remove();
      delItem($(this).parents('li'),5,6);
  })

  function delItem($item,a,b){
      console.log(a,b);
      $item.remove();
  }
  /*手势滑动删除 end*/

