package project.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dto.request.AcceptOrRejectReq;
import project.dto.response.SimpleResponse;
import project.entities.*;
import project.enums.ActionForHouse;
import project.enums.BlockOrUnBlock;
import project.enums.Role;
import project.exception.NotFoundException;
import project.repository.*;
import project.service.HouseService;
import project.service.UserService;

import java.security.Principal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    private final UserService userService;
    private final HouseService houseService;
    private final RentInfoRepository rentInfoRepository;
    private final FeedBackRepository feedBackRepository;
    private final FavoriteRepository favoriteRepository;
    private final HouseRepository houseRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final AddressRepository addressRepository;

    @Transactional
    public SimpleResponse acceptOrReject(Long houseId, ActionForHouse actionForHouse, Principal principal, AcceptOrRejectReq acceptOrRejectReq) {
        House house = houseRepository.findById(houseId).orElseThrow(() -> new NotFoundException("This house not found!  " + houseId));
        if (actionForHouse.equals(ActionForHouse.ACCEPT)) {
            User user = house.getUser();
            if (user.getRole().equals(Role.USER)) {
                user.setRole(Role.VENDOR);
            }
            house.setPublished(true);
            log.info("HOUSE SUCCESS ACCEPTED!" + houseId);
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success accepted this house!  " + house.getId()).build();
        } else if (actionForHouse.equals(ActionForHouse.REJECT)) {
            house.setPublished(false);
            house.setReason(acceptOrRejectReq.getReason());
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success reject this house:  " + house.getId()).build();
        }
        house.getFavorites().forEach(favorite -> favorite.getHouses().remove(house));
        log.info("ONE SUCCESS");
        Address byHouseId = addressRepository.findByHouseId(house.getId());
        if (byHouseId != null) {
            addressRepository.delete(byHouseId);
        }
        feedBackRepository.deleteAll(house.getFeedbacks());
        if (!house.getRentInfos().isEmpty()) {
            RentInfo lastRentInfo = house.getRentInfos().getLast();
            if (lastRentInfo != null && lastRentInfo.getCheckOut().isAfter(LocalDate.now())) {
                String clientEmail = lastRentInfo.getUser().getEmail();
                Card clientCard = cardRepository.findByUserEmail(clientEmail);
                String vendorEmail = house.getUser().getEmail();
                Card vendorCard = cardRepository.findByUserEmail(vendorEmail);
                vendorCard.setMoney(vendorCard.getMoney().subtract(lastRentInfo.getTotalPrice()));
                clientCard.setMoney(clientCard.getMoney().add(lastRentInfo.getTotalPrice()));
            }
        }
        rentInfoRepository.deleteAll(house.getRentInfos());
        houseRepository.delete(house);
        log.info("TWO SUCCESS");
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success deleted this houses").build();

    }

    @Transactional
    public SimpleResponse blockUnBlock(Long houseId, BlockOrUnBlock blockOrUnBlock) {
        House house = houseRepository.findById(houseId).orElseThrow(() -> new NotFoundException("This house not found!   " + houseId));
        if (blockOrUnBlock.equals(BlockOrUnBlock.BLOCK)) {
            if (!house.getRentInfos().isEmpty()) {
                RentInfo lastRentInfo = house.getRentInfos().getLast();
                if (lastRentInfo != null && lastRentInfo.getCheckOut().isAfter(LocalDate.now())) {
                    String clientEmail = lastRentInfo.getUser().getEmail();
                    Card clientCard = cardRepository.findByUserEmail(clientEmail);
                    String vendorEmail = house.getUser().getEmail();
                    Card vendorCard = cardRepository.findByUserEmail(vendorEmail);
                    vendorCard.setMoney(vendorCard.getMoney().subtract(lastRentInfo.getTotalPrice()));
                    clientCard.setMoney(clientCard.getMoney().add(lastRentInfo.getTotalPrice()));
                }

            }
            house.setBlock(true);
            house.setPublished(false);
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("House success blocked!" + houseId).build();
        }
        house.setBlock(false);
        house.setPublished(true);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("House success  un blocked!   " + houseId).build();
    }

    @Transactional
    public SimpleResponse userBlockOrUnBlock(Long userId, BlockOrUnBlock blockOrUnBlock) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("This user not found!   " + userId));
        if (blockOrUnBlock.equals(BlockOrUnBlock.BLOCK)) {
            user.setBlock(true);
            for (int i = 0; i < user.getHouses().size(); i++) {
                House house = houseRepository.findById(user.getHouses().get(i).getId()).get();
                if (!house.getRentInfos().isEmpty()) {
                    RentInfo lastRentInfo = house.getRentInfos().getLast();
                    if (lastRentInfo != null && lastRentInfo.getCheckOut().isAfter(LocalDate.now())) {
                        String clientEmail = lastRentInfo.getUser().getEmail();
                        Card clientCard = cardRepository.findByUserEmail(clientEmail);
                        String vendorEmail = house.getUser().getEmail();
                        Card vendorCard = cardRepository.findByUserEmail(vendorEmail);
                        vendorCard.setMoney(vendorCard.getMoney().subtract(lastRentInfo.getTotalPrice()));
                        clientCard.setMoney(clientCard.getMoney().add(lastRentInfo.getTotalPrice()));
                    }

                }
                log.info("BLOCK CHECK HOUSE");
                house.setBlock(true);
                house.setPublished(false);
                houseRepository.blockAllHousesUser(userId);
            }
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success blocked user and all houses!  " + userId).build();
        }
        user.setBlock(false);
        houseRepository.unBlockAllHousesUser(userId);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success un blocked user and all houses!  " + userId).build();
    }
}
