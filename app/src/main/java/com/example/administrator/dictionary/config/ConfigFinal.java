package com.example.administrator.dictionary.config;

/**
 * Created by Administrator on 2016/5/14.
 */
public class ConfigFinal {

    public final static int SHOW = 1;               //联网获取数据
    public final static int ADD_LISTBOOK = 2;
    public final static int SQL_MESSAGE = 3;          //历史搜索

    public final static int SOUND = 4;                //

    public final static String SELECT = "select";
    public final static String NO_SELECT = "no_select";
    public final static String NO_SUCCESS = "error";


    /*
    * 数据库名
    * */
    public final static String SQL_NAME = "Dictionary";

    /*
    * 数据库版本
    * */
    public final static int SQL_VERSION = 1;

    /*
    * 表名
    * */
    public final static String TB_HISTORY = "history";
    public final static String TB_LISTBOOK = "listbook";
    public final static String TB_DAILY = "daily";

    /*
    * 金山词霸词典json
    * */
    public final static String KING_BASE = "http://dict-co.iciba.com/api/dictionary.php?";
    public final static String KING_KEY = "117325ED55219C3AB327502258ECFA64";
    public final static String KING_TYPE = "json";


    /*
    * 每日一句json
    * */
    public final static String IMAGE_BASE = "http://open.iciba.com/dsapi/?date=";
    public final static int IMAGE = 1;          //联网模式
    public final static int SD_IMAGE = 2;        //SD卡寻找模式
    public final static int REFRESH = 3;           //刷新模式

    public final static int NORMAL=4;              //断网模式
    public final static String NORMAL_SENTENCE="I like the dreams of the future better than history of the past!";
    public final static String NORMAL_CONTENT="对于回忆过去,我更喜欢憧憬未来!";


    /*
    * 有道词典json
    * */
    public final static String YOUDAO_BASE = "http://fanyi.youdao.com/openapi.do";
    public final static String YOUDAO_KEY_FROM = "Williamchen";
    public final static String YOUDAO_KEY = "1982275727";
    public final static String YOUDAO_DOC_TYPE = "data";
    public final static String YOUDAO_TYPE = "json";
    public final static String YOUDAO_VERSION = "1.1";

    /*
    * 多选模式
    * */
    public final static int LISTBOOK_MODE = 3;
    public final static int HISTORY_MODE = 1;


    /*
    * 单选模式
    * */
    public final static int LISTBOOK_SINGLE = 2;
    public final static int HISTORY_SINGLE = 0;


    /*
    * 单词表排序模式
    * */
    public final static int NOMAL = 1;
    public final static int CHAR = 2;


    /*
    * 服务
    * */
    public final static int WAIT = 0;
    public final static int START = 1;        //开启


    /*
    * 各种显示处理
    * */


    /*
    * 模糊搜索
    * */
    public final static int SEARCH = 1;
    public final static int NOT_SEARCH = 0;


    /*
     *本地或联网搜索
     * */
    public final static int JSON = 0;
    public final static int SQL = 1;


    /*
    * 网络是否超时
    * */
    public final static int NOT_CONNECT = 0;
    public final static int CONNECT = 1;


    /*
    * 判断单词是否单词本
    * */
    public final static int HAS_LISTBOOK = 20;
    public final static int NO_LISTBOOK = 10;


    /*
    * 隐藏注释
    * */
    public final static int HIDE = 100;
    public final static int NOT_HIDE = 200;


    /*
    * 背单词的单词数量
    * */
    public final static int WORD_COUNTS = 1024;


    /*
    * 读取文件是否出错
    * */
    public final static int EROOR = -1;
    public final static int RIGHT = 0;


}
