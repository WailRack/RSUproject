package ru.ivan.rsuproject.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ru.ivan.rsuproject.dto.ScoreRequest;

@Slf4j
@Service
public class LogisticRegressionScoringService {
    private static final double BIAS = -32.0;
    private static final double B_AGE = 0.05;
    private static final double B_INCOME = 0.00005;
    private static final double B_YEARS_AT_JOB = 0.5;
    private static final double B_DEPENDENTS = -0.5;
    private static final double B_EXISTING_LOAN = -1.5;
    private static final double B_CREDIT_SCORE = 0.015;

    public double predictDefaultProbability(ScoreRequest r) {
        double z = BIAS;
        log.debug("{}", z);

        if (r.age() != null) {
            z += B_AGE * r.age();
        }
        log.info("{}", z);
        if (r.income() != null) {
            z += B_INCOME * r.income();
        }
        log.info("{}", z);
        if (r.yearsAtJob() != null) {
            z += B_YEARS_AT_JOB * r.yearsAtJob();
        }
        log.info("{}", z);
        if (r.dependents() != null) {
            z += B_DEPENDENTS * r.dependents();
        }
        log.info("{}", z);
        if (r.hasExistingLoan() != null) {
            z += B_EXISTING_LOAN * (Boolean.TRUE.equals(r.hasExistingLoan()) ? 1.0 : 0.0);
        }
        log.info("{}", z);
        if (r.creditScore() != null) {
            z += B_CREDIT_SCORE * r.creditScore();
        }
        log.info("{}", z);
        log.info("{}", sigmoid(z));

        return sigmoid(z);
    }

    private double sigmoid(double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }
}
