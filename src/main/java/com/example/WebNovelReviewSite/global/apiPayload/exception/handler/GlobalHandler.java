package com.example.WebNovelReviewSite.global.apiPayload.exception.handler;

import com.example.WebNovelReviewSite.global.apiPayload.code.BaseErrorCode;
import com.example.WebNovelReviewSite.global.apiPayload.exception.GeneralException;

public class GlobalHandler extends GeneralException {
    public GlobalHandler(BaseErrorCode code) {
        super(code);
    }
}
