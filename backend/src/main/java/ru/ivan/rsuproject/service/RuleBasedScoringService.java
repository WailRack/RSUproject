package ru.ivan.rsuproject.service;

import org.springframework.stereotype.Service;

import ru.ivan.rsuproject.dto.ScoreRequest;

@Service
public class RuleBasedScoringService {

    public double calculateScore(ScoreRequest r) {
        double score = 0.0;

        if (r.age() != null) {
            if (r.age() < 21) score -= 30;
            else if (r.age() < 25) score -= 10;
            else if (r.age() <= 60) score += 20;
            else score -= 10;
        }

        if (r.income() != null) {
            if (r.income() < 30000) score -= 20;
            else if (r.income() < 60000) score += 10;
            else score += 30;
        }

        if (r.yearsAtJob() != null) {
            if (r.yearsAtJob() < 1) score -= 15;
            else if (r.yearsAtJob() >= 3) score += 15;
        }

        if (Boolean.TRUE.equals(r.hasExistingLoan())) {
            score -= 10;
        }

        if (r.creditScore() != null) {
            if (r.creditScore() < 500) score -= 40;
            else if (r.creditScore() > 700) score += 30;
        }

        return score;
    }
}
