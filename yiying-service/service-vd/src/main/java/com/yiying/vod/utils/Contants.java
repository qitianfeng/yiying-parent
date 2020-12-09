package com.yiying.vod.utils;
/**
 * @author zzf
 * @version 1.0
 * @date 2020/12/09 21:56
 */
public class Contants {


	public static final String ffmpegpath = "D:/develop/nginx-rtmp-win32-master/ffmpeg.exe";		// ffmpeg工具安装位置

	public static final String mencoderpath = "D:/ffmpeg/bin/mencoder"; 	// mencoder工具安装的位置
	
	public static final String videofolder = "D://Projectpicture/website/temp/"; 	// 需要被转换格式的视频目录
	public static final String remoteVideofolder = "/roote/website/temp/"; 	// 需要被转换格式的视频目录

	public static final String videoRealPath = "D://Projectpicture/websiteimage/temp/"; 	// 需要被截图的视频目录
	
	public static final String targetfolder = "D://Projectpicture/website/finshvideo/"; // 转码后视频保存的目录
	public static final String remoteTargetfolder = "/root/website/finshvideo/"; // 转码后视频保存的目录

	public static final String imageRealPath = "D://Projectpicture/websiteimage/finshimg/"; // 截图的存放目录
	public static final String remoteImageRealPath = "/root/websiteimage/finshimg/"; // 远端存储截图的存放目录



}
