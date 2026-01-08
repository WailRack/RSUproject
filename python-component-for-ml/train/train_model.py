import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.pipeline import Pipeline
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import roc_auc_score, classification_report
import joblib

df = pd.read_csv("data/credit_data.csv")

X = df.drop(columns=["default"])
y = df["default"]

X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42
)

pipeline = Pipeline([
    ("scaler", StandardScaler()),
    ("model", LogisticRegression(
        max_iter=1000,
        class_weight="balanced"
    ))
])

pipeline.fit(X_train, y_train)

y_pred = pipeline.predict_proba(X_test)[:, 1]

print("ROC AUC:", roc_auc_score(y_test, y_pred))
print(classification_report(y_test, y_pred > 0.5))

joblib.dump(pipeline, "model/credit_scoring.joblib")
print("Model trained and saved")
