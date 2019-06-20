package com.homework.teacher;

import com.homework.teacher.data.GradeSubject;

import java.util.List;

public class Consts {

    public static final boolean DEBUG_MODE = false;

    public static final String USER_SERVICE_URL = "http://www.baidu.com";
    // 后厨直播背景图片地址
    public static final String IMAGE_LIVE_BG_URL = "https://vsc-img.oss-cn-shanghai.aliyuncs.com/live/live.jpg";
    // 营业执照图片地址
    public static final String IMAGE_BUSILICENSE_URL = "https://vsc-img.oss-cn-shanghai.aliyuncs.com/license/busiLicense.jpg";
    // 食品经营许可证图片地址
    public static final String IMAGE_FOODLICENSE_URL = "https://vsc-img.oss-cn-shanghai.aliyuncs.com/license/foodLicense.jpg";
    public static final String IMAGE_LICENSE_URL = "https://vsc-img.oss-cn-shanghai.aliyuncs.com/license/license.jpg";

    public static String SERVER_IP;
    public static String LIVE_URI;// 作业教师版直播后台推流地址
    public static final String businessId = "vsichuLive";// 自定义的业务名称，仅对于日志有关
    public static final String AccessKeyId = "LTAIgOCfNWt6kzEF";// 是在阿里平台上申请的accessKeyId
    public static final String AccessKeySecret = "JY7shLHC67N9sAEJMyBWRn8SR0rcnL"; // 是在阿里平台上申请的accessKeySecret
    // 两个是配套出现的
    // 微信支付，子商户的APPID
    public static final String APP_ID = "wxea2e3d27a054bd34";

    static {
        if (DEBUG_MODE) {
            /** 作业教师版测试环境地址 */
            // SERVER_IP = "http://120.27.138.158:80";
            SERVER_IP = "http://boss-test.vsichu.com";
            // LIVE_URI = "http://live.vsichu.com/VSC/test01.flv";
            LIVE_URI = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
        } else {
            /** 作业教师版生产环境地址 */
//			SERVER_IP = "https://boss.vsichu.com";
            SERVER_IP = "http://47.99.148.79:8080";
            LIVE_URI = "http://live.vsichu.com/VSC/test01.flv";
            // LIVE_URI = "rtmp://live.vsichu.com/VSC/test01";
        }
    }

    //	public static String SERVER_URL = SERVER_IP + "/boss/api";
    public static String SERVER_URL = SERVER_IP + "/hw/api";


    public static String SERVER_sessionFetch = SERVER_URL + "/session/fetch";// 获取会话凭证
    public static String SERVER_tokenFetch = SERVER_URL + "/token/fetch";// 获取接口访问令牌
    public static String SERVER_queryByKey = SERVER_URL
            + "/baseData/queryByKey";// 查询基础数据
    public static String SERVER_queryProductTypeByCityId = SERVER_URL
            + "/cityProduct/type/queryByCityId";// 查询城市产品类型
    public static String SERVER_getCityProduct = SERVER_URL
            + "/cityProduct/list";// 查询城市产品列表
    public static String SERVER_getFestival = SERVER_URL + "/festival/all";// 查询节假日
    public static String SERVER_getTakePoint = SERVER_URL
            + "/takePoint/getTpList";// 查询取餐点接口
    public static String SERVER_getSmsvercode = SERVER_URL + "/smsvercode/get";// 获取短信验证码接口
    public static String SERVER_doLogin = SERVER_URL + "/login/doLogin";// 短信验证码登录（注册）接口
    public static String SERVER_createOrQueryOrder = SERVER_URL
            + "/order/createOrQueryOrder";// 查询或创建某一餐的订单（单个下单、待提交状态）
    public static String SERVER_iodCart = SERVER_URL + "/cart/iodCart";// 增加或减少购物车中的商品
    public static String SERVER_orderQuery = SERVER_URL + "/order/query";// 查询订单信息
    public static String SERVER_getTakeTime = SERVER_URL
            + "/common/takeTime/list";// 查询取餐时间
    public static String SERVER_getCustomerInfo = SERVER_URL + "/customer/info";// 查询客户信息
    public static String SERVER_saveOrder = SERVER_URL + "/order/saveOrder";// 保存订单基本信息
    public static String SERVER_clearCart = SERVER_URL + "/cart/clear";// 清空购物车
    public static String SERVER_submitOrder = SERVER_URL + "/order/submitOrder";// 提交订单
    public static String SERVER_updateInfo = SERVER_URL
            + "/customer/updateInfo";// 修改客户基本信息
    public static String SERVER_intExcVou = SERVER_URL + "/customer/intExcVou";// 积分兑换代金券接口
    public static String SERVER_listIntegralRecord = SERVER_URL
            + "/customer/listIntegralRecord";// 积分交易记录查询接口
    public static String SERVER_listVoucher = SERVER_URL
            + "/customer/listVoucher";// 代金券查询接口

