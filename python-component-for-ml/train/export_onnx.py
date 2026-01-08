import joblib
import pandas as pd
from skl2onnx import convert_sklearn
from skl2onnx.common.data_types import FloatTensorType

pipeline = joblib.load("model/credit_scoring.joblib")

initial_type = [
    ("float_input", FloatTensorType([None, 6]))
]

onnx_model = convert_sklearn(
    pipeline,
    initial_types=initial_type,
    target_opset=15
)

with open("model/credit_scoring.onnx", "wb") as f:
    f.write(onnx_model.SerializeToString())

print("ONNX model exported")