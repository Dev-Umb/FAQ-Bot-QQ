package io.farewell12345.github.faqbot.DTO


import com.google.gson.annotations.SerializedName

data class ZhiHu(
        @SerializedName("author")
    val author: Author,
        @SerializedName("code")
    val code: Int,
        @SerializedName("data")
    val `data`: Data,
        @SerializedName("msg")
    val msg: String
) {
    data class Author(
        @SerializedName("desc")
        val desc: String,
        @SerializedName("name")
        val name: String
    )

    data class Data(
            @SerializedName("date")
        val date: String,
            @SerializedName("stories")
        val stories: List<Story>,
            @SerializedName("top_stories")
        val topStories: List<TopStory>
    ) {
        data class Story(
            @SerializedName("ga_prefix")
            val gaPrefix: String,
            @SerializedName("hint")
            val hint: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("image_hue")
            val imageHue: String,
            @SerializedName("images")
            val images: List<String>,
            @SerializedName("title")
            val title: String,
            @SerializedName("type")
            val type: Int,
            @SerializedName("url")
            val url: String
        )

        data class TopStory(
            @SerializedName("ga_prefix")
            val gaPrefix: String,
            @SerializedName("hint")
            val hint: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("image")
            val image: String,
            @SerializedName("image_hue")
            val imageHue: String,
            @SerializedName("title")
            val title: String,
            @SerializedName("type")
            val type: Int,
            @SerializedName("url")
            val url: String
        )
    }
}