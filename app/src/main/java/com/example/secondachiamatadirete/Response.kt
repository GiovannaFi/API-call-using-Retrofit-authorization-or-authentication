package com.example.secondachiamatadirete

data class Response(
    val cases: Cases,
    val country: String,
    val day: String,
    val deaths: Deaths,
    val tests: Tests,
    val time: String
)