    //---homework---
    public static String SERVER_register = SERVER_URL + "/common/teacher/register";// 教师端-注册
    //	public static String SERVER_getSalt = SERVER_URL + "/customer/salt";// 取盐接口
    public static String SERVER_getSalt = SERVER_URL + "/common/teacher/salt";// 教师端-取盐接口
    public static String SERVER_login = SERVER_URL + "/common/teacher/login";// 教师端-登录
    public static String SERVER_logout = SERVER_URL + "/teacher/logout";// 退出登录

    public static String SERVER_getClassMaster = SERVER_URL + "/teacher/class/master";// 查询我任班主任的班级
    public static String SERVER_getClassSubject = SERVER_URL + "/teacher/class/subject";// 查询我任教的班级

    public static String SERVER_getGradeList = SERVER_URL + "/teacher/school/grade/list";// 查询当前登录教师所在学校的年级列表
    public static String SERVER_listCanBeApply = SERVER_URL + "/teacher/school/class/listCanBeApply";// 查询可申请班级列表
    public static String SERVER_masterApply = SERVER_URL + "/teacher/class/master/apply";// 班主任申请

    public static String SERVER_getSubject = SERVER_URL + "/teacher/school/subject";// 查询当前登录教师所在学校的所有学科
    public static String SERVER_getGradeBySubject = SERVER_URL + "/teacher/school/grade";// 通过学科查询当前学校开设了此课程的年级
    public static String SERVER_getClassByGrade = SERVER_URL + "/teacher/grade/class";// 查询年级-班级列表
    public static String SERVER_subjectApply = SERVER_URL + "/teacher/class/subject/apply";// 任课教师申请

    public static String SERVER_teacherGradeSubject = SERVER_URL + "/teacher/grade/subject";// 查询当前登录教师加入的备课组
    public static String SERVER_workSheetCreate = SERVER_URL + "/teacher/workSheet/create/";// 创建作业单
    public static String SERVER_workSheet = SERVER_URL + "/teacher/workSheet/root/list/";// 作业单列表查询接口
    public static String SERVER_save = SERVER_URL + "/teacher/workSheet/save";// 保存作业单
    public static String SERVER_catalog = SERVER_URL + "/teacher/workSheet/medium/catalog";// 查询作业介质目录树
    public static String SERVER_classSubject = SERVER_URL + "/teacher/class/subject/";// ⦁按年级查询任教班级
    public static String SERVER_CLASS_SUBJECT = SERVER_URL + "/teacher/class/subject";// 查询我任教的班级
    public static String SERVER_PAPER_ADD = SERVER_URL + "/teacher/workSheet/medium/paper/add";// 添加试卷
    public static String SERVER_CATALOG_ADD_SUB = SERVER_URL + "/teacher/workSheet/medium/catalog/addSub";// 添加下级目录
    public static String SERVER_MEDIUM_CATALOG = SERVER_URL + "/teacher/workSheet/medium/catalog/";// 查询单个作业介质目录树
    public static String SERVER_UPDATE_NAME = SERVER_URL + "/teacher/workSheet/medium/catalog/updateName";// 修改目录名称
    public static String SERVER_LOGIC_DEL = SERVER_URL + "/teacher/workSheet/medium/logicDel/";// 删除作业介质（或目录）
    public static String SERVER_CATALOG_SEQ_CHANGE = SERVER_URL + "/teacher/workSheet/catalog/seq/change/";// 删除作业介质（或目录）
    public static String SERVER_INTERACT_QUE_ADD = SERVER_URL + "/teacher/workSheet/interact/que/add";// 互动作业模块添加题号
    public static String SERVER_INTERACT_QUE = SERVER_URL + "/teacher/workSheet/interact/que/";// 显示或隐藏题号
    public static String SERVER_ADD_ANSWER = SERVER_URL + "/teacher/workSheet/answer/add";// 添加答案
    public static String SERVER_ANSWER_LIST = SERVER_URL + "/teacher/workSheet/answer/list/";// 查询答案列表


    public static String SERVER_TENCENT_SIGN = SERVER_URL + "/teacher/video/tencent/signature";// 获取腾讯视频签名


    //---homework---

