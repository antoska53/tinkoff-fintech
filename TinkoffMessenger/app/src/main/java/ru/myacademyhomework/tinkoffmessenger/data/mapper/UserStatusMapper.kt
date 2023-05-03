package ru.myacademyhomework.tinkoffmessenger.data.mapper

import ru.myacademyhomework.tinkoffmessenger.data.network.model.PresenceResponseDto
import ru.myacademyhomework.tinkoffmessenger.domain.profile.UserStatus
import javax.inject.Inject

class UserStatusMapper @Inject constructor(){
    fun mapDtoToEntity(presenceResponseDto: PresenceResponseDto): UserStatus{
        return UserStatus(
            status = presenceResponseDto.presence.userStatus.status,
            timestamp = presenceResponseDto.presence.userStatus.timestamp
        )
    }
}