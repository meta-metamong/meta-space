package com.metamong.mt.global.apispec;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class CommonListUpdateRequestDto<T, ID> {
    private List<T> create;
    private List<CommonUpdateListItemRequestDto<T, ID>> update;
    private List<ID> delete;
    
    public boolean isCreateAvailable() {
        return !this.create.isEmpty();
    }
    
    public boolean isUpdateAvailable() {
        return !this.update.isEmpty();
    }
    
    public boolean isDeleteAvailable() {
        return !this.delete.isEmpty();
    }
}
