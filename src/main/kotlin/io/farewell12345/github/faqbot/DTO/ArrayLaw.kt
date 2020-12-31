package io.farewell12345.github.faqbot.DTO


import com.google.gson.annotations.SerializedName

class ArrayLaw : ArrayList<ArrayLaw.ArrayLawSubList>(){
    class ArrayLawSubList : ArrayList<ArrayLawSubList.ArrayLawSubListSubList>(){
        class ArrayLawSubListSubList : ArrayList<String>()
    }
}