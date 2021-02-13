package io.farewell12345.github.faqbot.Plugin.SobelImgEdge

import java.awt.image.BufferedImage
import kotlin.math.sqrt
class Sobel {
    private class Pixel {
        var red = 0
        var green = 0
        var blue = 0
        var alpha = 0xFF
        private var rgb = 0
        var rGB: Int
            get() {
                rgb = alpha shl 24 or 0x00010000 * red or 0x00000100 * green or blue
                return rgb
                // 将红绿蓝通道转为RGB值
            }
            set(rgb) {
                red = (rgb and 0x00FF0000) / 0x00010000
                green = (rgb and 0x0000FF00) / 0x00000100
                blue = rgb and 0x000000FF
                alpha = rgb shr 24 and 0x0ff
                this.rgb = rgb
            }
    }
    //Sobel 算子
    fun edgeExtract2(image: BufferedImage): BufferedImage {
        // 创建灰度图
        val bimg = BufferedImage(image.width, image.height, BufferedImage.TYPE_BYTE_GRAY)
        // 一个三维矩阵，用于增强边缘
        val p1 = Pixel()
        val p2 = Pixel()
        val p3 = Pixel()
        val p4 = Pixel()
        val p5 = Pixel()
        val p6 = Pixel()
        val p7 = Pixel()
        val p8 = Pixel()
        val p9 = Pixel()
        var sum1: Int
        var sum2: Int
        var sum: Int
        val p = Pixel()
        // 对每个像素进行边缘增强计算（算边缘梯度）
        for (y in 1 until bimg.height - 1) {
            for (x in 1 until bimg.width - 1) {
                // 取该像素点周围的3*3矩阵进行运算
                p1.rGB = image.getRGB(x - 1, y - 1)
                p2.rGB = image.getRGB(x, y - 1)
                p3.rGB = image.getRGB(x + 1, y - 1)
                p4.rGB = image.getRGB(x - 1, y)
                p5.rGB = image.getRGB(x, y)
                p6.rGB = image.getRGB(x + 1, y)
                p7.rGB = image.getRGB(x - 1, y + 1)
                p8.rGB = image.getRGB(x, y + 1)
                p9.rGB = image.getRGB(x + 1, y + 1)
                // 计算red通道，像素矩阵分别和算子1和算子2进行矩阵乘法，之后的green和blue通道同理
                /* 算子1
                    [1， 2， 1
                    0， 0， 0
                   -1， -2，-1]*/
                sum1 = p1.red + 2 * p2.red + p3.red - p7.red - 2 * p8.red - p9.red
                /*算子2
              [1， 0， -1
              2， 0， -2
              1， 0， -1]*/
                sum2 = p1.red + 2 * p4.red + p7.red - p3.red - 2 * p6.red - p9.red
                // 求导
                sum = Math.sqrt((sum1 * sum1 + sum2 * sum2 + 1).toDouble()).toInt()
                if (sum > 255) sum = 255
                p.red = 255 - sum
                // 计算green通道
                sum1 = p1.green + 2 * p2.green + p3.green - p7.green - 2 * p8.green - p9.green
                sum2 = p1.green + 2 * p4.green + p7.green - p3.green - 2 * p6.green - p9.green
                sum = Math.sqrt((sum1 * sum1 + sum2 * sum2 + 1).toDouble()).toInt()
                if (sum > 255) sum = 255
                p.green = 255 - sum
                // 计算blue通道
                sum1 = p1.blue + 2 * p2.blue + p3.blue - p7.blue - 2 * p8.blue - p9.blue
                sum2 = p1.blue + 2 * p4.blue + p7.blue - p3.blue - 2 * p6.blue - p9.blue
                sum = Math.sqrt((sum1 * sum1 + sum2 * sum2 + 1).toDouble()).toInt()
                if (sum > 255) sum = 255
                p.blue = 255 - sum
                bimg.setRGB(x, y, p.rGB)
            }
        }
        return bimg
    }
}