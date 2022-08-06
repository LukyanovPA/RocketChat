package com.pavellukyanov.rocketchat.domain.usecase.profile

import android.net.Uri
import com.pavellukyanov.rocketchat.domain.repository.Auth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetMyAvatar : suspend () -> Flow<Uri>

class GetMyAvatarImpl @Inject constructor(
    private val auth: Auth
) : GetMyAvatar {
    override suspend operator fun invoke(): Flow<Uri> =
        auth.getMyAvatar()
}