package project.service;

import project.dto.response.PaginationResponse;
import project.dto.response.SimpleResponse;

public interface FavoriteService {
    SimpleResponse save(Long houseId);

    PaginationResponse getMy(int page,int size);
}
