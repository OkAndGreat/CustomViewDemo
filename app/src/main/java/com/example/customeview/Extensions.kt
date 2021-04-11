package com.example.customeview

import android.content.res.Resources
import android.util.TypedValue

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