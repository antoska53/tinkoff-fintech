package ru.myacademyhomework.tinkoffmessenger.data.mapper

import ru.myacademyhomework.tinkoffmessenger.data.database.model.UserDb
import ru.myacademyhomework.tinkoffmessenger.data.network.model.UserDto
import ru.myacademyhomework.tinkoffmessenger.domain.profile.UserInfo
import ru.myacademyhomework.tinkoffmessenger.domain.profile.UserStatus
import javax.inject.Inject

class UserMapper @Inject constructor() {
    fun mapDtoToDbModel(userDto: UserDto): UserDb{
        return UserDb(
            avatarURL = userDto.avatarURL,
            email = userDto.email,
            fullName = userDto.fullName,
            userID = userDto.userID,
            isOwn = userDto.isOwner
        )
    }

    fun mapDbModelToEntity(dbModel: UserDb): UserInfo{
        return UserInfo(
            avatarURL = dbModel.avatarURL,
            email = dbModel.email,
            fullName = dbModel.fullName,
            userID = dbModel.userID,
            isOwner = dbModel.isOwn
        )
    }

    fun mapDtoToEntity(userDto: UserDto): UserInfo{
        return UserInfo(
            avatarURL = userDto.avatarURL,
            email = userDto.email,
            fullName = userDto.fullName,
            userID = userDto.userID,
            isOwner = userDto.isOwner
        )
    }
}