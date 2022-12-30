package com.example.customdesign.data

//You can use Medicine
//don't use var in data class if you don't want to update it in future.
data class Medicine(
    val name: String,
    val price: Int,
    var count: Int,


    var isSelected: Boolean = false
)
