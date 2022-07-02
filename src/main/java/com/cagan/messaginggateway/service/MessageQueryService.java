package com.cagan.messaginggateway.service;

import com.cagan.messaginggateway.domain.MessageDeliveryRequestLog;
import com.cagan.messaginggateway.domain.User;
import com.cagan.messaginggateway.repository.MessageDeliveryRequestLogRepository;
import com.cagan.messaginggateway.rest.dto.request.MessageQueryRequest;
import com.cagan.messaginggateway.rest.dto.response.QueryResponse;
import com.cagan.messaginggateway.rest.error.SearchQueryException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class MessageQueryService {
    private final MessageDeliveryRequestLogRepository messageDeliveryRequestRepository;
    private final MongoTemplate mongoTemplate;

    public QueryResponse searchMessageRequests(MessageQueryRequest request, User user) {
        Query query = new Query();
         var queryResponseBuilder = QueryResponse.builder();

        if (request.getStartTime() != null && request.getEndTime() != null) {
            query.addCriteria(Criteria.where("createDate").gt(request.getStartTime()).lt(request.getEndTime()));
            queryResponseBuilder.startTime(request.getStartTime());
            queryResponseBuilder.endTime(request.getEndTime());
        }

        if (request.isSuccess() && request.isFailed()) {
            throw new SearchQueryException("Can't search success and failed fields at the same time");
        }

        if (request.isSuccess()) {
            query.addCriteria(Criteria.where("status").is("SUCCESS"));
            queryResponseBuilder.success(true);
        }

        if (request.isFailed()) {
            query.addCriteria(Criteria.where("status").is("FAILED"));
            queryResponseBuilder.failed(true);
        }

        query.addCriteria(Criteria.where("user.$id").is(new ObjectId(user.getId())));

        log.info("QUERY: {}", query);
        long count = mongoTemplate.count(query, MessageDeliveryRequestLog.class);
        queryResponseBuilder.count(count);

        return queryResponseBuilder.build();
    }


    public Long findNumberOfRequestsWithinTimeRange(User user, LocalDateTime startTime, LocalDateTime endTime) {
        return messageDeliveryRequestRepository.countByCreateDateBetweenAndUser(startTime, endTime, user);
    }
}
