package com.academy.mobile.service.rest.xml;

import com.academy.mobile.model.Subscriber;
import com.academy.mobile.model.SubscriberNotFoundException;
import com.academy.mobile.model.Subscribers;
import com.academy.mobile.service.db.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/rest/xml/subscribers")
public class SubscriberServiceXML {

    @Autowired
    private SubscriberService subscriberService;

    @GetMapping(produces = { MediaType.APPLICATION_XML_VALUE })
    public Subscribers getSubscribers() {
        return new Subscribers(subscriberService.findAll());
    }

    @GetMapping(path="/{id}", produces = { MediaType.APPLICATION_XML_VALUE })
    public Subscriber getSubscriber(@PathVariable long id) throws SubscriberNotFoundException {
        Subscriber subscriber = subscriberService.getById(id);
        if (subscriber != null)
            return subscriber;

        throw new SubscriberNotFoundException();
    }

    @PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> addSubscriber(@RequestBody Subscriber subscriber) {
        try {
            long id = subscriber.getId() == 0L ? subscriberService.save(subscriber).getId() :
                    subscriberService.saveById(subscriber.getId(), subscriber).getId();

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(id).toUri();
            return ResponseEntity.created(location).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path= "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE })
    public Subscriber updateSubscriber(@PathVariable("id") long id,  @RequestBody  Subscriber subscriber) {
        return subscriberService.saveById(id, subscriber);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubscriber(@PathVariable("id") long id) {
        try {
            subscriberService.remove(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllSubscribers() {
        try {
            System.out.println("delete all");
            subscriberService.findAll().stream()
                    .mapToLong(Subscriber::getId)
                    .forEach(subscriberService::remove);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
