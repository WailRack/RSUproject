package ru.ivan.decision.service;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ivan.decision.dto.ScoringRequest;
import ru.ivan.decision.dto.ScoringResponse;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
public class OnnxScoringService {

    private OrtEnvironment env;
    private OrtSession session;
    private String inputName;

    @PostConstruct
    public void init() throws IOException, OrtException {
        this.env = OrtEnvironment.getEnvironment();
        // Load model from resources
        var modelStream = getClass().getResourceAsStream("/credit_scoring.onnx");
        if (modelStream == null) {
            throw new IOException("Model file not found in resources");
        }
        byte[] modelBytes = modelStream.readAllBytes();
        this.session = env.createSession(modelBytes, new OrtSession.SessionOptions());
        
        // Detect input name
        this.inputName = session.getInputNames().iterator().next();
        log.info("ONNX Model loaded. Input name: {}", inputName);
    }

    public ScoringResponse score(ScoringRequest request) {
        try {
            // Prepare input tensor [1, 6]
            // Order: age, income, years_at_job, dependents, has_existing_loan, credit_score
            float[] data = new float[6];
            data[0] = request.age() != null ? request.age().floatValue() : 0.0f;
            data[1] = request.income() != null ? request.income().floatValue() : 0.0f;
            data[2] = request.yearsAtJob() != null ? request.yearsAtJob().floatValue() : 0.0f;
            data[3] = request.dependents() != null ? request.dependents().floatValue() : 0.0f;
            data[4] = (request.hasExistingLoan() != null && request.hasExistingLoan()) ? 1.0f : 0.0f;
            data[5] = request.creditScore() != null ? request.creditScore().floatValue() : 0.0f;

            FloatBuffer buffer = FloatBuffer.wrap(data);
            long[] shape = {1, 6};
            
            OnnxTensor tensor = OnnxTensor.createTensor(env, buffer, shape);
            
            try (var results = session.run(Collections.singletonMap(inputName, tensor))) {
                // Assuming Output 0 is label (long) and Output 1 is probabilities (map or list)
                // Or scikit-learn style: "label", "probabilities"
                
                // Let's inspect outputs dynamically or assume standard sklearn-onnx output
                // Usually: output (int64), output_probability (seq<map<int64, float>>)
                
                var resultIterator = results.iterator();
                var labelResult = resultIterator.next(); // 0: label
                var probResult = resultIterator.hasNext() ? resultIterator.next() : null; // 1: probabilities

                long predictedLabel = ((long[]) labelResult.getValue().getValue())[0];
                double probability = 0.0;

                if (probResult != null) {
                    // Extract probability for class 1 (default)
                    // Structure depends on export. Often List<Map<Long, Float>>
                    var value = probResult.getValue().getValue();
                    log.debug("Probability output type: {}", value.getClass());
                    
                    if (value instanceof java.util.List) {
                        var list = (java.util.List<?>) value;
                        if (!list.isEmpty() && list.get(0) instanceof Map) {
                            var map = (Map<?, ?>) list.get(0);
                            log.info("Probabilities map content: {}", map);
                            
                            // Assuming 1L is 'default' (bad), 0L is good.
                            // Try Long first, then Integer
                            Object probObj = map.get(1L); 
                            if (probObj == null) {
                                probObj = map.get(1);
                            }
                            
                            if (probObj instanceof Float) {
                                probability = ((Float) probObj).doubleValue();
                            } else {
                                log.warn("Probability value for key 1 is not Float or not found: {}", probObj);
                            }
                        }
                    }
                }

                boolean approved = predictedLabel == 0; // Assuming 0 = No Default, 1 = Default
                String reason = approved ? "Low risk" : "High risk of default";

                return new ScoringResponse(approved, probability, reason);
            }

        } catch (OrtException e) {
            log.error("Scoring failed", e);
            throw new RuntimeException("Scoring computation failed", e);
        }
    }

    @PreDestroy
    public void close() {
        try {
            if (session != null) session.close();
            if (env != null) env.close();
        } catch (OrtException e) {
            log.warn("Error closing ONNX resources", e);
        }
    }
}
