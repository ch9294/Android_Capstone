package ch9294.kr.ac.kmu.beaconsystem

import android.view.View
import android.widget.EditText

/*
에디트 텍스트에 데이터가 입력이 되어 있는지 확인한다.
이 메소드는 데이터의 유무를 검사하는 위한 것
입력한 데이터가 제대로 된 값인지는 검사하지 않는다.
 */
fun List<View>.checkNotNull(): Boolean {
    for (v in this) {
        v as EditText
        if (v.text.equals("")) false else continue
    }
    return true
}