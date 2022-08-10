package com.pavellukyanov.rocketchat.domain.usecase.profile

import android.net.Uri
import com.pavellukyanov.rocketchat.domain.repository.IHome
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ChangeAvatar : suspend (Uri) -> Flow<Boolean>

class ChangeAvatarImpl @Inject constructor(
    private val auth: IHome
) : ChangeAvatar {
    override suspend operator fun invoke(uri: Uri): Flow<Boolean> =
        auth.changeAvatar(uri)
}