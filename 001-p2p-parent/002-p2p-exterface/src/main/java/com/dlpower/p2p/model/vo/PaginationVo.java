package com.dlpower.p2p.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页模板
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationVo<T> implements Serializable {

    private List<T> list;  // 数据

    private Long total;  // 总条数



}
