package com.rodsussumu.experian.exceptions;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionDetails {
    private String error;
    private Integer code;
}
