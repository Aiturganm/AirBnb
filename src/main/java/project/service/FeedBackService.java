package project.service;

import project.dto.request.FeedBackRequest;
import project.dto.response.FeedBackResponse;
import project.dto.response.PaginationFeedBack;
import project.dto.response.SimpleResponse;

import java.security.Principal;

public interface FeedBackService {
    SimpleResponse saveFeedBack(FeedBackRequest feedBackRequest, Principal principal, Long houseId);

    SimpleResponse delete(Long feedId, Principal principal);

    FeedBackResponse getFeedBack(Long feedId);

    SimpleResponse updateFeed(FeedBackRequest feedBackRequest, Long feedId, Principal principal);

    PaginationFeedBack getAllFeedBack(Long houseId,int page,int size);

    SimpleResponse like(Long feedBackId);

    SimpleResponse disLike(Long feedBackId);

}
