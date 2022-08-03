package cn.lubin;

import cn.hutool.core.io.FileUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.algorithm.MaxHeap;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.mining.word.TfIdfCounter;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.summary.TextRankKeyword;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.lucene.search.join.ScoreMode;
import org.assertj.core.util.Lists;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author: wangshengbin
 * @date: 2022/7/29 10:38 AM
 */
@Slf4j
public class HanNlpTests {
    private static final List<String> DROP_NATURE_LIST;

    static {
        String[] dropNature = new String[]{"i", "t", "d", "v", "l", "xu", "xx", "y", "yg", "w", "wh", "wky", "wkz", "wp", "ws", "wyy", "wyz", "wb", "u", "ud", "ude1", "ude2", "ude3", "udeng", "udh", "p", "rr", "cc"};
        DROP_NATURE_LIST = Lists.newArrayList(dropNature);
    }

    @Test
    public void demo() {
        TfIdfCounter tfIdfCounter = new TfIdfCounter();
        StringBuilder c = new StringBuilder("近日，中国企业评价协会发布了“2021年中国新经济企业500强”榜单。本次评选标准严格遵守《中国新经济企业评价准则》（T/CEEAS 001-2021），以“科技创新主体”为主题，重点考察了受评企业的科技创新能力。换言之，能够入榜的企业无一不是各自领域的科技创新佼佼者。\n" +
                "        整体来看，2021年新经济500强企业在激烈的市场竞争下，较去年榜单淘汰率超过三分之一。但在汽车行业，造车新势力代表企业蔚来汽车、威马汽车、小鹏汽车、理想汽车均连续两年榜上有名，与传统车企形成了四四平分的局面。似乎正对应了舆论已久“新老势力PK”说法。那么，作为此次评选重点的科技创新领域，新势力们究竟“新”在何处？\n" +
                "        汽车智能化成为行业风向\n" +
                "        当下，在新一代信息和通信、移动互联网、大数据、人工智能等为特征的第四次工业革命的影响下，万物互联时代正在到来。智能化将显著降低工业体系产销之间的信息不对称，加速两者之间的相互联系和反馈，催生出消费者驱动的商业模式。与此同时，经过多年发展，汽车行业的电动化水平已近触顶，车企之间同质化严重。比亚迪董事长王传福、威马创始人沈晖等汽车业内人士曾不只一次表达“新能源汽车的上半场是电动化，下半场是智能化”。\n" +
                "        哨声已经吹响，对比传统造车企业，新势力们往往没有太大的“包袱”，这使得他们在新技术的运用、新方向的开发上走得更为激进，为我国冲破国外传统车企的技术壁垒，走出自己的汽车工业发展路线贡献了巨大力量。智能架构、智能驾驶、智能座舱作为支撑汽车智能化体验的三大支点，成为车企们的角力焦点。\n" +
                "        汽车智能化的底座的诞生\n" +
                "        万丈高楼平地起，没有电子电器架构这一底座作支撑，汽车智能化技术纵使再先进，也只是空中楼阁。传统燃油车设计的核心理念在于满足基础驾驶体验，零部件都是单独控制，各自为政——“硬件功能归硬件，软件功能归软件”，单一指令只对应单一功能，因此车辆缺少除基础驾驶外的扩展功能。孤立的软硬件功能，过低的信息传输速度使得智能驾驶、车载语音等需要多模块协同与高即时反应速率的前沿科技难以应用。\n" +
                "        直到2007年，业内才由德尔福首次提出汽车电子电气架构(EEA, Electrical/Electronic Architecture)概念，它将传感器、ECU、线束、电子电气分配系统整合，重新设计和优化电子控制的软硬件进行系统，以实现汽车整体的配置和功能的实现，从根本上解决了汽车电子系统的通信和效率问题。演变到如今的智能电动汽车时代，EEA的追求已不再是纯粹的机械集成和软件加入，而是将零部件融合贯通，以电子化和智能化满足用户的个性化需求。\n" +
                "        蔚来的ICC、小鹏的X-EEA 3.0、理想的EEA、威马的EEA4.0电子电器架构，区别于传统车企单纯服务于汽车机械功能的思维，专为智能化体验打造，将整车分为中央控制域控制器、自动驾驶域控制器、智能座舱域控制器，使得整车电子电器布局能同时实现高模块化度与高集成度，具备了多模块协同工作，以及植入超高带宽模块的物理条件。正基于此，造车四小龙“蔚小理威”不仅仅实现了底盘舒适性、操控性、驾驶性的全面优化，还做到了在辅助驾驶系统、智能语音控制、智能场景等方面的快速开发和迭代。\n" +
                "        “激光雷达+单车算力”智能驾驶纷纷落地\n" +
                "        智能架构是基础，而智能驾驶则是消费者们感知最为强烈，也是技术难度最高的领域。\n" +
                "        在此方面，蔚来NOP、小鹏NGP、理想NOA、威马NLP智能驾驶系统走在了时代前沿。特斯拉选择了“纯视觉+云计算”路线，基于其庞大的用户体量实现了大量的数据积累，实现了低成本的自动驾驶；“蔚小理威”则选择了 “激光雷达+单车算力”方案。\n" +
                "        在“多枚激光雷达方案+超级算力”的打法下，“蔚小理威”麾下智能驾驶产品纷纷落地。如小鹏NGP跨过了城际高速路场景阶段，开启城市复杂道路的内测阶段，何小鹏亲自体验点赞。威马W6通过AVP技术实现了无人自主泊车功能。\n" +
                "        智能座舱是车企对生活的理解\n" +
                "        当下汽车已经从简单的交通工具演变为智能终端，成为人们介于家庭与工作之间的“第三生活空间”。\n" +
                "        由于EEA架构为智能座舱技术的落地提供了保障，“蔚小理威“在智能座舱的优化交互、增强科技、以及丰富场景联动方面相比于传统车企和其他新势力拉开了差距。\n" +
                "        像是理想L9，该车在座舱内搭载了5块可以交互和联动的屏幕，包括HUD+方向盘仪表、中控副驾双联屏和一块15.7英寸的OLED娱乐屏，对于五屏的联动，理想方面认为可以给消费者带来极致的视听和娱乐体验。\n" +
                "        而小鹏汽车方面反对在车内布局太多屏幕，对于智能座舱有着自己的理解。何小鹏的得意之作小鹏P5，奢侈地搭载了45英寸的投影幕布，可以在车内实现影音娱乐功能。\n" +
                "        蔚来汽车在车内布局了时下流行的AR/VR等技术，为消费者提供影院级的视听享受。居于其仪表台正中的NOMI更是以可爱的形象博得了消费者的广泛喜爱。\n" +
                "        威马汽车创始人沈晖认为，智能汽车不仅是硬件和车辆的互联、更要实现车内各模块之间的互通，基于用户的生活场景而推出人性化的解决方案。\n" +
                "        智能汽车的未来，我们仍然看好“造车四小龙”\n" +
                "        从涉及汽车智能化体验的智能架构、智能驾驶、智能座舱三个方面来看，“蔚小理威”都走在了时代的最前沿。此次“造车四小龙”联袂二进榜，反应了其在科技创新领域的领先成就。笔者认为，我们有理由期待他们在未来更进一步，为消费者带来更为卓越的智能化体验。");
        List<String> keywords = tfIdfCounter.getKeywords(c.toString(), 50);
        List<String> strings = FileUtil.readLines(new File("/Users/wangshengbin/Desktop/inputting_utf-8.txt"), Charset.defaultCharset());
        c = new StringBuilder();
        for (String t : strings) {
            c.append(t);
        }

        List<Term> segment = HanLP.segment(c.toString());
        List<Term> apply = CoreStopWordDictionary.apply(segment);
        List<Term> t = new ArrayList<>();
        for (Term term : apply) {
            if (term.word.length() <= 1) {
                continue;
            }
            String nature = term.nature.toString();
            if (!DROP_NATURE_LIST.contains(nature)) {
                t.add(term);
            }
        }
        List<String> list = tfIdfCounter.getKeywords(t, 50);
        System.out.println(list);

        tfIdfCounter.add(t);
        System.out.println(demoTextRank("", c.toString()));
        System.out.println(tfIdfCounter.documents());
    }

