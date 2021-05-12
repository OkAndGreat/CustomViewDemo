package com.example.customeview

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.widget.Toast

//这里也可以使用扩展函数
//dp2px
val Float.px
    get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
    )

val Float.dp
    get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
    )

val Int.dp
    get() = this.toFloat().dp

fun String.show(context:Context,time:Int=Toast.LENGTH_SHORT){
    Toast.makeText(context,this,time).show()
}
