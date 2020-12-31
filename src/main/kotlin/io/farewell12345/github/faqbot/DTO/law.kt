package io.farewell12345.github.faqbot.DTO


import com.google.gson.annotations.SerializedName

data class law(
    @SerializedName("第一编　总则")
    val first:First,
    @SerializedName("第七编　侵权责任")
    val seven:Seven,
    @SerializedName("第三编　合同")
    val three: Thrid,
    @SerializedName("第二编　物权")
    val second: Second,
    @SerializedName("第五编　婚姻家庭")
    val fifth:Five,
    @SerializedName("第六编　继承")
    val sixth: Six,
    @SerializedName("第四编　人格权")
    val fourth: Four
) {
    data class First(
            @SerializedName("第一章　基本规定")
        val first: List<String>,
            @SerializedName("第七章　代理")
        val seven: List<String>,
            @SerializedName("第三章　法人")
        val third: List<String>,
            @SerializedName("第九章　诉讼时效")
        val nine: List<String>,
            @SerializedName("第二章　自然人")
        val second: List<String>,
            @SerializedName("第五章　民事权利")
        val five: List<String>,
            @SerializedName("第八章　民事责任")
        val eight: List<String>,
            @SerializedName("第六章　民事法律行为")
        val six: List<String>,
            @SerializedName("第十章　期间计算")
        val ten: List<String>,
            @SerializedName("第四章　非法人组织")
        val four: List<String>
    )

    data class Seven(
        @SerializedName("第一章　一般规定")
        val one: List<String>,
        @SerializedName("第七章　环境污染和生态破坏责任")
        val seven: List<String>,
        @SerializedName("第三章　责任主体的特殊规定")
        val three: List<String>,
        @SerializedName("第九章　饲养动物损害责任")
        val nine: List<String>,
        @SerializedName("第二章　损害赔偿")
        val two: List<String>,
        @SerializedName("第五章　机动车交通事故责任")
        val five: List<String>,
        @SerializedName("第八章　高度危险责任")
        val eight: List<String>,
        @SerializedName("第六章　医疗损害责任")
        val six: List<String>,
        @SerializedName("第十章　建筑物和物件损害责任")
        val ten: List<String>,
        @SerializedName("第四章　产品责任")
        val four: List<String>
    )

    data class Thrid(
        @SerializedName("第一章　一般规定")
        val one: List<String>,
        @SerializedName("第七章　合同的权利义务终止")
        val seven: List<String>,
        @SerializedName("第三章　合同的效力")
        val three: List<String>,
        @SerializedName("第九章　买卖合同")
        val nine: List<String>,
        @SerializedName("第二十一章　保管合同")
        val twentyOne: List<String>,
        @SerializedName("第二十七章　合伙合同")
        val twentySeven: List<String>,
        @SerializedName("第二十三章　委托合同")
        val twentyThree: List<String>,
        @SerializedName("第二十九章　不当得利")
        val twentyNine: List<String>,
        @SerializedName("第二十二章　仓储合同")
        val twentyTwo: List<String>,
        @SerializedName("第二十五章　行纪合同")
        val twentyFive: List<String>,
        @SerializedName("第二十八章　无因管理")
        val twentyEight: List<String>,
        @SerializedName("第二十六章　中介合同")
        val twentySix: List<String>,
        @SerializedName("第二十四章　物业服务合同")
        val twentyFour: List<String>,
        @SerializedName("第二十章　技术合同")
        val twenty: List<String>,
        @SerializedName("第二章　合同的订立")
        val two: List<String>,
        @SerializedName("第五章　合同的保全")
        val five: List<String>,
        @SerializedName("第八章　违约责任")
        val eight: List<String>,
        @SerializedName("第六章　合同的变更和转让")
        val six: List<String>,
        @SerializedName("第十一章　赠与合同")
        val eleven: List<String>,
        @SerializedName("第十七章　承揽合同")
        val sevenTeen: List<String>,
        @SerializedName("第十三章　保证合同")
        val thrildtenn: List<String>,
        @SerializedName("第十九章　运输合同")
        val nineteen: List<String>,
        @SerializedName("第十二章　借款合同")
        val twelve: List<String>,
        @SerializedName("第十五章　融资租赁合同")
        val fiveteen: List<String>,
        @SerializedName("第十八章　建设工程合同")
        val eighteen: List<String>,
        @SerializedName("第十六章　保理合同")
        val sixteen: List<String>,
        @SerializedName("第十四章　租赁合同")
        val fourteen: List<String>,
        @SerializedName("第十章　供用电、水、气、热力合同")
        val ten: List<String>,
        @SerializedName("第四章　合同的履行")
        val four: List<String>
    )

    data class Second(
            @SerializedName("第一章　一般规定")
        val one: List<String>,
            @SerializedName("第七章　相邻关系")
        val seven: List<String>,
            @SerializedName("第三章　物权的保护")
        val three: List<String>,
            @SerializedName("第九章　所有权取得的特别规定")
        val nine: List<String>,
            @SerializedName("第二十章　占有")
        val twelfth: List<String>,
            @SerializedName("第二章　物权的设立、变更、转让和消灭")
        val two: List<String>,
            @SerializedName("第五章　国家所有权和集体所有权、私人所有权")
        val five: List<String>,
            @SerializedName("第八章　共有")
        val eight: List<String>,
            @SerializedName("第六章　业主的建筑物区分所有权")
        val six: List<String>,
            @SerializedName("第十一章　土地承包经营权")
        val elven: List<String>,
            @SerializedName("第十七章　抵押权")
        val seventeen: List<String>,
            @SerializedName("第十三章　宅基地使用权")
        val thridteen: List<String>,
            @SerializedName("第十九章　留置权")
        val ninteen: List<String>,
            @SerializedName("第十二章　建设用地使用权")
        val twelve: List<String>,
            @SerializedName("第十五章　地役权")
        val fivteen: List<String>,
            @SerializedName("第十八章　质权")
        val eighteen: List<String>,
            @SerializedName("第十六章　一般规定")
        val sixteen: List<String>,
            @SerializedName("第十四章　居住权")
        val fourteen: List<String>,
            @SerializedName("第十章　一般规定")
        val ten: List<String>,
            @SerializedName("第四章　一般规定")
        val four: List<String>
    )

    data class Five(
        @SerializedName("第一章　一般规定")
        val one: List<String>,
        @SerializedName("第三章　家庭关系")
        val three: List<String>,
        @SerializedName("第二章　结婚")
        val two: List<String>,
        @SerializedName("第五章　收养")
        val five: List<String>,
        @SerializedName("第四章　离婚")
        val four: List<String>
    )

    data class Six(
        @SerializedName("第一章　一般规定")
        val one: List<String>,
        @SerializedName("第三章　遗嘱继承和遗赠")
        val three: List<String>,
        @SerializedName("第二章　法定继承")
        val two: List<String>,
        @SerializedName("第四章　遗产的处理")
        val four: List<String>
    )

    data class Four(
        @SerializedName("第一章　一般规定")
        val one: List<String>,
        @SerializedName("第三章　姓名权和名称权")
        val three: List<String>,
        @SerializedName("第二章　生命权、身体权和健康权")
        val two: List<String>,
        @SerializedName("第五章　名誉权和荣誉权")
        val five: List<String>,
        @SerializedName("第六章　隐私权和个人信息保护")
        val six: List<String>,
        @SerializedName("第四章　肖像权")
        val four: List<String>
    )
}