    @Test
    public void demoStand() {
        List<Term> termList = StandardTokenizer.segment("首先没有你好 商品和服务");
        System.out.println(termList);
    }

    @Test
    public void demoNlp() {
        System.out.println(NLPTokenizer.segment("我新造一个词叫幻想乡你能识别并标注正确词性吗？"));
        // 注意观察下面两个“希望”的词性、两个“晚霞”的词性
        System.out.println(NLPTokenizer.analyze("我的希望是希望张晚霞的背影被晚霞映红").translateLabels());
        System.out.println(NLPTokenizer.analyze("支援臺灣正體香港繁體：微软公司於1975年由比爾·蓋茲和保羅·艾倫創立。"));
    }

    @Test
    public void demoIndex() {
        List<Term> termList = IndexTokenizer.segment("主副食品");
        for (Term term : termList) {
            System.out.println(term + " [" + term.offset + ":" + (term.offset + term.word.length()) + "]");
        }
    }

    @Test
    public void demoN() {
        Segment nShortSegment = new NShortSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
        Segment shortestSegment = new DijkstraSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
        String[] testCase = new String[]{
                "今天，刘志军案的关键人物,山西女商人丁书苗在市二中院出庭受审。",
                "刘喜杰石国祥会见吴亚琴先进事迹报告团成员",
        };
        for (String sentence : testCase) {
            System.out.println("N-最短分词：" + nShortSegment.seg(sentence) + "\n最短路分词：" + shortestSegment.seg(sentence));
        }
    }

