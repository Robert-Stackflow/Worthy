package com.cloudchewie.ui

/**
 *AUTHOR:AbnerMing
 *DATE:2022/10/27
 *INTRODUCE:监听是文字改变还是搜索点击
 */
interface OnTextSearchListener {
    fun textChanged(content: String)
    fun clickSearch(content: String)
}