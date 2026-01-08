package ru.ivan.decision;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ivan.decision.dto.ScoringRequest;
import ru.ivan.decision.dto.ScoringResponse;
import ru.ivan.decision.service.OnnxScoringService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DecisionEngineApplicationTests {

    @Autowired
    private OnnxScoringService scoringService;

    @Test
    void contextLoadsAndScoringWorks() {
        // Arrange
        ScoringRequest request = new ScoringRequest(
            30,         // age
            50000.0,    // income
            5,          // yearsAtJob
            2,          // dependents
            false,      // hasExistingLoan
            750         // creditScore
        );

        // Act
        ScoringResponse response = scoringService.score(request);

        // Assert
        assertNotNull(response);
        System.out.println("Scoring Response: " + response);
    }
}
