package com.academy.mobile.service.soap;

import com.academy.mobile.model.Subscriber;
import com.academy.mobile.service.db.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@CrossOrigin(origins = "*")
public class SubscriberWSImpl  {

    private static final String NAMESPACE_URI = "http://soap.service.mobile.academy.com";

    @Autowired
    private SubscriberService subscriberService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getSubscriberRequest")
    @ResponsePayload
    public GetSubscriberResponse getSubscriber(@RequestPayload GetSubscriberRequest request) {
        System.out.println(String.format(">> get subscriber by id: %d ", request.getId()));

        GetSubscriberResponse response = new GetSubscriberResponse();
        response.setSubscriber(convertFrom(subscriberService.getById(request.getId())));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getSubscribersRequest")
    @ResponsePayload
    public GetSubscribersResponse getAllSubscribers() {
        System.out.println(">> get subscribers ");

        GetSubscribersResponse response =  new GetSubscribersResponse();
        subscriberService.findAll().stream()
                .map(this::convertFrom)
                .forEach(response.getSubscribers()::add);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addSubscriberRequest")
    @ResponsePayload
    public AddSubscriberResponse addSubscriber(@RequestPayload AddSubscriberRequest request) {
        System.out.println(String.format(">> add subscriber, id= %d ", request.getSubscriber().getId()));
        long idResult = request.getSubscriber().getId();

        if (idResult == 0)
            idResult = subscriberService.save(convertFrom(request.getSubscriber())).getId();
        else
            subscriberService.saveById(request.getSubscriber().getId(), convertFrom(request.getSubscriber()));

        AddSubscriberResponse response = new AddSubscriberResponse();
        response.setId(idResult);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "removeSubscriberRequest")
    @ResponsePayload
    public RemoveSubscriberResponse removeSubscriber(@RequestPayload RemoveSubscriberRequest request) {
        System.out.println(String.format(">> remove subscriber, id= %d ", request.getId()));
        subscriberService.remove(request.getId());
        return new RemoveSubscriberResponse();
    }

    // TODO
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateSubscriberRequest")
    @ResponsePayload
    public UpdateSubscriberResponse updateSubscriber(@RequestPayload UpdateSubscriberRequest request) {
        System.out.println(String.format(">> update subscriber: id= %d", request.getId()));
        UpdateSubscriberResponse response = new UpdateSubscriberResponse();
        if (request.getSubscriber() == null || request.getId() != request.getSubscriber().getId())
            return response;

        subscriberService.saveById(request.getId(), convertFrom(request.getSubscriber()));
        response.setSubscriber(request.getSubscriber());

        return response;
    }

    private com.academy.mobile.service.soap.Subscriber convertFrom(Subscriber subscriber) {
        if (subscriber == null)
            return null;

        com.academy.mobile.service.soap.Subscriber converted = new com.academy.mobile.service.soap.Subscriber();

        converted.setId(subscriber.getId());
        converted.setFirstName(subscriber.getFirstName());
        converted.setLastName(subscriber.getLastName());
        converted.setAge(subscriber.getAge());
        converted.setGender(Gender.fromValue( Character.toString(subscriber.getGender().getEng())));

        return converted;
    }

    private Subscriber convertFrom(com.academy.mobile.service.soap.Subscriber subscriber) {
        if (subscriber == null)
            return null;

        Subscriber converted = new Subscriber();

        converted.setId(subscriber.getId());
        converted.setFirstName(subscriber.getFirstName());
        converted.setLastName(subscriber.getLastName());
        converted.setAge(subscriber.getAge());
        converted.setGender(com.academy.mobile.model.Gender.valueOf(subscriber.getGender().toString().toLowerCase().charAt(0)));

        return converted;
    }
}
