package com.example.WebNovelReviewSite.global.apiPayload;

import com.example.WebNovelReviewSite.global.apiPayload.code.status.GeneralSuccessCode;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(annotations = {RestController.class})
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 모든 응답 처리 (ResponseEntity 포함)
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        // Swagger UI 경로는 제외 (Swagger 문서 정확성을 위해)
        String path = request.getURI().getPath();
        if (path != null && (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/swagger"))) {
            return body;
        }

        // 이미 ApiResponse인 경우 그대로 반환 (중복 래핑 방지)
        // 에러 응답은 ExceptionAdvice에서 이미 ApiResponse로 처리됨
        if (body instanceof ApiResponse) {
            return body;
        }

        // ApiResponse가 아닌 모든 응답을 래핑
        // 에러는 ExceptionAdvice에서 처리되므로, 여기까지 온 응답은 성공 응답
        // ResponseEntity.ok() 등으로 반환된 경우 body만 전달됨
        
        if (body == null) {
            return ApiResponse.onSuccess(GeneralSuccessCode._OK, null);
        }
        
        return ApiResponse.onSuccess(GeneralSuccessCode._OK, body);
    }
}

