$(function () {
    //加载页面时清空所有sessionStorge
    sessionStorage.clear();
    /*收藏状态判断*/
    var collection;
    //登录状态
    getuserstate();
    //学习进度
    studyProgress();
    //课程信息
    getCourse();
    //章节信息
    getSection();
    //评论信息
    Comment(0,0);
    //答疑
    Question(0,1);
    //讲师信息
    userInfo();
    //加载收藏状态
    showCollectionState();
    //加载是否关注
    ShowFollowState();

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

    /*课程信息*/
    function getCourse(){
        var id = getQueryString('id');
        var courseUrl = '/mooc/course/onecourseinfo?id='+id;
        $.getJSON(courseUrl,function(data) {
            if(data.success){
                var course = data.course;
                var courseInfo ='<div class="info">'
                        +'<span>课程</span>/<span>'
                        +course.classifyName+'</span>/<span>'
                        +course.subClassifyName+'</span>'
                        +'<span id="collection" class="icon-star"></span><span class="collection">收藏</span></span>'+'</div>'
                        +'<div class="course-name">'
                        +course.courseName+'</div>'
                        +'<div class="course-message">'
                        +'时常:'+course.courseTime+' · 学习人数:'+course.studyCount
                        +'</div>';
                $('.course-details').prepend(courseInfo);
                $('.course-introduction').append(course.brief);
                $('.background-img').css({
                    "background":'url('+course.picture+')'
                });
            }
        });
    }

    /*收藏表*/
    $(document).on('click','#collection',function () {
               //将课程加入用户收藏表
        if (collection){
            console.log("加入");
            addcollection();
        }
        //将课程移除用户收藏表
        else{
            console.log("移除");
            removecollection();
        }
    });

    /*添加收藏*/
    function  addcollection() {
        var formData = new FormData();
        var tips = $('#tips').val();
        var courseId = getQueryString('id');
        formData.append("tips",tips);
        formData.append("courseid",courseId);
        $.ajax({
            url: ("/mooc/useroperator/addcollection"),
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    toast('id','系统消息',data.message);
                    showCollectionState();
                } else {
                    toast('id','系统消息',data.message);
                }
            }
        });
    }

    /*取消收藏*/
    function removecollection() {
        var formData = new FormData();
        var courseId = getQueryString('id');
        formData.append("courseid",courseId);
        $.ajax({
            url: ("/mooc/useroperator/rmovecollection"),
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    toast('id','系统消息',data.message);
                    showCollectionState();
                } else {
                    toast('id','系统消息',data.message);
                }
            }
        });
    }

    /*加载我的所有收藏匹配当前课程并显示收藏状态*/
    function showCollectionState() {
        var formData = new FormData();
        var courseId = getQueryString('id');
        formData.append("courseid",courseId);
        $.ajax({
            url: ("/mooc/useroperator/collectionstate"),
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    collection = false;
                    $("#collection").addClass("icon-star-fill").removeClass("icon-star")
                } else {
                    collection = true;
                    $("#collection").addClass("icon-star").removeClass("icon-star-fill");
                }
            }
        });
    }

    /*章节信息*/
    function getSection(){
        var id = getQueryString('id');
        var sectionUrl = '/mooc/section/sectionlist?id='+id;
        $.getJSON(sectionUrl,function(data) {
            if(data.success && data.sectionlist.length >0 ){
                var SectionList = data.sectionlist;
                var chapterModel = '';
                SectionList.map(function(item, index) {
                    var sectionModel = '';
                    for(var i=0;i<item.sections.length;i++){
                        sectionModel +='<div class="section" section-id="'
                            +item.sections[i].id+'">'
                            +'<span class="icon-play-fill"></span>'
                            +'<span class="section-name">'
                            +item.sort+'-'+item.sections[i].sort+item.sections[i].name
                            +'</span></div>';
                        var section = {
                            'sectionId':item.sections[i].id,
                            'sectionName':item.sections[i].name
                        };
                    }
                    chapterModel +='<div class="chapter"><div class="chapter-name">'
                        +'第'+item.sort+'章 '+item.name
                        +'</div>'
                        +sectionModel+'</div>';
                });
            }else{
                chapterModel = '<h5 class="null-tip">未查询到相关章节信息!</h5>';
            }
            $('#course-more').html(chapterModel);

        });
    }

    /*添加学习记录*/
    $(document).on('click','.section',function (e) {
        addStudyRecord($(this).attr("section-id"));
    });

    /*添加学习记录函数*/
    function addStudyRecord(sectionId) {
        var id = getQueryString('id');
        var url = '/mooc/useroperator/addrecord?courseId='+id+'&sectionId='+sectionId;
        $.getJSON(url,function(data) {
            if(data.success){

            }
        });
    }

    /*分页加载评论*/
    function Comment(pageIndex) {
        //拼接出查询条件的url
        var url = '/mooc/course/commentlist?type=0&pageSize=10&pageIndex='+pageIndex+'&id='+getQueryString('id');
        $.getJSON(url, function(data) {
            if (data.success && data.count>0) {
                sessionStorage.setItem('comment-count',data.count);
                sessionStorage.setItem('comment-page',Math.ceil(data.count/10));
                /*分页加载评论*/
                var commentList = '';
                data.courseCommentList.map(function(item, index) {
                    commentList += '<div class="details">'
                        +'<div class="comment"><img src="'
                        +item.header+'" alt="..." class="img-circle user-img">'
                        +'<span class="comment-name">'
                        +item.username+'</span>'
                        +'<span class="comment-time">'
                        +new Date(item.updateTime).Format("yyyy-MM-dd-hh:mm")+'</span></div>'
                        +'<div class="comment-text">'
                        +item.content+'</div></div>';
                });
            }else{
                commentList = '<h5 class="null-tip">未查询到相关数据!</h5>';
            }
            $('#comment-content').html(commentList);
        });
    }

    /*分页加载疑问*/
    function Question(pageIndex) {
        //拼接出查询条件的url
        var url = '/mooc/course/commentlist?type=1&pageSize=10&pageIndex='+pageIndex+'&id='+getQueryString('id');
        $.getJSON(url, function(data) {
            if (data.success && data.count>0) {
                sessionStorage.setItem('question-count',data.count);
                sessionStorage.setItem('question-page',Math.ceil(data.count/10));
                /*分页加载评论*/
                var commentList = '';
                data.courseCommentList.map(function(item, index) {
                    commentList += '<div class="details">'
                            +'<div class="comment"><img src="'
                            +item.header+'" class="img-circle user-img">'
                            +'<span class="comment-name">'
                            +item.username+'</span>'
                            +'<span class="comment-time">'
                            +new Date(item.updateTime).Format("yyyy-MM-dd-hh:mm")+'</span></div>'
                            +'<div class="comment-text">'
                            +item.content+'</div><div class="ref-content">'
                            +(item.refContent==null?'':item.refContent)+'</div><button class="replay-btn btn btn-sm btn-outline-info" data-username="'
                            +item.username+'" data-ref="'
                            +item.id+'" data-account="'
                            +item.account+'">'
                            +'回复'+'</button></div></div>';
                });
            }else{
                commentList = '<h5 class="null-tip">未查询到相关数据!</h5>';
            }
            $('#question-content').html(commentList);
        });
    }


    /*加载讲师信息*/
    function userInfo() {
        var url = '/mooc/auth/teacherinfo?id='+getQueryString('id');
        $.getJSON(url, function(data) {
            if (data.success) {
                var auther = data.authuser;
                var teacherInfo ='<img src="'
                        +auther.header+'" class="rounded-circle teacher-img">'
                        +'<div class="name">'
                        +auther.realname+'</div>'
                        +'<span class="info">'
                        +auther.collegeName+'·'+auther.education+'</span>'
                        +'<span class="sign">'
                        +auther.title+'·'+auther.sign+'</span>'
                        +'<button type="button" class="btn btn-success btn-follow">'
                        +'+关注'+'</button>';
                sessionStorage.setItem("account",auther.account);
            }
            $('.recommend').html(teacherInfo);
        });
    }

    /*显示是否关注*/
    function ShowFollowState() {
        var owner = sessionStorage.getItem("owner");
        var url = '/mooc/useroperator/isfollow?owner='+owner;
        $.get(url, function(data) {
            if (data.success) {
                $(document.getElementsByClassName("btn-follow")).
                    text('已关注').
                    css({
                        "background-color":'#145423'
                    })
            }else {
                $(document.getElementsByClassName("btn-follow")).
                text('+关注').
                css({
                    "background-color":'#28a745'
                })
            }
        });
    }

    /*关注切换*/
    $(document).on('click','.btn-follow',function () {
       var owner = sessionStorage.getItem("owner");
       var url = '/mooc/useroperator/dofollow?owner='+owner;
       $.get(url, function(data) {
           if (data.success) {
               toast("id","系统通知",data.message);
               ShowFollowState();
           }else {
               toast("id","系统通知",data.message);
               if(data.message === "登录失效!"){
                   $('#login-register').modal('show');
                   $('.login-modal').css({
                       "display":"block"
                   });
                   $('.register-modal').css({
                       "display":"none"
                   });
               }
           }
       });
   });

    /*添加评论&疑问*/
    function addComment(type,refId,toAccount,refContent) {
        var courseComment = {};
        var content = $('#comment-container').val();
        if(util.isEmpty(content)){
            $("#comment-container").css({ "border":"solid red 1px" });
            toast("id","表单通知","内容不能为空！");
            return;
        }
        courseComment.courseId = getQueryString('id');
        courseComment.content = content;
        courseComment.type = type;
        courseComment.refId = refId;
        courseComment.refContent = refContent;
        courseComment.toAccount = toAccount;
        $.ajax({
            url: ("/mooc/course/addcomment"),
            type:'POST',
            dataType:'json',
            data:JSON.stringify(courseComment),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    toast('id','系统消息',data.message);
                    $("#new-comment").modal("hide");
                } else {
                    toast('id','系统消息',data.message);
                    if(data.message ==="登录失效！"){
                        $("#new-comment").modal("hide");
                        $('#login-register').modal('show');
                        $('.login-modal').css({
                            "display":"block"
                        });
                        $('.register-modal').css({
                            "display":"none"
                        });
                    }
                }
            }
        });
    }

    /*加载学习进度*/
    function studyProgress(){
        var id = getQueryString('id');
        var url = '/mooc/useroperator/currentstudy?courseId='+id;
        $.getJSON(url,function(data) {
            if(data.success){
                $('.learning-progress').text("已学"+data.record.rate+"%");
                $('.study-progress').css({
                    "width":data.record.rate+'%'
                });
                if(data.record.id!=null){
                    $('.learning-chapter').text("上次学习至："+data.record.sectionName);
                    $('.btn-learning').text('继续学习').attr({'status':true,'sid':data.record.sectionId});
                }
            }
        });
    }

    /*加入课程&继续学习*/
    $(".btn-learning").click(function () {
        var courseId = getQueryString('id');
        if($(this).attr("status")==="true"){
            window.location.href = '/mooc/video?courseid=' + courseId + "&sectionid=" + $(this).attr('sid');
        }else{
            var url = '/mooc/useroperator/addstudycourse?courseId='+courseId;
            $.getJSON(url,function(data) {
                if(data.success){
                    toast("id","系统通知",data.message);
                    //若添加成功，同时向记录表新增一条记录
                    addStudyRecord($("#course-more").children('div:first').children('div').eq(1).attr("section-id"))
                    $('.btn-learning').text('继续学习').attr('status',true);
                }else{
                    toast("id","系统通知",data.message);
                }
            });
        }
    });

    /*触发添加评论模态框*/
    $('#add-comment').click(function () {
        $("#head-content").html("添加你的评论");
        $("#comment-container").attr('placeholder',"发布你的评论吧").css({"border":"solid 1px" });
        $(".btn-submit").attr('id',"submit-comment");
        $("#new-comment").modal("show");
    });
    $(document).on('click','#submit-comment',function () {
        addComment(0,0,sessionStorage.getItem("account"),'');
    });

    /*触发添加疑问模态框*/
    $("#add-question").click(function () {
        $("#head-content").html("请仔细描述你的问题");
        $("#comment-container").attr('placeholder',"输入你的问题").css({"border":"solid 1px" });;
        $(".btn-submit").attr('id',"submit-question");
        $("#new-comment").modal("show");
    });
    $(document).on('click','#submit-question',function () {
        addComment(1,0,sessionStorage.getItem("account"),'');
    });

    /*触发回复模态框*/
    $(document).on('click','.replay-btn',function (e) {
        $("#head-content").html("@"+e.currentTarget.dataset.username);
        $("#comment-container").attr('placeholder',"要回复的内容").css({"border":"solid 1px" });;
        $(".btn-submit").attr({
            id:"submit-replay",
            ref:e.currentTarget.dataset.ref,
            refcontent:$(this).prev().prev().text(),
            account:e.currentTarget.dataset.account
        });
        $("#new-comment").modal("show");
    });

    /*触发提交回复*/
    $(document).on('click','#submit-replay',function (e) {
        var refId =$(this).attr("ref");
        var toAccount = $(this).attr("account");
        var refContent = $(this).attr("refcontent");
        addComment(1,refId,toAccount,refContent);
        $(this).remove("ref").remove("account").remove("refContent");
    });

    /*跳转小节播放页*/
    $(document).on('click','.section',function(e){
        var courseid = getQueryString('id');
        var sectionid = $(this).attr("section-id");
        window.location.href = '/mooc/video?courseid=' + courseid + "&sectionid=" + sectionid;
    });

    /*章节、评论、答疑切换*/
    $('.manage').click(function () {
        $("#page").html('');
        $(".collapse").removeClass('show');
        $(this).addClass("active").siblings().removeClass("active");
        switch ($(this).attr("class").split(' ').shift()) {
            case "course-more":
                $("#course-more").collapse('show');
                break;
            case "comment-more":
                Comment(1,0);
                new Page({
                    id: 'page',
                    pageTotal: sessionStorage.getItem('comment-page'),
                    pageAmount: 10,
                    dataTotal: sessionStorage.getItem('comment-count'),
                    curPage:1, //初始页码,不填默认为1
                    pageSize: 5, //分页个数,不填默认为5
                    showPageTotalFlag:true, //是否显示数据统计,不填默认不显示
                    showSkipInputFlag:true, //是否支持跳转,不填默认不显示
                    getPage: function (page) {
                        Comment(page,0);
                    }
                });
                $("#comment-more").collapse('show');
                break;
            case "question-more":
                Question(1,1);
                new Page({
                    id: 'page',
                    pageTotal: sessionStorage.getItem('question-page'),
                    pageAmount: 10,
                    dataTotal: sessionStorage.getItem('question-count'),
                    curPage:1,
                    pageSize: 5,
                    showPageTotalFlag:true,
                    showSkipInputFlag:true,
                    getPage: function (page) {
                        Question(page,1);
                    }
                });
                $("#question-more").collapse('show');
                break;
        }
    });
});