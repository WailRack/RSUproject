package ru.ivan.decision.service;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OnnxValue;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ivan.decision.dto.ScoringRequest;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.Map;
import java.util.List;

@Slf4j
@Service
public class OnnxScoringService {

    private OrtEnvironment env;
    private OrtSession session;
    private String inputName;
    private String probabilityOutputName;

    @PostConstruct
    public void init() throws IOException, OrtException {
        this.env = OrtEnvironment.getEnvironment();
        var modelStream = getClass().getResourceAsStream("/credit_scoring.onnx");
        if (modelStream == null) {
            throw new IOException("Model file not found in resources");
        }
        byte[] modelBytes = modelStream.readAllBytes();
        this.session = env.createSession(modelBytes, new OrtSession.SessionOptions());
        this.inputName = session.getInputNames().iterator().next();

        for (String name : session.getOutputNames()) {
            if (name.toLowerCase().contains("prob")) {
                this.probabilityOutputName = name;
            }
        }
        if (this.probabilityOutputName == null && session.getOutputNames().size() > 1) {
            var it = session.getOutputNames().iterator();
            it.next();
            this.probabilityOutputName = it.next();
        }

        log.info("ONNX Model loaded. Input: {}, Prob Output: {}, All Outputs: {}",
            inputName, probabilityOutputName, session.getOutputNames());
    }

    public double predictProbability(ScoringRequest request) {
        try {
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
                if (probabilityOutputName == null) {
                    log.warn("Probability output name not found, returning 0.0");
                    return 0.0;
                }

                OnnxValue probValue = results.get(probabilityOutputName).get();
                Object value = probValue.getValue();
                log.debug("Probability output actual value class: {}", value.getClass().getName());

                if (value instanceof List) {
                    List<?> list = (List<?>) value;
                    if (!list.isEmpty()) {
                        Object firstElem = list.get(0);
                        if (firstElem instanceof Map) {
                            Map<?, ?> map = (Map<?, ?>) firstElem;
                            log.info("Probabilities Map found: {}", map);
                            Object p = map.get(1L);
                            if (p == null) p = map.get(1);
                            if (p instanceof Float) return ((Float) p).doubleValue();
                        } else {
                            log.warn("List element is not a Map: {}", firstElem.getClass().getName());
                        }
                    }
                }
                if (value instanceof float[][]) {
                    float[][] array = (float[][]) value;
                    if (array.length > 0 && array[0].length > 1) {
                        log.info("Probabilities Array found, taking [0][1]: {}", array[0][1]);
                        return array[0][1];
                    }
                }

                log.warn("Could not parse probability from output: {}", value);
                return 0.0;
            }

        } catch (OrtException e) {
            log.error("Scoring inference failed", e);
            throw new RuntimeException("ML Inference failed", e);
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
