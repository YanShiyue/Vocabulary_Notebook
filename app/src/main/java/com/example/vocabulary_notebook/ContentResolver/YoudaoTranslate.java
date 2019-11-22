package com.example.vocabulary_notebook.ContentResolver;


public class YoudaoTranslate {/*


    private String url = "http://fanyi.youdao.com/openapi.do";


    private String keyfrom = "";
    private String key = "";


    private String doctype = "xml";


    private String sendGet(String str) {

        // 编码成UTF-8
        try {
            str = URLEncoder.encode(str, "utf-8");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuffer buf = new StringBuffer();
        buf.append("keyfrom=");
        buf.append(keyfrom);
        buf.append("&key=");
        buf.append(key);
        buf.append("&type=data&doctype=");
        buf.append(doctype);
        buf.append("&version=1.1&q=");
        buf.append(str);

        String param = buf.toString();

        String result = "";
        BufferedReader in = null;
        try {
            String urlName = url + "?" + param;
            URL realUrl = new URL(urlName);

            //打开和URL之间的连接
            URL Connectionconn = realUrl.openConnection();

            //设置通用的请求属性
            //conn.setRequestProperty("accept", "*
            public String getYouDaoValue (String str){
                String result = null;

                // 发送GET请求翻译
                result = sendGet(str);

                // 处理XML中的值
                int re1 = result.indexOf("<errorCode>");
                int re2 = result.indexOf("</errorCode>");
                String in = result.substring(re1 + 11, re2);
                System.out.println("===========翻译是否成功============" + in);

                if (in.equals("0")) {
                    System.out.println("翻译正常");

                    re1 = result.indexOf("<paragraph><![CDATA[");
                    re2 = result.indexOf("]]></paragraph>");
                    in = result.substring(re1 + 20, re2);
                    System.out.println("==========有道翻译================" + in);

                    re1 = result.indexOf("<ex><![CDATA[");
                    re2 = result.indexOf("]]></ex>");
                    in = result.substring(re1 + 13, re2);
                    System.out.println("==========有道词典-网络释义================" + in);

                } else if (in.equals("20")) {
                    System.out.println("要翻译的文本过长");
                    return "要翻译的文本过长";
                } else if (in.equals("30")) {
                    System.out.println("无法进行有效的翻译");
                    return "无法进行有效的翻译";
                } else if (in.equals("40")) {
                    System.out.println("不支持的语言类型");
                    return "不支持的语言类型";
                } else if (in.equals("50")) {
                    System.out.println("无效的key");
                    return "无效的key";
                }

                return result;
            }

            public static void main (String[]args){

                String str = "The weather isgood today";


                YoudaoTranslate test = newYoudaoTranslate();
                String temp = test.getYouDaoValue(str);
                System.out.println(temp);
            }
        }*/
    }




/*
# -*- coding: utf-8 -*-
        #python 27
        #xiaodeng
        #调用网易有道词典api


        import urllib
        import json

class Youdao():
        def __init__(self,word):
        self.url='http://fanyi.youdao.com/openapi.do'    #url、key、keyfrom都是固定的值，所以采用这种方式赋值
        self.key='929705525'
        self.keyfrom='pythonxiaodeng'
        self.word=word

        def getTranslation(self):
        data={'keyfrom':self.keyfrom,
        'key':self.key,
        'type':'data',
        'doctype':'json',
        'version':'1.1',
        'q':self.word}
        #encode
        data=urllib.urlencode(data)
        #print data
        #keyfrom=pythonxiaodeng&doctype=json&q=student&version=1.1&key=929705525&type=data
        url=self.url+'?'+data               #链接url和参数dict
        html=urllib.urlopen(url).read()
        html=json.loads(html)
        return html

        #调用
        youdao=Youdao('student')
        result=youdao.getTranslation()
        for key in result:
        print key*/
