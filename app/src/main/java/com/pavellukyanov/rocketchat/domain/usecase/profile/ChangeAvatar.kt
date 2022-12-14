package com.pavellukyanov.rocketchat.domain.usecase.profile

import android.net.Uri
import com.pavellukyanov.rocketchat.domain.repository.IUsers
import javax.inject.Inject

interface ChangeAvatar : suspend (Uri) -> Unit

class ChangeAvatarImpl @Inject constructor(
    private val auth: IUsers
) : ChangeAvatar {
    override suspend operator fun invoke(uri: Uri) = auth.changeAvatar(uri)
}