package culinaryapi_Menu_Service.publishers;


import culinaryapi_Menu_Service.dtos.MenuEventDto;
import culinaryapi_Menu_Service.enums.ActionType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MenuEventPublisher {


    private final RabbitTemplate rabbitTemplate;

    public MenuEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @Value(value="${Culinary.broker.exchange.menuEventExchange}" )
    private String exchangeMenuEvent;

    public void publishMenuEvent(MenuEventDto menuEventDto, ActionType actionType){
        menuEventDto.setActionType(actionType.toString());
        rabbitTemplate.convertAndSend(exchangeMenuEvent,"menu.service.event",menuEventDto);
    }
}