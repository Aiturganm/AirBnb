package project.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dto.request.card.CardReq;
import project.dto.response.CardRes;
import project.dto.response.SimpleResponse;
import project.entities.Card;
import project.entities.User;
import project.exception.NotFoundException;
import project.repository.CardRepository;
import project.repository.UserRepository;
import project.service.CardService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SimpleResponse save(CardReq cardReq) {
        Card exists = cardRepository.findByNumber(cardReq.getCardNumber());
        if (exists != null)
            return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("This card number already exists!").build();
        User user = getCurrentUser();
        Card card = cardReq.convert();
        cardRepository.save(card);
        card.setUser(user);
        user.setCard(card);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).
                message("Success saved card with card number:     " + card.getCarNumber()).build();
    }

    @Override
    public CardRes getMyCard() {
        return getCurrentUser().getCard().convert();
    }

    @Override
    public SimpleResponse update(BigDecimal money) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Card currentUserCurd = cardRepository.findByUserEmail(currentUserEmail);
        currentUserCurd.setMoney(money);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success added money: " + money).build();
    }

    @Override
    public SimpleResponse delete() {
        User currentUser = getCurrentUser();
        cardRepository.delete(currentUser.getCard());
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success deleted your card!").build();
    }

    private User getCurrentUser() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new NotFoundException("Your token or email invalid!"));
    }
}
