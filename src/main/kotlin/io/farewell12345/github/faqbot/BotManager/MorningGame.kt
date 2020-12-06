package io.farewell12345.github.faqbot.BotManager


import com.google.gson.annotations.SerializedName

data class MorningGame(
    @SerializedName("desc")
    val desc: String,
    @SerializedName("list")
    val list: List<String>,
    @SerializedName("route")
    val route: String,
    @SerializedName("show_type")
    val showType: String
) {
    data class Game(
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
            @SerializedName("discount")
            val discount: Int,
            @SerializedName("is_lowest")
            val isLowest: Int,
            @SerializedName("original_coin")
            val originalCoin: Int
        )

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