# 在线电影
 在线电影基于springboot2.2.1-RELEASE,nacos,SpringCloud Hoxton.SR1,spring-cloud-alibaba-2.1.0-RELEASE,Sentinel,Seata,dubbo来构建的微服务项目
 博客指导教程链接：https://www.cnblogs.com/windyCoding/p/14188414.html
在线电影，实现在线观看电影以及在线购买电影票的主要功能等
技术架构，spring boot spring cloud、nacos、dubbo、mybatis-plus,采用前后端分离方式，前端主要由vue开发工具开发，分为前台门户页面和后台管理页面，后台主要由Java的一些架构开发

# 应用架构图

![](https://github.com/qitianfeng/yiying-parent/blob/master/img/%E5%9C%A8%E7%BA%BF%E7%94%B5%E5%BD%B1%E6%8A%80%E6%9C%AF%E6%9E%B6%E6%9E%84%E5%9B%BE.jpg)

# 项目用到的技术
项目采用前后端分离开发，前端需要独立部署。目前核心的技术栈采用的是SpringBoot2.1.5.RELEASE+Dubbo2.7.2
nodejs </br>
axios</br>
es6</br>
vue</br>
scss</br>
Element UI</br>
webpack</br>
vue router</br>

# 数据库E-R图
![](https://github.com/qitianfeng/yiying-parent/blob/master/img/Online%20Movie%20System%20ER%20Diagram.bmp)

前端页面链接
|                                                 |                                                           |
| ----------------------------------------------  | --------------------------------------------------------- |
|  前台门户页面                                    |    后台管理页面                                           |
| https://github.com/qitianfeng/online-movie-fronts| https://github.com/qitianfeng/online-movie-admin-template |


# 后端使用的技术
后端的主要架构是基于springboot+dubbo+mybatis-plus.

SpringBoot2.1.6 <br/>
Mybatis<br/>
Dubbo2.7.3<br/>
nacos<br/>
Mysql<br/>
Redis<br/>
Elasticsearch<br/>
druid<br/>
Docker<br/>
mybatis generator<br/>
<br/>
<br/>

# 后台项目模块说明及规划
|                              |                 |                                                              |
| ---------------------------- | --------------- | ------------------------------------------------------------ |
| db_script 本项目的数据库脚本 | 使用mysql数据库 | 暂时未做分表处理，不过有考虑到分表的情况                     |
| yiying-parent                | pom文件         | 父控文件，对服务统一jar包管理                                |
| yiying-common                | jar             | 公共组件，各个服务需要引用                                   |
| service-user                 | dubbo服务       | 采用sso单点登录，实现用户的登录与注册，用户信息的查询与修改，集成第三方登录(微博登录) |
| service-movie                | dubbo服务       | 实现堆电影的上传，修改，展示，删除等功能                     |
| service-oss                  | dubbo服务       | 实现文件的上传                                               |
| service-vod                  | dubbo服务       | 实现视频的上传，以及视频的点播                               |
| service-search               | dubbo服务       | 采用elasticsearch搜索引擎，实现对电影的条件检索              |
| service-order                | dubbo服务       | 创建订单，删除订单                                           |
| service-pay                  | dubbo服务       | 支付                                                         |
| service-banner               | dubbo服务       | 广告的展示等功能                                 |
| yiying-service-api           | jar包           | 各个服务创建各自的api工程                                    |




# 前台项目整体的规划有
首页渲染，轮播、自定义展示板块
电影查询、电影展示、电影详情
个人中心、用户注册、个人信息修改
订单查询、下单、支付
促销活动
![](https://github.com/qitianfeng/yiying-parent/blob/master/img/%E5%89%8D%E7%AB%AF%E6%9E%B6%E6%9E%84.jpg)


# ffmpeg相关操作及安装、依赖及命令
1. 安装nasm
wget https://www.nasm.us/pub/nasm/releasebuilds/2.14.02/nasm-2.14.02.tar.gz

tar -zxvf  nasm-2.14.02.tar.gz

cd nasm-2.14.02

./configure 

make &&make install 

echo $? 或者 whereis nasm 查看是否安装成功

2. 安装x264
cd x264
./configure --enable-static --enable-shared(注意：看到有的写的是share，但我编译的时候是shared)
make && make install
可能会提示有关shared的报错，这个时候需要安装nasm(目前是2.14.02)，如下
wget https://www.nasm.us/pub/nasm/releasebuilds/2.14.02/nasm-2.14.02.tar.bz2
./configure
make && make install

3.安装ffmpeg
（http://www.ffmpeg.org/releases/）
安装到/usr/local/ffmpeg目录

./configure --enable-gpl --enable-libx264 --prefix=/usr/local/ffmpeg  
make && make install     大约20分钟

修改 /etc/ld.so.conf
添加/usr/local/x2664/lib
ldconfing
编译完成后：cd  /usr/local/ffmpeg/bin/ 
ffmpeg测试是否安装成功

命令
ffmpeg的使用方式：
ffmpeg [options] [[infile options] -i infile]... {[outfile options] outfile}...

常用参数说明：
主要参数：
-i 设定输入流
-f 设定输出格式
-ss 开始时间
视频参数：
-b 设定视频流量，默认为200Kbit/s
-r 设定帧速率，默认为25
-s 设定画面的宽与高
-aspect 设定画面的比例
-vn 不处理视频
-vcodec 设定视频编解码器，未设定时则使用与输入流相同的编解码器
音频参数：
-ar 设定采样率
-ac 设定声音的Channel数
-acodec 设定声音编解码器，未设定时则使用与输入流相同的编解码器
-an 不处理音频


-fromats 显示可用的格式
-f fmt 强迫采用格式fmt
-I filename 输入文件
-y 覆盖输出文件
-t duration 设置纪录时间 hh:mm:ss[.xxx]格式的记录时间也支持(截图需要)
-ss position 搜索到指定的时间 [-]hh:mm:ss[.xxx]的格式也支持
-title string 设置标题
-author string 设置作者
-copyright string 设置版权
-comment string 设置评论
-target type 设置目标文件类型(vcd,svcd,dvd),所有的格式选项(比特率,编解码以及缓冲区大小)自动设置,
只需要输入如下的就可以了:ffmpeg -i myfile.avi -target vcd /tmp/vcd.mpg
-hq 激活高质量设置

-b bitrate 设置比特率,缺省200kb/s
-r fps 设置帧频,缺省25
-s size 设置帧大小,格式为WXH,缺省160X128.下面的简写也可以直接使用:Sqcif 128X96 qcif 176X144 cif 252X288 4cif 704X576
-aspect aspect 设置横纵比 4:3 16:9 或 1.3333 1.7777
-croptop/botton/left/right size 设置顶部切除带大小,像素单位
-padtop/botton/left/right size 设置顶部补齐的大小,像素单位
-padcolor color 设置补齐条颜色(hex,6个16进制的数,红:绿:蓝排列,比如 000000代表黑色)
-vn 不做视频记录
-bt tolerance 设置视频码率容忍度kbit/s
-maxrate bitrate设置最大视频码率容忍度
-minrate bitreate 设置最小视频码率容忍度
-bufsize size 设置码率控制缓冲区大小
-vcodec codec 强制使用codec编解码方式. 如果用copy表示原始编解码数据必须被拷贝.(很重要)

-ab bitrate 设置音频码率
-ar freq 设置音频采样率
-ac channels 设置通道,缺省为1
-an 不使能音频纪录
-acodec codec 使用codec编解码

-vd device 设置视频捕获设备,比如/dev/video0
-vc channel 设置视频捕获通道 DV1394专用
-tvstd standard 设置电视标准 NTSC PAL(SECAM)
-dv1394 设置DV1394捕获
-av device 设置音频设备 比如/dev/dsp

-map file:stream 设置输入流映射
-debug 打印特定调试信息
-benchmark 为基准测试加入时间
-hex 倾倒每一个输入包
-bitexact 仅使用位精确算法 用于编解码测试
-ps size 设置包大小，以bits为单位
-re 以本地帧频读数据，主要用于模拟捕获设备
-loop 循环输入流。只工作于图像流，用于ffserver测试


一、获取视频信息

ffmpeg -i video.avi　//查看本地的视频信息

ffmpeg -i http://static.tripbe.com/videofiles/20121214/9533522808.f4v.mp4//查看远程http视频信息

二、分离视频音频流

ffmpeg -i input_file -vcodec copy -an output_file_video　　//分离视频流

ffmpeg -i input_file -acodec copy -vn output_file_audio　　//分离音频流

ffmpeg -i input.mp4 -an output.mp4             //去掉视频中的音频

ffmpeg -i test.avi -vcodec copy -an output.avi //去掉视频中的音频

三、视频转码相关

ffmpeg -i test.ts -acodec copy -vcodec copy -f mp4 output.mp4//ts视频流转mp4

ffmpeg -i test.h264 -vcodec copy -f mpegts output.ts//h264视频转ts视频流

ffmpeg -i test.h264 -vcodec copy -f mp4 output.mp4//h264视频转mp4

ffmpeg -i test.mp4 -b:v 640k output.flv //mp4转flv

ffmpeg -i test.mp4 -acodec copy -vcodec copy -f flv output.flv //mp4转flv

ffmpeg -i test.flv -b:v 640k output.mp4 //flv转mp4

ffmpeg -i test.mp4 -s 176x144 -vcodec h263 -r 25 -b 12200 -ab 12200 -ac 1 -ar 8000 output.3gp //mp4转3gp

ffmpeg -i test.avi -s aqif -vcodec -acodec mp3 -ac 1 -ar 8000 -r 25 -ab 32 -y output.3gp //avi转3gp

ffmpeg -i test.3gp -f avi -vcodec xvid -acodec mp3 -ar 22050 output.avi //3gp转flv

ffmpeg -i test.flv -s 176x144 -vcodec h263 -r 25 -b 200 -ab 64 -acodec mp3 -ac 1 -ar 8000 output.3gp //flv转3gp

ffmpeg -i test.mp4 output.avi //mp4转avi

ffmpeg -i test.flv -vcodec h264 -r 25 -b 200 -ab 128 -acodec mp3 -ac 2 -ar 44100 output.mp4 //flv转mp4

ffmpeg -i test.mp4 -c:v libx264 -ar 22050 -crf 28 output.flv //mp4转flv

ffmpeg -i test.avi -c copy -map 0 output.mp4 //avi转mp4

ffmpeg -i  http://vfile1.grtn.cn/2018/1542/0254/3368/154202543368.ssm/154202543368.m3u8 -c copy -bsf:a aac_adtstoasc -movflags +faststart test.mp4 //m3u8转mp4

ffmpeg -i test.mkv -y -vcodec copy -acodec copy output.mp4 //mkv转mp4

ffmpeg -i test.mkv -vcodec copy -acodec copy output.avi  //mkv转avi

四、图像处理相关

ffmpeg –i test.mp4 -y -f image2 -ss 8 -t 0.001 -s 350x240 testImage.jpg //截取指定时间的缩略图，-ss后跟的时间单位是秒

ffmpeg -i input.mp4 -vf "movie=logo.png [logo]; [in][logo] overlay=10:10:1 [out]" output.mp4//添加图片水印

ffmpeg -i input.mp4 -i logo.png -filter_complex overlay test1.mp4//添加图片水印

ffmpeg –i input.mp4 -acodec copy-vcodec copy -vf "movie=test.png[watermark];[in][watermark]overlay=10:10:1[out]" output.mp4//添加图片水印

ffmpeg -y -i test2.mp4 -ignore_loop 0 -i test.gif -filter_complex overlay=0:H-h test_out2.mp4//添加GIF

ffmpeg -i input.flv -vf "drawtext=fontfile=simhei.ttf: text='抖音':x=100:y=10:fontsize=24:fontcolor=yellow:shadowy=2" drawtext.mp4//添加文字水印

五、视频方向处理相关

ffmpeg -i test.mp4 -metadata:s:v rotate="90" -codec copy out.mp4//旋转90°

ffmpeg -i test.mp4 -vf "transpose=1" out.mp4//顺时针旋转90°

ffmpeg -i test.mp4 -vf "transpose=2" out.mp4//逆时针旋转90°

ffmpeg -i test.mp4 -vf "transpose=3" out.mp4//顺时针旋转90°后再水平翻转

ffmpeg -i test.mp4 -vf "transpose=0" out.mp4//逆时针旋转90°后再水平翻转

ffmpeg -i test.mp4 -vf hflip out.mp4//水平翻转视频画面

ffmpeg -i test.mp4 -vf vflip out.mp4//垂直翻转视频画面

六、视频剪切相关

ffmpeg –i test.avi –r 1 –f image2 image-%3d.jpeg//提取图片

ffmpeg -ss 0:1:30 -t 0:0:20 -i input.avi -vcodec copy -acodec copy output.avi    //-r 提取图像的频率，-ss 开始时间，-t 持续时间
//从10s处开始剪切，持续15秒（此方法有时候会遇到视频第一帧黑屏，就是未播放时候的封面是黑色的，原因是未定位到关键帧I帧）
ffmpeg -i test.mp4 -ss 10 -t 15 -codec copy cut.mp4
//从10s处开始剪切，持续15秒（此方法为优化后的方法，此剪切视频的方法可以解决第一帧黑屏问题，但是可能切割的时间落点稍稍的有一丢丢的不准确，但是应该可以落在误差范围之内）

ffmpeg -ss 10 -t 15 -i test.mp4 -codec copy cut.mp4


七、倒放音视频&加速视频&减速视频
ffmpeg.exe -i inputfile.mp4 -filter_complex [0:v]reverse[v] -map [v] -preset superfast reversed.mp4 //视频倒放，无音频

ffmpeg.exe -i inputfile.mp4 -vf reverse reversed.mp4//视频倒放，音频不变

ffmpeg.exe -i inputfile.mp4 -map 0 -c:v copy -af "areverse" reversed_audio.mp4//音频倒放，视频不变

ffmpeg.exe -i inputfile.mp4 -vf reverse -af areverse -preset superfast reversed.mp4//音视频同时倒放

ffmpeg -i inputfile.mp4 -vf setpts=PTS/2 -af atempo=2 output.mp4//视频加速，帧速率变为2倍，调整倍速范围【0.25，4】

ffmpeg -i input.mkv -an -filter:v "setpts=0.5*PTS" output.mkv//视频减速播放
八、视频拼接


这里全部以mp4文件为例子，介绍两种方法，如果不是mp4文件，可以尝试先转码为mp4后再拼接起来

1、将 mp4 先转码为 mpeg文件，mpeg是支持简单拼接的，然后再转回 mp4。

ffmpeg -i 1.mp4 -qscale 4 1.mpg　

ffmpeg -i 2.mp4 -qscale 4 2.mpg　

cat 1.mpg 2.mpg | ffmpeg -f mpeg -i - -qscale 6 -vcodec mpeg4 output.mp4             


2、使用ts拼接

先将 mp4 转化为同样编码形式的 ts 流，因为 ts流是可以 concate 的，先把 mp4 封装成 ts ，然后 concate ts 流， 最后再把 ts 流转化为 mp4。

ffmpeg -i 1.mp4 -vcodec copy -acodec copy -vbsf h264_mp4toannexb 1.ts　　

ffmpeg -i 2.mp4 -vcodec copy -acodec copy -vbsf h264_mp4toannexb 2.ts

ffmpeg -i "concat:1.ts|2.ts" -acodec copy -vcodec copy -absf aac_adtstoasc output.mp4             
九、视频的合并

下面通过动手实现抖音合拍功能来演示命令行的使用，首先准备好两个文件input1和input2，都是用手机拍摄的，高度相同；

1、两个视频的合并（注意参数hstack代表是横向合并，也就是左右合并，横向合并的形象类比为“川”；如果是竖向合并，也就是上下合并，用参数vstack，竖向或者纵向合并的形象类比为“三”)

ffmpeg -i input1.mp4 -i input2.mp4 -lavfi hstack output.mp4

注意：经过此命令处理后的视频output.mp4只会保留input1的音频
2、提取视频中的音频并合并音频

ffmpeg -i input1.mp4 -vn -y -acodec copy audio1.m4a

ffmpeg -i input2.mp4 -vn -y -acodec copy audio2.m4a

ffmpeg -i audio1.m4a -i audio2.m4a -filter_complex amerge -ac 2 -c:a libmp3lame -q:a 4 audio3.mp3
3、把audio3.mp3文件合并到output.mp4中去

    ffmpeg -i output.mp4 -i audio3.mp3 -c:v copy -c:a aac -strict experimental success.mp4

最后我们得到的success.mp4就是合拍视频，包含了两个视频的音频。



三个视频合并（input=3表示希望合并的视频的个数）

ffmpeg -i input1.mp4 -i input2.mp4 -i input3.mp4 -lavfi hstack=inputs=3 output.mp4

需要配置文件信息的发送邮件  ---> qitiancode@163.com 


