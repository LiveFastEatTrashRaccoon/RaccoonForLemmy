package com.github.diegoberaldin.raccoonforlemmy.core_api.service

import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.LoginForm
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.LoginResponse
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST

interface AuthService {
    @POST("user/login")
    suspend fun login(@Body form: LoginForm): Response<LoginResponse>
}