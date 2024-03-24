package project.service;

import project.dto.request.card.CardReq;
import project.dto.response.CardRes;
import project.dto.response.SimpleResponse;

import java.math.BigDecimal;

public interface CardService {
    SimpleResponse save(CardReq cardReq);

    CardRes getMyCard();

    SimpleResponse update(BigDecimal money);

    SimpleResponse delete();

}



