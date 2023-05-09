package ru.myacademyhomework.tinkoffmessenger.data.profile

import io.reactivex.Single
import ru.myacademyhomework.tinkoffmessenger.data.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.data.mapper.UserMapper
import ru.myacademyhomework.tinkoffmessenger.data.mapper.UserStatusMapper
import ru.myacademyhomework.tinkoffmessenger.di.ApiClient
import ru.myacademyhomework.tinkoffmessenger.domain.profile.ProfileRepository
import ru.myacademyhomework.tinkoffmessenger.domain.profile.UserInfo
import ru.myacademyhomework.tinkoffmessenger.domain.profile.UserStatus
import ru.myacademyhomework.tinkoffmessenger.presentation.profilefragment.ProfileFragment
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val chatDao: ChatDao,
    private val mapper: UserMapper,
    private val userStatusMapper: UserStatusMapper
): ProfileRepository {

    override fun getUserInfo(userId: Int): Single<UserInfo> {
        return if (userId == ProfileFragment.USER_OWNER){
            getOwnUser()
        }else {
            getUser(userId)
        }
    }

    private fun getUser(userId: Int): Single<UserInfo>{
        return chatDao.getUser(userId).map {userDb ->
                mapper.mapDbModelToEntity(userDb)
        }
    }

    private fun getOwnUser(): Single<UserInfo> {
        return apiClient.chatApi.getOwnUser().map {userDto ->
            mapper.mapDtoToEntity(userDto)
        }
    }

    override fun getUserStatus(userId: Int): Single<UserStatus> {
        return apiClient.chatApi.getUserPresence(userId).map{
            userStatusMapper.mapDtoToEntity(it)
        }
    }
}