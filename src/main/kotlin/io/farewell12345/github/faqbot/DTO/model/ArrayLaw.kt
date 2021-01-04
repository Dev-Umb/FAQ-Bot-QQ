package io.farewell12345.github.faqbot.DTO.model


class ArrayLaw : ArrayList<ArrayLaw.ArrayLawSubList>(){
    class ArrayLawSubList : ArrayList<ArrayLawSubList.ArrayLawSubListSubList>(){
        class ArrayLawSubListSubList : ArrayList<String>()
    }
}