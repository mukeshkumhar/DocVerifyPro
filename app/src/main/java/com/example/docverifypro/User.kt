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

// Resume Created Data Class

data class ResumeCreate(
    val name: String,
    val contact: String,
    val email: String,
    val address: String,
    val percentage: Number,
)

data class ResumeResponse(
    val statusCode: Int,
    val data: resumeDataResponse,
    val message: String,
    val success: Boolean
)

data class resumeDataResponse(
    val _id: String,
    val name: String,
    val contact: String,
    val email: String,
    val address: String,
    val percentage: Number,
    val ownerId: String,
    val projects: List<projectCreate>,
    val __v: Int,
)

data class projectCreate(
    val projectName: String,
    val projectSummary: String,
    val _id: String,
)

// Resume Add Project

data class ProjectCreated (
    val projectName: String,
    val projectSummary: String,
    val resumeId: String,
)

data class ProjectResponse(
    val statusCode: Int,
    val data: ProjectDataResponse,
    val message: String,
    val success: Boolean
)

data class ProjectDataResponse(
    val _id: String,
    val name: String,
    val contact: String,
    val email: String,
    val address: String,
    val percentage: Number,
    val ownerId: String,
    val projects: List<ProjectSave>,
    val __v: Int,

)
data class ProjectSave(
    val projectName: String,
    val projectSummary: String,
    val _id: String,
)





