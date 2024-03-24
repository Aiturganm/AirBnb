package project.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.repository.RentInfoRepository;
import project.service.RentInfoService;
@Service
@RequiredArgsConstructor
@Slf4j
public class RentInfoServiceImpl implements RentInfoService {
    private final RentInfoRepository rentInfoRepository;
}
