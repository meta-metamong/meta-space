package com.metamong.mt.global.apispec;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class CommonUpdateListItemRequestDto<T, ID> {
    private ID id;
    private T to;
}
