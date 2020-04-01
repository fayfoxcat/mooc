$(function () {
    //登录状态
    getuserstate();

    //获取视频地址
    getVideo();

    /*用户登录状态*/
    function getuserstate(){
        var UserStateUrl = '/mooc/useroperator/loginstate';
        $.post(UserStateUrl,function(data) {
            if(data.success){
                var userimg = '';
                /*导航栏填充*/
                userimg +='<img src="'+data.userinfo.header+'" alt="个人中心" class="rounded-circle personal-img"/>';
                $('.user-img').html(userimg);
            }
        });
    }

    //返回前课程详情页
    $(".goback").click(function () {
        window.history.back();
    });

    //获取video元素
    var video = document.getElementById('video');

    /*进度条函数*/
    function progress(current) {
        var currentColor;
        //获得现时长
        var nowTime=video.currentTime;
        //获得总时长
        var maxDuration=video.duration;
        //获得现px
        if (current == 0){
            current=($('.play-progress').width()/maxDuration)*nowTime;
        }
        //修改长度和颜色
        $('.play-full').css( {
            "border-radius":'3px',
            'width': current + "px",
            'height':"6px",
            'background-color':"white",
        })
    }

    /*计算用户点击位置*/
    var current = 0;
    $(".play-progress").click(function(e){
        current=e.pageX-$(this).offset().left;
    })

    /*播放进度条实现*/
    setInterval(function () {
        progress(current);
        if (current !=0){
            video.currentTime = current/($('.play-progress').width()/video.duration);
            current = 0;
        }

    },300)

    //设置播放暂停
    $("#p-play").click(function () {
        if (video.paused==true){
            video.play();
            $("#p-play").toggleClass("icon-play-fill icon-pause-fill");
        }else{
            video.pause();
            $("#p-play").toggleClass("icon-pause-fill icon-play-fill");
        }
    })

    //播放时常与总时常
    setInterval(function () {
        $(".course-time").text(formatdate(video.currentTime)+'/'+formatdate(video.duration));
    },1000);

    //格式化时间函数
    function formatdate(time){
        var int_minute = Math.floor(time/60)
        time = time - int_minute * 60;
        var int_second = Math.floor(time)
        //不足双位数，补0
        if(int_minute < 10){
            int_minute = "0" + int_minute;
        }
        if(int_second < 10){
            int_second = "0" + int_second;
        }
        return int_minute+':'+int_second;
    }

    /*音量进度条实现*/
    video.volume = 0.5
    $(".volume-progress").click(function (e) {
        currentV = e.pageX-$(this).offset().left;
        //更改音量
        video.volume = currentV/$('.volume-progress').width();
        //修改长度和颜色
        $('.volume-full').css( {
            "border-radius":'2px',
            'width': currentV + "px",
            'height':"4px",
            'background-color':"#59a869",
        })
    })

    /*静音*/
    $('#v-ico').click(function () {
        if(video.volume!=0){
            $('#v-ico').toggleClass("icon-volume-up icon-volume-mute");
            video.volume=0;
        }else{
            $('#v-ico').toggleClass("icon-volume-mute icon-volume-up");
            video.volume=0.5
        }
    })

    //改变播放速率
    $(".select-rate").change(function () {
        video.playbackRate = $(".select-rate").val();
    })

    /*全屏切换*/
    $(".fullscreen").click(function () {
        if(!(document.fullscreen ||
            document.mozFullScreen ||
            document.webkitIsFullScreen ||
            document.webkitFullScreen ||
            document.msFullScreen
        )){
            video.requestFullscreen();
            this.toggleClass("gicon-arrows-expand icon-arrows-contract");
        }else{
            video.exitFullscreen();
            this.toggleClass("icon-arrows-contract icon-arrows-expand ");
        }

    })

    /*内嵌自自定义播放条*/
    setInterval(function () {
        $(".play-control").css({
            "margin-top":(video.offsetHeight-55)+'px'
        })
    },300)

    /*播放控制条的显示与隐藏*/
    //TODO
    $('.video').mouseenter(function (event) {
        moveX = event.clientX;
        moveY = event.clientY;
        if (moveX+moveY!=0){
            $('.play-control').css({
                'display':'block'
            })
            setInterval(function () {
                $('.play-control').css({
                    'display':'none'
                })
            },9000)
        }


    })

    /*获取当前视频url*/
    function getVideo(){
        var formData = new FormData();
        var sectionId = getQueryString('sectionid');
        var sectionUrl = '/mooc/section/videourl';
        formData.append("sectionId",sectionId);
        $.ajax({
            url:sectionUrl,
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    $('.course-name').text(data.videourl.name)
                    $('.video').attr('src',data.videourl.videoUrl);
                }
                else{
                    console.log(data.message);
                }
            }
        });
    }
});

