package FuckOkhttp


import com.google.gson.annotations.SerializedName

data class XiaoHeiHe(
    @SerializedName("msg")
    val msg: String,
    @SerializedName("result")
    val result: Result,
    @SerializedName("status")
    val status: String,
    @SerializedName("version")
    val version: String
) {
    data class Result(
        @SerializedName("all_list")
        val allList: List<All>,
        @SerializedName("header")
        val header: List<Any>,
        @SerializedName("overview")
        val overview: List<Overview>
    ) {
        data class All(
            @SerializedName("list")
            val list: List<Game>,
            @SerializedName("show_type")
            val showType: String,
            @SerializedName("style")
            val style: String,
            @SerializedName("title")
            val title: String
        ) {
            data class Game(
                @SerializedName("appid")
                val appid: Int,
                @SerializedName("background")
                val background: String,
                @SerializedName("click")
                val click: Int,
                @SerializedName("comment_num")
                val commentNum: Int,
                @SerializedName("create_at")
                val createAt: String,
                @SerializedName("description")
                val description: String,
                @SerializedName("game")
                val game: Game,
                @SerializedName("game_icon")
                val gameIcon: String,
                @SerializedName("game_img")
                val gameImg: String,
                @SerializedName("game_name")
                val gameName: String,
                @SerializedName("game_type")
                val gameType: String,
                @SerializedName("h_src")
                val hSrc: String,
                @SerializedName("has_video")
                val hasVideo: Int,
                @SerializedName("heybox_price")
                val heyboxPrice: HeyboxPrice,
                @SerializedName("imgs")
                val imgs: List<Any>,
                @SerializedName("is_web")
                val isWeb: Int,
                @SerializedName("link_award_num")
                val linkAwardNum: Int,
                @SerializedName("link_tag")
                val linkTag: Int,
                @SerializedName("link_type")
                val linkType: Int,
                @SerializedName("linkid")
                val linkid: Int,
                @SerializedName("modify_at")
                val modifyAt: String,
                @SerializedName("platforms")
                val platforms: List<Any>,
                @SerializedName("platforms_url")
                val platformsUrl: List<Any>,
                @SerializedName("price")
                val price: Price,
                @SerializedName("product_intro")
                val productIntro: String,
                @SerializedName("product_name")
                val productName: String,
                @SerializedName("product_type")
                val productType: Int,
                @SerializedName("product_type_desc")
                val productTypeDesc: String,
                @SerializedName("rec_cnt")
                val recCnt: Int,
                @SerializedName("score")
                val score: String,
                @SerializedName("share_url")
                val shareUrl: String,
                @SerializedName("special_type")
                val specialType: Int,
                @SerializedName("title")
                val title: String,
                @SerializedName("topics")
                val topics: List<Topic>,
                @SerializedName("up")
                val up: Int,
                @SerializedName("user")
                val user: User
            ) {
                data class Game(
                    @SerializedName("appicon")
                    val appicon: String,
                    @SerializedName("bundle_id")
                    val bundleId: String,
                    @SerializedName("follow_state")
                    val followState: String,
                    @SerializedName("game_type")
                    val gameType: String,
                    @SerializedName("genres")
                    val genres: List<Any>,
                    @SerializedName("hidden_type")
                    val hiddenType: String,
                    @SerializedName("image")
                    val image: String,
                    @SerializedName("is_free")
                    val isFree: Boolean,
                    @SerializedName("is_release")
                    val isRelease: Boolean,
                    @SerializedName("name")
                    val name: String,
                    @SerializedName("platform_infos")
                    val platformInfos: List<PlatformInfo>,
                    @SerializedName("platforms")
                    val platforms: List<String>,
                    @SerializedName("platforms_icon")
                    val platformsIcon: List<String>,
                    @SerializedName("platforms_url")
                    val platformsUrl: List<String>,
                    @SerializedName("price")
                    val price: Price,
                    @SerializedName("score")
                    val score: String,
                    @SerializedName("steam_appid")
                    val steamAppid: Int,
                    @SerializedName("type")
                    val type: String
                ) {
                    data class PlatformInfo(
                        @SerializedName("img_url")
                        val imgUrl: String,
                        @SerializedName("key")
                        val key: String,
                        @SerializedName("price")
                        val price: Price
                    ) {
                        data class Price(
                            @SerializedName("is_free")
                            val isFree: Boolean
                        )
                    }

                    data class Price(
                        @SerializedName("current")
                        val current: String,
                        @SerializedName("discount")
                        val discount: Int,
                        @SerializedName("initial")
                        val initial: String,
                        @SerializedName("lowest_discount")
                        val lowestDiscount: Int,
                        @SerializedName("lowest_price")
                        val lowestPrice: Int
                    )
                }

                data class HeyboxPrice(
                    @SerializedName("cost_coin")
                    val costCoin: Int,
                    @SerializedName("coupon_info")
                    val couponInfo: CouponInfo,
                    @SerializedName("discount")
                    val discount: Int,
                    @SerializedName("is_lowest")
                    val isLowest: Int,
                    @SerializedName("original_coin")
                    val originalCoin: Int
                ) {
                    data class CouponInfo(
                        @SerializedName("available_coupon_num")
                        val availableCouponNum: Int,
                        @SerializedName("coupon_desc")
                        val couponDesc: String,
                        @SerializedName("max_reduce")
                        val maxReduce: Int,
                        @SerializedName("primary_id")
                        val primaryId: Int,
                        @SerializedName("sub_title")
                        val subTitle: String,
                        @SerializedName("title")
                        val title: String
                    )
                }

                data class Price(
                    @SerializedName("current")
                    val current: String,
                    @SerializedName("deadline_date")
                    val deadlineDate: String,
                    @SerializedName("discount")
                    val discount: Int,
                    @SerializedName("initial")
                    val initial: String,
                    @SerializedName("lowest_discount")
                    val lowestDiscount: Int,
                    @SerializedName("lowest_price")
                    val lowestPrice: Int
                )

                data class Topic(
                    @SerializedName("app_id")
                    val appId: Int,
                    @SerializedName("name")
                    val name: String,
                    @SerializedName("pic_url")
                    val picUrl: String,
                    @SerializedName("topic_id")
                    val topicId: Int
                )

                data class User(
                    @SerializedName("userid")
                    val userid: Int,
                    @SerializedName("username")
                    val username: String
                )
            }
        }

        data class Overview(
            @SerializedName("desc")
            val desc: String,
            @SerializedName("list")
            val list: List<Game1>,
            @SerializedName("route")
            val route: String,
            @SerializedName("show_type")
            val showType: String
        ) {
            data class Game1(
                @SerializedName("appid")
                val appid: Int,
                @SerializedName("background")
                val background: String,
                @SerializedName("game_icon")
                val gameIcon: String,
                @SerializedName("game_img")
                val gameImg: String,
                @SerializedName("game_name")
                val gameName: String,
                @SerializedName("game_type")
                val gameType: String,
                @SerializedName("h_src")
                val hSrc: String,
                @SerializedName("heybox_price")
                val heyboxPrice: HeyboxPrice,
                @SerializedName("platforms")
                val platforms: List<Any>,
                @SerializedName("platforms_url")
                val platformsUrl: List<Any>,
                @SerializedName("price")
                val price: Price,
                @SerializedName("product_intro")
                val productIntro: String,
                @SerializedName("product_name")
                val productName: String,
                @SerializedName("product_type")
                val productType: Int,
                @SerializedName("product_type_desc")
                val productTypeDesc: String,
                @SerializedName("score")
                val score: String
            ) {
                data class HeyboxPrice(
                    @SerializedName("cost_coin")
                    val costCoin: Int,
                    @SerializedName("coupon_info")
                    val couponInfo: CouponInfo,
                    @SerializedName("discount")
                    val discount: Int,
                    @SerializedName("is_lowest")
                    val isLowest: Int,
                    @SerializedName("original_coin")
                    val originalCoin: Int
                ) {
                    data class CouponInfo(
                        @SerializedName("available_coupon_num")
                        val availableCouponNum: Int,
                        @SerializedName("coupon_desc")
                        val couponDesc: String,
                        @SerializedName("max_reduce")
                        val maxReduce: Int,
                        @SerializedName("primary_id")
                        val primaryId: Int,
                        @SerializedName("sub_title")
                        val subTitle: String,
                        @SerializedName("title")
                        val title: String
                    )
                }

                data class Price(
                    @SerializedName("current")
                    val current: String,
                    @SerializedName("deadline_date")
                    val deadlineDate: String,
                    @SerializedName("discount")
                    val discount: Int,
                    @SerializedName("initial")
                    val initial: String,
                    @SerializedName("lowest_discount")
                    val lowestDiscount: Int,
                    @SerializedName("lowest_price")
                    val lowestPrice: Int
                )
            }
        }
    }
}