package io.farewell12345.github.faqbot.BotManager

object DrawManager{
    private var groupList = mutableMapOf<Long, ArrayList<Int>>()
    fun createDraw(group: Long,star:Int,end:Int):Boolean{
        val tempIntList = ArrayList<Int>()
        if (groupList[group] == null||groupList[group]!!.isEmpty()) {
            for (i in star..end) {
                tempIntList.add(i)
            }
            groupList[group] = tempIntList
            return true
        }
        return false
    }
    fun deleteDraw(group: Long):Boolean{
        groupList.remove(group)
        return true
    }
    fun getInt(group: Long):Int{
        if (groupList[group] == null || groupList[group]!!.isEmpty())
            return -1
        val x = (groupList[group]!!.indices).random()
        val num = groupList[group]?.get(x)!!
        groupList[group]!!.minusAssign(num);
        return num
    }
}