package com.hs.common.update;

/**
 * Created by jilil on 2018/12/3.
 *
 * name	string	名称
 version	string	版本
 type	string	类型
 qrcodeDownAddress	string	二维码下载地址
 fileDownAddress	string	文件下载地址
 size	string	文件大小
 status	Intteger	可更新状态 （0 否 1是）
 updateHint	string	版本更新提示语
 updateTime	string	版本更新时间
 isConstraint	Intteger	强制更新（0 否 1是）
 */

public class AppVersionBean {

    public String size;

    public String qrcodeDownAddress;

    public int isConstraint;

    public String updateHint;

    public String name;

    public String updateTime;

    public String id;

    public String type;

    public int version;

    public String fileDownAddress;

    public int status;

}
