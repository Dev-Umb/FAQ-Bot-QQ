package io.farewell12345.github.faqbot.DTO.model


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
            @SerializedName("list")
        val list: List<Gamer>,
            @SerializedName("total_page")
        val totalPage: Int
    ) {
        data class Gamer(
                @SerializedName("appid")
            val appid: Int,
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
                @SerializedName("has_video")
            val hasVideo: Int,
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
                    @SerializedName("download_url_android")
                val downloadUrlAndroid: String,
                    @SerializedName("follow_state")
                val followState: String,
                    @SerializedName("game_type")
                val gameType: String,
                    @SerializedName("genres")
                val genres: List<Any>,
                    @SerializedName("has_achievement")
                val hasAchievement: Int,
                    @SerializedName("hidden_type")
                val hiddenType: String,
                    @SerializedName("hs_inventory")
                val hsInventory: Int,
                    @SerializedName("image")
                val image: String,
                    @SerializedName("is_bundle")
                val isBundle: String,
                    @SerializedName("is_free")
                val isFree: Boolean,
                    @SerializedName("is_release")
                val isRelease: Boolean,
                    @SerializedName("name")
                val name: String,
                    @SerializedName("online_player")
                val onlinePlayer: Int,
                    @SerializedName("peak_user_num")
                val peakUserNum: String,
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
                    @SerializedName("release_date")
                val releaseDate: String,
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
}