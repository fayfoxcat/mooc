$(function (){
    var index = 0;/*初始化一个变量，指向下标*/
    $(".tab-btn .btn").click(function () {
        index= $(this).index()/*获取当前点击元素的下标*/
        /*该当前元素添加样式active，并移除其他元素的active样式*/
        $(this).addClass("active").siblings().removeClass("active");
        /*将点击的图片淡入，其余淡出*/
        $(".item").eq(index).fadeIn().siblings().fadeOut();
    })

    //左按钮切换
    $(".lr-tab .left").click(function () {
        index --;
        if(index <0){
            index = 4;
        }
        $(".item").eq(index).fadeIn().siblings().fadeOut();
        $(".tab-btn .btn").eq(index).addClass("active").siblings().removeClass("active");
    })

    //右按钮切换
    $(".lr-tab .right").click(function () {
        index ++;
        if(index >4){
            index = 0;
        }
        $(".item").eq(index).fadeIn().siblings().fadeOut();
        $(".tab-btn .btn").eq(index).addClass("active").siblings().removeClass("active");
    })

    //自动轮播2s
    var time = setInterval(function () {
        index ++;
        if(index >4){
            index = 0;
        }
        $(".item").eq(index).fadeIn().siblings().fadeOut();
        $(".tab-btn .btn").eq(index).addClass("active").siblings().removeClass("active");
    },2000);

    getSiteCarousel(4);
    /*向后台获取轮播列表*/
    function getSiteCarousel(count) {
        var classifyUrl = '/mooc/homeshow/querysitecarousel?count='+count;
        $.getJSON(classifyUrl,function(data) {
            if(data.success){
                var siteCarousellist = data.sitecarousellist;
                var SiteImg = '';
                siteCarousellist.map(function(item, index) {
                    SiteImg +='<li class="item click-course" picture="'
                    +item.url+'"><img src="'
                    +item.picture
                    +'"></li>';
                });
                $('.li-img').html(SiteImg);
            }
        });
        var btn ='';
        for (var i=0;i<count;i++){
            btn +='<li class="btn"></li>';
        }
        $('.li-btn').html(btn);
    }

    /*调用顶级分类、课程*/
    getTopClassify();
    getCourse();
    getuserstate();
    getTeacher();

    /*加载顶级分类函数*/
    function getTopClassify() {
        var classifyUrl = '/mooc/homeshow/querytopclassify';
        $.getJSON(classifyUrl,function(data) {
            if(data.success){
                var courseClassifyList = data.classifylist;
                var topClassify = '';
                courseClassifyList.map(function(item, index) {
                    topClassify +='<button type="button"  id="top-classify" class="list-group-item"'
                        +'code="'
                        +item.code
                        +'">'
                        +item.name
                        +'</button>';
                });
                $('.list-group').html(topClassify);
                $('.list-group-item').css({
                    'font-size':'16px',
                    'text-align':'center',
                    'height': '50px'
                })
            }
        });
    }

    /*加载二级分类函数*/
    function getSubClassify(parentCode){
        var classifyUrl = '/mooc/homeshow/querysubclassify?code='+parentCode;
        $.getJSON(classifyUrl,function(data) {
            if(data.success){
                var courseClassifyList = data.subcalssifylist;
                var subClassify = '';
                courseClassifyList.map(function(item, index) {
                    subClassify +='<span class="'
                    +item.code+' sub-name">'
                    +item.name+'</span>';

                });
                $('.sub').html(subClassify);
                $(".sub-name").css({
                    'display':'inline-block',
                    'margin-right':'10px',
                })
            }
        });
    }

    /*调用二级分类函数并展开、收回*/
    $(document).on('mouseenter','.list-group-item',function(){
        $(".sub-calssify").css({
            'display':'block'
        });
        $(".parent-name").text($(this).text());
        getSubClassify($(this).attr("code"));

    });
    $(".sub-calssify").on('mouseleave',function(){
        $(".sub-calssify").css({
            'display':'none'
        });

    });

    /*加载课程*/
    function getCourse(){
        var pageIndex = 0,pageSize=5;
        /*加载热门课程*/
        var HotCourseUrl = '/mooc/course/courselist?sort=study_count&pageIndex='+pageIndex+'&pageSize='+pageSize;
        $.getJSON(HotCourseUrl, function (data) {
            if (data.success) {
                var hotCourse = '';
                data.courseList.map(function (item, index) {
                    hotCourse += '<div class="col-md-1-5 course" data-courseid="' +
                        item.id+'">' +
                        '<div class="simple-classify">'+item.subClassifyName+'</div>' +
                        '<div class="course-name">'+item.courseName+'</div>' +
                        '<div class="course-details"><strong>简介:</strong>'+item.brief+'</div><div>' +
                        '<span class="course-owner">'+item.ownerName+'</span>' +
                        '<span class="course-time">'+item.courseTime+'</span></div>' +
                        '<span class="course-count">'+item.studyCount+'人在学'+'</span></div>';
                });
                $('.popular').html(hotCourse);
            }
        });

        /*加载最新课程*/
        var newCourseUrl = '/mooc/course/courselist?sort=create_time&pageIndex='+pageIndex+"&pageSize="+pageSize;
        $.getJSON(newCourseUrl, function (data) {
            if (data.success) {
                var newCourse = '';
                data.courseList.map(function (item, index) {
                    newCourse += '<div class="col-md-1-5 course" data-courseid="' +
                        item.id+'">' +
                        '<div class="simple-classify">'+item.subClassifyName+'</div>' +
                        '<div class="course-name">'+item.courseName+'</div>' +
                        '<div class="course-details"><strong>简介:</strong>'+item.brief+'</div><div>' +
                        '<span class="course-owner">'+item.ownerName+'</span>' +
                        '<span class="course-time">'+item.courseTime+'</span></div>' +
                        '<span class="course-count">'+item.studyCount+'人在学'+'</span></div>';
                });
                $('.new').html(newCourse);
            }
        });
    }

    /*加载讲师*/
    function getTeacher(){
        var url = '/mooc/auth/teacherlist';
        $.getJSON(url, function (data) {
            if (data.success) {
                var list = '';
                data.authuser.map(function (item, index) {
                    list += '<div class="col-md-1-5 teacher" account="' +
                        item.account+'">' +
                        '<img src="'+item.header+'" alt="" class="rounded-circle img-circle img">' +
                        '<div class="name">'+item.realname+'</div>' +
                        '<span class="info">'+item.collegeName+"."+item.education+'</span>' +
                        '<span class="sign"><strong>'+item.title+"."+'</strong>'+item.sign+'</span>' +
                        '</div>';
                });
                $('.famous').html(list);
            }
        });
    }
    //点击不同课程跳转相应跳转到分类页
    $(document).on('click','.course',function(e){
        var courseid = e.currentTarget.dataset.courseid;
        var courseUrl = '/mooc/course?id=' + courseid;
        window.location.href = courseUrl;
    });
    //点击不同讲师跳转相应的跳转到分类页
    $(document).on('click','.teacher',function(e){
        var account = $(this).attr("account");
        window.location.href = '/mooc/classifypage?account='+account;
    });
    $(document).on('click','.click-course',function(e){
        window.location.href = $(this).attr("picture");
    });

    //点击不同分类跳转到分类页
    $(document).on('click','.sub-name',function(e){
        var subCode = $(this).attr("class").split(' ').shift();
        window.location.href = '/mooc/classifypage?sub='+subCode;
    });

    /*安全退出*/
    $(".exit").click(function () {
        exit();
    });
});

/*用户登录状态*/
function getuserstate(){
    var UserStateUrl = '/mooc/useroperator/loginstate';
    $.post(UserStateUrl,function(data) {
        if(data.success){
            var userimg = '';
            var loginadvice = '';
            /*导航栏填充*/
            userimg +='<img src="'+data.userinfo.header+'" alt="个人中心" class="rounded-circle personal-img"/>';
            //隐藏注册登录按钮、显示注销按钮
            $('.btn-rl').css({
                "display":"none"
            });
            $('.exit').css({"display":"inline-block"});
            /*登录建议区填充*/
            loginadvice +='<div class="advice">免费学习名校名师的精品课程</div><img src="'+data.userinfo.header+'" class="rounded-circle advice-img">' +
                '<br><button type="button" class="btn btn-success">' +
                '<a href="/mooc/classifypage" style="color: white">去添加课程</a></button>';
            $('.user-img').html(userimg);
            $('.login-advice').html(loginadvice);
        }
    });
}