    /**
     * 中国人名识别
     */
    @Test
    public void demoName() {
        String[] testCase = new String[]{
                "签约仪式前，秦光荣、李纪恒、仇和等一同会见了参加签约的企业家。",
                "王国强、高峰、汪洋、张朝阳光着头、韩寒、小四",
                "张浩和胡健康复员回家了",
                "王总和小丽结婚了",
                "编剧邵钧林和稽道青说",
                "这里有关天培的有关事迹",
                "龚学平等领导,邓颖超生前",
        };
        Segment segment = HanLP.newSegment().enableNameRecognize(true);
        for (String sentence : testCase) {
            List<Term> termList = segment.seg(sentence);
            System.out.println(termList);
        }
    }

    /**
     * 地名识别
     */
    @Test
    public void demoPlace() {
        String[] testCase = new String[]{
                "武胜县新学乡政府大楼门前锣鼓喧天",
                "蓝翔给宁夏固原市彭阳县红河镇黑牛沟村捐赠了挖掘机",
        };
        Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
        for (String sentence : testCase) {
            List<Term> termList = segment.seg(sentence);
            System.out.println(termList);
        }
    }

    @Test
    public void demoKeyWords() {
        String content = "程序员(英文Programmer)是从事程序开发、维护的专业人员。一般将程序员分为程序设计人员和程序编码人员，但两者的界限并不非常清楚，特别是在中国。软件从业人员分为初级程序员、高级程序员、系统分析员和项目经理四大类。";
        List<String> keywordList = HanLP.extractKeyword(content, 5);
        System.out.println(keywordList);
    }

