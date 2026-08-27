package recommendation_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecommendationController {

    @GetMapping("/api/recommendations")
    public String recommendations() {
        return "Recommendation Service is working!";
    }
}