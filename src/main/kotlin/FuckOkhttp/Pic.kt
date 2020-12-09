package FuckOkhttp


import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import net.mamoe.mirai.message.data.Image

data class Pic(
    @SerializedName("code")
    val code: Int,
    @SerializedName("height")
    val height: String,
    @SerializedName("acgurl")
    val acgurl: String,
    @SerializedName("width")
    val width: String,
    @SerializedName("size")
    val size: String
)

object PicManager{
    private var imgurl = GsonBuilder()
            .create().fromJson(
                    FuckOkhttp("https://tenapi.cn/acg/?return=json").getData(),
                    Pic::class.java
            ).acgurl
    fun getPic(): String {
        val url = imgurl
        imgurl = ""
        flush()
        return url
    }
    private fun flush(){
        Thread {
            synchronized(imgurl) {
                imgurl = GsonBuilder()
                        .create().fromJson(
                                FuckOkhttp("https://api.blogbig.cn/random/api.php?return=json").getData(),
                                Pic::class.java
                        ).acgurl
            }
        }.start()
    }

}