package com.faro.receptorgps.service;

import com.faro.receptorgps.entity.PointOfInterest;
import com.faro.receptorgps.repository.PointOfInterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointOfInterestService {


    private final PointOfInterestRepository repository;

    @Autowired
    public PointOfInterestService(PointOfInterestRepository repository) {
        this.repository = repository;
    }

    public void createPoi(String name, Long x, Long y) {
        repository.save(new PointOfInterest(name, x, y));
    }

    public void listPoi(Integer page, Integer pageSize) {
        repository.findAll(PageRequest.of(page, pageSize));
    }

    public List<PointOfInterest> listPoi(Long x, Long y, Long dMax) {
        var xMin = x - dMax;
        var xMax = x + dMax;
        var yMin = y - dMax;
        var yMax = y + dMax;

        return repository.findNearMe(xMin, xMax, yMin, yMax)
                .stream()
                .filter(p -> distanceBetweenPoints(x, y, p.getX(), p.getY()) <= dMax)
                .toList();
    }

    private Double distanceBetweenPoints(Long x1, Long y1, Long x2, Long y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
