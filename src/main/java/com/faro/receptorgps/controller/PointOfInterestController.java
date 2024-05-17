package com.faro.receptorgps.controller;

import com.faro.receptorgps.controller.dto.CreatePointOfInterest;
import com.faro.receptorgps.entity.PointOfInterest;
import com.faro.receptorgps.repository.PointOfInterestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PointOfInterestController {

    private final PointOfInterestRepository repository;

    public PointOfInterestController(PointOfInterestRepository repository) {
        this.repository = repository;
    }


    @PostMapping("/points-of-interest")
    public ResponseEntity<Void> createPoi(@RequestBody CreatePointOfInterest body) {
        repository.save(new PointOfInterest(body.name(),body.x(), body.y()));
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/points-of-interest")
    public ResponseEntity<Page<PointOfInterest>> listPoi(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        var body = repository.findAll(PageRequest.of(page, pageSize));

        return ResponseEntity.ok(body);
    }

    @GetMapping(value = "/near-me")
    public ResponseEntity<List<PointOfInterest>> listPoi(@RequestParam("x") Long x,
                                                         @RequestParam("y") Long y,
                                                         @RequestParam("dMax") Long dMax) {

        var xMin = x - dMax;
        var xMax = x + dMax;
        var yMin = y - dMax;
        var yMax = y + dMax;

        var body = repository.findNearMe(xMin, xMax, yMin, yMax);

        return ResponseEntity.ok(body);
    }
}