    public static String SERVER_changePassword = SERVER_URL
            + "/customer/changePwd";// 修改密码接口
    public static String SERVER_accountLogin = SERVER_URL
            + "/login/accountLogin";// 账号密码登录接口
    public static String SERVER_qryPrdMealCate = SERVER_URL
            + "/recipe/qryPrdMealCate";// 一周食谱-查询某商品已选餐别接口
    public static String SERVER_aodPrdMealCate = SERVER_URL
            + "/recipe/aodPrdMealCate";// 一周食谱-添加或删除某商品所选餐别
    public static String SERVER_countPrdType = SERVER_URL
            + "/recipe/countPrdType";// 一周食谱-已选商品品类统计接口
    public static String SERVER_qrySelectedPrd = SERVER_URL
            + "/recipe/qrySelectedPrd";// 一周食谱-已选商品列表查询接口
    public static String SERVER_deletePrd = SERVER_URL + "/recipe/deletePrd";// 一周食谱-删除某一商品接口
    public static String SERVER_countWeekPrd = SERVER_URL
            + "/recipe/countWeekPrd";// 一周食谱-餐别及商品数量统计接口
    public static String SERVER_qryDayPrd = SERVER_URL + "/recipe/qryDayPrd";// 一周食谱-根据餐别查询已选商品列表接口
    public static String SERVER_iodPrd = SERVER_URL + "/recipe/iodPrd";// 一周食谱-增减商品份数
    public static String SERVER_dinnerPlan = SERVER_URL
            + "/customer/dinnerPlan";// 用餐计划-餐别查询接口
    public static String SERVER_setDinnerPlan = SERVER_URL
            + "/customer/setDinnerPlan";// 用餐计划-餐别设置接口
    public static String SERVER_queryDinnerPlan = SERVER_URL
            + "/customer/queryDinnerPlan";// 一周食谱-用餐计划查询（添加）接口
    public static String SERVER_updateDinnerPlan = SERVER_URL
            + "/customer/updateDinnerPlan";// 一周食谱-修改用餐计划接口
    public static String SERVER_batchOrder = SERVER_URL + "/recipe/batchOrder";// 一周食谱-批量下单接口
    public static String SERVER_orderDelete = SERVER_URL + "/order/delete";// 删除订单接口（软删除）
    public static String SERVER_orderSettle = SERVER_URL + "/order/settle";// 订单（批量）结算接口
    //	public static String SERVER_logout = SERVER_URL + "/login/logout";// 退出登录接口
    public static String SERVER_getProductDetail = SERVER_URL
            + "/cityProduct/detail";// 查询城市产品详情接口
    public static String SERVER_statEvaNum = SERVER_URL
            + "/cityProduct/statEvaNum";// 按评分等级统计城市产品的好评、中评、差评数量
    public static String SERVER_getEvaInfo = SERVER_URL
            + "/cityProduct/getEvaInfo";// 查询城市产品点评信息接口
    public static String SERVER_passwordReset = SERVER_URL
            + "/customer/password/reset";// 重置密码接口
    public static String SERVER_stateNum = SERVER_URL + "/order/stateNum";// 按状态统计订单量接口
    public static String SERVER_listByState = SERVER_URL + "/order/listByState";// 按状态批量查询订单接口
    public static String SERVER_unSubscribe = SERVER_URL
            + "/payment/unSubscribe";// 订单退订接口
    public static String SERVER_evaSubmit = SERVER_URL + "/order/evaSubmit";// 提交点评接口
    public static String SERVER_clearOrd = SERVER_URL + "/order/clearOrd";// 清空客人某餐别下的订单（软删除）
    public static String SERVER_listRecName = SERVER_URL
            + "/recipe/listRecName";// 查询推荐食谱名称列表
    public static String SERVER_getRecDetail = SERVER_URL
            + "/recipe/getRecDetail";// 根据餐别查询推荐食谱明细
    public static String SERVER_recAddToMyRecipe = SERVER_URL
            + "/recipe/recAddToMyRecipe";// 添加推荐食谱到我的食谱
    public static String SERVER_getPaymentString = SERVER_URL
            + "/payment/getPaymentString";// 获取付款单加签字符串
    public static String SERVER_syncPayinfo = SERVER_URL
            + "/payment/syncPayinfo";// 支付结果同步通知
    public static String SERVER_checkUpdate = SERVER_URL
            + "/baseData/checkUpdate";// 检查更新


    public static String REQUEST_SUCCEED = "0";
    public static int REQUEST_SUCCEED_CODE = 0;

    public static List<GradeSubject> mSubjectList;

//	1：课本，2：习题册，3：试卷，4：其他

    public static final int EXRCISE_BOOK = 2;
    public static final int PAPER = 3;
    public static final int OTHER = 4;

}
