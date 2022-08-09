package ink.umb.faqbot.dto.model.dataclass


class ArrayLaw : ArrayList<ArrayLaw.ArrayLawSubList>(){
    class ArrayLawSubList : ArrayList<ArrayLawSubList.ArrayLawSubListSubList>(){
        class ArrayLawSubListSubList : ArrayList<String>()
    }
}