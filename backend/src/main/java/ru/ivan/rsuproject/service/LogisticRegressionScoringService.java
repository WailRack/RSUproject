package ru.ivan.rsuproject.service;

import org.springframework.stereotype.Service;

import ru.ivan.rsuproject.dto.ScoreRequest;

@Service
public class LogisticRegressionScoringService {
    private static final double BIAS = -33.96665684641257;
    private static final double B_AGE = 0.16873913094417098;
    private static final double B_INCOME = 0.00017358785572127173;
    private static final double B_YEARS_AT_JOB = 1.407236822348883;
    private static final double B_DEPENDENTS = -1.2759260447846899;
    private static final double B_EXISTING_LOAN = -4.559601911359217;
    private static final double B_CREDIT_SCORE = 0.055854701990360224;

    public double predictDefaultProbability(ScoreRequest r) {
        double z = BIAS;

        if (r.age() != null) {
            z += B_AGE * r.age();
        }
        if (r.income() != null) {
            z += B_INCOME * r.income();
        }
        if (r.yearsAtJob() != null) {
            z += B_YEARS_AT_JOB * r.yearsAtJob();
        }
        if (r.dependents() != null) {
            z += B_DEPENDENTS * r.dependents();
        }
        if (r.hasExistingLoan() != null) {
            z += B_EXISTING_LOAN * (r.hasExistingLoan() ? 1.0 : 0.0);
        }
        if (r.creditScore() != null) {
            z += B_CREDIT_SCORE * r.creditScore();
        }

        return sigmoid(z);
    }

    private double sigmoid(double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }
}