    /**
     * textRank
     * @param url
     * @param content
     * @return
     */
    //  @Test
    public Set<Map.Entry<String, Float>> demoTextRank(Object url, String content) {
        // String content = "手机：13363832368  固话：010-88430281微信：lhcq6666  邮箱：lhcqzk@126.com点击进入，招募ing......作者：张震、尚敦敏来源：中国军网7月21日，阿里云在北京正式发布超融合数字孪生平台。这个平台可以将感知、仿真、控制、可视等四域数据进行融合和计算，保障孪生世界中分析推演的速度和准确性，并在相对应的真实世界场景中实现业务价值。目前，该平台已广泛应用于高速管理、城市交通、港口调度、机场运营、车路协同等场景。所谓数字孪生，就是充分利用物理模型、传感器更新、运行历史等数据，集成多学科、多物理量、多尺度、多概率的仿真过程，在虚拟空间中完成映射，从而反映相对应的实体装备的全生命周期过程。简单来说，数字孪生就是在一个设备或系统的基础上，创造一个数字版的“克隆体”。作为元宇宙技术体系的一员，数字孪生技术与扩展现实、区块链、云计算等技术共同构成了一个虚拟时空的集合。随着数字孪生技术的不断迭代发展，其在社会领域得到广泛应用的同时，逐渐展现出强大的军事应用潜力。图片制作：张麒实景教学 让晦涩的授课变得通俗易懂要想学习一个新的机器或部件，最直接的方法就是将其拆解开来。但如果没有实物或不便于拆解呢？以俄罗斯AL-31航空发动机为例。该航空发动机的结构极其复杂，采用双轴、后燃器设计，全发动机零部件数量高达数万个。假如采取分解展示的方式进行教学，实践起来极其困难；通过二维构造图讲解装备理论，又枯燥难懂。数字孪生技术的出现，为航空发动机专业教学提供了新思路，有效解决了二维平面图纸难以展示复杂系统构造及原理的难题。基于数字孪生技术的实景教学，通过现实与虚拟场景双向映射，构建实景展示模型。这样不仅可以实现与VR全景展示类似的实景虚构功能，而且能够通过数字孪生体强化对实体设备的虚拟操作。这样的实景教学模式，在一些设有VR展馆的博物馆中已经得到了运用。在基于虚拟现实技术的展示平台中，人们可以随意放大、缩小或旋转数字化展品，甚至可以看到文物的内部。人们还可灵活切换不同文物，并同步聆听讲解员的专业讲解。试想一下，当复杂的装备教学也像参观文物一样可以对各种细节一目了然，晦涩的授课将会变得通俗易懂。数字孪生技术可将结构复杂、不便于拆解的部件进行数字可视化展示。构建数字化孪生体能够更加直观地展现装备结构、机件状态、行动原理，教学内容因此变得更加丰富且便于接受。如果用于教学的装备均制作数字孪生体，孪生体的参数和物理实体一一对应，学员可以在物理实体上进行实践操作，交互数据实时传回孪生体，孪生体根据操作动作实时反馈有效信息。这样既解决了传统教学装备实操成本高、网络教学体验差等问题，又能节省教学成本、提升教学效果。辅助决策 让虚拟场景与战场数据擦出火花当前，我们即将迎来智能化战争时代。在不确定因素导致诸多变量的未来战场，面对复杂的敌情和战场环境，决策者只有客观认知、理性判断、科学分析，才能作出正确的决策，而在决策方式的支持手段上，场景想定推演预判正在扮演着越来越重要的角色。近几十年，美军先后研发了收集战场信息的“智能微尘”系统、远程监视战场环境的“伦巴斯”系统、侦听武器平台运动的“沙地直线”系统、专门侦收电磁信号的“狼群”系统等一系列传感网络，整合战场信息，构建起统一的战场传感网络体系，以实现战场实体基础设施与信息基础设施互联互通的目标。通过数字孪生技术可以将现实战斗景况映射至虚拟战场，实时呈现战场数据。指挥员通过孪生体掌握各作战单元、任务部队的情况，能够更为快速正确地下定战场决心。此外，各级指挥员通过虚拟场景交互，实现战场信息的高度共享，有助于解决多系统控制要求高、多兵种协调性差、跨平台规划作战难等问题。通过数字孪生技术构建的虚拟战场，实际上是一种综合运用大数据技术、人工智能技术、3R（虚拟现实VR、增强现实AR、混合现实MR）技术、知识图谱技术等于一体的智能化战场。这样的虚拟战场环境场景更加逼真，对战场态势的演化过程计算模拟得更精确，推演过程、推演结果也都更加贴近实战。第十一届中国国际国防电子展览会上，北京某公司展示出了一款联合作战态势可视化平台系统。这个系统运用了数字孪生技术，支持海量数据资源整合、战场态势可视化显示、多军兵种作战辅助决策等。图片制作：张麒沉浸训练 让受训者身临其境感受战火硝烟一群穿戴全套训练设备的士兵正在训练，有的在车辆中紧张地做着驾驶动作，有的在左右转动车顶的机枪并狂扣扳机，有的在地面“原地”做出各种战术动作……这群士兵操纵的并非真实的武器装备，却能依托呈现设备感受到作战训练“实况”。数字孪生技术融合实景三维建模技术、大数据技术等，构建军事训练场景数字孪生平台，可以实现人、装备、环境虚拟与现实的有机结合，形成战场环境的动态虚拟呈现。在虚拟与现实相融合的训练环境中，参训者的触觉、嗅觉、视觉、听觉全面沉浸，虚拟装备和设备的数据及时反馈，训练效果显著提升。据报道，美国弹弓宇航公司（Slingshot Aerospace）为美国太空军开发了一款数字孪生训练工具。它基于物理学的模拟，结合在轨物体实时跟踪映射、先进的空间天气数据等，可准确模拟当前的太空状态，最大限度提升太空训练的沉浸感。通过搭建训练“新基地”，将数字化技术深度融入战术训练，可在实现资源共享的同时，大幅降低训练资源获取成本。建造基于数字孪生的实景三维模拟训练场，能够“把大的战场缩小来训、把远的战场拉近来训、让少数人训变成大多数人训、把境外战场放到境内来训”。培训设备费用昂贵、传统实景系统沉浸感不强以及人机交互系统构建复杂且交互性不好等问题，也能得以有效解决。目前，世界各军事大国均已认识到数字孪生技术的应用潜力，纷纷通过完善虚拟训练系统提升训练质效。各国纷纷将数字孪生技术应用于飞行模拟器，通过模拟复杂飞行环境及突发情况，训练飞行员应急应变能力。研发装备 让数字模型从虚拟向实体蜕变武器装备的及时研制、快速生产和维护维修是打赢战争、夺取胜利的重要保证。数字孪生模型将传统“设计-样品-测试-模具-再样品”的实体研发模式，变为“数字模型-测试-修改-定型”的虚拟研发模式，研发过程时间短、成本低、效率高。如今，不少国家已经“尝到了甜头”。法国达索飞机公司利用数字孪生技术打造先进的智能装备测试系统，将某新型战斗机的研发成本降低25％。英国宇航系统公司（BAE Systems Plc）公布了用于设计英国“暴风”第六代战斗机的数字技术，通过数字孪生模型和三维打印技术的结合，加快了战机的研发。美国依托数字孪生智能试验平台测试F-35战斗机，将生产周期缩短了5个月。基于数字孪生技术的虚拟研发模式，避免了在样品实体上进行繁琐修正，而是依托数字模型进行设计方案的优化。只需通过调整参数，即可快速、灵活地切换至指定型号产品进行测试，大幅缩减新型装备的研发和定型时间。装备的科技含量越高，维护难度及成本也就越大。数字孪生技术对解决复杂武器装备故障问题也具有独特优势。装备 交付使用前，通过虚拟空间获取装备详细运行参数，存入数字模型。装备使用过程中，通过传感器实时反馈装备运转数据，与模型参数进行比对，从而达到实时监测装备运行状态的目的。在数字孪生技术诞生之初，美军已经提出将其应用于航空航天飞行器的健康维护与保障。比如战机试飞，在数字空间建立孪生体模型，并通过传感器与飞机真实状态完全同步。每次试飞后，即可根据现有状况和过往载荷，及时分析评估是否需要维修，能否承受下次任务载荷等。回复关键字，获取相关主题精选文章热点专题：军民融合  |  一带一路  |  电磁武器  |  网络战  |  台海局势  |  朝鲜半岛  |  南海问题  |  中美关系大政方针：十三五  |  两会  |  国家战略  |  经济  |  军事  |  科技  |  科研  |  教育  |  产业  |  政策  |  创新驱动  |  信息化建设  |  中国制造  |  政策法规  |  产业快讯 军民融合：民参军  |  协调创新  |  成果转化  |  军工混改  |  重大项目  |  国防工业  |  第三方评估前沿技术：人工智能  |  颠覆性技术  |  无人系统  |  机器人  |  虚拟现实  |  脑机接口  |  可穿戴设备  |  3D/4D打印  |  生物科技   |  精准医疗  |  智能制造  | 云计算  |  大数据  |   物联网  |   5G通讯  |  区块链  |  量子通信  |  量子计算  |  超级计算机 |  新材料  |  新能源  |  太赫兹  |  航天  |  卫星  |  北斗  |  航空发动机  |  高性能芯片  |  半导体元器件  |  科技前沿应用  |  创新科技装备发展：航空母舰  |  潜航器  |  水面舰艇  |  无人机  |  新型轰炸机  |  先进雷达  | 新型导弹  |  新型坦克  |  反导  |  高超声速武器  |  武装直升机  |  装备  强军动态：军队改革 |  军事战略  |  人才培养安全纵横：综合安全  |  经济安全  |  军事安全  |  科技安全  |  信息安全  |  太空安全  |  发展安全  |  网络安全其他：未来战争  |  大国博弈  |  贸易战 |  国际新秩序 |  习近平  |  马化腾  |  马斯克 |  DARPA  |  兰德研究报告  |  潘建伟 |  梅宏  |  吴曼青  |  李德毅  |  施一公  |  金一南  |  顾建一  |  卢秉恒  |  邬江兴  |  王凤岭  | 邬贺铨  | 沈昌祥  |  名家言论  |国防建设  |  外军动态其他主题文章陆续整理中，敬请期待······";
        TextRankKeyword rankKeyword = new TextRankKeyword();
        Segment segment = rankKeyword.getSegment();
        List<Term> termList = segment.seg(content);
        List<Term> termNoStopWordList = CoreStopWordDictionary.apply(termList);
        List<Term> seg = new ArrayList<>();
        for (Term term : termNoStopWordList) {
            String nature = term.nature.toString();
            if (!DROP_NATURE_LIST.contains(nature)) {
                seg.add(term);
            }
        }
        int top = 100;
        Set<Map.Entry<String, Float>> entrySet = top(top, rankKeyword.getTermAndRank(seg)).entrySet();
        Set<Map.Entry<String, Float>> t = new LinkedHashSet<>();

        // TF                              IDF
        // 程序员 4/109 = 0.036697                 log(109/(1+1)) = 1.73639     TF*IDF = 0.06372
        // System.out.println(entrySet);

        for (Map.Entry<String, Float> entry : entrySet) {
            if (entry.getKey().length() > 1) {
                t.add(entry);
            }
        }
        return t;
    }

