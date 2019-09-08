package com.je.message.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.je.thrid.util.Note253Util;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;

/**
 * 短信工具类
 * @author zhangshuaipeng
 *
 */
public class NoteUtil {
	public static Integer send_count=0;
	private static NoteUtil noteUtil=null;
	private static List<String> errorText=new ArrayList<String>();
	static{
		String errorContext="聚宝金融集团/房地产/地产商/地产开发/江贼民/tataUFO/益投网贷/限量版/返点卡/周年/钥匙扣/天水生活网/美乐商城/中国牛仔服饰网/大奖/一等奖/二等奖/特等奖/代开/全民富联盟/艾美美妆/华荣超市/欢乐庆店/2周年/真人/登录官网/特约/赞助/促销/李克强/央企/中移动/常务会议/不整顿/任人唯亲/K粉/地产/中奖/安理会/白粉/绑架/报仇/抗议/暴乱/暴政/爆炸装置/北京当局/精液/避孕套/藏独/操B/操逼/操你/慈善/贷款/弹劾/弹药/党专制/倒卖/盗卖/抵押/抵制/电信公司/动乱/毒品/毒瘾/放款/坟墓/封杀/封锁/睾丸/个股/共产党/股指/龟头/国家领导/国家主席/国家总统/黑火药/黑社会/护法/火葬/鸡巴/集会/监听器/监狱/讲法/可卡因/刻章/恐怖主义/联通公司/两性视频/流氓/留学/六合彩/楼盘/卵巢/乱伦/轮奸/裸体/迷昏药/迷魂药/迷奸药/谋杀/穆斯林/你妈/牛股/牛市/迫害/菩萨/枪毙/枪支/强暴/强奸/抢劫/情色/全球通/权威/人大代表/人权/蹂躏/乳房/软禁/三级片/三陪/三唑仑/三唑伦/散户/桑拿/骚逼/色情/杀人/杀手/傻逼/少妇/射精/伸冤/尸/示威/兽交/私募/死亡/台独/太子党/讨债/套牌/特价房/天安门/挑逗/偷盗/偷拍/偷情/退党/西藏/吸毒/邪恶/邪魔/新股发行/信贷/性爱/性伴侣/性交/性奴/性虐/摇头丸/一夜情/移动公司/移民/阴唇/阴道/阴蒂/阴茎/淫荡/淫水/淫娃/淫邪/淫穴/瘾君子/诈骗/炸弹/炸药/真善美/真善忍/镇压/整形/政协委员/中共政府/中国电信/中国联通/中国移动/中央政府/众生/追债/自杀/总书记/诅咒/尊龙/做爱/黄皮/法伦功/中国移动通信/绿色环保手机/IP17908/大出血/跳楼价/洪志法轮/真善忍/发伦/发抡/发沦/发囵/发仑/发纶/法纶/法仑/法囵/法沦/法抡/法伦/功友 弟子/法论/发论/法，轮，功/法 轮 功/自焚/自 焚/自焚/教徒/人权/迫害/正法/玄机/江独裁/江八点/江泽民/朱鎔基/胡锦涛/温家宝/十六大/共产党/政治风波/疆独/民运/古怪歌/推翻/示威/政变/静坐/分裂/吕秀莲/中华民国/造反/新华内情/达赖/镇压/东突/游行/上访/罢课/罢工/一首歌/广闻/压迫/反革命/疆独/无能/新闻封锁/六合彩/色情/嫖娼/三陪/他妈的/龙卷风/王八蛋/笨蛋/走私/强奸/嫖妓/妓女/舞女/嫖娼/鸟人/混蛋/性生活/彩票/你大爷/舞女/死刑/坏蛋/坏人/吃屎/可恶/秃驴/吃屎/烂货/婊子/死B/犯贱/尿尿/恨你/套子/爱死你/你妈逼/体彩/犯罪/罪犯/起诉/男妓/我操/我靠/操你/操他/操她/狗屎/男妓/起诉/福彩/阴部/偷拍/白痴/考试答案/犯罪/通缉/梅毒/淋病/吃鸡/性爱/八婆/阴唇/亏心/还钱/还我钱/欠钱/放屁/讨债/躲债/小便/解手/狗娘/GOU娘/王八蛋/傻逼/傻X/犯罪/罪犯/私募/麻将/赌博/私服/作贱/做贱/贱人/捉奸/被捕/死逼/死B/代 开/贱B/你妈B/淫照/荡妇/淫荡/走狗/回扣/骂我/骂你/真贱/贱货/更贱/艾滋病/杀人/慈善总会/房海通/奶奶的/陪葬/肉麻/百家乐/投注/鸡巴/日你/勾引/骚扰/外遇/传票/轮奸/骗子/e95566.tk/5173提醒/5173网络/骰子/色子/建行提醒/你-妈/你 妈/警方/民航出票/按摩/黄金投资/魔鬼/老婆/老公/收敛/暧昧/执迷/内疚/后悔/屎/坑人/老实点/踏一宫/套现/绝情/离婚/恶心/恶毒/非法/举报/淫威/养女人/玩女人/抢劫/耶稣/佛教/宗教/中共/你妹的/变态/诱惑/f轮/fuck/hongzhi/IP17908/lihongzhi/minghui/minghuinews/NULL/piaoxue/playboy/PlayBoy/SIM卡抽奖/Soccer01.com/TMD/zhuanfalun/巴赫/罢工/罢课/包过/包赢/保钓/保过/暴乱/本 金/屄/逼样/婊子/薄谷开来/薄瓜瓜/薄西来/薄熙来/藏独/操你娘/草你妈/赤裸/蠢猪/达赖/大参考/大法/大纪元/代开发票/代人/弹药/邓小平/钓鱼岛/东突/东突厥斯坦/动乱/多维/发抡/发仑/局长/发伦/发囵/发沦/发纶/发轮/发论/法 轮 功/法.轮.功/法抡/法仑/法伦/法囵/法沦/法纶/法轮/法轮功/法论/反革命/房贷/放 款/放款/非典/复仇/干你娘/肛门/功友/共产/共产党/古怪歌/国耻/国研新闻邮件/何勇/红 歌/红歌/宏志/洪志/胡锦涛/花花公子/回民/婚外情/鸡巴/鸡毛信文汇/集会/妓女/家庭电台/假钞/简鸿章/贱人/测试/江八点/江独裁/江泽民/疆独/教徒/锦涛/经文/精子/靖国神社/九、评/九.评/九码/九评/九-评/抗日/抗议/快感/拉丹/拉登/李录/李鹏/利 息/联总之声/联总之声/灵动卡/刘晓波/六.合.彩/六合彩/六码/吕秀莲/绿色环保手机/轮大/罗干/卖国/瞒报/毛片/毛泽东/美国之音/迷药/民运/民猪/你他妈/牛逼/嫖娼/枪支/强奸/窃听器/人民报/人民报讯/人民大众时事参考/人民内情真相/人民真实报道/日你/日你妈/日他/肉棒/乳房/三陪/色情/傻B/上访/深圳红岭/审判日/圣经/十六大/屎/示威/私募/他妈的/台*湾/天安门/天葬/通信维权/突厥斯坦/推翻/王八蛋/王立军/网特/旺铺/温家宝/我X X你/我XX你/我操/我日/香总部/小产权房/小鸡鸡/邪恶/新华举报/新华内情/新闻封锁/信用卡提现/性福/性交/修炼/畜生/玄`机/压迫/阳痿/耶稣/一夜情/伊斯兰运动/阴 水/阴道/阴精/淫秽/银行联合管理局/印尼伊斯兰祈祷团/游行/舆论/造反/泽民/兆赫/真善忍/镇压/正法/政变/政治风波/中俄边界新约/中共/中华民国/朱琳/朱容基/朱鎔基/追帐/自焚/自由运动/走私/做爱/尼玛/暗爽/乱世/表白/工商卡/去死/派彩/彩金/十八大/坐牢/1hccc.com/跳楼/口交/A级/安乐死/氰化钾/致癌药/打炮/新党/贪婪/死亡/卫生巾/傻b/情何以堪/留学/胡书记/美女/出水/交友网/发情/取精/房产/蝴蝶兰/大奖/培訓/移動/黄志转/别墅/扣款/18大/诚邀/兑奖/港行/永乐/4000-115-156/房源/隐私/环球加美置业/基督/电视台/人渣/教养/您是不是旷工了/贷款/反腐败/激情/西藏/抵押/乐活争霸赛/独立/作弊/诈骗/赢得奖金/银行账号/外教/选举/假证/注册即送/中南海/习近平/信访/军转干部/生殖/统战部/阴水/中奖/基地/看中/总书记/短信平台/东洲/房地产/笔记本/毒死/国防部/最新测试/_张/ubscription/逮捕/9折促销/十八大/尖阁列岛/尖閣列島/尖閣列島/钓鱼岛/釣魚島/家庭电台/共产党/火葬场/中国移动/中国联通/中国电信/广电总局/国防部/暗杀/杀死/电视台/抢劫/强暴/圣经/耶稣/审判日/阴道/海洛因/冰毒/赌博/杀死/毒死/阴唇/阴毛/绑架/法轮功/佛教/宗教/基督教/法轮大法/法轮弟子/屄命/逼毛/婊子/冰毒/藏独/操逼/杀死你/干死你/高官挥霍公款/高丽棒子/高校暴乱/高校群体事件/高校骚乱/贱逼/贱比/贱货/作爱/作秀/做爱/装屄/装逼/自焚/自民党/自杀/自杀手册/自杀指南/自制手枪/自治机关/宗教/走私/小鸡鸡/操逼/性交/性爱/乳头/口交/乳交/江独裁/法.轮.功/自焚/中华民国/造反/政变/嫖娼/江泽民/新闻封锁/新华举报/人民大众时事参考/人民内情真相/人民真实报道/大纪元/鸡毛信文汇/联总之声传单/美国之音/中俄边界新约/国研新闻邮件/简鸿章/淫秽/反革命/突厥斯坦/印尼伊斯兰祈祷团/东突厥斯坦伊斯兰运动/胡锦涛/温家宝/枪支/迷药/婊子/暴乱/代开发票/华人媒体/肉棒/做爱/21世纪中国基金会/610洗脑班/爱国者同盟/安魂网/暴政/北大三角地论坛/北韩/北京当局/北京政权/北美自由论坛/春夏自由论坛/大纪元新闻网/大陆当局/大中华论坛/大众真人真事/戴相龙/弹劾/邓小平/邓颖超日记/迪里夏提/地下教会/地下刊物/电视流氓/东方时空/东南西北论谈/东土耳其斯坦/东西南北论坛/动乱/独裁/独裁政治/独立台湾会/独立中文笔会/二逼/发抡功/发伦功/法抡功/法轮功/法谪/法谪功/反封锁/反封锁技术/反腐败论坛/反攻大陆/反人类/反社会/方励之/方舟子/放入春药/飞扬论坛/斐得勒/废除劳教签名/粉嫩小洞/粉饰太平/风雨神州/风雨神州论坛/封从德/封杀/肛交/肛门/高文谦/高薪养廉/高自联/睾丸/狗操/狗卵子/狗娘/狗屎/贯通两极法/龟头/国贼/汉奸/和平请愿书/红灯区/红魂网站/红色恐怖/胡紧掏/胡锦滔/胡总书记/华通时事论坛/华夏文摘/华语世界论坛/华岳时事论坛/基督/激情大片/激情电影/集体上访/集体做爱/贾庆林/奸淫/贱逼/贱比/贱货/贱人/江澤民/江泽民/胡锦涛/茳澤民/僵贼/僵贼民/交媾/惊暴双乳/逮捕/军转安置/军转干部/开放杂志/抗议/跨世纪的良心犯/狂操/邝锦文/劳动教养所/理想信念斗争/联合起诉最高人民法院/廉政大论坛/炼功/梁光烈/梁擎墩/两岸关系/新官上任/两岸三地论坛/林昭纪念奖/毛泽东/毛贼东/冤案/强奸/蒙古独/蒙古独立/孟令伟/民进党/民意论坛/民主墙/民族矛盾/民族问题/木子论坛/奶头/南大自由论坛/南华早报/倪育贤/你说我说论坛/潘国平/盘古乐队/屁眼/青天白日旗/情色大片/情色电影/去你妈/群奸/群体灭绝/热站政论网/人民真实/人民之声论坛/仁吉旺姆/日内瓦金融/日死你/日元贷款/瑞士金融大学/骚逼/骚货/骚穴/傻逼/善恶有报/上海帮/上海孤儿院/邵家健/神通加持法/死者/台湾独立/台湾狗/台湾建国运动组织/台湾青年独立联盟/台湾政论区/台湾自由联盟/太子党/陶驷驹/讨伐/讨伐中宣部/滕文生/天安门/统独论坛/统战/涂运普/屠杀/退党/外交论坛/外交与方略/外蒙/西藏独/西藏论坛/小穴/新观察论坛/新华通论坛/新疆独/新疆独立/性爱/性交/性欲/亚洲自由之声/阎明复/燕南评论/央视内部晚会/夜话紫禁城/一党专政/一党专制/异见人士/异议人士/阴唇/阴道/阴蒂/阴茎/阴水/淫荡/淫水/淫图/淫穴/尹庆民/英语四六级答案/舆论反制/正邪大决战/正义党论坛/政治反对派/政治犯/指点江山论坛/致胡书记的公开信/中俄边界/中俄密约/中共/中国报禁/中国泛蓝联盟/中国复兴论坛/中国改革年代政治斗争/中国孤儿院/中国和平/中国论坛/中国社会的艾滋病/中国社会进步党/中国社会论坛/中国威胁论/中国问题论坛/中国真实内容/中国之春/中国猪/中國當局/中华大地/中华讲清/中华人民实话实说/中华人民正邪/中华时事/中华养生益智功/中华真实报道/中宣部/钟山风雨论坛/周恩来忏悔/周恩来后悔/周恩来自责/周锋锁/周刊纪事/转法轮/自民党/自已的故事/自由民主论坛/自由写作奖/自由亚洲/自主择业/宗教压迫/走私/做爱经典/做爱全过程/做爱挑逗/处女终结者/覆中国政权/颠覆中华人民共和国政/东突厥斯坦/东突厥斯坦伊斯兰/赌具/短信商务广告/法L功/法轮大法/反腐败/分裂中国/分裂中华人民共和国/封杀酱猪媳/高利贷/狗逼/华夏文摘快递/军委主席/李洪志/梦网洪志/窃听/人民大众/人民之声/三水法轮/省委大门集合/省政府大门集合/死亡/台湾共合国/天府广场集会/推翻社会主义制度/伊斯兰运动/政府评理/中共当局/中共独裁/中共统治/十八大/尖阁列岛/尖閣列島/尖閣列島/钓鱼岛/釣魚島/家庭电台/共产党/火葬场/中国移动/中国联通/中国电信/广电总局/国防部/暗杀/杀死/电视台/抢劫/强暴/圣经/耶稣/审判日/阴道/海洛因/冰毒/赌博/杀死/毒死/阴唇/阴毛/绑架/法轮功/佛教/宗教/基督教/法轮大法/法轮弟子/屄命/逼毛/婊子/冰毒/藏独/操逼/杀死你/干死你/高官挥霍公款/高丽棒子/高校暴乱/高校群体事件/高校骚乱/贱逼/贱比/贱货/作爱/作秀/做爱/装屄/装逼/自焚/自民党/自杀/自杀手册/自杀指南/自制手枪/自治机关/宗教/走私/小鸡鸡/操逼/性交/性爱/乳头/口交/乳交/江独裁/法.轮.功/自焚/中华民国/造反/政变/嫖娼/江泽民/新闻封锁/新华举报/人民大众时事参考/人民内情真相/人民真实报道/大纪元/鸡毛信文汇/联总之声传单/美国之音/中俄边界新约/国研新闻邮件/简鸿章/淫秽/反革命/突厥斯坦/印尼伊斯兰祈祷团/东突厥斯坦伊斯兰运动/胡锦涛/温家宝/枪支/迷药/婊子/暴乱/代开发票/华人媒体/肉棒/做爱/21世纪中国基金会/610洗脑班/爱国者同盟/安魂网/暴政/北大三角地论坛/北韩/北京当局/北京政权/北美自由论坛/春夏自由论坛/大纪元新闻网/大陆当局/大中华论坛/大众真人真事/戴相龙/弹劾/邓小平/邓颖超日记/迪里夏提/地下教会/地下刊物/电视流氓/东方时空/东南西北论谈/东土耳其斯坦/东西南北论坛/动乱/独裁/独裁政治/独立台湾会/独立中文笔会/二逼/发抡功/发伦功/法抡功/法轮功/法谪/法谪功/反封锁/反封锁技术/反腐败论坛/反攻大陆/反人类/反社会/方励之/方舟子/放入春药/飞扬论坛/斐得勒/废除劳教签名/粉嫩小洞/粉饰太平/风雨神州/风雨神州论坛/封从德/封杀/肛交/肛门/高文谦/高薪养廉/高自联/睾丸/狗操/狗卵子/狗娘/狗屎/贯通两极法/龟头/国贼/汉奸/和平请愿书/红灯区/红魂网站/红色恐怖/胡紧掏/胡锦滔/胡总书记/华通时事论坛/华夏文摘/华语世界论坛/华岳时事论坛/基督/激情大片/激情电影/集体上访/集体做爱/贾庆林/奸淫/贱逼/贱比/贱货/贱人/江澤民/江泽民/胡锦涛/茳澤民/僵贼/僵贼民/交媾/惊暴双乳/逮捕/军转安置/军转干部/开放杂志/抗议/跨世纪的良心犯/狂操/邝锦文/劳动教养所/理想信念斗争/联合起诉最高人民法院/廉政大论坛/炼功/梁光烈/梁擎墩/两岸关系/新官上任/两岸三地论坛/林昭纪念奖/毛泽东/毛贼东/冤案/强奸/蒙古独/蒙古独立/孟令伟/民进党/民意论坛/民主墙/民族矛盾/民族问题/木子论坛/奶头/南大自由论坛/南华早报/倪育贤/你说我说论坛/潘国平/盘古乐队/屁眼/青天白日旗/情色大片/情色电影/去你妈/群奸/群体灭绝/热站政论网/人民真实/人民之声论坛/仁吉旺姆/日内瓦金融/日死你/日元贷款/瑞士金融大学/骚逼/骚货/骚穴/傻逼/善恶有报/上海帮/上海孤儿院/邵家健/神通加持法/死者/台湾独立/台湾狗/台湾建国运动组织/台湾青年独立联盟/台湾政论区/台湾自由联盟/太子党/陶驷驹/讨伐/讨伐中宣部/滕文生/天安门/统独论坛/统战/涂运普/屠杀/退党/外交论坛/外交与方略/外蒙/西藏独/西藏论坛/小穴/新观察论坛/新华通论坛/新疆独/新疆独立/性爱/性交/性欲/亚洲自由之声/阎明复/燕南评论/央视内部晚会/夜话紫禁城/一党专政/一党专制/异见人士/异议人士/阴唇/阴道/阴蒂/阴茎/阴水/淫荡/淫水/淫图/淫穴/尹庆民/英语四六级答案/舆论反制/正邪大决战/正义党论坛/政治反对派/政治犯/指点江山论坛/致胡书记的公开信/中俄边界/中俄密约/中共/中国报禁/中国泛蓝联盟/中国复兴论坛/中国改革年代政治斗争/中国孤儿院/中国和平/中国论坛/中国社会的艾滋病/中国社会进步党/中国社会论坛/中国威胁论/中国问题论坛/中国真实内容/中国之春/中国猪/中國當局/中华大地/中华讲清/中华人民实话实说/中华人民正邪/中华时事/中华养生益智功/中华真实报道/中宣部/钟山风雨论坛/周恩来忏悔/周恩来后悔/周恩来自责/周锋锁/周刊纪事/转法轮/自民党/自已的故事/自由民主论坛/自由写作奖/自由亚洲/自主择业/宗教压迫/走私/做爱经典/做爱全过程/做爱挑逗/处女终结者/覆中国政权/颠覆中华人民共和国政/东突厥斯坦/东突厥斯坦伊斯兰/赌具/短信商务广告/法L功/法轮大法/反腐败/分裂中国/分裂中华人民共和国/封杀酱猪媳/高利贷/狗逼/华夏文摘快递/军委主席/李洪志/梦网洪志/窃听/人民大众/人民之声/三水法轮/省委大门集合/省政府大门集合/死亡/台湾共合国/天府广场集会/推翻社会主义制度/伊斯兰运动/政府评理/中共当局/中共独裁/中共统治/权威/华容超市/权 威/山丘舆情/楼盘/促销/促 销/畅销/死 亡/禁片//特价/军队/整形/XXX/重客隆/apple安全中心/地产/艾美美妆/套牌/慈善/移民/电信/移动/移 动/电 信/联 通/中石油/工信部/政协/周年/登录官网/赞助/监狱/一等奖/每天抢货价/白 粉/白粉//乐乐城/环亚/房地产/地产商/地产开发/江贼民/限量版/返点卡/周年/钥匙扣/天水生活网/美乐商城/中国牛仔服饰网/大奖/一等奖/二等奖/特等奖/代开/全民富联盟/艾美美妆/华荣超市/欢乐庆店/2周年/真人/登录官网/赞助/促销/李克强/央企/中移动/常务会议/不整顿/任人唯亲/K粉/地产/中奖/安理会/白粉/绑架/报仇/抗议/暴乱/暴政/爆炸装置/北京当局/精液/避孕套/藏独/操B/操逼/操你/慈善/贷款/弹劾/弹药/党专制/倒卖/盗卖/抵押/抵制/电信公司/动乱/毒品/毒瘾/放款/坟墓/封杀/封锁/睾丸/个股/共产党/股指/龟头/国家领导/国家主席/国家总统/黑火药/黑社会/护法/火葬/鸡巴/集会/监听器/监狱/讲法/可卡因/刻章/恐怖主义/联通公司/两性视频/流氓/留学/六合彩/楼盘/卵巢/乱伦/轮奸/裸体/迷昏药/迷魂药/迷奸药/谋杀/穆斯林/你妈/牛股/牛市/迫害/菩萨/枪毙/枪支/强暴/强奸/抢劫/情色/全球通/权威/人大代表/人权/蹂躏/乳房/软禁/三级片/三陪/三唑仑/三唑伦/散户/桑拿/骚逼/色情/杀人/杀手/傻逼/少妇/射精/伸冤/尸/示威/兽交/私募/死亡/台独/太子党/讨债/套牌/特价房/天安门/挑逗/偷盗/偷拍/偷情/退党/西藏/吸毒/邪恶/邪魔/新股发行/信贷/性爱/性伴侣/性交/性奴/性虐/摇头丸/一夜情/移动公司/移民/阴唇/阴道/阴蒂/阴茎/淫荡/淫水/淫娃/淫邪/淫穴/瘾君子/诈骗/炸弹/炸药/真善美/真善忍/镇压/整形/政协委员/中共政府/中国电信/中国联通/中国移动/中央政府/众生/追债/自杀/总书记/诅咒/尊龙/做爱/黄皮/法伦功/性 交/精选标上线啦/红包最高100元/堟椂闂翠负/投融贷新标上线/监 狱/留 学/会所/二手车/span/您可高枕无忧/大 奖/下单立减/官网曝光/民间借贷提供/数码特卖汇/房 产/尽享更多优惠/19元起/在线看房/【支付宝】/【建设银行】/【招商银行】/【工商银行】/【农业银行】/互换旅游邀请/淫贱/公寓/稽查/抽奖/【110】/okcn.cn/t3m2/urlpp.com/wRxI3o/4h1Ynk/caonima/新华网/【交通银行】/置丢失模失的iphone设备/拖欠/抢购/【95588】/牛皮癣/其授权我司可/砸金蛋/人民法院/研修班/面试/简历";
		for(String error:errorContext.split("/")){
			if(StringUtil.isNotEmpty(error)){
				errorText.add(error);
			}
		}
		Dictionary.loadExtendWords(errorText);
	}
	private NoteUtil(){};
	public static NoteUtil getInstance(){
		if(noteUtil==null){
			noteUtil=new NoteUtil();
		}
		return noteUtil;
	}

