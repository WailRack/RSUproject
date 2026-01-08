import onnxruntime as ort
import numpy as np

session = ort.InferenceSession("model/credit_scoring.onnx")

sample = np.array([[
    30,       # age
    60000,    # income
    4,        # years_at_job
    1,        # dependents
    0,        # has_existing_loan
    700       # credit_score
]], dtype=np.float32)

inputs = {"input": sample}
outputs = session.run(None, inputs)

print("Default probability:", outputs[1][0][1])
