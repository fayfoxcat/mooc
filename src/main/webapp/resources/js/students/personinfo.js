function checkFile(img){
    if (util.photoValid(img)){
        util.previewUploadImg("header","header-img");
        var formData = new FormData();
        var file = $('#header')[0].files[0];
        formData.append("file",file);
        $.ajax({
            url:("/mooc/useroperator/modifyheader"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    toast("id","系统消息","头像上传成功！");
                }
                else{
                    toast("id","系统消息",data.message);
                }
            }
        });
    }else {
        toast("id",'系统通知',"请选择png、jpg、jepg格式图片");
        $("#header").val('');
    }
}
$(function () {
    userState();
    getMessage(1);
    new Page({
        id: 'page',
        pageTotal: sessionStorage.getItem('message-page'),
        pageAmount: 5,
        dataTotal: sessionStorage.getItem('message-count'),
        curPage:1,
        pageSize: 5,
        showPageTotalFlag:true,
        showSkipInputFlag:true,
        getPage: function (page) {
            getMessage(page);
        }
    });

    $("#header-img").click(function () {
        $(".header").find('input').click();
    });

    /*用户登录状态*/
    function userState() {
        var UserStateUrl = '/mooc/useroperator/loginstate';
        $.post(UserStateUrl, function (data) {
            if (data.success) {
                //个人信息填充(头像、呢称、ID)
                $('.img').attr('src',data.userinfo.header);
                $('.name').text(data.userinfo.username);
                $('.account').text(data.userinfo.account);
                if (data.userinfo.role!==1){
                    $(".teach-model").css({"display":"block"});
                }
            }else{
                window.location.href = '/mooc';
            }
        });
    }

    //刷新页面初始加载个人信息栏
    $(document).ready(function () {
        $('.head').text($('#usermessage').text());
        $('.classify:first').addClass('active');
        $('#usermessage-tab').collapse('show');
    });

    //切换选项卡
    $(document).on('click', '.classify', function () {
        $(".operator").html('');
        userState();
        $("#page").html('');
        $('.collapse').removeClass('show');
        switch ($(this).children(':first').attr("id")) {
            case "usermessage":
                getMessage(1);
                new Page({
                    id: 'page',
                    pageTotal: sessionStorage.getItem('message-page'),
                    pageAmount: 5,
                    dataTotal: sessionStorage.getItem('message-count'),
                    curPage:1,
                    pageSize: 5,
                    showPageTotalFlag:true,
                    showSkipInputFlag:true,
                    getPage: function (page) {
                        getMessage(page);
                    }
                });
                $("#usermessage-tab").collapse('show');
                break;
            case "userinfo":
                $(".operator").html('<span class="describe" id="describe">编辑</span>');
                userInfo();
                $("#userinfo-tab").collapse('show');
                break;
            case "usercourse":
                $(".operator").html('<a href="/mooc/classifypage">寻找新课</a>');
                myCourse(1);
                new Page({
                    id: 'page',
                    pageTotal: sessionStorage.getItem('course-page'),
                    pageAmount: 5,
                    dataTotal: sessionStorage.getItem('course-count'),
                    curPage:1,
                    pageSize: 5,
                    showPageTotalFlag:true,
                    showSkipInputFlag:true,
                    getPage: function (page) {
                        myCourse(page);
                    }
                });
                $("#usercourse-tab").collapse('show');
                break;
            case "usercollections":
                $("#usercollections-tab").collapse('show');
                userCollection(1);
                new Page({
                    id: 'page',
                    pageTotal: sessionStorage.getItem('collection-page'),
                    pageAmount: 5,
                    dataTotal: sessionStorage.getItem('collection-count'),
                    curPage:1,
                    pageSize: 5,
                    showPageTotalFlag:true,
                    showSkipInputFlag:true,
                    getPage: function (page) {
                        userCollection(page);
                    }
                });
                break;
            case "userfollows":
                $("#userfollows-tab").collapse('show');
                userFollow(1);
                new Page({
                    id: 'page',
                    pageTotal: sessionStorage.getItem('follow-page'),
                    pageAmount: 5,
                    dataTotal: sessionStorage.getItem('follow-count'),
                    curPage:1,
                    pageSize: 5,
                    showPageTotalFlag:true,
                    showSkipInputFlag:true,
                    getPage: function (page) {
                        userFollow(page);
                    }
                });
                break;
            case "modifypassword":
                $("#modifypassword-tab").collapse('show');
                break;
            case "loginrecord":
                $("#loginrecord-tab").collapse('show');
                break;
        }
        $('.head').text($(this).text());
        $(this).addClass('active').siblings().removeClass('active');
    });

    /*个人信息*/
    function userInfo() {
        var UserStateUrl = '/mooc/useroperator/loginstate';
        $.post(UserStateUrl, function (data) {
            if (data.success) {
                //个人信息填充
                $('.username').text(data.userinfo.username);
                $('.sex').text(data.userinfo.gender === 1 ? "女" : "男");
                $('.mobile').text(data.userinfo.mobile);
                $('.birthday').text(new Date(data.userinfo.birthday).Format("yyyy年MM月dd日"));
                //模态框填充
                $('#username').val(data.userinfo.username);
                // $("#sex").get(0).selectedIndex=data.userinfo.gender;
                $('#mobile').val(data.userinfo.mobile);
                $('#birthday').val(new Date(data.userinfo.birthday).Format("yyyy-MM-dd"));
            }
        });
    }

    /*安全退出*/
    $(".exit").click(function () {
        exit();
    });

    $('.btn-modify-password').click(function () {
        modifyPassword();
    });


    /*旧密码*/
    $(".old-password").blur(function() {
        if(util.pwdValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });
    /*新密码*/
    $(".new-password").blur(function() {
        if(util.pwdValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*修改密码*/
    function modifyPassword() {
        var formData = new FormData();
        var newPassword = $('.new-password').val();
        var oldPassword = $('.old-password').val();
        var verifyCodeActual = $(".modify-code").val();
        if(!util.pwdValid(oldPassword)){
            $(".old-password").css({ "border":"solid red 1px" });
            toast("id","表单提示","密码必须是字母、符号、数字至少2种，6-16位！")
            return;
        }
        if(!util.pwdValid(newPassword)){
            $(".new-password").css({ "border":"solid red 1px" });
            toast("id","表单提示","密码必须是字母、符号、数字至少2种，6-16位！")
            return;
        }
        if(!verifyCodeActual){
            toast("id","系统通知","验证码不能为空");
            $("#cpatcha-img").click();
            return;
        }
        formData.append("newpassword",newPassword);
        formData.append("oldpassword",oldPassword);
        formData.append("verifyCodeActual",verifyCodeActual);
        $.ajax({
            url:("/mooc/useroperator/modifypassword"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    toast("id","系统消息",data.message);
                    // location.reload();
                }
                else{
                    toast("id","系统消息",data.message);
                }
            }
        });
    }

    /*昵称*/
    $("#username").blur(function() {
        if(util.zcValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*手机号码*/
    $("#mobile").blur(function() {
        if(util.mobileValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*出生日期*/
    $("#birthday").blur(function() {
        if(!util.isEmpty($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*提交更新个人信息*/
    $('.submit-userinfo').click(function () {
        var formData = new FormData();
        var username = $('#username').val();
        var sex = $('#sex').val();
        var mobile = $('#mobile').val();
        var birthday = $('#birthday').val();

        if(!util.zcValid(username)){
            $("#username").css({ "border":"solid red 1px"});
            toast("id","表单通知","昵称必须是中文、字母及-、_组合，2-12位！");
            return;
        }
        if(!util.mobileValid(mobile)){
            $("#mobile").css({ "border":"solid red 1px"});
            toast("id","表单通知","手机号码格式不正确，必须是11位数字");
            return;
        }
        console.log(util.isEmpty(birthday));
        if(util.isEmpty(birthday)){
            $("#birthday").css({ "border":"solid red 1px"});
            toast("id","表单通知","出生日期不能为空");
            return;
        }
        formData.append("username",username);
        formData.append("sex",sex);
        formData.append("mobile",mobile);
        formData.append("birthday",birthday);
        $.ajax({
            url:("/mooc/useroperator/modifyinfo"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    toast("id","系统消息",data.message);
                    userState();
                    getuserinfo();
                }
                else{
                    toast("id","系统消息",data.message);
                }
                $('#modifyinfo').modal('hide');
            }
        });
    });

    /*编辑按钮触发模态框*/
    $(document).on('click','#describe',function () {
        $('#modifyinfo').modal('show');
    });

    /*触发回复模态框*/
    $(document).on('click','.message-content',function () {
        //当前消息记录id、发送者id、引用内容id
        var messageId = $(this).attr("messageId");
        var userId = $(this).attr("userId");
        var refId = $(this).attr("refId");
        //填充模态框
        $(".message-text").text($(this).find("div").find("span").text());
        $(".submit-replay").attr({ "userId":userId, "refId":messageId});
        $('#message').modal('show');
        /*点击后更该消息为已读（数据库）*/
        $.getJSON('/mooc/message/modifystatus?id='+messageId, function(data) {
            if (data.success) {
                $('.message-state').css({
                    'display':"hidden"
                })
            }
        });
        $.getJSON('/mooc/message/comment?id='+refId, function(data) {
            if (data.success) {
                if(data.courseComment!=null){
                    var type = data.courseComment.type;
                    $.getJSON("/mooc/course/onecourseinfo?id="+data.courseComment.courseId,function(data) {
                        if(data.success) {
                            $(".message-course").text("#"+data.course.courseName+"#"+(type===0?'评论':'答疑'));
                        }
                    });
                }else{
                    $(".message-course").text("#私信");
                }
            }
        })
    });

    /*添加回复(私信)*/
    $(document).on('click','.submit-replay',function () {
        var content = $('#comment-container').val();
        console.log($(this).attr('userId'));
        if ($(this).attr('userId')==="-1"){
            toast('id',"提示信息","系统消息无需回复！");
            return;
        }
        var userMessage = {};
        if(util.isEmpty(content)){
            $("#comment-container").css({ "border":"solid red 1px"  });
            toast("id","表单通知","回复内容不能为空！");
            return;
        }
        userMessage.content = content;
        userMessage.type = 1;
        userMessage.refId = $(this).attr('refId');
        userMessage.userId = $(this).attr('userId');
        $.ajax({
            url: ("/mooc/message/createmessage"),
            type:'POST',
            dataType:'json',
            data:JSON.stringify(userMessage),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    toast('id','系统消息',data.message);
                    $("#message").modal("hide");
                } else {
                    toast('id','系统消息',data.message);
                    if(data.message ==="登录失效！"){
                        $("#message").modal("hide");
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
    });

    /*加载个人消息*/
    function getMessage(pageIndex) {
        var UserStateUrl = '/mooc/message/messagelist?pageSize=5&pageIndex='+pageIndex;
        $.post(UserStateUrl, function (data) {
            var messageList = '';
            if (data.success && data.count>0) {
                sessionStorage.setItem('message-count',data.count);
                sessionStorage.setItem("message-page",Math.ceil(data.count/5));
                data.messageList.map(function(item, index) {
                    messageList +='<div class="message-content" messageId="'
                    +item.id+'" refId="'
                    +item.refId+'" userId="'
                    +item.sendUserId+'"><img src="'
                    +item.header
                    +'" class="message-head rounded-circle" alt="">'
                    +(item.status===0? '<span class="message-state"></span>':'')
                    +'<span class="receipt-name">'
                    +item.sendUserName
                    +'</span><span class="message-time">'
                    + new Date(item.updateTime).Format("yyyy-MM-dd hh:mm:ss")
                    +'</span><div class="message-body"><span class="receipt-content">'
                    +item.content
                    +'</span></div></div>';
                });

            }else{
                messageList = '<h5 class="null-tip">未查询到相关数据!</h5>';
            }
            $('#usermessage-tab').html(messageList);
        });
    }

    /*加载我加入的学习课程*/
    function myCourse(pageIndex) {
        //拼接出查询条件的url
        var url = '/mooc/useroperator/usercourselist?pageSize=5&pageIndex='+pageIndex;
        $.get(url, function(data) {
            var courseList = '';
            if (data.success && data.count > 0) {
                sessionStorage.setItem('course-count',data.count);
                sessionStorage.setItem('course-page',Math.ceil(data.count/5));

                data.record.map(function(item, index) {
                    courseList += '<div class="course-container my-course" cid="'
                        +item.courseId+'">'
                        +'<img class="course-img" src="'
                        +item.picture
                        +'"/><div class="course-content">'
                        +'<span class="course-name">'
                        +item.courseName
                        +'</span><span class="course-owner">'
                        +'<span style="font-weight: bold;">讲师:</span>'
                        +item.ownerName
                        +'</span><span class="course-brief">'
                        +item.brief
                        +'</span></div><div class="user-operator">'
                        +'</div><button class="btn remove-course btn-outline-info" style="float: right;right: 20px">'
                        +'移除课程'+'</button></div>';
                });
            }else{
                courseList = '<h5 class="null-tip">未查询到相关数据!</h5>';
            }
            $('.usercourse-tab').html(courseList);
        });
    }

    /*加载我的收藏*/
    function userCollection(pageIndex) {
        //拼接出查询条件的url
        var url = '/mooc/useroperator/usercollection?pageSize=5&pageIndex='+pageIndex;
        $.post(url, function(data) {
            var collectionList = '';
            if (data.success && data.count > 0 ) {
                sessionStorage.setItem('collection-count',data.count);
                sessionStorage.setItem('collection-page',Math.ceil(data.count/5));

                data.collectionsList.map(function(item, index) {
                    collectionList += '<div class="course-container my-collection" cid="'
                        +item.objectId+'">'
                        +'<img class="course-img" src="'
                        +item.picture
                        +'"/><div class="course-content">'
                        +'<span class="course-name">'
                        +item.name
                        +'</span><span class="course-brief">'
                        +item.brief
                        +'</span></div><div class="user-operator">'
                        /*+'<span class="tips"><strong>Tips:</strong>'
                        +item.tips+'</span>'*/
                        +'<span class="cancel-collection">取消收藏</span></div></div>';
                });
            }else{
                collectionList = '<h5 class="null-tip">未查询到相关数据!</h5>';
            }
            $('.usercollections-tab').html(collectionList);
        });
    }

    /*加载我的关注*/
    function userFollow(pageIndex) {
        //拼接出查询条件的url
        var url = '/mooc/useroperator/followlist?pageSize=5&pageIndex='+pageIndex;
        $.get(url, function(data) {
            var follows = '';
            if (data.success && data.count > 0 ) {
                sessionStorage.setItem('follow-count',data.count);
                sessionStorage.setItem('follow-page',Math.ceil(data.count/5));

                data.authUsers.map(function(item, index) {
                    follows += '<div class="follows" account="'
                        +item.account+'"><img class="rounded-circle teacher-img" src="'
                        +item.header+'"/><div class="teacher-info"><span class="teacher-name">'
                        +item.realname+'</span><span class="teacher-education">'
                        +item.collegeName+'-'+item.education+'</span><span class="teacher-title">'
                        +item.title+'</span><span class="teacher-sign">'
                        +item.sign+'</span></div><button class="cancel-follow btn btn-sm btn-outline-warning" data-owner="'
                        +item.id+'">'
                        +'取消关注'+'</button></div>';
                });
            }else{
                follows = '<h5 class="null-tip">未查询到相关数据!</h5>';
            }
            $('#userfollows-tab').html(follows);
        });
    }

    /*取消关注*/
    $(document).on('click','.cancel-follow',function (e) {
        var url = '/mooc/useroperator/dofollow?owner='+e.currentTarget.dataset.owner;
        $.get(url, function(data) {
            if (data.success) {
                toast("id","系统通知",data.message);
            }else {
                toast("id","系统通知",data.message);
            }
        });
        return false;
    });

    /*移除课程*/
    $(document).on('click','.remove-course',function (e) {
        var url = '/mooc/useroperator/rmovecourse?courseId='+$(this).parent().attr('cid');
        $.get(url, function(data) {
            if (data.success) {
                toast("id",'用户通知',data.message)
            }else{
                toast("id",'用户通知',data.message);
            }
        });
        return false;
    });

    /*取消收藏*/
    $(document).on('click','.cancel-collection',function (e) {
        var id = $(this).parent().parent().attr("cid");
        removecollection(id);
        return false;
    });

    function removecollection(id) {
        var formData = new FormData();
        formData.append("courseid",id);
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
                } else {
                    toast('id','系统消息',data.message);
                }
            }
        });
    }

    /*跳转到讲师课程管理页面*/
    $("#publish").click(function () {
        window.location.href = '/mooc/coursemanage';
    });

    /*跳转到课程分类页，并显示当前讲师所有课程*/
    $(document).on('click','.follows',function(e){
        var account = $(this).attr("account");
        window.location.href = '/mooc/classifypage?account='+account;
    });

    /*收藏：点击不同课程跳转相应的详情页*/
    $(document).on('click','.my-course, .my-collection',function(e){
        var courseId =$(this).attr("cid");
        window.location.href = '/mooc/course?id=' + courseId;
    });
});

