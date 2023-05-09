package ru.myacademyhomework.tinkoffmessenger.data.mapper

import ru.myacademyhomework.tinkoffmessenger.data.database.model.TopicDb
import ru.myacademyhomework.tinkoffmessenger.data.network.model.StreamDto
import ru.myacademyhomework.tinkoffmessenger.data.network.model.SubscriptionDto
import ru.myacademyhomework.tinkoffmessenger.data.network.model.TopicDto
import ru.myacademyhomework.tinkoffmessenger.domain.pager.Topic
import javax.inject.Inject

class TopicMapper @Inject constructor() {
    fun mapDbModelToEntity(topicDb: TopicDb): Topic {
        return Topic(topicDb.streamId, topicDb.nameTopic, topicDb.nameStream)
    }

    fun mapDtoToDbModel(topic: TopicDto, subscriptionDto: SubscriptionDto): TopicDb{
        return TopicDb(topic.name, subscriptionDto.name, subscriptionDto.streamID)
    }

    fun mapDtoToDbModel(topic: TopicDto, streamDto: StreamDto): TopicDb{
        return TopicDb(topic.name, streamDto.name, streamDto.streamID)
    }
}