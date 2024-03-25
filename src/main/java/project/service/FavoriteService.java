package project.service;

import project.dto.response.SimpleResponse;

public interface FavoriteService {
    SimpleResponse save(Long houseId);
}