	/**
	 *  TODO未处理
	 * @param phoneNumber 电话号码
	 * @param context 内容
	 * @param zhId 租户ID
	 * @return
	 */
	public Integer sendNote(String phoneNumber,String context,String zhId){
		try{
			Map<String,Object> sysVars=WebUtils.getAllSysVar(zhId);
			String noteType=sysVars.get("JE_SYS_NOTEDIY")+"";
			if("DIY".equals(noteType)){
				return com.note.util.NoteUtil.sendNote(phoneNumber, context, zhId);
			}else if("253".equals(noteType)){
				Note253Util note253Util=Note253Util.getInstance(sysVars.get("NOTE_253_USERNAME")+"",sysVars.get("NOTE_253_PASSWORD")+"",sysVars.get("NOTE_253_SIGN")+"",sysVars.get("NOTE_253_REPORT")+"",sysVars.get("NOTE_253_SENDURL")+"");
				return note253Util.sendNote(phoneNumber,context);
			}else{
				HttpClient client = new HttpClient();
				PostMethod post = new PostMethod("http://gbk.sms.webchinese.cn");
				post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
				NameValuePair[] data ={
						new NameValuePair("Uid", sysVars.get("JE_SYS_NOTE_USER")+""),
						new NameValuePair("Key", sysVars.get("JE_SYS_NOTE_PASSWORD")+""),
						new NameValuePair("smsMob",phoneNumber),
						new NameValuePair("smsText",context)};
				post.setRequestBody(data);
				client.executeMethod(post);
				Header[] headers = post.getResponseHeaders();
				int statusCode = post.getStatusCode();
				String resultStr = new String(post.getResponseBodyAsString().getBytes("gbk"));
				//解析结果信息
				post.releaseConnection();
				Integer result=Integer.parseInt(resultStr);
				return result;
			}
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
//    public Integer sendNote(String phoneNumber,String context,String zhId){
//        try{
//            if("DIY".equals(WebUtils.getSysVar("JE_SYS_NOTEDIY"))){
//                return com.note.util.NoteUtil.sendNote(phoneNumber, context, zhId);
//            }
//            HttpClient client = new HttpClient();
//            PostMethod post = new PostMethod("http://gbk.sms.webchinese.cn");
//            post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
//            NameValuePair[] data ={
//                    new NameValuePair("Uid", WebUtils.getSysVar("JE_SYS_NOTE_USER")),
//                    new NameValuePair("Key", WebUtils.getSysVar("JE_SYS_NOTE_PASSWORD")),
//                    new NameValuePair("smsMob",phoneNumber),
//                    new NameValuePair("smsText",context)};
//            post.setRequestBody(data);
//            client.executeMethod(post);
////		    Header[] headers = post.getResponseHeaders();
////		    int statusCode = post.getStatusCode();
////		    for(Header h : headers){
////		    	System.out.println(h.toString());
////		    }
//            String resultStr = new String(post.getResponseBodyAsString().getBytes("gbk"));
//            //解析结果信息
//            post.releaseConnection();
//            Integer result=Integer.parseInt(resultStr);
//            return result;
//        }catch(Exception e){
//            e.printStackTrace();
//            return 0;
//        }
//    }
	public String formatSendError(Integer result){
		String errors="发送出抛出异常";
		switch (result) {
		case -1:
			errors="没有该用户账户!";
			break;
		case -2:
			errors="密钥不正确!";
			break;
		case -3:
			errors="短信数量不足!";
			break;
		case -4:
			errors="手机号格式不正确!";
			break;
		case -11:
			errors="该用户被禁用!";
			break;
		case -14:
			errors="短信内容出现非法内容!";
			break;
		case -41:
			errors="手机号为空!";
			break;
		case -42:
			errors="短信内容为空!";
			break;
		case -51:
			errors="短信签名格式不正确!";
			break;
		case -999:
			errors="短信服务剩余条数不足，请及时续费!";
			break;
		default:
			break;
		}
		return errors;
	}

	/**
	 *  TODO未处理
	 * @param context 内容
	 * @return
	 */
	public String formatSendText(String context){
		try{
			StringReader reader = new StringReader(context);     
			Analyzer analyzer=new IKAnalyzer(false); 
	        TokenStream ts = analyzer.tokenStream("", reader);  
	        ts.addAttribute(TermAttribute.class);
	        while (ts.incrementToken()) {  
	            TermAttribute ta = ts.getAttribute(TermAttribute.class);  
	            String trim=ta.term();
	            if(trim.length()>1 && errorText.contains(trim)){
	         	   String result=trim.replaceAll("", "_");
	         	   context=context.replace(trim, result.substring(1,result.length()-1));
	            }
	        } 
		}catch(Exception e){
			e.printStackTrace();
		}
		return context;
	}
	/**
	 *  TODO未处理
	 * @param context 内容
	 * @return
	 */
	public String checkSendText(String context){
		String error="";
		try{
			StringReader reader = new StringReader(context);     
			Analyzer analyzer=new IKAnalyzer(false); 
	        TokenStream ts = analyzer.tokenStream("", reader);  
	        ts.addAttribute(TermAttribute.class);
	        while (ts.incrementToken()) {  
	            TermAttribute ta = ts.getAttribute(TermAttribute.class);  
	            String trim=ta.term();
	            if(trim.length()>1 && errorText.contains(trim)){
	            	error=trim;
	            	break;
//	         	   String result=trim.replaceAll("", "_");
//	         	   context=context.replace(trim, result.substring(1,result.length()-1));
	            }
	        } 
		}catch(Exception e){
			e.printStackTrace();
		}
		return error;
	}
}