    /**
     * 读取索引导入数据计算特征词
     */
    @Test
    public void modifyEsData() {
        int size = 1000;
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.filter(QueryBuilders.matchQuery("subjects.id", 1597115550874L));
        query.filter(QueryBuilders.matchQuery("subjects.version", 45));
        NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("subjects", query, ScoreMode.None);

        RestHighLevelClient restHighLevelClient = getRestHighLevelClient("192.168.67.85:9700");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(size);
        sourceBuilder.query(nestedQuery);

        SearchRequest request = new SearchRequest("mf-company-test-c");
        request.scroll(TimeValue.timeValueSeconds(60));
        request.source(sourceBuilder);
        String targetIndex = "mf-company-test-cloud-4";

        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            String scrollId = response.getScrollId();
            SearchHit[] hits = response.getHits().getHits();
            log.info("hits total = {}", hits.length);
            bulkInsertEs(hits, restHighLevelClient, targetIndex);
            while (hits.length == size) {
                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                scrollRequest.scroll(TimeValue.timeValueSeconds(60));
                response = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
                scrollId = response.getScrollId();
                hits = response.getHits().getHits();
                log.info("hits total = {}", hits.length);
                bulkInsertEs(hits, restHighLevelClient, targetIndex);
            }
        } catch (Exception e) {
            log.error("Error : ", e);
        }

    }

    /**
     * 计算的特征词结果写入ES
     */
    @Test
    public void insertDataEsTests() {
        int size = 1000;

        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.filter(QueryBuilders.matchQuery("subjects.id", 1597115550874L));
        query.filter(QueryBuilders.matchQuery("subjects.version", 45));
        NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("subjects", query, ScoreMode.None);

        RestHighLevelClient restHighLevelClient = getRestHighLevelClient("192.168.67.85:9700");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(size);
        sourceBuilder.query(nestedQuery);

        SearchRequest request = new SearchRequest("mf-company-test-cloud-4");
        request.scroll(TimeValue.timeValueSeconds(30));
        request.source(sourceBuilder);
        String targetIndex = "mf-company-test-1442";
        long labelId = 1601373470405L;
        int version = 23;
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            String scrollId = response.getScrollId();
            SearchHit[] hits = response.getHits().getHits();
            log.info("hits total = {}", hits.length);
            putLabelId(hits, labelId, version);
            bulkInsert2Es(hits, restHighLevelClient, targetIndex);
            while (hits.length == size) {
                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                scrollRequest.scroll(TimeValue.timeValueSeconds(30));

                response = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
                scrollId = response.getScrollId();
                hits = response.getHits().getHits();
                putLabelId(hits, labelId, version);

                log.info("hits total = {}", hits.length);
                bulkInsert2Es(hits, restHighLevelClient, targetIndex);
            }
        } catch (Exception e) {
            log.error("Error : ", e);
        }
    }

    private void putLabelId(SearchHit[] hits, long labelId, int version) {
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            List<Map<String, Object>> map = (List<Map<String, Object>>) sourceAsMap.get("clouds");
            List<String> wordClouds = new LinkedList<>();
            if (!ObjectUtils.isEmpty(map)) {
                for (Map<String, Object> t : map) {
                    wordClouds.add((String) t.get("name"));
                }
            }

            sourceAsMap.put("wordCloud", wordClouds);
            List<Map<String, Object>> subjectsMap = (List<Map<String, Object>>) sourceAsMap.get("subjects");
            Map<String, Object> stringObjectMap = subjectsMap.get(0);
            stringObjectMap.put("id", labelId);
            stringObjectMap.put("version", version);

        }
    }

    private void bulkInsert2Es(SearchHit[] hits, RestHighLevelClient restHighLevelClient, String index) throws Exception {
        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                int numberOfActions = request.numberOfActions();
                log.debug("Executing bulk [{}] with {} requests",
                        executionId, numberOfActions);
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  BulkResponse response) {
                if (response.hasFailures()) {
                    log.warn("Bulk [{}] executed with failures", executionId);
                } else {
                    log.debug("Bulk [{}] completed in {} milliseconds",
                            executionId, response.getTook().getMillis());
                }
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  Throwable failure) {
                log.error("Failed to execute bulk", failure);
            }
        };
        BulkProcessor bulkProcessor = BulkProcessor.builder(
                        (request, bulkListener) ->
                                restHighLevelClient.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
                        listener)
                .setBulkActions(1000)
                .setBulkSize(new ByteSizeValue(10L, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueMinutes(10L))
                .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3))
                .build();
        long millis = System.currentTimeMillis();
        for (SearchHit hit : hits) {
            IndexRequest request = new IndexRequest(index);
            request.source(hit.getSourceAsMap());
            bulkProcessor.add(request);
        }
        log.info("cost :{} ms", System.currentTimeMillis() - millis);
        boolean terminated = bulkProcessor.awaitClose(30L, TimeUnit.SECONDS);
        System.out.println("result = " + terminated);
        bulkProcessor.close();

    }

    private void bulkInsertEs(SearchHit[] hits, RestHighLevelClient restHighLevelClient, String index) throws InterruptedException {

        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                int numberOfActions = request.numberOfActions();
                log.debug("Executing bulk [{}] with {} requests",
                        executionId, numberOfActions);
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  BulkResponse response) {
                if (response.hasFailures()) {
                    log.warn("Bulk [{}] executed with failures", executionId);
                } else {
                    log.debug("Bulk [{}] completed in {} milliseconds",
                            executionId, response.getTook().getMillis());
                }
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  Throwable failure) {
                log.error("Failed to execute bulk", failure);
            }
        };
        BulkProcessor bulkProcessor = BulkProcessor.builder(
                        (request, bulkListener) ->
                                restHighLevelClient.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
                        listener)
                .setBulkActions(1000)
                .setBulkSize(new ByteSizeValue(10L, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueMinutes(10L))
                .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3))
                .build();
        long millis = System.currentTimeMillis();
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String content = (String) sourceAsMap.get("title");
            Set<Map.Entry<String, Float>> tfIdf = demoTextRank(sourceAsMap.get("url"), content);
            List<Map<String, Object>> list = new LinkedList<>();
            for (Map.Entry<String, Float> entry : tfIdf) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", entry.getKey());
                map.put("value", entry.getValue());
                list.add(map);
            }
            if (list.size() > 0) {
                sourceAsMap.put("clouds", list);
            }
            IndexRequest request = new IndexRequest(index);
            request.source(sourceAsMap);
            bulkProcessor.add(request);
        }
        log.info("cost :{} ms", System.currentTimeMillis() - millis);
        boolean terminated = bulkProcessor.awaitClose(30L, TimeUnit.SECONDS);
        System.out.println("result = " + terminated);
        bulkProcessor.close();
    }

    private RestHighLevelClient getRestHighLevelClient(String hostPort) {
        String[] hosts = hostPort.split(",");
        HttpHost[] httpHosts = new HttpHost[hosts.length];
        for (int i = 0; i < hosts.length; i++) {
            String[] t = hosts[i].split(":");
            HttpHost httpHost = new HttpHost(t[0], Integer.parseInt(t[1]), "http");
            httpHosts[i] = httpHost;
        }
        RestClientBuilder builder = RestClient
                .builder(httpHosts)
                .setRequestConfigCallback(requestConfigBuilder -> {
                    requestConfigBuilder.setConnectTimeout(5000);
                    requestConfigBuilder.setSocketTimeout(120000);
                    requestConfigBuilder.setConnectionRequestTimeout(30000);
                    return requestConfigBuilder;
                });
        return new RestHighLevelClient(builder);
    }

    private Map<String, Float> top(int size, Map<String, Float> map) {
        Map<String, Float> result = new LinkedHashMap<>();
        List<Map.Entry<String, Float>> entries = new MaxHeap<>(size, new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        }).addAll(map.entrySet()).toList();
        for (Map.Entry<String, Float> entry : entries) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @Test
    public void demoSummary() {
        String document = "算法可大致分为基本算法、数据结构的算法、数论算法、计算几何的算法、图的算法、动态规划以及数值分析、加密算法、排序算法、检索算法、随机化算法、并行算法、厄米变形模型、随机森林算法。\n" +
                "算法可以宽泛的分为三类，\n" +
                "一，有限的确定性算法，这类算法在有限的一段时间内终止。他们可能要花很长时间来执行指定的任务，但仍将在一定的时间内终止。这类算法得出的结果常取决于输入值。\n" +
                "二，有限的非确定算法，这类算法在有限的时间内终止。然而，对于一个（或一些）给定的数值，算法的结果并不是唯一的或确定的。\n" +
                "三，无限的算法，是那些由于没有定义终止定义条件，或定义的条件无法由输入的数据满足而不终止运行的算法。通常，无限算法的产生是由于未能确定的定义终止条件。";
        List<String> sentenceList = HanLP.extractSummary(document, 3);
        System.out.println(sentenceList);
    }

    @Test
    public void demoWord2Vec() {

    }
}

