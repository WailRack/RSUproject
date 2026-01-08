import numpy as np
import pandas as pd

np.random.seed(42)

N = 50_000

age = np.random.randint(18, 70, N)
income = np.random.normal(70_000, 25_000, N).clip(10_000)
years_at_job = np.random.exponential(5, N).clip(0, 40)
dependents = np.random.randint(0, 5, N)
has_loan = np.random.randint(0, 2, N)
credit_score = np.random.randint(300, 900, N)

risk = (
    (age < 23) * 0.15 +
    (income < 40_000) * 0.25 +
    (years_at_job < 1) * 0.2 +
    (dependents >= 3) * 0.15 +
    (has_loan == 1) * 0.1 +
    (credit_score < 550) * 0.4
)

prob_default = np.clip(risk + np.random.normal(0, 0.05, N), 0, 1)
default = (prob_default > 0.5).astype(int)

df = pd.DataFrame({
    "age": age,
    "income": income,
    "yearsAtJob": years_at_job,
    "dependents": dependents,
    "hasExistingLoan": has_loan,
    "creditScore": credit_score,
    "default": default
})

df.to_csv("data/credit_data.csv", index=False)
print("Dataset generated:", df.shape)