$(function () {
    //加载页面时清空所有sessionStorge
    sessionStorage.clear();
    userState();
    TopClassify();
    myPublish(1);
    new Page({
        id: 'page',
        pageTotal: sessionStorage.getItem('publish-page'),
        pageAmount: 5,
        dataTotal: sessionStorage.getItem('publish-count'),
        curPage:1,
        pageSize: 5,
        showPageTotalFlag:true,
        showSkipInputFlag:true,
        getPage: function (page) {
            myPublish(page);
        }
    });
    /*用户登录状态*/
    function userState(){
        var UserStateUrl = '/mooc/useroperator/loginstate';
        $.post(UserStateUrl,function(data) {
            if(data.success){
                var userinfo ='<img src="'
                    +data.userinfo.header
                    +'" alt="个人中心" class="rounded-circle personal-img"/>'
                    +'<span class="user-name">'
                    +data.userinfo.username+'</span>';
                $('.userinfo').html(userinfo);
                $('.img').attr('src',data.userinfo.header);
                $('.user-name').html(data.userinfo.username);
            }else{
                if(data.message ==="登录失效!"){
                    $('#login-register').modal('show');
                    $('.login-modal').css({
                        "display":"block"
                    });
                    $('.register-modal').css({
                        "display":"none"
                    });
                    userState();
                }
            }
        });
    }

    /*加载顶级分类列表*/
    function TopClassify() {
        var topfUrl = '/mooc/homeshow/querytopclassify';
        $.getJSON(topfUrl,function(data) {
            if(data.success){
                var topClassify = '';
                data.classifylist.map(function(item, index) {
                    topClassify +='<option value="'
                        +item.code+'">'
                        +item.name+'</option>';
                });
                $('#top-classify').html(topClassify);
            }
        });
    }
    $('#top-classify').click(function () {
        SubClassify();
    });

    /*加载二级分类列表*/
    function SubClassify(){
        var parentCode = $('#top-classify option:selected').attr('value');
        var classifyUrl = '/mooc/homeshow/querysubclassify?code='+parentCode;
        $.getJSON(classifyUrl,function(data) {
            if(data.success){
                var subClassify = '';
                data.subcalssifylist.map(function(item, index) {
                    subClassify +='<option value="'
                        +item.code+'">'
                        +item.name+'</option>';
                });
                $('#sub-classify').html(subClassify);
            }
        });
    }

    /*加载当前课程*/
    function getCourse(id){
        var courseUrl = '/mooc/course/onecourseinfo?id='+id;
        $.getJSON(courseUrl,function(data) {
            if(data.success){
                $("#course-picture").attr('src',data.course.picture);
                $("#course-name").val(data.course.courseName);
                $("#top-classify").val(data.course.classifyName);
                $("#sub-classify").val(data.course.subClassifyName);
                $("#course-owner").val(data.course.ownerName);
                $("#course-time").val(data.course.courseTime);
                $("#course-brief").val(data.course.brief);

            }
        });
    }

    /*课程名*/
    $("#course-name").blur(function() {
        if(util.zcValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*讲师*/
    $("#course-owner").blur(function() {
        if(util.cnValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*时常*/
    $("#course-time").blur(function() {
        if(util.numZnValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*简介*/
    $("#course-brief").blur(function() {
        if(!util.isEmpty($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*加载我发布的课程*/
    function myPublish(pageIndex) {
        var formData = new FormData();
        formData.append("pageIndex", pageIndex);
        formData.append("pageSize",5);
        //拼接出查询条件的url
        console.log();
        $.ajax({
            url: ("/mooc/teacher/courselist"),
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success:function (data) {
                if (data.success && data.count > 0) {
                    sessionStorage.setItem('publish-page',Math.ceil(data.count/10));
                    sessionStorage.setItem('publish-count',data.count);
                    /*分页加载评论*/
                    var courseList = '';
                    data.courseList.map(function(item, index) {
                        courseList += '<div class="course-container publish" courseId="'
                            +item.id+'">'
                            +'<img class="course-img" src="'
                            +item.picture
                            +'"/><div class="course-content">'
                            +'<span class="name-course">'
                            +item.courseName
                            +'</span><span class="course-owner">'
                            +'<span style="font-weight: bold;">讲师:</span>'
                            +item.ownerName
                            +'</span><span class="course-brief">'
                            +item.brief
                            +'</span></div><div class="user-operator">'
                            +'</div></div>';
                    });
                }else{
                    courseList = '<h5 class="null-tip">未查询到相关数据!</h5>';
                }
                $('#course-list').html(courseList);
            }
        });
    }

    $(document).on('click','.btn-start',function () {
        $("#picker").find('input').click();
        sessionStorage.setItem('v-list',$(this).prev().attr('class').split(' ').shift());
        sessionStorage.setItem('card',$(this).parent().parent().attr("section-id"));

    });
    $(document).on('click','.qqq',function () {
        $("#ctlBtn").click();
    });

    /*章节信息*/
    function getSection(){
        var sectionUrl = '/mooc/section/sectionlist?id='+sessionStorage.getItem("courseId");
        $.getJSON(sectionUrl,function(data) {
            var chapterModel = '';
            if(data.success && data.sectionlist.length > 0){
                data.sectionlist.map(function(item, index) {
                    var sectionModel = '';
                    for(var i=0;i<item.sections.length;i++){
                        sectionModel +='<div class="section" section-id="'
                            +item.sections[i].id+'" section-name="'
                            +item.sections[i].name+'">'
                            +'<span class="icon-play-fill"></span>'
                            +'<span class="name-section">'+(index+1)+'-'+(i+1)+item.sections[i].name+'</span>'
                            +'<span style="float: right">'
                            +'<span class="btn-upload-'
                            +item.sections[i].id+' v-list" style="margin-right:20px;color:coral;cursor: pointer;">'
                            +(item.sections[i].videoUrl!==''?'':'没有检索到课程资源')
                            +'</span><button class="btn-start btn btn-sm btn-secondary" style="margin: 0 15px 5px 0;">'
                            +(item.sections[i].videoUrl===''?'上传视频':'重新上传')+'</button><a href="javascript:" class="modify-section" section-time="'
                            +item.sections[i].time+'" section-url="'
                            +item.sections[i].videoUrl+'">修改</a>'
                            +'<a href="javascript:" class="delete-section" style="margin: 0 10px;">删除</a>'
                            +'</span></div>';
                    }
                    chapterModel +='<div class="chapter"'
                        +'><div class="name-chapter" section-name="'
                        + item.name+'" section-id="'
                        + item.id+'"><span style="float: right;font-size: 14px;font-weight:normal; ">'
                        +'<a href="javascript:" class="modify-chapter">修改</a>'
                        +'<a href="javascript:" class="delete-section" style="margin: 0 10px;">删除</a></span>'
                        +'第'+(index+1)+'章 '+item.name
                        +'</div>'
                        +sectionModel+'</div>';
                });
            }else{
                chapterModel = '<h5 class="null-tip">未查询到相关章节信息!</h5>';
            }
            $('.modify-content').html(chapterModel);
        });
    }

    /*修改章节*/
    $(document).on('click','.modify-section , .modify-chapter',function () {
        $("#sheet-section").css({"display":"none"});
        $("#chapter-name").val($(this).parent().parent().attr("section-name")).attr("section-id",$(this).parent().parent().attr("section-id"));
        if($(this).attr("class")!=="modify-chapter"){
            $("#video-url").val($(this).attr("section-url"));
            $("#video-time").val($(this).attr("section-time"));
            $("#sheet-section").css({"display":"block"});
        }
        $("#modify-section").modal("show");
    });

    /*章名*/
    $(document).on('blur','#chapter-name',function () {
        if(util.zcValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*url*/
    $(document).on('blur','#video-url',function () {
        if(util.urlValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });


    /*时常*/
    $(document).on('blur','#video-time',function() {
        if(util.timeValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*修改章节信息函数*/
    $("#save-section").click(function () {
        var formData = new FormData();
        var $c = $("#chapter-name");

        if(!util.zcValid($c.val())){
            $c.css({ "border":"solid red 1px"  });
            toast("id","表单通知","格式不正确，要求2-16中文、字母、数字及-符号！");
            return;
        }

        if(!util.urlValid($("#video-url").val())){
            $("#video-url").css({ "border":"solid red 1px"  });
            toast("id","表单通知","url地址格式不正确！");
            return;
        }

        if(!util.timeValid($("#video-time").val())){
            $("#video-time").css({ "border":"solid red 1px"  });
            toast("id","表单通知","格式不正确，参考10:10！");
            return;
        }

        formData.append("id",$c.attr("section-id"));
        formData.append("name",$c.val());
        formData.append("url",$("#video-url").val());
        formData.append("time",$("#video-time").val());
        $.ajax({
            url:("/mooc/teacher/modifysection"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    toast("id","系统消息",data.message);
                    $("#modify-section").modal("hide");
                }
                else{
                    toast("id","系统消息",data.message);
                }
            }
        });
    });

    /*删除章节信息函数*/
    $(document).on("click",".delete-section",function () {
        var formData = new FormData();
        formData.append("id",$(this).parent().parent().attr("section-id"));
        $.ajax({
            url:("/mooc/teacher/deletesection"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    toast("id","系统消息",data.message);
                }
                else{
                    toast("id","系统消息",data.message);
                }
            }
        });
    });

    /*创建课程*/
    $("#save-course").click(function(){
        //生成表单对象，用于接收参数并传递给后台
        var formData = new FormData();
        var courseName = $("#course-name").val();
        var topClassify = $("#top-classify").val();
        var topClassifyName = $("#top-classify option:selected").text();
        var subClassify = $("#sub-classify").val();
        var subClassifyName = $("#sub-classify option:selected").text();
        var owner = $("#course-owner").val();
        var time = $("#course-time").val();
        var picture = $('#course-img')[0].files[0];
        var brief = $("#course-brief").val();

        if(!util.zcValid(courseName)){
            $("#course-name").css({ "border":"solid red 1px"  });
            toast("id","表单通知","课程名在2-16个字符之间！");
            return;
        }
        if(picture===undefined){
            toast("id","表单提示","课程封面不能为空！");
            return;
        }
        if (!util.photoValid(picture.name)||picture.size>1024*1024){
            toast("id","表单提示","图片格式或大小不正确，仅支持大小不得超过1M的png、jpg、jepg图片");
            return;
        }

        if(util.isEmpty(subClassify)){
            $("#sub-classify").css({ "border":"solid red 1px"  });
            toast("id","表单通知","分类不能为空！");
            return;
        }
        if(!util.cnValid(owner)){
            $("#course-owner").css({ "border":"solid red 1px"  });
            toast("id","表单通知","讲师名必须在2-5字符之间！");
            return;
        }
        if(!util.numZnValid(time)){
            $("#course-time").css({ "border":"solid red 1px"  });
            toast("id","表单通知","要求2-16中文、数字及-符号！");
            return;
        }
        if(util.isEmpty(brief)){
            $("#course-brief").css({ "border":"solid red 1px"  });
            toast("id","表单通知","简介不能为空！");
            return;
        }

        formData.append('courseName', courseName);
        formData.append('topClassify', topClassify);
        formData.append('topClassifyName', topClassifyName);
        formData.append('subClassify', subClassify);
        formData.append('subClassifyName', subClassifyName);
        formData.append('owner', owner);
        formData.append('file', picture);
        formData.append("brief",brief);
        formData.append("time",time);
        $.ajax({
            url : '/mooc/teacher/createcourse',
            type : 'POST',
            data : formData,
            contentType : false,
            processData : false,
            cache : false,
            success : function(data) {
                if (data.success) {
                    toast("id","用户通知",data.message);
                    sessionStorage.setItem("courseId",data.courseid);
                } else {
                    toast("id","用户通知",data.message);
                }
            }
        });
    });

    /*章名*/
    $(document).on('blur','.chapter-name',function () {
        if(util.zcValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*节名*/
    $(document).on('blur','.section-name',function () {
        if(util.numZnValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    //保存章节信息
    $('#save-chapter').click(function () {
        if(sessionStorage.getItem("courseId") === ''){
            toast("id","用户通知","请先添加课程或保存课程基本信息");
            return;
        }
        var chapters = $('.course-details').find("div[sid='chapter-content']");
        var courseSections = [];
        //章
        $.each(chapters,function(i,item){
            var chapterName = $(item).find("input[name='chapter-name']").val();
            if(chapterName && $.trim(chapterName) !== ''){
                var chapter = {};
                chapter.name = $.trim(chapterName);
                chapter.courseId = sessionStorage.getItem("courseId");
                chapter.sections = [];
                //节
                var sections = $(item).find("div[sid='section-content']");
                $.each(sections,function(j,subItem){
                    var sectionName = $(subItem).find("input[name='section-name']").val();
                    var video = $(subItem).find("input[name='section-video']").val();
                    var time = $(subItem).find("input[name='section-time']").val();

                    //正则表达式验证time
                    var regTime = RegExp(/^([0-5][0-9]):([0-5][0-9])$/);
                    if (!regTime.test(time)) {//如果验证不通过
                        time = "00:00";
                    }
                    if(sectionName && $.trim(sectionName) !== ''){
                        var section = {};
                        section.name = $.trim(sectionName);
                        section.videoUrl = $.trim(video);
                        section.time = $.trim(time);
                        chapter.sections.push(section);
                    }
                });
                courseSections.push(chapter);
            }
        });
        if(courseSections.length === 0){
            toast("id","用户通知","请填写章节信息");
        }
        $.ajax({
            url:'/mooc/teacher/chaptersection',
            type:'POST',
            contentType: "application/json",
            dataType:'json',
            data:JSON.stringify(courseSections),
            success:function(data){
                if(data.success){
                    toast("id","用户通知",data.message);
                    getSection();
                    $(".collapse-chapter").removeClass('show');
                    $("#manage-modify").collapse('show');
                }else{
                    toast("id","用户通知",data.message);
                }
            }
        });
    });


    /*点击对应已经发布课程，进入对应修改页面*/
    $(document).on('click','.publish',function(e){
        //清除分页
        $("#page").html('');
        var courseId = $(this).attr("courseId");
        sessionStorage.setItem("courseId",courseId);
        //加载课程信息
        getCourse(courseId);
        $(".collapse").removeClass('show');
        $("#manage-publish").collapse('show');
        //加载章节信息
        getSection();
        $(".collapse-chapter").removeClass('show');
        $("#manage-modify").collapse('show');
    });

    //切换子分类选项卡
    $('.manage , .btn-manage').click(function () {
        userState();
        $(this).addClass("active").siblings().removeClass("active");
        $("#page").html('');
        switch ($(this).attr("class").split(' ').shift()) {
            case "manage-course":
                myPublish(1);
                new Page({
                    id: 'page',
                    pageTotal: sessionStorage.getItem('publish-page'),
                    pageAmount: 5,
                    dataTotal: sessionStorage.getItem('publish-count'),
                    curPage:1,
                    pageSize: 5,
                    showPageTotalFlag:true,
                    showSkipInputFlag:true,
                    getPage: function (page) {
                        myPublish(page);
                    }
                });
                $(".collapse-course").removeClass('show');
                $("#manage-course").collapse('show');
                break;
            case "manage-publish":
                $(".reset").val('');
                sessionStorage.setItem("courseId",'');
                $(".collapse-course").removeClass('show');
                $("#manage-publish").collapse('show');
                $(".collapse-chapter").removeClass('show');
                $("#manage-add").collapse('show');
                break;
            case "manage-add":
                $(".collapse-chapter").removeClass('show');
                $("#manage-add").collapse('show');
                break;
            case "manage-modify":
                getSection();
                $(".collapse-chapter").removeClass('show');
                $("#manage-modify").collapse('show');
                break;
        }
    });

    //添加章
    $("#add-chapter").click(function (){
        var template = $('.chapter-content').clone();
        $('#manage-add').append(template);
    });
    //删除章
    $(document).on('click','.remove-chapter',function () {
        $(this).parent().parent().parent().remove();
    });

    //添加节
    $(document).on('click','.add-section',function () {
        var sectionDiv = $('.section-content').clone();
        $(this).parent().parent().parent().append(sectionDiv);
    });
    //删除节
    $(document).on('click','.remove-section',function () {
        $(this).parent().parent().remove();
    });

});

function checkCourse(img){
    if (util.photoValid(img)){
        util.previewUploadImg("course-img","course-picture");
    }else {
        toast("id",'系统通知',"请选择png、jpg、jepg格式图片");
        $("#header").val('');
    }
}

/*文件断点续传（基于百度webupload）*/
$(function () {
    var $ = jQuery,
        // $list = $('.v-list'),
        $btn = $('#ctlBtn'),
        state = 'pending',
        uploader;
    var fileMd5;//文件的MD5值
    var fileName;//文件名称
    var blockSize = 10 * 1024 * 1024;
    var md5Arr = new Array(); //文件MD5数组
    var timeArr = new Array();//文件上传时间戳数组
    WebUploader.Uploader.register({
        "before-send-file": "beforeSendFile",//整个文件上传前
        "before-send": "beforeSend",//每个分片上传前
        "after-send-file": "afterSendFile"//分片上传完毕
    }, {
        //1.生成整个文件的MD5值
        beforeSendFile: function (file) {
            var index = file.id.slice(8);//文件下标
            var startTime = new Date();//一个文件上传初始化时，开始计时
            timeArr[index] = startTime;//将每一个文件初始化时的时间放入时间数组
            var deferred = WebUploader.Deferred();
            //计算文件的唯一标记fileMd5，用于断点续传  如果.md5File(file)方法里只写一个file参数则计算MD5值会很慢 所以加了后面的参数：10*1024*1024
            (new WebUploader.Uploader())
                .md5File(file, 0, blockSize)
                // .progress(function (percentage) {
                //     $('#' + file.id).find('p.state').text('正在读取文件信息...');
                // })
                .then(function (value) {
                    // $("#" + file.id).find('p.state').text('成功获取文件信息...');
                    fileMd5 = value;
                    var index = file.id.slice(8);
                    md5Arr[index] = fileMd5;//将文件的MD5值放入数组，以便分片合并时能够取到当前文件对应的MD5
                    uploader.options.formData.guid = fileMd5;//全局的MD5
                    deferred.resolve();
                });
            fileName = file.name;
            return deferred.promise();
        },
        //2.如果有分快上传，则每个分块上传前调用此函数
        beforeSend: function (block) {
            var deferred = WebUploader.Deferred();
            $.ajax({
                type: "POST",
                url: '/mooc/upload/checkblock',/*[[@{/upload/checkblock}]]*/ //ajax验证每一个分片
                data: {
                    //fileName: fileName,
                    //fileMd5: fileMd5, //文件唯一标记
                    chunk: block.chunk, //当前分块下标
                    chunkSize: block.end - block.start,//当前分块大小
                    guid: uploader.options.formData.guid
                },
                cache: false,
                async: false, // 与js同步
                timeout: 1000, // 超时的话，只能认为该分片未上传过
                dataType: "json",
                success: function (response) {
                    if (response.ifExist) {
                        //分块存在，跳过
                        deferred.reject();
                    } else {
                        //分块不存在或不完整，重新发送该分块内容
                        deferred.resolve();
                    }
                }
            });
            this.owner.options.formData.fileMd5 = fileMd5;
            deferred.resolve();
            return deferred.promise();
        },
        //3.当前所有的分块上传成功后调用此函数
        afterSendFile: function (file) {
            //如果分块全部上传成功，则通知后台合并分块
            var index = file.id.slice(8);//获取文件的下标
            $('#' + file.id).parent().next().text('上传成功').attr('class','btn btn-sm btn-success');
            $.post('/mooc/upload/combine'/*[[@{/upload/combine}]]*/, {"guid": md5Arr[index], fileName: file.name},
                function (data) {
                }, "json");
        }
    });

    //上传方法
    uploader = WebUploader.create({
        // swf文件路径
        swf: '/mooc/resources/js/common/Uploader.swf',
        // 文件接收服务端。
        server:'/mooc/upload/save' /*[[@{/upload/save}]]*/,
        accept: {
            extensions: "mp4",
            mimeTypes: ".mp4"
        },
        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#picker',
        chunked: true, //分片处理
        chunkSize: 10 * 1024 * 1024, //每片5M
        threads: 3,//上传并发数。允许同时最大上传进程数。
        // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
        resize: false
    });
    // 当有文件被添加进队列的时候
    uploader.on('fileQueued', function (file) {
        //文件列表添加文件页面样式

        $('.'+sessionStorage.getItem("v-list")).html('<div id="' + file.id + '" class="item" style="display: inline-block"></div>').parent().prepend('<span>'+file.name+'</span></span>');
        $('#' + file.id).parent().next().text('已选资源');
        $btn.css({"display":"inline-block"});
        file.name = sessionStorage.getItem('card')+file.name.substring(file.name.lastIndexOf(".")).toLowerCase();
    });
    // 文件上传过程中创建进度条实时显示
    uploader.on('uploadProgress', function (file, percentage) {
        //计算每个分块上传完后还需多少时间
        var index = file.id.slice(8);//文件的下标
        var currentTime = new Date();
        var timeDiff = currentTime.getTime() - timeArr[index].getTime();//获取已用多少时间
        var timeStr;
        //如果percentage==1说明已经全部上传完毕，则需更改页面显示
        if (1 === percentage) {
            timeStr = "上传用时：" + countTime(timeDiff);//计算总用时
        } else {
            timeStr = "预计剩余时间：" + countTime(timeDiff / percentage * (1 - percentage));//估算剩余用时
        }
        //创建进度条
        var $li = $('#' + file.id), $percent = $li.find('.progress .progress-bar');
        // 避免重复创建
        if (!$percent.length) {
            $percent = $(
                '<div class="progress progress-striped active" style="width: 150px;margin-top: 13px">'
                + '<div class="progress-bar progress-bar-striped bg-info" style="width: 0%">'
                + '</div>' + '</div>')
                .appendTo($li).find('.progress-bar');
        }
        // $('#' + file.id).parent().next().text('正在上传');
        // $li.find('span.time').text(timeStr);
        $percent.css('width', percentage * 100 + '%');
    });
    /*    uploader.on('uploadSuccess', function (file) {
            var index = file.id.slice(8);
            $('#' + file.id).find('p.state').text('已上传');
            $.post(/!*[[@{/upload/combine}]]*!/, {
                "guid": md5Arr[index],
                fileName: file.name,
            }, function () {
                uploader.removeFile(file);
            }, "json");
        });*/

    //上传失败时
    uploader.on('uploadError', function (file) {
        $('#' + file.id).parent().next().text('上传出错');
    });
    //上传完成时
    uploader.on('uploadComplete', function (file) {
        $('#' + file.id).find('.progress').fadeOut();
    });
    //上传状态
    uploader.on('all', function (type) {
        if (type === 'startUpload') {
            state = 'uploading';
        } else if (type === 'stopUpload') {
            state = 'paused';
        } else if (type === 'uploadFinished') {
            state = 'done';
        }
        if (state === 'uploading') {
            $btn.text('暂停上传');
        } else {
            $btn.text('开始上传');
        }
    });
    //开始上传，暂停上传的函数
    $btn.on('click', function () {
        //每个文件的删除按钮不可用
        $(".delbtn").attr("disabled", true);
        if (state === 'uploading') {
            uploader.stop(true);//暂停
            //删除按钮可用
            $(".delbtn").removeAttr("disabled");
        } else {
            uploader.upload();
        }
    });
    //删除文件
    function delFile(id) {
        //将文件从uploader的文件列表中删除
        uploader.removeFile(uploader.getFile(id, true));
        //清除页面元素
        $("#" + id).remove();
    }
    //获取上传时还需多少时间
    function countTime(date) {
        var str = "";
        //计算出相差天数
        var days = Math.floor(date / (24 * 3600 * 1000));
        if (days > 0) {
            str += days + " 天 ";
        }
        //计算出小时数
        var leave1 = date % (24 * 3600 * 1000); //计算天数后剩余的毫秒数
        var hours = Math.floor(leave1 / (3600 * 1000));
        if (hours > 0) {
            str += hours + " 小时 ";
        }
        //计算相差分钟数
        var leave2 = leave1 % (3600 * 1000);//计算小时数后剩余的毫秒数
        var minutes = Math.floor(leave2 / (60 * 1000));
        if (minutes > 0) {
            str += minutes + " 分 ";
        }
        //计算相差秒数
        var leave3 = leave2 % (60 * 1000);//计算分钟数后剩余的毫秒数
        var seconds = Math.round(leave3 / 1000);
        if (seconds > 0) {
            str += seconds + " 秒 ";
        } else {
            /* str += parseInt(date) + " 毫秒"; */
            str += " < 1 秒";
        }
        return str;
    }
});