package com.maids.librarysystem.dto.response;

import com.maids.librarysystem.enums.ErrorType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ErrorResponse {
	private boolean status;
	private int code;
	private String message;
	private ErrorType type;
	private List<String> errors;
}
