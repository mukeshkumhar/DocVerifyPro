package com.example.docverifypro

data class User(
    val _id: String?,
    val fullName: String?,
    val userName: String?,
    val email: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val __v: Int?
)

data class RegisterUser(

    val fullName: String,
    val userName: String,
    val email: String,
    val password: String
)
data class Tokens(
    val accessToken: String,
    val refreshToken: String
)

data class RegisterResponse(
    val tokens: Tokens,
    val user: User,
    val message: String,
    val success: Boolean,
)

data class LoginUser(
    val email: String,
    val password: String
)

data class UserRes(
    val user: User,
    val accessToken: String?,
    val refreshToken: String?,
)

data class LoginResponse(
    val statusCode: Int,
    val data: UserRes,
    val message: String,
    val success: Boolean,
)



