package ru.myacademyhomework.tinkoffmessenger.data.mapper

import ru.myacademyhomework.tinkoffmessenger.data.database.model.StreamDb
import ru.myacademyhomework.tinkoffmessenger.domain.stream.StreamInfo
import javax.inject.Inject

class StreamMapper @Inject constructor() {
    fun mapDbModelToEntity(stream: StreamDb): StreamInfo{
        return StreamInfo(
            streamId = stream.streamId,
            nameChannel = stream.nameChannel,
            subscribedStatus = stream.subscribedStatus
        )
    }
}