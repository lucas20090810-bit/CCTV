package com.example.data

data class MediaItem(
    val id: String,
    val title: String,
    val tagline: String,
    val description: String,
    val rating: String,
    val year: String,
    val duration: String,
    val isMovie: Boolean,
    val category: String, // "hot", "recommend", "ranking", "new", "watching", "upcoming"
    val genre: String,
    val rank: Int = 0, // For Top 10 ranking
    val cast: List<String>,
    val episodes: List<String> = emptyList(),
    val mockTrailerUrl: String = ""
)

object CinemaCatalog {
    val items = listOf(
        // Taiwan Top 10 Rankings
        MediaItem(
            id = "rank1",
            title = "關於我和鬼變成家人的那件事",
            tagline = "從荒謬的死後冥婚，看見最真摯的愛。",
            description = "恐同男警吳明翰，誤撿地上紅包，沒想到過世的同性戀男子毛毛竟成了他的老公！一人一鬼攜手破案，闖關過程爆笑又催淚。",
            rating = "9.5",
            year = "2023",
            duration = "2h 10m",
            isMovie = true,
            category = "ranking",
            genre = "喜劇 / 奇幻",
            rank = 1,
            cast = listOf("許光漢", "林柏宏", "王淨", "蔡振南")
        ),
        MediaItem(
            id = "rank2",
            title = "周處除三害",
            tagline = "我是惡人，也是除惡之人。",
            description = "通緝犯陳桂林生命將盡，發現自己在通緝名單中僅列第三。他決定在臨終前幹掉前兩名通緝犯，成為當代「周處」，展開瘋狂的除惡救贖之旅。",
            rating = "9.4",
            year = "2023",
            duration = "2h 14m",
            isMovie = true,
            category = "ranking",
            genre = "動作 / 犯罪",
            rank = 2,
            cast = listOf("阮經天", "袁富華", "陳以文", "王淨")
        ),
        MediaItem(
            id = "rank3",
            title = "不夠善良的我們",
            tagline = "這是一場關於遺憾與妒忌的長跑。",
            description = "兩個同月同日生、經常買同一件衣服的女孩，在命運中互相糾纏、窺視、甚至搶了同一個男人。在人生的中年關卡，她們重新審視愛情的初衷與不甘。",
            rating = "9.6",
            year = "2024",
            duration = "8 Episodes",
            isMovie = false,
            category = "ranking",
            genre = "都會 / 寫實 / 劇情",
            rank = 3,
            cast = listOf("林依晨", "許瑋甯", "賀軍翔", "柯震東"),
            episodes = listOf(
                "Ep 1: 被愛的人是贏家嗎？",
                "Ep 2: 我偷看了她的社交帳號",
                "Ep 3: 婚姻是妥協還是解脫",
                "Ep 4: 被留下來的人",
                "Ep 5: 如果我們沒錯過",
                "Ep 6: 下一站的幸福",
                "Ep 7: 當嫉妒化為眼淚",
                "Ep 8: 最溫柔的告別"
            )
        ),
        MediaItem(
            id = "rank4",
            title = "霓虹駭客：2055",
            tagline = "在賽博朋克的極限邊緣，尋找最後的自由。",
            description = "故事發生在近未來的台北，巨大的新店溪防壁矗立，反烏托邦的高科技都市正被人工智能接管。少女駭客阿雪為查明雙胞胎姐姐的失蹤，侵入了高度防禦的 CCTV 智慧控制晶片，捲入了這場推翻跨國財閥的地下革命。",
            rating = "9.8",
            year = "2025",
            duration = "10 Episodes",
            isMovie = false,
            category = "ranking",
            genre = "賽博朋克 / 科幻",
            rank = 4,
            cast = listOf("桂綸鎂", "鳳小岳", "戴立忍"),
            episodes = listOf(
                "Ep 1: 記憶晶片走私線",
                "Ep 2: 虛擬巨蛋的地下派對",
                "Ep 3: 台北地下200層的低端生活",
                "Ep 4: 防火牆中的幽靈",
                "Ep 5: 記憶重組的深淵",
                "Ep 6: 仿生人的叛變警告",
                "Ep 7: 防壁邊緣的黑市交易",
                "Ep 8: 跨國財閥的極機密檔案",
                "Ep 9: 粒子風暴席捲",
                "Ep 10: 霓虹盡頭的光明"
            )
        ),
        // Today's Hot
        MediaItem(
            id = "hot1",
            title = "沙丘：第二部 (Dune: Part Two)",
            tagline = "先知降臨，沙丘風暴席捲星際。",
            description = "保羅亞崔迪與契妮以及弗瑞曼人聯手，對摧毀他家族的陰謀者展開報復。他在面對一生摯愛與已知宇宙命運之間的抉擇時，竭力阻止只有他能預見的恐怖未來。",
            rating = "9.7",
            year = "2024",
            duration = "2h 46m",
            isMovie = true,
            category = "hot",
            genre = "科幻 / 史詩 / 冒險",
            cast = listOf("提摩西·夏勒梅", "辛蒂亞", "蕾貝卡·弗格森", "奧斯汀·巴特勒")
        ),
        MediaItem(
            id = "hot2",
            title = "黑鏡：第七季 (Black Mirror S7)",
            tagline = "科技依然冰冷，人性依然殘存。",
            description = "全新六個驚悚而發人深省的科技警示錄。探討在AI普及與虛擬肉身永生的年代，人類心靈面臨的全新地獄與終極覺醒。",
            rating = "9.1",
            year = "2025",
            duration = "6 Episodes",
            isMovie = false,
            category = "hot",
            genre = "懸疑 / 奇幻 / 科幻",
            cast = listOf("亞倫·保羅", "莎莉·霍金斯", "派屈克·史都華"),
            episodes = listOf(
                "Ep 1: 天使晶片",
                "Ep 2: 演算法殺手",
                "Ep 3: 深海記憶島",
                "Ep 4: 靈魂雲端備份",
                "Ep 5: 機械獵犬之怒",
                "Ep 6: 進擊的數據人"
            )
        ),
        // For You Recommend
        MediaItem(
            id = "rec1",
            title = "星際效應 (Interstellar)",
            tagline = "愛是唯一可以超越時間與空間的事物。",
            description = "地球即將毀滅，一群探險家扛起人類歷史上最重要的任務：越過太陽系外的蟲洞，尋找人類得以延續生命的新家園。",
            rating = "9.9",
            year = "2014",
            duration = "2h 49m",
            isMovie = true,
            category = "recommend",
            genre = "科幻 / 催淚 / 航太",
            cast = listOf("馬修·麥康納", "安·海瑟薇", "潔西卡·崔絲坦")
        ),
        MediaItem(
            id = "rec2",
            title = "雙城之戰：第二季 (Arcane S2)",
            tagline = "皮爾托福與祖安的決戰，雙生姐妹的宿命。",
            description = "奧術能量爆發引燃戰火。菲艾與吉茵珂絲身處對立陣營，友情與仇恨交織，在鋼鐵都市與污染地帶展開驚心動魄的宿命之戰。",
            rating = "9.8",
            year = "2024",
            duration = "9 Episodes",
            isMovie = false,
            category = "recommend",
            genre = "科幻 / 動畫 / 動作",
            cast = listOf("Hailee Steinfeld", "Ella Purnell", "Kevin Alejandro"),
            episodes = listOf(
                "Ep 1: 廢墟誕生",
                "Ep 2: 皮城的喪鐘",
                "Ep 3: 毒氣與魔法",
                "Ep 4: 宿命再會",
                "Ep 5: 黑暗中的微光",
                "Ep 6: 家族的詛咒",
                "Ep 7: 化學狂人登場",
                "Ep 8: 姐妹決裂",
                "Ep 9: 終焉之曲"
            )
        ),
        // New Releases
        MediaItem(
            id = "new1",
            title = "奧本海默 (Oppenheimer)",
            tagline = "我現在成了死神，世界的毀滅者。",
            description = "一場關於原子彈之父羅伯特·奧本海默的傳記片。記錄他在曼哈頓計劃中主導研發原子彈，以及在戰後安全聽證會上面臨的政治迫害與內心道德譴責。",
            rating = "9.5",
            year = "2023",
            duration = "3h 00m",
            isMovie = true,
            category = "new",
            genre = "傳記 / 歷史 / 劇情",
            cast = listOf("席尼·墨菲", "艾蜜莉·布朗", "麥特·戴蒙", "小勞勃·道尼")
        ),
        MediaItem(
            id = "new2",
            title = "模仿犯 (Copycat Killer)",
            tagline = "真相面前，沒有任何人能假裝盲目。",
            description = "100% 台灣在地改編日本宮部美幸推理鉅作。檢察官郭曉其面對一連串殘酷的女屍連環命案，兇手狂妄挑釁媒體與法律，逼人陷入瘋狂，看曉其如何堅守正義邊際破案。",
            rating = "9.3",
            year = "2023",
            duration = "10 Episodes",
            isMovie = false,
            category = "new",
            genre = "犯罪 / 驚悚",
            cast = listOf("吳慷仁", "柯佳嬿", "庹宗華", "姚淳耀"),
            episodes = listOf(
                "Ep 1: 紅盒子與挑釁訊號",
                "Ep 2: 電視台即時秀",
                "Ep 3: 線索：深夜電話",
                "Ep 4: 被戴面具的受害者",
                "Ep 5: 警網布局失敗",
                "Ep 6: 黑暗中的模仿",
                "Ep 7: 深淵的低喃",
                "Ep 8: 檢察官的家庭悲劇",
                "Ep 9: 瘋狂直播大審判",
                "Ep 10: 正義的終極落幕"
            )
        ),
        // Upcoming list
        MediaItem(
            id = "up1",
            title = "阿凡達：火與灰 (Avatar: Fire and Ash)",
            tagline = "潘朵拉星的火族，即將爆發史無前例的衝突。",
            description = "傑克與奈蒂莉在潘朵拉星面臨更具進攻性的全新納美人部落——「灰燼之人」。由納美人與人類欲望交織而成的火狂潮將席捲雨林與海洋。",
            rating = "期待度 99%",
            year = "2025 年 12 月",
            duration = "3h 05m",
            isMovie = true,
            category = "upcoming",
            genre = "科幻 / 史詩 / 冒險",
            cast = listOf("山姆·沃辛頓", "柔伊·莎達娜", "雪歌妮·薇佛")
        ),
        MediaItem(
            id = "up2",
            title = "魷魚遊戲：第三季 (Squid Game S3)",
            tagline = "最終賽局開始，奇勳的復仇大結局。",
            description = "贏得大獎的成奇勳放棄前往美國，重返生死遊戲。這一次，他要徹底揭露這個幕後跨國黑色網絡，並親身做為裁判終結殘忍的魷魚競賽。",
            rating = "期待度 98%",
            year = "2025 年末",
            duration = "8 Episodes",
            isMovie = false,
            category = "upcoming",
            genre = "驚悚 / 冒險 / 心理",
            cast = listOf("李政宰", "李炳憲", "魏嘏雋", "孔劉")
        )
    )
}
